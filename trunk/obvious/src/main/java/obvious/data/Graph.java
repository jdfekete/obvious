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
 * Class Graph.
 *
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
// PROPOSED Graph interface

//Open questions:
//(1) Generics? - YES!
//(2) Node and edge types?
//(3) Explicit Table-related methods?

public interface Graph<V, E> extends Data
{
	enum EdgeType {
		DIRECTED,
		UNDIRECTED
	}
	
    Collection<E> getEdges();
    Collection<V> getNodes();
    
    Collection<V> getNeighbors(V node);
    Collection<E> getIncidentEdges(V node);
    Collection<V> getIncidentNodes(E edge);

    // TODO: do we want this method or to let the user call getConnectingEdges(v1, v2).iterator().next()?
    E getConnectingEdge(V v1, V v2);
    Collection<E> getConnectingEdges(V v1, V v2);

    boolean addNode(V node);
    boolean removeNode(V node);

    /**
     * Add a hyperedge. 
     * @param edge
     * @param nodes
     * @param edgeType
     * @return
     */
    boolean addEdge(E edge, Collection<? extends V> nodes, EdgeType edgeType);
    
    /**
     * Convenience method for multigraphs.
     * @param edge
     * @param source
     * @param target
     * @param edgeType
     * @return
     */
    boolean addEdge(E edge, V source, V target, EdgeType edgeType);
    boolean removeEdge(E edge);

    Collection<E> getInEdges(V node);
    Collection<E> getOutEdges(V node);

    Collection<V> getPredecessors(V node);
    Collection<V> getSuccessors(V node);
    
    V getSource(E directed_edge);
    V getTarget(E directed_edge);
    
    V getOpposite(V node, E edge); 

    EdgeType getEdgeType(E edge);
}

