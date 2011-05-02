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
import java.util.Collections;

import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.impl.NodeImpl;


/**
 * Implementation of an Obvious Tree based on Jung toolkit. It simply
 * subclasses the
 * {@link obvious.jung.data.JungObviousNetwork JungObviousNetwork} class.
 * @see obvious.jung.data.JungObviousNetwork
 * @see obvious.data.Tree
 * @author Hemery
 */
public class JungObviousTree extends JungObviousNetwork
    implements Tree<Node, Edge> {

  /**
   * Jung tree.
   */
  private edu.uci.ics.jung.graph.Tree<Node, Edge> jungTree;

  /**
   * Constructor from Jung Tree instance.
   * @param tree original Jung Tree instance
   */
  public JungObviousTree(edu.uci.ics.jung.graph.Tree<Node, Edge> tree) {
    super(tree);
    this.jungTree = tree;
  }

  /**
   * Constructor from Obvious Schema.
   * @param nodeSchema schema for the node Table
   * @param edgeSchema schema for the edge Table
   */
  public JungObviousTree(Schema nodeSchema, Schema edgeSchema) {
    this(new JungTree(nodeSchema, edgeSchema));
  }


  /**
   * Constructor from Obvious Schema.
   * @param nodeSchema schema for the node Table
   * @param edgeSchema schema for the edge Table
   * @param source column name used in edge schema to identify source node
   * @param target column name used in edge schema to identify target node
   */
  public JungObviousTree(Schema nodeSchema, Schema edgeSchema,
      String source, String target) {
    this(new JungTree(nodeSchema, edgeSchema, source, target));
  }

  /**
   * Constructor from Obvious Schema.
   * @param nodeSchema schema for the node Table
   * @param edgeSchema schema for the edge Table
   * @param node column name used in node schema to spot a node (can be null)
   * @param source column name used in edge schema to identify source node
   * @param target column name used in edge schema to identify target node
   */
  public JungObviousTree(Schema nodeSchema, Schema edgeSchema, String node,
      String source, String target) {
    this(new JungTree(nodeSchema, edgeSchema, source, target));
  }

  /**
   * Gets all the child edges of a node.
   * @param node parent node
   * @return collection of child edges
   */
  public Collection<Edge> getChildEdges(Node node) {
    return this.jungTree.getChildEdges(node);
  }

  /**
   * Gets all the child nodes of a node.
   * @param node parent node
   * @return collection of child nodes
   */
  public Collection<Node> getChildNodes(Node node) {
    return this.jungTree.getChildren(node);
  }

  /**
   * Gets depth of a node.
   * @param node node to inspect
   * @return depth of the node
   */
  public int getDepth(Node node) {
    return this.jungTree.getDepth(node);
  }

  /**
   * Gets height of the tree.
   * @return the height of the tree.
   */
  public int getHeight() {
    return this.jungTree.getHeight();
  }

  /**
   * Gets the parent edge of a node.
   * @param node child node
   * @return parent edge
   */
  public Edge getParentEdge(Node node) {
    return this.jungTree.getParentEdge(node);
  }

  /**
   * Gets the parent node of a node.
   * @param node child node
   * @return parent node
   */
  public Node getParentNode(Node node) {
    return this.jungTree.getParent(node);
  }

  /**
   * Gets the root of the tree.
   * @return root node
   */
  public Node getRoot() {
    return this.jungTree.getRoot();
  }

  /**
   * Gets the forest associated to the tree.
   * @return the forest (collection of tree) of the tree
   */
  public Collection<Tree<Node, Edge>> getTrees() {
    Collection<Tree<Node, Edge>> forest = new ArrayList<Tree<Node, Edge>>();
    forest.add(this);
    return forest;
  }

  @Override
  public Object getUnderlyingImpl(Class<?> c) {
    if (c.equals(edu.uci.ics.jung.graph.Tree.class)) {
      return jungTree;
    }
    return null;
  }

  /**
   * Simple implementation of Jung AbstractGraph.
   * Used as default Jung Graph implementation for Obvious Jung.
   * @author Pierre-Luc Hémery
   *
   */
  protected static class JungTree extends JungGraph implements
    edu.uci.ics.jung.graph.Tree<Node, Edge> {

    /**
     * Constructor for Jung Tree.
     * @param nodeSchema schema for the nodeTable
     * @param edgeSchema schema for the edgeTable
     * @param source column name used in edge schema to identify source node
     * @param target column name used in edge schema to identify target node
     */
    protected JungTree(Schema nodeSchema, Schema edgeSchema,
        String source, String target) {
      super(nodeSchema, edgeSchema, source, target);
    }

    /**
     * Constructor for Jung Graph.
     * @param nodeSchema schema for the nodeTable
     * @param edgeSchema schema for the edgeTable
     */
    public JungTree(Schema nodeSchema, Schema edgeSchema) {
      super(nodeSchema, edgeSchema);
    }

    @Override
    protected void setType(Edge edge, edu.uci.ics.jung.graph.util.EdgeType t) {
      edgeTypeMap.put(edge, edu.uci.ics.jung.graph.util.EdgeType.DIRECTED);
    }

    @Override
    public edu.uci.ics.jung.graph.util.EdgeType getEdgeType(Edge e) {
      return edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
    }

    @Override
    public edu.uci.ics.jung.graph.util.EdgeType getDefaultEdgeType() {
      return edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
    }

    /**
     * Gets the depth of a node.
     * @param node a node of the tree
     * @return depth of the node
     */
    public int getDepth(Node node) {
      if (node.equals(this.getRoot())) {
        return 0;
      } else {
        int depth = 0;
        for (int i = node.getRow(); i != getRoot().getRow() && i >= 0; ++depth,
            i = getParent(new NodeImpl(getNodeTable(), i)).getRow()) {
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
      for (Node currentNode : this.getChildren(node)) {
        int height = this.getHeight(currentNode);
        max = java.lang.Math.max(max, height);
      }
      return 1 + max;
    }

    /**
     * Get the root node.
     * @return the root Node
     */
    public Node getRoot() {
      for (Node node  : this.getVertices()) {
        if (null == this.getParent(node)) {
          return node;
        }
      } return this.getVertices().iterator().next();
    }

    /**
     * Gets the number of children for a specific node.
     * @param node a node of the graph
     * @return number of children for this node
     */
    public int getChildCount(Node node) {
      return this.getChildren(node).size();
    }

    /**
     * Gets the children edges for a node.
     * @param node a node of the graph
     * @return a collection of child edges
     */
    public Collection<Edge> getChildEdges(Node node) {
      return this.getOutEdges(node);
    }

    /**
     * Gets the children of a node.
     * @param node a node of the graph
     * @return collection of children
     */
    public Collection<Node> getChildren(Node node) {
      return this.getSuccessors(node);
    }

    /**
     * Gets the parent of a node.
     * @param node a node of the graph
     * @return a node of the graph
     */
    public Node getParent(Node node) {
      for (Node n : this.getPredecessors(node)) {
        return n;
      }
      return null;
    }

    /**
     * Gets the parent edge of a node.
     * @param node a node of the graph
     * @return the parent edge of the node
     */
    public Edge getParentEdge(Node node) {
      for (Edge e : this.getInEdges(node)) {
        return e;
      }
      return null;
    }

    /**
     * Returns a forest.
     * @return a forest
     */
    public Collection<edu.uci.ics.jung.graph.Tree<Node, Edge>> getTrees() {
      return Collections.<edu.uci.ics.jung.graph.Tree<Node, Edge>>singleton(
          this);
    }

  }
}
