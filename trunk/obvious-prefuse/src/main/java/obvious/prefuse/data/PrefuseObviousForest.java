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

package obvious.prefuse.data;

import java.util.ArrayList;
import java.util.Collection;

import obvious.data.Edge;
import obvious.data.Forest;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;

/**
 * Implementation of an Obvious Network based on Prefuse toolkit.
 * @author Hemery
 *
 */
public class PrefuseObviousForest extends PrefuseObviousNetwork
    implements Forest<Node, Edge> {

  /**
   * Obvious node schema.
   */
  private Schema nodeSchema;

  /**
   * Obvious edge schema.
   */
  private Schema edgeSchema;

  /**
   * Source column.
   */
  private String source;

  /**
   * Target column.
   */
  private String target;

  /**
   * Node id column.
   */
  private String nodeId;

  /**
   * Constructor from obvious schemas and extra parameters.
   * @param nodeSchema original schema for the nodes
   * @param edgeSchema original schema for the edges
   * @param directed boolean indicating if the graph is directed or not
   * @param nodeId nodeKey data field used to uniquely identify a node. If this
   * field is null, the node table row numbers will be used
   * @param sourceId data field used to denote the source node in an edge
   * table
   * @param targetId data field used to denote the target node in an edge
   * table
   */
  public PrefuseObviousForest(Schema nodeSchema, Schema edgeSchema,
      boolean directed, String nodeId, String sourceId, String targetId) {
    super(nodeSchema, edgeSchema, directed, nodeId, sourceId, targetId);
    this.nodeSchema = nodeSchema;
    this.edgeSchema = edgeSchema;
    this.target = targetId;
    this.source = sourceId;
    this.nodeId = nodeId;
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
      Tree<Node, Edge> tree = new PrefuseObviousTree(nodeSchema, edgeSchema,
          nodeId, source, target);
      tree.addNode(node);
      populateNetwork(tree, node);
      trees.add(tree);
    }
    return trees;
  }

  /**
   * Internal method.
   * @param tree a Jung tree
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
