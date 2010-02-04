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

package obvious.impl;

import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;

/**
 * Implements abstract class DataFactory for the Obvious Impl implementation.
 * @author Pierre-Luc Hémery
 *
 */
public class DataFactoryImpl extends DataFactory {

  /**
   * Obvious Impl does not have its own implementation of network.
   * So this method is unused and always throws an ObviousException.
   * @param name name of the network
   * @param nodeSchema original schema for the node
   * @param edgeSchema original schema for the edge
   * @return nothing unimplemented
   * @throws ObviousException always throws an ObviousException
   */
  @Override
  public Network createGraph(String name, Schema nodeSchema, Schema edgeSchema)
      throws ObviousException {
    throw new ObviousException("Can't create a network Obvious Impl doest not"
        + "support network!");
  }

  @Override
  public Table createTable(String name, Schema schema) throws ObviousException {
    return new TableImpl(schema);
  }

  /**
   * Returns a Network instance if the underlyingGraph is an instance of
   * an implementation of Network. Else, it will rise an exception.
   * @param underlyingGraph a candidate Network
   * @return an Obvious Network
   * @throws ObviousException if Network creation failed
   */
  @Override
  public Network wrapGraph(Object underlyingGraph) throws ObviousException {
    if (underlyingGraph instanceof Network) {
      return (Network) underlyingGraph;
    } else {
      throw new ObviousException("Can't create network from this input object");
    }
  }

  /**
   * Returns a Network instance if the underlyingTable is an instance of
   * an implementation of Table. Else, it will rise an exception.
   * @param underlyingTable a candidate Table
   * @return an Obvious Table
   * @throws ObviousException if Table creation failed
   */
  public Table wrapTable(Object underlyingTable) throws ObviousException {
    if (underlyingTable instanceof Table) {
      return (Table) underlyingTable;
    } else {
      throw new ObviousException("Can't create table from this input object");
    }
  }

  /**
   * Obvious Impl does not have its own implementation of network.
   * So this method is unused and always throws an ObviousException.
   * @param name name of the network
   * @param nodeSchema original schema for the node
   * @param edgeSchema original schema for the edge
   * @param param unused parameter
   * @return nothing unimplemented
   * @throws ObviousException always throws an ObviousException
   */
  @Override
  public Network createGraph(String name, Schema nodeSchema, Schema edgeSchema,
      Map<String, Object> param) throws ObviousException {
    return createGraph(name, nodeSchema, edgeSchema);
  }

  /**
   * Returns an Obvious Table. Obvious Impl tables only use schema
   * as constructor's parameter.
   * @param name name of the table
   * @param schema schema of the table
   * @param param unused parameter
   * @return an Obvious Table
   * @throws ObviousException if table creation failed
   */
  @Override
  public Table createTable(String name, Schema schema,
      Map<String, Object> param) throws ObviousException {
    return createTable(name, schema, param);
  }

}
