/*****************************************************************************
 * Copyright (C) 2008 Jean-Daniel Fekete and INRIA, France                  *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license.txt file.                                                         *
 *****************************************************************************/
package obvious;



/**
 * Class Schema
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Schema extends Table {
    String NAME = "Name";
    String TYPE = "Type";
    String DEFAULT_VALUE = "DefaultValue";
    String CATEGORIES = "Categories";
    String SCALE = "Scale"; //nominal, ordinal, interval, ratio
    String FORMAT = "Format";
    String LOCALE = "Locale";
    String UNIQ = "Uniq";
    String HAS_NULL = "HasNull";
//    String UNIT = "Unit";
//    String HIDDEN = "Hidden";
    //TODO
    
    /**
     * Get the number of columns / data fields in this table.
     * @return the number of columns 
     */
    int getColumnCount();
    
    /**
     * Get the data type of the column at the given column index.
     * @param col the column index
     * @return the data type (as a Java Class) of the column
     */
    public Class getColumnType(int col);
    
    public Object getColumnDefault(int col);
    
    /**
     *TODO
     * @param col
     * @param type can be null
     * @return
     */
    public boolean canGet(int col, Class type);
    public boolean canSet(int col, Class type);
    
    /**
     * Get the data type of the column with the given data field name.
     * @param field the column / data field name
     * @return the data type (as a Java Class) of the column
     */
    public Class getColumnType(String field);
    
    /**
     * Internal method indicating if the given data field is included as a
     * data column.
     */
    boolean hasColumn(String name);
    
    public String getColumnName(int col);
    /**
     * Get the column number for a given data field name.
     * @param field the name of the column to lookup
     * @return the column number of the column, or -1 if the name is not found
     */
    public int getColumnIndex(String field);
    
    /**
     * Add a column with the given name and data type to this table.
     * @param name the data field name for the column
     * @param type the data type, as a Java Class, for the column
     * @param defaultValue the default value for column data values
     * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.Class, java.lang.Object)
     * @return the column index
     * @throws Exception when the column name already exists.
     */
    public int addColumn(String name, Class type, Object defaultValue);
    
//    /**
//     * Add a derived column to this table, using an Expression instance to
//     * dynamically calculate the column data values.
//     * @param name the data field name for the column
//     * @param expr the Expression that will determine the column values
//     * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, prefuse.data.expression.Expression)
//     */
//    public void addColumn(String name, Expression expr);
    
    public boolean removeColumn(String field);
    public boolean removeColumn(int col);
    
}
