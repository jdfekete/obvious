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

package obvious.demo.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import obvious.ObviousException;
import obvious.ObviousRuntimeException;
import obvious.impl.TupleImpl;
import obvious.jdbc.JDBCObviousTable;
import obvious.prefuse.PrefuseObviousSchema;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.event.TableListener;

/**
 * Transaction demo.
 * @author hemery
 *
 */
public final class TransactionDemo {

  /**
   * Constructor.
   */
  private TransactionDemo() { };

  /**
   * Main method.
   * @param args arguments of the main
   * @throws SQLException if something bad with the DB happens
   * @throws ObviousException if something bad happens
   */
  public static void main(String[] args) throws SQLException, ObviousException {
    Schema schema = new PrefuseObviousSchema();
    schema.addColumn("name", String.class, "Doe");
    schema.addColumn("firstName", String.class, "John");

    Table table = new JDBCObviousTable(
        schema, "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/test",
        "root", "", "person", "name");
    Connection con = (Connection) table.getUnderlyingImpl(
        java.sql.Connection.class);
    table.addTableListener(new TriggerListener(con));
    table.addRow(new TupleImpl(schema, new Object[] {"avantTransac", "Jan"}));
    table.beginEdit(0);
    for (int i = 0; i < 100; i++) {
      table.addRow(new TupleImpl(schema, new Object[] {
          String.valueOf(200 + i), "charles"}));
    }
    table.endEdit(0);
    table.addRow(new TupleImpl(schema, new Object[] {"apresTransac", "Jan"}));
  }

  /**
   * A listener.
   * @author Hemery
   *
   */
  public static class TriggerListener implements TableListener {

    /**
     * The number of time beginEdit has been called minus the number of time
     * endEdit has been called.
     */
    private int inhibitNotify = 0;

    /**
     * JDBC connection.
     */
    private Connection con;

    /**
     * Constructor.
     * @param inCon JDBC connection
     */
    public TriggerListener(Connection inCon) {
      this.con = inCon;
      initTrigger();
    }


    /**
     * Inits the trigger.
     */
    private void initTrigger() {
      PreparedStatement cleanTrigger = null;
      PreparedStatement setTrigger = null;
      try {
        cleanTrigger = con.prepareStatement("DROP TRIGGER IF EXISTS sinceDate");
        cleanTrigger.executeUpdate();
        setTrigger = con.prepareStatement(
            "CREATE TRIGGER sinceDate " +
            "BEFORE INSERT ON person " +
            "FOR EACH ROW " +
            "BEGIN  " +
            "SET NEW.SINCE = now(); " +
            "END;");
        setTrigger.executeUpdate();
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      } finally {
        try { cleanTrigger.close(); } catch (Exception e) {
          e.printStackTrace();
        }
        try { setTrigger.close(); } catch (Exception e) { e.printStackTrace(); }
      }
    }

    /**
     * Specifies that the following calls to tableChanged belong to the same
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
    public void beginEdit(int context) {
      inhibitNotify++;
      // Starting the transaction.
      try {
        con.setAutoCommit(false);
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      }
    }

    /**
     * Specifies that the calls to tableChanged belonging to the same
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
     * (for instance replaying sequence of events).
     */

    public void endEdit(int context) {
      inhibitNotify--;
      if (inhibitNotify <= 0) {
          inhibitNotify = 0;
      }
      try {
        if (!checkInvariant()) {
          con.rollback();
        }
        con.setAutoCommit(true);
      } catch (Exception e) {
        throw new ObviousRuntimeException(e);
      }
    }

    /**
     * Notifies that a table has changed.
     * @param t the table that has changed
     * @param start the starting row index of the changed table region
     * @param end the ending row index of the changed table region
     * @param col the column that has changed, or
     * {@link EventConstants#ALL_COLUMNS} if the operation affects all
     * columns
     * @param type the type of modification
     */
    public void tableChanged(Table t, int start, int end, int col, int type) {
      if (inhibitNotify != 0) {
        return;
      } else if (type == TableListener.DELETE) {

      } else if (type == TableListener.UPDATE) {

      } else if (type == TableListener.INSERT) {
      } else {
        return;
      }
    }

    /**
     * Checks invariant.
     * @return true if invariant are checked.
     */
    protected boolean checkInvariant() {
      PreparedStatement pStmt = null;
      ResultSet rslt = null;
      int minSize = 0;
      try {
        String request = "SELECT MIN( CHAR_LENGTH( NAME ) ) FROM person ";
        pStmt = con.prepareStatement(request);
        rslt = pStmt.executeQuery();
        while (rslt.next()) {
          minSize = rslt.getInt(1);
        }
      } catch (SQLException e) {
        throw new ObviousRuntimeException(e);
      } finally {
        try { pStmt.close(); } catch (Exception e) { e.printStackTrace(); }
        try { rslt.close(); } catch (Exception e) { e.printStackTrace(); }
      }
      return minSize < 2;
    }

  }
}
