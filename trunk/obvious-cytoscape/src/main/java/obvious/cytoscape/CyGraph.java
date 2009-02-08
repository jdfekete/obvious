
package obvious.cytoscape;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import obvious.Graph;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyNetworkFactory;

public class CyGraph<V,E> implements Graph<V,E> {

	private CyNetwork net;
	private BidiMap<V,CyNode> nm;
	private BidiMap<E,CyEdge> em;

	public CyGraph(CyNetworkFactory factory) {
		net = factory.getInstance();	
		nm = new DualHashBidiMap<V,CyNode>();
		em = new DualHashBidiMap<E,CyEdge>();
	}

    public Collection<E> getEdges() {
		return Collections.unmodifiableCollection( em.keySet() );
	}

    public Collection<V> getNodes() {
		return Collections.unmodifiableCollection( nm.keySet() );
	}
    
    private Collection<V> getNeighbors(V node, CyEdge.Type type) {
		List<CyNode> nl = net.getNeighborList(nm.get(node),type); 
		List<V> ret = new ArrayList<V>(nl.size());
		for ( CyNode n : nl ) 
			ret.add( nm.getKey(n) );

		return ret;
	}

    public Collection<V> getNeighbors(V node) {
		return getNeighbors(node,CyEdge.Type.ANY);
	}

    private Collection<E> getAdjacentEdges(V node, CyEdge.Type type) {
		List<CyEdge> el = net.getAdjacentEdgeList(nm.get(node),type); 
		List<E> ret = new ArrayList<E>(el.size());
		for ( CyEdge e : el ) 
			ret.add( em.getKey(e) );

		return ret;
	}

    public Collection<E> getIncidentEdges(V node) {
		return getAdjacentEdges(node,CyEdge.Type.ANY);
	}

    public Collection<V> getIncidentNodes(E edge) {
		List<V> ret = new ArrayList<V>(2);
		CyEdge e = em.get(edge);
		ret.add( nm.getKey( e.getSource() ) ); 
		ret.add( nm.getKey( e.getTarget() ) ); 
	
		return ret;
	}


    public E getConnectingEdge(V v1, V v2) {
		List<CyEdge> el = net.getConnectingEdgeList(nm.get(v1),nm.get(v2),CyEdge.Type.ANY); 
		if ( el.size() > 0 )
			return em.getKey( el.get(0) );
		else
			return null;
	}

    public Collection<E> getConnectingEdges(V v1, V v2) {
		List<CyEdge> el = net.getConnectingEdgeList(nm.get(v1),nm.get(v2),CyEdge.Type.ANY); 
		List<E> ret = new ArrayList<E>(el.size());
		for ( CyEdge e : el ) 
			ret.add( em.getKey(e) );

		return ret;
	}

    public boolean addNode(V node) {
		CyNode n = net.addNode();
		nm.put(node,n);
		return true;
	}

    public boolean removeNode(V node) {
		CyNode n = nm.remove(node);
		return net.removeNode(n);
	}

    public boolean addEdge(E edge, Collection<? extends V> nodes, EdgeType edgeType) {
		throw new UnsupportedOperationException("no support for hypergraphs!");
	}

    public boolean addEdge(E edge, V source, V target, EdgeType edgeType) {
		CyNode s = nm.get(source);
		CyNode t = nm.get(target);
		CyEdge e = net.addEdge(s,t, (edgeType == EdgeType.DIRECTED));
		em.put(edge,e);
		return true;
	}

    public boolean removeEdge(E edge) {
		CyEdge e = em.remove(edge);
		return net.removeEdge(e);
	}


    public Collection<E> getInEdges(V node) {
		return getAdjacentEdges(node,CyEdge.Type.INCOMING);
	}

    public Collection<E> getOutEdges(V node) {
		return getAdjacentEdges(node,CyEdge.Type.OUTGOING);
	}

    public Collection<V> getPredecessors(V node) {
		return getNeighbors(node,CyEdge.Type.INCOMING);
	}

    public Collection<V> getSuccessors(V node) {
		return getNeighbors(node,CyEdge.Type.OUTGOING);
	}
    
    public V getSource(E directed_edge) {
		return nm.getKey( em.get(directed_edge).getSource() );
	}

    public V getTarget(E directed_edge) {
		return nm.getKey( em.get(directed_edge).getTarget() );
	}

    public V getOpposite(V node, E edge) {
		CyNode n = nm.get(node);
		CyEdge e = em.get(edge);
		CyNode op = net.getNode(e.getSource().getIndex() ^ e.getTarget().getIndex() ^ n.getIndex());
		return nm.getKey( op );
	}

    public EdgeType getEdgeType(E edge) {
		return (em.get(edge).isDirected() ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
	}
}
