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

import java.util.Map;

import obvious.ObviousException;

/**
 * Abstract Class DataFactory. A factory that can create Network and
 * Table instances. Several methods are proposed to build such instances
 * from existing instances of other tables or from Obvious Schema(s).
 * Each implementation of Obvious should implement this class.
 * @author  Jean-Daniel Fekete
 * @version  $Revision$
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
     * @throws ObviousException  if undefined system property
     * @return  static instance of DataFactory
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
     * Creates an Empty Schema that can be changed.
     * @return A changeable empty Schema.
     */
    public abstract Schema createSchema();

    /**
     * Creates a table from an existing Schema instance.
     * @param schema original schema for the table
     * @return table derived from the schema
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Table createTable(Schema schema)
        throws ObviousException;

    /**
     * Creates a table from an existing Schema instance.
     * This method allows the use of specific parameters placed in param
     * collection.
     * @param schema original schema for the table
     * @param param a collection of parameter for underlying constructors.
     * @throws ObviousException when the table cannot be created.
     * @return table derived from the schema
     */
    public abstract Table createTable(Schema schema,
        Map<String, Object> param) throws ObviousException;

    /**
     * Creates a table from an existing table object.
     * @param underlyingTable original table
     * @return obvious table
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Table wrapTable(Object underlyingTable)
        throws ObviousException;

    /**
     * Creates a network from an existing Schema instance.
     * @param nodeSchema original schema for nodes
     * @param edgeSchema original schema for edges
     * @return network derived from the network
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Network createGraph(
          Schema nodeSchema, Schema edgeSchema)
        throws ObviousException;

    /**
     * Creates a network from an existing Schema instance.
     * This method allows the use of specific parameters placed in param
     * collection.
     * @param nodeSchema original schema for nodes
     * @param edgeSchema original schema for edges
     * @param param a collection of parameter for underlying constructors.
     * @return network derived from the network
     * @throws ObviousException when the table cannot be created.
     */
    public abstract Network createGraph(Schema nodeSchema,
            Schema edgeSchema, Map<String, Object> param)
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
