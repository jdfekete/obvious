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
import java.util.Enumeration;
import java.util.Random;

import obvious.data.Table;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ObviousWekaInstances extends Instances {
  
  private Table table;
  
  public ObviousWekaInstances(Table table, String name, FastVector attInfo, int cap) {
    super(name, attInfo, cap);
    this.table = table;
  }
  
  @Override
  public void add(Instance instance) {
    // TODO Auto-generated method stub
    super.add(instance);
  }

  @Override
  public Attribute attribute(int index) {
    // TODO Auto-generated method stub
    return super.attribute(index);
  }

  @Override
  public Attribute attribute(String arg0) {
    // TODO Auto-generated method stub
    return super.attribute(arg0);
  }

  @Override
  public AttributeStats attributeStats(int arg0) {
    // TODO Auto-generated method stub
    return super.attributeStats(arg0);
  }

  @Override
  public double[] attributeToDoubleArray(int arg0) {
    // TODO Auto-generated method stub
    return super.attributeToDoubleArray(arg0);
  }

  @Override
  public boolean checkForAttributeType(int attType) {
    // TODO Auto-generated method stub
    return super.checkForAttributeType(attType);
  }

  @Override
  public boolean checkForStringAttributes() {
    // TODO Auto-generated method stub
    return super.checkForStringAttributes();
  }

  @Override
  public boolean checkInstance(Instance arg0) {
    // TODO Auto-generated method stub
    return super.checkInstance(arg0);
  }

  @Override
  public Attribute classAttribute() {
    // TODO Auto-generated method stub
    return super.classAttribute();
  }

  @Override
  public int classIndex() {
    // TODO Auto-generated method stub
    return super.classIndex();
  }

  @Override
  public void compactify() {
    // TODO Auto-generated method stub
    super.compactify();
  }

  @Override
  protected void copyInstances(int arg0, Instances arg1, int arg2) {
    // TODO Auto-generated method stub
    super.copyInstances(arg0, arg1, arg2);
  }

  @Override
  public void delete() {
    // TODO Auto-generated method stub
    super.delete();
  }

  @Override
  public void delete(int index) {
    // TODO Auto-generated method stub
    super.delete(index);
  }

  @Override
  public void deleteAttributeAt(int arg0) {
    // TODO Auto-generated method stub
    super.deleteAttributeAt(arg0);
  }

  @Override
  public void deleteAttributeType(int attType) {
    // TODO Auto-generated method stub
    super.deleteAttributeType(attType);
  }

  @Override
  public void deleteStringAttributes() {
    // TODO Auto-generated method stub
    super.deleteStringAttributes();
  }

  @Override
  public void deleteWithMissing(Attribute att) {
    // TODO Auto-generated method stub
    super.deleteWithMissing(att);
  }

  @Override
  public void deleteWithMissing(int arg0) {
    // TODO Auto-generated method stub
    super.deleteWithMissing(arg0);
  }

  @Override
  public void deleteWithMissingClass() {
    // TODO Auto-generated method stub
    super.deleteWithMissingClass();
  }

  @Override
  public Enumeration enumerateAttributes() {
    // TODO Auto-generated method stub
    return super.enumerateAttributes();
  }

  @Override
  public Enumeration enumerateInstances() {
    // TODO Auto-generated method stub
    return super.enumerateInstances();
  }

  @Override
  public boolean equalHeaders(Instances arg0) {
    // TODO Auto-generated method stub
    return super.equalHeaders(arg0);
  }

  @Override
  public Instance firstInstance() {
    // TODO Auto-generated method stub
    return super.firstInstance();
  }

  @Override
  protected void freshAttributeInfo() {
    // TODO Auto-generated method stub
    super.freshAttributeInfo();
  }

  @Override
  public Random getRandomNumberGenerator(long seed) {
    // TODO Auto-generated method stub
    return super.getRandomNumberGenerator(seed);
  }

  @Override
  public String getRevision() {
    // TODO Auto-generated method stub
    return super.getRevision();
  }

  @Override
  protected void initialize(Instances dataset, int capacity) {
    // TODO Auto-generated method stub
    super.initialize(dataset, capacity);
  }

  @Override
  public void insertAttributeAt(Attribute arg0, int arg1) {
    // TODO Auto-generated method stub
    super.insertAttributeAt(arg0, arg1);
  }

  @Override
  public Instance instance(int index) {
    // TODO Auto-generated method stub
    return super.instance(index);
  }

  @Override
  protected String instancesAndWeights() {
    // TODO Auto-generated method stub
    return super.instancesAndWeights();
  }

  @Override
  public double kthSmallestValue(Attribute att, int k) {
    // TODO Auto-generated method stub
    return super.kthSmallestValue(att, k);
  }

  @Override
  public double kthSmallestValue(int attIndex, int k) {
    // TODO Auto-generated method stub
    return super.kthSmallestValue(attIndex, k);
  }

  @Override
  public Instance lastInstance() {
    // TODO Auto-generated method stub
    return super.lastInstance();
  }

  @Override
  public double meanOrMode(Attribute att) {
    // TODO Auto-generated method stub
    return super.meanOrMode(att);
  }

  @Override
  public double meanOrMode(int arg0) {
    // TODO Auto-generated method stub
    return super.meanOrMode(arg0);
  }

  @Override
  public int numAttributes() {
    // TODO Auto-generated method stub
    return super.numAttributes();
  }

  @Override
  public int numClasses() {
    // TODO Auto-generated method stub
    return super.numClasses();
  }

  @Override
  public int numDistinctValues(Attribute att) {
    // TODO Auto-generated method stub
    return super.numDistinctValues(att);
  }

  @Override
  public int numDistinctValues(int arg0) {
    // TODO Auto-generated method stub
    return super.numDistinctValues(arg0);
  }

  @Override
  public int numInstances() {
    // TODO Auto-generated method stub
    return super.numInstances();
  }

  @Override
  protected int partition(int attIndex, int l, int r) {
    // TODO Auto-generated method stub
    return super.partition(attIndex, l, r);
  }

  @Override
  protected void quickSort(int arg0, int arg1, int arg2) {
    // TODO Auto-generated method stub
    super.quickSort(arg0, arg1, arg2);
  }

  @Override
  public void randomize(Random arg0) {
    // TODO Auto-generated method stub
    super.randomize(arg0);
  }

  @Override
  public boolean readInstance(Reader reader) throws IOException {
    // TODO Auto-generated method stub
    return super.readInstance(reader);
  }

  @Override
  public String relationName() {
    // TODO Auto-generated method stub
    return super.relationName();
  }

  @Override
  public void renameAttribute(Attribute att, String name) {
    // TODO Auto-generated method stub
    super.renameAttribute(att, name);
  }

  @Override
  public void renameAttribute(int arg0, String arg1) {
    // TODO Auto-generated method stub
    super.renameAttribute(arg0, arg1);
  }

  @Override
  public void renameAttributeValue(Attribute att, String val, String name) {
    // TODO Auto-generated method stub
    super.renameAttributeValue(att, val, name);
  }

  @Override
  public void renameAttributeValue(int arg0, int arg1, String arg2) {
    // TODO Auto-generated method stub
    super.renameAttributeValue(arg0, arg1, arg2);
  }

  @Override
  public Instances resample(Random random) {
    // TODO Auto-generated method stub
    return super.resample(random);
  }

  @Override
  public Instances resampleWithWeights(Random arg0, double[] arg1) {
    // TODO Auto-generated method stub
    return super.resampleWithWeights(arg0, arg1);
  }

  @Override
  public Instances resampleWithWeights(Random arg0) {
    // TODO Auto-generated method stub
    return super.resampleWithWeights(arg0);
  }

  @Override
  protected int select(int arg0, int arg1, int arg2, int arg3) {
    // TODO Auto-generated method stub
    return super.select(arg0, arg1, arg2, arg3);
  }

  @Override
  public void setClass(Attribute att) {
    // TODO Auto-generated method stub
    super.setClass(att);
  }

  @Override
  public void setClassIndex(int classIndex) {
    // TODO Auto-generated method stub
    super.setClassIndex(classIndex);
  }

  @Override
  public void setRelationName(String newName) {
    // TODO Auto-generated method stub
    super.setRelationName(newName);
  }

  @Override
  public void sort(Attribute att) {
    // TODO Auto-generated method stub
    super.sort(att);
  }

  @Override
  public void sort(int attIndex) {
    // TODO Auto-generated method stub
    super.sort(attIndex);
  }

  @Override
  public void stratify(int arg0) {
    // TODO Auto-generated method stub
    super.stratify(arg0);
  }

  @Override
  protected void stratStep(int arg0) {
    // TODO Auto-generated method stub
    super.stratStep(arg0);
  }

  @Override
  public Instances stringFreeStructure() {
    // TODO Auto-generated method stub
    return super.stringFreeStructure();
  }

  @Override
  protected String stringWithoutHeader() {
    // TODO Auto-generated method stub
    return super.stringWithoutHeader();
  }

  @Override
  public double sumOfWeights() {
    // TODO Auto-generated method stub
    return super.sumOfWeights();
  }

  @Override
  public void swap(int i, int j) {
    // TODO Auto-generated method stub
    super.swap(i, j);
  }

  @Override
  public Instances testCV(int arg0, int arg1) {
    // TODO Auto-generated method stub
    return super.testCV(arg0, arg1);
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return super.toString();
  }

  @Override
  public String toSummaryString() {
    // TODO Auto-generated method stub
    return super.toSummaryString();
  }

  @Override
  public Instances trainCV(int numFolds, int numFold, Random random) {
    // TODO Auto-generated method stub
    return super.trainCV(numFolds, numFold, random);
  }

  @Override
  public Instances trainCV(int arg0, int arg1) {
    // TODO Auto-generated method stub
    return super.trainCV(arg0, arg1);
  }

  @Override
  public double variance(Attribute att) {
    // TODO Auto-generated method stub
    return super.variance(att);
  }

  @Override
  public double variance(int arg0) {
    // TODO Auto-generated method stub
    return super.variance(arg0);
  }
  
}


