/*
* Copyright (c) 2009, INRIA
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

package obvious.data;

import obvious.data.Table;


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
