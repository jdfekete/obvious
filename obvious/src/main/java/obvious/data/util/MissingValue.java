/*****************************************************************************
 * Copyright (C) 2009 Jean-Daniel Fekete and INRIA, France                   *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license.txt file.                                                         *
 *****************************************************************************/
package obvious.data.util;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * <b>MissingValue</b> is a singleton used
 * for missing values.
 *
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public final class MissingValue implements Serializable {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /** The singleton value. */
    private static final MissingValue INSTANCE = new MissingValue();

    /**
     * @return Returns the instance of the singleton
     */
    public static MissingValue getInstance() {
        return INSTANCE;
    }

    /**
     * Privte constructor.
     */
    private MissingValue() { }

    /**
     * Resolve the serialized object to the singleton.
     * @return the singleton.
     * @throws ObjectStreamException never for this class
     */
    private Object readResolve() throws ObjectStreamException {
        return getInstance();
    }
}
