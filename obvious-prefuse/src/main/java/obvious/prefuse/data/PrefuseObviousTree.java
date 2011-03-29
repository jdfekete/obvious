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

package obvious.prefuse.data;

import java.util.ArrayList;
import java.util.Collection;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tree;

/**
 * Implementation of an Obvious Tree based on Prefuse toolkit.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousTree extends PrefuseObviousNetwork
  implements Tree<Node, Edge> {

  /**
   * Forest associated to this tree.
   */
  private Collection<Tree<Node, Edge>> forest;

  /**
   * Constructor from Obvious schema(s) for edges and nodes with specific
   * id.
   * @param nodeSchema original node schema
   * @param edgeSchema original edge schema
   * @param nodeId nodeKey data field used to uniquely identify a node. If this
   * field is null, the node table row numbers will be used
   * @param source data field used to identify the source node in an edge
   * table
   * @param target data field used to identify the target node in an edge
   * table
   */
  public PrefuseObviousTree(Schema nodeSchema , Schema edgeSchema,
      String nodeId, String source, String target) {
    super(prefuse.data.Tree.DEFAULT_SOURCE_KEY,
        prefuse.data.Tree.DEFAULT_TARGET_KEY);
    Table node = new PrefuseObviousTable(nodeSchema);
    Table edge = new PrefuseObviousTable(edgeSchema);
    prefuse.data.Tree prefTree = new prefuse.data.Tree(
        ((PrefuseObviousTable) node).getPrefuseTable(),
        ((PrefuseObviousTable) edge).getPrefuseTable(), nodeId, source,
        target);
    this.setPrefuseGraph(prefTree);
    if (this.forest == null) {
      this.forest = new ArrayList<Tree<Node, Edge>>();
    }
    if (!this.forest.contains(this)) {
      this.forest.add(this);
    }
  }

  /**
   * Constructor from Obvious schema(s) for edges and nodes.
   * @param nodeSchema original node schema
   * @param edgeSchema original edge schema
   */
  public PrefuseObviousTree(Schema nodeSchema , Schema edgeSchema) {
    this(nodeSchema, edgeSchema, prefuse.data.Tree.DEFAULT_NODE_KEY,
        prefuse.data.Tree.DEFAULT_SOURCE_KEY,
        prefuse.data.Tree.DEFAULT_TARGET_KEY);
  }

  /**
   * Constructor from a prefuse Tree.
   * @param prefTree original prefuse Tree
   */
  public PrefuseObviousTree(prefuse.data.Tree prefTree) {
    super(prefTree);
    if (this.forest == null) {
      this.forest = new ArrayList<Tree<Node, Edge>>();
    }
    if (!this.forest.contains(this)) {
      this.forest.add(this);
    }
  }

  /**
   * Gets a collection of child Edges of a node in the tree.
   * @param node a node of the tree
   * @return child edges of the tree
   */
  public Collection<Edge> getChildEdges(Node node) {
    Collection<Edge> edgeList = new ArrayList<Edge>();
    Collection<Node> nodeList = this.getChildNodes(node);
    prefuse.data.Node prefNode = this.getPrefuseGraph().getNodeFromKey(
        this.getPrefuseGraph().getKey(node.getRow()));
    for (Node  currentNode : nodeList) {
      prefuse.data.Node currentChild = this.getPrefuseGraph().getNodeFromKey(
          this.getPrefuseGraph().getKey(currentNode.getRow()));
      prefuse.data.Edge currentEdge = this.getPrefuseGraph().getEdge(
          prefNode, currentChild);
      edgeList.add(new PrefuseObviousEdge(currentEdge));
    }
    return edgeList;
  }

  /**
   * Gets a collection of children of a node in the tree.
   * @param node a node of the tree
   * @return children of the node
   */
  public Collection<Node> getChildNodes(Node node) {
    Collection<Node> nodeList = new ArrayList<Node>();
    prefuse.data.Node prefNode = this.getPrefuseGraph().getNodeFromKey(
        this.getPrefuseGraph().getKey(node.getRow()));
    prefuse.data.Tree prefTree = (prefuse.data.Tree) this.getPrefuseGraph();
    for (int i = 0; i < prefTree.getChildCount(node.getRow()); i++) {
      nodeList.add(new PrefuseObviousNode(prefTree.getChild(prefNode, i)));
    }
    return nodeList;
  }

  /**
   * Gets the depth of the given node in the tree.
   * @param n a node of the tree
   * @return parent edge
   */
  public int getDepth(Node n) {
    if (n.equals(this.getRoot())) {
      return 0;
    } else {
      int depth = 0;
      for (int i = n.getRow(); i != getRoot().getRow() && i >= 0; ++depth, i =
          getParentNode(new PrefuseObviousNode(
              getPrefuseGraph().getNode(i))).getRow()) {
        continue;
      }
      return depth;
    }
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
    prefuse.data.Node prefNode = this.getPrefuseGraph().getNodeFromKey(
        this.getPrefuseGraph().getKey(node.getRow()));
    prefuse.data.Edge parentEdge = ((prefuse.data.Tree)
        this.getPrefuseGraph()).getParentEdge(prefNode);
    return new PrefuseObviousEdge(parentEdge);
  }

  /**
   * Gets the parent node.
   * @param node a node of the tree
   * @return parent node
   */
  public Node getParentNode(Node node) {
    if (node.equals(this.getRoot())) {
      return null;
    } else {
      prefuse.data.Node prefNode = this.getPrefuseGraph().getNodeFromKey(
          this.getPrefuseGraph().getKey(node.getRow()));
      prefuse.data.Node parentNode = ((prefuse.data.Tree)
          this.getPrefuseGraph()).getParent(prefNode);
      return new PrefuseObviousNode(parentNode);
    }
  }

  /**
   * Get the root node.
   * @return the root Node
   */
  public Node getRoot() {
    try {
      prefuse.data.Node n = ((prefuse.data.Tree) getPrefuseGraph()).getRoot();
      return new PrefuseObviousNode(n);
    } catch (IllegalArgumentException e) {
      for (Node node  : this.getNodes()) {
        int nodeRow = node.getRow();
        if (((prefuse.data.Tree) getPrefuseGraph()).getParent(nodeRow) < 0) {
          return new PrefuseObviousNode(
              ((prefuse.data.Tree) getPrefuseGraph()).getNode(nodeRow));
        }
      }
      return null;
    }
  }

  /**
   * Gets the forest associated to the tree.
   * @return the forest (collection of tree) of the tree
   */
  public Collection<Tree<Node, Edge>> getTrees() {
    return this.forest;
  }

}
