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


/**
 * Implementation of an Obvious Schema based on infovis toolkit.
 *
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
    if (c == null || col < 0) {
      return false;
    } else {
        Class<?> columnType = this.getColumnType(col);
        return (columnType == null ? false : c.isAssignableFrom(columnType));
    }
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
      cols.remove(col);
      return true;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  public int addRow() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int addRow(Tuple tuple) {
    // TODO Auto-generated method stub
    return 0;
  }

  public void addTableListener(TableListener listnr) {
    // TODO Auto-generated method stub
    
  }

  public void beginEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
    
  }

  public boolean canAddRow() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean canRemoveRow() {
    // TODO Auto-generated method stub
    return false;
  }

  public void endEdit(int col) throws ObviousException {
    // TODO Auto-generated method stub
    
  }

  public int getRowCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  public Schema getSchema() {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection getTableListeners() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getValue(int rowId, String field) {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getValue(int rowId, int col) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isEditing(int col) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isValidRow(int rowId) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isValueValid(int rowId, int col) {
    // TODO Auto-generated method stub
    return false;
  }

  public void removeAllRows() {
    // TODO Auto-generated method stub
    
  }

  public boolean removeRow(int row) {
    // TODO Auto-generated method stub
    return false;
  }

  public void removeTableListener(TableListener listnr) {
    // TODO Auto-generated method stub
    
  }

  public IntIterator rowIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  public void set(int rowId, String field, Object val) {
    // TODO Auto-generated method stub
    
  }

  public void set(int rowId, int col, Object val) {
    // TODO Auto-generated method stub
    
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    // TODO Auto-generated method stub
    return null;
  }

}

