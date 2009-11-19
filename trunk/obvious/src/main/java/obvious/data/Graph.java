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

package obvious.data;

import java.util.Collection;

/**
 * Interface Graph.
 * @param <V> Vertex object
 * @param <E> Edge object
 *
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
// PROPOSED Graph interface

//Open questions:
//(1) Generics? - YES!
//(2) Node and edge types?
//(3) Explicit Table-related methods?

public interface Graph<V, E> extends Data {


  /**
   * Describes two families of graphs.
   * @author obvious
   *
   */
  enum EdgeType {
    /**
     * Directed graph.
     */
    DIRECTED,
    /**
     * Undirected graph.
     */
    UNDIRECTED
  }

    /**
     * Gets the edges of the graph instance.
     * @return collection of edges
     */
    Collection<E> getEdges();

    /**
     * Gets the nodes of the graph instance.
     * @return collection of nodes
     */
    Collection<V> getNodes();

    /**
     * Gets the neighbors node (exact definition?).
     * @param node central for the neighborhood
     * @return collection of nodes that are neighbors
     */
    Collection<V> getNeighbors(V node);

    /**
     * Gets incident edges for a spotted node.
     * @param node spotted
     * @return collection of incident edges
     */
    Collection<E> getIncidentEdges(V node);

    /**
     * Gets incident nodes for a spotted edge.
     * @param edge spotted
     * @return collection of incident nodes
     */
    Collection<V> getIncidentNodes(E edge);

    // TODO: do we want this method or to let the user call getConnectingEdges(v1, v2).iterator().next()?

    /**
     * Gets for two nodes the connecting edge.
     * @param v1 first node
     * @param v2 second node
     * @return connecting edge
     */
    E getConnectingEdge(V v1, V v2);

    /**
     * Gets connecting edges between two nodes.
     * @param v1 first node
     * @param v2 second node
     * @return collection of connecting edges
     */
    Collection<E> getConnectingEdges(V v1, V v2);

    /**
     * Adds a node.
     * @param node to add
     * @return boolean status success
     */
    boolean addNode(V node);

    /**
     * Removes a node.
     * @param node to remove
     * @return boolean status success
     */
    boolean removeNode(V node);

    /**
     * Adds a hyperedge.
     * @param edge to add
     * @param nodes concerned by add
     * @param edgeType directed or not
     * @return boolean status success
     */
    boolean addEdge(E edge, Collection<? extends V> nodes, EdgeType edgeType);

    /**
     * Convenience method for multigraphs.
     * @param edge to add
     * @param source node
     * @param target node
     * @param edgeType directed or not
     * @return boolean status success
     */
    boolean addEdge(E edge, V source, V target, EdgeType edgeType);

    /**
     * Removes an edge.
     * @param edge to remove
     * @return boolean status success
     */
    boolean removeEdge(E edge);

    /**
     * Get in edges.
     * @param node spotted
     * @return collection of in edges
     */
    Collection<E> getInEdges(V node);

    /**
     * Get out edges.
     * @param node spotted
     * @return collection of out edges
     */
    Collection<E> getOutEdges(V node);

    /**
     * Get predecessors nodes.
     * @param node spotted
     * @return collection of nodes
     */
    Collection<V> getPredecessors(V node);

    /**
     * Get successor nodes.
     * @param node spotted
     * @return collection of node
     */
    Collection<V> getSuccessors(V node);

    /**
     * Get the source of a directed edge.
     * @param directedEdge spotted
     * @return source node
     */
    V getSource(E directedEdge);

    /**
     * Get the target of a directed edge.
     * @param directedEdge spotted
     * @return target node
     */
    V getTarget(E directedEdge);

    /**
     * Get the opposite node for a couple (node,edge).
     * @param node spotted
     * @param edge spotted
     * @return opposite node
     */
    V getOpposite(V node, E edge);

    /**
     * Get the edge type.
     * @param edge spotted
     * @return directed or indirected
     */
    EdgeType getEdgeType(E edge);
}

