/*
* Copyright (c) 20011, INRIA
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
#ifndef __obviousSchema_hpp
#define __obviousSchema_hpp

namespace obvious
{

/**
 * class Schema.
 *
 * @author obvious
 * @version $Revision$
 */
  class Schema : public Table
  {
  public:
    /**
     * Metadata title for name.
     */
    static const std::string NAME = "Name";
    /**
     * Metadata title for type.
     */
    static const std::string TYPE = "Type";
    /**
     * Metadata title for default value.
     */
    static const std::string DEFAULT_VALUE = "DefaultValue";
    /**
     * Metadata title for categories.
     */
    static const std::string CATEGORIES = "Categories";
    /**
     * Metadata title for scale.
     */
    static const std::string SCALE = "Scale"; //nominal, ordinal, interval, ratio
    /**
     * Metadata title for format.
     */
    static const std::string FORMAT = "Format";
    /**
     * Metadata title for locale.
     */
    static const std::string LOCALE = "Locale";
    /**
     * Metadata title for uniq.
     */
    static const std::string UNIQ = "Uniq";
    /**
     * Metadata title for has null.
     */
    static const std::string HAS_NULL = "HasNull";

    /**
     * Gets the number of columns / data fields in this table.
     * @return the number of columns
     */
    virtual int getColumnCount() = 0;

    /**
     * Gets the data type of the column at the given column index.
     * @param col the column index
     * @return the data type (as a Java Class) of the column
     */
    virtual Type getColumnType(int col) = 0;

    /**
     * Gets the default value for a column.
     * @param col column index
     * @return default value for the specified column
     */
    Object getColumnDefault(int col);

    /**
     * Checks if the getValue method can return values that are compatibles
     * with a given type.
     * @param col column index
     * @param c type to check
     * @return true if types are compatibles
     */
    boolean canGet(int col, Class<?> c);

    /**
     * Checks if the set method can accept for a specific column values that
     * are compatible with a given type.
     * @param col column index
     * @param c type to check
     * @return true if the types compatibles
     */
    boolean canSet(int col, Class<?> c);

    /**
     * Checks if the getValue method can return values that are compatibles
     * with a given type.
     * @param field column name
     * @param c type to check
     * @return true if the types are compatibles
     */
    boolean canGet(static const std::string field, Class<?> c);

    /**
     * Checks if the set method can accept for a specific column values that
     * are compatible with a given type.
     * @param field column name
     * @param c type to check
     * @return true if the types compatibles
     */
    boolean canSet(static const std::string field, Class<?> c);

    /**
     * Get the data type of the column with the given data field name.
     * @param field the column name
     * @return the data type (as a Java Class) of the column
     */
    Class<?> getColumnType(static const std::string field);

    /**
     * Internal method indicating if the given data field is included as a
     * data column.
     * @param name name to seek
     * @return true if the name exists.
     */
    boolean hasColumn(static const std::string name);

    /**
     * Gets the column name.
     * @param col column index
     * @return name of the column
     */
    static const std::string getColumnName(int col);

    /**
     * Get the column number for a given data field name.
     * @param field the name of the column to lookup
     * @return the column number of the column, or -1 if the name is not found
     */
    int getColumnIndex(static const std::string field);

    /**
     * Add a column with the given name and data type to this schema.
     * It throws a runtime exception when the column name already exists.
     * @param name name of the column
     * @param type the data type, as a Java Class, for the column
     * @param defaultValue the default value for the column
     * @return the column index
     */
    int addColumn(static const std::string name, Class<?> type, Object defaultValue);

//    /**
//     * Add a derived column to this table, using an Expression instance to
//     * dynamically calculate the column data values.
//     * @param name the data field name for the column
//     * @param expr the Expression that will determine the column values
//     */
//    public void addColumn(static const std::string name, Expression expr);

    /**
     * Removes a column.
     * @param field name of column to remove
     * @return true if removed
     */
    boolean removeColumn(static const std::string field);

    /**
     * Removes a column.
     * @param col column index
     * @return true if removed
     */
    boolean removeColumn(int col);

    /**
     * Gets the corresponding schema without internal columns.
     * @return a schema only composed by data columns
     */
    Schema getDataSchema();

};

#endif
