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

import obvious.data.Graph;
import obvious.data.Tree;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

/**
 * Class TreeTest.
 *
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */

public class TreeTest {

  /**
   * Tree to test.
   */
  private Tree<String, String> tree;
  /**
   * Height of the tree.
   */
  private static final int HEIGHT = 3;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
    tree.addNode("president");
    tree.addNode("prime_minister");
    tree.addNode("minister_one");
    tree.addNode("minister_two");
    tree.addNode("minister_three");
    tree.addNode("secretary_one");
    tree.addEdge("president-prime-dir",
        "president", "prime_minister", Graph.EdgeType.DIRECTED);
    tree.addEdge("prime-min1-dir",
        "prime_minister", "minister_one", Graph.EdgeType.DIRECTED);
    tree.addEdge("prime-min2-dir",
        "prime_minister", "minister_two", Graph.EdgeType.DIRECTED);
    tree.addEdge("prime-min3-dir",
        "prime_minister", "minister_three", Graph.EdgeType.DIRECTED);
    tree.addEdge("min1-sec-dir",
        "minister_one", "secretary_one", Graph.EdgeType.DIRECTED);
  }

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    tree = null;
  }

  /**
   * Test method for obvious.data.Tree.getRoot() method.
   */
  @Test
  public void testGetRoot() {
    assertEquals("president", tree.getRoot());
  }

  /**
   * Test method for obvious.data.Tree.getHeight() method.
   */
  @Test
  public void testGetHeight() {
    assertEquals(HEIGHT, tree.getHeight());
  }

  /**
   * Test method for obvious.data.Tree.getDepth(V) method.
   */
  @Test
  public void testGetDepth() {
    assertEquals(1, tree.getDepth("prime_minister"));
    assertEquals(2, tree.getDepth("minister_one"));
    assertEquals(HEIGHT, tree.getDepth("secratary_one"));
  }

  /**
   * Test method for obvious.data.Tree.getParentNode(V) method.
   */
  @Test
  public void testGetParentNode() {
    assertEquals("minister_one", tree.getParentNode("secretary_one"));
    assertEquals("prime_minister", tree.getParentNode("minister_one"));
    assertEquals("president", tree.getParentNode("prime_minister"));
  }

  /**
   * Test method for obvious.data.Tree.getParentEdge(V) method.
   */
  @Test
  public void testGetParentEdge() {
    assertEquals("min1-sec-dir", tree.getParentEdge("secretary_one"));
    assertEquals("prime-min1-dir", tree.getParentEdge("minister_one"));
    assertEquals("president-prime-dir", tree.getParentEdge("prime_minister"));
  }

}
