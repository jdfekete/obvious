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

package obvious.prefuse;

import java.util.Collection;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;

/**
 * Implementation of an Obvious Schema based on prefuse toolkit.
 * It is mainly a wrapper, Obvious Schema and Prefuse ones are really closed.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousSchema implements Schema {

  /**
   * Prefuse schema wrapped around obvious schema.
   */
  private prefuse.data.Schema schema;

  /**
   * Obvious table that represents this schema.
   */
  private obvious.data.Table schemaTable;

  /**
   * Number of column in the schema's table.
   */
  private static final Integer SCHEMA_COL_NUMBER = 3;

  /**
   * Default Constructor.
   * It creates an empty Obvious Schema.
   */
  public PrefuseObviousSchema() {
    this(new prefuse.data.Schema(0));
  }

  /**
   * Constructor from a prefuse schema instance.
   * @param prefuseSchema A prefuse schema to wrap around Obvious schema.
   */
  public PrefuseObviousSchema(prefuse.data.Schema prefuseSchema) {
    this.schema = prefuseSchema;
    prefuse.data.Table prefuseSchemaTable =
      new prefuse.data.Table(this.schema.getColumnCount(), SCHEMA_COL_NUMBER);
    prefuseSchemaTable.addColumn("Name", String.class);
    prefuseSchemaTable.addColumn("Type", Class.class);
    prefuseSchemaTable.addColumn("Default Value", Object.class);
    for (int i = 0; i < this.schema.getColumnCount(); i++) {
      prefuseSchemaTable.set(i, "Name", this.schema.getColumnName(i));
      prefuseSchemaTable.set(i, "Type", this.schema.getColumnType(i));
      prefuseSchemaTable.set(i, "Default Value", this.schema.getDefault(i));
    }
    this.schemaTable =  new PrefuseObviousTable(prefuseSchemaTable);
  }

  /**
   * Add a column to this schema.
   * @param name the column name
   * @param type the column type (as a Class instance)
   * @param defaultValue the column default value
   * @return the number of columns in the schema
   */
  public int addColumn(String name, Class<?> type, Object defaultValue) {
    this.schema.addColumn(name, type, defaultValue);
    return this.getColumnCount();
  }

  /**
   * Checks if the getValue method can return values that are compatibles
   * with a given type.
   * @param col Index of the column
   * @param c Expected type to check
   * @return true if the types are compatibles
   */
  public boolean canGet(int col, Class<?> c) {
    if (c == null || col < 0) {
      return false;
    } else {
      try {
        Class<?> columnType = this.getColumnType(col);
        return (columnType == null ? false : c.isAssignableFrom(columnType));
      } catch (Exception e) {
        return false;
      }
    }
  }

  /**
   * Checks if the set method can accept for a specific column values that
   * are compatible with a given type.
   * @param col Index of the column
   * @param c Expected type to check
   * @return true if the types compatibles
   */
  public boolean canSet(int col, Class<?> c) {
    if (c == null || col < 0) {
      return false;
    } else {
        try {
          Class<?> columnType = this.getColumnType(col);
          return (columnType == null ? false : c.isAssignableFrom(columnType));
        } catch (Exception e) {
          return false;
        }
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
   * @param field Index of the column
   * @param c Expected type to check
   * @return true if the types compatibles
   */
  public boolean canSet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canSet(col, c);
  }

  /**
   * Get the number of columns in this schema.
   * @return the number of columns
   */
  public int getColumnCount() {
    return this.schema.getColumnCount();
  }

  /**
   * The default value of the column at the given position.
   * @param col the column index
   * @return the column's default value
   */
  public Object getColumnDefault(int col) {
    return this.schema.getDefault(col);
  }

  /**
   * The column index for the column with the given name.
   * @param field the column name
   * @return the column index
   */
  public int getColumnIndex(String field) {
    return this.schema.getColumnIndex(field);
  }

  /**
   * The name of the column at the given position.
   * @param col the column index
   * @return the column name
   */
  public String getColumnName(int col) {
    return this.schema.getColumnName(col);
  }

  /**
   * The type of the column at the given position.
   * @param col the column index
   * @return the column type
   */
  public Class<?> getColumnType(int col) {
    return this.schema.getColumnType(col);
  }

  /**
   * The type of the column with the given name.
   * @param field the column name
   * @return the column type
   */
  public Class<?> getColumnType(String field) {
    return this.schema.getColumnType(field);
  }

  /**
   * Checks if a column name exists or not.
   * @param name the name of the column to find
   * @return true if found else false
   */
  public boolean hasColumn(String name) {
    for (int i = 0; i < this.getColumnCount(); i++) {
      if (this.getColumnName(i).equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Deletes a column in the schema.
   * Prefuse schema doesn't support column removal.
   * @param field name of the column to delete
   * @return false
   */
  public boolean removeColumn(String field) {
    return false;
  }

  /**
   * Deletes a column in the schema.
   * Prefuse schema doesn't support column removal.
   * @param col index of the column to delete
   * @return false
   */
  public boolean removeColumn(int col) {
    return false;
  }

  /**
   * Adds a row.
   * @return number of rows
   */
  public int addRow() {
    return this.schemaTable.addRow();
  }

  /**
   * Adds a table listener.
   * @param listnr to add
   */
  public void addTableListener(TableListener listnr) {
    this.schemaTable.addTableListener(listnr);
  }

  /**
   * Indicates the beginning of a column edit.
   * @param col edited
   * @throws ObviousException if edition is not supported.
   */
  public void beginEdit(int col) throws ObviousException {
    this.schemaTable.beginEdit(col);
  }

  /**
   * Indicates if possible to add rows.
   * @return true if possible
   */
  public boolean canAddRow() {
    return this.schemaTable.canAddRow();
  }

  /**
   * Indicates if possible to remove rows.
   * @return true if possible
   */
  public boolean canRemoveRow() {
    return this.schemaTable.canRemoveRow();
  }

  /**
   * Indicates the end of a column edit.
   * @param col edited
   * @throws ObviousException if edition is not supported.
   */
  public void endEdit(int col) throws ObviousException {
    this.schemaTable.endEdit(col);
  }

  /**
   * Get the number of rows in the table.
   * @return the number of rows
   */
  public int getRowCount() {
    return this.schemaTable.getRowCount();
  }

  /**
   * Returns this Table's schema.
   * @return a copy of this Table's schema
   */
  public Schema getSchema() {
    return this.schemaTable.getSchema();
  }

  /**
   * Gets all table listener.
   * @return a collection of table listeners.
   */
  public Collection<TableListener> getTableListeners() {
    return this.schemaTable.getTableListeners();
  }

  /**
   * Gets a specific value.
   * @param rowId spotted row
   * @param field field dedicated to spotted column
   * @return value
   */
  public Object getValue(int rowId, String field) {
    return this.schemaTable.getValue(rowId, field);
  }

  /**
   * Gets a specific value.
   * @param rowId spotted row
   * @param col spotted column
   * @return value
   */
  public Object getValue(int rowId, int col) {
    return this.schemaTable.getValue(rowId, col);
  }

  /**
   * Indicates if a column is being edited.
   * @param col spotted
   * @return true if edited
   */
  public boolean isEditing(int col) {
    return this.schemaTable.isEditing(col);
  }

  /**
   * Indicates if the given row number corresponds to a valid table row.
   * @param rowId the row number to check for validity
   * @return true if the row is valid, false if it is not
   */
  public boolean isValidRow(int rowId) {
    return this.schemaTable.isValidRow(rowId);
  }

  /**
   * Indicates if a given value is correct.
   * @param rowId spotted row
   * @param col column spotted
   * @return true if the coordinates are valid
   */
  public boolean isValueValid(int rowId, int col) {
    return this.schemaTable.isValueValid(rowId, col);
  }

  /**
   * Removes all the rows.
   *
   * <p>After this method, the table is almost in the same state as if
   * it had been created afresh except it contains the same columns as before
   * but they are all cleared.
   *
   */
  public void removeAllRows() {
    this.schemaTable.removeAllRows();
  }

  /**
   * Removes a row.
   * @param row row to remove
   * @return true if done
   */
  public boolean removeRow(int row) {
    return this.schemaTable.removeRow(row);
  }

  /**
   * Removes a table listener.
   * @param listnr listener to remove
   */
  public void removeTableListener(TableListener listnr) {
    this.schemaTable.removeTableListener(listnr);
  }

  /**
   * Get an iterator over the row numbers of this table.
   * @return an iterator over the rows of this table
   */
  public IntIterator rowIterator() {
    return this.schemaTable.rowIterator();
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param field field to set
   * @param val value to set
   */
  public void set(int rowId, String field, Object val) {
    this.schemaTable.set(rowId, field, val);
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param col column to set
   * @param val value to set
   */
  public void set(int rowId, int col, Object val) {
    this.schemaTable.set(rowId, col, val);
  }


}
