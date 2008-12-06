/*****************************************************************************
 * Copyright (C) 2008 Jean-Daniel Fekete and INRIA, France                  *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license.txt file.                                                         *
 *****************************************************************************/
package obvious;

/**
 * Class Predicate
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Predicate {
    boolean apply(Table table, int rowId);
}
