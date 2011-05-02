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
import infovis.column.ColumnFactory;
import infovis.tree.DefaultTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tree;
import obvious.impl.EdgeImpl;

/**
 * This class is in an implementation of the
 * {@link obvious.data.Tree Tree} for the Obvious Ivtk binding based on
 * the Ivtk Tree class. Obvious and Ivtk do not share the same design
 * for Tree. In Obvious Tree extends Graph and are meant to be built
 * around two tables one for edges and one for nodes. However, in Ivtk,
 * trees do not extend graph and are built around only one table. Thus,
 * the current implementation is not a wrapper but has to reimplement
 * all method from the Obvious graph and tree interfaces using some
 * complementary Ivtk code.
 * @see obvious.data.Tree
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkObviousTree implements Tree<Node, Edge> {

  /**
   * A table for the edge.
   */
  private Table edgeTable;

  /**
   * Wrapped ivtk tree.
   */
  private infovis.Tree tree;

  /**
   * Map linking Obvious Node to ivtk node index.
   */
  private Map<Node, Integer> nodeToId = new HashMap<Node, Integer>();

  /**
   * Map linking Obvious Edge to ivtk edge index.
   */
  private Map<Edge, Integer> edgeToId = new HashMap<Edge, Integer>();

  /**
   * Forest associated to this tree.
   */
  private Collection<Tree<Node, Edge>> forest;

  /**
   * Schema for the nodes.
   */
  private Schema nodeSchema;

  /**
   * Schema for the edges.
   */
  private Schema edgeSchema;

  /**
   * Constructor.
   * @param inNodeSchema original schema for nodes
   * @param inEdgeSchema original schema for edges
   */
  public IvtkObviousTree(Schema inNodeSchema, Schema inEdgeSchema) {
    this.nodeSchema = inNodeSchema;
    this.edgeSchema = inEdgeSchema;
    if (!edgeSchema.hasColumn("parent")) {
      edgeSchema.addColumn("parent", Integer.class, 0);
    }
    if (!edgeSchema.hasColumn("child")) {
      edgeSchema.addColumn("child", Integer.class, 0);
    }
    edgeTable = new IvtkObviousTable(edgeSchema);
    tree = new DefaultTree();
    ColumnFactory factory = ColumnFactory.getInstance();
    for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
      Column col = factory.create(nodeSchema.getColumnType(i).getSimpleName(),
          nodeSchema.getColumnName(i));
      tree.addColumn(col);
    }
    if (this.forest == null) {
      this.forest = new ArrayList<Tree<Node, Edge>>();
    }
    if (!this.forest.contains(this)) {
      this.forest.add(this);
    }
  }

  /**
   * Constructor.
   * @param inTree Ivtk tree to wrap
   */
  public IvtkObviousTree(infovis.Tree inTree) {
    this.tree = inTree;
  }

  /**
   * Gets a collection of child Edges of a node in the tree.
   * @param node a node of the tree
   * @return child edges of the tree
   */
  public Collection<Edge> getChildEdges(Node node) {
    Collection<Edge> children = new ArrayList<Edge>();
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      if (getNodeId(node) == edgeTable.getValue(i, "parent")) {
        children.add(new EdgeImpl(edgeTable, i));
      }
    }
    return children;
  }

  /**
   * Gets a collection of children of a node in the tree.
   * @param node a node of the tree
   * @return children of the node
   */
  public Collection<Node> getChildNodes(Node node) {
    Collection<Node> children = new ArrayList<Node>();
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      if (getNodeId(node) == edgeTable.getValue(i, "parent")) {
        for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
          if (e.getValue() == edgeTable.getValue(i, "child")) {
            children.add(e.getKey());
          }
        }
      }
    }
    return children;
  }

  /**
   * Gets depth of a node.
   * @param node a node of the tree
   * @return depth of the node
   */
  public int getDepth(Node node) {
    return tree.getDepth(getNodeId(node)) - 1;
  }

  /**
   * Gets the height of the tree.
   * @return the height of tree.
   */
  public int getHeight() {
    return this.getHeight(this.getRoot()) - 1;
  }

  /**
   * Determines the height of a node of the tree.
   * @param node a node of the tree
   * @return height of the node
   */
  private int getHeight(Node node) {
    int max = 0;
    for (Node currentNode : this.getChildNodes(node)) {
      int height = this.getHeight(currentNode);
      max = java.lang.Math.max(max, height);
    }
    return 1 + max;
  }

  /**
   * Gets the parent edge.
   * @param node a node of the tree
   * @return parent edge
   */
  public Edge getParentEdge(Node node) {
    Edge parent = null;
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      if (getNodeId(node) == edgeTable.getValue(i, "child")) {
        parent = new EdgeImpl(edgeTable, i);
      }
    }
    return parent;
  }

  /**
   * Gets the parent node.
   * @param node a node of the tree
   * @return parent node
   */
  public Node getParentNode(Node node) {
    Node parent = null;
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      if (getNodeId(node) == edgeTable.getValue(i, "child")) {
        for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
          if (e.getValue() ==  edgeTable.getValue(i, "parent")) {
            parent = e.getKey();
            break;
          }
        }
      }
    }
    return parent;
  }

  /**
   * Gets the root node.
   * @return the root Node
   */
  public Node getRoot() {
    Node trueRoot = null;
    if (tree.getChildCount(infovis.Tree.ROOT) > 0) {
      int rootId = tree.getChild(infovis.Tree.ROOT, 0);
      for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
        if (e.getValue() == rootId) {
          trueRoot = e.getKey();
        }
      }
    }
    return trueRoot;
  }

  /**
   * Gets the forest associated to the tree.
   * @return the forest (collection of tree) of the tree
   */
  public Collection<Tree<Node, Edge>> getTrees() {
    return this.forest;
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
      if (!tree.isRowValid(getNodeId(source))
          || !tree.isRowValid(getNodeId(target))
          || edgeType.equals(obvious.data.Graph.EdgeType.UNDIRECTED)) {
        return false;
      }
      tree.reparent(getNodeId(target), getNodeId(source));
      int edgeId = edgeTable.addRow();
      edgeToId.put(edge, edgeId);
      for (int i = 0; i < edgeTable.getSchema().getColumnCount(); i++) {
        edgeTable.set(edgeId - 1, edge.getSchema().getColumnName(i),
            edge.get(i));
      }
      edgeTable.set(edgeId - 1, "parent", new Integer(getNodeId(source)));
      edgeTable.set(edgeId - 1, "child", getNodeId(target));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Adds a given node to the current tree.
   * @param node node to add to the graph
   * @return true if the node is added
   */
  public boolean addNode(Node node) {
    try {
      int nodeId = tree.addNode(infovis.Tree.ROOT);
      nodeToId.put(node, nodeId);
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
    Edge connecting = null;
    int v1Id = getNodeId(v1);
    int v2Id = getNodeId(v2);
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      int parentId = (Integer) edgeTable.getValue(i, "parent");
      int childId = (Integer) edgeTable.getValue(i, "child");
      if ((v1Id == parentId && v2Id == childId)
          || (v2Id == parentId && v1Id == childId)) {
        connecting = new EdgeImpl(edgeTable, i);
      }
    }
    return connecting;
  }

  /**
   * Gets connecting edges between two nodes.
   * @param v1 first node
   * @param v2 second node
   * @return collection of connecting edges
   */
  public Collection<Edge> getConnectingEdges(Node v1, Node v2) {
    Collection<Edge> connEdges = new ArrayList<Edge>();
    connEdges.add(this.getConnectingEdge(v1, v2));
    return connEdges;
  }

  /**
   * Indicates if an edge is directed or not.
   * In a tree all edges are directed.
   * @param edge an edge of the tree
   * @return the corresponding EdgeType enum value
   */
  public obvious.data.Graph.EdgeType getEdgeType(Edge edge) {
    return obvious.data.Graph.EdgeType.DIRECTED;
  }

  /**
   * Returns a collection of all edges of the tree.
   * @return all edges of the tree
   */
  public Collection<Edge> getEdges() {
    Collection<Edge> edges = new ArrayList<Edge>();
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      if (edgeTable.isValidRow(i)) {
        edges.add(new EdgeImpl(edgeTable, i));
      }
    }
    return edges;
  }

  /**
   * Gets in-linking edges for a specific node.
   * @param node a node of the tree
   * @return collection of in-linking edges
   */
  public Collection<Edge> getInEdges(Node node) {
    Collection<Edge> inEdges = new ArrayList<Edge>();
    inEdges.add(this.getParentEdge(node));
    return inEdges;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param node a node of the tree
   * @return collection of incident edges
   */
  public Collection<Edge> getIncidentEdges(Node node) {
    Collection<Edge> inEdges = new ArrayList<Edge>();
    inEdges.addAll(this.getInEdges(node));
    inEdges.addAll(this.getOutEdges(node));
    return inEdges;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param edge an edge of the graph
   * @return collection of incident nodes
   */
  public Collection<Node> getIncidentNodes(Edge edge) {
    Collection<Node> inNodes = new ArrayList<Node>();
    inNodes.add(this.getSource(edge));
    inNodes.add(this.getTarget(edge));
    return inNodes;
  }

  /**
   * Gets the neighbors node.
   * @param node central node to determine the neighborhood
   * @return collection of nodes that are neighbors
   */
  public Collection<Node> getNeighbors(Node node) {
    Collection<Node> neighbors = new ArrayList<Node>();
    neighbors.addAll(this.getPredecessors(node));
    neighbors.addAll(this.getSuccessors(node));
    return neighbors;
  }

  /**
   * Returns a collection of all nodes of the tree.
   * @return all nodes of the tree
   */
  public Collection<Node> getNodes() {
    Collection<Node> nodes = new ArrayList<Node>();
    for (int i = 0; i < tree.getRowCount(); i++) {
      if (tree.isRowValid(i)) {
        for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
          if (e.getValue() == i) {
            nodes.add(e.getKey());
          }
        }
      }
    }
    return nodes;
  }

  /**
   * Get the opposite node for a couple (node,edge).
   * @param node a node of the tree
   * @param edge an edge of the tree
   * @return opposite node
   */
  public Node getOpposite(Node node, Edge edge) {
    Node opposite = null;
    if (edgeTable.getValue(getEdgeId(edge), "parent")
        == getNodeId(node)) {
      for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
        if (e.getValue() == edgeTable.getValue(getEdgeId(edge), "child")) {
          opposite = e.getKey();
        }
      }
    } else if (edgeTable.getValue(edgeToId.get(edge), "child")
        == getNodeId(node)) {
      for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
        if (e.getValue() == edgeTable.getValue(getEdgeId(edge), "parent")) {
          opposite = e.getKey();
        }
      }
    }
    return opposite;
  }

  /**
   * Get out-linking edges for a specific node.
   * @param node a node of the tree
   * @return collection of out-linking edges
   */
  public Collection<Edge> getOutEdges(Node node) {
    return this.getChildEdges(node);
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node a node of the tree
   * @return collection of nodes
   */
  public Collection<Node> getPredecessors(Node node) {
    Collection<Node> preds = new ArrayList<Node>();
    preds.add(this.getParentNode(node));
    return preds;
  }

  /**
   * Gets the source (parent) node of a directed edge.
   * @param directedEdge an edge of the tree
   * @return source (parent) node
   */
  public Node getSource(Edge directedEdge) {
    Node source = null;
    Integer sourceId = (Integer)
        edgeTable.getValue(getEdgeId(directedEdge), "parent");
    for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
      if (e.getValue() == sourceId && tree.isRowValid(sourceId)) {
        source = e.getKey();
        break;
      }
    }
    return source;
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node  a node of the tree
   * @return collection of nodes
   */
  public Collection<Node> getSuccessors(Node node) {
    return this.getChildNodes(node);
  }

  /**
   * Gets the target (child) node of a directed edge.
   * @param directedEdge an edge of the tree
   * @return target (child) node
   */
  public Node getTarget(Edge directedEdge) {
    Node target = null;
    Integer targetId = (Integer)
        edgeTable.getValue(getEdgeId(directedEdge), "child");
    for (Map.Entry<Node, Integer> e : nodeToId.entrySet()) {
      if (e.getValue() == targetId && tree.isRowValid(targetId)) {
        target = e.getKey();
        break;
      }
    }
    return target;
  }

  /**
   * Removes an edge.
   * @param edge edge to remove
   * @return true if removed
   */
  public boolean removeEdge(Edge edge) {
    try {
      return edgeTable.removeRow(edge.getRow());
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
      if (tree.removeNode(getNodeId(node))) {
        for (Edge edge : this.getIncidentEdges(node)) {
          edgeTable.removeRow(edge.getRow());
        }
      }
      return tree.removeNode(getNodeId(node));
    } catch (Exception e) {
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
   * @return Cytoscape graph instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if  (type.equals(infovis.Tree.class)) {
      return tree;
    }
    return null;
  }

}
