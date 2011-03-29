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

import prefuse.data.Table;
import prefuse.data.util.RowManager;
import prefuse.util.collections.IntIterator;

/**
 * Class creating a prefuse RowManager from a WrapToPrefTable a custom
 * prefuse Table based on Obvious Table.
 * @author Hemery
 *
 */
public class WrapToPrefRowManager extends RowManager {

  /**
   * Constructor.
   * @param table a WrapToPrefTable instance
   */
  public WrapToPrefRowManager(WrapToPrefTable table) {
    super(table);
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
