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

import java.util.Collection;
import java.util.ArrayList;

import obvious.data.Schema;
import obvious.data.util.IntIterator;
import obvious.data.util.TableListener;

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
   * Contains name of columns.
   */
  private ArrayList<String> names;
  /**
   * Contains type of columns.
   */
  private ArrayList<Class> types;
  /**
   * Contains default value of columns.
   */
  private ArrayList<Object> defaultValues;

    // ------------------------------------------------------------------------
    // Constructors

  /**
   * Constructor of SchemaImpl.
   */
  public SchemaImpl() {
    this.names = new ArrayList<String>();
    this.types = new ArrayList<Class>();
    this.defaultValues = new ArrayList<Object>();
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
  public Class getColumnType(int col) {
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
    } catch (IndexOutOfBoundsException e) { return null;}

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
   * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.Class, java.lang.Object)
   * @return the column index
   * @throws Exception when the column name already exists.
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
    /* Not implemented in this example.
     * @return current schema
     */
    public Schema getSchema() {
    	return this;
    }

    /**
    /* Not implemented in this example.
     * @return 0
     */
    public int getRowCount() {
    	return 0;
    }

    /**
    /* Not implemented in this example.
     * @return null
     */
    public IntIterator rowIterator() {
    	return null;
    }

    /**
    /* Not implemented in this example.
     * @return false
     */
    public boolean isValidRow(int rowId) {
    	return false;
    }

    /**
    /* Not implemented in this example.
     * @return null
     */
    public Object getValue(int rowId, String field) {
    	return null;
    }

    /**
    /* Not implemented in this example.
     * @return null
     */
    public Object getValue(int rowId, int col) {
    	return null;
    }

    /**
    /* Not implemented in this example.
     * @return false
     */
    public boolean isValueValid(int rowId, int col) {
    	return false;
    }

    /**
    /* Not implemented in this example.
     */
    public void beginEdit(int col) {
    }

    /**
    /* Not implemented in this example.
     */
    public void endEdit(int col) {
    }

    /**
    /* Not implemented in this example.
     * @return false
     */
    public boolean isEditing(int col) {
    	return false;
    }

    /**
    /* Not implemented in this example.
     */
    public void addTableListener(TableListener listnr) {
    }

    /**
    /* Not implemented in this example.
     */
    public void removeTableListener(TableListener listnr) {
    }

    /**
    /* Not implemented in this example.
     * @return null
     */
    public Collection<TableListener> getTableListeners() {
    	return null;
    }

    /**
    /* Not implemented in this example.
     * @return false
     */
    public boolean canAddRow() {
    	return false;
    }

    /**
    /* Not implemented in this example.
     * @return false
     */
    public boolean canRemoveRow() {
    	return false;
    }

    /**
    /* Not implemented in this example.
     * @return 0
     */
    public int addRow() {
    	return 0;
    }

    /**
    /* Not implemented in this example.
     * @param row
     * @return false
     */
    public boolean removeRow(int row) {
    	return false;
    }

    /**
    /* Not implemented in this example.
     */
    public void removeAllRows() { }

    /**
    /* Not implemented in this example.
     */
    public void set(int rowId, String field, Object val) {}

    /**
    /* Not implemented in this example.
     * @param rowId
     * @param col
     * @param val
     */
    public void set(int rowId, int col, Object val) { }
}
