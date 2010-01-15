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

import java.util.Date;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;

/**
 * Implementation of an Obvious Tuple based on Prefuse toolkit.
 * Obvious and Prefuse tuples are pretty close, this class is mainly a wrapper.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousTuple implements Tuple {

  /**
   * Prefuse tuple wrapped around obvious tuple.
   */
  private prefuse.data.Tuple tuple;

  /**
   * Constructor.
   * @param inputTuple prefuse Tuple
   */
  public PrefuseObviousTuple(prefuse.data.Tuple inputTuple) {
    this.tuple = inputTuple;
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
    return tuple.canGet(field, type);
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
    return tuple.canGetBoolean(field);
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
    return tuple.canGetDate(field);
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
    return tuple.canGetDouble(field);
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
    return tuple.canGetFloat(field);
  }

  /**
   * Check if the given data field can return primitive <code>int</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return primitive <code>int</code>
   * values, false otherwise. If true, the {@link #getInt(String)} method
   * can be used safely.
   */
  public boolean canGetInt(String field) {
    return tuple.canGetInt(field);
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
    return tuple.canGetLong(field);
  }

  /**
   * Check if the given data field can return <code>String</code>
   * values.
   * @param field the data field to check
   * @return true if the data field can return <code>String</code>
   * values, false otherwise. If true, the {@link #getString(String)} method
   * can be used safely.
   */
  public boolean canGetString(String field) {
    return tuple.canGetString(field);
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
    return tuple.canSet(field, type);
  }

  /**
   * Check if the <code>setBoolean</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setBoolean(String, boolean)} method can
   * safely be used for the given field, false otherwise.
   */
  public boolean canSetBoolean(String field) {
    return tuple.canSetBoolean(field);
  }

  /**
   * Check if the <code>setDate</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setDate(String, Date)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetDate(String field) {
    return tuple.canSetDate(field);
  }

  /**
   * Check if the <code>setDouble</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setDouble(String, double)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetDouble(String field) {
    return tuple.canSetDouble(field);
  }

  /**
   * Check if the <code>setFloat</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setFloat(String, float)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetFloat(String field) {
    return tuple.canSetFloat(field);
  }

  /**
   * Check if the <code>setInt</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setInt(String, int)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetInt(String field) {
    return tuple.canSetInt(field);
  }

  /**
   * Check if the <code>setLong</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setLong(String, long)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetLong(String field) {
    return tuple.canSetLong(field);
  }

  /**
   * Check if the <code>setString</code> method can safely be used for the
   * given data field.
   * @param field the data field to check
   * @return true if the {@link #setString(String, String)} method can safely
   * be used for the given field, false otherwise.
   */
  public boolean canSetString(String field) {
    return tuple.canSetString(field);
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
    return tuple.get(field);
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
    return tuple.getValueAt(col);
  }

  /**
   * Get the data value at the given field as a <code>boolean</code>.
   * @param field the data field to retrieve
   * @see #canGetBoolean(String)
   * @return boolean
   */
  public boolean getBoolean(String field) {
    return tuple.getBoolean(field);
  }

  /**
   * Get the data value at the given field as a <code>boolean</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetBoolean(String)
   * @return boolean
   */
  public boolean getBoolean(int col) {
    return this.getBoolean(tuple.getColumnName(col));
  }

  /**
   * Gets column type.
   * @param field to inspect
   * @return type of the field
   */
  public Class<?> getColumnType(String field) {
    return tuple.getColumnType(field);
  }

  /**
   * Get the data value at the given field as a <code>Date</code>.
   * @param field the data field to retrieve
   * @see #canGetDate(String)
   * @return date
   */
  public Date getDate(String field) {
    return tuple.getDate(field);
  }

  /**
   * Get the data value at the given field as a <code>Date</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetDate(String)
   * @return date
   */
  public Date getDate(int col) {
    return this.getDate(tuple.getColumnName(col));
  }

  /**
   * Get the default value for the given data field.
   * @param field the data field
   * @return the default value, as an Object, used to populate rows
   * of the data field.
   */
  public Object getDefault(String field) {
    return tuple.getDefault(field);
  }

  /**
   * Get the data value at the given field as a <code>double</code>.
   * @param field the data field to retrieve
   * @see #canGetDouble(String)
   * @return double
   */
  public double getDouble(String field) {
    return tuple.getDouble(field);
  }

  /**
   * Get the data value at the given field as a <code>double</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetDouble(String)
   * @return double
   */
  public double getDouble(int col) {
    return this.getDouble(tuple.getColumnName(col));
  }

  /**
   * Get the data value at the given field as a <code>float</code>.
   * @param field the data field to retrieve
   * @see #canGetFloat(String)
   * @return float
   */
  public float getFloat(String field) {
    return tuple.getFloat(field);
  }

  /**
   * Get the data value at the given field as a <code>float</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetFloat(String)
   * @return float
   */
  public float getFloat(int col) {
    return this.getFloat(tuple.getColumnName(col));
  }

  /**
   * Get the data value at the given field as an <code>int</code>.
   * @param field the data field to retrieve
   * @see #canGetInt(String)
   * @return int
   */
  public int getInt(String field) {
    return tuple.getInt(field);
  }

  /**
   * Get the data value at the given field as an <code>int</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetInt(String)
   * @return int
   */
  public int getInt(int col) {
    return this.getInt(tuple.getColumnName(col));
  }

  /**
   * Get the data value at the given field as a <code>long</code>.
   * @param field the data field to retrieve
   * @see #canGetLong(String)
   * @return long
   */
  public long getLong(String field) {
    return tuple.getLong(field);
  }

  /**
   * Get the data value at the given field as a <code>long</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetLong(String)
   * @return long
   */
  public long getLong(int col) {
    return this.getLong(tuple.getColumnName(col));
  }

  /**
   * Returns the row index for this Tuple's backing Table, if it exists.
   * @return the backing row index, or -1 if there is no backing table
   * or if this Tuple has been invalidated (i.e., the Tuple's row was
   * deleted from the backing table).
   */
  public int getRow() {
    return tuple.getRow();
  }

  /**
   * Returns the schema for this tuple's data.
   * @return the Tuple Schema
   */
  public Schema getSchema() {
    return new PrefuseObviousSchema(tuple.getSchema());
  }

  /**
   * Get the data value at the given field as a <code>String</code>.
   * @param field the data field to retrieve
   * @see #canGetString(String)
   * @return string
   */
  public String getString(String field) {
    return tuple.getString(field);
  }

  /**
   * Get the data value at the given field as a <code>String</code>.
   * @param col the column number of the data field to retrieve
   * @see #canGetString(String)
   * @return string
   */
  public String getString(int col) {
    return this.getString(tuple.getColumnName(col));
  }

  /**
   * Returns the Table instance that backs this Tuple, if it exists.
   * @return the backing Table, or null if there is none.
   */
  public Table getTable() {
    return new PrefuseObviousTable(tuple.getTable());
  }

  /**
   * Indicates if this Tuple is valid. Trying to get or set values on an
   * invalid Tuple will result in a runtime exception.
   * @return true if this Tuple is valid, false otherwise
   */
  public boolean isValid() {
    return tuple.isValid();
  }

  /**
   * Revert this tuple's value for the given field to the default value
   * for the field.
   * @param field the data field
   * @see #getDefault(String)
   */
  public void revertToDefault(String field) {
    tuple.revertToDefault(field);
  }

  /**
   * Set the value of a given data field.
   * @param field the data field to set
   * @param value the value for the field.
   * @see #canSet(String, Class)
   * @see #getColumnType(String)
   */
  public void set(String field, Object value) {
    tuple.set(field, value);
  }

  /**
   * Set the value of at the given column number.
   * @param col the column number
   * @param value the value for the field.
   * @see #canSet(String, Class)
   * @see #getColumnType(String)
   */
  public void set(int col, Object value) {
    this.set(tuple.getColumnName(col), value);
  }

  /**
   * Set the data value of the given field with a <code>boolean</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetBoolean(String)
   */
  public void setBoolean(String field, boolean val) {
    tuple.setBoolean(field, val);
  }

  /**
   * Set the data value of the given field with a <code>boolean</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetBoolean(String)
   */
  public void setBoolean(int col, boolean val) {
    this.setBoolean(tuple.getColumnName(col), val);
  }

  /**
   * Set the data value of the given field with a <code>Date</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetDate(String)
   */
  public void setDate(String field, Date val) {
    tuple.setDate(field, val);
  }

  /**
   * Set the data value of the given field with a <code>Date</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetDate(String)
   */
  public void setDate(int col, Date val) {
    this.setDate(tuple.getColumnName(col), val);
  }

  /**
   * Set the data value of the given field with a <code>double</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetDouble(String)
   */
  public void setDouble(String field, double val) {
    tuple.setDouble(field, val);
  }

  /**
   * Set the data value of the given field with a <code>double</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetDouble(String)
   */
  public void setDouble(int col, double val) {
    this.setDouble(tuple.getColumnName(col), val);
  }

  /**
   * Set the data value of the given field with a <code>float</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetFloat(String)
   */
  public void setFloat(String field, float val) {
    tuple.setFloat(field, val);
  }

  /**
   * Set the data value of the given field with a <code>float</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetFloat(String)
   */
  public void setFloat(int col, float val) {
    this.setFloat(tuple.getColumnName(col), val);
  }

  /**
   * Set the data value of the given field with an <code>int</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetInt(String)
   */
  public void setInt(String field, int val) {
    tuple.setInt(field, val);
  }

  /**
   * Set the data value of the given field with an <code>int</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetInt(String)
   */
  public void setInt(int col, int val) {
    this.setInt(col, val);
  }

  /**
   * Set the data value of the given field with a <code>long</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetLong(String)
   */
  public void setLong(String field, long val) {
    tuple.setLong(field, val);
  }

  /**
   * Set the data value of the given field with a <code>long</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetLong(String)
   */
  public void setLong(int col, long val) {
    this.setLong(tuple.getColumnName(col), val);
  }

  /**
   * Set the data value of the given field with a <code>String</code>.
   * @param field the data field to set
   * @param val the value to set
   * @see #canSetString(String)
   */
  public void setString(String field, String val) {
    tuple.setString(field, val);
  }

  /**
   * Set the data value of the given field with a <code>String</code>.
   * @param col the column number of the data field to set
   * @param val the value to set
   * @see #canSetString(String)
   */
  public void setString(int col, String val) {
    this.setString(tuple.getColumnName(col), val);
  }

}
