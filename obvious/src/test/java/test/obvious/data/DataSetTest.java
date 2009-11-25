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

import obvious.data.DataSet;
import obvious.data.Data;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**.
 * Class DataSetTest
 *
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */

public class DataSetTest {

    /**
     * DatatSet to test.
     */
    private DataSet testSet;
    /**
     * Data for the dataset.
     */
    private Data alice, bob;

    /**
     * Wrapper class around String to implement Data interface.
     *
     */
    public final class DataString implements Data {
      /**
       * String data.
       */
      @SuppressWarnings("unused")
      private String data;

      /**
       * Constructor for DataString.
       * @param inputString String to wrap in the class.
       */
      private DataString(String inputString) {
        this.data = inputString;
      }

    }

  /**
   * @see junit.framework.TestCase#setUp().
   */
  @Before
  public void setUp() {
    alice = new DataString("Alice");
    bob = new DataString("Bob");
  }

  /**
   * @see junit.framework.TestCase#tearDown().
   */
  @After
  public void tearDown() {
    testSet = null;
    alice = null;
    bob = null;
  }

  /**
   * Test method for obvious.data.DataSet.set(String,Data) method.
   */
  @Test
  public void testSet() {
    testSet.set("first", alice);
    testSet.set("second", bob);
    assertEquals(alice, testSet.get("first"));
    assertEquals(bob, testSet.get("second"));
    assertEquals(alice, testSet.get("first", Data.class));
    assertEquals(bob, testSet.get("second", Data.class));
  }

}
