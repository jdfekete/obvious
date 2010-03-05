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

package obvious.cytoscape;

import java.util.Collection;

import org.cytoscape.model.CyNetworkFactory;

import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Tree;

/**
 * Implementation of Obvious Tree based on Cytoscape toolkit.
 * @author Pierre-Luc Hemery
 *
 */
public class CyTree extends CyNetwork implements Tree<Node, Edge> {

  /**
   * Constructor.
   * @param factory a CytoScape Network factory.
   */
  public CyTree(CyNetworkFactory factory) {
    super(factory);
  }

  /**
   * Gets all the child edges of a node.
   * @param node a node of the tree
   * @return collection of children edges
   */
  public Collection<Edge> getChildEdges(Node node) {
    return this.getOutEdges(node);
  }

  /**
   * Gets all the child nodes of a node.
   * @param node a node of the tree
   * @return collection of child nodes
   */
  public Collection<Node> getChildNodes(Node node) {
    System.out.println("Node : " + node);
    return this.getSuccessors(node);
  }

  /**
   * Gets depth of a node.
   * @param node a node of the tree
   * @return depth of the node
   */
  public int getDepth(Node node) {
    int depth = 0;
    Node currentNode = node;
    while (!currentNode.equals(this.getRoot()) && currentNode != null) {
      depth++;
      currentNode = this.getParentNode(currentNode);
    }
    return depth;
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
   * Gets the parent edge of a node.
   * @param node a node of the tree
   * @return parent edge
   */
  public Edge getParentEdge(Node node) {
    return this.getInEdges(node).iterator().next();
  }

  /**
   * Gets the parent node of a node.
   * @param node a node of the tree
   * @return parent node
   */
  public Node getParentNode(Node node) {
    if (node != this.getRoot()) {
      return this.getPredecessors(node).iterator().next();
    } else {
      return null;
    }
  }

  /**
   * Gets the root of the tree.
   * @return root node
   */
  public Node getRoot() {
    Node root = null;
    for (Node node : this.getNodes()) {
      if (this.getPredecessors(node).size() == 0) {
        root = node;
        break;
      }
    }
    return root;
  }

  /**
   * Gets the forest associated to the tree.
   * @return the forest (collection of tree) of the tree
   */
  public Collection<Tree<Node, Edge>> getTrees() {
    // TODO Auto-generated method stub
    return null;
  }

}
