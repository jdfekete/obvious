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

import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

/**
 * Class GraphObjectTest.
 *
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */

public class GraphObjectTest {

  /**
   * Graph to test.
   */
  private Graph<String, String> graph;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() {
  }

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    graph = null;
  }

  /**
   * Test method for obvious.data.Graph.addNode(V) method.
   */
  @Test
  public void testAddNode() {
    assertTrue(graph.addNode("homer"));
    assertTrue(graph.addNode("marge"));
  }

  /**
   * Test method for obvious.data.Graph.addEdge(E) method.
   */
  @Test
  public void testAddEdge() {
    graph.addNode("a");
    graph.addNode("b");
    graph.addNode("c");
    assertTrue(graph.addEdge("ab-dir", "a", "b", Graph.EdgeType.DIRECTED));
    assertTrue(graph.addEdge("bc-undir", "b", "c", Graph.EdgeType.UNDIRECTED));
  }

  /**
   * Test method for obvious.data.Graph.removeNode(V) method.
   */
  @Test
  public void testRemoveNode() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addNode("abraham");
    graph.addEdge("homer-marge-undir",
         "homer", "marge", Graph.EdgeType.UNDIRECTED);
    graph.addEdge("abraham-homer-undir",
         "abraham", "homer", Graph.EdgeType.UNDIRECTED);
    assertTrue(graph.removeNode("abraham"));
    assertFalse(graph.getNodes().contains("abraham"));
    assertFalse(graph.getEdges().contains("abraham-homer-undir"));
  }

  /**
   * Test method for obvious.data.Graph.removeEdge(E) method.
   */
  @Test
  public void testRemoveEdge() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addNode("abraham");
    graph.addEdge("homer-marge-undir",
        "homer", "marge", Graph.EdgeType.UNDIRECTED);
    graph.addEdge("abraham-homer-undir",
        "abraham", "homer", Graph.EdgeType.UNDIRECTED);
    assertTrue(graph.removeEdge("abraham-homer-undir"));
    assertFalse(graph.getEdges().contains("abraham-homer-undir"));
  }

  /**
   * Test method for obvious.data.Graph.getSource(V) method.
   */
  @Test
  public void testGetSource() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addNode("abraham");
    graph.addEdge("homer-marge-dir", "homer", "marge", Graph.EdgeType.DIRECTED);
    graph.addEdge("abraham-homer-dir",
        "abraham", "homer", Graph.EdgeType.DIRECTED);
    assertEquals("abraham", graph.getSource("homer"));
  }

  /**
   * Test method for obvious.data.Graph.getTarget(V) method.
   */
  @Test
  public void testGetTarget() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addNode("abraham");
    graph.addEdge("homer-marge-dir", "homer", "marge", Graph.EdgeType.DIRECTED);
    graph.addEdge("abraham-homer-dir", "abraham",
        "homer", Graph.EdgeType.DIRECTED);
    assertEquals("marge", graph.getTarget("homer"));
  }

  /**
   * Test method for obvious.data.Graph.getIncidentNodes(V) method.
   */
  @Test
  public void testGetIncidentNodes() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addNode("abraham");
    graph.addEdge("homer-marge-undir",
        "homer", "marge", Graph.EdgeType.UNDIRECTED);
    graph.addEdge("abraham-homer-undir",
        "abraham", "homer", Graph.EdgeType.UNDIRECTED);
    assertTrue(graph.getIncidentNodes("homer").contains("marge"));
    assertTrue(graph.getIncidentNodes("homer").contains("abraham"));
  }

  /**
   * Test method for obvious.data.Graph.getIncidentEdges(V) method.
   */
  @Test
  public void testGetIncidentEdges() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addNode("abraham");
    graph.addEdge("homer-marge-undir",
        "homer", "marge", Graph.EdgeType.UNDIRECTED);
    graph.addEdge("abraham-homer-undir",
        "abraham", "homer", Graph.EdgeType.UNDIRECTED);
    assertTrue(graph.getIncidentEdges("homer").contains("abraham-homer-undir"));
    assertTrue(graph.getIncidentNodes("homer").contains("homer-marge-undir"));
  }

  /**
   * Test method for obvious.data.Graph.getNeighbors(V) method.
   */
  @Test
  public void testGetNeighbors() {
  }

  /**
   * Test method for obvious.getOpposite(V,E) method.
   */
  @Test
  public void testGetOpposite() {
    graph.addNode("homer");
    graph.addNode("marge");
    graph.addEdge("homer-marge-undir",
        "homer", "marge", Graph.EdgeType.UNDIRECTED);
    assertTrue(graph.getOpposite("homer", "homer-marge-undir").equals("marge"));
  }

}
