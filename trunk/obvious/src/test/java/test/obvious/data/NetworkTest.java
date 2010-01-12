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

import java.util.Collection;
import java.util.Iterator;

import obvious.ObviousException;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Graph.EdgeType;


import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class GraphObjectTest.
 * Override test methods that have specific needs in your implementation.
 * @author Pierre-Luc Hemery
 * @version $Revision$
 */

public abstract class NetworkTest {

  /**
   * Network instance used for tests.
   */
  private Network network;

  /**
   * Number of nodes of the network test instance.
   */
  private static final int NUMNODE = 2;

  /**
   * Gets the network test instance.
   * @return network test instance
   */
  public Network getNetwork() {
    return this.network;
  }

  /**
   * @see junit.framework.TestCase#setUp()
   * @throws ObviousException when problem occurs.
   */
  @Before
  public void setUp() throws ObviousException {
    this.network = this.newInstance(NUMNODE);
  }

  /**
   * Creates a suitable instance of network.
   * This Network should at least have one edge and two nodes.
   * @param numNode number of nodes of the graph.
   * @return suitable network implementation instance
   * @throws ObviousException if Network instantiation fails
   */
  public abstract Network newInstance(int numNode) throws ObviousException;

  /**
   * Creates a new instance of Node.
   * This method should be used to build Node instances that match your
   * Obvious implementation when you need to introduce node in your test.
   * It must generate a node that is not already in the network.
   * @return an Obvious Node
   */
  public abstract Node newNode();

  /**
   * Creates a new instance of Edge.
   * This method should be used to build Node instances that match your
   * Obvious implementation when you need to introduce edge in your test.
   * It must generate an edge that is not already in the network.
   * @return an Obvious Edge
   */
  public abstract Edge newEdge();

  /**
   * Test method for obvious.data.Network.addNode(Node) method.
   */
  @Test
  public void testAddNode() {
    Node addableNode = this.newNode();
    assertTrue(this.network.addNode(addableNode));
  }

  /**
   * Test method for obvious.data.Network.removeNode(Node) method.
   */
  @Test
  public void testRemoveNode() {
    Node externalNode = this.newNode();
    Node internalNode = this.network.getNodes().iterator().next();
    assertFalse(this.network.removeNode(externalNode));
    assertTrue(this.network.removeNode(internalNode));
    }

  /**
   * Test method for obvious.data.Network.addEdge(Edge,Node,Node) method.
   */
  @Test
  public void testAddEge() {
    Edge addableEdge = this.newEdge();
    Node[] node = new Node[2];
    Iterator<Node> it = this.network.getNodes().iterator();
    int count = 0;
    while (it.hasNext()) {
      if (count < 2) {
        node[count] = it.next();
      }
      count++;
    }
    int numEdge = this.network.getEdges().size();
    assertTrue(this.network.addEdge(addableEdge, node[0], node[1],
        EdgeType.UNDIRECTED));
    assertEquals(numEdge + 1, this.network.getEdges().size());
  }

  /**
   * Test method for obvious.data.Network.removeEdge(Edge) method.
   */
  @Test
  public void testRemoveEdge() {
    Edge externalEdge = this.newEdge();
    Edge internalEdge = this.network.getEdges().iterator().next();
    assertFalse(this.network.removeEdge(externalEdge));
    assertTrue(this.network.removeEdge(internalEdge));
  }

  /**
   * Test method for obvious.data.Network.getConnectingEdge(Node, Node).
   */
  @Test
  public void testGetConnectingEdge() {
    Node node1 = this.newNode();
    this.network.addNode(node1);
    Node node2 = this.newNode();
    this.network.addNode(node2);
    Edge edge = this.newEdge();
    this.network.addEdge(edge, node1, node2, EdgeType.DIRECTED);
    assertEquals(edge, this.network.getConnectingEdge(node1, node2));
  }

  /**
   * Test method for obvious.data.Network.getConnectingEdges(Node, Node).
   */
  @Test
  public void testGetConnectingEdges() {
    Node node1 = this.newNode();
    this.network.addNode(node1);
    Node node2 = this.newNode();
    this.network.addNode(node2);
    Edge edge1 = this.newEdge();
    this.network.addEdge(edge1, node1, node2, EdgeType.DIRECTED);
    Edge edge2 = this.newEdge();
    this.network.addEdge(edge2, node1, node2, EdgeType.DIRECTED);
    Collection<Edge> conEdge = this.network.getConnectingEdges(node1, node2);
    assertEquals(2, conEdge.size());
  }

  /**
   * Test method for obvious.data.Network.getOpposite(Node, Edge) method.
   */
  @Test
  public void testGetOpposite() {
    Node node1 = this.newNode();
    this.network.addNode(node1);
    Node node2 = this.newNode();
    this.network.addNode(node2);
    Edge edge = this.newEdge();
    this.network.addEdge(edge, node1, node2, EdgeType.DIRECTED);
    assertEquals(node1, this.network.getOpposite(node2, edge));
    assertEquals(node2, this.network.getOpposite(node1, edge));
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @After
  public void tearDown() {
    this.network = null;
  }

}
