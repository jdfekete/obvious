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

package obvious.weka.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.SchemaImpl;
import obviousx.io.weka.ObviousWekaUtils;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


/**
 * An implementation of Obvious Table based on Weka.
 * This table can only contains String, Date and Numeric types of values.
 * However, this implementation is fully compatible with the Obvious Table
 * interface.
 * @author Hemery
 *
 */
public class WekaObviousTable implements Table {

  /**
   * A Weka Instances instance.
   */
  private Instances instances;
  
  /**
   * Obvious schema for this table.
   */
  private Schema schema;
  
  /**
   * Listeners collection.
   */
  private ArrayList<TableListener> listeners;

  /**
   * Is the schema being edited.
   */
  private boolean editing = false;

  /**
   * Constructor.
   * @param inst a Weka Instances instance
   */
  @SuppressWarnings("unchecked")
  public WekaObviousTable(Instances inst) {
    this.instances = inst;
    this.schema = new SchemaImpl();
    Enumeration attributes = instances.enumerateAttributes();
    while (attributes.hasMoreElements()) {
      Attribute att = (Attribute) attributes.nextElement();
      Class<?> c = checkClass(att);
      schema.addColumn(att.name(), c, null);
    }
  }
  
  /**
   * Constructor.
   * @param inSchema an Obvious schema
   */
  public WekaObviousTable(Schema inSchema) {
    this.schema = inSchema;
    this.instances = createInstances();
  }
  
  protected  Instances createInstances() {
    FastVector attributes = new FastVector();
    for (int i = 0; i < schema.getColumnCount(); i++) {
      Attribute attribute = createAttribute(schema.getColumnName(i),
          schema.getColumnType(i));
      attributes.addElement(attribute);
    }
    return new Instances("test", attributes, 1);
  }
  
  /**
   * Creates weka Attribute.
   * @param colName attribute name
   * @param colType attribute type
   * @return a weka Attribute
   */
  protected Attribute createAttribute(String colName, Class<?> colType) {
    Attribute attr = null;
    if (ObviousWekaUtils.isNumeric(colType)) {
      attr = new Attribute(colName);
    } else if (ObviousWekaUtils.isString(colType)) {
      attr = new Attribute(colName, (FastVector) null);
    } else if (ObviousWekaUtils.isDate(colType)) {
      attr = new Attribute(colName, "yyyy-MM-dd");
    } else if (ObviousWekaUtils.isNominal(colType)) {
      attr = new Attribute(colName, (FastVector) null);
    } else if (ObviousWekaUtils.isRelational(colType)) {
      attr = new Attribute(colName);
    }
    return attr;
  }

  /**
   * Determines the corresponding Obvious class for a Weka instances.
   * @param att
   * @return
   */
  private Class<?> checkClass(Attribute att) {
    if (att.isDate()) {
      return Date.class;
    } else if (att.isNumeric()) {
      return Number.class;
    } else if (att.isNominal() || att.isString()) {
      return String.class;
    }
    return String.class;
  }
  
  @Override
  public int addRow() {
    return -1;
  }

  @Override
  public int addRow(Tuple tuple) {
    Schema tupleSchema = tuple.getSchema();
    for (int i = 0; i < tupleSchema.getColumnCount(); i++) {
      if (!tupleSchema.getColumnName(i).equals(schema.getColumnName(i))
          || !tupleSchema.getColumnType(i).equals(schema.getColumnType(i))) {
        return -1;
      }
    }
    Instance inst = new Instance(schema.getColumnCount());
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      Attribute att = instances.attribute(i);
      if (att.isNumeric())  {
        inst.setValue(att, Double.parseDouble(tuple.get(i).toString()));
      } else if (att.isString() || att.isNominal()) {
        inst.setValue(att, tuple.getString(i));
      } else if (att.isDate()) {
        try {
          inst.setValue(att, att.parseDate(tuple.getDate(i).toString()));
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
    }
    instances.add(inst);
    return getRowCount();
  }

  @Override
  public void addTableListener(TableListener listnr) {
    listeners.add(listnr); 
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
    return true;
  }

  @Override
  public void fireTableEvent(int start, int end, int col, int type) {
    if (listeners.isEmpty()) {
      return;
    }
    for (TableListener listnr : listeners) {
      listnr.tableChanged(this, start, end, col, type);
    }
  }

  @Override
  public int getRowCount() {
    return instances.numInstances();
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
    return getValue(rowId, schema.getColumnIndex(field));
  }

  @Override
  public Object getValue(int rowId, int col) {
    if (!isValueValid(rowId, col)) {
      return null; 
    }
    Attribute att = instances.attribute(col);
    if (att.isNumeric()) {
      return instances.attributeToDoubleArray(col)[rowId];
    } else if (att.isNominal() || att.isString()) {
      return instances.instance(rowId).stringValue(col);
    } else if (att.isDate()) {
      double dateValue = instances.attributeToDoubleArray(col)[rowId];
      return att.formatDate(dateValue);
    }
    return null;
  }

  @Override
  public boolean isEditing(int col) {
    return this.editing;
  }

  @Override
  public boolean isValidRow(int rowId) {
    if (rowId < 0 || rowId >= this.getRowCount()) {
      return false;
    }
    return instances.instance(rowId) != null;
  }

  @Override
  public boolean isValueValid(int rowId, int col) {
    return isValidRow(rowId) && col < schema.getColumnCount();
  }

  @Override
  public void removeAllRows() {
    instances.delete();
  }

  @Override
  public boolean removeRow(int row) {
    if (instances.instance(row) == null) {
      return false;
    }
    instances.delete(row);
    return true;
  }

  @Override
  public void removeTableListener(TableListener listnr) {
    listeners.remove(listnr);
  }

  @Override
  public IntIterator rowIterator() {
    return new WekaObviousIntIterator(instances);
  }

  @Override
  public IntIterator rowIterator(Predicate pred) {
    return new WekaObviousIntIterator(instances);
  }

  @Override
  public void set(int rowId, String field, Object val) {
    set(rowId, schema.getColumnIndex(field), val);
  }

  @Override
  public void set(int rowId, int col, Object val) {
    if (!isValueValid(rowId, col)) {
      return;
    }
    Attribute att = instances.attribute(col);
    if (att.isNumeric())  {
      instances.instance(rowId).setValue(att, Double.parseDouble(
          val.toString()));
      return;
    } else if (att.isString() || att.isNominal()) {
      instances.instance(rowId).setValue(att, val.toString());
      return;
    } else if (att.isDate()) {
      try {
        instances.instance(rowId).setValue(att, att.parseDate(val.toString()));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return;
    }
  }

  @Override
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(weka.core.Instances.class)) {
      return instances;
    }
    return null;
  }

  public class WekaObviousIntIterator implements IntIterator {

    @SuppressWarnings("unchecked")
    private Enumeration enumInst;
    
    private int currentIndex = -1;
    
    /**
     * Constructor.
     */
    public WekaObviousIntIterator(Instances instances) {
      this.enumInst = instances.enumerateInstances();
    }
    
    @Override
    public int nextInt() {
      return currentIndex++;
    }

    @Override
    public boolean hasNext() {
      return enumInst.hasMoreElements();
    }

    @Override
    public Integer next() {
      return  currentIndex++;
    }

    @Override
    public void remove() {
    }
    
  }
}
