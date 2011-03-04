/*
* Copyright (c) 2010, INRIA
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

package obvious.jung.data;

import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Node;
import obvious.data.Edge;
import obvious.impl.SchemaImpl;
import obvious.impl.TableImpl;

/**
 * Implementation of Obvious DataFactoryInterface.
 * Use this class to build Obvious Network with Jung toolkit.
 * This factory can be used to build Obvious Table using the TableImpl from
 * Obvious package. So for tables, no extra parameters are used in the factory,
 * only schema.
 * @author Pierre-Luc Hemery
 *
 */
public class JungDataFactory extends DataFactory {

  @Override
  public Network createGraph(Schema nodeSchema, Schema edgeSchema)
      throws ObviousException {
    return new JungObviousNetwork(nodeSchema, edgeSchema);
  }

  @Override
  public Table createTable(Schema schema) throws ObviousException {
    return new TableImpl(schema);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Network wrapGraph(Object underlyingGraph) throws ObviousException {
    if (underlyingGraph instanceof Network) {
      return (Network) underlyingGraph;
    } else if (underlyingGraph instanceof edu.uci.ics.jung.graph.Graph<?, ?>) {
      try {
      return new JungObviousNetwork(
          (edu.uci.ics.jung.graph.Graph<Node, Edge>) underlyingGraph);
      } catch (Exception e) {
        throw new ObviousException(e);
      }
    } else {
      throw new ObviousException("Can't create network from this input object");
    }
  }

  /**
   * Returns a Table instance if the underlyingTable is an instance of
   * an implementation of Table. Else, it will rise an exception.
   * @param underlyingTable a candidate Table
   * @return an Obvious Table
   * @throws ObviousException if Table creation failed
   */
  @Override
  public Table wrapTable(Object underlyingTable) throws ObviousException {
    if (underlyingTable instanceof Table) {
      return (Table) underlyingTable;
    } else {
      throw new ObviousException("Can't create table from this input object");
    }
  }

  /**
   * Returns an Obvious Network. Obvious Jung networks accepts column names
   * id for edge schema to identify source and target nodes.
   * @param nodeSchema original schema for nodes
   * @param edgeSchema original schema for edges
   * @param param unused parameter
   * @return an Obvious Table
   * @throws ObviousException if table creation failed
   */
  @Override
  public Network createGraph(Schema nodeSchema, Schema edgeSchema,
      Map<String, Object> param) throws ObviousException {
    if (!param.containsKey("sourceKey")) {
      param.put("sourceKey", JungObviousNetwork.SRCNODE);
    }
    if (!param.containsKey("targetKey")) {
      param.put("targetKey",  JungObviousNetwork.DESTNODE);
    }
    return new JungObviousNetwork(nodeSchema, edgeSchema,
        (String) param.get("sourceKey"), (String) param.get("targetKey"));
  }

  /**
   * Returns an Obvious Table. Obvious Jung tables only use schema
   * as constructor's parameter.
   * @param schema schema of the table
   * @param param unused parameter
   * @return an Obvious Table
   * @throws ObviousException if table creation failed
   */
  @Override
  public Table createTable(Schema schema,
      Map<String, Object> param) throws ObviousException {
    return new TableImpl(schema);
  }

  @Override
  public Schema createSchema() {
    return new SchemaImpl();
  }

}
