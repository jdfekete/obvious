/*
* Copyright (c) 2011, INRIA
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

import java.util.ArrayList;
import java.util.Collection;

import obvious.data.Edge;
import obvious.data.Forest;
import obvious.data.Node;
import obvious.data.Tree;

import org.cytoscape.model.CyNetworkFactory;

/**
 * Implementation of the Obvious Forest interface based on the Cytoscape
 * toolkit.
 * @author Hemery
 *
 */
public class CyForest extends CyNetwork implements Forest<Node, Edge> {

  /**
   * A Cytoscape data factory used by the instance.
   */
  private CyNetworkFactory fact;

  /**
   * Constructor.
   * @param factory a data factory implementation for the Cytoscape
   * implementation of Jung
   */
  public CyForest(CyNetworkFactory factory) {
    super(factory);
    this.fact = factory;
  }

  @Override
  public Collection<Tree<Node, Edge>> getTrees() {
    Collection<Tree<Node, Edge>> trees = new ArrayList<Tree<Node, Edge>>();
    Collection<Node> rootNodes = new ArrayList<Node>();
    for (Node node : getNodes()) {
      if (getPredecessors(node) == null || getPredecessors(node).size() == 0) {
        rootNodes.add(node);
      }
    }
    for (Node node : rootNodes) {
      Tree<Node, Edge> tree = new CyTree(fact);
      tree.addNode(node);
      populateNetwork(tree, node);
      trees.add(tree);
    }
    return trees;
  }

  /**
   * Internal method.
   * @param tree a Cytoscape tree
   * @param node an Obvious node
   */
  private void populateNetwork(Tree<Node, Edge> tree, Node node) {
    if (this.getSuccessors(node) != null
        || this.getSuccessors(node).size() > 0) {
      for (Node otherNode : this.getSuccessors(node)) {
        tree.addNode(otherNode);
        tree.addEdge(getConnectingEdge(node, otherNode), node, otherNode,
            EdgeType.DIRECTED);
        populateNetwork(tree, otherNode);
      }
    }
  }

}
