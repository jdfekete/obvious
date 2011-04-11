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

package obvious.improvise.data;

import java.util.ArrayList;
import java.util.Collection;

import oblivion.db.Record;
import oblivion.db.TableIterator;
import obvious.ObviousException;
import obvious.ObviousRuntimeException;
import obvious.data.Schema;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.SchemaImpl;

/**
 * Obvious schema for improvise implementation.
 * @author Hemery
 *
 */
public class ImproviseObviousSchema implements Schema {

  /**
   * Wrapped improvise schema.
   */
  private oblivion.db.Schema schema;

  /**
   * Table dedicated to the schema.
   */
  private oblivion.db.MemoryTable schemaTable;

  /**
   * Number of column in the schema's table.
   */
  private static final Integer SCHEMA_COL_NUMBER = 3;
  
  /**
   * Is editing boolean.
   */
  private boolean editing = false;

  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener = new ArrayList<TableListener>();

  /**
   * Index of schema column.
   */
  private final int name = 0, type = 1, defaultVal = 2;

  /**
   * Constructor.
   */
  public ImproviseObviousSchema() {
    this(new oblivion.db.Schema());
  }

  /**
   * Constructor from an improvise (oblivion) schema.
   * @param inSchema improvise (oblivion) schema to wrap.
   */
  public ImproviseObviousSchema(oblivion.db.Schema inSchema) {
    this.schema = inSchema;
    oblivion.db.Attribute[] attributes =
      new oblivion.db.Attribute[SCHEMA_COL_NUMBER];
    attributes[0] = new oblivion.db.Attribute(String.class, "name");
    attributes[1] = new oblivion.db.Attribute(Class.class, "type");
    attributes[2] = new oblivion.db.Attribute(Object.class, "default");
    oblivion.db.Schema schemaOfTable = new oblivion.db.Schema(attributes);
    schemaTable = new oblivion.db.MemoryTable(schemaOfTable);
    for (int i = 0; i < schema.size(); i++) {
      Record record = new Record(schemaOfTable, true);
      record.setID(i);
      record.setValue(0, schema.nameAt(i));
      record.setValue(1, schema.typeAt(i));
      Object nullValue = null;
      record.setValue(SCHEMA_COL_NUMBER - 1, nullValue);
      schemaTable.add(record);
    }
  }

  @Override
  public int addColumn(String name, Class<?> type, Object defaultValue) {
    try {
      Record record = new Record(schemaTable.getSchema(), true);
      record.setID(this.getColumnCount());
      record.setValue(0, name);
      record.setValue(1, type);
      record.setValue(SCHEMA_COL_NUMBER - 1, defaultValue);
      schemaTable.add(record);
      return schemaTable.size();
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  @Override
  public boolean canGet(int col, Class<?> c) {
    if (c == null || col < 0) {
      return false;
    } else {
      try {
        Class<?> columnType = this.getColumnType(col);
        return (columnType == null ? false : c.isAssignableFrom(columnType));
      } catch (Exception e) {
        return false;
      }
    }
  }

  @Override
  public boolean canGet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canGet(col, c);
  }

  @Override
  public boolean canSet(int col, Class<?> c) {
    if (c == null || col < 0) {
      return false;
    } else {
        try {
          Class<?> columnType = this.getColumnType(col);
          return (columnType == null ? false : c.isAssignableFrom(columnType));
        } catch (Exception e) {
          return false;
        }
    }
  }

  @Override
  public boolean canSet(String field, Class<?> c) {
    int col = this.getColumnIndex(field);
    return this.canSet(col, c);
  }

  @Override
  public int getColumnCount() {
    return schemaTable.size();
  }

  @Override
  public Object getColumnDefault(int col) {
    return schemaTable.at(col).getValue(SCHEMA_COL_NUMBER - 1);
  }

  @Override
  public int getColumnIndex(String field) {
    TableIterator it = schemaTable.scan();
    while (it.hasMoreElements()) {
      Record currentRecord = (Record) it.nextElement();
      if (currentRecord.getValue(0).equals(field)) {
        return currentRecord.getID();
      }
    }
    return -1;
  }

  @Override
  public String getColumnName(int col) {
    TableIterator it = schemaTable.scan();
    while (it.hasMoreElements()) {
      Record currentRecord = (Record) it.nextElement();
      if (currentRecord.getID() == col) {
        return currentRecord.getString(0);
      }
    }
    return null;
  }

  @Override
  public Class<?> getColumnType(int col) {
    return (Class<?>) schemaTable.at(col).getValue(1);
  }

  @Override
  public Class<?> getColumnType(String field) {
    return this.getColumnType(getColumnIndex(field));
  }

  @Override
  public boolean hasColumn(String name) {
    TableIterator it = schemaTable.scan();
    while (it.hasMoreElements()) {
      Record currentRecord = (Record) it.nextElement();
      if (currentRecord.getString(0).equals(name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean removeColumn(String field) {
    return removeColumn(getColumnIndex(field));
  }

  @Override
  public boolean removeColumn(int col) {
    if (col < 0) {
      return false;
    } else {
      TableIterator it = schemaTable.scan();
      while (it.hasMoreElements()) {
        Record currentRecord = (Record) it.nextElement();
        System.out.println(col + " " + currentRecord.getID());
        if (currentRecord.getID() == col) {
          schemaTable.remove(currentRecord);
          return true;
        }
      }
      return false;
    }
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
    return this.getColumnCount();
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
    return this.editing;
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
  public IntIterator rowIterator(Predicate pred) {
    return null;
  }

  @Override
  public void set(int rowId, String field, Object val) {
    set(rowId, getColumnIndex(field), val);
  }

  @Override
  public void set(int rowId, int col, Object val) {
    return;
  }

  @Override
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(oblivion.db.Schema.class)) {
      return schema;
    }
    return null;
  }

  /**
   * Gets the corresponding schema without internal columns.
   * @return a schema only composed by data columns
   */
  public Schema getDataSchema() {
    return this;
  }
  
  @Override
  public void fireTableEvent(int start, int end, int col, int type) {
   if (this.getTableListeners().isEmpty()) {
     return;
   }
   for (TableListener listnr : this.getTableListeners()) {
     listnr.tableChanged(this, start, end, col, type);
   }
  }

}
