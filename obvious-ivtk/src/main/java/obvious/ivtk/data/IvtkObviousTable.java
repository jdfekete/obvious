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

package obvious.ivtk.data;

import infovis.Column;
import infovis.DynamicTable;
import infovis.column.ColumnFactory;
import infovis.table.DefaultDynamicTable;
import infovis.utils.RowIterator;
import infovis.utils.TableIterator;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;

import obvious.ObviousException;
import obvious.ObviousRuntimeException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.FilterIntIterator;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

/**
 * Implementation of an Obvious Table based on infovis toolkit.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkObviousTable implements Table {

  /**
   * Schema for the table.
   */
  private Schema schema;

  /**
   * Wrapped ivtk table.
   */
  private infovis.DynamicTable table;

  /**
   * Is the schema being edited.
   */
  private boolean editing = false;

  /**
   * Are rows removable.
   */
  private boolean canRemoveRow;

  /**
   * Are rows addable.
   */
  private boolean canAddRow;

  /**
   * ArrayList of listeners.
   */
  private ArrayList<TableListener> listener = new ArrayList<TableListener>();

  /**
   * Format factory.
   */
  private FormatFactory formatFactory;

  /**
   * Constructor for IvtkObvious table.
   * @param inSchema schema for the table
   */
  public IvtkObviousTable(Schema inSchema) {
    this(inSchema, true, true);
  }

  /**
   * Constructor for IvtkObvious table.
   * @param inSchema schema for the table
   * @param add boolean indicating if row addition is allowed
   * @param rem boolean indicating if row addition is allowed
   */
  public IvtkObviousTable(Schema inSchema, Boolean add, Boolean rem) {
    this.schema = inSchema;
    this.table = new DefaultDynamicTable();
    this.canAddRow = add;
    this.canRemoveRow = rem;
    BasicConfigurator.configure();
    ColumnFactory factory = ColumnFactory.getInstance();
    formatFactory = new FormatFactoryImpl();
    for (int i = 0; i < schema.getColumnCount(); i++) {
      Column col = factory.create(
          schema.getColumnType(i).getSimpleName(), schema.getColumnName(i));
      table.addColumn(col);
    }
  }

  /**
   * Constructor for IvtkObviousTable from an ivtk dynamic table and params.
   * @param inTable an ivtk DefaultDynamicTable instance to wrap
   * @param add boolean indicating if row addition is allowed
   * @param rem boolean indicating if row addition is allowed
   */
  public IvtkObviousTable(infovis.DynamicTable inTable, Boolean add,
      Boolean rem) {
    this.table = inTable;
    this.canAddRow = add;
    this.canRemoveRow = rem;
    this.formatFactory = new FormatFactoryImpl();
    ArrayList<Column> cols = new ArrayList<Column>();
    for (int i = 0; i < table.getColumnCount(); i++) {
      cols.add(table.getColumnAt(i));
    }
    this.schema = new IvtkObviousSchema(cols);
  }

  /**
   * Constructor for IvtkObviousTable from an ivtk dynamic table.
   * @param inTable an ivtk DefaultDynamicTable instance to wrap
   */
  public IvtkObviousTable(DynamicTable inTable) {
    this(inTable, true, true);
  }

  /**
   * Constructor from an infovis Table instance.
   * @param inTable an ivtk Table
   */
  public IvtkObviousTable(infovis.Table inTable) {
      table = new DefaultDynamicTable();
      this.canAddRow = true;
      this.canRemoveRow = true;
      this.formatFactory = new FormatFactoryImpl();
      ArrayList<Column> cols = new ArrayList<Column>();
      for (int i = 0; i < inTable.getColumnCount(); i++) {
          Column col = inTable.getColumnAt(i);
          table.addColumn(col);
          cols.add(col);
      }
      this.schema = new IvtkObviousSchema(cols);
      RowIterator it = inTable.iterator();
      while (it.hasNext()) {
          int rowId = it.nextRow();
          int currentRow = table.addRow();
          for (int i = 0; i < inTable.getColumnCount(); i++) {
              table.setValueAt(inTable.getValueAt(rowId, i),
                      currentRow, i);
          }
      }
  }

  /**
   * Adds a row with default value.
   * @return number of rows
   */
  public int addRow() {
    Object defaultValue = "n/a";
    try {
      if (canAddRow()) {
        int rowId = table.addRow();
        for (int i = 0; i < schema.getColumnCount(); i++) {
          TypedFormat format = formatFactory.getFormat(
             schema.getColumnType(i).getSimpleName());
          defaultValue = schema.getColumnDefault(i);
          if (schema.getColumnDefault(i) == null) {
            defaultValue = setDefaultValue(format);
          }
          StringBuffer val = format.format(defaultValue,
              new StringBuffer(), new FieldPosition(0));
          table.setValueAt(val.toString(), rowId, i);
          //table.getColumnAt(i).setValueAt(table.getColumnAt(i).size(),
          //    val.toString());
        }
        this.fireTableEvent(table.getLastRow(), table.getLastRow(),
            TableListener.ALL_COLUMN, TableListener.INSERT);
      }
      return this.getRowCount();
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Sets the default value (internal method to avoid null pointer exception).
   * @param format format used
   * @return a convenient default value when the null value is specified as
   * default value
   */
  private Object setDefaultValue(TypedFormat format) {
    if (format instanceof FormatFactoryImpl.TypedDecimalFormat) {
      return 0;
    }
    return "null";
  }

  /**
   * Adds a row corresponding to the input tuple.
   * @param tuple tuple to insert in the table
   * @return number of rows
   */
  public int addRow(Tuple tuple) {
    try {
      if (canAddRow()) {
        int rowId = table.addRow();
        for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
          String field = tuple.getSchema().getColumnName(i);
          if (schema.hasColumn(field)) {
            TypedFormat format = formatFactory.getFormat(
               tuple.getSchema().getColumnType(i).getSimpleName());
            if (format instanceof FormatFactoryImpl.TypedDecimalFormat) {
              table.setValueAt(tuple.get(i).toString(), rowId,
                  schema.getColumnIndex(field));
            } else {
            StringBuffer v = format.format(tuple.get(i),
                new StringBuffer(), new FieldPosition(0));
            table.setValueAt(v.toString(), rowId, schema.getColumnIndex(field));
            }
          }
        }
        this.fireTableEvent(table.getLastRow(), table.getLastRow(),
            TableListener.ALL_COLUMN, TableListener.INSERT);
      }
      return this.getRowCount();
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Adds a table listener.
   * @param listnr an Obvious TableListener
   */
  public void addTableListener(TableListener listnr) {
    listener.add(listnr);
  }

  /**
   * Indicates the beginning of a column edit.
   * @param col column index
   * @throws ObviousException if edition is not supported.
   */
  public void beginEdit(int col) throws ObviousException {
    this.editing = true;
    for (TableListener listnr : this.getTableListeners()) {
      listnr.beginEdit(col);
    }
  }

  /**
   * Indicates if possible to add rows.
   * @return true if possible
   */
  public boolean canAddRow() {
    return this.canAddRow;
  }

  /**
   * Indicates if possible to remove rows.
   * @return true if possible
   */
  public boolean canRemoveRow() {
    return this.canRemoveRow;
  }

  /**
   * Indicates the end of a column edit.
   * @param col column index
   * @return true if transaction succeed
   * @throws ObviousException if edition is not supported.
   */
  public boolean endEdit(int col) throws ObviousException {
    this.editing = false;
    boolean success = true;
    TableListener failedListener = null;
    for (TableListener listnr : this.getTableListeners()) {
      if (!listnr.checkInvariants()) {
        listnr.endEdit(col);
        failedListener = listnr;
        success = false;
        break;
      }
    }
    for (TableListener listnr : this.getTableListeners()) {
      if (success && !listnr.equals(failedListener)) {
        listnr.endEdit(col);
      }
    }
    return success;
  }

  /**
   * Gets the number of rows in the table.
   * @return the number of rows
   */
  public int getRowCount() {
    return table.getRowCount();
  }

  /**
   * Returns this table's schema.
   * @return the schema of the table.
   */
  public Schema getSchema() {
    return this.schema;
  }

  /**
   * Gets all table listener.
   * @return a collection of table listeners.
   */
  public Collection<TableListener> getTableListeners() {
    return listener;
  }

  /**
   * Gets a specific value.
   * @param rowId row index
   * @param field column name
   * @return value for this couple
   */
  public Object getValue(int rowId, String field) {
    return getValue(rowId, schema.getColumnIndex(field));
  }

  /**
   * Gets a specific value.
   * @param rowId row index
   * @param col column index
   * @return value for this couple
   */
  public Object getValue(int rowId, int col) {
    if (isValidRow(rowId)) {
    TypedFormat format = formatFactory.getFormat(
        schema.getColumnType(col).getSimpleName());
    Object value = format.parseObject((String) table.getValueAt(rowId, col),
        new ParsePosition(0));
    return value;
    } else {
      return null;
    }
  }

  /**
   * Indicates if a column is being edited.
   * @param col column index
   * @return true if edited
   */
  public boolean isEditing(int col) {
    return this.editing;
  }

  /**
   * Indicates if the given row number corresponds to a valid table row.
   * @param rowId row index
   * @return true if the row is valid, false if it is not
   */
  public boolean isValidRow(int rowId) {
    return table.isRowValid(rowId);
  }

  /**
   * Indicates if a given value is correct.
   * @param rowId row index
   * @param col column index
   * @return true if the coordinates are valid
   */
  public boolean isValueValid(int rowId, int col) {
    return isValidRow(rowId) && (col < schema.getColumnCount());
  }

  /**
   * Removes all the rows.
   *
   * <p>After this method, the table is almost in the same state as if
   * it had been created afresh except it contains the same columns as before
   * but they are all cleared.
   *
   */
  public void removeAllRows() {
    try {
      int lastRow = table.getLastRow();
      TableIterator it = new TableIterator(0,
          table.getLastRow() + 1);
      while (it.hasNext()) {
        int rowId = it.nextRow();
        table.removeRow(rowId);
      }
      this.fireTableEvent(0, lastRow,
          TableListener.ALL_COLUMN, TableListener.DELETE);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
  /* Removes a row in the schema's table.
   * @param row row index
   * @return true if removes, else false.
   */
  public boolean removeRow(int row) {
    try {
      if (!canRemoveRow()) {
        return false;
      }
      if (!isValidRow(row)) {
        return false;
      }
      table.removeRow(row);
      this.fireTableEvent(row, row,
          TableListener.ALL_COLUMN, TableListener.DELETE);
      return true;
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Removes a table listener.
   * @param listnr an Obvious TableListener
   */
  public void removeTableListener(TableListener listnr) {
    listener.remove(listnr);
  }

  /**
   * Gets an iterator over the row numbers of this table.
   * @return an iterator over the rows of this table
   */
  public IntIterator rowIterator() {
    TableIterator it = new TableIterator(0,
        table.getLastRow() + 1);
    return new IvtkIntIterator(it);
  }

  /**
   * Gets an iterator over the row id of this table matching the given
   * predicate.
   * @param pred an obvious predicate
   * @return an iterator over the rows of this table.
   */
  public IntIterator rowIterator(Predicate pred) {
    return new FilterIntIterator(this, pred);
  }

  /**
   * Sets a value.
   * @param rowId row index
   * @param field column name
   * @param val value to set
   */
  public void set(int rowId, String field, Object val) {
    set(rowId, schema.getColumnIndex(field), val);
  }

  /**
   * Sets a value.
   * @param rowId row index
   * @param col column index
   * @param val value to set
   */
  public void set(int rowId, int col, Object val) {
    try {
      if (val != null) {
        TypedFormat format = formatFactory.getFormat(
            val.getClass().getSimpleName());
        if (format instanceof FormatFactoryImpl.TypedDecimalFormat) {
          table.setValueAt(val.toString(), rowId, col);
        } else {
        //StringBuffer v = format.format(val,
        //    new StringBuffer(), new FieldPosition(0));
        table.setValueAt(val.toString(), rowId, col);
        }
      } else {
        table.setValueAt(val, rowId, col);
      }
      this.fireTableEvent(rowId, rowId, col, TableListener.UPDATE);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed table region
   * @param end the ending row index of the changed table region
   * @param col the column that has changed
   * @param type the type of modification
   */
  public void fireTableEvent(int start, int end, int col, int type) {
   if (this.getTableListeners().isEmpty()) {
     return;
   }
   for (TableListener listnr : this.getTableListeners()) {
     listnr.tableChanged(this, start, end, col, type);
   }
  }

  /**
   * Wrapper around ivtk TableIterator class.
   * It makes this class compliant to obvious IntIterator.
   * @author Pierre-Luc Hemery
   *
   */
  public class IvtkIntIterator implements IntIterator {

    /**
     * Wrapped TableIterator.
     */
    private TableIterator it;

    /**
     * Constructor for IvktInterator.
     * @param iter an existing ivtk TableIteror
     */
    public IvtkIntIterator(TableIterator iter) {
      this.it = iter;
    }

    /**
     * Returns the next element in the iteration as an int.
     * @return the next element in iteration (as int)
     */
    public int nextInt() {
      return it.nextRow();
    }

    /**
     * Returns true if the iteration has more elements. (In other words, returns
     * true if next would return an element rather than throwing an exception.)
     * @return true if the iterator has more elements.
     */
    public boolean hasNext() {
      return it.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in iteration (as Integer)
     */
    public Integer next() {
      return it.nextRow();
    }

    /**
     * Unsupported.
     */
    public void remove() {
      it.remove();
    }
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(infovis.Table.class) || type.equals(
        infovis.table.DefaultDynamicTable.class)) {
      return table;
    }
    return null;
  }

}
