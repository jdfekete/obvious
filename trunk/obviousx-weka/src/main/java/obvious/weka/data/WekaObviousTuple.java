package obvious.weka.data;

import java.util.Date;
import java.util.Enumeration;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.impl.SchemaImpl;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class WekaObviousTuple implements Tuple {

  /**
   * Weka Instance instance.
   */
  private Instance instance;
  
  /**
   * Obvious schema.
   */
  private Schema schema;

  /**
   * Constructor
   * @param instance wrapped weka Instance
   */
  @SuppressWarnings("unchecked")
  public WekaObviousTuple(Instance instance) {
    this.instance = instance;
    this.schema = new SchemaImpl();
    Enumeration attributes = instance.enumerateAttributes();
    while (attributes.hasMoreElements()) {
      Attribute att = (Attribute) attributes.nextElement();
      Class<?> c = checkClass(att);
      schema.addColumn(att.name(), c, null);
    }
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
  public boolean canGet(String field, Class<?> type) {
    if (!schema.hasColumn(field)) {
      return false;
    }
    return schema.getColumnType(field).equals(type);
  }

  @Override
  public boolean canGetBoolean(String field) {
    return canGet(field, Boolean.class);
  }

  @Override
  public boolean canGetDate(String field) {
    return canGet(field, String.class);
  }

  @Override
  public boolean canGetDouble(String field) {
    return canGet(field, Double.class);
  }

  @Override
  public boolean canGetFloat(String field) {
    return canGet(field, Float.class);
  }

  @Override
  public boolean canGetInt(String field) {
    return canGet(field, Integer.class);
  }

  @Override
  public boolean canGetLong(String field) {
    return canGet(field, Long.class);
  }

  @Override
  public boolean canGetString(String field) {
    return canGet(field, String.class);
  }

  @Override
  public boolean canSet(String field, Class<?> type) {
    return canGet(field, type);
  }

  @Override
  public boolean canSetBoolean(String field) {
    return canSet(field, Boolean.class);
  }

  @Override
  public boolean canSetDate(String field) {
    return canSet(field, Date.class);
  }

  @Override
  public boolean canSetDouble(String field) {
    return canSet(field, Double.class);
  }

  @Override
  public boolean canSetFloat(String field) {
    return canSet(field, Float.class);
  }

  @Override
  public boolean canSetInt(String field) {
    return canSet(field, Integer.class);
  }

  @Override
  public boolean canSetLong(String field) {
    return canSet(field, Long.class);
  }

  @Override
  public boolean canSetString(String field) {
    return canSet(field, String.class);
  }

  @Override
  public Object get(String field) {
    return get(schema.getColumnIndex(field));
  }

  @Override
  public Object get(int col) {
    return getTable().getValue(getRow(), col);
  }

  @Override
  public boolean getBoolean(String field) {
    return getBoolean(schema.getColumnIndex(field));
  }

  @Override
  public boolean getBoolean(int col) {
    if (canGetBoolean(schema.getColumnName(col))) {
       return (Boolean) get(col);
    }
    return (Boolean) null;
  }

  @Override
  public Class<?> getColumnType(String field) {
    return schema.getColumnType(field);
  }

  @Override
  public Date getDate(String field) {
    return getDate(schema.getColumnIndex(field));
  }

  @Override
  public Date getDate(int col) {
    if (canGetDate(schema.getColumnName(col))) {
      return (Date) get(col);
   }
   return null;
  }

  @Override
  public Object getDefault(String field) {
    return schema.getColumnDefault(schema.getColumnIndex(field));
  }

  @Override
  public double getDouble(String field) {
    return getDouble(schema.getColumnIndex(field));
  }

  @Override
  public double getDouble(int col) {
    if (canGetDouble(schema.getColumnName(col))) {
      return (Double) get(col);
   }
   return (Double) null;
  }

  @Override
  public float getFloat(String field) {
    return getFloat(schema.getColumnIndex(field));
  }

  @Override
  public float getFloat(int col) {
    if (canGetFloat(schema.getColumnName(col))) {
      return (Float) get(col);
   }
   return (Float) null;
  }

  @Override
  public int getInt(String field) {
    return getInt(schema.getColumnIndex(field));
  }

  @Override
  public int getInt(int col) {
    if (canGetInt(schema.getColumnName(col))) {
      return (Integer) get(col);
   }
   return (Integer) null;
  }

  @Override
  public long getLong(String field) {
    return getLong(schema.getColumnIndex(field));
  }

  @Override
  public long getLong(int col) {
    if (canGetLong(schema.getColumnName(col))) {
      return (Long) get(col);
   }
   return (Long) null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public int getRow() {
    Instances instances = instance.dataset();
    Enumeration instEnum = instances.enumerateInstances();
    int count = -1;
    while (instEnum.hasMoreElements()) {
      count++;
      if (instance.equals(instEnum.nextElement())) {
        return count;
      }
    }
    return -1;
  }

  @Override
  public Schema getSchema() {
    return this.schema;
  }

  @Override
  public String getString(String field) {
    return getString(schema.getColumnIndex(field));
  }

  @Override
  public String getString(int col) {
    if (canGetString(schema.getColumnName(col))) {
      return (String) get(col);
   }
   return (String) null;
  }

  @Override
  public Table getTable() {
    return new WekaObviousTable(instance.dataset());
  }

  @Override
  public boolean isValid() {
    return instance != null;
  }

  @Override
  public void revertToDefault(String field) {
    return; 
  }

  @Override
  public void set(String field, Object value) {
    set(schema.getColumnIndex(field), value); 
  }

  @Override
  public void set(int col, Object value) {
    getTable().set(getRow(), col, value);
  }

  @Override
  public void setBoolean(String field, boolean val) {
    setBoolean(schema.getColumnIndex(field), val); 
  }

  @Override
  public void setBoolean(int col, boolean val) {
    if (canSetBoolean(schema.getColumnName(col))) {
      set(col, val);
    }
  }

  @Override
  public void setDate(String field, Date val) {
    setDate(schema.getColumnIndex(field), val); 
  }

  @Override
  public void setDate(int col, Date val) {
    if (canSetDate(schema.getColumnName(col))) {
      set(col, val);
    }
  }

  @Override
  public void setDouble(String field, double val) {
    setDouble(schema.getColumnIndex(field), val); 
  }

  @Override
  public void setDouble(int col, double val) {
    if (canSetDouble(schema.getColumnName(col))) {
      set(col, val);
    }
  }

  @Override
  public void setFloat(String field, float val) {
    setFloat(schema.getColumnIndex(field), val);  
  }

  @Override
  public void setFloat(int col, float val) {
    if (canSetFloat(schema.getColumnName(col))) {
      set(col, val);
    }
  }

  @Override
  public void setInt(String field, int val) {
    setInt(schema.getColumnIndex(field), val); 
  }

  @Override
  public void setInt(int col, int val) {
    if (canSetInt(schema.getColumnName(col))) {
      set(col, val);
    }
  }

  @Override
  public void setLong(String field, long val) {
    setLong(schema.getColumnIndex(field), val);  
  }

  @Override
  public void setLong(int col, long val) {
    if (canSetLong(schema.getColumnName(col))) {
      set(col, val);
    }
  }

  @Override
  public void setString(String field, String val) {
    setString(schema.getColumnIndex(field), val); 
  }

  @Override
  public void setString(int col, String val) {
    if (canSetString(schema.getColumnName(col))) {
      set(col, val);
    }
  }

}
