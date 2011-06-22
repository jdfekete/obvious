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
#ifndef __obviousTable_hpp
#define __obviousTable_hpp

#include <iterator>
#include <string>
#include <vector>

namespace obvious
{

  class Schema;
  class TableListener;
  class Tuple;
  class Table;

  class RowIterator : public std::iterator<std::input_iterator_tag,int>
  {
  private:
    int row;
    const Table * table;
  public:
    RowIterator(int r, const Table * t) : row(r), table(t) {}
    RowIterator(const RowIterator& other) : row(other.row), table(other.table) {}
    RowIterator& operator++();
    bool operator==(const RowIterator& rhs) const { return row == rhs.row; }
    bool operator!=(const RowIterator& rhs) const { return row != rhs.row; }
    int& operator*() { return row; }
  };

  class Table
  {
  public:
    Table();
    /**
     * Returns this Table's schema.
     * @return a copy of this Table's schema
     */
    virtual Schema* getSchema() const = 0;

    /**
     * Get the number of rows in the table.
     * @return the number of rows
     */
    virtual int getRowCount() const = 0;

    /**
     * Gets an iterator over the row id of this table.
     * @return an iterator over the rows of this table
     */
    virtual RowIterator beginRow() const = 0;
    virtual RowIterator endRow() const = 0;
    virtual int nextRow(int row) const = 0;

    /**
     * Indicates if the given row number corresponds to a valid table row.
     * @param rowId row index
     * @return true if the row is valid, false if it is not
     */
    virtual bool isValidRow(int rowId) const = 0;

    /**
     * Gets a specific value for a couple row/column.
     * @param rowId row index
     * @param field column name
     * @return value for this couple
     */
    virtual const std::string getValue(int rowId, const std::string& field) = 0;

    /**
     * Gets a specific value row/column.
     * @param rowId row index
     * @param col column index
     * @return value for this couple
     */
    virtual const std::string getValue(int rowId, int col) = 0;

    /**
     * Indicates if a given value is correct.
     * @param rowId row index
     * @param col column index
     * @return true if the coordinates are valid
     */
    virtual bool isValueValid(int rowId, int col) = 0;

    /**
     * Indicates the beginning of a column edit.
     * <p>
     * This function could be used to create a context when a large number
     * of modifications happens to a same column to avoid time wasting with
     * plenty of notifications. In this context, TableListeners could ignore
     * notifications if wanted.
     * </p>
     * @param col column index
     * @throws ObviousException if edition is not supported.
     */
    virtual void beginEdit(int col) = 0; // throws ObviousException;

    /**
     * Indicates the end of a column edit.
     * <p>
     * This function indicates, if notifications were disabled, that now they
     * are enabled. It could also call a mechanism to replay the sequence of
     * ignored events if wanted.
     * </p>
     * @param col column index
     * @return true if transaction succeed
     * @throws ObviousException if edition is not supported.
     */
    virtual bool endEdit(int col) = 0; // throws ObviousException;

    /**
     * Indicates if a column is being edited.
     * @param col column index
     * @return true if edited
     */
    virtual bool isEditing(int col) = 0;

    /**
     * Adds a table listener.
     * @param listnr an obvious table listener
     */
    virtual void addTableListener(TableListener * listnr) = 0;

    /**
     * Removes a table listener.
     * @param listnr an obvious table listener
     */
    virtual void removeTableListener(TableListener * listnr) = 0;

    /**
     * Gets all table listener.
     * @return a collection of table listeners.
     */
    virtual std::vector<TableListener*> getTableListeners() const = 0;

    //Mutable methods
    /**
     * Indicates if possible to add rows.
     * @return true if possible
     */
    virtual bool canAddRow() const = 0;

    /**
     * Indicates if possible to remove rows.
     * @return true if possible
     */
    virtual bool canRemoveRow() const = 0;

    /**
     * Adds a row filled with default values.
     * @return number of rows
     */
    virtual int addRow() = 0;

    /**
     * Adds a row with the value of the tuple.
     * @param tuple tuple to insert in the table
     * @return number of rows
     */
    virtual int addRow(Tuple * tuple) = 0;

    /**
     * Removes a row.
     * @param row row index
     * @return true if done
     */
    virtual bool removeRow(int row) = 0;

    /**
     * Removes all the rows.
     *
     * <p>After this method, the table is almost in the same state as if
     * it had been created afresh except it contains the same columns as before
     * but they are all cleared.
     *
     */
    virtual void removeAllRows() = 0;

    /**
     * Sets a value.
     * @param rowId row index
     * @param field field index
     * @param val value to set
     */
    virtual void set(int rowId, const std::string& field, const std::string& val) = 0;

    /**
     * Sets a value.
     * @param rowId row index
     * @param col column index
     * @param val value to set
     */
    virtual void set(int rowId, int col, const std::string& val) = 0;

    /**
     * Notifies changes to listener.
     * @param start the starting row index of the changed table region
     * @param end the ending row index of the changed table region
     * @param col the column that has changed
     * @param type the type of modification
     */
    virtual void fireTableEvent(int start, int end, int col, int type) = 0;
  };

  inline RowIterator& RowIterator::operator++() {
    row = table->nextRow(row);
    return *this;
  }

} // end namespace obvious

#endif
