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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;

/**
 * Example implementation of Tuple Interface.
 * @author Pierre-Luc Hemery
 *
 */
public class TupleImpl implements Tuple  {

  /**
   * Reference table for the tuple.
   */
  private Table table;

  /**
   * Row index for tuple.
   */
  private int row = -1;

  /**
   * Tuple modelisation.
   */
  private Map<Integer, Object> tuple;

  /**
   * Constructor from a table.
   * @param tableIn reference table for this tuple.
   * @param rowId row represented by this tuple.
   */
  public TupleImpl(Table tableIn, int rowId) {
    this.table = tableIn;
    this.row = rowId;
    tuple = new HashMap<Integer, Object>();
    for (int i = 0; i < this.table.getSchema().getColumnCount(); i++) {
      Object data = this.table.getValue(rowId, i);
      tuple.put(i, data);
    }
  }

  /**
   * Constructor from a schema with input values.
   * The values have to be ordered in the correct order for the schema.
   * @param schema schema for the tuple
   * @param values value for the tuple
   */
  public TupleImpl(Schema schema, Object[] values) {
    Table tableIn = new TableImpl(schema);
    for (int i = 0; i < schema.getColumnCount(); i++) {
      tableIn.addRow();
      tableIn.set(0, i, values[i]);
    }
    this.table = tableIn;
    this.row = 0;
    tuple = new HashMap<Integer, Object>();
    for (int i = 0; i < this.table.getSchema().getColumnCount(); i++) {
      Object data = this.table.getValue(0, i);
      tuple.put(i, data);
    }

  }

  /**
   * Returns the schema for this tuple's data.
   * @return the Tuple Schema
   */
   public Schema getSchema() {
     return this.table.getSchema();
   }

  /**
   * Returns the Table instance that backs this Tuple, if it exists.
   * @return the backing Table, or null if there is none.
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * Returns the row index for this Tuple's backing Table, if it exists.
   * @return the backing row index, or -1 if there is no backing table
   * or if this Tuple has been invalidated (i.e., the Tuple's row was
   * deleted from the backing table).
   */
  public int getRow() {
    return this.row;
  }
  /**
   * Indicates if this Tuple is valid. Trying to get or set values on an
   * invalid Tuple will result in a runtime exception.
   * @return true if this Tuple is valid, false otherwise
   */
  public boolean isValid() {
    return this.row == -1;
  }

  /**
   * Calls by the enclosing table to invalidate the tuple.
   */
  protected void invalidate() {
    this.row = -1;
  }

  /**
   * Internal method to check the state of the tuple (valid or not).
   */
  private void validityCheck() {
    if (this.row == -1) {
      throw new IllegalStateException("Invalid tuple!");
    }
  }

  /**
   * Gets column type.
   * @param field to inspect
   * @return type of the field
   */
  public Class<?> getColumnType(String field) {
    return this.table.getSchema().getColumnType(field);
  }

  /**
   * Check if the <code>get</code> method for the given data field returns
   * values that are compatible with a given target type.
   * @param field the data field to check
   * @param type a Class instance to check for compatibility with the
   * data field values.
   * @return true if the data field is compatible with provided type,
   * false otherwise. If the value is true, objects returned by
   * the {@link #get(String)} can be cast to the given type.
   * @see #get(String)
   */
  public boolean canGet(String field, Class<?> type) {
    return this.table.getSchema().canGet(field, type);
  }

  /**
   * Check if the given data field can return primitive <code>boolean</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>boolean</code>
   * values, false otherwise. If true, the {@link #getBoolean(String)} method
   * can be used safely.
   */
  public boolean canGetBoolean(String field) {
    return this.canGet(field, Boolean.class);
  }

  /**
   * Check if the given data field can return <code>Date</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return <code>Date</code>
   * values, false otherwise. If true, the {@link #getDate(String)} method
   * can be used safely.
   */
  public boolean canGetDate(String field) {
    return this.canGet(field, Date.class);
  }

  /**
   * Check if the given data field can return primitive <code>double</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>double</code>
   * values, false otherwise. If true, the {@link #getDouble(String)} method
   * can be used safely.
   */
  public boolean canGetDouble(String field) {
    return this.canGet(field, Double.class);
  }

  /**
   * Check if the given data field can return primitive <code>float</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>float</code>
   * values, false otherwise. If true, the {@link #getFloat(String)} method
   * can be used safely.
   */
  public boolean canGetFloat(String field) {
    return this.canGet(field, Float.class);
  }

  /**
   * Check if the given data field can return primitive <code>integer</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>integer</code>
   * values, false otherwise. If true, the {@link #getInt(String)} method
   * can be used safely.
   */
  public boolean canGetInt(String field) {
    return this.canGet(field, Integer.class);
  }

