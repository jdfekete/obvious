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

package obviousx.wrappers.jung;

import java.util.ArrayList;
import java.util.Collection;

import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import obvious.data.Edge;
import obvious.data.Node;

/**
 * Wrapper for obvious table to jung tree.
 * @author Hemery
 *
 */
public class WrapToJungTree implements edu.uci.ics.jung.graph.Tree<Node, Edge> {

  /**
   * Wrapped obvious Tree.
   */
  private obvious.data.Tree<Node, Edge> tree;

  /**
   * Constructor.
   * @param t a tree instance
   */
  public WrapToJungTree(obvious.data.Tree<Node, Edge> t) {
    this.tree  = t;
  }

  @Override
  public int getDepth(Node vertex) {
    return tree.getDepth(vertex);
  }

  @Override
  public int getHeight() {
    return tree.getHeight();
  }

  @Override
  public Node getRoot() {
    return tree.getRoot();
  }

  @Override
  public int getChildCount(Node vertex) {
    return tree.getChildNodes(vertex).size();
  }

  @Override
  public Collection<Edge> getChildEdges(Node vertex) {
    return tree.getChildEdges(vertex);
  }

  @Override
  public Collection<Node> getChildren(Node vertex) {
    return tree.getChildNodes(vertex);
  }

  @Override
  public Node getParent(Node vertex) {
    return tree.getParentNode(vertex);
  }

  @Override
  public Edge getParentEdge(Node vertex) {
    return tree.getParentEdge(vertex);
  }

  @Override
  public Collection<Tree<Node, Edge>> getTrees() {
     Collection<Tree<Node, Edge>> trees = new ArrayList<Tree<Node, Edge>>();
     for (obvious.data.Tree<Node, Edge> t : tree.getTrees()) {
       trees.add(new WrapToJungTree(t));
     }
     return trees;
  }

  @Override
  public boolean addEdge(Edge e, Node v1, Node v2) {
    return tree.addEdge(e, v1, v2, obvious.data.Graph.EdgeType.UNDIRECTED);
  }

  @Override
  public boolean addEdge(Edge e, Node v1, Node v2, EdgeType edgeType) {
    if (edgeType.equals(EdgeType.UNDIRECTED)) {
      return this.addEdge(e, v1, v2);
    } else {
      return tree.addEdge(e, v1, v2, obvious.data.Graph.EdgeType.DIRECTED);
    }
  }

  @Override
  public Node getDest(Edge directedEdge) {
    return tree.getTarget(directedEdge);
  }

  @Override
  public Pair<Node> getEndpoints(Edge edge) {
    return new Pair<Node>(getSource(edge), getDest(edge));
  }

  @Override
  public Collection<Edge> getInEdges(Node vertex) {
    return tree.getInEdges(vertex);
  }

  @Override
  public Node getOpposite(Node vertex, Edge edge) {
    return tree.getOpposite(vertex, edge);
  }

  @Override
  public Collection<Edge> getOutEdges(Node vertex) {
    return tree.getOutEdges(vertex);
  }

  @Override
  public int getPredecessorCount(Node vertex) {
    return getPredecessors(vertex).size();
  }

  @Override
  public Collection<Node> getPredecessors(Node vertex) {
    return tree.getPredecessors(vertex);
  }

  @Override
  public Node getSource(Edge directedEdge) {
    return tree.getSource(directedEdge);
  }

  @Override
  public int getSuccessorCount(Node vertex) {
    return getSuccessors(vertex).size();
  }

  @Override
  public Collection<Node> getSuccessors(Node vertex) {
    return tree.getSuccessors(vertex);
  }

  @Override
  public int inDegree(Node vertex) {
    return getInEdges(vertex).size();
  }

  @Override
  public boolean isDest(Node vertex, Edge edge) {
    return getDest(edge).equals(vertex);
  }

  @Override
  public boolean isPredecessor(Node v1, Node v2) {
    return getPredecessors(v1).contains(v2);
  }

