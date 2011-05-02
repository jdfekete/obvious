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

package obvious.prefuse.data;

import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;

/**
 * This class is in an implementation of the
 * {@link obvious.data.DataFactory DataFactory} for the Obvious Prefuse binding.
 * This class allows to instantiate Obvious Network and Table from wrapped
 * Prefuse data structures and from Obvious schema. For further information,
 * see {@link obvious.dataDataFactory Datafactory class}.
 * @see obvious.data.Table
 * @see obvious.data.Network
 * @see obvious.data.Schema
 * @see obvious.data.DataFactory
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseDataFactory extends DataFactory {

  @Override
  public Network createGraph(Schema nodeSchema, Schema edgeSchema)
      throws ObviousException {
    return new PrefuseObviousNetwork(nodeSchema, edgeSchema);
  }

  @Override
  public Schema createSchema() {
    return new PrefuseObviousSchema();
  }

  @Override
  public Table createTable(Schema schema) throws ObviousException {
    return new PrefuseObviousTable(schema);
  }

  /**
   * Returns a Network instance if the underlyingTable is an instance of
   * an implementation of Network or a prefuse network. Else, it will rise an
   * exception.
   * @param underlyingGraph a candidate Network
   * @return an Obvious Network
   * @throws ObviousException if Network creation failed
   */
  @Override
  public Network wrapGraph(Object underlyingGraph) throws ObviousException {
    if (underlyingGraph instanceof Network) {
      return (Network) underlyingGraph;
    } else if (underlyingGraph instanceof prefuse.data.Graph) {
      return new PrefuseObviousNetwork((prefuse.data.Graph) underlyingGraph);
    } else {
      throw new ObviousException("Can't create network from this input object");
    }
  }

  /**
   * Returns a Table instance if the underlyingTable is an instance of
   * an implementation of Table or a prefuse Table. Else, it will rise an
   * exception.
   * @param underlyingTable a candidate Table
   * @return an Obvious Table
   * @throws ObviousException if Table creation failed
   */
  @Override
  public Table wrapTable(Object underlyingTable) throws ObviousException {
    if (underlyingTable instanceof Table) {
      return (Table) underlyingTable;
    } else if (underlyingTable instanceof prefuse.data.Table) {
      return new PrefuseObviousTable((prefuse.data.Table) underlyingTable);
    } else {
      throw new ObviousException("Can't create table from this input object");
    }
  }

  /**
   * Creates a Prefuse Obvious Table from the input parameter.
   * The parameter 'param' is crucial cause it contains all informations
   * to build the network.
   * The map could contain the following keys : "directed" a boolean
   * indicating if the network is oriented or not, "nodeKey" a string
   * indicating the column used to identify nodes,
   * "sourceKey" a string indicating the column used to identify source nodes
   * in edgeTable, and "targetKey" a string indicating the column used to
   * identity target nodes in edgeTable.
   * Extra keys should be simply ignored. If keys are not set, default values
   * of Prefuse toolkit will be used.
   * @param nodeSchema original obvious schema for the nodes
   * @param edgeSchema original obvious schema for the edges
   * @param param a map with specific prefuse graph constructors parameters
   * @throws ObviousException if Table creation failed
   * @return a Prefuse Obvious table
   */
  @Override
  public Network createGraph(Schema nodeSchema, Schema edgeSchema,
      Map<String, Object> param) throws ObviousException {
    if (!param.containsKey("directed")) {
      param.put("directed", false);
    }
    if (!param.containsKey("sourceKey")) {
      param.put("sourceKey", prefuse.data.Graph.DEFAULT_SOURCE_KEY);
    }
    if (!param.containsKey("targetKey")) {
      param.put("targetKey", prefuse.data.Graph.DEFAULT_TARGET_KEY);
    }
    if (!param.containsKey("nodeKey")) {
      param.put("nodeKey", prefuse.data.Graph.DEFAULT_NODE_KEY);
    }
    return new PrefuseObviousNetwork(nodeSchema, edgeSchema,
        (Boolean) param.get("directed"), (String) param.get("nodeKey"),
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
    return createTable(schema);
  }
}
