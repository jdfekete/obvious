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

import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import obvious.ObviousException;
import obvious.ObviousRuntimeException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.FilterIntIterator;
import obvious.impl.IntIteratorImpl;
import obvious.jdbc.utils.FormatFactorySQL;
import obviousx.text.TypedFormat;

/**
 *
 * Implementation of an Obvious Table based on JDBC.
 * @author Pierre-Luc Hemery
 *
 */
public class JDBCObviousTable implements Table {

  /**
   * JDBC Table name.
   */
  private String tableName;

  /**
   * Associated Obvious schema.
   */
  private Schema schema;

  /**
   * URL for the database.
   */
  private String url;

  /**
   * User name for the database.
   */
  private String username;

  /**
   * Password for the database.
   */
  private String password;

  /**
   * Table Listeners.
   */
  private Collection<TableListener> listener;

  /**
   * SQL Format Factory.
   */
  private FormatFactorySQL formatFactory;

  /**
   * Name of the column that is the primary key of the table.
   * To work as an obvious Table, the table behind this JDBC implementation
   * must have a column that builds a primary key.
   */
  private String primaryKey;

  /**
   * Map that links row Index to the value of their primary key.
   */
  private Map<Integer, Object> rowIndexMap;

  /**
   * Is the schema being edited.
   */
  private boolean editing = false;

  /**
   * JDBC connection.
   */
  private Connection con;

  /**
   * Is the table in batch mode.
   */
  private boolean isInBatchMode = false;

  /**
   * Batch statement (used when user sets the batch mode with the beginEdit
   * method).
   */
  private Statement batchStmt;

  /**
   * Batch mode constant.
   */
  public static final int BATCH_MODE = 1;

  /**
   * Constructor for an Obvious table based on JDBC.
   * @param inSchema an Obvious schema
   * @param driver driver for the database (JDBC)
   * @param inUrl URL to access to the database
   * @param inUsername user name to access to the database
   * @param inPswd password to access to the database
   * @param tName table name
   * @param inKeyColumn name of column used as a primary key
   */
  public JDBCObviousTable(Schema inSchema, String driver, String inUrl,
      String inUsername, String inPswd, String tName, String inKeyColumn) {
    // Initialize attributes.
    this.tableName = tName;
    this.schema = inSchema;
    this.url = inUrl;
    this.username = inUsername;
    this.password = inPswd;
    this.formatFactory = new FormatFactorySQL();
    this.listener = new ArrayList<TableListener>();
    this.primaryKey = inKeyColumn;
    this.rowIndexMap = new HashMap<Integer, Object>();
    // Creating table
    try {
      this.con = DriverManager.getConnection(url, username, password);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
    if (!tableExist()) {
      PreparedStatement pStatement = null;
      try {
        String request = "CREATE TABLE " + this.tableName + " (";
        for (int i = 0; i < inSchema.getColumnCount(); i++) {
          String typeSQL = formatFactory.getSQLType(inSchema.getColumnType(i));
          request += inSchema.getColumnName(i) + " " + typeSQL;
          if (i < inSchema.getColumnCount() - 1) {
            request += ", ";
          } else {
            request += ")";
          }
        }
        pStatement = con.prepareStatement(request);
        pStatement.executeUpdate(request);
      } catch (SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
      } finally {
        try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
        //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
      }
    }
    // Load the JDBC driver.
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      System.err.print("ClassNotFoundException: ");
      System.err.println(e.getMessage());
    }
  }

  /**
   * Constructor for an Obvious table based on JDBC. This constructor
   * tries to resolve itself the primary key of the Table.
   * @param inSchema an Obvious schema
   * @param driver driver for the database (JDBC)
   * @param inUrl URL to access to the database
   * @param inUsername user name to access to the database
   * @param inPswd password to access to the database
   * @param tName table name
   * @throws ObviousException when obvious table creation failed
   */
  public JDBCObviousTable(Schema inSchema, String driver, String inUrl,
      String inUsername, String inPswd, String tName) throws ObviousException {
    this(inSchema, driver, inUrl, inUsername, inPswd, tName,
        getPrimaryKey(driver, inUrl, inUsername, inPswd, tName));
  }

