package obvious.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;

/**
 * An obvious network implementation using a database with a JDCB link
 * in order to store data.
 * @author Hemery
 *
 */
public class JDBCObviousNetwork implements Network {

  /**
   * Node counter.
   */
  private static int nodeCount = 0;

  /**
   * Edge counter.
   */
  private static int edgeCount = 0;

  /**
   * JDBC connection.
   */
  private Connection con;

  /**
   * Name of the node table.
   */
  private String nodeTable;

  /**
   * Name of the edge table.
   */
  private String edgeTable;

  /**
   * Column containing node primary key value.
   */
  private String nodeKey;

  /**
   * Column containing edge primary key value.
   */
  private String edgeKey;

  /**
   * Map linking node primary key value (in JDBC) to nodeId in obvious.
   */
  private Map<Object, Integer> nodeKeyToNodeId;

  /**
   * Map linking edge primary key value (in JDBC) to edgeId in obvious.
   */
  private Map<Object, Integer> edgeKeyToEdgeId;

  /**
   * Describe the network structure.
   */
  private Map<Integer, Integer[]> networkStruct;

  /**
   * Column describing source node in edge table.
   */
  private String sourceCol;

  /**
   * Column describing target node in edge table.
   */
  private String targetCol;

  /**
   * Node schema.
   */
  private Schema nodeSchema;

  /**
   * Edge schema.
   */
  private Schema edgeSchema;

  /**
   * Constructor.
   * @param con JDBC connection
   * @param nodeTable the JDBC node table
   * @param edgeTable the JDBC edge table
   * @param nodeId primary key column in node table
   * @param edgeId primary key column in edge table
   * @param source source node column in edge table
   * @param target target node column in node table
   * @param nodeSchema obvious schema for node
   * @param edgeSchema obvious schema for edge
   * @throws SQLException  if something bad happens
   */
  public JDBCObviousNetwork(Connection con, String nodeTable, String edgeTable,
      String nodeId, String edgeId, String source, String target,
      Schema nodeSchema, Schema edgeSchema) throws SQLException {
    this.con = con;
    this.nodeTable = nodeTable;
    this.edgeTable = edgeTable;
    this.nodeKey = nodeId;
    this.edgeKey = edgeId;
    this.sourceCol = source;
    this.targetCol = target;
    this.nodeSchema = nodeSchema;
    this.edgeSchema = edgeSchema;
    this.nodeKeyToNodeId = new HashMap<Object, Integer>();
    this.edgeKeyToEdgeId = new HashMap<Object, Integer>();
    this.networkStruct = new HashMap<Integer, Integer[]>();
    loadFromJDBC(nodeTable, nodeKey, nodeKeyToNodeId, nodeCount);
    loadStructure();
  }

  /**
   * Load the node id into the memory.
   * @param tableName name of the node table
   * @param colId column id for the node table
   * @param map map containing nodes
   * @param counter node counter
   * @throws SQLException if something bad happens
   */
  private void loadFromJDBC(String tableName, String colId,
      Map<Object, Integer> map, int counter) throws SQLException {
    String request = "SELECT " + colId + " FROM " + tableName;
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(request);
    while (rs.next()) {
      map.put(rs.getObject(colId), counter);
      counter++;
    }
    rs.close();
    stmt.close();
  }

  /**
   * Loads the graph structure into the memory (only ids are loaded).
   * @throws SQLException if something bad happens
   */
  private void loadStructure() throws SQLException {
    String request = "SELECT " + edgeKey + ", " + sourceCol + "," + targetCol
        + " FROM " + edgeTable + " WHERE EDGE_ID < 123000 ORDER BY " + edgeKey;
    Statement stmt = con.createStatement();
    ResultSet rslt = stmt.executeQuery(request);
    while (rslt.next()) {
      Object edgeKeyVal = rslt.getObject(edgeKey);
      Object sourceNode = rslt.getObject(sourceCol);
      Object targetNode = rslt.getObject(targetCol);
      edgeKeyToEdgeId.put(edgeKeyVal, edgeCount);
      networkStruct.put(edgeCount, new Integer[] {
          nodeKeyToNodeId.get(sourceNode), nodeKeyToNodeId.get(targetNode)});
      edgeCount++;
    }
    rslt.close();
    stmt.close();
  }

