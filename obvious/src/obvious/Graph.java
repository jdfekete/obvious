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

public interface Graph<V, E, T> extends Data
{
    Collection<E> getEdges();
    Collection<V> getNodes();
    
    Collection<V> getNeighbors(V node);
    Collection<E> getIncidentEdges(V node);
    Collection<V> getIncidentVertices(E edge);

    // TODO: do we want this method or to let the user call getConnectingEdges(v1, v2).iterator().next()?
    E getConnectingEdge(V v1, V v2);
    Collection<E> getConnectingEdges(V v1, V v2);

    boolean addVertex(V node);
    boolean removeVertex(V node);
    boolean addEdge(E edge, Collection<? extends V> nodes, T edgeType);
    boolean removeEdge(E edge);

    Collection<E> getInEdges(V node);
    Collection<E> getOutEdges(V node);

    Collection<V> getPredecessors(V node);
    Collection<V> getSuccessors(V node);
    
    V getSource(E directed_edge);
    V getDest(E directed_edge);
    
    V getOpposite(V node, E edge); 

   T getEdgeType(E edge);
}
