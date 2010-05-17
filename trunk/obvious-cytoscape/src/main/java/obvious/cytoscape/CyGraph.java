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
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import obvious.data.Graph;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyNetworkFactory;

/**
 * An implementation of Obvious Graph based on Cytoscape toolkit.
 * @author obvious group
 *
 * @param <V> generic parameter for vertices
 * @param <E> generic paramater for edges
 */
public class CyGraph<V, E> implements Graph<V, E> {

  /**
   * A Cytoscape network.
   */
  private CyNetwork net;

  /**
   * Vertices map between V instances and CyNode instances.
   */
  private BidiMap<V, CyNode> nm;

  /**
   * Edges map between E instances and CyEdge instances.
   */
  private BidiMap<E, CyEdge> em;

  /**
   * Constructor.
   * @param factory a CyNetworkFactory
   */
  public CyGraph(CyNetworkFactory factory) {
    net = factory.getInstance();
    nm = new DualHashBidiMap<V, CyNode>();
    em = new DualHashBidiMap<E, CyEdge>();
  }

  /**
   * Gets the edges of the graph instance.
   * @return collection of edges
   */
  public Collection<E> getEdges() {
    return Collections.unmodifiableCollection(em.keySet());
  }

  /**
   * Gets the nodes of the graph instance.
   * @return collection of nodes
   */
  public Collection<V> getNodes() {
    return Collections.unmodifiableCollection(nm.keySet());
  }

  /**
   * Gets the neighborhood of a node.
   * @param node a node of the graph
   * @param type a type of edge (directed or not) to match
   * @return the neighborhood of the node as a collection
   */
  private Collection<V> getNeighbors(V node, CyEdge.Type type) {
    List<CyNode> nl = net.getNeighborList(nm.get(node), type);
    List<V> ret = new ArrayList<V>(nl.size());
    for (CyNode n : nl) {
      ret.add(nm.getKey(n));
    }
    return ret;
  }

  /**
   * Gets the neighbors node.
   * @param node a node of the graph
   * @return the neighborhood of the node as a collection
   */
  public Collection<V> getNeighbors(V node) {
      return getNeighbors(node, CyEdge.Type.ANY);
  }

  /**
   * Gets the adjacent edges of a node.
   * @param node a node of the graph
   * @param type a type of edge (directed or not) to match
   * @return adjacent edges of the node as a collection
   */
  private Collection<E> getAdjacentEdges(V node, CyEdge.Type type) {
    List<CyEdge> el = net.getAdjacentEdgeList(nm.get(node), type);
    List<E> ret = new ArrayList<E>(el.size());
    for (CyEdge e : el) {
      ret.add(em.getKey(e));
    }
    return ret;
  }

  /**
   * Gets incident edges for a spotted node.
   * @param node a node of the graph
   * @return collection of incident edges
   */
  public Collection<E> getIncidentEdges(V node) {
    return getAdjacentEdges(node, CyEdge.Type.ANY);
  }

  /**
   * Gets incident nodes for a spotted edge.
   * @param edge an edge of the graph
   * @return collection of incident nodes
   */
  public Collection<V> getIncidentNodes(E edge) {
    List<V> ret = new ArrayList<V>(2);
    CyEdge e = em.get(edge);
    ret.add(nm.getKey(e.getSource()));
    ret.add(nm.getKey(e.getTarget()));
    return ret;
  }

  /**
   * Gets for two nodes the connecting edge.
   * @param v1 a node of the graph
   * @param v2 another node of the graph
   * @return connecting edge as collection
   */
  public E getConnectingEdge(V v1, V v2) {
    List<CyEdge> el = net.getConnectingEdgeList(nm.get(v1), nm.get(v2),
        CyEdge.Type.ANY);
    if (el.size() > 0) {
      return em.getKey(el.get(0));
    } else {
      return null;
    }
  }