  /**
   * Checks if the table already exist in the database.
   * @return true if table already created
   */
  public boolean tableExist() {
    // There are two levels of table, a JDBC one and an obvious. Sometimes, dev
    // or user want to create both at the same time, sometimes they just want
    // to convert an existing JDBC table to Obvious. Constructors support both
    // cases, they determine which one to use with the result of this method.
    //con = null;
    try {
      Connection connect = DriverManager.getConnection(url, username, password);
      DatabaseMetaData  metadata = connect.getMetaData();
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
      connect.close();
      return existingTable;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return false;
    } finally {
      //try { connect.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Gets the attribute primaryKey. Fails if the table has zero or two or more
   * columns as primary key.
   * @param inDriver driver for the JDBC database
   * @param inUrl URL to access to the database
   * @param inUser user name to access to the database
   * @param inPswd password to access to the database
   * @param tName table name
   * @return name of the column used as primary key.
   * @throws ObviousException if table has zero or two (more) columns for PK.
   */
  public static String getPrimaryKey(String inDriver,
      String inUrl, String inUser, String inPswd,
      String tName) throws ObviousException {
    Connection con = null;
    ResultSet result = null;
    String primKey = "";
    try {
      con = DriverManager.getConnection(inUrl, inUser, inPswd);
      DatabaseMetaData meta = con.getMetaData();
      result = meta.getPrimaryKeys(null, null, tName);
      if (result == null) {
        throw new ObviousException("The table " + tName
            + " doesn't define a primary key!");
      } else {
        int count = 0;
        while (result.next()) {
          primKey = result.getString(count);
          if (count > 1) {
            throw new ObviousException("The Table " + tName
                + " should have an unique column as primary key!");
          }
          count++;
        }
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    return primKey;
  }

  /**
   * Adds a row at the end of the database.
   * @return the number of row in the database.
   */
  public int addRow() {
    return this.getRowCount();
  }

  /**
   * Adds an indicated table listener.
   * @param listnr given table listener
   */
  public void addTableListener(TableListener listnr) {
    if (!listener.contains(listnr)) {
      listener.add(listnr);
    }
  }

  /**
   * Indicates the beginning of a column edit.
   * @param col column index
   * @throws ObviousException if edition is not supported.
   */
  public void beginEdit(int col) throws ObviousException {
    this.editing = true;
    if (col == BATCH_MODE) {
      this.isInBatchMode = true;
      try {
        this.batchStmt = con.createStatement();
        this.batchStmt.clearBatch();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    for (TableListener listnr : this.getTableListeners()) {
      listnr.beginEdit(col);
    }
  }

  /**
   * Indicates if it is possible to add a row in this table.
   * If a column or the database is readOnly, the implementation assumes
   * it is impossible to add a row.
   * @return true if possible
   */
  public boolean canAddRow() {
    Boolean addable = true;
    //con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      DatabaseMetaData metadata = con.getMetaData();
      if (metadata.isReadOnly()) {
        addable = false;
      } else {
        String request = "SELECT * FROM " + this.tableName;
        pStatement = con.prepareStatement(request);
        result = pStatement.executeQuery(request);
        ResultSetMetaData setMetadata = result.getMetaData();
        for (int i = 1; i <= setMetadata.getColumnCount(); i++) {
         if (setMetadata.isReadOnly(i)) {
           addable = false;
         }
        }
      }
      return addable;
    }  catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return false;
    } finally {
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Indicates if it is possible to remove a row in this table.
   * If a column or the database is readOnly, the implementation assumes
   * it is impossible to remove a row.
   * @return true if possible
   */
  public boolean canRemoveRow() {
    return this.canAddRow();
  }

  /**
   * Indicates the end of a column edit.
   * @param col column index
   * @return true if transaction succeed
   * @throws ObviousException if edition is not supported.
   */
  public boolean endEdit(int col) throws ObviousException {
    this.editing = false;
    if (isInBatchMode) {
      this.isInBatchMode = false;
      try {
        this.batchStmt.executeBatch();
        this.batchStmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    boolean success = true;
    TableListener failedListener = null;
    for (TableListener listnr : this.getTableListeners()) {
      if (!listnr.checkInvariants()) {
        listnr.endEdit(col);
        failedListener = listnr;
        success = false;
        break;
      }
    }
    for (TableListener listnr : this.getTableListeners()) {
      if (success && !listnr.equals(failedListener)) {
        listnr.endEdit(col);
      }
    }
    return success;
  }

  /**
   * Gets the number of line in the table.
   * @return number of line in the table
   */
  public int getRowCount() {
    //con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      String request = "SELECT * FROM " + this.tableName;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      int rowCount = 0;
      while (result.next()) {
        rowCount++;
      }
      return rowCount;
    } catch (Exception e) {
      System.err.println("SQLException: " + e.getMessage());
      return 0;
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Gets the schema attribute.
   * @return the Obvious schema of this table
   */
  public Schema getSchema() {
    return this.schema;
  }

  /**
   * Returns all the TableListener as a collection.
   * @return a collection of TableListener instance(s)
   */
  public Collection<TableListener> getTableListeners() {
    return listener;
  }

  /**
   * Gets a specified value in the database.
   * @param rowId JDBC index of the row
   * @param field name of the column
   * @return the value described by rowId and field
   */
  public Object getValue(int rowId, String field) {
    //con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      String request = "SELECT " + field + " FROM " + tableName + " WHERE "
        + primaryKey + " = ";
      TypedFormat formatSQL = formatFactory.
        getFormat(getColumnSQLType(schema.getColumnIndex(primaryKey)));
      StringBuffer tupleSQL = formatSQL.format(rowIndexMap.get(rowId),
          new StringBuffer(), new FieldPosition(0));
      request += tupleSQL;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      // result must have an unique row, cause it has been built with a primary
      // key as parameter.
      result.next();
      return result.getObject(field);
    }  catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return null;
    } finally {
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }


  /**
   * Gets a specified value in the database.
   * @param rowId JDBC index of the row
   * @param col JDBC index of the column
   * @return the value described by rowId and col
   */
  public Object getValue(int rowId, int col) {
    String field = this.getSchema().getColumnName(col);
    return this.getValue(rowId, field);
  }

  /**
   * Indicates if a column is being edited.
   * @param col column index
   * @return true if edited
   */
  public boolean isEditing(int col) {
    return editing;
  }

  /**
   * Checks if a row is valid i.e. its row index exists in the table.
   * @param rowId index of the row
   * @return true if valid
   */
  public boolean isValidRow(int rowId) {
    return rowIndexMap.containsKey(rowId);
  }

  /**
   * Checks if a given value is valid.
   * In this implementation, the value is valid if its row is valid and column
   * index exists.
   * @param rowId index of the row
   * @param col index of the column
   * @return true if valid
   */
  public boolean isValueValid(int rowId, int col) {
    boolean valid = true;
    if (!this.isValidRow(rowId) || this.getSchema().getColumnCount() < col) {
      valid = false;
    }
    return valid;
  }

  /**
   * Removes all the rows in the table.
   */
  public void removeAllRows() {
    //con = null;
    PreparedStatement pStatement = null;
    if (this.canRemoveRow()) {
      try {
        int r = getRowCount() - 1;
        String request = "DELETE FROM " + this.tableName;
        if (!isInBatchMode) {
          pStatement = con.prepareStatement(request);
          pStatement.executeUpdate(request);
          rowIndexMap.clear();
        } else {
          batchStmt.addBatch(request);
        }
        this.fireTableEvent(0, r,
            TableListener.ALL_COLUMN, TableListener.DELETE);
      }  catch (SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
        try {
          if (!con.getAutoCommit()) {
            con.rollback();
          }
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      } finally {
        try {
          if (pStatement != null) {
            pStatement.close();
          }
        } catch (Exception e) { e.printStackTrace(); }
        //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
      }
    }
  }

  /**
   * Deletes the indicated row in the database.
   * @param row JDBC index of the row to delete
   * @return true if deleted
   */
  public boolean removeRow(int row) {
    //con = null;
    PreparedStatement pStatement = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      String request = "DELETE FROM " + tableName + " WHERE " + primaryKey
        + " = ";
      TypedFormat formatSQL = formatFactory.
      getFormat(getColumnSQLType(schema.getColumnIndex(primaryKey)));
      StringBuffer tupleSQL = formatSQL.format(rowIndexMap.get(row),
          new StringBuffer(), new FieldPosition(0));
      request += tupleSQL;
      if (!isInBatchMode) {
        pStatement = con.prepareStatement(request);
        pStatement.execute(request);
      } else {
        batchStmt.addBatch(request);
      }
      rowIndexMap.remove(row);
      this.fireTableEvent(row, row,
          TableListener.ALL_COLUMN, TableListener.DELETE);
      return true;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      try {
        if (!con.getAutoCommit()) {
          con.rollback();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      return false;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    } finally {
      try {
        if (pStatement != null) {
          pStatement.close();
        }
      } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Removes a specified TableListener.
   * @param listnr listener to remove
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  /**
   * Gets an iterator over the row numbers of this table.
   * @return an iterator over the rows of this table
   */
  public IntIterator rowIterator() {
    return new IntIteratorImpl(rowIndexMap.keySet().iterator());
  }

  /**
   * Gets an iterator over the row id of this table matching the given
   * predicate.
   * @param pred an obvious predicate
   * @return an iterator over the rows of this table.
   */
  public IntIterator rowIterator(Predicate pred) {
    return new FilterIntIterator(this, pred);
  }

  /**
   * Sets for a specified row and column a value in the database.
   * @param rowId JDBC index of the row
   * @param field name of the column
   * @param val value to set in the database
   */
  public void set(int rowId, String field, Object val) {
    //con = null;
    PreparedStatement pStatement = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      String request = "UPDATE " + tableName + " SET " + field + " = ";
      TypedFormat formatSQL = formatFactory.getFormat(
          getColumnSQLType(schema.getColumnIndex(field)));
      StringBuffer newValue = formatSQL.format(val,
          new StringBuffer(), new FieldPosition(0));
      StringBuffer formerValue = formatSQL.format(this.getValue(rowId, field),
          new StringBuffer(), new FieldPosition(0));
      request += newValue + " WHERE " + field + " = " + formerValue;
      if (!isInBatchMode) {
        pStatement = con.prepareStatement(request);
        pStatement.executeUpdate(request);
      } else {
        batchStmt.addBatch(request);
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      try {
        if (!con.getAutoCommit()) {
          con.rollback();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    } catch (Exception e) {
      new ObviousRuntimeException(e);
    } finally {
      try {
        if (pStatement != null) {
          pStatement.close();
        }
      } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    this.fireTableEvent(rowId, rowId, this.getSchema().getColumnIndex(field),
        TableListener.UPDATE);
  }

  /**
   * Sets for a specified row and column a value in the database.
   * @param rowId JDBC index of the row
   * @param col JDBC index of the column
   * @param val value to set in the database
   */
  public void set(int rowId, int col, Object val) {
    String field = this.getSchema().getColumnName(col);
    this.set(rowId, field, val);
  }

  /**
   * Adds a row corresponding to a certain tuple in the database.
   * @param tuple tuple to add in the table
   * @return number of rows
   */
  public int addRow(Tuple tuple) {
    //con = null;
    PreparedStatement pStatement = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      Object primaryValue = null;
      String request = "INSERT INTO " + tableName + " (";
      for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
        request += tuple.getSchema().getColumnName(i);
        if (i == tuple.getSchema().getColumnCount() - 1) {
          request += ") VALUES (";
        } else {
          request += ", ";
        }
      }
      for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
        TypedFormat formatSQL = formatFactory.getFormat(getColumnSQLType(i));
        StringBuffer tupleSQL = formatSQL.format(tuple.get(i),
            new StringBuffer(), new FieldPosition(0));
        request += tupleSQL;
        if (tuple.getSchema().getColumnName(i).equals(primaryKey)) {
          primaryValue = tuple.get(i);
        }
        if (i == tuple.getSchema().getColumnCount() - 1) {
          request += ")";
        } else {
          request += ", ";
        }
      }
      if (!isInBatchMode) {
        pStatement = con.prepareStatement(request);
        pStatement.executeUpdate(request);
      } else {
        batchStmt.addBatch(request);
      }
      rowIndexMap.put(this.getRowCount() - 1, primaryValue);
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      try {
        if (!con.getAutoCommit()) {
          System.out.println("ROLLBACK");
          con.rollback();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    } catch (Exception e) {
      new ObviousRuntimeException(e);
    } finally {
      try {
        if (pStatement != null) {
          pStatement.close();
        }
      } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    int r = this.getRowCount() - 1;
    this.fireTableEvent(r, r, TableListener.ALL_COLUMN, TableListener.INSERT);
    return this.getRowCount();
  }

  /**
   * Return the corresponding SQL Type as a string for a column in the schema.
   * This method should not be called directly by the programmer.
   * @param col column index
   * @return SQL type of the column (string)
   */
  private String getColumnSQLType(int col) {
    //con = null;
    PreparedStatement pStatement = null;
    ResultSet result = null;
    try {
      //con = DriverManager.getConnection(url, username, password);
      String request = "SELECT * FROM " + tableName;
      pStatement = con.prepareStatement(request);
      result = pStatement.executeQuery(request);
      ResultSetMetaData setMetadata = result.getMetaData();
      return setMetadata.getColumnTypeName(col + 1); // JDBC cols start from 1
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      return null;
    } finally {
      try { result.close(); } catch (Exception e) { e.printStackTrace(); }
      try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
      //try { con.close(); } catch (Exception e) { e.printStackTrace(); }
    }
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(java.sql.Connection.class)) {
      return this.con;
    }
    return null;
  }

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param type the type of modification
   */
  public void fireTableEvent(int start, int end, int col, int type) {
    if (listener.isEmpty()) {
      return;
    }
    for (TableListener listnr : listener) {
      listnr.tableChanged(this, start, end, col, type);
    }
  }

}
