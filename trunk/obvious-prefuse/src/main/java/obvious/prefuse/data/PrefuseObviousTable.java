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

package obvious.prefuse.data;

import java.util.ArrayList;
import java.util.Collection;

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
import prefuse.data.util.TableIterator;

/**
 * This class is in an implementation of the {@link obvious.data.Table Table}
 * interface based on Prefuse Table class. Since Obvious and Prefuse tables
 * adopt the same design approach, this class is mainly a wrapper: Prefuse
 * tables are fully compatible with Obvious ones.
 * @see obvious.data.Table
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousTable implements Table {

  /**
   * Prefuse Table.
   */
  private prefuse.data.Table table;

  /**
   * Is the schema being edited.
   */
  private boolean editing = false;

  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener;


  /**
   * Constructor that takes an existing Prefuse table as argument.
   * @param prefuseTable Prefuse table to wrap around Obvious Table.
   */
  public PrefuseObviousTable(prefuse.data.Table prefuseTable) {
    this.table = prefuseTable;
    this.listener = new ArrayList<TableListener>();
  }

  /**
   * Constructor that used Obvious Schema to wrap Prefuse Tables.
   * @param schema original obvious schema for the table
   */
  public PrefuseObviousTable(Schema schema) {
    this.table = new prefuse.data.Table(0, schema.getColumnCount());
    this.listener = new ArrayList<TableListener>();
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
        int r = this.table.addRow();
        this.fireTableEvent(r, r, TableListener.ALL_COLUMN,
            TableListener.INSERT);
        return r;
      } else {
        return -1;
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Adds a table listener.
   * @param listnr an Obvious TableListener
   */
  public void addTableListener(TableListener listnr) {
    listener.add(listnr);
  }

  /**
   * Indicates the beginning of a column edit.
   * @param col edited
   * @throws ObviousException if edition is not supported.
   */
  public void beginEdit(int col) throws ObviousException {
    this.editing = false;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.beginEdit(col);
    }
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
   * @param col column index
   * @return true if transaction succeed
   * @throws ObviousException if edition is not supported.
   */
  public boolean endEdit(int col) throws ObviousException {
    this.editing = false;
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
   * Gets all table listener.
   * @return a collection of table listeners.
   */
  public Collection<TableListener> getTableListeners() {
    return listener;
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
    return this.table.get(rowId, col);
  }

  /**
   * Indicates if a column is being edited.
   * @param col column to check
   * @return true if the column is being edited
   */
  public boolean isEditing(int col) {
    return this.editing;
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
          this.fireTableEvent(0, getLastRowIndex(),
              TableListener.ALL_COLUMN, TableListener.DELETE);
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
        this.fireTableEvent(row, row,
            TableListener.ALL_COLUMN, TableListener.DELETE);
        return this.table.removeRow(row);
      } else {
        return false;
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Removes a table listener.
   * @param listnr an Obvious TableListener
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
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
   * Gets an iterator over the row id of this table matching the given
   * predicate.
   * @param pred an obvious predicate
   * @return an iterator over the rows of this table.
   */
  public IntIterator rowIterator(Predicate pred) {
    return new FilterIntIterator(this, pred);
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
      this.fireTableEvent(rowId, rowId, this.getSchema().getColumnIndex(field),
          TableListener.UPDATE);
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
      this.table.set(rowId, col, val);
      this.fireTableEvent(rowId, rowId, col, TableListener.UPDATE);
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
    this.beginEdit(TableListener.ALL_COLUMN);
    int r = table.addRow();
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      this.set(r, tuple.getSchema().getColumnName(i), tuple.get(i));
    }
    this.endEdit(TableListener.ALL_COLUMN);
    this.fireTableEvent(r, r, TableListener.ALL_COLUMN, TableListener.INSERT);
    return r;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
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

  /**
   * Returns the last valid row of this table.
   * @return the last valid row of this table
   */
  private int getLastRowIndex() {
    int lastValidRow = 0;
    for (TableIterator iter = table.iterator(); iter.hasNext();) {
      int i = iter.nextInt();
      if (table.isValidRow(i)) {
        lastValidRow = i;
      }
    }
    return lastValidRow;
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return prefuse table instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(prefuse.data.Table.class)) {
      return table;
    }
    return null;
  }

}
