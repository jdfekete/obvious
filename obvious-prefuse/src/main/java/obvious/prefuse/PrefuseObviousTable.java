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
import obvious.ObviousRuntimeException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.impl.IntIteratorImpl;

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

  /**
   * Constructor that used Obvious Schema to wrap Prefuse Tables.
   * @param schema original obvious schema for the table
   */
  public PrefuseObviousTable(Schema schema) {
    this.table = new prefuse.data.Table(0, schema.getColumnCount());
    for (int i = 0; i < schema.getColumnCount(); i++) {
      this.table.addColumn(schema.getColumnName(i), schema.getColumnType(i),
          schema.getColumnDefault(i));
    }
  }

  /**
   * Gets the Prefuse Table associated to the Obvious Table.
   * @return a Prefuse Table
   */
  public prefuse.data.Table getPrefuseTable() {
    return this.table;
  }

  /**
   * Adds a row.
   * @return number of rows
   */
  public int addRow() {
    try {
      if (this.canAddRow()) {
        return this.table.addRow();
      } else {
        return -1;
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
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
      return col <= this.getSchema().getColumnCount();
    }
  }

  /**
   * Removes all the rows.
   *
   */
  public void removeAllRows() {
    try {
      if (this.canRemoveRow()) {
          this.table.clear();
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Removes a row from this table.
   * @param row the row to delete
   * @return true if the row was successfully deleted, false if the
   * row was already invalid
   */
  public boolean removeRow(int row) {
    try {
      if (this.canRemoveRow()) {
        return this.table.removeRow(row);
      } else {
        return false;
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
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
  @SuppressWarnings("unchecked")
  public IntIterator rowIterator() {
    return new IntIteratorImpl(this.table.rows());
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param field field to set
   * @param val value to set
   */
  public void set(int rowId, String field, Object val) {
    try {
      this.table.set(rowId, field, val);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param col to set
   * @param val to set
   */
  public void set(int rowId, int col, Object val) {
    try {
      this.table.setValueAt(rowId, col, val);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Adds a row to the table (tuple).
   * @param tuple tuple to insert in the table.
   * @return row count
   */
  public int addRow(Tuple tuple) {
    try {
    int r = addRow();
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      this.set(r, tuple.getSchema().getColumnName(i), tuple.get(i));
    }
    return r;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

}