  @Override
  public boolean addEdge(Edge edge, Collection<? extends Node> nodes,
      obvious.data.Graph.EdgeType edgeType) {
    try {
      if (null == nodes) {
        throw new IllegalArgumentException(
            "'nodes' parameter must not be null");
      } else if (nodes.size() == 2) {
        Node[] nodeArray = nodes.toArray(new Node[2]);
        return this.addEdge(edge, nodeArray[0], nodeArray[1], edgeType);
      } else if (nodes.size() == 1) {
        Node[] nodeArray = nodes.toArray(new Node[1]);
        return this.addEdge(edge, nodeArray[0], nodeArray[0], edgeType);
      } else {
        throw new IllegalArgumentException("Networks connect 1 or 2 nodes,"
            + "'nodes' size is " + nodes.size());
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  @Override
  public boolean addEdge(Edge edge, Node source, Node target,
      obvious.data.Graph.EdgeType edgeType) {
    if (!edge.getSchema().equals(edgeSchema)) {
      return false;
    } else {
      try {
        Object edgeKeyVal = edge.get(edgeKey);
        Object sourceKeyVal = source.get(nodeKey);
        Object targetKeyVal = target.get(nodeKey);
        if (!nodeKeyToNodeId.containsKey(sourceKeyVal)) {
          addNode(source);
        }
        if (!nodeKeyToNodeId.containsKey(targetKeyVal)) {
          addNode(target);
        }
        edgeKeyToEdgeId.put(edgeKeyVal, edgeCount);
        edgeCount++;
        String request = "INSERT INTO " + edgeTable + " ";
        String colNames = "(";
        String values = "(";
        for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
          colNames += edgeSchema.getColumnName(i);
          values += "'" + edge.get(i).toString() + "'";
          if (i != edgeSchema.getColumnCount() - 1) {
            colNames += ", ";
            values += ", ";
          } else {
            colNames += ")";
            values += ")";
          }
        }
        request = request + colNames + " VALUES " + values;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(request);
        stmt.close();
        return true;
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      }
    }
  }

  @Override
  public boolean addNode(Node node) {
    if (!node.getSchema().equals(nodeSchema)) {
      return false;
    } else {
        try {
          Object nodeKeyVal = node.get(nodeKey);
          nodeKeyToNodeId.put(nodeKeyVal, nodeCount);
          nodeCount++;
          String request = "INSERT INTO " + nodeTable + " ";
          String colNames = "(";
          String values = "(";
          for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
            colNames += nodeSchema.getColumnName(i);
            values += "'" + node.get(i).toString() + "'";
            if (i != nodeSchema.getColumnCount() - 1) {
              colNames += ", ";
              values += ", ";
            } else {
              colNames += ")";
              values += ")";
            }
          }
          request = request + colNames + " VALUES " + values;
          Statement stmt = con.createStatement();
          stmt.executeUpdate(request);
          stmt.close();
          return true;
        } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      }
    }
  }

