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

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;

/**
 * Wrapper for obvious table to jung graph.
 * @author Hemery
 *
 */
public class WrapToJungGraph implements
    edu.uci.ics.jung.graph.Graph<Node, Edge> {

  /**
   * Wrapped obvious network.
   */
  private Network network;

  /**
   * Constructor.
   * @param inNetwork an obvious network
   */
  public WrapToJungGraph(Network inNetwork) {
    this.network = inNetwork;
  }

  @Override
  public boolean addEdge(Edge e, Node v1, Node v2) {
    return network.addEdge(e, v1, v2, obvious.data.Graph.EdgeType.UNDIRECTED);
  }

  @Override
  public boolean addEdge(Edge e, Node v1, Node v2, EdgeType edgeType) {
    if (edgeType.equals(EdgeType.UNDIRECTED)) {
      return this.addEdge(e, v1, v2);
    } else {
      return network.addEdge(e, v1, v2, obvious.data.Graph.EdgeType.DIRECTED);
    }
  }

  @Override
  public Node getDest(Edge directedEdge) {
    return network.getTarget(directedEdge);
  }

  @Override
  public Pair<Node> getEndpoints(Edge edge) {
    Node source = null;
    Node target = null;
    int count = 0;
    for (Node node : getIncidentVertices(edge)) {
      if (count == 0) {
        source = node;
        count++;
      } else {
        target = node;
        break;
      }
    }
    return new Pair<Node>(source, target);
  }

  @Override
  public Collection<Edge> getInEdges(Node vertex) {
    return network.getInEdges(vertex);
  }

  @Override
  public Node getOpposite(Node vertex, Edge edge) {
    return network.getOpposite(vertex, edge);
  }

  @Override
  public Collection<Edge> getOutEdges(Node vertex) {
    return network.getOutEdges(vertex);
  }

  @Override
  public int getPredecessorCount(Node vertex) {
    return getPredecessors(vertex).size();
  }

  @Override
  public Collection<Node> getPredecessors(Node vertex) {
    return network.getPredecessors(vertex);
  }

  @Override
  public Node getSource(Edge directedEdge) {
    return network.getSource(directedEdge);
  }

  @Override
  public int getSuccessorCount(Node vertex) {
    return getSuccessors(vertex).size();
  }

  @Override
  public Collection<Node> getSuccessors(Node vertex) {
    return network.getSuccessors(vertex);
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
      return network.addEdge(
          edge, vertices, obvious.data.Graph.EdgeType.DIRECTED);
    } else {
      return network.addEdge(
          edge, vertices, obvious.data.Graph.EdgeType.UNDIRECTED);
    }
  }

  @Override
  public boolean addVertex(Node vertex) {
    return network.addNode(vertex);
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
    return network.getConnectingEdge(v1, v2);
  }

  @Override
  public Collection<Edge> findEdgeSet(Node v1, Node v2) {
    return network.getConnectingEdges(v1, v2);
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
    if (network.getEdgeType(edge).equals(
        obvious.data.Graph.EdgeType.DIRECTED)) {
      return EdgeType.DIRECTED;
    } else {
      return EdgeType.UNDIRECTED;
    }
  }

  @Override
  public Collection<Edge> getEdges() {
    return network.getEdges();
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
    return network.getIncidentEdges(vertex);
  }

  @Override
  public Collection<Node> getIncidentVertices(Edge edge) {
    return network.getIncidentNodes(edge);
  }

  @Override
  public int getNeighborCount(Node vertex) {
    return getNeighbors(vertex).size();
  }

  @Override
  public Collection<Node> getNeighbors(Node vertex) {
    return network.getNeighbors(vertex);
  }

  @Override
  public int getVertexCount() {
    return getVertices().size();
  }

  @Override
  public Collection<Node> getVertices() {
    return network.getNodes();
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
    return network.removeEdge(edge);
  }

  @Override
  public boolean removeVertex(Node vertex) {
    return network.removeNode(vertex);
  }
}
