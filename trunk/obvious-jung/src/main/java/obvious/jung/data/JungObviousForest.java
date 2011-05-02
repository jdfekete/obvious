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


package obvious.jung.data;

import java.util.ArrayList;
import java.util.Collection;

import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.util.Pair;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Forest;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.jung.data.JungObviousNetwork.JungGraph;


/**
 * Implementation of an Obvious {@link obvious.data.Forest Forest} based on
 * Jung toolkit. This implementation wraps an existing Jung Forest object.
 * This class directly binds Jung and Obvious methods since those two toolkits
 * share the same design for graphs, trees and forests.
 * @see obvious.data.Forest
 * @author Hemery
 *
 */
public class JungObviousForest implements Forest<Node, Edge> {

  /**
   * Jung tree.
   */
  private edu.uci.ics.jung.graph.Forest<Node, Edge> jungForest;

  /**
   * Constructor.
   * @param forest a Jung forest
   */
  public JungObviousForest(edu.uci.ics.jung.graph.Forest<Node, Edge> forest) {
    this.jungForest = forest;
  }

  /**
   * Constructor from Obvious Schema.
   * @param nodeSchema schema for the node Table
   * @param edgeSchema schema for the edge Table
   * @param source column name used in edge schema to identify source node
   * @param target column name used in edge schema to identify target node
   */
  public JungObviousForest(Schema nodeSchema, Schema edgeSchema, String source,
      String target) {
    this.jungForest = new JungForest(nodeSchema, edgeSchema, null, source,
        target);
  }

  /**
   * Constructor from Obvious Schema.
   * @param nodeSchema schema for the node Table
   * @param edgeSchema schema for the edge Table
   * @param node column name used in node schema to spot a node (can be null)
   * @param source column name used in edge schema to identify source node
   * @param target column name used in edge schema to identify target node
   */
  public JungObviousForest(Schema nodeSchema, Schema edgeSchema, String node,
      String source, String target) {
    this.jungForest = new JungForest(nodeSchema, edgeSchema, node, source,
        target);
  }

  @Override
  public Collection<Tree<Node, Edge>> getTrees() {
    Collection<Tree<Node, Edge>> trees = new ArrayList<Tree<Node, Edge>>();
    for (edu.uci.ics.jung.graph.Tree<Node, Edge> t : jungForest.getTrees()) {
      trees.add(new JungObviousTree(t));
    }
    return trees;
  }