  /**
   * Check if the given data field can return primitive <code>long</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>long</code>
   * values, false otherwise. If true, the {@link #getLong(String)} method
   * can be used safely.
   */
  public boolean canGetLong(String field) {
    return this.canGet(field, Long.class);
  }

  /**
   * Check if the given data field can return primitive <code>string</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>string</code>
   * values, false otherwise. If true, the {@link #getString(String)} method
   * can be used safely.
   */
  public boolean canGetString(String field) {
    return this.canGet(field, String.class);
  }

  /**
   * Check if the <code>set</code> method for the given data field can
   * accept values of a given target type.
   * @param field the data field to check
   * @param type a Class instance to check for compatibility with the
   * data field values. null means settable for any type.
   * @return true if the data field is compatible with provided type,
   * false otherwise. If the value is true, objects of the given type
   * can be used as parameters of the {@link #set(String, Object)} method.
   * @see #set(String, Object)
   */
  public boolean canSet(String field, Class<?> type) {
    return this.table.getSchema().canSet(field, type);
  }

  /**
   * Check if the <code>setBoolean</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setBoolean(String, boolean)} method can
   * safely be used for the given field, false otherwise.
   */
  public boolean canSetBoolean(String field) {
    return this.canSet(field, Boolean.class);
  }

  /**
   * Check if the <code>setBoolean</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setBoolean(String, boolean)} method can
   * safely be used for the given field, false otherwise.
   */
  public boolean canSetDate(String field) {
    return this.canSet(field, Date.class);
  }

  /**
   * Check if the <code>setDouble</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setDouble(String, double)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetDouble(String field) {
    return this.canSet(field, Double.class);
  }

  /**
   * Check if the <code>setFloat</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setFloat(String, float)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetFloat(String field) {
    return this.canSet(field, Float.class);
  }

  /**
   * Check if the <code>setInt</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setInt(String, int)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetInt(String field) {
    return this.canSet(field, String.class);
  }

  /**
   * Check if the <code>setLong</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setLong(String, long)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetLong(String field) {
    return this.canSet(field, Long.class);
  }

  /**
   * Check if the <code>setString</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setString(String, String)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetString(String field) {
    return this.canSet(field, Long.class);
  }

  /**
   * Get the data value at the given field as an Object.
   * @param field the data field to retrieve
   * @return the data value as an Object. The concrete type of this
   * Object is dependent on the underlying data column used.
   * @see #canGet(String, Class)
   * @see #getColumnType(String)
   */
  public Object get(String field) {
    this.validityCheck();
    return this.table.getValue(row, field);
  }

  /**
   * Get the data value at the given column number as an Object.
   * @param col the column number
   * @return the data value as an Object. The concrete type of this
   * Object is dependent on the underlying data column used.
   * @see #canGet(String, Class)
   * @see #getColumnType(int)
   */
  public Object get(int col) {
    this.validityCheck();
    return this.table.getValue(row, col);
  }

  /**
   * Get the data value at the given field as a <code>boolean</code>.
   * @param field the data field to retrieve
   * @see #canGetBoolean(String)
   * @return boolean
   */
  public boolean getBoolean(String field) {
    this.validityCheck();
    return (Boolean) this.get(field);
  }

  /**
   * Get the data value at the given field as a <code>boolean</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetBoolean(String)
   * @return boolean
   */
  public boolean getBoolean(int col) {
    this.validityCheck();
    return (Boolean) this.get(col);
  }

  /**
   * Get the data value at the given field as a <code>date</code>.
   * @param field the data field to retrieve
   * @see #canGetDate(String)
   * @return date
   */
  public Date getDate(String field) {
    this.validityCheck();
    return (Date) this.get(field);
  }

  /**
   * Get the data value at the given field as a <code>date</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetDate(String)
   * @return date
   */
  public Date getDate(int col) {
    this.validityCheck();
    return (Date) this.get(col);
  }

  /**
   * Get the default value for the given data field.
   * @param field the data field
   * @return the default value, as an Object, used to populate rows
   * of the data field.
   */
  public Object getDefault(String field) {
    this.validityCheck();
    return this.table.getSchema().getColumnType(field);
  }

  /**
   * Get the data value at the given field as a <code>double</code>.
   * @param field the data field to retrieve
   * @see #canGetDouble(String)
   * @return double
   */
  public double getDouble(String field) {
    this.validityCheck();
    return (Double) this.get(field);
  }

  /**
   * Get the data value at the given field as a <code>double</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetDouble(String)
   * @return double
   */
  public double getDouble(int col) {
    this.validityCheck();
    return (Double) this.get(col);
  }

