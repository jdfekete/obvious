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

package obvious.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.SchemaImpl;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;

/**
 * A class that takes a table result set and creates the
 * associated Obvious table.
 * @author Pierre-Luc Hemery
 *
 */
public class TableImporter {

  /**
   * Imported table.
   */
  private Table table;

  /**
   * Schema of the imported Table.
   */
  private Schema schema;

  /**
   * Table name.
   */
  private String name;

  /**
   * Factory to use to create table.
   */
  private DataFactory dataFactory;

  /**
   * Factory to use to create formats.
   */
  private FormatFactory formatFactory;

  /**
   * Metadata associated to the database.
   */
  private DatabaseMetaData metadata;

  /**
   * Connection to database.
   */
  private Connection connection;

  /**
   * Constructor.
   * @param tableName table name
   * @param dbMetadata metadata imported from the database.
   * @param con connection to database.
   * @param dFactory factory to use to create tables.
   * @param fFactory factory to use to create formats.
   */
  public TableImporter(String tableName, DatabaseMetaData dbMetadata,
      Connection con, DataFactory dFactory, FormatFactory fFactory) {
    this.name = tableName;
    this.metadata = dbMetadata;
    this.connection = con;
    this.dataFactory = dFactory;
    this.formatFactory = fFactory;
    this.schema = new SchemaImpl(true, true);
  }

  /**
   * Getter for the table attribute.
   * @return table attribute
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * Creates the schema for the table.
   * @throws SQLException if bad things happen.
   */
  public void createSchema() throws SQLException {
    ResultSet setColumn = metadata.getColumns(null, null, name, null);
    while (setColumn.next()) {
      String title = setColumn.getString("COLUMN_NAME");
      String typeString = setColumn.getString("TYPE_NAME");
      TypedFormat format = this.formatFactory.getFormat(typeString);
      Class<?> type = format.getFormattedClass();
      this.schema.addColumn(title, type, null);
    }
  }

  /**
   * Creates the table.
   * @throws SQLException if bad things happens.
   */
  public void createTable() throws SQLException {
    this.createSchema();
    try {
      this.table = dataFactory.createTable(this.schema);
      String selectStar = "SELECT * FROM " + name;
      Statement stmt = this.connection.createStatement();
      ResultSet tableContent = stmt.executeQuery(selectStar);
      int rowCount = this.table.getRowCount();
      while (tableContent.next()) {
        this.table.addRow();
        for (int i = 0; i < this.schema.getColumnCount(); i++) {
          String colName = schema.getColumnName(i);
          Object val = tableContent.getObject(colName);
          table.set(rowCount, colName , val);
        }
        rowCount++;
      }
    } catch (ObviousException e) {
      System.err.println("Can't create the obvious table!");
    }
  }
}
