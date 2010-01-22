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

package obvious.jung.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.util.Pair;

import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.TableImpl;

/**
 * Implementation of an Obvious Network based on Jung toolkit.
 * @author Pierre-Luc Hemery
 *
 */
public class JungObviousNetwork implements Network {

  /**
   * A Jung graph used for this obvious implementation.
   */
  private edu.uci.ics.jung.graph.Graph<Node, Edge> jungGraph;

  /**
   * Constructor from Jung Graph instance.
   * @param graph original Jung graph instance
   */
  public JungObviousNetwork(edu.uci.ics.jung.graph.Graph<Node, Edge> graph) {
    this.jungGraph = graph;
  }

  /**
   * Constructor from Obvious Schema.
   * @param nodeSchema schema for the node Table
   * @param edgeSchema schema for the edge Table
   */
  public JungObviousNetwork(Schema nodeSchema, Schema edgeSchema) {
    JungGraph graph = new JungGraph(nodeSchema, edgeSchema);
    this.jungGraph = graph;
  }

  /**
   * Adds a hyperedge.
   * @param edge to add
   * @param nodes concerned by addition
   * @param edgeType directed or undirected edge
   * @return true if added
   */
  public boolean addEdge(Edge edge, Collection<? extends Node> nodes,
      obvious.data.Graph.EdgeType edgeType) {
    edu.uci.ics.jung.graph.util.EdgeType type =
      edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED;
    if (edgeType == obvious.data.Graph.EdgeType.DIRECTED) {
      type = edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
    }
    return this.jungGraph.addEdge(edge, nodes, type);
  }

  /**
   * Convenience method for multigraphs.
   * @param edge edge to add
   * @param source source node
   * @param target target node
   * @param edgeType directed or undirected edge
   * @return true if added
   */
  public boolean addEdge(Edge edge, Node source, Node target,
      obvious.data.Graph.EdgeType edgeType) {
    edu.uci.ics.jung.graph.util.EdgeType type =
      edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED;
    if (edgeType == obvious.data.Graph.EdgeType.DIRECTED) {
      type = edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
    }
    return this.jungGraph.addEdge(edge, source, target, type);
  }

  /**
   * Adds a node.
   * @param node node to add
   * @return true if added
   */
  public boolean addNode(Node node) {
    return this.jungGraph.addVertex(node);
  }

  /**
   * Gets for two nodes the connecting edge.
   * @param v1 a node of the graph
   * @param v2 another node of the graph
   * @return connecting edge
   */
  public Edge getConnectingEdge(Node v1, Node v2) {
    return this.jungGraph.findEdge(v1, v2);
  }

  /**
   * Gets connecting edges between two nodes.
   * @param v1 a node of the graph
   * @param v2 another node of the graph
   * @return collection of connecting edges
   */
  public Collection<Edge> getConnectingEdges(Node v1, Node v2) {
    return this.jungGraph.findEdgeSet(v1, v2);
  }

  /**
   * Get the edge type.
   * @param edge an edge of the graph
   * @return directed or indirected {@link #EdgeType}
   */
  public obvious.data.Graph.EdgeType getEdgeType(Edge edge) {
    edu.uci.ics.jung.graph.util.EdgeType type = jungGraph.getEdgeType(edge);
    if (type == edu.uci.ics.jung.graph.util.EdgeType.DIRECTED) {
      return obvious.data.Graph.EdgeType.DIRECTED;
    } else {
      return obvious.data.Graph.EdgeType.DIRECTED;
    }
  }

  /**
   * Gets the edges of the graph instance.
   * @return collection of edges
   */
  public Collection<Edge> getEdges() {
    return this.jungGraph.getEdges();
  }

  /**
   * Gets in-linking edges for a specific node.
   * @param node a node of the graph
   * @return collection of in-linking edges
   */
  public Collection<Edge> getInEdges(Node node) {
    return this.jungGraph.getInEdges(node);
  }

  /**
   * Gets incident edges for a spotted node.
   * @param node a node of the graph
   * @return collection of incident edges
   */
  public Collection<Edge> getIncidentEdges(Node node) {
    return this.jungGraph.getIncidentEdges(node);
  }

  /**
   * Gets incident nodes for a spotted edge.
   * @param edge an edge of the graph
   * @return collection of incident nodes
   */
  public Collection<Node> getIncidentNodes(Edge edge) {
    return this.jungGraph.getIncidentVertices(edge);
  }

