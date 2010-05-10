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

import java.util.Collection;

import obvious.ObviousException;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.MissingValue;
import obvious.util.Adaptable;

/**
 * Interface Table.
 *
 * @author obvious
 * @version $Revision$
 */
public interface Table extends Data, Adaptable {
    /**
     * Missing value.
     */
    MissingValue MISSING_VALUE = MissingValue.getInstance();

    /**
     * Returns this Table's schema.
     * @return a copy of this Table's schema
     */
    Schema getSchema();

    /**
     * Get the number of rows in the table.
     * @return the number of rows
     */
    int getRowCount();

    /**
     * Gets an iterator over the row numbers of this table.
     * @return an iterator over the rows of this table
     */
    IntIterator rowIterator();

    /**
     * Indicates if the given row number corresponds to a valid table row.
     * @param rowId row index
     * @return true if the row is valid, false if it is not
     */
    boolean isValidRow(int rowId);
//    /**
//     * Get the minimum row index currently in use by this Table.
//     *
//     * @return the minimum row index
//     */
//    public int getMinimumRow();
//
//    /**
//     * Get the maximum row index currently in use by this Table.
//     * @return the maximum row index
//     */
//    public int getMaximumRow();
//    /**
//     * Get the Tuple instance providing object-oriented access to the given
//     * table row.
//     * @param rowId the table row
//     * @return the Tuple for the given table row
//     */
//    public Tuple getTuple(int rowId);
//
//    /**
//     * Indicates if this table contains the given Tuple instance.
//     * @param t the Tuple to check for containment
//     * @return true if the Tuple represents a row of this table, false if
//     * it does not
//     * @see prefuse.data.tuple.TupleSet#containsTuple(prefuse.data.Tuple)
//     */
//    public boolean containsTuple(Tuple t);

    /**
     * Gets a specific value for a couple row/column.
     * @param rowId row index
     * @param field column name
     * @return value for this couple
     */
    Object getValue(int rowId, String field);

    /**
     * Gets a specific value row/column.
     * @param rowId row index
     * @param col column index
     * @return value for this couple
     */
    Object getValue(int rowId, int col);

    /**
     * Indicates if a given value is correct.
     * @param rowId row index
     * @param col column index
     * @return true if the coordinates are valid
     */
    boolean isValueValid(int rowId, int col);

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
    void beginEdit(int col) throws ObviousException;

    /**
     * Indicates the end of a column edit.
     * <p>
     * This function indicates, if notifications were disabled, that now they
     * are enabled. It could also call a mechanism to replay the sequence of
     * ignored events if wanted.
     * </p>
     * @param col column index
     * @throws ObviousException if edition is not supported.
     */
    void endEdit(int col) throws ObviousException;

    /**
     * Indicates if a column is being edited.
     * @param col column index
     * @return true if edited
     */
    boolean isEditing(int col);

    /**
     * Adds a table listener.
     * @param listnr an obvious table listener
     */
    void addTableListener(TableListener listnr);

    /**
     * Removes a table listener.
     * @param listnr an obvious table listener
     */
    void removeTableListener(TableListener listnr);

    /**
     * Gets all table listener.
     * @return a collection of table listeners.
     */
    Collection<TableListener> getTableListeners();

    //Mutable methods
    /**
     * Indicates if possible to add rows.
     * @return true if possible
     */
    boolean canAddRow(); //FIXME

    /**
     * Indicates if possible to remove rows.
     * @return true if possible
     */
    boolean canRemoveRow(); //FIXME

    /**
     * Adds a row filled with default values.
     * @return number of rows
     */
    int addRow();

    /**
     * Adds a row with the value of the tuple.
     * @param tuple tuple to insert in the table
     * @return number of rows
     */
    int addRow(Tuple tuple);

    /**
     * Removes a row.
     * @param row row index
     * @return true if done
     */
    boolean removeRow(int row);

    /**
     * Removes all the rows.
     *
     * <p>After this method, the table is almost in the same state as if
     * it had been created afresh except it contains the same columns as before
     * but they are all cleared.
     *
     */
    void removeAllRows();

    /**
     * Sets a value.
     * @param rowId row index
     * @param field field index
     * @param val value to set
     */
    void set(int rowId, String field, Object val);

    /**
     * Sets a value.
     * @param rowId row index
     * @param col column index
     * @param val value to set
     */
    void set(int rowId, int col, Object val);
}
