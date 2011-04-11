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

package obvious.jdbc.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import obvious.ObviousException;
import obvious.ObviousRuntimeException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.SchemaImpl;
import obvious.jdbc.utils.FormatFactorySQL;

/**
*
* Implementation of an Obvious Table based on JDBC.
* @author Pierre-Luc Hemery
*
*/
public class JDBCObviousSchema implements Schema {

  /**
   * URL for the database.
   */
  private String url;

  /**
   * Username for the database.
   */
  private String username;

  /**
   * Password for the database.
   */
  private String password;

  /**
   * SQL Format Factory.
   */
  private FormatFactorySQL formatFactory;

  /**
   * Table of the schema.
   */
  private Table schemaTable;

  /**
   * Index of schema column.
   */
  private final int name = 0, type = 1, defaultVal = 2;

  /**
   * Name of the table associated to the schema.
   */
  private String tableName;

  /**
   * Map between Column and their default value.
   */
  private Map<String, Object> defaultValues;

  /**
   * Is the schema being edited.
   */
  private boolean editing = false;

  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener = new ArrayList<TableListener>();

  /**
   * Constructor.
   * @param driver driver for this database
   * @param inUrl url for the database
   * @param inUsername user name for the database
   * @param inPswd password for the database
   * @param tName of the table associated to this schema
   */
  public JDBCObviousSchema(String driver, String inUrl, String inUsername,
      String inPswd, String tName) {
    this.url = inUrl;
    this.username = inUsername;
    this.password = inPswd;
    this.tableName = tName;
    this.formatFactory = new FormatFactorySQL();
    this.defaultValues = new HashMap<String, Object>();
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      System.err.print("ClassNotFoundException: ");
      System.err.println(e.getMessage());
    }
  }

  /**
   * Checks if the table already exist in the database.
   * @return true if already created
   */
  public boolean tableExist() {
    Connection con = null;
    try {
      con = DriverManager.getConnection(url, username, password);
      DatabaseMetaData  metadata = con.getMetaData();
      String[] myTables = {"TABLE"};
      ResultSet tables = metadata.getTables(null,
      null, "%", myTables);
      boolean existingTable = false;
      while (tables.next()) {
        if (tables.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
          existingTable = true;
          break;
        }
      }
      return existingTable;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return false;
    } finally {
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
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
    Connection con = null;
    PreparedStatement pStatement = null;
    try {
      con = DriverManager.getConnection(url, username, password);
      String typeSQL = this.formatFactory.getSQLType(type);
      String request = "";
      // creating the table if not existing
      if (!tableExist()) {
        request = "CREATE TABLE " + this.tableName + " (" + name + " "
          + typeSQL + ")";
      } else {
        request = "ALTER TABLE " + this.tableName + " ADD " + name + " "
          + typeSQL;
      }
      pStatement = con.prepareStatement(request);
      pStatement.executeUpdate(request);
      defaultValues.put(name, defaultValue);
      return this.getColumnCount();
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return -1;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    } finally {
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
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
    if (c == null || col >= this.getColumnCount()) {
      return false;
    } else {
        Class<?> columnType = this.getColumnType(col);
        return (columnType == null ? false : c.isAssignableFrom(columnType));
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
    return (col == -1 ? false : this.canGet(col, c));
  }

  /**
   * Checks if the set method can accept for a specific column values that
   * are compatible with a given type.
   * @param col Index of the column
   * @param c Expected type to check
   * @return true if the types compatibles
   */
  public boolean canSet(int col, Class<?> c) {
    if (c == null || col >= this.getColumnCount()) {
      return false;
    } else {
        Class<?> columnType = this.getColumnType(col);
        return (columnType == null ? false : c.isAssignableFrom(columnType));
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
    return (col == -1 ? false : this.canSet(col, c));
  }

  /**
   * Returns the number of column in the schema.
   * This method returns -1 if JDBC table metadata are corrupted.
   * @return number of columns
   */
  public int getColumnCount() {
    if (!tableExist()) {
      return 0;
    }
    Connection con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      con = DriverManager.getConnection(url, username, password);
      String request = "SELECT * FROM " + this.tableName;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      return result.getMetaData().getColumnCount();
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return -1;
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Unused in this implementation.
   * @param col index of the column
   * @return null for it's unused in this implementation
   */
  public Object getColumnDefault(int col) {
    if (col < 0 || col >= this.getColumnCount()) {
      return null;
    } else {
      String colName = this.getColumnName(col);
      return colName == null ? null : defaultValues.get(colName);
    }
  }

  /**
   * Gets the column index associated to a specific name.
   * @param field name of the column
   * @return index (>0) if succeed else if -1 for a not found column
   *
   */
  public int getColumnIndex(String field) {
    Connection con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      con = DriverManager.getConnection(url, username, password);
      String request = "SELECT * FROM " + this.tableName;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      ResultSetMetaData setMetadata = result.getMetaData();
      for (int i = 1; i < setMetadata.getColumnCount() + 1; i++) {
        if (setMetadata.getColumnName(i).equalsIgnoreCase(field)) {
          return i - 1; // Obvious begins indexation with 0, JDBC with 1
        }
      }
      return -1;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return -1;
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Gets the column name associated to a specific index.
   * @param col index of the column
   * @return the name of the column
   */
  public String getColumnName(int col) {
    Connection con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    if (col > this.getColumnCount() || !tableExist()) {
      return null;
    }
    try {
      con = DriverManager.getConnection(url, username, password);
      String request = "SELECT * FROM " + this.tableName;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      ResultSetMetaData setMetadata = result.getMetaData();
      return setMetadata.getColumnName(col + 1);
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return null;
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Gets the column type (Java Type) from an index.
   * @param col index of the column
   * @return a Java class
   */
  public Class<?> getColumnType(int col) {
    Connection con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      con = DriverManager.getConnection(url, username, password);
      String request = "SELECT * FROM " + this.tableName;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      ResultSetMetaData setMetadata = result.getMetaData();
      String className =  setMetadata.getColumnClassName(col + 1);
      return Class.forName(className);
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return null;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
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
    Connection con = null;
    ResultSet result = null;
    try {
       con = DriverManager.getConnection(url, username, password);
       DatabaseMetaData metadata = con.getMetaData();
       result = metadata.getColumns(null, null, this.tableName, name);
       return result == null;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return false;
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }

  }

  /**
   * Removes the indicated column.
   * @param field name of column to remove
   * @return true if removed
   */
  public boolean removeColumn(String field) {
    if (!tableExist() || field == null) {
      return false;
    }
    Connection con = null;
    PreparedStatement pStatement = null;
    try {
      con = DriverManager.getConnection(url, username, password);
      String request = "";
      if (this.getColumnCount() > 1) {
        request = "ALTER TABLE " + this.tableName + " DROP COLUMN " + field;
      } else  {
        request = "DROP TABLE " + this.tableName;
      }
        pStatement = con.prepareStatement(request);
        pStatement.executeUpdate(request);
        defaultValues.remove(field);
        return true;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return false;
    } finally {
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      try { con.close(); } catch (Exception e) { e.printStackTrace(); }
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

  /**
   * Gets the corresponding schema without internal columns.
   * @return a schema only composed by data columns
   */
  public Schema getDataSchema() {
    return this;
  }

  @Override
  public int addRow() {
    return -1;
  }

  @Override
  public void addTableListener(TableListener listnr) {
    listener.add(listnr);
  }

  @Override
  public void beginEdit(int col) throws ObviousException {
    this.editing = true;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.beginEdit(col);
    }
  }

  @Override
  public boolean canAddRow() {
    return true;
  }

  @Override
  public boolean canRemoveRow() {
    return true;
  }

  @Override
  public boolean endEdit(int col) throws ObviousException {
    this.editing = false;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.endEdit(col);
    }
    return this.editing;
  }

  @Override
  public int getRowCount() {
    return this.getColumnCount();
  }

  @Override
  public Schema getSchema() {
    Schema baseSchema = new SchemaImpl();
    baseSchema.addColumn("name", String.class, "defaulCol");
    baseSchema.addColumn("type", Class.class, String.class);
    baseSchema.addColumn("default", Object.class, null);
    return baseSchema;
  }

  @Override
  public Collection<TableListener> getTableListeners() {
    return listener;
  }

  @Override
  public Object getValue(int rowId, String field) {
    return getValue(rowId, getColumnIndex(field));
  }

  @Override
  public Object getValue(int rowId, int col) {
    if (isValueValid(rowId, col)) {
      if (col == name) {
        return getColumnName(rowId);
      } else if (col == type) {
        return getColumnType(rowId);
      } else if (col == defaultVal) {
        return getColumnDefault(rowId);
      }
      return null; // this should not happens
    } else {
      return null;
    }
  }

  @Override
  public boolean isEditing(int col) {
    return this.editing;
  }

  @Override
  public boolean isValidRow(int rowId) {
    return rowId < getColumnCount();
  }

  @Override
  public boolean isValueValid(int rowId, int col) {
    final int colNumber = 3;
    return isValidRow(rowId) && col < colNumber;
  }

  @Override
  public void removeAllRows() {
    if (canRemoveRow()) {
      for (int i = 0; i < getColumnCount(); i++) {
        removeColumn(i);
      }
      this.fireTableEvent(0, getColumnCount(),
          TableListener.ALL_COLUMN, TableListener.DELETE);
    }
  }

  @Override
  public boolean removeRow(int row) {
    boolean removed = removeColumn(row);
    if (removed) {
      this.fireTableEvent(row, row,
          TableListener.ALL_COLUMN, TableListener.DELETE);
    }
    return removed;
  }

  /**
   * Gets an iterator over the row id of this table matching the given
   * predicate.
   * @param pred an obvious predicate
   * @return an iterator over the rows of this table.
   */
  public IntIterator rowIterator(Predicate pred) {
    return null;
  }

  @Override
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  @Override
  public IntIterator rowIterator() {
    return null;
  }

  @Override
  public void set(int rowId, String field, Object val) {
    set(rowId, getColumnIndex(field), val);
  }

  @Override
  public void set(int rowId, int col, Object val) {
    return;
  }

  @Override
  public int addRow(Tuple tuple) {
    if (tuple.getSchema().equals(getSchema())) {
      String colName = tuple.getString("name");
      Class<?> c = (Class<?>) tuple.get("type");
      Object value = tuple.get("default");
      addColumn(colName, c, value);
    }
    this.fireTableEvent(getColumnCount(), getColumnCount(),
        TableListener.ALL_COLUMN, TableListener.INSERT);
    return getColumnCount();
  }

  /**
   * Return the underlying implementation.
   * @param inType targeted class
   * @return null
   */
  public Object getUnderlyingImpl(Class<?> inType) {
    return null;
  }

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param inType the type of modification
   */
  public void fireTableEvent(int start, int end, int col, int inType) {
   if (this.getTableListeners().isEmpty()) {
     return;
   }
   for (TableListener listnr : this.getTableListeners()) {
     listnr.tableChanged(this, start, end, col, inType);
   }
  }

}
