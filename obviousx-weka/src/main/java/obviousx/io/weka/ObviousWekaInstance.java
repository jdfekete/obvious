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

import java.util.Date;
import java.util.Enumeration;

import obvious.data.Tuple;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class extends Weka Instance and allows to wrap an Obvious Tuple into
 * a Weka Instance.
 * This class should not be normally manipulated directly by a developer
 * -i.e. outside the ObviousWekaInstances-. To extract Instance from an
 * Obvious data structure, a developer should first use ObviousWekaInstances
 * class and then retrieve Instances from it.
 * @author plhemery
 *
 */
@SuppressWarnings("serial")
public class ObviousWekaInstance extends Instance {

  /**
   * Wrapped obvious tuple.
   */
  private Tuple tuple;
  
  /**
   * Reference instances.
   */
  private Instances instances;
  
  /**
   * Constructor.
   * @param tuple an obvious tuple
   */
  public ObviousWekaInstance(Tuple tuple, Instances instances) {
    this.tuple = tuple;
    this.instances = instances;
    this.m_Dataset = this.instances;
    this.m_Weight = 1.0;
    this.m_AttValues = new double[tuple.getSchema().getColumnCount()];
    this.m_AttValues = toDoubleArray();
  }
  
  @Override
  public Attribute attribute(int index) {
    return dataset().attribute(index);
  }

  @Override
  public Attribute attributeSparse(int indexOfIndex) {
    return dataset().attribute(indexOfIndex);
  }

  @Override
  public Attribute classAttribute() {
    return dataset().classAttribute();
  }

  @Override
  public int classIndex() {
    return dataset().classIndex();
  }

  @Override
  public boolean classIsMissing() {
    return isMissing(classIndex());
  }

  @Override
  public double classValue() {
    return value(classIndex());
  }

  @Override
  public Object copy() {
    Instance result = new Instance(this);
    return result;
  }

  @Override
  public Instances dataset() {
    return instances;
  }

  @Override
  public void deleteAttributeAt(int position) {
    tuple.set(position, null);
  }

  @Override
  public Enumeration enumerateAttributes() {
    return dataset().enumerateAttributes();
  }

  @Override
  public boolean equalHeaders(Instance inst) {
    return dataset().equalHeaders(inst.dataset());
  }

  /**
   * Unused
   * @return Unused
   */
  public String getRevision() {
    return "";
  }

  @Override
  public boolean hasMissingValue() {
    for (int i = 0; i < numAttributes(); i++) {
      if (i != classIndex()) {
        if (isMissing(i)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public int index(int position) {
    return position;
  }

  @Override
  public void insertAttributeAt(int position) {
    return;
  }

  @Override
  public boolean isMissing(Attribute att) {
    return isMissing(att.index());
  }

  @Override
  public boolean isMissing(int attIndex) {
    if (Double.isNaN(value(attIndex))) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isMissingSparse(int indexOfIndex) {
    if (Double.isNaN(value(indexOfIndex))) {
      return true;
    }
    return false;
  }

  @Override
  public Instance mergeInstance(Instance arg0) {
    return super.mergeInstance(arg0);
  }

  @Override
  public int numAttributes() {
    return tuple.getSchema().getColumnCount();
  }

  @Override
  public int numClasses() {
    return dataset().numClasses();
  }

  @Override
  public int numValues() {
    return tuple.getSchema().getColumnCount();
  }

  @Override
  public void replaceMissingValues(double[] arg0) {
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      if (isMissing(i)) {
        tuple.set(i, arg0[i]);
      }
    }
  }

  @Override
  public void setClassMissing() {
    setMissing(classIndex());
  }

  @Override
  public void setClassValue(double value) {
    setValue(classIndex(), value);
  }

  @Override
  public void setValue(int attIndex, double value) {
    Class<?> c = tuple.getSchema().getColumnType(attIndex);
    if (ObviousWekaUtils.isNumeric(c)) {
      tuple.set(attIndex, value); 
    } else if (ObviousWekaUtils.isString(c)) {
      return;
    } else if (ObviousWekaUtils.isDate(c)) {
      Date date = new Date((long) value);
      tuple.set(attIndex, date);
    }
  }

  @Override
  public void setValueSparse(int indexOfIndex, double value) {
    setValue(indexOfIndex, value);
  }

  @Override
  public double[] toDoubleArray() {
    for (int i = tuple.getSchema().getColumnCount() - 1; i >= 0; i--) {
      m_AttValues[i] = this.value(i);
    }
    return m_AttValues;
  }

  @Override
  public String toString() {
    StringBuffer text = new StringBuffer();
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      if (i > 0) text.append(",");
      text.append(tuple.get(i));
    }
    return text.toString();
  }


  protected String toStringNoWeight() {
    return toString();
  }

  @Override
  public double value(Attribute att) {
    return value(att.index());
  }

  @Override
  public double value(int attIndex) {
    Class<?> c = tuple.getSchema().getColumnType(attIndex);
    double value = -1;
    if (ObviousWekaUtils.isNumeric(c)) {
      return Double.valueOf(tuple.get(attIndex).toString());
    } else if (ObviousWekaUtils.isString(c)) {
      return Double.valueOf(attribute(attIndex).indexOfValue(
          tuple.getString(attIndex)));
    } else if (ObviousWekaUtils.isDate(c)) {
      return tuple.getDate(attIndex).getTime();
    }
    return value;
  }

  @Override
  public double valueSparse(int indexOfIndex) {
    return value(indexOfIndex);
  }

}