  /**
   * Get the data value at the given field as a <code>float</code>.
   * @param field the data field to retrieve
   * @see #canGetFloat(String)
   * @return float
   */
  public float getFloat(String field) {
    this.validityCheck();
    return (Float) this.get(field);
  }

  /**
   * Get the data value at the given field as a <code>float</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetFloat(String)
   * @return float
   */
  public float getFloat(int col) {
    this.validityCheck();
    return (Float) this.get(col);
  }

  /**
   * Get the data value at the given field as an <code>integer</code>.
   * @param field the data field to retrieve
   * @see #canGetInt(String)
   * @return integer
   */
  public int getInt(String field) {
    this.validityCheck();
    return (Integer) this.get(field);
  }

  /**
   * Get the data value at the given field as an <code>integer</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetInt(String)
   * @return integer
   */
  public int getInt(int col) {
    this.validityCheck();
    return (Integer) this.get(col);
  }

  /**
   * Get the data value at the given field as a <code>long</code>.
   * @param field the data field to retrieve
   * @see #canGetLong(String)
   * @return long
   */
  public long getLong(String field) {
    this.validityCheck();
    return (Long) this.get(field);
  }

  /**
   * Get the data value at the given field as a <code>long</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetLong(String)
   * @return long
   */
  public long getLong(int col) {
    this.validityCheck();
    return (Long) this.get(col);
  }

  /**
   * Get the data value at the given field as a <code>string</code>.
   * @param field the data field to retrieve
   * @see #canGetString(String)
   * @return string
   */
  public String getString(String field) {
    this.validityCheck();
    return (String) this.get(field);
  }

  /**
   * Get the data value at the given field as a <code>string</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetString(String)
   * @return string
   */
  public String getString(int col) {
    this.validityCheck();
    return (String) this.get(col);
  }

  /**
   * Revert this tuple's value for the given field to the default value
   * for the field.
   * @param field the data field
   * @see #getDefault(String)
   */
  public void revertToDefault(String field) {
    this.validityCheck();
    this.set(field, this.getDefault(field));
  }

  /**
   * Set the value of a given data field.
   * @param field the data field to set
   * @param value the value for the field. .
   * @see #canSet(String, Class)
   * @see #getColumnType(String)
   */
  public void set(String field, Object value) {
    this.validityCheck();
    this.table.set(row, field, value);
  }

  /**
   * Set the value of at the given column number.
   * @param col the column number
   * @param value the value for the field.
   * @see #canSet(String, Class)
   * @see #getColumnType(String)
   */
  public void set(int col, Object value) {
    this.validityCheck();
    this.table.set(row, col, value);
  }

  /**
   * Set the data value of the given field with a <code>boolean</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetBoolean(String)
   */
  public void setBoolean(String field, boolean val) {
    this.validityCheck();
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with a <code>boolean</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetBoolean(String)
   */
  public void setBoolean(int col, boolean val) {
    this.validityCheck();
    this.set(col, val);
  }

  /**
   * Set the data value of the given field with a <code>Date</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetDate(String)
   */
  public void setDate(String field, Date val) {
    this.validityCheck();
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with a <code>Date</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetDate(String)
   */
  public void setDate(int col, Date val) {
    this.validityCheck();
    this.set(col, val);
  }

  /**
   * Set the data value of the given field with a <code>double</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetDouble(String)
   */
  public void setDouble(String field, double val) {
    this.validityCheck();
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with a <code>double</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetDouble(String)
   */
  public void setDouble(int col, double val) {
    this.validityCheck();
    this.set(col, val);
  }

  /**
   * Set the data value of the given field with a <code>float</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetFloat(String)
   */
  public void setFloat(String field, float val) {
    this.validityCheck();
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with a <code>float</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetFloat(String)
   */
  public void setFloat(int col, float val) {
    this.validityCheck();
    this.set(col, val);
  }

  /**
   * Set the data value of the given field with an <code>int</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetInt(String)
   */
  public void setInt(String field, int val) {
    this.validityCheck();
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with an <code>int</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetInt(String)
   */
  public void setInt(int col, int val) {
    this.validityCheck();
    this.set(col, val);
  }

  /**
   * Set the data value of the given field with a <code>long</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetLong(String)
   */
  public void setLong(String field, long val) {
    this.validityCheck();
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with a <code>long</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetLong(String)
   */
  public void setLong(int col, long val) {
    this.validityCheck();
    this.set(col, val);
  }

  /**
   * Set the data value of the given field with a <code>String</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetString(String)
   */
  public void setString(String field, String val) {
    this.set(field, val);
  }

  /**
   * Set the data value of the given field with a <code>String</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetString(String)
   */
  public void setString(int col, String val) {
    this.set(col, val);
  }


}