  @Override
  public Edge getConnectingEdge(Node v1, Node v2) {
    int v1Id = nodeKeyToNodeId.get(v1.get(nodeKey));
    int v2Id = nodeKeyToNodeId.get(v2.get(nodeKey));
    for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
      if ((entry.getValue()[0] == v1Id && entry.getValue()[1] == v2Id)
          || (entry.getValue()[0] == v2Id && entry.getValue()[1] == v1Id)) {
        return getEdge(entry.getKey());
      }
    }
    return null;
  }

  @Override
  public Collection<Edge> getConnectingEdges(Node v1, Node v2) {
    Edge edge = getConnectingEdge(v1, v2);
    ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.add(edge);
    return edges;
  }

  @Override
  public obvious.data.Graph.EdgeType getEdgeType(Edge edge) {
    return Graph.EdgeType.DIRECTED;
  }

  @Override
  public Collection<Edge> getEdges() {
    try {
      ArrayList<Edge> edges = new ArrayList<Edge>();
      String request = "SELECT ";
      String colNames = "";
      for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
        colNames += edgeSchema.getColumnName(i);
        if (i != edgeSchema.getColumnCount() - 1) {
          colNames += ", ";
        } else {
          colNames += " ";
        }
      }
      request = request + colNames + " FROM " + edgeTable + " WHERE EDGE_ID < 123000";
      Statement stmt = con.createStatement();
      ResultSet rslt = stmt.executeQuery(request);
      while (rslt.next()) {
        Object[] values = new Object[edgeSchema.getColumnCount()];
        for (int i = 1; i <= edgeSchema.getColumnCount(); i++) {
          values[i - 1] = rslt.getString(i);
        }
        edges.add(new EdgeImpl(edgeSchema, values));
      }
      return edges;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  @Override
  public Collection<Edge> getInEdges(Node node) {
    ArrayList<Edge> inEdges = new ArrayList<Edge>();
    Object nodeKeyVal = node.get(nodeKey);
    for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
      Edge currentEdge = getEdge(entry.getKey());
      if (entry.getValue()[0].equals(nodeKeyToNodeId.get(nodeKeyVal))
          && getEdgeType(currentEdge).equals(Graph.EdgeType.UNDIRECTED)) {
        inEdges.add(currentEdge);
      } else if (entry.getValue()[1].equals(nodeKeyToNodeId.get(nodeKeyVal))) {
        inEdges.add(currentEdge);
      }
    }
    return inEdges;
  }

  @Override
  public Collection<Edge> getIncidentEdges(Node node) {
    ArrayList<Edge> incidentEdges = new ArrayList<Edge>();
    if (nodeKeyToNodeId.containsKey(node.get(nodeKey))) {
      int nodeId = nodeKeyToNodeId.get(node.get(nodeKey));
      for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
        if (entry.getValue()[0] == nodeId || entry.getValue()[1] == nodeId) {
          incidentEdges.add(getEdge(entry.getKey()));
        }
      }
    }
    return incidentEdges;
  }

  @Override
  public Collection<Node> getIncidentNodes(Edge edge) {
    ArrayList<Node> incidentNodes = new ArrayList<Node>();
    if (edgeKeyToEdgeId.containsKey(edge.get(edgeKey))) {
      int edgeId = edgeKeyToEdgeId.get(edge.get(edgeKey));
      if (networkStruct.containsKey(edgeId)) {
        Integer[] nodes = networkStruct.get(edgeId);
        incidentNodes.add(getNode(nodes[0]));
        incidentNodes.add(getNode(nodes[1]));
      }
    }
    return incidentNodes;
  }

  @Override
  public Collection<Node> getNeighbors(Node node) {
    Object nodeKeyVal = node.get(nodeKey);
    ArrayList<Node> neighbors = new ArrayList<Node>();
    for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
      if (entry.getValue()[0].equals(nodeKeyToNodeId.get(nodeKeyVal))) {
        neighbors.add(getNode(entry.getValue()[1]));
      } else if (entry.getValue()[1].equals(nodeKeyToNodeId.get(nodeKeyVal))) {
        neighbors.add(getNode(entry.getValue()[0]));
      }
    }
    return neighbors;
  }

  @Override
  public Collection<Node> getNodes() {
    try {
      ArrayList<Node> nodes = new ArrayList<Node>();
      String request = "SELECT ";
      String colNames = "";
      for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
        colNames += nodeSchema.getColumnName(i);
        if (i != nodeSchema.getColumnCount() - 1) {
          colNames += ", ";
        } else {
          colNames += " ";
        }
      }
      request = request + colNames + " FROM " + nodeTable;
      Statement stmt = con.createStatement();
      ResultSet rslt = stmt.executeQuery(request);
      while (rslt.next()) {
        Object[] values = new Object[nodeSchema.getColumnCount()];
        for (int i = 1; i <= nodeSchema.getColumnCount(); i++) {
          values[i - 1] = rslt.getObject(i);
        }
        nodes.add(new NodeImpl(nodeSchema, values));
      }
      return nodes;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  @Override
  public Node getOpposite(Node node, Edge edge) {
    int edgeId = edgeKeyToEdgeId.get(edge.get(edgeKey));
    int nodeId = nodeKeyToNodeId.get(node.get(nodeKey));
    if (nodeId == networkStruct.get(edgeId)[0]) {
      return getNode(networkStruct.get(edgeId)[1]);
    } else if (nodeId == networkStruct.get(edgeId)[1]) {
      return getNode(networkStruct.get(edgeId)[0]);
    }
    return null;
  }

  @Override
  public Collection<Edge> getOutEdges(Node node) {
    ArrayList<Edge> outEdges = new ArrayList<Edge>();
    Object nodeKeyVal = node.get(nodeKey);
    for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
      Edge currentEdge = getEdge(entry.getKey());
      if (entry.getValue()[0].equals(nodeKeyToNodeId.get(nodeKeyVal))) {
        outEdges.add(currentEdge);
      } else if (entry.getValue()[1].equals(nodeKeyToNodeId.get(nodeKeyVal))
          && getEdgeType(currentEdge).equals(Graph.EdgeType.UNDIRECTED)) {
        outEdges.add(currentEdge);
      }
    }
    return outEdges;
  }

  @Override
  public Collection<Node> getPredecessors(Node node) {
    Object nodeKeyVal = node.get(nodeKey);
    ArrayList<Node> predecessors = new ArrayList<Node>();
    for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
      Edge currentEdge = getEdge(entry.getKey());
      if (entry.getValue()[0].equals(nodeKeyToNodeId.get(nodeKeyVal))
          && getEdgeType(currentEdge).equals(Graph.EdgeType.UNDIRECTED)) {
        predecessors.add(getNode(entry.getValue()[1]));
      } else if (entry.getValue()[1].equals(nodeKeyToNodeId.get(nodeKeyVal))) {
        predecessors.add(getNode(entry.getValue()[0]));
      }
    }
    return predecessors;
  }

  @Override
  public Node getSource(Edge directedEdge) {
    int edgeId = edgeKeyToEdgeId.get(new BigDecimal(
        directedEdge.get(edgeKey).toString()));
    return getNode(networkStruct.get(edgeId)[0]);
  }

  @Override
  public Collection<Node> getSuccessors(Node node) {
    Object nodeKeyVal = node.get(nodeKey);
    ArrayList<Node> successors = new ArrayList<Node>();
    for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
      Edge currentEdge = getEdge(entry.getKey());
      if (entry.getValue()[0].equals(nodeKeyToNodeId.get(nodeKeyVal))) {
        successors.add(getNode(entry.getValue()[1]));
      } else if (entry.getValue()[1].equals(nodeKeyToNodeId.get(nodeKeyVal))
          && getEdgeType(currentEdge).equals(Graph.EdgeType.UNDIRECTED)) {
        successors.add(getNode(entry.getValue()[0]));
      }
    }
    return successors;
  }

  @Override
  public Node getTarget(Edge directedEdge) {
    int edgeId = edgeKeyToEdgeId.get(new BigDecimal(
        directedEdge.get(edgeKey).toString()));
    return getNode(networkStruct.get(edgeId)[1]);
  }

  @Override
  public boolean removeEdge(Edge edge) {
    Object edgeKeyVal = edge.get(edgeKey);
    if (!edgeKeyToEdgeId.containsKey(edgeKeyVal)) {
      return false;
    } else {
      try {
        String request = "DELETE FROM " + edgeTable + "WHERE " + edgeKey
            + " = '" + edgeKeyVal.toString();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(request);
        stmt.close();
        int edgeId = edgeKeyToEdgeId.get(edgeKeyVal);
        networkStruct.remove(edgeId);
        edgeKeyToEdgeId.remove(edgeKeyVal);
        return true;
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      }
    }
  }

  @Override
  public boolean removeNode(Node node) {
    Object nodeKeyVal = node.get(nodeKey);
    if (!nodeKeyToNodeId.containsKey(nodeKeyVal)) {
      return false;
    } else {
      try {
        int nodeId = nodeKeyToNodeId.get(nodeKeyVal);
        ArrayList<Integer> relativeEdges = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer[]> entry : networkStruct.entrySet()) {
          if (entry.getValue()[0] == nodeId || entry.getValue()[1] == nodeId) {
            relativeEdges.add(entry.getKey());
          }
        }
        for (Integer i : relativeEdges) {
          removeEdge(getEdge(i));
        }
        String request = "DELETE FROM " + nodeTable + "WHERE " + nodeKey
        + " = '" + nodeKeyVal.toString();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(request);
        stmt.close();
        edgeKeyToEdgeId.remove(nodeId);
        return true;
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      }
    }
  }

  @Override
  public Object getUnderlyingImpl(Class<?> type) {
    return null;
  }

  /**
   * Gets a specific node.
   * @param nodeId a node id
   * @return a node
   */
  private Node getNode(int nodeId) {
    try {
      String request = "SELECT * FROM " + nodeTable + " WHERE " + nodeKey
      + " = ?";
      PreparedStatement stmt = con.prepareStatement(request);
      String value = retrieveKey(nodeKeyToNodeId, nodeId).toString();
      stmt.setString(1, value);
      ResultSet rslt = stmt.executeQuery();
      ResultSetMetaData metadata = rslt.getMetaData();
      while (rslt.next()) {
        Object[] values = new Object[nodeSchema.getColumnCount()];
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
          for (int j = 0; j < nodeSchema.getColumnCount(); j++) {
            if (metadata.getColumnName(i).equals(
                nodeSchema.getColumnName(j))) {
              values[j] = rslt.getObject(i);
            }
          }
        }
        rslt.close();
        stmt.close();
        return new NodeImpl(nodeSchema, values);
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
    return null;
  }

  /**
   * Gets a specific edge.
   * @param edgeId an edge id
   * @return an edge
   */
  private Edge getEdge(int edgeId) {
    try {
      String request = "SELECT * FROM " + edgeTable + " WHERE " + edgeKey
      + " = ?";
      PreparedStatement stmt = con.prepareStatement(request);
      String value = retrieveKey(edgeKeyToEdgeId, edgeId).toString();
      stmt.setString(1, value);
      ResultSet rslt = stmt.executeQuery();
      ResultSetMetaData metadata = rslt.getMetaData();
      while (rslt.next()) {
        Object[] values = new Object[edgeSchema.getColumnCount()];
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
          for (int j = 0; j < edgeSchema.getColumnCount(); j++) {
            if (metadata.getColumnName(i).equals(
                edgeSchema.getColumnName(j))) {
              values[j] = rslt.getObject(i);
            }
          }
        }
        rslt.close();
        stmt.close();
        return new EdgeImpl(edgeSchema, values);
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
    return null;
  }

  /**
   * Retrieves a key in the map.
   * @param map a map
   * @param id associated value to the wanted key
   * @return a key
   */
  private Object retrieveKey(Map<Object, Integer> map, int id) {
    for (Map.Entry<Object, Integer> entry : map.entrySet()) {
      if (entry.getValue() == id) {
        return entry.getKey();
      }
    }
    return null;
  }

}