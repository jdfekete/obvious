package obvious;

import java.util.Collection;

/**
 * Class Forest
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
// existing JUNG interfaces below
public interface Forest<V,E,T> extends Graph<V,E,T> 
{
    Collection<Tree<V,E,T>> getTrees();
} 
