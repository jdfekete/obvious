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
import obvious.data.Table;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;

/**
 * Implementation of an Obvious Table based on prefuse toolkit.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousTable implements Table {

  /**
   * Prefuse Table.
   */
  private prefuse.data.Table table;

  /**
   * Constructor that takes an existing Prefuse table as argument.
   * @param prefuseTable Prefuse table to wrap around Obvious Table.
   */
  public PrefuseObviousTable(prefuse.data.Table prefuseTable) {
    this.table = prefuseTable;
  }
  
  public PrefuseObviousTable(Schema schema) {
    this.table = new prefuse.data.Table(0, schema.getColumnCount());
    for(int i = 0; i < schema.getColumnCount(); i++) {
      this.table.addColumn(schema.getColumnName(i), schema.getColumnType(i),
          schema.getColumnDefault(i));
    }
  }

  /**
   * Adds a row.
   * @return number of rows
   */
  public int addRow() {
    if (this.canAddRow()) {
      return this.table.addRow();
    } else {
      return -1;
    }
  }

  /**
   * Add a table listener to this table.
   * @param listnr the listener to add
   */
  public void addTableListener(TableListener listnr) {
    this.table.addTableListener((prefuse.data.event.TableListener) listnr);
  }

  /**
   * Indicates the beginning of a column edit.
   * @param col edited
   * @throws ObviousException if edition is not supported.
   */
  public void beginEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
  }

  /**
   * Indicates if possible to add rows.
   * @return true if possible
   */
  public boolean canAddRow() {
    return this.table.isAddColumnSupported();
  }

  /**
   * Indicates if possible to remove rows.
   * @return true if possible
   */
  public boolean canRemoveRow() {
    return true;
  }

  /**
   * Indicates the end of a column edit.
   * @param col edited
   * @throws ObviousException if edition is not supported.
   */
  public void endEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
  }

  /**
   * Get the number of rows in the table.
   * @return the number of rows
   */
  public int getRowCount() {
    return this.table.getRowCount();
  }

  /**
   * Returns this Table's schema.
   * @return a copy of this Table's schema
   */
  public Schema getSchema() {
    return new PrefuseObviousSchema(this.table.getSchema());
  }

  /**
   * Not implemented yet.
   * @return null
   */
  public Collection<TableListener> getTableListeners() {
    return null;
  }

  /**
   * Gets a specific value.
   * @param rowId spotted row
   * @param field dedicated to spotted column
   * @return value
   */
  public Object getValue(int rowId, String field) {
    return this.table.get(rowId, field);
  }

  /**
   * Gets a specific value.
   * @param rowId spotted row
   * @param col spotted
   * @return value
   */
  public Object getValue(int rowId, int col) {
    return this.table.getValueAt(rowId, col);
  }

  /**
   * Indicates if a column is being edited.
   * @param col column to check
   * @return true if the colum is being edited
   */
  public boolean isEditing(int col) {
    return false;
  }

  /**
   * Indicates if the given row number corresponds to a valid table row.
   * @param rowId the row number to check for validity
   * @return true if the row is valid, false if it is not
   */
  public boolean isValidRow(int rowId) {
    return this.table.isValidRow(rowId);
  }

  /**
   * Indicates if a given value is correct.
   * @param rowId spotted row
   * @param col spotted
   * @return true if the coordinates are valid
   */
  public boolean isValueValid(int rowId, int col) {
    if (!this.isValidRow(rowId)) {
      return false;
    } else {
      return this.getSchema().getColumnCount() <= col;
    }
  }

  /**
   * Removes all the rows.
   *
   */
  public void removeAllRows() {
    if (this.canRemoveRow()) {
        this.table.clear();
      }

  }

  /**
   * Removes a row from this table.
   * @param row the row to delete
   * @return true if the row was successfully deleted, false if the
   * row was already invalid
   */
  public boolean removeRow(int row) {
    if (this.canRemoveRow()) {
      return this.table.removeRow(row);
    } else {
      return false;
    }
  }

  /**
   * Remove a table listener from this table.
   * @param listnr the listener to remove
   */
  public void removeTableListener(TableListener listnr) {
    this.table.removeTableListener((prefuse.data.event.TableListener) listnr);
  }

  /**
   * Get an iterator over the row numbers of this table.
   * @return an iterator over the rows of this table
   */
  public IntIterator rowIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param field field to set
   * @param val value to set
   */
  public void set(int rowId, String field, Object val) {
    this.table.set(rowId, field, val);
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param col to set
   * @param val to set
   */
  public void set(int rowId, int col, Object val) {
    this.table.setValueAt(rowId, col, val);
  }

}
