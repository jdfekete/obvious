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

package obvious.data.event;

import obvious.data.Table;

/**
 * Listener interface for monitoring changes to a table.

 * There are three different scenarios:
 * <ol>
 * <li>When the table structure is changed
 * (e.g. when a column is created or deleted);
 * <li>When some table metadata is changed
 * (e.g. a column name changed);
 * <li>When some data has changed (e.g. a
 * row has been modified, added or deleted).
 * </ol>
 * <pre>
 * //  The data, ie. all rows changed:
 * tableChanged(source, 0, source.getRowCount()-1, ALL_COLUMN, UPDATE);
 * // Structure change, reallocate TableColumns:
 * tableChanged(source, -1, -1, ALL_COLUMN, UPDATE);
 * // Metadata for column 3 has changed:
 * tableChanged(source, -1, -1, 3, UPDATE);
 * //  Row 1 changed:
 * tableChanged(source, 1, 1, ALL_COLUMN, UPDATE);
 * //  Rows 3 to 6 inclusive changed:
 * tableChanged(source, 3, 6, ALL_COLUMN, UPDATE);
 * //  Cell at (2, 6) changed:
 * tableChanged(source, 2, 2, 6, UPDATE);
 *  // Rows (3, 6) were inserted:
 * tableChanged(source, 3, 6, ALL_COLUMNS, INSERT);
 * // Rows (3, 6) were deleted:
 * tableChanged(source, 3, 6, ALL_COLUMNS, DELETE);
 * </pre>
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface TableListener {
    /**
     * Constant.
     */
    int ALL_COLUMN = -1;

    /**
     * Indicates a data delete operation.
     */
    int DELETE = -1;

    /**
     * Indicates a data update operation.
     */
    int UPDATE = 0;

    /**
     * Indicates a data insert operation.
     */
    int INSERT = 1;

    /**
     * Specify that the following calls to tableChanged belong to the same
     * transaction.
     * <p>
     * This method could be used when a large number of modification
     * are made on a Table and notifying everything would be time expansive.
     * For instance, it could disable notifications. The behavior of this
     * method clearly depends of the purpose of the Obvious implementation.
     * </p>
     * @param context an integer used, if needed, to identify the edition
     * context of the edit.
     *
     */
    void beginEdit(int context);

    /**
     * Specify that the calls to tableChanged belonging to the same transaction
     * are finished.
     * <p>
     * This function could be used to re-enabled notifications for the current
     * TableListener if they were disabled. It could also replay a stored
     * context of the period where the notifications where disabled. The
     * behavior of this method clearly depends of the purpose of the Obvious
     * implementation.
     * </p>
     * @param context an integer used, if needed, to retrieve the edition
     * context It could be used to execute further operations on the table
     * (for instance replaying sequence of events).
     */
    void endEdit(int context);

    /**
     * Notification that a table has changed.
     * @param t the table that has changed
     * @param start the starting row index of the changed table region
     * @param end the ending row index of the changed table region
     * @param col the column that has changed, or
     * {@link EventConstants#ALL_COLUMNS} if the operation affects all
     * columns
     * @param type the type of modification
     */
    void tableChanged(Table t, int start, int end, int col, int type);

} // end of interface TableListener
