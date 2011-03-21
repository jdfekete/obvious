package obviousx.io.weka;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import obvious.data.Tuple;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

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
    this.m_Weight = 1.0;
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

  @Override
  public String getRevision() {
    return super.getRevision();
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
    double[] values = new double[tuple.getSchema().getColumnCount()];
    for (int i = 0; i < tuple.getSchema().getColumnCount(); i++) {
      values[i] = this.value(i);
    }
    return values;
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

  @Override
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
      value = Double.valueOf(tuple.get(attIndex).toString());
    } else if (ObviousWekaUtils.isString(c)) {
      value = Double.valueOf(attribute(attIndex).indexOfValue(
          tuple.getString(attIndex)));
    } else if (ObviousWekaUtils.isDate(c)) {
      value = tuple.getDate(attIndex).getTime();
    }
    return value;
  }

  @Override
  public double valueSparse(int indexOfIndex) {
    return value(indexOfIndex);
  }

}
