package obviousx.wrappers;

import java.text.Format;
import java.text.ParseException;

import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelListener;
import javax.swing.text.MutableAttributeSet;

import obvious.data.Table;
import obvious.data.Tuple;

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

  public void addColumn(Column c) {
    // TODO Auto-generated method stub
    
  }

  /**
   * Removes all rows.
   */
  public void clear() {
    table.removeAllRows();
  }

  public Column getColumn(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  public Column getColumnAt(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getColumnCount() {
    return table.getSchema().getColumnCount();
  }

  public Item getItem(int row) {
    // TODO Auto-generated method stub
    return null;
  }

  public int getLastRow() {
    return 0;
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
    // TODO Auto-generated method stub
    return 0;
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
    // TODO Auto-generated method stub
    return false;
  }

  public RowIterator reverseIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setColumnAt(int i, Column c) {
    // TODO Auto-generated method stub
    
  }

  public void addChangeListener(ChangeListener listener) {
    // TODO Auto-generated method stub
    
  }

  public int capacity() {
    // TODO Auto-generated method stub
    return 0;
  }

  public void copyValueFrom(int toIndex, Column c, int fromIndex)
      throws ParseException {
    // TODO Auto-generated method stub
    
  }

  public void disableNotify() {
    // TODO Auto-generated method stub
    
  }

  public void enableNotify() {
    // TODO Auto-generated method stub
    
  }

  public void ensureCapacity(int minCapacity) {
    // TODO Auto-generated method stub
    
  }

  public String format(Object o) {
    // TODO Auto-generated method stub
    return null;
  }

  public Format getFormat() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getMaxIndex() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int getMinIndex() {
    // TODO Auto-generated method stub
    return 0;
  }

  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getObjectAt(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  public String getValueAt(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  public Class getValueClass() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean hasUndefinedValue() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isInternal() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isValueUndefined(int i) {
    // TODO Auto-generated method stub
    return false;
  }

  public RowIterator iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object parse(String s) throws ParseException {
    // TODO Auto-generated method stub
    return null;
  }

  public void removeChangeListener(ChangeListener listener) {
    // TODO Auto-generated method stub
    
  }

  public void setFormat(Format format) {
    // TODO Auto-generated method stub
    
  }

  public void setName(String name) {
    // TODO Auto-generated method stub
    
  }

  public void setObjectAt(int index, Object o) {
    // TODO Auto-generated method stub
    
  }

  public void setSize(int newSize) {
    // TODO Auto-generated method stub
    
  }

  public void setValueAt(int index, String element) throws ParseException {
    // TODO Auto-generated method stub
    
  }

  public boolean setValueOrNullAt(int index, String v) {
    // TODO Auto-generated method stub
    return false;
  }

  public void setValueUndefined(int i, boolean undef) {
    // TODO Auto-generated method stub
    
  }

  public int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  public MutableAttributeSet getClientProperty() {
    // TODO Auto-generated method stub
    return null;
  }

  public MutableAttributeSet getMetadata() {
    // TODO Auto-generated method stub
    return null;
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

  public int compare(Object o1, Object o2) {
    // TODO Auto-generated method stub
    return 0;
  }

  public void addTableModelListener(TableModelListener l) {
    // TODO Auto-generated method stub
    
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

  public void removeTableModelListener(TableModelListener l) {
    // TODO Auto-generated method stub
    
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
