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

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.util.IntIterator;
import obvious.impl.TupleImpl;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class ObviousWekaInstances extends Instances {
  
  private Table table;
 
  protected int m_classIndex;
  
  public ObviousWekaInstances(Table table, String name, FastVector attInfo, int cap) {
    super(name, attInfo, cap);
    this.table = table;
    IntIterator iter = this.table.rowIterator();
    while(iter.hasNext()) {
      m_Instances.addElement(new ObviousWekaInstance(
          new TupleImpl(table, iter.nextInt()), this));
    }
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {

      m_Attributes.addElement(attribute(i));
    }
  }
  
  @Override
  public void add(Instance instance) {
    double[] values = instance.toDoubleArray();
    Object[] obvValues = new Object[table.getSchema().getColumnCount()];
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      Attribute att = instance.attribute(i);
      Class<?> c = table.getSchema().getColumnType(i);
      if(att.isString() && ObviousWekaUtils.isString(c)) {
        obvValues[i] = instance.attribute(i).value(i);
      } else if (att.isNumeric() && ObviousWekaUtils.isNumeric(c)) {
        obvValues[i] = instance.value(att);
      } else if (att.isDate() && ObviousWekaUtils.isDate(c)) {
        obvValues[i] = new Date((long) values[i]);
      } else {
        obvValues[i] = null;
      }
    }
    Tuple tuple = new TupleImpl(table.getSchema(), obvValues);
    m_Instances.addElement(new ObviousWekaInstance(tuple, this));
    table.addRow(tuple);
  }

  @Override
  public Attribute attribute(int index) {
    if (index >= table.getSchema().getColumnCount()) {
      return null;
    }
    FastVector fastVect = new FastVector();
    Class<?> c = table.getSchema().getColumnType(index);
    IntIterator iter = table.rowIterator();
    while (iter.hasNext()) {
      int row = iter.nextInt();
      if (!fastVect.contains(table.getValue(row, index))) {
        fastVect.addElement(table.getValue(row, index));
        }
    }
    if (ObviousWekaUtils.isNumeric(c)) {
      return new Attribute(table.getSchema().getColumnName(index));
    } else if (ObviousWekaUtils.isString(c)) {
      return new Attribute(table.getSchema().getColumnName(index), fastVect);
    } else if (ObviousWekaUtils.isDate(c)) {
      return new Attribute(table.getSchema().getColumnName(index), "yyyy-MM-dd", null);
    }
    return new Attribute(table.getSchema().getColumnName(index));
  }

  @Override
  public Attribute attribute(String arg0) {
    return attribute(table.getSchema().getColumnIndex(arg0));
  }

  @Override
  public AttributeStats attributeStats(int index) {
    ObviousWekaAttStats result = new ObviousWekaAttStats();
    if (attribute(index).isNominal()) {
      result.nominalCounts = new int [attribute(index).numValues()];
    }
    if (attribute(index).isNumeric()) {
      result.numericStats = new weka.experiment.Stats();
    }
    result.totalCount = numInstances();

    double [] attVals = attributeToDoubleArray(index);
    int [] sorted = Utils.sort(attVals);
    int currentCount = 0;
    double prev = Instance.missingValue();
    for (int j = 0; j < numInstances(); j++) {
      Instance current = instance(sorted[j]);
      if (current.isMissing(index)) {
        result.missingCount = numInstances() - j;
        break;
      }
      if (current.value(index) == prev) {
        currentCount++;
      } else {
        result.addDistinct(prev, currentCount);
        currentCount = 1;
        prev = current.value(index);
      }
    }
    result.addDistinct(prev, currentCount);
    result.distinctCount--; // So we don't count "missing" as a value
    return result;
  }

  @Override
  public double[] attributeToDoubleArray(int arg0) {
    return super.attributeToDoubleArray(arg0);
  }

  @Override
  public boolean checkForAttributeType(int attType) {
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      if (attribute(i).type() == attType) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean checkForStringAttributes() {
    return super.checkForStringAttributes();
  }

  @Override
  public boolean checkInstance(Instance arg0) {
    return super.checkInstance(arg0);
  }

  @Override
  public Attribute classAttribute() {
    return super.classAttribute();
  }

  @Override
  public int classIndex() {
    return super.classIndex();
  }

  @Override
  public void compactify() {
    m_Instances.trimToSize();
  }

  @Override
  protected void copyInstances(int arg0, Instances arg1, int arg2) {
    super.copyInstances(arg0, arg1, arg2);
  }

  @Override
  public void delete() {
    table.removeAllRows();
    super.delete();
  }

  @Override
  public void delete(int index) {
    table.removeRow(index);
    super.delete(index);
  }

  @Override
  public void deleteAttributeAt(int arg0) {
    super.deleteAttributeAt(arg0);
  }

  @Override
  public void deleteAttributeType(int attType) {
    super.deleteAttributeType(attType);
  }

  @Override
  public void deleteStringAttributes() {
    super.deleteStringAttributes();
  }

  @Override
  public void deleteWithMissing(Attribute att) {
    super.deleteWithMissing(att);
  }

  @Override
  public void deleteWithMissing(int arg0) {
    super.deleteWithMissing(arg0);
  }

  @Override
  public void deleteWithMissingClass() {
    super.deleteWithMissingClass();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Enumeration enumerateAttributes() {
    return super.enumerateAttributes();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Enumeration enumerateInstances() {
    return super.enumerateInstances();
  }

  @Override
  public boolean equalHeaders(Instances arg0) {
    return super.equalHeaders(arg0);
  }

  @Override
  public Instance firstInstance() {
    return super.firstInstance();
  }

  @Override
  protected void freshAttributeInfo() {
    super.freshAttributeInfo();
  }

  @Override
  public Random getRandomNumberGenerator(long seed) {
    return super.getRandomNumberGenerator(seed);
  }

  @Override
  public String getRevision() {
    return super.getRevision();
  }

  @Override
  protected void initialize(Instances dataset, int capacity) {
    super.initialize(dataset, capacity);
  }

  @Override
  public void insertAttributeAt(Attribute arg0, int arg1) {
    super.insertAttributeAt(arg0, arg1);
  }

  @Override
  public Instance instance(int index) {
    return new ObviousWekaInstance(new TupleImpl(table, index), this);
  }

  @Override
  protected String instancesAndWeights() {
    return super.instancesAndWeights();
  }

  @Override
  public double kthSmallestValue(Attribute att, int k) {
    return super.kthSmallestValue(att, k);
  }

  @Override
  public double kthSmallestValue(int attIndex, int k) {
    return super.kthSmallestValue(attIndex, k);
  }

  @Override
  public Instance lastInstance() {
    return super.lastInstance();
  }

  @Override
  public double meanOrMode(Attribute att) {
    return super.meanOrMode(att);
  }

  @Override
  public double meanOrMode(int arg0) {
    return super.meanOrMode(arg0);
  }

  @Override
  public int numAttributes() {
    return super.numAttributes();
  }

  @Override
  public int numClasses() {
    return super.numClasses();
  }

  @Override
  public int numDistinctValues(Attribute att) {
    return super.numDistinctValues(att);
  }

  @Override
  public int numDistinctValues(int arg0) {
    return super.numDistinctValues(arg0);
  }

  @Override
  public int numInstances() {
    return super.numInstances();
  }

  @Override
  protected int partition(int attIndex, int l, int r) {
    return super.partition(attIndex, l, r);
  }

  @Override
  protected void quickSort(int arg0, int arg1, int arg2) {
    super.quickSort(arg0, arg1, arg2);
  }

  @Override
  public void randomize(Random arg0) {
    super.randomize(arg0);
  }

  @Override
  public boolean readInstance(Reader reader) throws IOException {
    return super.readInstance(reader);
  }

  @Override
  public String relationName() {
    return super.relationName();
  }

  @Override
  public void renameAttribute(Attribute att, String name) {
    super.renameAttribute(att, name);
  }

  @Override
  public void renameAttribute(int arg0, String arg1) {
    super.renameAttribute(arg0, arg1);
  }

  @Override
  public void renameAttributeValue(Attribute att, String val, String name) {
    super.renameAttributeValue(att, val, name);
  }

  @Override
  public void renameAttributeValue(int arg0, int arg1, String arg2) {
    super.renameAttributeValue(arg0, arg1, arg2);
  }

  @Override
  public Instances resample(Random random) {
    return super.resample(random);
  }

  @Override
  public Instances resampleWithWeights(Random arg0, double[] arg1) {
    return super.resampleWithWeights(arg0, arg1);
  }

  @Override
  public Instances resampleWithWeights(Random arg0) {
    return super.resampleWithWeights(arg0);
  }

  @Override
  protected int select(int arg0, int arg1, int arg2, int arg3) {
    return super.select(arg0, arg1, arg2, arg3);
  }

  @Override
  public void setClass(Attribute att) {
    super.setClass(att);
  }

  @Override
  public void setClassIndex(int classIndex) {
    super.setClassIndex(classIndex);
  }

  @Override
  public void setRelationName(String newName) {
    super.setRelationName(newName);
  }

  @Override
  public void sort(Attribute att) {
    super.sort(att);
  }

  @Override
  public void sort(int attIndex) {
    super.sort(attIndex);
  }

  @Override
  public void stratify(int arg0) {
    super.stratify(arg0);
  }

  @Override
  protected void stratStep(int arg0) {
    super.stratStep(arg0);
  }

  @Override
  public Instances stringFreeStructure() {
    return super.stringFreeStructure();
  }

  @Override
  protected String stringWithoutHeader() {
    return super.stringWithoutHeader();
  }

  @Override
  public double sumOfWeights() {
    return super.sumOfWeights();
  }

  @Override
  public void swap(int i, int j) {
    super.swap(i, j);
  }

  @Override
  public Instances testCV(int arg0, int arg1) {
    return super.testCV(arg0, arg1);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public String toSummaryString() {
    return super.toSummaryString();
  }

  @Override
  public Instances trainCV(int numFolds, int numFold, Random random) {
    return super.trainCV(numFolds, numFold, random);
  }

  @Override
  public Instances trainCV(int arg0, int arg1) {
    return super.trainCV(arg0, arg1);
  }

  @Override
  public double variance(Attribute att) {
    return super.variance(att);
  }

  @Override
  public double variance(int arg0) {
    return super.variance(arg0);
  }
  
  public static class ObviousWekaAttStats extends AttributeStats {
    
    public void addDistinct(double value, int count) {
      if (count > 0) {
        if (count == 1) {
          uniqueCount++;
        }
        if (Utils.eq(value, (double)((int)value))) {
          intCount += count;
        } else {
          realCount += count;
        }
        if (nominalCounts != null) {
          nominalCounts[(int)value] = count;
        }
        if (numericStats != null) {
          numericStats.add(value, count);
          numericStats.calculateDerived();
        }
      }
      distinctCount++;
    }
    
  }
  
}


