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

package obviousx.io.rminer;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Table;
import obvious.data.util.IntIterator;
import obviousx.io.weka.ObviousWekaUtils;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeRole;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.DataRowReader;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.att.AttributeSet;

/**
 * This class implements the RapidMiner interface ExampleTable. It mainly
 * wraps an Obvious Table instance into an ExampleTable instance.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class ObviousRMinerExTable implements ExampleTable {

  /**
   * Obvious table.
   */
  private Table table;
  
  /**
   * DataRowFactory for RapidMiner.
   */
  private DataRowFactory dataFactory;
  
  /**
   * List of DataRows instances that compose this table.
   */
  private List<DataRow> dataList = new ArrayList<DataRow>();
  
  /**
   * List of attributes.
   */
  private Attribute[] attributes;
  
  /**
   * Constructor.
   * @param inTable an ObviousTable
   */
  public ObviousRMinerExTable(Table inTable) {
    this.table = inTable;
    this.dataFactory = new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY);
    IntIterator it = table.rowIterator();
    this.attributes = new Attribute[this.table.getSchema().getColumnCount()];
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      attributes[i] = getAttribute(i);
    }
    while (it.hasNext()) {
      int id = it.nextInt();
      Object[] data = new Object[this.getAttributeCount()];
      for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
        data[i] = table.getValue(id, i);
      }
      dataList.add(dataFactory.create(data, attributes));
    }
  }
  
  @Override
  public int addAttribute(Attribute attribute) {
    try {
      int type = attribute.getValueType();
      String name = attribute.getName();
      if (type == Ontology.INTEGER) {
        table.getSchema().addColumn(name, Integer.class, -1);
      } else if (type == Ontology.NUMERICAL || type == Ontology.REAL) {
        table.getSchema().addColumn(name, Double.class, -1);
      } else if (type == Ontology.STRING) {
        table.getSchema().addColumn(name, String.class, "defaultValue");
      } else if (type == Ontology.DATE) {
        table.getSchema().addColumn(name, Date.class, new Date(0));
      } else if (type == Ontology.NOMINAL) {
        table.getSchema().addColumn(name, String.class, "defaultValue");
      }
      return table.getSchema().getColumnCount();
    } catch (ObviousRuntimeException e) {
      System.err.println("This Obvious binding doest not support column "
          + "add/removal for tables that are already filled");
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public void addAttributes(Collection<Attribute> attributes) {
    for (Attribute att : attributes) {
      addAttribute(att);
    }
  }

  @Override
  public ExampleSet createExampleSet(Attribute label) {
    return createExampleSet(label, null, null);
  }
  
  @Override
  public ExampleSet createExampleSet(Attribute labelAttribute,
      Attribute weightAttribute, Attribute idAttribute) {
    Map<Attribute, String> specialAttributes = new HashMap<Attribute, String>();
    if (labelAttribute != null)
      specialAttributes.put(labelAttribute, Attributes.LABEL_NAME);
    if (weightAttribute != null)
      specialAttributes.put(weightAttribute, Attributes.WEIGHT_NAME);
    if (idAttribute != null)
      specialAttributes.put(idAttribute, Attributes.ID_NAME);
    return new SimpleExampleSet(this, specialAttributes);
  }
  
  @Override
  public ExampleSet createExampleSet(Iterator<AttributeRole> newSpecialAttributes) {
    Map<Attribute, String> attributes = new HashMap<Attribute, String>();
    while (newSpecialAttributes.hasNext()) {
      AttributeRole attRole = newSpecialAttributes.next();
      attributes.put(attRole.getAttribute(), attRole.getSpecialName());
    }
    return new SimpleExampleSet(this, attributes);
  }
  

  @Override
  public ExampleSet createExampleSet(AttributeSet attributeSet) {
    Map<Attribute, String> attributes = new HashMap<Attribute, String>();
    for (String name : attributeSet.getSpecialNames()) {
      attributes.put(attributeSet.getSpecialAttribute(name), name);
    }
    return createExampleSet(attributes);
  }

  @Override
  public ExampleSet createExampleSet() {
    return createExampleSet(new HashMap<Attribute, String>());
  }

  @Override
  public ExampleSet createExampleSet(Map<Attribute, String> attributes) {
    return new SimpleExampleSet(this, attributes);
  }

  @Override
  public Attribute findAttribute(String field) throws OperatorException {
    return getAttribute(table.getSchema().getColumnIndex(field));
  }

  @Override
  public Attribute getAttribute(int id) {
    Attribute att;
    if (ObviousWekaUtils.isNumeric(table.getSchema().getColumnType(id))) {
      att = AttributeFactory.createAttribute(
          table.getSchema().getColumnName(id), Ontology.REAL);
    } else if (ObviousWekaUtils.isNominal(
        table.getSchema().getColumnType(id))) {
      att = AttributeFactory.createAttribute(
          table.getSchema().getColumnName(id), Ontology.NOMINAL);
    } else if (ObviousWekaUtils.isDate(table.getSchema().getColumnType(id))) {
      att = AttributeFactory.createAttribute(
          table.getSchema().getColumnName(id), Ontology.DATE);
    } else {
      att = AttributeFactory.createAttribute(
          table.getSchema().getColumnName(id), Ontology.NOMINAL);
    }
    att.setTableIndex(id);
    return att;
  }

  @Override
  public int getAttributeCount() {
    return this.table.getSchema().getColumnCount();
  }

  @Override
  public Attribute[] getAttributes() {
    Attribute[] attributes = new Attribute[table.getSchema().getColumnCount()];
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      attributes[i] = getAttribute(i);
    }
    return attributes;
  }

  @Override
  public DataRow getDataRow(int arg0) {
    return dataList.get(arg0);
  }

  @Override
  public DataRowReader getDataRowReader() {
    List<DataRow> rows = new LinkedList<DataRow>();
    for (int i = 0; i < size(); i++) {
      rows.add(getDataRow(i));
    }
    return new ListDataRowReader(rows.iterator());
  }

  @Override
  public int getNumberOfAttributes() {
    return this.table.getSchema().getColumnCount();
  }

  @Override
  public void removeAttribute(Attribute attribute) {
    removeAttribute(table.getSchema().getColumnIndex(attribute.getName()));
  }

  @Override
  public void removeAttribute(int col) {
    try {
      table.getSchema().removeColumn(col);
    } catch (ObviousRuntimeException e) {
      System.err.println("This Obvious binding doest not support column "
          + "add/removal for tables that are already filled or the column "
          + "does not exist (index " + col + ")");
      e.printStackTrace();
    }
  }

  @Override
  public int size() {
    return this.table.getRowCount();
  }

  @Override
  public String toDataString() {
    StringBuffer result = new StringBuffer(toString() + Tools.getLineSeparator());
    DataRowReader reader = getDataRowReader();
    while (reader.hasNext()) {
      result.append(reader.next().toString() + Tools.getLineSeparator());
    }
    return result.toString();
  }

}
