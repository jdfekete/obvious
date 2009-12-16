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

package obviousx.io;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import obvious.data.Table;

/**
 * An implementation for Obvious Table that supports TableModel.
 * @author Pierre-Luc Hemery
 *
 */
public class ObviousTableModel implements TableModel {

  /**
   * Obvious Table.
   */
  private Table table;

  /**
   * Constructor.
   * @param obviousTable obviousTable to wrap
   */
  public ObviousTableModel(Table obviousTable) {
    this.table = obviousTable;
  }

  /**
   * Adds a listener to the list that is notified
   * each time a change to the data model occurs.
   * @param l  the TableModelListener
   */
  public void addTableModelListener(TableModelListener l) {
    // TODO Auto-generated method stub
  }

  /**
   * Returns the most specific superclass for all the cell values in the column
   * This is used by the JTable to set up a default renderer
   * and editor for the column.
   * @param columnIndex  the index of the column
   * @return the common ancestor class of the object values in the model.
   */
  public Class<?> getColumnClass(int columnIndex) {
    return this.table.getSchema().getColumnType(columnIndex);
  }

  /**
   * Returns the number of columns in the model.
   * A JTable uses this method to determine how many columns it should create
   * and display by default.
   * @return the number of columns in the model
   */
  public int getColumnCount() {
    return this.table.getSchema().getColumnCount();
  }

  /**
   * Returns the name of the column at columnIndex.
   * This is used to initialize the table's column header name.
   * Note: this name does not need to be unique; two columns
   * in a table can have the same name.
   * @param columnIndex  the index of the column
   * @return the name of the column
   */
  public String getColumnName(int columnIndex) {
    return this.table.getSchema().getColumnName(columnIndex);
  }

  /**
   * Returns the number of rows in the model.
   * A JTable uses this method to determine how many rows it should display.
   * This method should be quick, as it is called frequently during rendering.
   * @return the number of rows in the model
   */
  public int getRowCount() {
    return this.table.getRowCount();
  }

  /**
   * Returns the value for the cell at columnIndex and rowIndex.
   * @param rowIndex - the row whose value is to be queried
   * @param columnIndex - the column whose value is to be queried
   * @return the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    return this.table.getValue(rowIndex, columnIndex);
  }

  /**
   * Returns true if the cell at rowIndex and columnIndex  is editable.
   * Otherwise, setValueAt on the cell will not change the value of that cell.
   * @param rowIndex - the row whose value is to be queried
   * @param columnIndex - the column whose value is to be queried
   * @return true if the cell is editable
   */
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return (this.table.canAddRow() && this.table.canRemoveRow());
  }

  /**
   * Removes a listener from the list that is notified each time a change
   * to the data model occurs.
   * @param l  the TableModelListener
   */
  public void removeTableModelListener(TableModelListener l) {
  }

  /**
   * Sets the value in the cell at columnIndex and rowIndex to aValue.
   * @param aValue the new value
   * @param rowIndex - the row whose value is to be queried
   * @param columnIndex - the column whose value is to be queried
   */
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    this.table.set(rowIndex, columnIndex, aValue);
  }

}
