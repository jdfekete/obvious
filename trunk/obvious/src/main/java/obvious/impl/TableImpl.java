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

package obvious.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;


/**
 * Example implementation of Interface Table.
 * @author  Pierre-Luc Hemery.
 */
public class TableImpl implements Table {

  /**
   * Table's schema.
   * @uml.property  name="schema"
   */
  private Schema schema;

  /**
   * Is the schema being edited.
   */
  private boolean editing;

  /**
   * Are rows removable.
   */
  private boolean canRemoveRow = true;

  /**
   * Are rows addable.
   */
  private boolean canAddRow = true;

  /**
   * Columns of the table.
   */
  private Map<String, ArrayList<Object>> column;

  /**
   * Index of the columns of the table of the schema.
   */
  private Map<String, Integer> columnIndex;

  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener;

  /**
   * Constructor for TableImpl.
   * @param schemaIn Input schema
   */
  public TableImpl(Schema schemaIn) {
    this.schema = schemaIn;
    this.column = new HashMap<String, ArrayList<Object>>();
    this.columnIndex = new HashMap<String, Integer>();
    this.listener = new ArrayList<TableListener>();
    for (int i = 0; i < schema.getColumnCount(); i++) {
      String title = schema.getColumnName(i);
      column.put(title, new ArrayList<Object>());
      columnIndex.put(title, i);
    }
  }

  /**
   * Adds a row with default value.
   * @return number of rows
   */
  public int addRow() {
    if (this.canAddRow()) {
      for (Map.Entry<String, ArrayList<Object>> e : column.entrySet()) {
        e.getValue().add(schema.getColumnDefault(
            schema.getColumnIndex(e.getKey())));
      }
    }
    this.fireTableEvent(this.getRowCount() - 1, this.getRowCount() - 1,
        TableListener.ALL_COLUMN, TableListener.INSERT);
    return this.getRowCount();
  }

  /**
   * Adds a row corresponding to the input tuple.
   * @param tuple tuple to insert in the table
   * @return number of rows
   */
  public int addRow(Tuple tuple) {
    if (this.canAddRow()) {
      for (Map.Entry<String, ArrayList<Object>> e : column.entrySet()) {
        e.getValue().add(tuple.get(e.getKey()));
      }
    }
    this.fireTableEvent(this.getRowCount() - 1, this.getRowCount() - 1,
        TableListener.ALL_COLUMN, TableListener.INSERT);
    return this.getRowCount();
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
   * @param col column index
   * @throws ObviousException if edition is not supported.
   */
  public void beginEdit(int col) throws ObviousException {
    this.editing = true;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.beginEdit(col);
    }
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
   * Indicates if a column is being edited.
   * @param col column index
   * @return true if edited
   */
  public boolean isEditing(int col) {
    return this.editing;
  }

  /**
   * Indicates if possible to add rows.
   * @return true if possible
   */
  public boolean canAddRow() {
    return this.canAddRow;
  }

  /**
   * Indicates if possible to remove rows.
   * @return true if possible
   */
  public boolean canRemoveRow() {
    return this.canRemoveRow;
  }

  /**
   * Gets the number of rows in the table.
   * @return the number of rows
   */
  public int getRowCount() {
    int maxArraySize = 0;
    for (Map.Entry<String, ArrayList<Object>> e : column.entrySet()) {
      ArrayList<Object> currentArray = (ArrayList<Object>) e.getValue();
      if (currentArray.size() > maxArraySize) {
        maxArraySize = currentArray.size();
      }
    }
    return maxArraySize;
  }

  /**
   * Returns this table's schema.
   * @return  the schema of the table.
   * @uml.property  name="schema"
   */
  public Schema getSchema() {
    return this.schema;
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
   * @param rowId row index
   * @param field column name
   * @return value for this couple
   */
  public Object getValue(int rowId, String field) {
    return column.get(field).get(rowId);
  }

  /**
   * Gets a specific value.
   * @param rowId row index
   * @param col column index
   * @return value for this couple
   */
  public Object getValue(int rowId, int col) {
    for (Map.Entry<String, Integer> e : columnIndex.entrySet()) {
      if (e.getValue() == col) {
        return this.getValue(rowId, e.getKey());
      }
    }
    return null;
  }


  /**
   * Indicates if the given row number corresponds to a valid table row.
   * @param rowId row index
   * @return true if the row is valid, false if it is not
   */
  public boolean isValidRow(int rowId) {
    boolean isValid = false;
    if (rowId < this.getRowCount()) {
      isValid = true;
    }
    return isValid;
  }

  /**
   * Indicates if a given value is correct.
   * @param rowId row index
   * @param col column index
   * @return true if the coordinates are valid
   */
  public boolean isValueValid(int rowId, int col) {
    if (!this.isValidRow(rowId)) {
      return false;
    } else {
      return columnIndex.containsValue(col);
    }
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
    if (this.canRemoveRow()) {
      for (Map.Entry<String, ArrayList<Object>> e : column.entrySet()) {
        ArrayList<Object> spottedArray = (ArrayList<Object>) e.getValue();
        spottedArray.clear();
      }
    }
    this.fireTableEvent(0, this.getRowCount() - 1, TableListener.ALL_COLUMN,
        TableListener.DELETE);
  }

  /**
  /* Removes a row in the schema's table.
   * @param row row index
   * @return true if removes, else false.
   */
  public boolean removeRow(int row) {
    if (!this.canRemoveRow()) {
      return false;
    } else if (this.isValidRow(row)) {
      for (Map.Entry<String, ArrayList<Object>> e : column.entrySet()) {
        ArrayList<?> spottedArray = (ArrayList<Object>) e.getValue();
        spottedArray.remove(row);
      }
      this.fireTableEvent(row, row, TableListener.ALL_COLUMN,
          TableListener.DELETE);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets an iterator over the row numbers of this table.
   * @return an iterator over the rows of this table
   */
  public IntIterator rowIterator() {
    IntIterator intIterator = new IntIteratorImpl(
        columnIndex.values().iterator());
    return intIterator;
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
   * Removes a table listener.
   * @param listnr an Obvious TableListener
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  /**
   * Sets a value.
   * @param rowId row index
   * @param field column name
   * @param val value to set
   */
  public void set(int rowId, String field, Object val) {
    column.get(field).set(rowId, val);
  }

  /**
   * Sets a value.
   * @param rowId row index
   * @param col column index
   * @param val value to set
   */
  public void set(int rowId, int col, Object val) {
    for (Map.Entry<String, Integer> e : columnIndex.entrySet()) {
      if (e.getValue() == col) {
        this.set(rowId, e.getKey(), val);
      }
    }
    this.fireTableEvent(rowId, rowId, col, TableListener.UPDATE);
  }

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param type the type of modification
   */
  public void fireTableEvent(int start, int end, int col, int type) {
   if (this.getTableListeners().isEmpty()) {
     return;
   }
   for (TableListener listnr : this.getTableListeners()) {
     listnr.tableChanged(this, start, end, col, type);
   }
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    return null;
  }

}
