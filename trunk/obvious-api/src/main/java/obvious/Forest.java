/*****************************************************************************
 * Copyright (C) 2008 Jean-Daniel Fekete and INRIA, France                  *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license.txt file.                                                         *
 *****************************************************************************/
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
