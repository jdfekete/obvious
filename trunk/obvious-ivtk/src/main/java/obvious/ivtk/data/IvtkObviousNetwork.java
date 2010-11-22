/*
* Copyright (c) 2010, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package obvious.ivtk.data;

import infovis.Column;
import infovis.DynamicTable;
import infovis.column.ColumnFactory;
import infovis.graph.DefaultGraph;
import infovis.utils.RowIterator;

import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.event.NetworkListener;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

/**
 * Implementation of an Obvious Network based on infovis toolkit.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkObviousNetwork implements Network {

  /**
   * Wrapped ivtk graph.
   */
  private infovis.Graph graph;

  /**
   * Map linking Obvious Node to ivtk node index.
   */
  private Map<Node, Integer> nodeToId = new HashMap<Node, Integer>();

  /**
   * Map linking Obvious edge to ivtk edge index.
   */
  private Map<Edge, Integer> edgeToId = new HashMap<Edge, Integer>();

  /**
   * Format factory.
   */
  private FormatFactory formatFactory = new FormatFactoryImpl();

  /**
   * Schema for the nodes.
   */
  private Schema nodeSchema;

  /**
   * Schema for the edges.
   */
  private Schema edgeSchema;

  /**
   * Collection of NetworkListener.
   */
  private Collection<NetworkListener> listeners =
    new ArrayList<NetworkListener>();

  /**
   * Constructor from Obvious Schemas.
   * @param inNodeSchema original schema for node
   * @param inEdgeSchema original schema for edge
   * @param directed boolean indicating if the graph is oriented
   */
  public IvtkObviousNetwork(Schema inNodeSchema, Schema inEdgeSchema,
      Boolean directed) {
    graph = new DefaultGraph();
    graph.setDirected(directed);
    this.nodeSchema = inNodeSchema;
    this.edgeSchema = inEdgeSchema;
    ColumnFactory factory = ColumnFactory.getInstance();
    for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
      if (graph.getVertexTable().indexOf(nodeSchema.getColumnName(i)) == -1) {
        Column col = factory.create(nodeSchema.getColumnType(i).getSimpleName(),
            nodeSchema.getColumnName(i));
        graph.getVertexTable().addColumn(col);
      }
    }
    for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
      if (graph.getEdgeTable().indexOf(edgeSchema.getColumnName(i)) == -1) {
        Column col = factory.create(edgeSchema.getColumnType(i).getSimpleName(),
            edgeSchema.getColumnName(i));
        graph.getEdgeTable().addColumn(col);
      }
    }
  }

  /**
   * Constructor from Obvious Schemas.
   * @param inNodeSchema original schema for node
   * @param inEdgeSchema original schema for edge
   */
  public IvtkObviousNetwork(Schema inNodeSchema, Schema inEdgeSchema) {
    this(inNodeSchema, inEdgeSchema, false);
  }

  /**
   * Constructor from an ivtk Graph.
   * @param inGraph an ivtk graph instance
   */
  public IvtkObviousNetwork(infovis.Graph inGraph) {
    graph = inGraph;
  }

  /**
   * Constructor.
   */
  protected IvtkObviousNetwork() {
  }

  /**
   * Gets the nodeToId map.
   * @return nodeToId map
   */
  protected Map<Node, Integer> getNodeToId() {
    return this.nodeToId;
  }

  /**
   * Gets the edgeToId map.
   * @return edgeToId map
   */
  protected Map<Edge, Integer> getEdgeToId() {
    return this.edgeToId;
  }

  /**
   * Set the ivtk graph.
   * @param inGraph an infovis graph
   */
  protected void setGraph(infovis.Graph inGraph) {
    this.graph = inGraph;
  }

  /**
   * Adds an edge between one or two nodes contained in a collection.
   * @param edge edge to add
   * @param nodes one or two nodes to link (collection)
   * @param edgeType unused parameter
   * @return true if added
   */
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

  /**
   * Adds an edge to the graph.
   * @param edge edge to add
   * @param source source node
   * @param target target node
   * @param edgeType unused parameter
   * @return true if added
   */
  public boolean addEdge(Edge edge, Node source, Node target,
      obvious.data.Graph.EdgeType edgeType) {
    try {
      int edgeId = graph.addEdge(getNodeId(source), getNodeId(target));
      for (int i = 0; i < edge.getSchema().getColumnCount(); i++) {
        TypedFormat format = formatFactory.getFormat(
            edge.getSchema().getColumnType(i).getSimpleName());
         StringBuffer v = format.format(edge.get(i),
             new StringBuffer(), new FieldPosition(0));
        graph.getEdgeTable().setValueAt(v.toString(), edgeId,
            graph.getEdgeTable().indexOf(edge.getSchema().getColumnName(i)));
      }
      edgeToId.put(edge, edgeId);
      fireNetworkEvent(edge.getRow(), edge.getRow(), 0,
          NetworkListener.INSERT_EDGE);
      return true;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Adds a given node to the current graph.
   * @param node node to add to the graph
   * @return true if the node is added
   */
  public boolean addNode(Node node) {
    try {
      int rowId = graph.addVertex();
      for (int i = 0; i < node.getSchema().getColumnCount(); i++) {
        TypedFormat format = formatFactory.getFormat(
            node.getSchema().getColumnType(i).getSimpleName());
         StringBuffer v = format.format(node.get(i),
             new StringBuffer(), new FieldPosition(0));
        graph.getVertexTable().setValueAt(v.toString(), rowId,
            graph.getVertexTable().indexOf(node.getSchema().getColumnName(i)));
      }
      nodeToId.put(node, rowId);
      fireNetworkEvent(node.getRow(), node.getRow(), 0,
    		  NetworkListener.INSERT_NODE);
      return true;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Gets for two nodes the connecting edge.
   * @param v1 first node
   * @param v2 second node
   * @return connecting edge
   */
  public Edge getConnectingEdge(Node v1, Node v2) {
    int id = -1;
    int edgeId = graph.getEdge(getNodeId(v1), getNodeId(v2));
    int bisId = graph.getEdge(getNodeId(v2), getNodeId(v1));
    if (edgeId != -1 || bisId == -1) {
      id = edgeId;
    } else if (edgeId == -1 || bisId != -1) {
      id = bisId;
    } else if (edgeId == bisId) {
      id = edgeId;
    }
    for (Map.Entry<Edge, Integer> e : edgeToId.entrySet()) {
      if (e.getValue() == id) {
        return e.getKey();
      }
    }
    return null;
  }

  /**
   * Gets connecting edges between two nodes.
   * @param v1 first node
   * @param v2 second node
   * @return collection of connecting edges
   */
  public Collection<Edge> getConnectingEdges(Node v1, Node v2) {
    Collection<Edge> edges = new ArrayList<Edge>();
    if (this.getConnectingEdge(v1, v2) != null) {
      edges.add(this.getConnectingEdge(v1, v2));
    }
    return edges;
  }

  /**
   * Indicates if an edge is directed or not.
   * In ivtk graph, all edges follow the same "directed" attributes. There
   * is no distinction between edges in a ivtk graph so in this
   * implementation too.
   * @param edge an edge of the graph
   * @return the corresponding EdgeType enum value
   */
  public obvious.data.Graph.EdgeType getEdgeType(Edge edge) {
    if (graph.isDirected()) {
      return obvious.data.Graph.EdgeType.DIRECTED;
    } else {
      return obvious.data.Graph.EdgeType.UNDIRECTED;
    }
  }

  /**
   * Returns a collection of all edges of the graph.
   * @return all edges of the graph
   */
  public Collection<Edge> getEdges() {
    Collection<Edge> edges = new ArrayList<Edge>();
    DynamicTable baseTable = graph.getEdgeTable();
    Table edgeTable = new IvtkObviousTable(baseTable);
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      edges.add(new EdgeImpl(edgeTable, i));
    }
    return edges;
  }

  /**
   * Gets in-linking edges for a specific node.
   * @param node a node of the graph
   * @return collection of in-linking edges
   */
  public Collection<Edge> getInEdges(Node node) {
    Collection<Edge> inEdges = new ArrayList<Edge>();
    RowIterator it = graph.inEdgeIterator(getNodeId(node));
    while (it.hasNext()) {
      for (Map.Entry<Edge, Integer> e : edgeToId.entrySet()) {
        if (e.getValue() == it.next()) {
          inEdges.add(e.getKey());
        }
      }
    }
    return inEdges;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param node a node of the graph
   * @return collection of incident edges
   */
  public Collection<Edge> getIncidentEdges(Node node) {
    Collection<Edge> edges = new ArrayList<Edge>();
    int nodeId = getNodeId(node);
    for (Node otherNode : nodeToId.keySet()) {
      int nil = infovis.Graph.NIL;
      if (graph.getEdge(nodeId, getNodeId(otherNode)) != nil
          || graph.getEdge(getNodeId(otherNode), nodeId) != nil) {
        for (Map.Entry<Edge, Integer> e : edgeToId.entrySet()) {
          if (e.getValue()
              == graph.getEdge(nodeId, getNodeId(otherNode))
              || e.getValue() == graph.getEdge(
                  getNodeId(otherNode), nodeId)) {
            edges.add(e.getKey());
            break;
          }
        }
      }
    }
    return edges;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param edge an edge of the graph
   * @return collection of incident nodes
   */
  public Collection<Node> getIncidentNodes(Edge edge) {
    Collection<Node> nodes = new ArrayList<Node>();
    int first = graph.getFirstVertex(getEdgeId(edge));
    int second = graph.getSecondVertex(getEdgeId(edge));
    for (Node node : nodeToId.keySet()) {
      if (getNodeId(node) == first || getNodeId(node) == second) {
        nodes.add(node);
      }
    }
    return nodes;
  }

  /**
   * Gets the neighbors node.
   * @param node central node to determine the neighborhood
   * @return collection of nodes that are neighbors
   */
  public Collection<Node> getNeighbors(Node node) {
    Collection<Node> neighborhood = new ArrayList<Node>();
    for (Edge edge : this.getIncidentEdges(node)) {
      neighborhood.add(this.getOpposite(node, edge));
    }
    return neighborhood;
  }

  /**
   * Returns a collection of all nodes of the graph.
   * @return all nodes of the graph
   */
  public Collection<Node> getNodes() {
    Collection<Node> nodes = new ArrayList<Node>();
    DynamicTable baseTable = graph.getVertexTable();
    Table nodeTable = new IvtkObviousTable(baseTable);
    for (int i = 0; i < nodeTable.getRowCount(); i++) {
      nodes.add(new NodeImpl(nodeTable, i));
    }
    return nodes;
  }

  /**
   * Get the opposite node for a couple (node,edge).
   * @param node a node of the graph
   * @param edge an edge of the graph
   * @return opposite node
   */
  public Node getOpposite(Node node, Edge edge) {
    int nodeId = graph.getOtherVertex(getEdgeId(edge), getNodeId(node));
    for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
      if (e.getValue() == nodeId) {
        return e.getKey();
      }
    }
    return null;
  }

  /**
   * Get out-linking edges for a specific node.
   * @param node a node of the graph
   * @return collection of out-linking edges
   */
  public Collection<Edge> getOutEdges(Node node) {
    Collection<Edge> outEdges = new ArrayList<Edge>();
    RowIterator it = graph.outEdgeIterator(getNodeId(node));
    while (it.hasNext()) {
      for (Map.Entry<Edge, Integer> e : edgeToId.entrySet()) {
        if (e.getValue() == it.next()) {
          outEdges.add(e.getKey());
        }
      }
    }
    return outEdges;
  }


  /**
   * Get predecessors nodes for a specific node.
   * @param node a node of the graph
   * @return collection of nodes
   */
  public Collection<Node> getPredecessors(Node node) {
    if (!graph.isDirected()) {
      return this.getNeighbors(node);
    } else {
      Collection<Node> preds = new ArrayList<Node>();
      Collection<Edge> edges = this.getIncidentEdges(node);
      for (Edge edge : edges) {
        if (graph.getSecondVertex(getEdgeId(edge)) == getNodeId(node)) {
          for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
            if (e.getValue() == graph.getFirstVertex(getEdgeId(edge))) {
              preds.add(e.getKey());
              break;
            }
          }
        }
      }
      return preds;
    }
  }

  /**
   * Gets the source node of a directed edge.
   * @param directedEdge an edge of the graph
   * @return source node
   */
  public Node getSource(Edge directedEdge) {
    if (!graph.isDirected()) {
      return null;
    } else {
        Node source = null;
        int nodeId = graph.getFirstVertex(getEdgeId(directedEdge));
        for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
          if (e.getValue() == nodeId) {
            source = e.getKey();
            break;
          }
        }
        return source;
    }
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node  a node of the graph
   * @return collection of nodes
   */
  public Collection<Node> getSuccessors(Node node) {
    if (!graph.isDirected()) {
      return this.getNeighbors(node);
    } else {
      Collection<Node> succs = new ArrayList<Node>();
      Collection<Edge> edges = this.getIncidentEdges(node);
      for (Edge edge : edges) {
        if (graph.getFirstVertex(getEdgeId(edge)) == getNodeId(node)) {
          for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
            if (e.getValue() == graph.getSecondVertex(getEdgeId(edge))) {
              succs.add(e.getKey());
              break;
            }
          }
        }
      }
      return succs;
    }
  }

  /**
   * Gets the target node of a directed edge.
   * @param directedEdge an edge of the graph
   * @return target node
   */
  public Node getTarget(Edge directedEdge) {
    if (!graph.isDirected()) {
      return null;
    } else {
        Node target = null;
        int nodeId = graph.getSecondVertex(getEdgeId(directedEdge));
        for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
          if (e.getValue() == nodeId) {
            target = e.getKey();
            break;
          }
        }
        return target;
    }
  }

  /**
   * Removes an edge.
   * @param edge edge to remove
   * @return true if removed
   */
  public boolean removeEdge(Edge edge) {
    try {
      if (!edgeToId.containsKey(edge)) {
        return false;
      }
      graph.removeEdge(getEdgeId(edge));
      edgeToId.remove(edge);
      fireNetworkEvent(edge.getRow(), edge.getRow(), 0,
    		  NetworkListener.DELETE_EDGE);
      return true;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Removes a node.
   * @param node node to remove
   * @return true if removed
   */
  public boolean removeNode(Node node) {
    try {
      if (!nodeToId.containsValue(node.getRow())) {
        return false;
      }
      graph.removeVertex(node.getRow());
      nodeToId.remove(node);
      fireNetworkEvent(node.getRow(), node.getRow(), 0,
    		  NetworkListener.DELETE_NODE);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Gets the id of an input node.
   * @param node a node of the graph
   * @return id of the node
   */
  private Integer getNodeId(Node node) {
    Integer id = null;
    for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
      boolean nodeEquals = true;
      for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
        String colName = nodeSchema.getColumnName(i);
        if (!node.get(colName).equals(e.getKey().get(colName))) {
          nodeEquals = false;
          break;
        }
      }
      if (nodeEquals) {
        id = e.getValue();
        break;
      }
    }
    return id;
  }

  /**
   * Gets the id of an input edge.
   * @param edge an edge of the graph
   * @return id of the edge
   */
  private Integer getEdgeId(Edge edge) {
    Integer id = null;
    for (Map.Entry<Edge, Integer> e : edgeToId.entrySet()) {
      boolean edgeEquals = true;
      for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
        String colName = edgeSchema.getColumnName(i);
        if (!edge.get(colName).equals(e.getKey().get(colName))) {
          edgeEquals = false;
          break;
        }
      }
      if (edgeEquals) {
        id = e.getValue();
        break;
      }
    }
    return id;
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return Ivtk graph instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(infovis.Graph.class)) {
      return graph;
    }
    return null;
  }

	public Collection<NetworkListener> getNetworkListeners() {
	 return listeners;
	}
	
	public void removeNetworkListener(NetworkListener l) {
	 listeners.remove(l);
	}
	
	public void addNetworkListener(NetworkListener l) {
	 listeners.add(l);
	}
	
  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param type the type of modification
   */
  protected void fireNetworkEvent(int start, int end, int col, int type) {
   if (this.getNetworkListeners().isEmpty()) {
     return;
   }
   for (NetworkListener listnr : this.getNetworkListeners()) {
     listnr.networkChanged(this, start, end, col, type);
   }
  }

  public Table getEdgeTable() {
    return new IvtkObviousTable(graph.getEdgeTable());
  }

  public Table getNodeTable() {
    return new IvtkObviousTable(graph.getVertexTable());
  }

}
