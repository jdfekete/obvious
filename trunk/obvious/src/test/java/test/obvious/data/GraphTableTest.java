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
import obvious.data.Network;
import obvious.data.DataFactory;
import obvious.impl.SchemaImpl;

import org.junit.Before;
import org.junit.After;

/**
 * Class GraphObjectTest.
 *
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */

public class GraphTableTest {

  /**
   * Network instance used for tests.
   */
  @SuppressWarnings("unused")
  private Network network;

  /**
   * @see junit.framework.TestCase#setUp()
   * @throws ObviousException when problem occurs.
   */
  @Before
  public void setUp() throws ObviousException {
    SchemaImpl nodeSchema = new SchemaImpl();
    SchemaImpl edgeSchema = new SchemaImpl();
    DataFactory factory = DataFactory.getInstance();
    network = factory.createGraph("myGraph", nodeSchema, edgeSchema);
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @After
  public void tearDown() {
    network = null;
  }

}
