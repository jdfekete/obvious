package obvious;

import java.util.Collection;

/**
 * Class Tree
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Tree<V,E> extends Forest<V,E>
{
    public int getDepth(V node);
    public int getHeight();
    
    public V getRoot();
    
    public V getParentNode(V node);
    public E getParentEdge(V node);
    
    public Collection<V> getChildNodes(V node);
    public Collection<E> getChildEdges(V node);
} 
