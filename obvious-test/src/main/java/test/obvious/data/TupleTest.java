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

import obvious.data.Tuple;

import org.junit.Before;
import org.junit.After;

/**
 * Test class for Tuple class.
 *
 * @author Pierre-Luc Hemery
 *
 */
public abstract class TupleTest {

  /**
   * Tuple to test.
   */
  @SuppressWarnings("unused")
  private Tuple tuple;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
    this.tuple = this.newInstance();
  }

  /**
   * Creates a suitable instance of tuple.
   * @return suitable tuple implementation instance
   */
  public abstract Tuple newInstance();

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    this.tuple = null;
  }
}
