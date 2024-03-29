/*
* Copyright (c) 2011, INRIA
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

package obvious.prefuse.utils.wrappers;

import java.util.Date;

import obvious.impl.TupleImpl;
import prefuse.data.Table;

/**
 * Wrapper for obvious tuple to pref tuple.
 * @author Hemery
 *
 */
public class WrapToPrefTuple implements prefuse.data.Tuple {

  /**
   * Obvious tuple to wrap.
   */
  private TupleImpl tuple;

  /**
   * Index of the tuple.
   */
  private Integer rowId = null;

  /**
   * Constructor.
   * @param inTuple obvious tuple to wrap
   */
  public WrapToPrefTuple(TupleImpl inTuple) {
    this.tuple = inTuple;
    this.rowId = inTuple.getRow();
  }

  /**
   * Gets the underlying obvious tuple.
   * @return the underlying obvious tuple
   */
  protected obvious.data.Tuple getObviousTuple() {
    return tuple;
  }

  /**
   * Constructor.
   * @param inTuple obvious tuple to wrap
   * @param row index of the tuple
   */
  public WrapToPrefTuple(TupleImpl inTuple, int row) {
    this.tuple = inTuple;
    this.rowId = row;
  }

  @Override
  public int getColumnCount() {
    return tuple.getSchema().getColumnCount();
  }

  @Override
  public int getColumnIndex(String arg0) {
    return tuple.getSchema().getColumnIndex(arg0);
  }

  @Override
  public String getColumnName(int arg0) {
    return tuple.getSchema().getColumnName(arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getColumnType(int arg0) {
    return tuple.getSchema().getColumnType(arg0);
  }

  public Object get(int arg0) {
    return tuple.get(arg0);
  }

  public void set(int arg0, Object arg1) {
    tuple.set(arg0, arg1);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean canGet(String field, Class type) {
    return tuple.canGet(field, type);
  }

  @Override
  public boolean canGetBoolean(String arg0) {
    return tuple.canGetBoolean(arg0);
  }

  @Override
  public boolean canGetDate(String arg0) {
    return tuple.canGetDate(arg0);
  }

  @Override
  public boolean canGetDouble(String arg0) {
    return tuple.canGetDouble(arg0);
  }

  @Override
  public boolean canGetFloat(String arg0) {
    return tuple.canGetFloat(arg0);
  }

  @Override
  public boolean canGetInt(String arg0) {
    return tuple.canGetInt(arg0);
  }

  @Override
  public boolean canGetLong(String arg0) {
    return tuple.canGetLong(arg0);
  }

  @Override
  public boolean canGetString(String arg0) {
    return tuple.canGetString(arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean canSet(String arg0, Class arg1) {
    return tuple.canSet(arg0, arg1);
  }

  @Override
  public boolean canSetBoolean(String arg0) {
    return tuple.canSetBoolean(arg0);
  }

  @Override
  public boolean canSetDate(String arg0) {
    return tuple.canSetDate(arg0);
  }

  @Override
  public boolean canSetDouble(String arg0) {
    return tuple.canSetDouble(arg0);
  }

  @Override
  public boolean canSetFloat(String arg0) {
    return tuple.canSetFloat(arg0);
  }

  @Override
  public boolean canSetInt(String arg0) {
    return tuple.canSetInt(arg0);
  }

  @Override
  public boolean canSetLong(String arg0) {
    return tuple.canSetLong(arg0);
  }

  @Override
  public boolean canSetString(String arg0) {
    return tuple.canSetString(arg0);
  }

  @Override
  public Object get(String arg0) {
    return tuple.get(arg0);
  }

  @Override
  public boolean getBoolean(String arg0) {
    return tuple.getBoolean(arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getColumnType(String arg0) {
    return tuple.getSchema().getColumnType(arg0);
  }

  @Override
  public Date getDate(String arg0) {
    return tuple.getDate(arg0);
  }

  @Override
  public Object getDefault(String arg0) {
    return tuple.getSchema().getColumnDefault(
        tuple.getSchema().getColumnIndex(arg0));
  }

  @Override
  public double getDouble(String arg0) {
    return tuple.getDouble(arg0);
  }

  @Override
  public float getFloat(String arg0) {
    return tuple.getFloat(arg0);
  }

  @Override
  public int getInt(String arg0) {
    return tuple.getInt(arg0);
  }

  @Override
  public long getLong(String arg0) {
    return tuple.getLong(arg0);
  }

  @Override
  public int getRow() {
    return rowId;
  }

  @Override
  public prefuse.data.Schema getSchema() {
    return this.getTable().getSchema();
  }

  @Override
  public String getString(String arg0) {
    return tuple.getString(arg0);
  }

  @Override
  public Table getTable() {
    return new WrapToPrefTable(tuple.getTable());
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public void revertToDefault(String arg0) {
    return;
  }

  @Override
  public void set(String arg0, Object arg1) {
    tuple.set(arg0, arg1);
  }

  @Override
  public void setBoolean(String arg0, boolean arg1) {
    tuple.setBoolean(arg0, arg1);
  }

  @Override
  public void setDate(String arg0, Date arg1) {
    tuple.setDate(arg0, arg1);
  }

  @Override
  public void setDouble(String arg0, double arg1) {
    tuple.setDouble(arg0, arg1);
  }

  @Override
  public void setFloat(String arg0, float arg1) {
    tuple.setFloat(arg0, arg1);
  }

  @Override
  public void setInt(String arg0, int arg1) {
    tuple.setInt(arg0, arg1);
  }

  @Override
  public void setLong(String arg0, long arg1) {
    tuple.setLong(arg0, arg1);
  }

  @Override
  public void setString(String arg0, String arg1) {
    tuple.setString(arg0, arg1);
  }

  public boolean getBoolean(int arg0) {
    return tuple.getBoolean(arg0);
  }

  public Date getDate(int arg0) {
    return tuple.getDate(arg0);
  }

  public double getDouble(int arg0) {
    return tuple.getDouble(arg0);
  }

  public float getFloat(int arg0) {
    return tuple.getFloat(arg0);
  }

  public int getInt(int arg0) {
    return tuple.getInt(arg0);
  }

  public long getLong(int arg0) {
    return tuple.getLong(arg0);
  }

  public String getString(int arg0) {
    return tuple.getString(arg0);
  }

  public void setBoolean(int arg0, boolean arg1) {
    tuple.setBoolean(arg0, arg1);
  }

  public void setDate(int arg0, Date arg1) {
    tuple.setDate(arg0, arg1);
  }

  public void setDouble(int arg0, double arg1) {
    tuple.setDouble(arg0, arg1);
  }

  public void setFloat(int arg0, float arg1) {
    tuple.setFloat(arg0, arg1);
  }

  public void setInt(int arg0, int arg1) {
    tuple.setInt(arg0, arg1);
  }

  public void setLong(int arg0, long arg1) {
    tuple.setLong(arg0, arg1);
  }

  public void setString(int arg0, String arg1) {
    tuple.setString(arg0, arg1);
  }

  @Override
  public Object getValueAt(int arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueAt(int arg0, Object arg1) {
    // TODO Auto-generated method stub
    
  }

}
