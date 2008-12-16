package obvious;

import java.util.Collection;

/**
 * Class Graph
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
