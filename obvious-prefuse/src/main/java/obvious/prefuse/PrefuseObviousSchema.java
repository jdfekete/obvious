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
   * Prefuse schema.
   */
  private prefuse.data.Schema schema;

  /**
   * Obvious table that represents this schema.
   */
  private obvious.data.Table schemaTable;

  /**
   * Constructor.
   * @param prefuseSchema A prefuse schema to wrap around Obvious schema.
   */
  public PrefuseObviousSchema(prefuse.data.Schema prefuseSchema) {
    this.schema = prefuseSchema;
    prefuse.data.Table prefuseSchemaTable =
      new prefuse.data.Table(this.schema.getColumnCount(), 3);
    prefuseSchemaTable.addColumn("Name", String.class);
    prefuseSchemaTable.addColumn("Type", "Class");
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
   * Checks if it's possible to get this type from this column.
   * @param col index of the column to check
   * @param type type to test
   * @return true if the types are compatible.
   */
  public boolean canGet(int col, Class<?> type) {
    return this.getColumnDefault(col).equals(type);
  }

  /**
   * Checks if it's possible to set this type from this column.
   * @param col index of the column to check
   * @param type type to test
   * @return true if the types are compatible.
   */
  public boolean canSet(int col, Class<?> type) {
    return this.getColumnDefault(col).equals(type);
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


  public int addRow() {
    return this.schemaTable.addRow();
  }

  public void addTableListener(TableListener listnr) {
    this.schemaTable.addTableListener(listnr);
  }

  public void beginEdit(int col) throws ObviousException {
    this.schemaTable.beginEdit(col);
  }

  public boolean canAddRow() {
    return this.schemaTable.canAddRow();
  }

  public boolean canRemoveRow() {
    return this.schemaTable.canRemoveRow();
  }

  public void endEdit(int col) throws ObviousException {
    this.schemaTable.endEdit(col);
  }

  public int getRowCount() {
    return this.schemaTable.getRowCount();
  }

  public Schema getSchema() {
    return this.schemaTable.getSchema();
  }

  public Collection<TableListener> getTableListeners() {
    return this.schemaTable.getTableListeners();
  }

  public Object getValue(int rowId, String field) {
    return this.schemaTable.getValue(rowId, field);
  }

  public Object getValue(int rowId, int col) {
    return this.schemaTable.getValue(rowId, col);
  }

  public boolean isEditing(int col) {
    return this.schemaTable.isEditing(col);
  }

  public boolean isValidRow(int rowId) {
    return this.schemaTable.isValidRow(rowId);
  }

  public boolean isValueValid(int rowId, int col) {
    return this.schemaTable.isValueValid(rowId, col);
  }

  public void removeAllRows() {
    this.schemaTable.removeAllRows();
  }

  public boolean removeRow(int row) {
    return this.schemaTable.removeRow(row);
  }

  public void removeTableListener(TableListener listnr) {
    this.schemaTable.removeTableListener(listnr);
  }

  public IntIterator rowIterator() {
    return this.schemaTable.rowIterator();
  }

  public void set(int rowId, String field, Object val) {
    this.schemaTable.set(rowId, field, val);
  }

  public void set(int rowId, int col, Object val) {
    this.schemaTable.set(rowId, col, val);
  }

}
