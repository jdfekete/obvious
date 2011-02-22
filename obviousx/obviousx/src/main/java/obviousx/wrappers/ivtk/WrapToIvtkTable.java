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

package obviousx.wrappers.ivtk;

import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;

import obvious.ObviousException;
import obvious.data.Table;

import infovis.Column;
import infovis.DynamicTable;
import infovis.table.Item;
import infovis.utils.RowIterator;

/**
 * Wrapper for obvious table to ivtk table.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class WrapToIvtkTable implements DynamicTable {

  /**
   * Table to wrap.
   */
  private Table table;

  /**
   * Name.
   */
  private String tableName = null;

  /**
   * Collection of TableModelListener of this table.
   */
  private Collection<TableModelListener> tableListener =
    new ArrayList<TableModelListener>();

  /**
   * Collection of ChangeListener of this table.
   */
  private Collection<ChangeListener> changeListener =
    new ArrayList<ChangeListener>();

  /**
   * Constructor.
   * @param inTable table to wrap.
   */
  public WrapToIvtkTable(Table inTable) {
    this.table = inTable;
  }

  /**
   * Adds an item.
   * @return item added
   */
  public Item addItem() {
    return getItem(addRow());
  }

  /**
   * Adds a row to the table.
   * @return index of the added column.
   */
  public int addRow() {
    return table.addRow();
  }

  /**
   * Removes the item from the table.
   * @param item item to remove
   */
  public void removeItem(Item item) {
    removeRow(item.getId());
  }

  /**
   * Removes a row.
   * @param row index of the row to remove
   */
  public void removeRow(int row) {
    table.removeRow(row);
  }

  @Override
  public void addColumn(Column c) {
    table.getSchema().addColumn(c.getName(), c.getValueClass(), null);
  }

  /**
   * Removes all rows.
   */
  public void clear() {
    table.removeAllRows();
  }

  @Override
  public Column getColumn(String name) {
    return getColumnAt(table.getSchema().getColumnIndex(name));
  }

  @Override
  public Column getColumnAt(int index) {
    return new WrapToIvtkCol(table, index);
  }

  @Override
  public int getColumnCount() {
    return table.getSchema().getColumnCount();
  }

  @Override
  public Item getItem(int row) {
    return new WrapToIvtkItem(this, row);
  }

  @Override
  public int getLastRow() {
    int validEncounteredRows = 0;
    int currentRow = 0;
    while (validEncounteredRows < table.getRowCount()) {
      if (table.isValidRow(currentRow)) {
        validEncounteredRows++;
      }
      currentRow++;
    }
    return currentRow - 1;
  }

  /**
   * Returns the number of rows.
   * @return number of rows
   */
  public int getRowCount() {
    return table.getRowCount();
  }

  /**
   * Returns the underlying ivtk table.
   * @return underlying ivtk table
   */
  public infovis.Table getTable() {
    return this;
  }

  /**
   * Returns the index of a column of the table.
   * @param name name of the index to find
   * @return column index
   */
  public int indexOf(String name) {
    return table.getSchema().getColumnIndex(name);
  }

  @Override
  public int indexOf(Column column) {
    return table.getSchema().getColumnIndex(column.getName());
  }

  /**
   * Indicates if a row is valid.
   * @param row index to check
   * @return true if the row is valid (i.e it exists in the table).
   */
  public boolean isRowValid(int row) {
    return table.isValidRow(row);
  }

  @Override
  public boolean removeColumn(Column c) {
    return table.getSchema().removeColumn(c.getName());
  }

  @Override
  public RowIterator reverseIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setColumnAt(int i, Column c) {
    return;
  }

  @Override
  public void addChangeListener(ChangeListener listener) {
    changeListener.add(listener);
  }

  @Override
  public int capacity() {
    return table.getRowCount();
  }

  @Override
  public void copyValueFrom(int toIndex, Column c, int fromIndex)
      throws ParseException {
    // TODO Auto-generated method stub
  }

  @Override
  public void disableNotify() {
    try {
      table.beginEdit(0);
    } catch (ObviousException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotify() {
    try {
      table.endEdit(0);
    } catch (ObviousException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void ensureCapacity(int minCapacity) {
    if (minCapacity <= table.getRowCount()) {
      return;
    }
    int newRows = minCapacity - table.getRowCount();
    for (int i = 0; i < newRows; i++) {
      table.addRow();
    }
  }

  @Override
  public String format(Object o) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Format getFormat() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getMaxIndex() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMinIndex() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getName() {
    return tableName;
  }

  @Override
  public Object getObjectAt(int index) {
    return getColumnAt(index);
  }

  @Override
  public String getValueAt(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getValueClass() {
    return Column.class;
  }

  @Override
  public boolean hasUndefinedValue() {
    RowIterator it = iterator();
    while (it.hasNext()) {
      if (isValueUndefined(it.nextRow())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean isInternal() {
    return false;
  }

  @Override
  public boolean isValueUndefined(int i) {
    if (i < 0 || i >= size()) {
      return true;
    }
    return getValueAt(i) == null;
  }

  @Override
  public RowIterator iterator() {
    return new WrapToIvtkIterator(table.rowIterator());
  }

  @Override
  public Object parse(String s) throws ParseException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeChangeListener(ChangeListener listener) {
    changeListener.remove(listener);
  }

  @Override
  public void setFormat(Format format) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setName(String name) {
    tableName = name;
  }

  @Override
  public void setObjectAt(int index, Object o) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setSize(int newSize) {
    ensureCapacity(newSize);
  }

  @Override
  public void setValueAt(int index, String element) throws ParseException {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean setValueOrNullAt(int index, String v) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setValueUndefined(int i, boolean undef) {
    // TODO Auto-generated method stub
  }

  @Override
  public int size() {
    return table.getRowCount();
  }

  @Override
  public MutableAttributeSet getClientProperty() {
    return new SimpleAttributeSet();
  }

  @Override
  public MutableAttributeSet getMetadata() {
    return new SimpleAttributeSet();
  }

  /**
   * Compares two objects described by their row index.
   * @param arg0 first row index
   * @param arg1 second row index
   * @return 0 if equals
   */
  public int compare(int arg0, int arg1) {
    Object o1 = getObjectAt(arg0);
    Object o2 = getObjectAt(arg1);
    return compare(o1, o2);
  }

  @Override
  public int compare(Object o1, Object o2) {
    return 0;
  }

  @Override
  public void addTableModelListener(TableModelListener l) {
    tableListener.add(l);
  }

  /**
   * Gets the class of a column.
   * @param columnIndex index of the column
   * @return class of the column
   */
  public Class<?> getColumnClass(int columnIndex) {
    return table.getSchema().getColumnType(columnIndex);
  }

  /**
   * Gets the name of a column.
   * @param columnIndex index of the column
   * @return name of the column
   */
  public String getColumnName(int columnIndex) {
    return table.getSchema().getColumnName(columnIndex);
  }

  /**
   * Gets the value at a specified row and column indexes.
   * @param rowIndex index of the row where to get value
   * @param columnIndex index of the column where to get value
   * @return a value
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    return table.getValue(rowIndex, columnIndex);
  }

  /**
   * Indicates if the value of a cell is editable.
   * @param rowIndex row index of the cell
   * @param columnIndex column index of the cell
   * @return true if the cell is valid
   */
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return table.isValueValid(rowIndex, columnIndex);
  }

  @Override
  public void removeTableModelListener(TableModelListener l) {
    tableListener.remove(l);
  }

  /**
   * Sets the value in the table.
   * @param aValue value to set
   * @param rowIndex index of the row where to set the value
   * @param columnIndex index of the column where to set the value
   */
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    table.set(rowIndex, columnIndex, aValue);
  }

}
