package obvious.data.event;

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

import obvious.data.Network;

/**
 * A listener interface for network.
 * @author Hemery
 *
 */
public interface NetworkListener {

    /**
     * Constant.
     */
    int ALL_COLUMN = -1;

    /**
     * Indicates a data delete operation.
     */
    int DELETE_NODE = -1;

    /**
     * Indicates a data delete operation.
     */
    int DELETE_EDGE = -2;

    /**
     * Indicates a data update operation.
     */
    int UPDATE_NODE = 0;

    /**
     * Indicates a data update operation.
     */
    int UPDATE_EDGE = 1;

    /**
     * Indicates a data insert operation.
     */
    int INSERT_NODE = 2;

    /**
     * Indicates a data insert operation.
     */
    int INSERT_EDGE = 3;

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
     * Specify that the calls to networkChanged belonging to the same
     * transaction are finished.
     * <p>
     * This function could be used to re-enabled notifications for the current
     * TableListener if they were disabled. It could also replay a stored
     * context of the period where the notifications where disabled. The
     * behavior of this method clearly depends of the purpose of the Obvious
     * implementation.
     * </p>
     * @param context an integer used, if needed, to retrieve the edition
     * context It could be used to execute further operations on the table
     * (for instance replaying sequence of events)
     * @return true if transaction succeed
     */
    boolean endEdit(int context);

    /**
     * Checks if the table meets criteria defined by invariant(s). If no
     * invariant is defined for this structure, the methods has to return
     * true.
     * @return true if the invariant is checked
     */
    boolean checkInvariants();

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
    void networkChanged(Network t, int start, int end, int col, int type);

}
