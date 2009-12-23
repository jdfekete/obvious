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

import java.util.ArrayList;
import java.util.Collection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.ResultSet;

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
public class JDBCObviousTable implements Table {

  /**
   * JDBC connection.
   */
  private Connection con;

  /**
   * JDBC Table name.
   */
  private String tableName;

  /**
   * Associated Obvious schema.
   */
  private Schema schema;

  /**
   * Table Listeners.
   */
  private Collection<TableListener> listener;

  /**
   * Constructor for JDBCObviousTable.
   * @param connection a JDBC connection instance
   * @param tName the Table SQL name
   * @throws ObviousException if something bad happens with the JDBC link.
   */
  public JDBCObviousTable(Connection connection, String tName)
      throws ObviousException {
    this.con = connection;
    this.tableName = tName;
    this.schema = new JDBCObviousSchema(this.con, this.tableName);
    this.listener = new ArrayList<TableListener>();
  }

  /**
   * Adds a row at the end of the database.
   * @return the number of row in the database.
   */
  public int addRow() {
    try {
    String request = "SELECT * FROM " + this.tableName;
    Statement stmt = this.con.createStatement();
    ResultSet result = stmt.executeQuery(request);
    result.last();
    result.insertRow();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this.getRowCount();
  }

  /**
   * Adds an indicated table listener.
   * @param listnr given table listnr
   */
  public void addTableListener(TableListener listnr) {
    if (!listener.contains(listnr)) {
      listener.add(listnr);
    }
  }

  public void beginEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
    
  }

  /**
   * Indicates if it is possible to add a row in this table.
   * If a column or the database is readOnly, the implementation assumes
   * it is impossible to add a row.
   * @return true if possible
   */
  public boolean canAddRow() {
    Boolean addable = true;
    try {
      DatabaseMetaData metadata = this.con.getMetaData();
      if (metadata.isReadOnly()) {
        addable = false;
      } else {
        String request = "SELECT * FROM " + this.tableName;
        Statement stmt = this.con.createStatement();
        ResultSet result = stmt.executeQuery(request);
        ResultSetMetaData setMetadata = result.getMetaData();
        for (int i = 0; i < setMetadata.getColumnCount(); i++) {
         if (setMetadata.isReadOnly(i)) {
           addable = false;
         }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return addable;
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

  public void endEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
  }

  /**
   * Gets the number of line in the table.
   * @return number of line in the table
   */
  public int getRowCount() {
    try {
      String request = "SELECT * FROM " + this.tableName;
      Statement stmt = this.con.createStatement();
      ResultSet result = stmt.executeQuery(request);
      result.last();
      return result.getRow();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
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
    try {
      String request = "SELECT " + field + " FROM " + this.tableName;
      Statement stmt = this.con.createStatement();
      ResultSet result = stmt.executeQuery(request);
      Integer count = 0;
      while (result.next()) {
        if (count == rowId) {
          return result.getObject(field);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
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

  public boolean isEditing(int col) {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Checks if a row is valid i.e. its row index exists in the table.
   * @param rowId index of the row
   * @return true if valid
   */
  public boolean isValidRow(int rowId) {
    boolean valid = true;
    try {
      String request = "SELECT * FFROM " + this.tableName;
      Statement stmt = this.con.createStatement();
      ResultSet result = stmt.executeQuery(request);
      result.last();
      if (result.getRow() < rowId) {
        valid = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return valid;
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
    if (this.canRemoveRow()) {
      try {
        String request = "DELETE FROM " + this.tableName;
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(request);
      } catch (Exception e) {
        e.printStackTrace();
      }
      }
  }

  /**
   * Deletes the indicated row in the database.
   * @param row JDBC index of the row to delete
   * @return true if deleted
   */
  public boolean removeRow(int row) {
    if (this.canRemoveRow()) {
      try {
        String request = "SELECT * FROM " + this.tableName;
        Statement stmt = this.con.createStatement();
        ResultSet result = stmt.executeQuery(request);
        int count = 0;
        while (result.next()) {
          if (count == row) {
            result.deleteRow();
            return true;
          }
          count++;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return false;
    } else {
      return false;
    }
  }

  /**
   * Removes a specified TableListener.
   * @param listnr listener to remove
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  public IntIterator rowIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Sets for a specified row and column a value in the database.
   * @param rowId JDBC index of the row
   * @param field name of the column
   * @param val value to set in the database
   */
  public void set(int rowId, String field, Object val) {
    try {
      String request = "SELECT " + field + " FROM " + this.tableName;
      Statement stmt = this.con.createStatement();
      ResultSet result = stmt.executeQuery(request);
      Integer count = 0;
      while (result.next()) {
        if (count == val) {
          result.updateObject(field, val);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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

}
