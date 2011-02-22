package obviousx.wrappers.prefuse;

import java.util.Iterator;

import prefuse.data.Table;
import prefuse.data.util.RowManager;
import prefuse.util.collections.IntIterator;
import prefuse.visual.VisualTable;

public class WrapToPrefRowManager extends RowManager {

  public WrapToPrefRowManager(WrapToPrefTable table) {
    super(table);
    // TODO Auto-generated constructor stub
  }

  @Override
  public int addRow() {
    return super.addRow();
  }

  @Override
  public void clear() {
    super.clear();
  }

  @Override
  public IntIterator columnRows(int col, boolean reverse) {
    return super.columnRows(col, reverse);
  }

  @Override
  public IntIterator columnRows(int col) {
    return super.columnRows(col);
  }

  @Override
  public IntIterator columnRows(IntIterator rows, int col) {
    return super.columnRows(rows, col);
  }

  @Override
  public int getColumnRow(int row, int col) {
    return m_table.getColumnRow(row, col);
  }

  @Override
  public int getMaximumRow() {
    return m_table.getRowCount() - 1;
  }

  @Override
  public int getMinimumRow() {
    return 0;
  }

  @Override
  public int getRowCount() {
    return m_table.getRowCount();
  }

  @Override
  public Table getTable() {
    return m_table;
  }

  @Override
  public int getTableRow(int columnRow, int col) {
    return super.getTableRow(columnRow, col);
  }

  @Override
  public boolean isValidRow(int row) {
    return m_table.isValidRow(row);
  }

  @Override
  public boolean releaseRow(int row) {
    return super.releaseRow(row);
  }

  @Override
  public IntIterator rows() {
    return m_table.rows();
  }

  @Override
  public IntIterator rows(boolean reverse) {
    return m_table.rows(reverse);
  }

}