  /**
   * Gets the neighbors node.
   * @param node central node to determine the neighborhood
   * @return collection of nodes that are neighbors
   */
  public Collection<Node> getNeighbors(Node node) {
    return this.jungGraph.getNeighbors(node);
  }

  /**
   * Returns a collection of all nodes of the graph.
   * @return all nodes of the graph
   */
  public Collection<Node> getNodes() {
    return this.jungGraph.getVertices();
  }

  /**
   * Get the opposite node for a couple (node,edge).
   * @param node spotted node
   * @param edge spotted edge
   * @return opposite node
   */
  public Node getOpposite(Node node, Edge edge) {
    return this.jungGraph.getOpposite(node, edge);
  }

  /**
   * Gets out-linking edges for a specific node.
   * @param node a node of the graph
   * @return collection of out-linking edges
   */
  public Collection<Edge> getOutEdges(Node node) {
    return this.jungGraph.getOutEdges(node);
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node spotted node
   * @return collection of nodes
   */
  public Collection<Node> getPredecessors(Node node) {
    return this.jungGraph.getPredecessors(node);
  }

  /**
   * Gets the source of a directed edge.
   * @param directedEdge a directed edge of the graph
   * @return source node
   */
  public Node getSource(Edge directedEdge) {
    return this.jungGraph.getSource(directedEdge);
  }

  /**
   * Get successor nodes.
   * @param node a node of the graph
   * @return collection of nodes
   */
  public Collection<Node> getSuccessors(Node node) {
    return this.jungGraph.getSuccessors(node);
  }

  /**
   * Get the target of a directed edge.
   * @param directedEdge a directed edge of the graph
   * @return target node
   */
  public Node getTarget(Edge directedEdge) {
    return this.jungGraph.getDest(directedEdge);
  }

  /**
   * Removes an edge.
   * @param edge edge to remove
   * @return true if removed
   */
  public boolean removeEdge(Edge edge) {
    return this.jungGraph.removeEdge(edge);
  }

  /**
   * Removes a node.
   * @param node node to remove
   * @return true if removed
   */
  public boolean removeNode(Node node) {
    return this.jungGraph.removeVertex(node);
  }

  /**
   * Simple implementation of Jung AbstractGraph.
   * Used as default Jung Graph implementation for Obvious Jung.
   * @author Pierre-Luc H�mery
   *
   */
  protected static class JungGraph extends AbstractGraph<Node, Edge> {

    /**
     * Table of the nodes of the graph.
     */
    protected Table nodeTable;

    /**
     * Table of the edges of the graph.
     */
    protected Table edgeTable;

    /**
     * Map indicating the type of an edge.
     */
    protected Map<Edge, edu.uci.ics.jung.graph.util.EdgeType> edgeTypeMap;

    /**
     * Name for the Source Node index in edgeSchema.
     */
    public static final String SRCNODE = "SRCNODE";

    /**
     * Name for the Target Node index in edgeSchema.
     */
    public static final String DESTNODE = "DESTNODE";

    /**
     * Constructor for Jung Graph.
     * @param nodeSchema schema for the nodeTable
     * @param edgeSchema schema for the edgeTable
     */
    protected JungGraph(Schema nodeSchema, Schema edgeSchema) {
      this.nodeTable = new TableImpl(nodeSchema);
      this.edgeTable = new TableImpl(edgeSchema);
      this.edgeTypeMap =
        new HashMap<Edge, edu.uci.ics.jung.graph.util.EdgeType>();
      for (int i = 0; i < edgeTable.getRowCount(); i++) {
        this.edgeTypeMap.put(new EdgeImpl(edgeTable, i), getDefaultEdgeType());
      }
    }

    @Override
    public boolean addEdge(Edge edge, Pair<? extends Node> nodes,
        edu.uci.ics.jung.graph.util.EdgeType type) {
      if (!containsVertex(nodes.getFirst())
          || !containsVertex(nodes.getSecond())) {
        return false;
      } else {
        edgeTable.addRow();
        edge.set(SRCNODE, nodes.getFirst().getRow());
        edge.set(DESTNODE, nodes.getSecond().getRow());
        edgeTable.set(edgeTable.getRowCount() - 1, SRCNODE,
            nodes.getFirst().getRow());
        edgeTable.set(edgeTable.getRowCount() - 1, DESTNODE,
            nodes.getSecond().getRow());
        for (int i = 0; i < edgeTable.getSchema().getColumnCount(); i++) {
          if (!edgeTable.getSchema().getColumnName(i).equals(SRCNODE)
              && !edgeTable.getSchema().getColumnName(i).equals(DESTNODE)) {
            edgeTable.set(edgeTable.getRowCount() - 1, i,
                edge.get(edgeTable.getSchema().getColumnName(i)));
          }
        }
        setType(edge, type);
        return true;
      }
    }

    /**
     * Set the convenient type in edgeTypeMap.
     * @param edge an edge of the graph
     * @param type DIRECTED or UNDIRECTED
     */
    protected void setType(Edge edge,
        edu.uci.ics.jung.graph.util.EdgeType type) {
      edgeTypeMap.put(edge, type);
    }

    /**
     * Gets the target node for a directed edge.
     * @param e of the graph
     * @return target node or null if the edge is undirected
     */
    public Node getDest(Edge e) {
      if (!getEdgeType(e).equals(edu.uci.ics.jung.graph.util.EdgeType.DIRECTED)
          || !containsEdge(e)) {
        return null;
      } else {
        return new NodeImpl(nodeTable, e.getInt(DESTNODE));
      }
    }

    /**
     * Gets the two endpoints for a given edge.
     * @param edge an edge of the graph
     * @return two endpoints nodes
     */
    public Pair<Node> getEndpoints(Edge edge) {
      Pair<Node> endpoint =
        new Pair<Node>(new NodeImpl(nodeTable, edge.getInt(SRCNODE)),
            new NodeImpl(nodeTable, edge.getInt(DESTNODE)));
      return endpoint;
    }

    /**
     * Gets all in-linking edges for a node of the graph.
     * @param node a node of the graph
     * @return collection of in-linking edges
     */
    public Collection<Edge> getInEdges(Node node) {
      Collection<Edge> inEdges = new ArrayList<Edge>();
      for (Map.Entry<Edge, edu.uci.ics.jung.graph.util.EdgeType> e
          : edgeTypeMap.entrySet()) {
        if (e.getValue().equals(
            edu.uci.ics.jung.graph.util.EdgeType.DIRECTED)) {
          if (e.getKey().getInt(DESTNODE) == node.getRow()) {
            inEdges.add(e.getKey());
          }
        } else {
          if (e.getKey().getInt(DESTNODE) == node.getRow()
              || e.getKey().getInt(SRCNODE) == node.getRow()) {
            inEdges.add(e.getKey());
          }
        }
      }
      return inEdges;
    }

    /**
     * Gets all out-linking edges for a node of the graph.
     * @param node a node of the graph
     * @return collection of out-linking edges
     */
    public Collection<Edge> getOutEdges(Node node) {
      Collection<Edge> outEdges = new ArrayList<Edge>();
      for (Map.Entry<Edge, edu.uci.ics.jung.graph.util.EdgeType> e
          : edgeTypeMap.entrySet()) {
        if (e.getValue().equals(
            edu.uci.ics.jung.graph.util.EdgeType.DIRECTED)) {
          if (e.getKey().getInt(SRCNODE) == node.getRow()) {
            outEdges.add(e.getKey());
          }
        } else {
          if (e.getKey().getInt(DESTNODE) == node.getRow()
              || e.getKey().getInt(SRCNODE) == node.getRow()) {
            outEdges.add(e.getKey());
          }
        }
      }
      return outEdges;
    }

    /**
     * Gets the predecessors of a node in the graph.
     * @param node a node of the graph
     * @return collection of predecessors
     */
    public Collection<Node> getPredecessors(Node node) {
      Collection<Node> preds = new ArrayList<Node>();
      // a node is predecessor of "our node", if for a directed edge, this node
      // is the source and "our node" the target. For an undirected edge, the
      // opposite of the node for this edge is a predecessor.
      for (Map.Entry<Edge, edu.uci.ics.jung.graph.util.EdgeType> e
          : edgeTypeMap.entrySet()) {
        if (e.getValue().equals(
            edu.uci.ics.jung.graph.util.EdgeType.DIRECTED)
            && node.getRow() == e.getKey().getInt(DESTNODE)) {
          preds.add(new NodeImpl(nodeTable, e.getKey().getInt(SRCNODE)));
        } else if (e.getValue().equals(
            edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED)
             && (node.getRow() == e.getKey().getInt(SRCNODE)
              || node.getRow() == e.getKey().getInt(DESTNODE))) {
          preds.add(new NodeImpl(nodeTable, getOpposite(node,
              e.getKey()).getRow()));
        }
      }
      return preds;
    }

    /**
     * Gets the source node for a directed edge.
     * @param e of the graph
     * @return source node or null if the edge is undirected
     */
    public Node getSource(Edge e) {
      if (!getEdgeType(e).equals(edu.uci.ics.jung.graph.util.EdgeType.DIRECTED)
          || !containsEdge(e)) {
        return null;
      } else {
        return new NodeImpl(nodeTable, e.getInt(SRCNODE));
      }
    }

    /**
     * Gets the successors of a node in the graph.
     * @param node a node of the graph
     * @return collection of successors
     */
    public Collection<Node> getSuccessors(Node node) {
      Collection<Node> succs = new ArrayList<Node>();
      // a node is successor of "our node", if for a directed edge, this node
      // is the target and "our node" the source. For an undirected edge, the
      // opposite of the node for this edge is a successor.
      for (Map.Entry<Edge, edu.uci.ics.jung.graph.util.EdgeType> e
          : edgeTypeMap.entrySet()) {
        if (e.getValue().equals(
            edu.uci.ics.jung.graph.util.EdgeType.DIRECTED)
            && node.getRow() == e.getKey().getInt(SRCNODE)) {
          succs.add(new NodeImpl(nodeTable, e.getKey().getInt(DESTNODE)));
        } else if (e.getValue().equals(
            edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED)
             && (node.getRow() == e.getKey().getInt(SRCNODE)
              || node.getRow() == e.getKey().getInt(DESTNODE))) {
          succs.add(new NodeImpl(nodeTable, getOpposite(node,
              e.getKey()).getRow()));
        }
      }
      return succs;
    }

    /**
     * Checks if a node is the target of a specific edge.
     * @param node supposed target node
     * @param edge an edge of the graph
     * @return true if the node is target for this edge
     */
    public boolean isDest(Node node, Edge edge) {
      return node.equals(this.getDest(edge));
    }

    /**
     * Checks if a node is source of a specific edge.
     * @param node supposed source node
     * @param edge an edge of the graph
     * @return true if the node is source for this edge
     */
    public boolean isSource(Node node, Edge edge) {
      return node.equals(this.getSource(edge));
    }

    /**
     * Adds a node to the graph.
     * @param node node to add
     * @return true if added
     */
    public boolean addVertex(Node node) {
      for (int i = 0; i < node.getSchema().getColumnCount(); i++) {
        if (!nodeTable.getSchema().hasColumn(
            node.getSchema().getColumnName(i))) {
          return false;
        }
      }
      nodeTable.addRow();
      for (int i = 0; i < nodeTable.getSchema().getColumnCount(); i++) {
        nodeTable.set(nodeTable.getRowCount() - 1,
            node.getSchema().getColumnName(i), node.get(i));
      }
      return true;
    }

    /**
     * Checks if a specific edge is in the graph.
     * @param edge an edge
     * @return true if the input edge is in the graph
     */
    public boolean containsEdge(Edge edge) {
      for (int i = 0; i < edge.getSchema().getColumnCount(); i++) {
        if (i > edgeTable.getSchema().getColumnCount()) { // check schema size
          return false;
        } else if (!edgeTable.getSchema().hasColumn(
            edge.getSchema().getColumnName(i))) { // check column name
          return false;
        }
      }
      return edge.getRow() < edgeTable.getRowCount()
      && nodeTable.isValidRow(edge.getRow());
    }

    /**
     * Checks if a specific node is in the graph.
     * @param node a node
     * @return true if the input node is in the graph
     */
    public boolean containsVertex(Node node) {
      for (int i = 0; i < node.getSchema().getColumnCount(); i++) {
        if (i > nodeTable.getSchema().getColumnCount()) { // check schema size
          return false;
        } else if (!nodeTable.getSchema().hasColumn(
            node.getSchema().getColumnName(i))) { // check column name
          return false;
        }
      }
      return node.getRow() < nodeTable.getRowCount()
        && nodeTable.isValidRow(node.getRow());
    }

    /**
     * Gets the default edgeType for the graph.
     * @return UNDIRECTED
     */
    public edu.uci.ics.jung.graph.util.EdgeType getDefaultEdgeType() {
      // TODO Auto-generated method stub
      return edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED;
    }

    /**
     * Gets the number of edges in the graph.
     * @return number of edges
     */
    public int getEdgeCount() {
      return getEdges().size();
    }

    /**
     * Gets the number of edges of a specified type.
     * @param type type to match (directed or undirected)
     * @return number of edges of the specified type
     */
    public int getEdgeCount(edu.uci.ics.jung.graph.util.EdgeType type) {
      return this.getEdges(type).size();
    }

    /**
     * Gets the type of an edge.
     * @param edge an edge of the graph
     * @return the type of the edge (directed or undirected)
     */
    public edu.uci.ics.jung.graph.util.EdgeType getEdgeType(Edge edge) {
      for (Entry<Edge, edu.uci.ics.jung.graph.util.EdgeType> e
        : edgeTypeMap.entrySet()) {
        if (e.getKey().equals(edge)) {
          return edgeTypeMap.get(e.getKey());
        }
      }
      edgeTypeMap.put(edge, this.getDefaultEdgeType());
      return edgeTypeMap.get(edge);
    }

    /**
     * Gets a collection of the edges of the graph.
     * @return collection of edges
     */
    public Collection<Edge> getEdges() {
      Collection<Edge> edgeColl = new ArrayList<Edge>();
      for (int i = 0; i < edgeTable.getRowCount(); i++) {
        if (this.edgeTable.isValidRow(i)) {
          edgeColl.add(new EdgeImpl(edgeTable, i));
        }
      }
      return edgeColl;
    }

    /**
     * Gets edges of a specific type.
     * @param type type to match (directed or undirected)
     * @return edge of the specified type
     */
    public Collection<Edge> getEdges(
        edu.uci.ics.jung.graph.util.EdgeType type) {
      Collection<Edge> edgeTypedColl = new ArrayList<Edge>();
      for (Map.Entry<Edge, edu.uci.ics.jung.graph.util.EdgeType> e
        : edgeTypeMap.entrySet()) {
        if (e.getValue() == type) {
          edgeTypedColl.add(e.getKey());
        }
      }
      return edgeTypedColl;
    }

    /**
     * Gets incident edges for a specific node.
     * @param node a node of the graph
     * @return a collection of edges
     */
    public Collection<Edge> getIncidentEdges(Node node) {
      Collection<Edge> incidentEdge = new ArrayList<Edge>();
      for (int i = 0; i < edgeTable.getRowCount(); i++) {
        if (edgeTable.getValue(i, SRCNODE).equals(node.getRow())
            || edgeTable.getValue(i, DESTNODE).equals(node.getRow())) {
          incidentEdge.add(new EdgeImpl(edgeTable, i));
        }
      }
      return incidentEdge;
    }

    /**
     * Gets the neighbors for a given node.
     * @param node a node of the graph
     * @return collection of neighbors node
     */
    public Collection<Node> getNeighbors(Node node) {
     Collection<Node> neighbors = new ArrayList<Node>();
     for (int i = 0; i < edgeTable.getRowCount(); i++) {
       if (edgeTable.getValue(i, SRCNODE).equals(node.getRow())) {
         neighbors.add(new NodeImpl(nodeTable,
             (Integer) edgeTable.getValue(i, DESTNODE)));
       } else if (edgeTable.getValue(i, DESTNODE).equals(node.getRow())) {
         neighbors.add(new NodeImpl(nodeTable,
             (Integer) edgeTable.getValue(i, SRCNODE)));
       }
     }
      return neighbors;
    }

    /**
     * Gets the number of nodes in the graph.
     * @return number of nodes
     */
    public int getVertexCount() {
      return getVertices().size();
    }

    /**
     * Gets a collection of the nodes of the graph.
     * @return collection of nodes
     */
    public Collection<Node> getVertices() {
      Collection<Node> nodeColl = new ArrayList<Node>();
      for (int i = 0; i < nodeTable.getRowCount(); i++) {
        if (this.nodeTable.isValidRow(i)) {
          nodeColl.add(new NodeImpl(nodeTable, i));
        }
      }
      return nodeColl;
    }

    /**
     * Removes an edge from the graph.
     * @param edge an edge of the graph
     * @return true if removed
     */
    public boolean removeEdge(Edge edge) {
      if (!this.containsEdge(edge)) {
        return false;
      } else {
          edgeTable.removeRow(edge.getRow());
        return true;
      }
    }

    /**
     * Removes a node from the graph.
     * @param node a node of the graph
     * @return true if removed
     */
    public boolean removeVertex(Node node) {
      if (!this.containsVertex(node)) {
        return false;
      } else {
          nodeTable.removeRow(node.getRow());
          // removing the associated edge
          for (int i = 0; i < edgeTable.getRowCount(); i++) {
            if (edgeTable.getValue(i, SRCNODE).equals(node.getRow())
                || edgeTable.getValue(i, DESTNODE).equals(node.getRow())) {
              edgeTable.removeRow(i);
            }
          }
        return true;
      }
    }

  }
}
