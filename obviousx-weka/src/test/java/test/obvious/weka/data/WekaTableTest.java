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

package test.obvious.weka.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.weka.data.WekaObviousTable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Since this binding is not based on an InfoVis toolkit, we do not adapt
 * generic tests for Obvious Table, since Weka tables only handle a limited
 * type of column types.
 * The method to create the tested table instance has been rewritten to set
 * up a convenient data set and other tests have modified to follow these
 * changes.
 * @author Hemery
 *
 */
public class WekaTableTest {

  /**
   * Table instance to test.
   */
  private Table table;
  
  /**
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
    Attribute id = new Attribute("id");
    FastVector boolColumn = new FastVector();
    boolColumn.addElement("no");
    boolColumn.addElement("yes");
    Attribute cls = new Attribute("check", boolColumn);
    FastVector attributes = new FastVector();
    attributes.addElement(id);
    attributes.addElement(cls);
    Instances dataset = new Instances("test", attributes, 10);
    dataset.add(new Instance(1.0, new double[] {0.0,
        dataset.attribute(1).indexOfValue("yes")}));
    dataset.add(new Instance(1.0, new double[] {1.0,
        dataset.attribute(1).indexOfValue("yes")}));
    dataset.add(new Instance(1.0, new double[] {2.0,
        dataset.attribute(1).indexOfValue("no")}));
    dataset.add(new Instance(1.0, new double[] {3.0,
        dataset.attribute(1).indexOfValue("yes")}));
    dataset.add(new Instance(1.0, new double[] {4.0,
        dataset.attribute(1).indexOfValue("no")}));
    dataset.add(new Instance(1.0, new double[] {5.0,
        dataset.attribute(1).indexOfValue("no")}));
    dataset.add(new Instance(1.0, new double[] {6.0,
        dataset.attribute(1).indexOfValue("yes")}));
    dataset.add(new Instance(1.0, new double[] {7.0,
        dataset.attribute(1).indexOfValue("no")}));
    table = new WekaObviousTable(dataset);
  }
  
  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    table = null;
  }

  /**
   * Test method for obvious.data.Table.isValidRow() method.
   */
  @Test
  public void testIsValidRow() {
    final int falseIndex = 555;
    assertTrue(table.isValidRow(0));
    assertTrue(table.isValidRow(1));
    assertTrue(table.isValidRow(3));
    assertTrue(table.isValidRow(2));
    assertFalse(table.isValidRow(falseIndex));
    assertFalse(table.isValidRow(-1));
  }

  /**
   * Test method for obvious.data.Table.getRowCount() method.
   */
  @Test
  public void testGetRowCount() {
    final int numRow = 8;
    assertEquals(numRow, table.getRowCount());
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
    assertEquals(0.0, table.getValue(0, 0));
    assertEquals(0.0, table.getValue(0, "id"));
    assertEquals("no", table.getValue(4, 1));
    assertEquals("no", table.getValue(4, "check"));
  }

  /**
   * Test method for obvious.data.Table.set() method.
   */
  @Test
  public void testSet() {
    table.set(4, 1, "yes");
    assertEquals("yes", table.getValue(4, "check"));
    table.set(5, "check", "yes");
    assertEquals("yes", table.getValue(4, "check"));
  }
  
  /**
   * Test method for obvious.data.Table.addRow() method.
   */
  @Test
  public void testAddRow() {
     table.addRow(new TupleImpl(table.getSchema(), new Object[] {8.0, "yes"}));
     assertEquals(9, table.getRowCount());
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
   * Test method for obvious.data.Table.removeRow() method.
   */
  @Test
  public void testRemoveRow() {
    table.removeRow(0);
    assertEquals(7, table.getRowCount());
  }
  
}
