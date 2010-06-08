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

import oblivion.db.Attribute;
import oblivion.db.MemoryTable;
import oblivion.db.Record;
import oblivion.db.TableIterator;
import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.impl.TupleImpl;

/**
 * ImproviseObviousTable.
 * @author Hemery
 *
 */
public class ImproviseObviousTable implements Table {

  /**
   * Wrapped Improvise (oblivion) table.
   */
  private oblivion.db.MemoryTable oblivionTable;

  /**
   * Schema of the table.
   */
  private Schema schema;

  /**
   * List of obvious listeners.
   */
  private Collection<TableListener> listeners = new ArrayList<TableListener>();

  /**
   * Is the table being edited.
   */
  private boolean editing = false;

  /**
   * Constructor.
   * @param inSchema obvious schema of the table.
   */
  public ImproviseObviousTable(Schema inSchema) {
    this.schema = inSchema;
    Attribute[] attributes = new Attribute[schema.getColumnCount()];
    for (int i = 0; i < schema.getColumnCount(); i++) {
      attributes[i] = new Attribute(schema.getColumnType(i),
          schema.getColumnName(i));
    }
    oblivion.db.Schema oblivionSchema = new oblivion.db.Schema(attributes);
    oblivionTable = new MemoryTable(oblivionSchema);
  }

  @Override
  public int addRow() {
    Object[] values = new Object[getSchema().getColumnCount()];
    for (int i = 0; i < getSchema().getColumnCount(); i++) {
      values[i] = getSchema().getColumnDefault(i);
    }
    Tuple tuple = new TupleImpl(getSchema(), values);
    return  addRow(tuple);
  }

  @Override
  public int addRow(Tuple tuple) {
    Record record = new Record(oblivionTable.getSchema(), true);
    record.setID(this.getRowCount());
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      record.setValue(i, tuple.get(i));
    }
    oblivionTable.add(record);
    return record.getID();
  }

  @Override
  public void addTableListener(TableListener listnr) {
    listeners.add(listnr);
  }

  @Override
  public void beginEdit(int col) throws ObviousException {
    editing = true;
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
  public void endEdit(int col) throws ObviousException {
    editing = false;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.endEdit(col);
    }
  }

  @Override
  public int getRowCount() {
    return oblivionTable.size();
  }

  @Override
  public Schema getSchema() {
    return this.schema;
  }

  @Override
  public Collection<TableListener> getTableListeners() {
    return listeners;
  }

  @Override
  public Object getValue(int rowId, String field) {
    return getValue(rowId, this.getSchema().getColumnIndex(field));
  }

  @Override
  public Object getValue(int rowId, int col) {
    if (isValueValid(rowId, col)) {
      Record record = oblivionTable.at(rowId);
      return record.getValue(col);
    }
    return null;
  }

  @Override
  public boolean isEditing(int col) {
    return editing;
  }

  @Override
  public boolean isValidRow(int rowId) {
    TableIterator it = oblivionTable.scan();
    while (it.hasMoreElements()) {
      Record currentRecord = (Record) it.nextElement();
      if (currentRecord.getID() == rowId) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isValueValid(int rowId, int col) {
    return isValidRow(rowId) && (col < getSchema().getColumnCount());
  }

  @Override
  public void removeAllRows() {
    TableIterator it = oblivionTable.scan();
    while (it.hasMoreElements()) {
      Record currentRecord = it.getRecord();
      oblivionTable.remove(currentRecord);
    }
  }

  @Override
  public boolean removeRow(int row) {
    oblivionTable.remove(row);
    return !isValidRow(row);
  }

  @Override
  public void removeTableListener(TableListener listnr) {
    listeners.remove(listnr);
  }

  @Override
  public IntIterator rowIterator() {
    return new ImproviseIntIterator();
  }

  @Override
  public void set(int rowId, String field, Object val) {
    set(rowId, getSchema().getColumnIndex(field), val);
  }

  @Override
  public void set(int rowId, int col, Object val) {
    Record record = oblivionTable.at(rowId);
    record.setValue(col, val);
  }

  @Override
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(oblivionTable.getClass())) {
      return oblivionTable;
    }
    return null;
  }

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param type the type of modification
   */
  protected void fireTableEvent(int start, int end, int col, int type) {
   if (this.getTableListeners().isEmpty()) {
     return;
   }
   for (TableListener listnr : this.getTableListeners()) {
     listnr.tableChanged(this, start, end, col, type);
   }
  }

  /**
   * IntIterator implementation for Improvise.
   * @author Hemery
   *
   */
  public class ImproviseIntIterator implements IntIterator {

    /**
     * Improvise table iterator wrapped.
     */
    private TableIterator it = oblivionTable.scan();

    /**
     * Constructor.
     */
    public ImproviseIntIterator() { }

    @Override
    public int nextInt() {
      return next();
    }

    @Override
    public boolean hasNext() {
      return it.hasMoreElements();
    }

    @Override
    public Integer next() {
      return ((Record) it.nextElement()).getID();
    }

    @Override
    public void remove() {
    }

  }

}
