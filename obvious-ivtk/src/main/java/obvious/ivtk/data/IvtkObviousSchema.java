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

package obvious.ivtk.data;

import infovis.Column;
import infovis.column.ColumnFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;

import obvious.ObviousException;
import obvious.ObviousRuntimeException;
import obvious.data.Schema;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.SchemaImpl;


/**
 * This class is in an implementation of the
 * {@link obvious.data.Schema Schema} for the Obvious Ivtk binding.
 * Since Ivtk does not have a schema structure, but use column object
 * to store column description, this implementation is not a wrapper
 * class. This class implements service introduced in Obvious schemas
 * with a java collection of columns objects from Ivtk.
 * @see obvious.data.Schema
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkObviousSchema implements Schema {

  /**
   * Columns contained in the table.
   */
  private ArrayList<Column> cols;

  /**
   * Map associating columns of the schema with default values.
   */
  private HashMap<String, Object> colToDefault;

  /**
   * Index of schema column.
   */
  private final int name = 0, type = 1, defaultVal = 2;

  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener = new ArrayList<TableListener>();

  /**
   * Is the schema being edited.
   */
  private boolean editing = false;

  /**
   * Default Constructor.
   */
  public IvtkObviousSchema() {
    BasicConfigurator.configure();
    cols = new ArrayList<Column>();
    colToDefault = new HashMap<String, Object>();
  }

  /**
   * Constructor from an existing columns list.
   * @param inputCols an existing ivtk columns list
   */
  public IvtkObviousSchema(ArrayList<Column> inputCols) {
    BasicConfigurator.configure();
    cols = inputCols;
  }

  /**
   * Add a column with the given name and data type to this schema.
   * It throws a runtime exception when the column name already exists.
   * @param name name of the column
   * @param type the data type, as a Java Class, for the column
   * @param defaultValue the default value for the column
   * @return the column index
   */
  public int addColumn(String name, Class<?> type, Object defaultValue) {
    try {
      if (hasColumn(name)) {
        throw new ObviousRuntimeException("Name :" + name + " already exists");
      }
      ColumnFactory factory = ColumnFactory.getInstance();
      Column col = factory.create(type.getSimpleName(), name);
      cols.add(col);
      colToDefault.put(name, defaultValue);
      return getColumnCount();
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Checks if the getValue method can return values that are compatibles
   * with a given type.
   * @param col column index
   * @param c type to check
   * @return true if the types are compatibles
   */
  public boolean canGet(int col, Class<?> c) {
    if (c == null || col < 0) {
      return false;
    } else {
        Class<?> columnType = this.getColumnType(col);
        if (columnType != null && columnType.isPrimitive()) {
          Class<?> envelopType = retrieveObjectType(columnType);
          return (columnType == null ? false : (c.isAssignableFrom(columnType)
              || c.isAssignableFrom(envelopType)));
        }
        return (columnType == null ? false : c.isAssignableFrom(columnType));
    }
  }

  /**
   * Checks if the getValue method can return values that are compatibles
   * with a given type.
   * @param field column name
   * @param c type to check
   * @return true if the types are compatibles
   */
  public boolean canGet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canGet(col, c);
  }

  /**
   * Checks if the set method can accept for a specific column values that
   * are compatible with a given type.
   * @param col column index
   * @param c type to check
   * @return true if the types compatibles
   */
  public boolean canSet(int col, Class<?> c) {
    return canGet(col, c);
  }

  /**
   * Checks if the set method can accept for a specific column values that
   * are compatible with a given type.
   * @param field column index
   * @param c type to check
   * @return true if the types compatibles
   */
  public boolean canSet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canSet(col, c);
  }

  /**
   * Retrieves the envelop class of a primitive type.
   * @param c a class
   * @return the associated envelop class
   */
  private Class<?> retrieveObjectType(Class<?> c) {
    if (!c.isPrimitive()) {
      return c;
    } else {
      if (c.equals(int.class)) {
        return Integer.class;
      } else if (c.equals(boolean.class)) {
        return Boolean.class;
      } else if (c.equals(double.class)) {
        return Double.class;
      } else if (c.equals(float.class)) {
        return Float.class;
      } else if (c.equals(long.class)) {
        return Long.class;
      } else if (c.equals(short.class)) {
        return Short.class;
      } else if (c.equals(byte.class)) {
        return Byte.class;
      } else if (c.equals(char.class)) {
        return Character.class;
      } else {
        return c;
      }
    }
  }

  /**
   * Gets the number of columns / data fields in this table.
   * @return the number of columns
   */
  public int getColumnCount() {
    return cols.size();
  }

  /**
   * Gets the default value for a column.
   * @param col column index
   * @return default value for the specified column
   */
  public Object getColumnDefault(int col) {
    try {
      return colToDefault.get(getColumnName(col));
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Get the column number for a given data field name.
   * @param field the name of the column to lookup
   * @return the column number of the column, or -1 if the name is not found
   */
  public int getColumnIndex(String field) {
    for (int i = 0; i < cols.size(); i++) {
      if (cols.get(i).getName().equals(field)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Gets the column name.
   * @param col column index
   * @return name of the column
   */
  public String getColumnName(int col) {
    return cols.get(col).getName();
  }

  /**
   * Gets the data type of the column at the given column index.
   * @param col the column index
   * @return the data type (as a Java Class) of the column
   */
  public Class<?> getColumnType(int col) {
    if (col < getColumnCount()) {
      if (cols.get(col).getValueClass().equals(Integer.class)) {
        return int.class;
      }
      return cols.get(col).getValueClass();
    } else {
      return null;
    }
  }

  /**
   * Get the data type of the column with the given data field name.
   * @param field the column name
   * @return the data type (as a Java Class) of the column
   */
  public Class<?> getColumnType(String field) {
    if (!hasColumn(field)) {
      return null;
    } else {
      return getColumnType(getColumnIndex(field));
    }
  }

  /**
   * Internal method indicating if the given data field is included as a
   * data column.
   * @param name name to seek
   * @return true if the name exists.
   */
  public boolean hasColumn(String name) {
    for (Column col : cols) {
      if (col.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Removes a column.
   * @param field name of column to remove
   * @return true if removed
   */
  public boolean removeColumn(String field) {
    return removeColumn(getColumnIndex(field));
  }

  /**
   * Removes a column.
   * @param col column index
   * @return true if removed
   */
  public boolean removeColumn(int col) {
    try {
      if (col < 0 || col >= getColumnCount()) {
        return false;
      }
      cols.remove(getColumnIndex(getColumnName(col)));
      return true;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Gets the corresponding schema without internal columns.
   * @return a schema only composed by data columns
   */
  public Schema getDataSchema() {
    IvtkObviousSchema cleanSchema = new IvtkObviousSchema();
    for (int i = 0; i < this.getColumnCount(); i++) {
      if (!this.getColumnName(i).startsWith("_")) {
        cleanSchema.addColumn(this.getColumnName(i), this.getColumnType(i),
            this.getColumnDefault(i));
      }
    }
    return cleanSchema;
  }

  @Override
  public int addRow() {
    return -1;
  }

  @Override
  public int addRow(Tuple tuple) {
    if (tuple.getSchema().equals(getSchema())) {
      String colName = tuple.getString("name");
      Class<?> c = (Class<?>) tuple.get("type");
      Object value = tuple.get("default");
      addColumn(colName, c, value);
    }
    this.fireTableEvent(getColumnCount(), getColumnCount(),
        TableListener.ALL_COLUMN, TableListener.INSERT);
    return getColumnCount();
  }

  @Override
  public void addTableListener(TableListener listnr) {
    listener.add(listnr);
  }

  @Override
  public void beginEdit(int col) throws ObviousException {
    this.editing = true;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.beginEdit(col);
    }
  }

  @Override
  public boolean canAddRow() {
    return true;
  }

  @Override
  public boolean canRemoveRow() {
    return true;
  }

  @Override
  public boolean endEdit(int col) throws ObviousException {
    this.editing = false;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.endEdit(col);
    }
    return this.editing;
  }

  @Override
  public int getRowCount() {
    return getColumnCount();
  }

  @Override
  public Schema getSchema() {
    Schema baseSchema = new SchemaImpl();
    baseSchema.addColumn("name", String.class, "defaulCol");
    baseSchema.addColumn("type", Class.class, String.class);
    baseSchema.addColumn("default", Object.class, null);
    return baseSchema;
  }

  @Override
  public Collection<TableListener> getTableListeners() {
    return listener;
  }

  @Override
  public Object getValue(int rowId, String field) {
    return getValue(rowId, getColumnIndex(field));
  }

  @Override
  public Object getValue(int rowId, int col) {
    if (isValueValid(rowId, col)) {
      if (col == name) {
        return getColumnName(rowId);
      } else if (col == type) {
        return getColumnType(rowId);
      } else if (col == defaultVal) {
        return getColumnDefault(rowId);
      }
      return null; // this should not happens
    } else {
      return null;
    }
  }

  @Override
  public boolean isEditing(int col) {
    return editing;
  }

  @Override
  public boolean isValidRow(int rowId) {
    return rowId < getColumnCount();
  }

  @Override
  public boolean isValueValid(int rowId, int col) {
    final int colNumber = 3;
    return isValidRow(rowId) && col < colNumber;
  }

  @Override
  public void removeAllRows() {
    if (canRemoveRow()) {
      for (int i = 0; i < getColumnCount(); i++) {
        removeColumn(i);
      }
      this.fireTableEvent(0, getColumnCount(),
          TableListener.ALL_COLUMN, TableListener.DELETE);
    }
  }

  @Override
  public boolean removeRow(int row) {
    boolean removed = removeColumn(row);
    if (removed) {
      this.fireTableEvent(row, row,
          TableListener.ALL_COLUMN, TableListener.DELETE);
    }
    return removed;
  }

  @Override
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  @Override
  public IntIterator rowIterator() {
    return null;
  }

  @Override
  public void set(int rowId, String field, Object val) {
    set(rowId, getColumnIndex(field), val);
  }

  /**
   * Gets an iterator over the row id of this table matching the given
   * predicate.
   * @param pred an obvious predicate
   * @return an iterator over the rows of this table.
   */
  public IntIterator rowIterator(Predicate pred) {
    return null;
  }

  @Override
  public void set(int rowId, int col, Object val) {
    if (!isValueValid(rowId, col)) {
      return;
    }
    if (col == name) {
      cols.get(rowId).setName(val.toString());
    } else if (col == type) {
      throw new ObviousRuntimeException("Can't change the type of a column");
    } else if (col == defaultVal) {
      colToDefault.put(getColumnName(rowId), val);
    }
    this.fireTableEvent(rowId, rowId, col, TableListener.UPDATE);
  }

  /**
   * Return the underlying implementation.
   * @param inType targeted class
   * @return null
   */
  public Object getUnderlyingImpl(Class<?> inType) {
    return null;
  }

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param eventType the type of modification
   */
  public void fireTableEvent(int start, int end, int col, int eventType) {
    if (this.getTableListeners().isEmpty()) {
      return;
    }
    for (TableListener listnr : this.getTableListeners()) {
      listnr.tableChanged(this, start, end, col, eventType);
    }

  }

}

