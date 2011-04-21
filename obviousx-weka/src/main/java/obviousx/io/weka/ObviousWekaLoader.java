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

package obviousx.io.weka;

import java.text.ParseException;
import java.util.Date;

import obvious.data.Table;
import obvious.data.util.IntIterator;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class allows to load an Obvious Table into a Weka Instances.
 * Data structures are duplicated, so this approach is not memory oriented.
 * However, it allows to directly use data structures introduced in Weka
 * that are designed to be efficient with Weka's algorithms.
 * This class should be used when a developper wants to optimize execution
 * time.
 * @author Hemery
 *
 */
public class ObviousWekaLoader {

  /**
   * Obvious table.
   */
  private Table table;
  
  /**
   * Weka Instances.
   */
  private Instances instances;
  
  
  /**
   * Constructor.
   */
  public ObviousWekaLoader(Table table) {
    this.table = table;
  }
  
  /**
   * Loads the weka Instances from the Obvious table indicated in the
   * constructor.
   * @return a weka Instance
   */
  public Instances loadInstances() {
    instances = new Instances("obvious-dataset", createAttributes(),
        table.getRowCount());
    loadData();
    return instances;
  }
  
  /**
   * Creates attributes vector from an obvious table.
   * @return vector from attributes
   */
  protected FastVector createAttributes() {
    FastVector attributes = new FastVector();
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      attributes.addElement(createAttribute(table.getSchema().getColumnName(i),
          table.getSchema().getColumnType(i)));
      
    }
    return attributes;
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
   * Load data for weka Instances.
   */
  protected void loadData() {
    IntIterator it = table.rowIterator();
    while(it.hasNext()) {
      double[] values = new double[table.getRowCount()];
      int currentRow = it.next();
      int currentIndex = 0;
      for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
        Class<?> c = table.getSchema().getColumnType(i);
        if (ObviousWekaUtils.isNumeric(c)) {
          values[currentIndex] = Double.valueOf(table.getValue(currentRow, i).toString());
        } else if (ObviousWekaUtils.isString(c)) {
          values[currentIndex] = instances.attribute(currentIndex)
               .addStringValue((String) table.getValue(currentRow, i));
        } else if (ObviousWekaUtils.isDate(c)) {
          try {
            values[currentIndex] = instances.attribute(currentIndex).parseDate(
                ((Date) table.getValue(currentRow, i)).toString());
          } catch (ParseException e) {
            e.printStackTrace();
          }
        } else if (ObviousWekaUtils.isNominal(c)) {
          values[currentIndex] = instances.attribute(currentIndex)
          .addStringValue((String) table.getValue(currentRow, i));
        } else if (ObviousWekaUtils.isRelational(c)) {
          values[currentIndex] = instances.attribute(currentIndex)
          .addStringValue((String) table.getValue(currentRow, i));
        }
        currentIndex++;
      }
      instances.add(new Instance(1.0, values));
    }
  }
  
  
}
