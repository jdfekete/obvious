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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;


/**
 * Example implementation of Interface Table.
 * @author Pierre-Luc Hemery.
 *
 */
public class TableImpl implements Table {

  /**
   * Table's schema.
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
  private Map<String, ArrayList<?>> column;

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
    this.column = new HashMap<String, ArrayList<?>>();
    this.columnIndex = new HashMap<String, Integer>();
    for (int i = 0; i < schema.getColumnCount(); i++) {
      String title = schema.getColumnName(i);
      column.put(title, new ArrayList<Object>());
      columnIndex.put(title, i);
    }
  }

  /**
   * Adds a row.
   * @return number of rows
   */
  public int addRow() {
    if (this.canAddRow()) {
      for (Iterator<ArrayList<?>> iter = column.values().iterator();
          iter.hasNext();) {
        ArrayList<?> spottedArray = (ArrayList<?>) iter.next();
        spottedArray.add(null);
      }
    }
    return this.getRowCount();
  }

  /**
   * Adds a table listener.
   * @param listnr to add
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
    this.editing = true;
  }

  /**
   * Indicates the end of a column edit.
   * @param col edited
   * @throws ObviousException if edition is not supported.
   */
  public void endEdit(int col) throws ObviousException {
    this.editing = false;
  }

  /**
   * Indicates if a column is being edited.
   * @param col spotted
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
   * Get the number of rows in the table.
   * @return the number of rows
   */
  public int getRowCount() {
    int maxArraySize = 0;
    for (Iterator<ArrayList<?>> i = column.values().iterator(); i.hasNext();) {
      ArrayList<?> currentArray = (ArrayList<?>) i.next();
      if (currentArray.size() > maxArraySize) {
        maxArraySize = currentArray.size();
      }
    }
    return maxArraySize;
  }

  /**
   * Returns this Table's schema.
   * @return the schema of the table.
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
   * @param rowId spotted row
   * @param field dedicated to spotted column
   * @return value
   */
  public Object getValue(int rowId, String field) {
    ArrayList<?> spottedColumn = (ArrayList<?>) column.get(field);
    return spottedColumn.get(rowId);
  }

  /**
   * Gets a specific value.
   * @param rowId spotted row
   * @param col spotted
   * @return value
   */
  public Object getValue(int rowId, int col) {
    Set<Map.Entry<String, Integer>> mapSet = columnIndex.entrySet();
    ArrayList<Map.Entry<String, Integer>> indexList =
        new ArrayList<Map.Entry<String, Integer>>(mapSet);
    for (Iterator<Map.Entry<String, Integer>> iter = indexList.iterator();
        iter.hasNext();) {
      Map.Entry<String, Integer> spottedEntry =
        (Map.Entry<String, Integer>) (iter.next());
      if (spottedEntry.getValue() == col) {
        return this.getValue(rowId, spottedEntry.getKey());
      }
    }
    return null;
  }


  /**
   * Indicates if the given row number corresponds to a valid table row.
   * @param rowId the row number to check for validity
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
   * @param rowId spotted row
   * @param col spotted
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
      for (Iterator<ArrayList<?>> iter = column.values().iterator();
          iter.hasNext();) {
        ArrayList<?> spottedArray = (ArrayList<?>) iter.next();
        spottedArray.clear();
      }
    }
  }

  /**
  /* Removes a row in the schema's table.
   * @param row the index of the row to remove
   * @return true if removes, else false.
   */
  public boolean removeRow(int row) {
    if (!this.canRemoveRow()) {
      return false;
    } else if (this.isValidRow(row)) {
      for (Iterator<ArrayList<?>> iter = column.values().iterator();
          iter.hasNext();) {
        ArrayList<?> spottedArray = (ArrayList<?>) iter.next();
        spottedArray.remove(row);
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Removes a table listener.
   * @param listnr to remove
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  /**
   * Get an iterator over the row numbers of this table.
   * @return an iterator over the rows of this table
   */
  public IntIterator rowIterator() {
    IntIterator intIterator = (IntIterator) columnIndex.values().iterator();
    return intIterator;
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param field field to set
   * @param val to set
   */
  @SuppressWarnings("unchecked")
  public void set(int rowId, String field, Object val) {
    ArrayList<Object> spottedArray = (ArrayList<Object>) column.get(field);
    spottedArray.set(rowId, val);
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param col to set
   * @param val to set
   */
  public void set(int rowId, int col, Object val) {
    Set<Map.Entry<String, Integer>> mapSet = columnIndex.entrySet();
    ArrayList<Map.Entry<String, Integer>> indexList =
        new ArrayList<Map.Entry<String, Integer>>(mapSet);
    for (Iterator<Map.Entry<String, Integer>> iter = indexList.iterator();
        iter.hasNext();) {
      Map.Entry<String, Integer> spottedEntry =
          (Map.Entry<String, Integer>) (iter.next());
      if (spottedEntry.getValue() == col) {
        this.set(rowId, spottedEntry.getKey(), val);
      }
    }
  }

}