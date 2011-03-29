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

package obvious.jdbc;

import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.SchemaImpl;

/**
 * Implementation of Obvious DataFactory Interface.
 * Use this class to build Obvious Table based on a JDBC implementation.
 * Network are not supported for the moment.
 * @author Pierre-Luc Hemery
 *
 */
public class JDBCDataFactory extends DataFactory {

  @Override
  public Network createGraph(Schema nodeSchema, Schema edgeSchema)
      throws ObviousException {
    throw new ObviousException("Can't create a Network, JDBC Obvious"
        + "does not support it");
  }

  @Override
  public Network createGraph(Schema nodeSchema, Schema edgeSchema,
      Map<String, Object> param) throws ObviousException {
    throw new ObviousException("Can't create a Network, JDBC Obvious"
        + "does not support it");
  }

  @Override
  public Table createTable(Schema schema) throws ObviousException {
    throw new ObviousException("Can't create a JDBC Obvious table without JDBC"
        + "connection parameters! Call the createTable(String, Schema, Map)"
        + "method");
  }

  /**
   * Creates a JDBC Table from the input parameter. The parameter 'param' is
   * crucial cause it contains all informations to access to the to database.
   * The map should contain the following keys : "driver" the jdbc driver,
   * "url" url for the database, "user" login for the db , "password"
   * password for the database and "primary" for the column that serves of
   * primary key. If "primary" is not given, the implementation tries to
   * find an existing key, but the process could fail. Specially, if the key is
   * defined on several columns or does not exist.
   * Extra keys should be simply ignored.
   * @param schema original obvious schema for the table
   * @param param a map with connection parameters
   * @throws ObviousException if Table creation failed
   * @return Obvious Table for the underlying 'JBDC table'
   */
  @Override
  public Table createTable(Schema schema,
      Map<String, Object> param) throws ObviousException {
    if (!param.containsKey("driver") || !param.containsKey("url")
        || param.containsKey("user") || !param.containsKey("password")) {
      throw new ObviousException("Can't create an obvious Table, missing JDBC"
          + "parameters for connection. You should specify driver, url, login"
          + "and password.");
    } else if (param.containsKey("primary")) {
      return new JDBCObviousTable(schema, (String) param.get("driver"),
          (String) param.get("url"), (String) param.get("user"),
          (String) param.get("password"), (String) param.get("name"),
          (String) param.get("primary"));
    } else {
      return new JDBCObviousTable(schema, (String) param.get("driver"),
          (String) param.get("url"), (String) param.get("user"),
          (String) param.get("password"), (String) param.get("name"));
    }
  }

  @Override
  public Network wrapGraph(Object underlyingGraph) throws ObviousException {
    if (underlyingGraph instanceof Network) {
      return (Network) underlyingGraph;
    } else {
      throw new ObviousException("Can't create network from this input object");
    }
  }

  @Override
  public Table wrapTable(Object underlyingTable) throws ObviousException {
    if (underlyingTable instanceof Table) {
      return (Table) underlyingTable;
    } else {
      throw new ObviousException("Can't create table from this input object");
    }
  }

  @Override
  public Schema createSchema() {
    return new SchemaImpl();
  }

}
