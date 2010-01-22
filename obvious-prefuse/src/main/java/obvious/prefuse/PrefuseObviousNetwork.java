/*
* Copyright (c) 2009, INRIA
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

package obvious.prefuse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;

/**
 * Implementation of an Obvious Network based on Prefuse toolkit.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousNetwork implements Network {

  /**
   * Prefuse graph of the Obvious network instance.
   */
  private prefuse.data.Graph graph;

  /**
   * Constructor from Obvious Schemas.
   * @param nodeSchema original schema for the nodes.
   * @param edgeSchema original schema for the edges.
   */
  public PrefuseObviousNetwork(Schema nodeSchema , Schema edgeSchema) {
    Table node = new PrefuseObviousTable(nodeSchema);
    Table edge = new PrefuseObviousTable(edgeSchema);
    this.graph = new prefuse.data.Graph((
        (PrefuseObviousTable) node).getPrefuseTable(),
        ((PrefuseObviousTable) edge).getPrefuseTable(), false);
  }

  /**
   * Constructor from Prefuse Graph instance.
   * @param prefGraph original Prefuse graph instance
   */
  public PrefuseObviousNetwork(prefuse.data.Graph prefGraph) {
    this.graph = prefGraph;
  }

  /**
   * Default constructor.
   */
  protected PrefuseObviousNetwork() {
  }

  /**
   * Gets the original prefuse graph.
   * @return prefuse graph
   */
  public prefuse.data.Graph getPrefuseGraph() {
    return this.graph;
  }

  /**
   * Sets the attribute graph of the class.
   * @param inputGraph graph to set
   */
  protected void setPrefuseGraph(prefuse.data.Graph inputGraph) {
    this.graph = inputGraph;
  }

  /**
   * Unused in this implementation : hyperedge (and hypergraph) are
   * not supported by prefuse.data.Graph class.
   * @param edge edge to add
   * @param nodes one or two nodes to link (collection)
   * @param edgeType unused parameter
   * @return true if added
   */
  public boolean addEdge(Edge edge, Collection<? extends Node> nodes,
      obvious.data.Graph.EdgeType edgeType) {
    if (null == nodes) {
      throw new IllegalArgumentException("'nodes' parameter must not be null");
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
      // prefuse creates a new edge, so will have to fill it with
      // existing informations of the parameter edge.
      prefuse.data.Edge prefEdge = this.graph.getEdge(
          graph.addEdge(source.getRow(), target.getRow()));
      // graph.getEdge(source.getRow(), target.getRow()));
      // harvesting informations from parameter edge...
      for (int i = 0; i < prefEdge.getColumnCount(); i++) {
        String colName = prefEdge.getColumnName(i);
        String sourceGraphName = prefuse.data.Graph.DEFAULT_SOURCE_KEY;
        String targetGraphName = prefuse.data.Graph.DEFAULT_TARGET_KEY;
        Boolean graphCol = colName.equals(sourceGraphName) || colName.equals(
            targetGraphName);
        // preventing overriding of prefuse internal values for graph model!
        if (!graphCol && edge.get(colName) != null) {
          prefEdge.set(colName, edge.get(colName));
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Adds a given node to the current graph.
   * @param node node to add to the graph
   * @return true if the node is added
   */
  public boolean addNode(Node node) {
    try {
      // if prefNode (prefuse) tuple and node (obvious) tuple are compatible
      // i.e their schema are compatible.
      prefuse.data.Node prefNode = this.graph.addNode();
      for (int i = 0; i < node.getTable().getSchema().getColumnCount(); i++) {
        prefNode.setValueAt(i, node.get(i));
      }
      return true;
    } catch (Exception e) {
      // if the node in parameter is not compatible with the current graph
      return false;
    }
  }

  /**
   * Gets for two nodes the connecting edge.
   * @param v1 first node
   * @param v2 second node
   * @return connecting edge
   */
  public Edge getConnectingEdge(Node v1, Node v2) {
    prefuse.data.Node prefV1 = this.graph.getNodeFromKey(
        this.graph.getKey(v1.getRow()));
    prefuse.data.Node prefV2 = this.graph.getNodeFromKey(
        this.graph.getKey(v2.getRow()));
    prefuse.data.Edge prefEdge = this.graph.getEdge(prefV1, prefV2);
    return new PrefuseObviousEdge(prefEdge);
  }

  /**
   * Gets connecting edges between two nodes.
   * @param v1 first node
   * @param v2 second node
   * @return collection of connecting edges
   */
  public Collection<Edge> getConnectingEdges(Node v1, Node v2) {
    Collection<Edge> edge = new ArrayList<Edge>(); // connecting edges list
    ArrayList<Integer> v1Edge = new ArrayList<Integer>(); // edges list of v1
    ArrayList<Integer> v2Edge = new ArrayList<Integer>(); // edges list of v2
    ArrayList<Integer> commonEdge = new ArrayList<Integer>(); // edges of v1/v2
    prefuse.util.collections.IntIterator v1EdgeIter =
      this.graph.edgeRows(v1.getRow());
    prefuse.util.collections.IntIterator v2EdgeIter =
      this.graph.edgeRows(v2.getRow());
    // fill in the list of v1 edges
    while (v1EdgeIter.hasNext()) {
      v1Edge.add(v1EdgeIter.nextInt());
    }
    // fill in the list of v2 edges
    while (v2EdgeIter.hasNext()) {
      v2Edge.add(v2EdgeIter.nextInt());
    }
    // find the connecting edges
    for (int i = 0; i < v1Edge.size(); i++) {
      for (int j = 0; j < v2Edge.size(); j++) {
        if (v1Edge.get(i).equals(v2Edge.get(j))) {
          commonEdge.add(v1Edge.get(i));
          break;
        }
      }
    }
    // build the collection
    for (int i = 0; i < commonEdge.size(); i++) {
      edge.add(new PrefuseObviousEdge(this.graph.getEdge(commonEdge.get(i))));
    }
    return edge;
  }

  /**
   * Indicates if the graph is made of directed edges.
   * In Prefuse graph, all edges follow the same "directed" attributes. There
   * is no distinction between edges in a prefuse graph so in this
   * implementation.
   * @param edge unused in this implementation
   * @return the corresponding EdgeType enum value
   */
  public obvious.data.Graph.EdgeType getEdgeType(Edge edge) {
    if (this.graph.isDirected()) {
      return EdgeType.DIRECTED;
    } else {
      return EdgeType.UNDIRECTED;
    }
  }

  /**
   * Returns a collection of all edges of the graph.
   * @return all edges of the graph
   */
  public Collection<Edge> getEdges() {
    Collection<Edge> edge = new ArrayList<Edge>();
    for (int i = 0; i < this.graph.getEdgeCount(); i++) {
      edge.add(new PrefuseObviousEdge(this.graph.getEdge(i)));
    }
    return edge;
  }

  /**
   * Gets in-linking edges for a specific node.
   * @param node spotted node
   * @return collection of in-linking edges
   */
  @SuppressWarnings("unchecked")
  public Collection<Edge> getInEdges(Node node) {
    Collection<Edge> inEdge = new ArrayList<Edge>();
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        this.graph.getKey(node.getRow()));
    Iterator<Edge> i = this.graph.inEdges(prefNode);
    while (i.hasNext()) {
      inEdge.add(new PrefuseObviousEdge((prefuse.data.Edge) i.next()));
    }
    return inEdge;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param node spotted
   * @return collection of incident edges
   */
  @SuppressWarnings("unchecked")
  public Collection<Edge> getIncidentEdges(Node node) {
    Collection<Edge> edge = new ArrayList<Edge>();
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        this.graph.getKey(node.getRow()));
    Iterator<Edge> i = this.graph.edges(prefNode);
    while (i.hasNext()) {
      edge.add(new PrefuseObviousEdge((prefuse.data.Edge) i.next()));
    }
    return edge;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param edge edge spotted
   * @return collection of incident nodes
   */
  public Collection<Node> getIncidentNodes(Edge edge) {
    Collection<Node> node = new ArrayList<Node>();
    prefuse.data.Edge prefEdge = this.graph.getEdge(edge.getRow());
    node.add(new PrefuseObviousNode(prefEdge.getSourceNode()));
    node.add(new PrefuseObviousNode(prefEdge.getTargetNode()));
    return node;
  }

  /**
   * Gets the neighbors node.
   * @param node central node to determine the neighborhood
   * @return collection of nodes that are neighbors
   */
  @SuppressWarnings("unchecked")
  public Collection<Node> getNeighbors(Node node) {
    Collection<Node> neighbor = new ArrayList<Node>();
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        this.graph.getKey(node.getRow()));
    Iterator<Node> i = this.graph.neighbors(prefNode);
    while (i.hasNext()) {
      neighbor.add(new PrefuseObviousNode((prefuse.data.Node) i.next()));
    }
    return neighbor;
  }

  /**
   * Returns a collection of all nodes of the graph.
   * @return all nodes of the graph
   */
  public Collection<Node> getNodes() {
    Collection<Node> node = new ArrayList<Node>();
    for (int i = 0; i < this.graph.getNodeCount(); i++) {
      node.add(new PrefuseObviousNode(this.graph.getNode(i)));
    }
    return node;
  }

  /**
   * Get the opposite node for a couple (node,edge).
   * @param node spotted node
   * @param edge spotted edge
   * @return opposite node
   */
  public Node getOpposite(Node node, Edge edge) {
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        graph.getKey(node.getRow()));
    prefuse.data.Edge prefEdge = this.graph.getEdge(edge.getRow());
    return new PrefuseObviousNode(this.graph.getAdjacentNode(prefEdge,
        prefNode));
  }

  /**
   * Get out-linking edges for a specific node.
   * @param node spotted node
   * @return collection of out-linking edges
   */
  @SuppressWarnings("unchecked")
  public Collection<Edge> getOutEdges(Node node) {
    Collection<Edge> outEdge = new ArrayList<Edge>();
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        this.graph.getKey(node.getRow()));
    Iterator<Edge> i = this.graph.outEdges(prefNode);
    while (i.hasNext()) {
      outEdge.add(new PrefuseObviousEdge((prefuse.data.Edge) i.next()));
    }
    return outEdge;
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node spotted node
   * @return collection of nodes
   */
  @SuppressWarnings("unchecked")
  public Collection<Node> getPredecessors(Node node) {
    if (this.graph.isDirected()) {
      ArrayList<Node> predecessor = new ArrayList<Node>();
      Iterator<Node> i = this.graph.inNeighbors(
          this.graph.getNodeFromKey(this.graph.getKey(node.getRow())));
      while (i.hasNext()) {
        predecessor.add(new PrefuseObviousNode((prefuse.data.Node) i.next()));
      }
      return predecessor;
    } else {
      return this.getNeighbors(node);
    }
  }

  /**
   * Gets the source node of a directed edge.
   * @param directedEdge spotted edge
   * @return source node
   */
  public Node getSource(Edge directedEdge) {
    int index = this.graph.getSourceNode(directedEdge.getRow());
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        this.graph.getKey(index));
    return new PrefuseObviousNode(prefNode);
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node  spotted node
   * @return collection of nodes
   */
  @SuppressWarnings("unchecked")
  public Collection<Node> getSuccessors(Node node) {
    if (this.graph.isDirected()) {
      ArrayList<Node> successor = new ArrayList<Node>();
      Iterator<Node> i = this.graph.outNeighbors(
          this.graph.getNodeFromKey(this.graph.getKey(node.getRow())));
      while (i.hasNext()) {
        successor.add(new PrefuseObviousNode((prefuse.data.Node) i.next()));
      }
      return successor;
    } else {
      return this.getNeighbors(node);
    }
  }

  /**
   * Gets the target node of a directed edge.
   * @param directedEdge spotted edge
   * @return target node
   */
  public Node getTarget(Edge directedEdge) {
    int index = this.graph.getTargetNode(directedEdge.getRow());
    prefuse.data.Node prefNode = this.graph.getNodeFromKey(
        this.graph.getKey(index));
    return new PrefuseObviousNode(prefNode);
  }

  /**
   * Removes an edge.
   * @param edge edge to remove
   * @return true if removed
   */
  public boolean removeEdge(Edge edge) {
    return this.graph.removeEdge(edge.getRow());
  }

  /**
   * Removes a node.
   * @param node node to remove
   * @return true if removed
   */
  public boolean removeNode(Node node) {
    return this.graph.removeNode(node.getRow());
  }

}
