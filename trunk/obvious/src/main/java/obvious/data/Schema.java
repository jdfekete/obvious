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


/**
 * Interface Schema.
 *
 * @author obvious
 * @version $Revision$
 */
public interface Schema extends Table {
    /**
     * Metadata title for name.
     */
    String NAME = "Name";
    /**
     * Metadata title for type.
     */
    String TYPE = "Type";
    /**
     * Metadata title for default value.
     */
    String DEFAULT_VALUE = "DefaultValue";
    /**
     * Metadata title for categories.
     */
    String CATEGORIES = "Categories";
    /**
     * Metadata title for scale.
     */
    String SCALE = "Scale"; //nominal, ordinal, interval, ratio
    /**
     * Metadata title for format.
     */
    String FORMAT = "Format";
    /**
     * Metadata title for locale.
     */
    String LOCALE = "Locale";
    /**
     * Metadata title for uniq.
     */
    String UNIQ = "Uniq";
    /**
     * Metadata title for has null.
     */
    String HAS_NULL = "HasNull";
//    String UNIT = "Unit";
//    String HIDDEN = "Hidden";
    //TODO

    /**
     * Gets the number of columns / data fields in this table.
     * @return the number of columns
     */
    int getColumnCount();

    /**
     * Gets the data type of the column at the given column index.
     * @param col the column index
     * @return the data type (as a Java Class) of the column
     */
    Class<?> getColumnType(int col);

    /**
     * Gets the default value for a column.
     * @param col spotted
     * @return value default
     */
    Object getColumnDefault(int col);

    /**
     * Checks if the getValue method can return values that are compatibles
     * with a given type.
     * @param col Index of the column
     * @param c Expected type to check
     * @return true if the types are compatibles
     */
    boolean canGet(int col, Class<?> c);

    /**
     * Checks if the set method can accept for a specific column values that
     * are compatible with a given type.
     * @param col Index of the column
     * @param c Expected type to check
     * @return true if the types compatibles
     */
    boolean canSet(int col, Class<?> c);

    /**
     * Checks if the getValue method can return values that are compatibles
     * with a given type.
     * @param field Name of the column
     * @param c Expected type to check
     * @return true if the types are compatibles
     */
    boolean canGet(String field, Class<?> c);

    /**
     * Checks if the set method can accept for a specific column values that
     * are compatible with a given type.
     * @param field Index of the column
     * @param c Expected type to check
     * @return true if the types compatibles
     */
    boolean canSet(String field, Class<?> c);

    /**
     * Get the data type of the column with the given data field name.
     * @param field the column / data field name
     * @return the data type (as a Java Class) of the column
     */
    Class<?> getColumnType(String field);

    /**
     * Internal method indicating if the given data field is included as a
     * data column.
     * @param name name to seek
     * @return true if the name exists.
     */
    boolean hasColumn(String name);

    /**
     * Gets the column name.
     * @param col spotted
     * @return name of the column
     */
    String getColumnName(int col);

    /**
     * Get the column number for a given data field name.
     * @param field the name of the column to lookup
     * @return the column number of the column, or -1 if the name is not found
     */
    int getColumnIndex(String field);

    /**
     * Add a column with the given name and data type to this table.
     * @param name the data field name for the column
     * @param type the data type, as a Java Class, for the column
     * @param defaultValue the default value for column data values
     * @return the column index
     * throws a runtime exception when the column name already exists.
     */
    int addColumn(String name, Class<?> type, Object defaultValue);

//    /**
//     * Add a derived column to this table, using an Expression instance to
//     * dynamically calculate the column data values.
//     * @param name the data field name for the column
//     * @param expr the Expression that will determine the column values
//     */
//    public void addColumn(String name, Expression expr);

    /**
     * Removes a column.
     * @param field name of column to remove
     * @return true if removed
     */
    boolean removeColumn(String field);

    /**
     * Removes a column.
     * @param col spotted
     * @return true if removed
     */
    boolean removeColumn(int col);

}
