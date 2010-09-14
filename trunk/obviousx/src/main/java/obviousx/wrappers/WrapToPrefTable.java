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

package obviousx.wrappers;

import java.beans.PropertyChangeListener;
import java.util.Iterator;

import obvious.data.Table;
import obvious.impl.TupleImpl;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.column.Column;
import prefuse.data.column.ColumnFactory;
import prefuse.data.column.ColumnMetadata;
import prefuse.data.event.TableListener;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.tuple.TupleManager;
import prefuse.data.util.Index;
import prefuse.data.util.TableIterator;
import prefuse.util.collections.IntIterator;

/**
 * Wrapper for obvious table to prefuse table.
 * @author Hemery
 *
 */
public class WrapToPrefTable extends prefuse.data.Table {

  /**
   * Obvious table to wrap.
   */
  private Table table;

  /**
   * Constructor.
   * @param inTable wrapped obvious table
   */
  public WrapToPrefTable(Table inTable) {
    this.table = inTable;
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      addColumn(table.getSchema().getColumnName(i),
          table.getSchema().getColumnType(i),
          table.getSchema().getColumnDefault(i));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addColumn(String arg0, Class arg1, Object arg2) {
    if (!m_names.contains(arg0)) {
      if (!table.getSchema().hasColumn(arg0)) {
        table.getSchema().addColumn(arg0, arg1, arg2);
      }
      Column col = ColumnFactory.getColumn(arg1, getRowCount(), arg2);
      int colIndex = table.getSchema().getColumnIndex(arg0);
      this.m_lastCol = colIndex;
      this.m_columns.add(col);
      this.m_names.add(arg0);
      ColumnEntry entry = new ColumnEntry(colIndex, col,
          new ColumnMetadata(this, arg0));
      ColumnEntry oldEntry = (ColumnEntry) this.m_entries.put(arg0, entry);
      if (oldEntry != null) {
        oldEntry.dispose();
      }

      invalidateSchema();
      // listen to what the column has to say
      col.addColumnListener(this);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addColumn(String arg0, Class arg1) {
    this.addColumn(arg0, arg1, null);
  }

  @Override
  protected void addColumn(String arg0, Column arg1) {
    this.addColumn(arg0, arg1.getColumnClass(),
        arg1.getDefaultValue());
  }

  @Override
  public void addColumn(String arg0, Expression arg1) {
    this.addColumn(arg0, ColumnFactory.getColumn(this, arg1));
  }

  @Override
  public void addColumn(String arg0, String arg1) {
    Expression ex = ExpressionParser.parse(arg1);
    Throwable t = ExpressionParser.getError();
    if (t != null) {
        throw new RuntimeException(t);
    } else {
        addColumn(arg1, ex);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addConstantColumn(String arg0, Class arg1, Object arg2) {
    addColumn(arg0, ColumnFactory.getConstantColumn(arg1, arg2));
  }

  @Override
  public int addRow() {
    return table.addRow();
  }

  @Override
  public void addRows(int arg0) {
    for (int i = 0; i < arg0; i++) {
      table.addRow();
    }
  }

  @Override
  public void addTableListener(TableListener arg0) {
    super.addTableListener(arg0);
  }

  @Override
  public Tuple addTuple(Tuple arg0) {
    Object[] values = new Object[table.getSchema().getColumnCount()];
    obvious.data.Tuple obviousTuple = new TupleImpl(table.getSchema(), values);
    int r = table.addRow(obviousTuple);
    return this.getTuple(r);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean canGet(String arg0, Class arg1) {
    return table.getSchema().canGet(arg0, arg1);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean canSet(String arg0, Class arg1) {
    return table.getSchema().canSet(arg0, arg1);
  }

  @Override
  public void clear() {
    table.removeAllRows();
  }

  @Override
  public void columnChanged(Column arg0, int arg1, boolean arg2) {
    handleColumnChanged(arg0, arg1, arg1);
  }

  @Override
  public void columnChanged(Column arg0, int arg1, double arg2) {
    handleColumnChanged(arg0, arg1, arg1);
  }

  @Override
  public void columnChanged(Column arg0, int arg1, float arg2) {
    handleColumnChanged(arg0, arg1, arg1);
  }

  @Override
  public void columnChanged(Column arg0, int arg1, int arg2, int arg3) {
    handleColumnChanged(arg0, arg2, arg3);
  }

  @Override
  public void columnChanged(Column arg0, int arg1, int arg2) {
    handleColumnChanged(arg0, arg1, arg1);
  }

  @Override
  public void columnChanged(Column arg0, int arg1, long arg2) {
    handleColumnChanged(arg0, arg1, arg1);
  }

  @Override
  public void columnChanged(Column arg0, int arg1, Object arg2) {
    handleColumnChanged(arg0, arg1, arg1);
  }

  @Override
  public boolean containsTuple(Tuple arg0) {
    obvious.data.util.IntIterator it = table.rowIterator();
    while (it.hasNext()) {
      if (this.getTuple(it.nextInt()).equals(arg0)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected void fireTableEvent(int arg0, int arg1, int arg2, int arg3) {
    super.fireTableEvent(arg0, arg1, arg2, arg3);
  }

  @Override
  public Object get(int arg0, String arg1) {
    return table.getValue(arg0, arg1);
  }

  @Override
  public Column getColumn(int arg0) {
    Column column = ColumnFactory.getColumn(getColumnType(arg0), getRowCount(),
        this.getSchema().getDefault(arg0));
    IntIterator it = this.rows();
    while (it.hasNext()) {
      int currentRow = it.nextInt();
      column.set(table.getValue(currentRow, arg0), currentRow);
    }
    return column;
  }

  @Override
  public Column getColumn(String arg0) {
    return this.getColumn(this.getColumnNumber(arg0));
  }

  @Override
  public int getColumnCount() {
    return table.getSchema().getColumnCount();
  }

  @Override
  public String getColumnName(int arg0) {
    return table.getSchema().getColumnName(arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Iterator getColumnNames() {
    return super.getColumnNames();
  }

  @Override
  public int getColumnNumber(Column arg0) {
    int colCount = this.getColumnCount();
    int colNumber = -1;
    for (int i = 0; i < colCount; i++) {
      if (arg0.equals(this.getColumn(i))) {
        colNumber = i;
        break;
      }
    }
    return colNumber;
  }

  @Override
  public int getColumnNumber(String arg0) {
    return table.getSchema().getColumnIndex(arg0);
  }

  @Override
  public int getColumnRow(int arg0, int arg1) {
    if (this.isValidRow(arg0)) {
      return arg0;
    } else {
      return -1;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Iterator getColumns() {
    return super.getColumns();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getColumnType(int arg0) {
    return table.getSchema().getColumnType(arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getColumnType(String arg0) {
    return table.getSchema().getColumnType(arg0);
  }

  @Override
  public Object getDefault(String arg0) {
    return getColumn(getColumnNumber(arg0)).getDefaultValue();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Index getIndex(String arg0, Class arg1, boolean arg2) {
    return super.getIndex(arg0, arg1, arg2);
  }

  @Override
  public Index getIndex(String arg0) {
    return super.getIndex(arg0);
  }

  @Override
  public ColumnMetadata getMetadata(String arg0) {
    return super.getMetadata(arg0);
  }

  @Override
  public int getModificationCount() {
    return super.getModificationCount();
  }

  @Override
  public int getRowCount() {
    if (table == null) {
      return 0;
    } else {
      return table.getRowCount();
    }
  }

  @Override
  public Schema getSchema() {
    int colCount = table.getSchema().getColumnCount();
    String[] names = new String[colCount];
    Class<?>[] classes = new Class[colCount];
    Object[] values = new Object[colCount];
    for (int i = 0; i < colCount; i++) {
      names[i] = table.getSchema().getColumnName(i);
      classes[i] = table.getSchema().getColumnType(i);
      values[i] = table.getSchema().getColumnDefault(i);
    }
    return new Schema(names, classes, values);
  }

  @Override
  public int getTableRow(int arg0, int arg1) {
    if (this.isValidRow(arg0)) {
      return arg0;
    } else {
      return -1;
    }
  }

  @Override
  public Tuple getTuple(int arg0) {
    if (this.isValidRow(arg0)) {
      Object[] values = new Object[table.getSchema().getColumnCount()];
      for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
        values[i] = table.getValue(arg0, i);
      }
      return new WrapToPrefTuple(new TupleImpl(table.getSchema(), values));
    } else {
      return null;
    }
  }

  @Override
  public int getTupleCount() {
    return this.getRowCount();
  }

  @Override
  public Object getValueAt(int arg0, int arg1) {
    return table.getValue(arg0, arg1);
  }

  @Override
  protected void handleColumnChanged(Column c, int start, int end) {
    super.handleColumnChanged(c, start, end);
  }

  @Override
  protected boolean hasColumn(String arg0) {
    return table.getSchema().hasColumn(arg0);
  }

  @Override
  public Index index(String arg0) {
    return super.index(arg0);
  }

  @Override
  protected void invalidateSchema() {
    super.invalidateSchema();
  }

  @Override
  public boolean isAddColumnSupported() {
    return super.isAddColumnSupported();
  }

  @Override
  public boolean isCellEditable(int arg0, int arg1) {
    return table.isValueValid(arg0, arg1);
  }

  @Override
  public boolean isValidRow(int arg0) {
    return table.isValidRow(arg0);
  }

  @Override
  public TableIterator iterator() {
    return super.iterator();
  }

  @Override
  public TableIterator iterator(IntIterator arg0) {
    return super.iterator(arg0);
  }

  @Override
  public IntIterator rangeSortedBy(String arg0, double arg1, double arg2,
      int arg3) {
    return super.rangeSortedBy(arg0, arg1, arg2, arg3);
  }

  @Override
  public IntIterator rangeSortedBy(String arg0, float arg1, float arg2,
      int arg3) {
    return super.rangeSortedBy(arg0, arg1, arg2, arg3);
  }

  @Override
  public IntIterator rangeSortedBy(String arg0, int arg1, int arg2, int arg3) {
    return super.rangeSortedBy(arg0, arg1, arg2, arg3);
  }

  @Override
  public IntIterator rangeSortedBy(String arg0, long arg1, long arg2,
      int arg3) {
    return super.rangeSortedBy(arg0, arg1, arg2, arg3);
  }

  @Override
  public IntIterator rangeSortedBy(String arg0, Object arg1, Object arg2,
      int arg3) {
    return super.rangeSortedBy(arg0, arg1, arg2, arg3);
  }

  @Override
  public void removeColumn(Column arg0) {
    if (!table.getSchema().hasColumn(getColumnName(getColumnNumber(arg0)))) {
      return;
    }
    removeColumn(getColumnNumber(arg0));
  }

  @Override
  protected Column removeColumn(int arg0) {
    table.getSchema().removeColumn(arg0);
    return super.removeColumn(arg0);
  }

  @Override
  public Column removeColumn(String arg0) {
    if (!table.getSchema().hasColumn(arg0)) {
      return null;
    } else {
      return removeColumn(this.getColumnNumber(arg0));
    }
  }

  @Override
  public boolean removeIndex(String arg0) {
    return super.removeIndex(arg0);
  }

  @Override
  public boolean removeRow(int arg0) {
    return table.removeRow(arg0);
  }

  @Override
  public void removeTableListener(TableListener arg0) {
    super.removeTableListener(arg0);
  }

  @Override
  public boolean removeTuple(Tuple arg0) {
    return table.removeRow(arg0.getRow());
  }

  @Override
  protected void renumberColumns() {
    super.renumberColumns();
  }

  @Override
  public void revertToDefault(int arg0, String arg1) {
    super.revertToDefault(arg0, arg1);
  }

  @Override
  public IntIterator rows() {
    return new PrefRowIterator(this, false);
  }

  @Override
  public IntIterator rows(boolean arg0) {
    return new PrefRowIterator(this, arg0);
  }

  @Override
  public IntIterator rows(Predicate arg0) {
    return super.rows(arg0);
  }

  @Override
  public IntIterator rowsSortedBy(String arg0, boolean arg1) {
    return super.rowsSortedBy(arg0, arg1);
  }

  @Override
  public void set(int arg0, String arg1, Object arg2) {
    table.set(arg0, arg1, arg2);
  }

  @Override
  public Tuple setTuple(Tuple arg0) {
    return super.setTuple(arg0);
  }

  @Override
  public void setTupleManager(TupleManager arg0) {
    super.setTupleManager(arg0);
  }

  @Override
  public void setValueAt(int arg0, int arg1, Object arg2) {
    table.set(arg0, arg1, arg2);
  }

  @Override
  public String toString() {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("Table[");
    sbuf.append("rows=").append(getRowCount());
    sbuf.append(", cols=").append(getColumnCount());
    sbuf.append("]");
    return sbuf.toString();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator tuples() {
    return new PrefTupleIterator(this, rows());
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator tuples(IntIterator arg0) {
    return new PrefTupleIterator(this, arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator tuplesReversed() {
    return new PrefTupleIterator(this, rows(true));
  }

  @Override
  protected void updateRowCount() {
    super.updateRowCount();
  }

  @Override
  public void addColumns(Schema arg0) {
    for (int i = 0; i < arg0.getColumnCount(); i++) {
      this.addColumn(arg0.getColumnName(i), arg0.getColumnType(i),
          arg0.getDefault(i));
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener arg0) {
    super.addPropertyChangeListener(arg0);
  }

  @Override
  public void addPropertyChangeListener(
      String arg0, PropertyChangeListener arg1) {
    super.addPropertyChangeListener(arg0, arg1);
  }

  @Override
  public void addTupleSetListener(TupleSetListener arg0) {
    super.addTupleSetListener(arg0);
  }

  @Override
  protected void fireTupleEvent(prefuse.data.Table arg0,
      int arg1, int arg2, int arg3) {
    super.fireTupleEvent(arg0, arg1, arg2, arg3);
  }

  @Override
  protected void fireTupleEvent(Tuple arg0, int arg1) {
    super.fireTupleEvent(arg0, arg1);
  }

  @Override
  protected void fireTupleEvent(Tuple[] arg0, Tuple[] arg1) {
    super.fireTupleEvent(arg0, arg1);
  }

  @Override
  public Object getClientProperty(String arg0) {
    return super.getClientProperty(arg0);
  }

  @Override
  public void putClientProperty(String arg0, Object arg1) {
    super.putClientProperty(arg0, arg1);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener arg0) {
    super.removePropertyChangeListener(arg0);
  }

  @Override
  public void removePropertyChangeListener(String arg0,
      PropertyChangeListener arg1) {
    super.removePropertyChangeListener(arg0, arg1);
  }

  @Override
  public void removeTupleSetListener(TupleSetListener arg0) {
    super.removeTupleSetListener(arg0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator tuples(Predicate arg0) {
    return new PrefTupleIterator(this, rows(arg0));
  }

}
