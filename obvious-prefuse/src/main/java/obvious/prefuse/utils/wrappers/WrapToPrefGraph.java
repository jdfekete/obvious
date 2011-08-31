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

package obvious.prefuse.utils.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import obvious.data.Graph;
import obvious.data.Network;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.TupleImpl;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tree;
import prefuse.data.Tuple;
import prefuse.data.event.GraphListener;
import prefuse.data.expression.Predicate;
import prefuse.data.tuple.TupleManager;
import prefuse.data.tuple.TupleSet;
import prefuse.util.collections.IntIterator;

/**
 * Wrapper for obvious table to prefuse graph.
 * @author Hemery
 *
 */
public class WrapToPrefGraph extends prefuse.data.Graph {

  /**
   * Obvious network.
   */
  private Network network;

  /**
   * Node Schema (obvious).
   */
  private obvious.data.Schema nodeSchema;

  /**
   * Edge Schema (obvious).
   */
  private obvious.data.Schema edgeSchema;

  /**
   * Constructor.
   * @param inNetwork obvious network to wrap
   */
  public WrapToPrefGraph(Network inNetwork) {
    super(new WrapToPrefTable(inNetwork.getNodeTable()),
        new WrapToPrefTable(inNetwork.getEdgeTable()), true);
    this.network = inNetwork;
    if (network.getNodes().size() != 0) {
      this.nodeSchema = network.getNodes().iterator().next().getSchema();
    }
    if (network.getEdges().size() != 0) {
      this.edgeSchema = network.getEdges().iterator().next().getSchema();
    }
    m_nodeTuples = new TupleManager(getNodeTable(), this, Table.class);
    m_edgeTuples = new TupleManager(getEdgeTable(), this, Table.class);
  }

