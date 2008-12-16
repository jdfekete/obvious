package obvious;

import java.util.Collection;

/**
 * Class Forest
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Forest<V,E> extends Graph<V,E> 
{
    Collection<Tree<V,E>> getTrees();
} 
