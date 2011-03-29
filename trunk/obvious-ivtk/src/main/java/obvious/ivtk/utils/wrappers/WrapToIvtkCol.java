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

package obvious.ivtk.utils.wrappers;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.swing.event.ChangeListener;
import javax.swing.text.MutableAttributeSet;

import infovis.Column;
import infovis.column.FilterColumn;
import infovis.utils.RowIterator;

import obvious.ObviousException;
import obvious.impl.IntIteratorImpl;
import obviousx.ObviousxRuntimeException;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;
import obviousx.text.TypedFormat;


/**
 * Wrapper for ivtk column (obvious).
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class WrapToIvtkCol implements Column {

  /**
   * Backing obvious table.
   */
  private obvious.data.Table table;

  /**
   * Id of the column.
   */
  private int id;

  /**
   * The format of this column.
   */
  private TypedFormat format;

  /**
   * Format factory.
   */
  private FormatFactory formatFactory;


  /**
   * Constructor.
   * @param inTable backing obvious table
   * @param colId id of the column
   */
  public WrapToIvtkCol(obvious.data.Table inTable, int colId) {
    //super(null);
    this.table = inTable;
    this.id = colId;
    this.formatFactory = new FormatFactoryImpl();
    if (id != -1) {
    this.format = formatFactory.getFormat(
        table.getSchema().getColumnType(id).getSimpleName());
    }
  }

  @Override
  public void addChangeListener(ChangeListener listener) {
    // TODO Auto-generated method stub
  }

  @Override
  public int capacity() {
    return table.getRowCount();
  }

  @Override
  public void clear() {
    RowIterator it = iterator();
    while (it.hasNext()) {
      setValueUndefined(it.nextRow(), true);
    }
  }

  @Override
  public void copyValueFrom(int toIndex, Column c, int fromIndex)
      throws ParseException {
    // TODO Auto-generated method stub
  }

  @Override
  public void disableNotify() {
    try {
      table.endEdit(id);
    } catch (ObviousException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotify() {
    try {
      table.beginEdit(id);
    } catch (ObviousException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void ensureCapacity(int minCapacity) {
    // TODO Auto-generated method stub
  }

  @Override
  public String format(Object o) {
    return format.format(o, new StringBuffer(),
        new FieldPosition(0)).toString();
  }

  @Override
  public Format getFormat() {
    return format.getFormat();
  }

  @Override
  public int getMaxIndex() {
    return 0;
  }

  @Override
  public int getMinIndex() {
    return 0;
  }

  @Override
  public String getName() {
    return table.getSchema().getColumnName(id);
  }

  @Override
  public Object getObjectAt(int index) {
    return table.getValue(index, id);
  }

  @Override
  public String getValueAt(int index) {
    return format(table.getValue(index, id));
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getValueClass() {
    return table.getSchema().getColumnType(id);
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
    return table.getSchema().getColumnName(id) != null
    && table.getSchema().getColumnName(id).length() != 0
    && table.getSchema().getColumnName(id).charAt(0) == '#';
  }

  @Override
  public boolean isValueUndefined(int i) {
    if (i < 0 || i >= size()) {
      return true;
    }
    return table.getValue(i, id) == null;
  }

  @Override
  public RowIterator iterator() {
    obvious.impl.IntIteratorImpl it = new IntIteratorImpl(table.rowIterator());
    return new WrapToIvtkIterator(it);
  }

  @Override
  public Object parse(String s) throws ParseException {
    return format.parseObject(s, new ParsePosition(0));
  }

  @Override
  public void removeChangeListener(ChangeListener listener) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setFormat(Format inFormat) {
    format = formatFactory.getFormat(inFormat);
  }

  @Override
  public void setName(String name) {
    throw new ObviousxRuntimeException("Operation unsupported : setName!");
  }

  @Override
  public void setObjectAt(int index, Object o) {
    table.set(index, id, o);
  }

  @Override
  public void setSize(int newSize) {
    if (newSize <= size()) {
      return;
    }
    int rowToAdd = newSize - size();
    for (int i = 0; i < rowToAdd; i++) {
      table.addRow();
    }
  }

  @Override
  public void setValueAt(int index, String element) throws ParseException {
    setObjectAt(index, parse(element));
  }

  @Override
  public boolean setValueOrNullAt(int index, String v) {
    try {
      setValueAt(index, v);
      return true;
    } catch (Exception e) {
      setValueUndefined(index, true);
      return false;
    }
  }

  @Override
  public void setValueUndefined(int i, boolean undef) {
    if (undef) {
      table.set(i, id, null);
    }
  }

  @Override
  public int size() {
    return table.getRowCount();
  }

  @Override
  public MutableAttributeSet getClientProperty() {
    return null;
  }

  @Override
  public MutableAttributeSet getMetadata() {
    return null;
  }

  @Override
  public int compare(int arg0, int arg1) {
    return 0;
  }

  @Override
  public int compare(Object o1, Object o2) {
    return 0;
  }

}
