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

import obvious.data.Schema;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Schema Interface.
 * @author Pierre-Luc Hemery
 *
 */
public abstract class SchemaTest {

  /**
   * Schema instance for tests.
   */
  private Schema schema;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
      this.schema = this.newInstance();
      schema.addColumn("col1", String.class, "");
      schema.addColumn("col2", Integer.class, 0);
  }

  /**
   * Creates a suitable instance of schema.
   * @return suitable Schema implementation instance
   */
  public abstract Schema newInstance();

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    schema = null;
  }

  /**
   * Test method for obvious.data.Schema.getColumnCount() method.
   */
  @Test
  public void testGetColumnCount() {
    assertEquals(2, schema.getColumnCount());
  }

  /**
   * Test method for obvious.data.Schema.getColumnType(int col) method.
   */
  @Test
  public void testGetColumnTypeByIndex() {
    assertEquals(String.class, schema.getColumnType(0));
    assertEquals(Integer.class, schema.getColumnType(1));
  }

  /**
   * Test method for obvious.data.Schema.getColumnDefault(string field) method.
   */
  @Test
  public void testGetColumnTypeByField() {
    assertEquals(String.class, schema.getColumnType("col1"));
    assertEquals(Integer.class, schema.getColumnType("col2"));
  }

  /**
   * Test method for obvious.data.Schema.getColumnDefault(int col) method.
   */
  @Test
  public void testGetColumnDefault() {
    assertEquals("", schema.getColumnDefault(0));
    assertEquals(0, schema.getColumnDefault(1));
  }

  /**
   * Test method for obvious.data.Schema.getColumnName(int col) method.
   */
  @Test
  public void testGetColumnName() {
    assertEquals("col1", schema.getColumnName(0));
    assertEquals("col2", schema.getColumnName(1));
  }

  /**
   * Test method for obvious.data.Schema.getColumnIndex(String field) method.
   */
  @Test
  public void testGetColumnIndex() {
    assertEquals(0, schema.getColumnIndex("col1"));
    assertEquals(1, schema.getColumnIndex("col2"));
  }

  /**
   * Test method for obvious.data.Schema.hasColumn(string field) method.
   */
  public void testHasColumn() {
    assertTrue(schema.hasColumn("col1"));
    assertTrue(schema.hasColumn("col2"));
    assertFalse(schema.hasColumn("foooo"));
  }

  /**
   * Test method for obvious.data.Schema.removeColumn(int col) method.
   */
  @Test
  public void testRemoveColumnByIndex() {
    final int falseIndex = 3;
    assertTrue(schema.removeColumn(0));
    assertTrue(schema.removeColumn(0));
    assertFalse(schema.removeColumn(falseIndex));
  }

  /**
   * Test method for obvious.data.Schema.removeColumn(String field) method.
   */
  public void testRemoveColumnByField() {
    assertTrue(schema.removeColumn("col1"));
    assertTrue(schema.removeColumn("col2"));
    assertFalse(schema.removeColumn("foo"));
  }
}