  @Override
  public int addEdge(int s, int t) {
    obvious.data.Node source = getObviousNode(s);
    obvious.data.Node target = getObviousNode(t);
    Object[] values = new Object[nodeSchema.getColumnCount()];
    for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
      values[i] = edgeSchema.getColumnDefault(i);
    }
    EdgeImpl edge = new EdgeImpl(edgeSchema, values);
    network.addEdge(edge, source, target, Graph.EdgeType.DIRECTED);
    return getEdgeCount();
  }

  @Override
  public Edge addEdge(Node s, Node t) {
    return super.addEdge(s, t);
  }

  @Override
  public void addGraphModelListener(GraphListener listnr) {
    super.addGraphModelListener(listnr);
  }

  @Override
  protected void addLink(String field, int len, int n, int e) {
    super.addLink(field, len, n, e);
  }

  @Override
  public Node addNode() {
    Object[] values = new Object[nodeSchema.getColumnCount()];
    for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
      values[i] = nodeSchema.getColumnDefault(i);
    }
    NodeImpl node = new NodeImpl(nodeSchema, values);
    network.addNode(node);
    return new WrapToPrefNode(network, node, getNodeCount());
  }

  @Override
  public int addNodeRow() {
    return super.addNodeRow();
  }

  @Override
  public void clear() {
    super.clear();
  }

  @Override
  protected void clearEdges() {
    super.clearEdges();
  }

  @Override
  public void clearSpanningTree() {
    super.clearSpanningTree();
  }

  @Override
  protected Table createLinkTable() {
    return super.createLinkTable();
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  @Override
  protected boolean edgeCheck(Edge e, boolean throwException) {
    return super.edgeCheck(e, throwException);
  }

  @Override
  public IntIterator edgeRows() {
    return super.edgeRows();
  }

  @Override
  public IntIterator edgeRows(int node, int direction) {
    return super.edgeRows(node, direction);
  }

  @Override
  public IntIterator edgeRows(int node) {
    return super.edgeRows(node);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator edges() {
    Collection<obvious.data.Edge> obvEdges = network.getEdges();
    ArrayList<Edge> prefEdges = new ArrayList<Edge>();
    for (obvious.data.Edge edge : obvEdges) {
      prefEdges.add(new WrapToPrefEdge(network, (TupleImpl) edge,
          edge.getRow()));
    }
    return prefEdges.iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator edges(Node node) {
    Collection<obvious.data.Edge> obvEdges = network.getIncidentEdges(
        getObviousNode(node));
    ArrayList<Edge> prefEdges = new ArrayList<Edge>();
    for (obvious.data.Edge edge : obvEdges) {
      prefEdges.add(new WrapToPrefEdge(network, (TupleImpl) edge,
          edge.getRow()));
    }
    return prefEdges.iterator();
  }

  @Override
  protected void fireGraphEvent(Table t, int first, int last, int col,
      int type) {
    super.fireGraphEvent(t, first, last, col, type);
  }

  @Override
  public Node getAdjacentNode(Edge e, Node n) {
    return new WrapToPrefNode(network,
        (TupleImpl) network.getOpposite(getObviousNode(n),
        getObviousEdge(e)), network.getOpposite(getObviousNode(n),
            getObviousEdge(e)).getRow());
  }

  @Override
  public int getAdjacentNode(int edge, int node) {
    return network.getOpposite(getObviousNode(node),
        getObviousEdge(edge)).getRow();
  }

  @Override
  public int getDegree(int node) {
    return network.getIncidentEdges(getObviousNode(node)).size();
  }

  @Override
  public int getDegree(Node n) {
    return network.getIncidentEdges(getObviousNode(n)).size();
  }

  @Override
  public int getEdge(int source, int target) {
    obvious.data.Node sourceNode = getObviousNode(source);
    obvious.data.Node targetNode = getObviousNode(target);
    return network.getConnectingEdge(sourceNode, targetNode).getRow();
  }

  @Override
  public Edge getEdge(int e) {
    return new WrapToPrefEdge(network, (TupleImpl) getObviousEdge(e), e);
  }

  @Override
  public Edge getEdge(Node source, Node target) {
    return super.getEdge(source, target);
  }

  @Override
  public int getEdgeCount() {
    return network.getEdges().size();
  }

  @Override
  public TupleSet getEdges() {
    return super.getEdges();
  }

  @Override
  public String getEdgeSourceField() {
    return super.getEdgeSourceField();
  }

  @Override
  public Table getEdgeTable() {
    if (network == null) {
      return new Table();
    }
    return new WrapToPrefTable(network.getEdgeTable());
  }

  @Override
  public String getEdgeTargetField() {
    return super.getEdgeTargetField();
  }

  @Override
  public int getInDegree(int node) {
    return network.getInEdges(getObviousNode(node)).size();
  }

  @Override
  public int getInDegree(Node n) {
    return network.getInEdges(getObviousNode(n)).size();
  }

  @Override
  public int getKey(int node) {
    return  node;
  }

  @Override
  public Node getNode(int n) {
    return new WrapToPrefNode(network, (TupleImpl) getObviousNode(n), n);
  }

  @Override
  public int getNodeCount() {
    if (network == null) {
      return 0;
    }
    return network.getNodes().size();
  }

  @Override
  public Node getNodeFromKey(int arg0) {
    return super.getNodeFromKey(arg0);
  }

  @Override
  public int getNodeIndex(int arg0) {
    return super.getNodeIndex(arg0);
  }

  @Override
  public String getNodeKeyField() {
    return super.getNodeKeyField();
  }

  @Override
  public TupleSet getNodes() {
    TupleSet nodes = new Table();
    for (obvious.data.Node node : network.getNodes()) {
      nodes.addTuple(new WrapToPrefNode(network, (TupleImpl) node,
          node.getRow()));
    }
    return nodes;
  }

  @Override
  public Table getNodeTable() {
    if (network == null) {
      return new Table();
    }
    return new WrapToPrefTable(network.getNodeTable());
  }

  @Override
  public int getOutDegree(int node) {
    return network.getOutEdges(getObviousNode(node)).size();
  }

  @Override
  public int getOutDegree(Node n) {
    return network.getOutEdges(getObviousNode(n)).size();
  }

  @Override
  public Node getSourceNode(Edge e) {
    return new WrapToPrefNode(network,
        (TupleImpl) network.getSource(getObviousEdge(e)),
        network.getSource(getObviousEdge(e)).getRow());
  }

  @Override
  public int getSourceNode(int edge) {
    return network.getSource(getObviousEdge(edge)).getRow();
  }

  @Override
  public Tree getSpanningTree() {
    return super.getSpanningTree();
  }

  @Override
  public Tree getSpanningTree(Node root) {
    return super.getSpanningTree(root);
  }

  @Override
  public Node getTargetNode(Edge e) {
    return new WrapToPrefNode(network,
        (TupleImpl) network.getTarget(getObviousEdge(e)),
        network.getTarget(getObviousEdge(e)).getRow());
  }

  @Override
  public int getTargetNode(int edge) {
    return network.getTarget(getObviousEdge(edge)).getRow();
  }

  @Override
  public IntIterator inEdgeRows(int node) {
    return super.inEdgeRows(node);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator inEdges(Node node) {
    Collection<obvious.data.Edge> obvEdges = network.getInEdges(
        getObviousNode(node));
    ArrayList<Edge> prefEdges = new ArrayList<Edge>();
    for (obvious.data.Edge edge : obvEdges) {
      prefEdges.add(new WrapToPrefEdge(network, (TupleImpl) edge,
          edge.getRow()));
    }
    return prefEdges.iterator();
  }

  @Override
  protected void init(Table nodes, Table edges, boolean directed,
      String nodeKey, String sourceKey, String targetKey) {
    super.init(nodes, edges, directed, nodeKey, sourceKey, targetKey);
  }

  @Override
  protected void initLinkTable() {
    super.initLinkTable();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator inNeighbors(Node n) {
    Collection<obvious.data.Node> obvNodes = network.getPredecessors(
        getObviousNode(n));
    ArrayList<Node> prefNodes = new ArrayList<Node>();
    for (obvious.data.Node node : obvNodes) {
      prefNodes.add(new WrapToPrefNode(network, (TupleImpl) node,
          node.getRow()));
    }
    return prefNodes.iterator();
  }

  @Override
  public boolean isDirected() {
    return super.isDirected();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator neighbors(Node n) {
    Collection<obvious.data.Node> obvNodes = network.getNeighbors(
        getObviousNode(n));
    ArrayList<Node> prefNodes = new ArrayList<Node>();
    for (obvious.data.Node node : obvNodes) {
      prefNodes.add(new WrapToPrefNode(network, (TupleImpl) node,
          node.getRow()));
    }
    return prefNodes.iterator();
  }

  @Override
  protected boolean nodeCheck(Node n, boolean throwException) {
    return super.nodeCheck(n, throwException);
  }

  @Override
  public IntIterator nodeRows() {
    return super.nodeRows();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator nodes() {
    Collection<obvious.data.Node> obvNodes = network.getNodes();
    ArrayList<Node> prefNodes = new ArrayList<Node>();
    for (obvious.data.Node node : obvNodes) {
      prefNodes.add(new WrapToPrefNode(network, (TupleImpl) node,
          node.getRow()));
    }
    return prefNodes.iterator();
  }

  @Override
  public IntIterator outEdgeRows(int node) {
    return super.outEdgeRows(node);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator outEdges(Node node) {
    Collection<obvious.data.Edge> obvEdges = network.getOutEdges(
        getObviousNode(node));
    ArrayList<Edge> prefEdges = new ArrayList<Edge>();
    for (obvious.data.Edge edge : obvEdges) {
      prefEdges.add(new WrapToPrefEdge(network, (TupleImpl) edge,
          edge.getRow()));
    }
    return prefEdges.iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator outNeighbors(Node n) {
    Collection<obvious.data.Node> obvNodes = network.getSuccessors(
        getObviousNode(n));
    ArrayList<Node> prefNodes = new ArrayList<Node>();
    for (obvious.data.Node node : obvNodes) {
      prefNodes.add(new WrapToPrefNode(network, (TupleImpl) node,
          node.getRow()));
    }
    return prefNodes.iterator();
  }

  @Override
  protected boolean remLink(String field, int len, int n, int e) {
    return super.remLink(field, len, n, e);
  }

  @Override
  public boolean removeEdge(Edge e) {
    return network.removeEdge(getObviousEdge(e));
  }

  @Override
  public boolean removeEdge(int edge) {
    return network.removeEdge(getObviousEdge(edge));
  }

  @Override
  public void removeGraphModelListener(GraphListener listnr) {
    super.removeGraphModelListener(listnr);
  }

  @Override
  public boolean removeNode(int node) {
    return network.removeNode(getObviousNode(node));
  }

  @Override
  public boolean removeNode(Node n) {
    return network.removeNode(getObviousNode(n));
  }

  @Override
  public boolean removeTuple(Tuple t) {
    return super.removeTuple(t);
  }

  @Override
  public void setEdgeTable(Table edges) {
    super.setEdgeTable(edges);
  }

  @Override
  public void setTupleManagers(TupleManager ntm, TupleManager etm) {
    super.setTupleManagers(ntm, etm);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator tuples() {
    return super.tuples();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator tuples(Predicate filter) {
    return super.tuples(filter);
  }

  @Override
  protected void updateDegrees(int e, int s, int t, int incr) {
    super.updateDegrees(e, s, t, incr);
  }

  @Override
  protected void updateDegrees(int e, int incr) {
    super.updateDegrees(e, incr);
  }

  @Override
  protected void updateNodeData(int r, boolean added) {
    super.updateNodeData(r, added);
  }

  /**
   * Returns an obvious node corresponding to a prefuse node.
   * @param node prefuse node
   * @return an obvious node
   */
  private obvious.data.Node getObviousNode(Node node) {
    Object[] values = new Object[node.getSchema().getColumnCount()];
    for (int i = 0; i < node.getSchema().getColumnCount(); i++) {
      values[i] = node.get(node.getSchema().getColumnName(i));
    }
    return new NodeImpl(nodeSchema, values);
  }

  /**
   * Returns an obvious node corresponding to a node id.
   * @param nodeId node id
   * @return an obvious node
   */
  private obvious.data.Node getObviousNode(int nodeId) {
    return getObviousNode(getNodeFromKey(nodeId));
  }

  /**
   * Returns an obvious edge corresponding to an edge id.
   * @param edgeId edge id
   * @return an obvious edge
   */
  private obvious.data.Edge getObviousEdge(int edgeId) {
    return getObviousEdge(getEdge(edgeId));
  }

  /**
   * Returns an obvious edge corresponding to a prefuse edge.
   * @param edge prefuse edge
   * @return an obvious edge
   */
  private obvious.data.Edge getObviousEdge(Edge edge) {
    Object[] values = new Object[edge.getSchema().getColumnCount()];
    for (int i = 0; i < edge.getSchema().getColumnCount(); i++) {
      values[i] = edge.get(edge.getSchema().getColumnName(i));
    }
    return new EdgeImpl(edgeSchema, values);
  }
}
