package obvious.impl;

import obvious.ObviousRuntimeException;
import obvious.data.Table;
import obvious.data.event.TableListener;

/**
 * This listener has been written to allow two identical obvious table
 * using different implementations to be synchronized. Both tables should
 * add a listener to the other to listen. This is largely used in visualization
 * and view package to synchronize data model and visualization / view that
 * does not used the same obvious implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class ObviousLinkListener implements TableListener {

  /**
   * The number of time beginEdit has been called minus the number of time
   * endEdit has been called.
   */
  private int inhibitNotify = 0;

  /**
   * Source table.
   */
  private Table source;

  /**
   * Target table.
   */
  private Table target;

  /**
   * Constructor.
   * @param targetTable table to update
   */
  public ObviousLinkListener(Table targetTable) {
    this.target = targetTable;
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
  }

  /**
   * Specifies that the calls to tableChanged belonging to the same transaction
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
  public void endEdit(int context) {
    inhibitNotify--;
    if (inhibitNotify <= 0) {
        inhibitNotify = 0;
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
    source = t;
    System.out.println("CHANGE detected");
    if (inhibitNotify != 0) {
      return;
    } else if (type == TableListener.DELETE) {
      delete(target, start, end, col);
    } else if (type == TableListener.UPDATE) {
      update(target, start, end, col);
    } else if (type == TableListener.INSERT) {
      insert(target, start, end, col);
    } else {
      return;
    }
  }

  /**
   * Deletes row in the table associated to the source Table.
   * @param table an obvious table
   * @param start first deleted row
   * @param end last deleted row
   * @param col modified column
   */
  protected void delete(Table table, int start, int end, int col) {
    if (col != TableListener.ALL_COLUMN || inhibitNotify != 0) {
      return;
    }
    try {
      table.beginEdit(col);
      for (int i = start; i <= end; i++) {
        if (table.isValidRow(i)) {
          table.removeRow(i);
        }
      }
      table.endEdit(col);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Updates row in the table associated to the source Table.
   * @param table an obvious table
   * @param start first updated row
   * @param end last updated row
   * @param col modified column(s)
   */
  protected void update(Table table, int start, int end, int col) {
    if (inhibitNotify != 0) {
      return;
    }
    try {
      table.beginEdit(col);
      if (col != TableListener.ALL_COLUMN) {
        for (int i = start; i <= end; i++) {
          if (table.isValidRow(i) && source.isValidRow(i)) {
            table.set(i, col, source.getValue(i, col));
          }
        }
      } else {
        for (int i = start; i <= end; i++) {
          if (table.isValidRow(i) && source.isValidRow(i)) {
            for (int j = 0; j < source.getSchema().getColumnCount(); j++) {
              table.set(i, j, source.getValue(i, j));
            }
          }
        }
      }
      table.endEdit(col);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }

  /**
   * Inserts row in the table associated to the source Table.
   * @param table an obvious table
   * @param start first inserted row
   * @param end last inserted row
   * @param col modified column
   */
  protected void insert(Table table, int start, int end, int col) {
    if (col != TableListener.ALL_COLUMN || inhibitNotify != 0) {
      return;
    }
    try {
      table.beginEdit(col);
      for (int i = start; i <= end; i++) {
        if (source.isValidRow(i)) {
          table.addRow(new TupleImpl(source, i));
        }
      }
      table.endEdit(col);
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
  }
}
