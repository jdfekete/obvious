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

package test.obvious.data;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.SchemaImpl;
import obvious.impl.TupleImpl;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class TableTest.
 *
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */

public abstract class TableTest implements TableTestData {

  /**
   * Table instance to test.
   */
  private Table table;

  /**
   * Gets the table test instance.
   * @return test table instance
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * Sets the table test instance.
   * @param inTable table to set
   */
  public void setTable(Table inTable) {
    this.table = null;
  }


  /**
  * @see junit.framework.TestCase#setUp()
  * @throws ObviousException when problem occurs
  */
  @Before
  public void setUp() throws ObviousException {
    SchemaImpl  schema = new SchemaImpl();
    for (int i = 0; i < NUMCOL; i++) {
      schema.addColumn(HEADERS[i], TYPES[i], DEFAULTS[i]);
    }
    table = this.newInstance(schema);
    table.addRow(new TupleImpl(schema, ROW1));
    table.addRow(new TupleImpl(schema, ROW2));
    table.addRow(new TupleImpl(schema, ROW3));
    table.addRow(new TupleImpl(schema, ROW4));
  }

  /**
   * Creates a suitable instance of table.
   * @param schema based schema to create the table
   * @return suitable Table implementation instance
   * @throws ObviousException if Table instantiation fails
   */
  public abstract Table newInstance(Schema schema) throws ObviousException;

  /**
  * @see junit.framework.TestCase#tearDown()
  */
  @After
  public void tearDown() {
    table = null;
  }

  /**
   * Test method for obvious.data.Table.getRowCount() method.
   */
  @Test
  public void testGetRowCount() {
    assertEquals(NUMROW, table.getRowCount());
  }

  /**
  * Test method for obvious.data.Table.isValidRow() method.
  */
  @Test
  public void testIsValidRow() {
    final int falseIndex = 555;
    assertTrue(table.isValidRow(0));
    assertTrue(table.isValidRow(2));
    assertFalse(table.isValidRow(falseIndex));
  }

  /**
  * Test method for obvious.data.Table.isValueValid() method.
  */
  @Test
  public void isValueValid() {
    final int falseIndex = 4;
    assertTrue(table.isValueValid(0, 0));
    assertFalse(table.isValueValid(falseIndex, falseIndex));
  }

  /**
  * Test method for obvious.data.Table.getValue() method.
  */
  @Test
  public void testGetValue() {
    final int falseIndex = 3;
    assertEquals("Hello", table.getValue(1, 0));
    assertEquals("Hello", table.getValue(1, "col1"));
    assertEquals(1, table.getValue(0, 1));
    assertEquals(1, table.getValue(0, "col2"));
    assertEquals(true, table.getValue(falseIndex, 2));
    assertEquals(true, table.getValue(falseIndex, "col3"));
  }

// Read-write test

  /**
  * Test method for obvious.data.Table.addRow() method.
  */
  @Test
  public void testAddRow() {
    Object[] defaultValue = new Object[NUMCOL];
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      defaultValue[i] = table.getSchema().getColumnDefault(i);
    }
    table.addRow(new TupleImpl(table.getSchema(), defaultValue));
    assertEquals(NUMROW + 1, table.getRowCount());
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      System.out.println(table.getValue(table.getRowCount() - 1, i) + " : " +
          table.getValue(table.getRowCount() - 1, i).getClass() + " : " +
          table.getSchema().getColumnDefault(i).getClass());
      assertEquals(table.getValue(table.getRowCount() - 1, i),
          table.getSchema().getColumnDefault(i));
    }
  }


//Read-write test

  /**
  * Test method for obvious.data.Table.removeRow() method.
  */
  @Test
  public void testRemoveRow() {
    table.removeRow(0);
    System.out.println(table.isValidRow(0));
    assertFalse(table.isValidRow(0));
    assertEquals(NUMROW - 1, table.getRowCount());
  }

  /**
  * Test method for obvious.data.Table.removeAllRows() method.
  */
  @Test
  public void removeAllRows() {
    table.removeAllRows();
    assertEquals(0, table.getRowCount());
  }

  /**
  * Test method for obvious.data.Table.set() method.
  */
  @Test
  public void testSet() {
    table.set(0, 0, "hey");
    assertEquals("hey", table.getValue(0, 0));
  }

  /**
   * Test method for obvious.data.Table.canRemoveRow().
   */
  @Test
  public void testCanRemoveRow() {
  }

  /**
   * Test method for obvious.data.Table.canRemoveRow()
   * At the moment, not enough information about the method to write unit test.
   */
  @Test
  public void testCanAddRow() {
  }

  /**
   * Test method for obvious.data.Table.rowIterator().
   */
  @Test
  public void testRowIterator() {
    for (int i = 0; i < table.getRowCount(); i++) {
      assertTrue(table.rowIterator().hasNext());
    }
  }


  /**
  * Test method for testing isolation i.e.
  * obvious.data.Table.beginEdit(),isEditing() and endEdit()
  */
  /*
  @Test
  public void testIsolation() {
    //TableTestListener listener = new TableTestListener();
    for (int i = 0; i < NUMROW; i++) {
      assertFalse(table.isEditing(i));
    }
    try {
      table.beginEdit(0);
      assertTrue(table.isEditing(0));
      table.endEdit(0);
    } catch (ObviousException e) {
        // Edit not supported
      return;
    }
//    finally {
//    }
  }
*/
}