  /**
   * Gets connecting edges between two nodes.
   * @param v1 a node of the graph
   * @param v2 another node of the graph
   * @return collection of connecting edges
   */
  public Collection<E> getConnectingEdges(V v1, V v2) {
    List<CyEdge> el = net.getConnectingEdgeList(nm.get(v1), nm.get(v2),
        CyEdge.Type.ANY);
    List<E> ret = new ArrayList<E>(el.size());
    for (CyEdge e : el) {
      ret.add(em.getKey(e));
    }
    return ret;
  }

  /**
   * Adds a node.
   * @param node node to add
   * @return true if added
   */
  public boolean addNode(V node) {
    CyNode n = net.addNode();
    nm.put(node, n);
    return true;
  }

  /**
   * Removes a node.
   * @param node node to remove
   * @return true if removed
   */
  public boolean removeNode(V node) {
    CyNode n = nm.remove(node);
    return net.removeNode(n);
  }

  /**
   * Function not supported in this implementation.
   * @param edge an edge to add
   * @param nodes a collection of nodes linked by the edge
   * @param edgeType type of the edge (directed or not)
   * @return UnsupportedOperationException
   */
  public boolean addEdge(E edge, Collection<? extends V> nodes,
      EdgeType edgeType) {
    throw new UnsupportedOperationException("no support for hypergraphs!");
  }

  /**
   * Convenience method for multigraphs.
   * @param edge edge to add
   * @param source source node
   * @param target target node
   * @param edgeType directed or undirected edge
   * @return true if added
   */
  public boolean addEdge(E edge, V source, V target, EdgeType edgeType) {
    CyNode s = nm.get(source);
    CyNode t = nm.get(target);
    CyEdge e = net.addEdge(s, t, (edgeType == EdgeType.DIRECTED));
    em.put(edge, e);
    return true;
  }

  /**
   * Removes an edge.
   * @param edge edge to remove
   * @return true if removed
   */
  public boolean removeEdge(E edge) {
    CyEdge e = em.remove(edge);
    return net.removeEdge(e);
  }

  /**
   * Gets in-linking edges for a specific node.
   * @param node a node of the graph
   * @return collection of in-linking edges
   */
  public Collection<E> getInEdges(V node) {
    return getAdjacentEdges(node, CyEdge.Type.INCOMING);
  }

  /**
   * Gets out-linking edges for a specific node.
   * @param node a node of the graph
   * @return collection of out-linking edges
   */
  public Collection<E> getOutEdges(V node) {
    return getAdjacentEdges(node, CyEdge.Type.OUTGOING);
  }

  /**
   * Get predecessors nodes for a specific node.
   * @param node  a node of the graph
   * @return collection of nodes
   */
  public Collection<V> getPredecessors(V node) {
    return getNeighbors(node, CyEdge.Type.INCOMING);
  }

  /**
   * Get successor nodes.
   * @param node a node of the graph
   * @return collection of nodes
   */
  public Collection<V> getSuccessors(V node) {
    return getNeighbors(node, CyEdge.Type.OUTGOING);
  }

  /**
   * Gets the source of a directed edge.
   * @param directedEdge a directed edge of the graph
   * @return source node
   */
  public V getSource(E directedEdge) {
    return nm.getKey(em.get(directedEdge).getSource());
  }

  /**
   * Get the target of a directed edge.
   * @param directedEdge a directed edge of the graph
   * @return target node
   */
  public V getTarget(E directedEdge) {
    return nm.getKey(em.get(directedEdge).getTarget());
  }

  /**
   * Get the opposite node for a couple (node,edge).
   * @param node a node of the graph
   * @param edge an edge of the graph
   * @return opposite node for this couple node/edge
   */
  public V getOpposite(V node, E edge) {
    CyNode n = nm.get(node);
    CyEdge e = em.get(edge);
    CyNode op = net.getNode(
        e.getSource().getIndex() ^ e.getTarget().getIndex() ^ n.getIndex());
    return nm.getKey(op);
  }

  /**
   * Get the edge type.
   * @param edge an edge of the graph
   * @return directed or indirected {@link #EdgeType}
   */
  public EdgeType getEdgeType(E edge) {
    return (
        em.get(edge).isDirected() ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return Cytoscape graph instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    return null;
  }
}
