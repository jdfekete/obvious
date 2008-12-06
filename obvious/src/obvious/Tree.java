package obvious;

import java.util.Collection;

/**
 * Class Tree
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Tree<V,E,T> extends Forest<V,E,T>
{
    public int getDepth(V node);
    public int getHeight();
    
    public V getRoot();
    
    public V getParent(V node);
    public E getParentEdge(V node);
    
    public Collection<V> getChildren(V node);
    public Collection<E> getChildEdges(V node);
//    public int getChildCount(V node);
} 
