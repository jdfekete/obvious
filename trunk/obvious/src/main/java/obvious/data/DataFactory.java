/*
* Copyright (c) 2009, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package obvious.data;

import obvious.ObviousException;

/**
 * Class DataFactory.
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public abstract class DataFactory {

    /**
     * Static instance of DataFactory.
     */
    private static DataFactory instance;

    /**
     * Constructor.
     */
    protected DataFactory() { }

    /**
     * Getter for attribute instance of DataFactory.
     * @throws ObviousException if undefined system property
     * @return static instance of DataFactory
     */
    public static DataFactory getInstance() throws ObviousException {
        if (instance == null) {
            String className = System.getProperty("obvious.DataFactory");
            if (className == null) {
                throw new ObviousException(
                        "Property obvious.DataFactory not set");
            }
            try {
                Class<?> c = Class.forName(className);
                instance = (DataFactory) c.newInstance();
            } catch (Exception e) {
                throw new ObviousException(e);
            }
        }
        return instance;
    }

    /**
     * Creates a table from an existing Schema instance.
     * @param name table's name
     * @param schema original schema for the table
     * @return table derived from the schema
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Table createTable(String name, Schema schema)
        throws ObviousException;

    /**
     * Creates a table from an existing table object.
     * @param unerlyingTable original table
     * @return obvious table
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Table wrapTable(Object unerlyingTable)
        throws ObviousException;

    /**
     * Creates a network from an existing Schema instance.
     * @param name network's name
     * @param nodeSchema original schema for nodes
     * @param edgeSchema original schema for edges
     * @return network derived from the network
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Network createGraph(
            String name, Schema nodeSchema, Schema edgeSchema)
        throws ObviousException;

    /**
     * Creates a network from an existing graph/network object.
     * @param underlyingGraph original graph object
     * @return obvious network
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Network wrapGraph(Object underlyingGraph)
        throws ObviousException;

}
