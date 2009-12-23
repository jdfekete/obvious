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

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Collection;


import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;

/**
*
* Implementation of an Obvious Table based on JDBC.
* @author Pierre-Luc Hemery
*
*/
public class JDBCObviousSchema implements Schema {

  /**
   * JDBC connection.
   */
  private Connection con;

  /**
   * JDBC Table metadata.
   */
  private DatabaseMetaData metadata;

  /**
   * JDBC Table name.
   */
  private String tableName;

  /**
   * SQL Format Factory.
   */
  private FormatFactorySQL formatFactory;

  /**
   * Table of the schema.
   */
  private Table schemaTable;

  /**
   * Constructor.
   * @param connection JDBC connection to use for this schema.
   * @param tName name of the table to examine
   * @throws ObviousException if something bad happens during metadata harvest.
   */
  public JDBCObviousSchema(Connection connection, String tName)
      throws ObviousException {
    try {
    this.con = connection;
    this.metadata = con.getMetaData();
    this.formatFactory = new FormatFactorySQL();
    this.tableName = tName;
    } catch (Exception e) {
      throw new ObviousException(e);
    }
  }

  /**
   * Adds a column into the database.
   * @param name name of the column to add
   * @param type Java type of the column to add
   * @param defaultValue unused in  this implementation
   * @return the number of column if succeed else -1
   */
  public int addColumn(String name, Class<?> type, Object defaultValue) {
    try {
    String typeSQL = this.formatFactory.getSQLType(type);
    String request = "ALTER TABLE " + this.tableName + " ADD " + name + " "
        + typeSQL;
    Statement stmt = this.con.createStatement();
    stmt.executeUpdate(request);
    return this.getColumnCount();
    } catch (SQLException e) {
      return -1;
    }
  }

  /**
   * Checks if the getValue method can return values that are compatibles
   * with a given type.
   * @param col Index of the column
   * @param c Expected type to check
   * @return true if the types are compatibles
   */
  public boolean canGet(int col, Class<?> c) {
    if (c == null) {
      return false;
    } else {
        Class<?> columnType = this.getColumnType(col);
        return c.isAssignableFrom(columnType);
    }
  }

  /**
   * Checks if the getValue method can return values that are compatibles
   * with a given type.
   * @param field Name of the column
   * @param c Expected type to check
   * @return true if the types are compatibles
   */
  public boolean canGet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canGet(col, c);
  }

  /**
   * Checks if the set method can accept for a specific column values that
   * are compatible with a given type.
   * @param col Index of the column
   * @param c Expected type to check
   * @return true if the types compatibles
   */
  public boolean canSet(int col, Class<?> c) {
    if (c == null) {
      return false;
    } else {
        Class<?> columnType = this.getColumnType(col);
        return c.isAssignableFrom(columnType);
    }
  }

  /**
   * Checks if the set method can accept for a specific column values that
   * are compatible with a given type.
   * @param field Index of the column
   * @param c Expected type to check
   * @return true if the types compatibles
   */
  public boolean canSet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canGet(col, c);
  }

  /**
   * Returns the number of column in the schema.
   * This method returns -1 if JDBC table metadata are corrupted.
   * @return number of columns
   */
  public int getColumnCount() {
    try {
    int columnCount = 0;
    ResultSet setColumn = metadata.getColumns(null, null, "%", null);
    while (setColumn.next()) {
      columnCount++;
    }
    return columnCount;
    } catch (Exception e) {
      return -1;
    }
  }

  /**
   * Unused in this implementation.
   * @param col index of the column
   * @return null for it's unused in this implementation
   */
  public Object getColumnDefault(int col) {
    return null;
  }

  /**
   * Gets the column index associated to a specific name.
   * @param field name of the column
   * @return index (>0) if succeed else if -1 for a not found column
   *
   */
  public int getColumnIndex(String field) {
    try {
    String request = "SELECT * FROM " + this.tableName;
    Statement stmt = this.con.createStatement();
    ResultSet result = stmt.executeQuery(request);
    ResultSetMetaData setMetadata = result.getMetaData();
    for (int i = 0; i < setMetadata.getColumnCount(); i++) {
      if (setMetadata.getColumnName(i).equals(field)) {
        return i;
      }
    }
    return -1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  /**
   * Gets the column name associated to a specific index.
   * @param col index of the column
   * @return the name of the column
   */
  public String getColumnName(int col) {
    try {
      String request = "SELECT * FROM " + this.tableName;
      Statement stmt = this.con.createStatement();
      ResultSet result = stmt.executeQuery(request);
      ResultSetMetaData setMetadata = result.getMetaData();
      return setMetadata.getColumnName(col);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gets the column type (Java Type) from an index.
   * @param col index of the column
   * @return a Java class
   */
  public Class<?> getColumnType(int col) {
    try {
      String request = "SELECT * FROM " + this.tableName;
      Statement stmt = this.con.createStatement();
      ResultSet result = stmt.executeQuery(request);
      ResultSetMetaData setMetadata = result.getMetaData();
      String className =  setMetadata.getColumnClassName(col);
      return Class.forName(className);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gets the column type (Java Type) from a column name.
   * @param field name of the column
   * @return a Java class
   */
  public Class<?> getColumnType(String field) {
    int col = this.getColumnIndex(field);
    return this.getColumnType(col);
  }

  /**
   * Internal method indicating if the given data field is included as a
   * data column.
   * @param name name to seek
   * @return true if the name exists.
   */
  public boolean hasColumn(String name) {
    ResultSet result = null;
    try {
       result = metadata.getColumns(null, null, this.tableName, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result == null;
  }

  /**
   * Removes the indicated column.
   * @param field name of column to remove
   * @return true if removed
   */
  public boolean removeColumn(String field) {
    try {
    String request = "ALTER TABLE " + this.tableName + "DROP COLUMN " + field;
      Statement stmt = this.con.createStatement();
      stmt.executeUpdate(request);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Removes the indicated column.
   * @param col index of the column
   * @return true if removed
   */
  public boolean removeColumn(int col) {
    String field = this.getColumnName(col);
    return this.removeColumn(field);
  }

  public int addRow() {
    // TODO Auto-generated method stub
    return 0;
  }

  public void addTableListener(TableListener listnr) {
    // TODO Auto-generated method stub
    
  }

  public void beginEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
    
  }

  public boolean canAddRow() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean canRemoveRow() {
    // TODO Auto-generated method stub
    return false;
  }

  public void endEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
    
  }

  public int getRowCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  public Schema getSchema() {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection<TableListener> getTableListeners() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getValue(int rowId, String field) {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getValue(int rowId, int col) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isEditing(int col) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isValidRow(int rowId) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isValueValid(int rowId, int col) {
    // TODO Auto-generated method stub
    return false;
  }

  public void removeAllRows() {
    // TODO Auto-generated method stub
    
  }

  public boolean removeRow(int row) {
    // TODO Auto-generated method stub
    return false;
  }

  public void removeTableListener(TableListener listnr) {
    // TODO Auto-generated method stub
    
  }

  public IntIterator rowIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  public void set(int rowId, String field, Object val) {
    // TODO Auto-generated method stub
    
  }

  public void set(int rowId, int col, Object val) {
    // TODO Auto-generated method stub
    
  }

}