  @Override
  public boolean addEdge(Edge edge, Collection<? extends Node> nodes,
      obvious.data.Graph.EdgeType edgeType) {
    try {
      edu.uci.ics.jung.graph.util.EdgeType type =
        edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED;
      if (edgeType == obvious.data.Graph.EdgeType.DIRECTED) {
        type = edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
      }
      return this.jungForest.addEdge(edge, nodes, type);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  @Override
  public boolean addEdge(Edge edge, Node source, Node target,
      obvious.data.Graph.EdgeType edgeType) {
    try {
      edu.uci.ics.jung.graph.util.EdgeType type =
        edu.uci.ics.jung.graph.util.EdgeType.UNDIRECTED;
      if (edgeType == obvious.data.Graph.EdgeType.DIRECTED) {
        type = edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
      }
      Pair<Node> nodes = new Pair<Node>(source, target);
      return this.jungForest.addEdge(edge, nodes, type);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  @Override
  public boolean addNode(Node node) {
    return jungForest.addVertex(node);
  }

  @Override
  public Edge getConnectingEdge(Node v1, Node v2) {
    return jungForest.findEdge(v1, v2);
  }

  @Override
  public Collection<Edge> getConnectingEdges(Node v1, Node v2) {
    return jungForest.findEdgeSet(v1, v2);
  }

  @Override
  public obvious.data.Graph.EdgeType getEdgeType(Edge edge) {
    edu.uci.ics.jung.graph.util.EdgeType type = jungForest.getEdgeType(edge);
    if (type == edu.uci.ics.jung.graph.util.EdgeType.DIRECTED) {
      return obvious.data.Graph.EdgeType.DIRECTED;
    } else {
      return obvious.data.Graph.EdgeType.UNDIRECTED;
    }
  }

  @Override
  public Collection<Edge> getEdges() {
    return jungForest.getEdges();
  }

  @Override
  public Collection<Edge> getInEdges(Node node) {
    return jungForest.getInEdges(node);
  }

  @Override
  public Collection<Edge> getIncidentEdges(Node node) {
    return jungForest.getIncidentEdges(node);
  }

  @Override
  public Collection<Node> getIncidentNodes(Edge edge) {
    return jungForest.getIncidentVertices(edge);
  }

  @Override
  public Collection<Node> getNeighbors(Node node) {
    return jungForest.getNeighbors(node);
  }

  @Override
  public Collection<Node> getNodes() {
    return jungForest.getVertices();
  }

  @Override
  public Node getOpposite(Node node, Edge edge) {
    return jungForest.getOpposite(node, edge);
  }

  @Override
  public Collection<Edge> getOutEdges(Node node) {
    return jungForest.getOutEdges(node);
  }

  @Override
  public Collection<Node> getPredecessors(Node node) {
    return jungForest.getPredecessors(node);
  }

  @Override
  public Node getSource(Edge directedEdge) {
    return jungForest.getSource(directedEdge);
  }

  @Override
  public Collection<Node> getSuccessors(Node node) {
    return jungForest.getSuccessors(node);
  }

  @Override
  public Node getTarget(Edge directedEdge) {
    return jungForest.getDest(directedEdge);
  }

  @Override
  public boolean removeEdge(Edge edge) {
    return jungForest.removeEdge(edge);
  }

  @Override
  public boolean removeNode(Node node) {
    return jungForest.removeVertex(node);
  }

  @Override
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(edu.uci.ics.jung.graph.Forest.class)
        || type.equals(edu.uci.ics.jung.graph.Tree.class)) {
      return jungForest;
    }
    return null;
  }

  /**
   * Simple implementation of Jung Forest.
   * Used as default Jung Forest implementation for Obvious Jung.
   * @author Hemery
   */
  protected  class JungForest extends JungGraph implements
      edu.uci.ics.jung.graph.Forest<Node, Edge> {

    /**
     * Constructor.
     * @param nodeSchema schema for nodes
     * @param edgeSchema schema for edges
     * @param node node column
     * @param source source node colum for edges
     * @param target target node column for nodes
     */
    protected JungForest(Schema nodeSchema, Schema edgeSchema, String node,
        String source, String target) {
      super(nodeSchema, edgeSchema, node, source, target);
    }

    @Override
    public int getChildCount(Node vertex) {
      return super.getSuccessorCount(vertex);
    }

    @Override
    public Collection<Edge> getChildEdges(Node vertex) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Collection<Node> getChildren(Node vertex) {
      return super.getSuccessors(vertex);
    }

    @Override
    public Node getParent(Node vertex) {
      if (super.getPredecessors(vertex).iterator().hasNext()) {
        return super.getPredecessors(vertex).iterator().next();
      }
      return null;
    }

    @Override
    public Edge getParentEdge(Node vertex) {
      return super.getInEdges(vertex).iterator().next();
    }

    @Override
    public Collection<edu.uci.ics.jung.graph.Tree<Node, Edge>> getTrees() {
      Collection<edu.uci.ics.jung.graph.Tree<Node, Edge>> trees =
        new ArrayList<edu.uci.ics.jung.graph.Tree<Node, Edge>>();
      Collection<Node> rootNodes = new ArrayList<Node>();
      for (Node node : getVertices()) {
        if (getParent(node) == null) {
          rootNodes.add(node);
        }
      }
      for (Node node : rootNodes) {
        edu.uci.ics.jung.graph.Tree<Node, Edge> tree = new
            DelegateTree<Node, Edge>();
        tree.addVertex(node);
        populateNetwork(tree, node);
        trees.add(tree);
      }
      return trees;
    }

    /**
     * Internal method.
     * @param tree a Jung tree
     * @param baseNode an Obvious node
     */
    private void populateNetwork(edu.uci.ics.jung.graph.Tree<Node, Edge> tree,
        Node baseNode) {
      if (getChildren(baseNode) != null || getChildren(baseNode).size() > 0) {
        for (Node node : getChildren(baseNode)) {
          tree.addEdge(getConnectingEdge(baseNode, node), baseNode, node,
              edu.uci.ics.jung.graph.util.EdgeType.DIRECTED);
          populateNetwork(tree, node);
        }
      }
    }
  }

}