  @Override
  public boolean isSource(Node vertex, Edge edge) {
    return getSource(edge).equals(vertex);
  }

  @Override
  public boolean isSuccessor(Node v1, Node v2) {
    return getSuccessors(v1).contains(v2);
  }

  @Override
  public int outDegree(Node vertex) {
    return getOutEdges(vertex).size();
  }

  @Override
  public boolean addEdge(Edge edge, Collection<? extends Node> vertices) {
    return addEdge(edge, vertices, getDefaultEdgeType());
  }

  @Override
  public boolean addEdge(Edge edge, Collection<? extends Node> vertices,
      EdgeType edgeType) {
    if (edgeType.equals(EdgeType.DIRECTED)) {
      return tree.addEdge(
          edge, vertices, obvious.data.Graph.EdgeType.DIRECTED);
    } else {
      return tree.addEdge(
          edge, vertices, obvious.data.Graph.EdgeType.UNDIRECTED);
    }
  }

  @Override
  public boolean addVertex(Node vertex) {
    return tree.addNode(vertex);
  }

  @Override
  public boolean containsEdge(Edge edge) {
    return getEdges().contains(edge);
  }

  @Override
  public boolean containsVertex(Node vertex) {
    return getVertices().contains(vertex);
  }

  @Override
  public int degree(Node vertex) {
    return getIncidentEdges(vertex).size();
  }

  @Override
  public Edge findEdge(Node v1, Node v2) {
    return tree.getConnectingEdge(v1, v2);
  }

  @Override
  public Collection<Edge> findEdgeSet(Node v1, Node v2) {
    return tree.getConnectingEdges(v1, v2);
  }

  @Override
  public EdgeType getDefaultEdgeType() {
    return EdgeType.UNDIRECTED;
  }

  @Override
  public int getEdgeCount() {
    return getEdges().size();
  }

  @Override
  public int getEdgeCount(EdgeType edgeType) {
    return getEdges(edgeType).size();
  }

  @Override
  public EdgeType getEdgeType(Edge edge) {
    if (tree.getEdgeType(edge).equals(
        obvious.data.Graph.EdgeType.DIRECTED)) {
      return EdgeType.DIRECTED;
    } else {
      return EdgeType.UNDIRECTED;
    }
  }

  @Override
  public Collection<Edge> getEdges() {
    return tree.getEdges();
  }

  @Override
  public Collection<Edge> getEdges(EdgeType edgeType) {
    Collection<Edge> typedEdges = new ArrayList<Edge>();
    for (Edge edge : getEdges()) {
      if (getEdgeType(edge).equals(edgeType)) {
        typedEdges.add(edge);
      }
    }
    return typedEdges;
  }

  @Override
  public int getIncidentCount(Edge edge) {
    return getIncidentVertices(edge).size();
  }

  @Override
  public Collection<Edge> getIncidentEdges(Node vertex) {
    return getIncidentEdges(vertex);
  }

  @Override
  public Collection<Node> getIncidentVertices(Edge edge) {
    return tree.getIncidentNodes(edge);
  }

  @Override
  public int getNeighborCount(Node vertex) {
    return getNeighbors(vertex).size();
  }

  @Override
  public Collection<Node> getNeighbors(Node vertex) {
    return tree.getNeighbors(vertex);
  }

  @Override
  public int getVertexCount() {
    return getVertices().size();
  }

  @Override
  public Collection<Node> getVertices() {
    return tree.getNodes();
  }

  @Override
  public boolean isIncident(Node vertex, Edge edge) {
    return getIncidentEdges(vertex).contains(edge);
  }

  @Override
  public boolean isNeighbor(Node v1, Node v2) {
    return getNeighbors(v1).contains(v2);
  }

  @Override
  public boolean removeEdge(Edge edge) {
    return tree.removeEdge(edge);
  }

  @Override
  public boolean removeVertex(Node vertex) {
    return tree.removeNode(vertex);
  }

}
