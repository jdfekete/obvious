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


package obvious.ivtk.data;

import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;

/**
 * Implementation of an Obvious DataFactory based on infovis toolkit.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkDataFactory extends DataFactory {

  @Override
  public Network createGraph(String name, Schema nodeSchema, Schema edgeSchema)
      throws ObviousException {
    return new IvtkObviousNetwork(nodeSchema, edgeSchema);
  }

  @Override
  public Network createGraph(String name, Schema nodeSchema, Schema edgeSchema,
      Map<String, Object> param) throws ObviousException {
    if (!param.containsKey("directed")) {
      param.put("directed", false);
    }
    return new IvtkObviousNetwork(nodeSchema, edgeSchema,
        (Boolean) param.get("directed"));
  }

  @Override
  public Table createTable(String name, Schema schema) throws ObviousException {
    return new IvtkObviousTable(schema);
  }

  @Override
  public Table createTable(String name, Schema schema, Map<String, Object> par)
      throws ObviousException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Network wrapGraph(Object underlyingGraph) throws ObviousException {
    if (underlyingGraph instanceof Network) {
      return (Network) underlyingGraph;
    } else if (underlyingGraph instanceof infovis.Graph) {
      return new IvtkObviousNetwork((infovis.Graph) underlyingGraph);
    } else {
      throw new ObviousException("Can't create network from this input object");
    }
  }

  @Override
  public Table wrapTable(Object underlyingTable) throws ObviousException {
    if (underlyingTable instanceof Table) {
      return (Table) underlyingTable;
    } else if (underlyingTable instanceof infovis.DynamicTable) {
      return new IvtkObviousTable((infovis.DynamicTable) underlyingTable);
    } else {
      throw new ObviousException("Can't create network from this input object");
    }
  }

}
