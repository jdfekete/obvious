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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;

/**
 * Class SchemaImpl.
 * A simple implementation of Schema interface to
 * write unit tests for Table interface.
 *
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */
public class SchemaImpl implements Schema {

  /**
   * Is the schema being edited.
   */
  private boolean editing;
  /**
   * Are rows removable.
   */
  private boolean canRemoveRow;
  /**
   * Are rows addable.
   */
  private boolean canAddRow;
  /**
   * Contains name of columns.
   */
  private ArrayList<String> names;
  /**
   * Contains type of columns.
   */
  private ArrayList<Class<?>> types;
  /**
   * Contains default value of columns.
   */
  private ArrayList<Object> defaultValues;
  /**
   * Columns of the table associated to the schema.
   */
  private Map<String, ArrayList<?>> columns;
  /**
   * Index of the columns of the table of the schema.
   */
  private Map<String, Integer> columnIndex;
  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener;

    // ------------------------------------------------------------------------
    // Constructors

  /**
   * Constructor of SchemaImpl with parameters.
   * @param addable true if row addition possible
   * @param removable true if row removal possible
   */
  public SchemaImpl(boolean addable, boolean removable) {
    this.canAddRow = addable;
    this.canRemoveRow = removable;
    this.editing = false;
    this.names = new ArrayList<String>();
    this.types = new ArrayList<Class<?>>();
    this.defaultValues = new ArrayList<Object>();
    this.listener = new ArrayList<TableListener>();
    columns.put(NAME, names);
    columns.put(TYPE, types);
    columns.put(DEFAULT_VALUE, defaultValues);
  }

  /**
   * Constructor of SchemaImpl without parameters.
   * Assumes table can not be changed.
   */
  public SchemaImpl() {
    this.canAddRow = false;
    this.canRemoveRow = false;
    this.editing = false;
    this.names = new ArrayList<String>();
    this.types = new ArrayList<Class<?>>();
    this.defaultValues = new ArrayList<Object>();
    columns.put(NAME, names);
    columns.put(TYPE, types);
    columns.put(DEFAULT_VALUE, defaultValues);
  }

    // ------------------------------------------------------------------------
    // Interface schema implementation


  /**
   * Gets the number of columns / data fields in this table.
   * @return the number of columns
   */
  public int getColumnCount() {
    return names.size();
  }

  /**
   * Gets the data type of the column at the given column index.
   * @param col the column index
   * @return the data type (as a Java Class) of the column
   */
  public Class<?> getColumnType(int col) {
    return this.types.get(col);
  }

  /**
   * Gets the default value for a column.
   * @param col spotted
   * @return value default
   */
  public Object getColumnDefault(int col) {
    return this.defaultValues.get(col);
  }

  /**
   * Gets the column name.
   * @param col spotted
   * @return name of the column
   */
  public String getColumnName(int col) {
    try {
      return this.names.get(col);
    } catch (IndexOutOfBoundsException e) { return null;
    }
  }

  /**
   * Get the column number for a given data field name.
   * @param field the name of the column to lookup
   * @return the column number of the column, or -1 if the name is not found
   */
  public int getColumnIndex(String field) {
    return this.names.indexOf(field);
  }

  /**
   * Indicates if possible to read a column.
   * @param col spotted
   * @param type can be null
   * @return true if readable
   */
  public boolean canGet(int col, Class type) {
    return false;
  }

  /**
   * Indicates if possible to write a column.
   * @param col spotted
   * @param type can be null
   * @return true if writable
   */
  public boolean canSet(int col, Class type) {
    return false;
  }

  /**
   * Internal method indicating if the given data field is included as a
   * data column.
   * @param name to seek
   * @return true if the name exists.
   */
  public boolean hasColumn(String name) {
    boolean hasColumn = false;
    int index = names.indexOf(name);
    if (index != -1) {
      hasColumn = true;
    }
    return hasColumn;
  }

  /**
   * Get the data type of the column with the given data field name.
   * @param field the column / data field name
   * @return the data type (as a Java Class) of the column
   */
  public Class getColumnType(String field) {
    if (hasColumn(field)) {
      int index = getColumnIndex(field);
      return types.get(index);
    } else {
      return null;
    }
  }

  /**
   * Add a column with the given name and data type to this table.
   * @param name the data field name for the column
   * @param type the data type, as a Java Class, for the column
   * @param defaultValue the default value for column data values
   * @return the column index
   * an Exception when the column name already exists.
   */
  public int addColumn(String name, Class type, Object defaultValue) {
    this.names.add(name);
    this.types.add(type);
    this.defaultValues.add(defaultValue);
    return getColumnIndex(name);
  }

  /**
   * Removes a column.
   * @param col spotted
   * @return true if removed
   */
  public boolean removeColumn(int col) {
    return false;
  }

  /**
   * Removes a column.
   * @param field name of column to remove
   * @return true if removed
   */
  public boolean removeColumn(String field) {
    if (hasColumn(field)) {
      return removeColumn(getColumnIndex(field));
    } else {
      return false;
    }
  }

  // ------------------------------------------------------------------------
  // Methods inherited from Table

  /**
   * Returns this Table's schema.
   * @return a copy of this Table's schema
   */
  public Schema getSchema() {
    return this;
  }

  /**
   * Get the number of rows in the table.
   * @return the number of rows
   */
  public int getRowCount() {
    int maxArraySize = 0;
    for (Iterator<ArrayList<?>> i = columns.values().iterator(); i.hasNext();) {
      ArrayList<?> currentArray = (ArrayList<?>) i.next();
      if (currentArray.size() > maxArraySize) {
        maxArraySize = currentArray.size();
      }
    }
    return maxArraySize;
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
   * Gets a specific value.
   * @param rowId spotted row
   * @param field dedicated to spotted column
   * @return value
   */
  public Object getValue(int rowId, String field) {
    ArrayList<?> spottedColumn = (ArrayList<?>) columns.get(field);
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
   * Adds a table listener.
   * @param listnr to add
   */
  public void addTableListener(TableListener listnr) {
    listener.add(listnr);
  }

  /**
   * Removes a table listener.
   * @param listnr to remove
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  /**
   * Gets all table listener.
   * @return a collection of table listeners.
   */
  public Collection<TableListener> getTableListeners() {
    return null;
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
   * Adds a row.
   * @return number of rows
   */
  public int addRow() {
    if (this.canAddRow()) {
      for (Iterator<ArrayList<?>> iter = columns.values().iterator();
          iter.hasNext();) {
        ArrayList<?> spottedArray = (ArrayList<?>) iter.next();
        spottedArray.add(null);
      }
    }
    return this.getRowCount();
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
      for (Iterator<ArrayList<?>> iter = columns.values().iterator();
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
   * Removes all the rows.
   *
   * <p>After this method, the table is almost in the same state as if
   * it had been created afresh except it contains the same columns as before
   * but they are all cleared.
   *
   */
  public void removeAllRows() {
    if (this.canRemoveRow()) {
      for (Iterator<ArrayList<?>> iter = columns.values().iterator();
          iter.hasNext();) {
        ArrayList<?> spottedArray = (ArrayList<?>) iter.next();
        spottedArray.clear();
      }
    }
  }

  /**
   * Sets a value.
   * @param rowId row to set
   * @param field field to set
   * @param val to set
   */
  public void set(int rowId, String field, Object val) {
    ArrayList<Object> spottedArray = (ArrayList<Object>) columns.get(field);
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
