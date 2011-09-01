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

package test.obvious.data;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import obvious.data.Edge;
import obvious.data.Forest;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;

/**
 * Class ForestTest.
 * Override test methods that have specific needs in your implementation.
 * @author Hemery
 * @version $Revision$
 */
public abstract class ForestTest {

  /**
   * Forest instance used for tests.
   */
  protected Forest<Node, Edge> forest;

  /**
   * Gets the Forest test instance.
   * @return the Forest test instance
   */
  public Forest<Node, Edge> getForest() {
    return this.forest;
  }

  /**
   * Creates a suitable instance of network.
   * @param nSchema schema for the nodes the network
   * @param eSchema schema for the edges of the network
   * @return suitable network implementation instance
   */
  public abstract Forest<Node, Edge> newInstance(Schema nSchema,
      Schema eSchema);

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
    // Creating the starting schema for node and edges
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("nodeName", String.class, "node_default");
    nodeSchema.addColumn("nodeId", int.class, -1);
    Schema edgeSchema = new SchemaImpl();
    edgeSchema.addColumn("edgeName", String.class, "edge_default");
    edgeSchema.addColumn("source", int.class, -1);
    edgeSchema.addColumn("target", int.class, -1);
    // Requesting a forest instance corresponding to these schemas.
    forest = newInstance(nodeSchema, edgeSchema);
    Map<String, Node> nodeMap = new HashMap<String, Node>();
    // Adding nodes
    for (int i = 0; i < 10; i++) {
      Node node = new NodeImpl(nodeSchema, new Object[] {"A" + i, i});
      nodeMap.put("A" + i, node);
      forest.addNode(node);
    }
    // Adding edges
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A0-A1", 0, 1}),
        nodeMap.get("A0"), nodeMap.get("A1"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A0-A2", 0, 2}),
        nodeMap.get("A0"), nodeMap.get("A2"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A1-A3", 1, 3}),
        nodeMap.get("A1"), nodeMap.get("A3"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A2-A4", 2, 4}),
        nodeMap.get("A2"), nodeMap.get("A4"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A2-A5", 2, 5}),
        nodeMap.get("A2"), nodeMap.get("A5"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A6-A7", 6, 7}),
        nodeMap.get("A6"), nodeMap.get("A7"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A6-A8", 6, 8}),
        nodeMap.get("A6"), nodeMap.get("A8"), EdgeType.DIRECTED);
    forest.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A8-A9", 8, 9}),
        nodeMap.get("A8"), nodeMap.get("A9"), EdgeType.DIRECTED);
  }

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    this.forest = null;
  }

  /**
   * Test method for obvious.data.Forest.getTrees() method.
   */
  @Test
  public void testGetTrees() {
    assertEquals(2, forest.getTrees().size());
    assertEquals(6, forest.getTrees().iterator().next().getNodes().size());
    assertEquals(5, forest.getTrees().iterator().next().getEdges().size());
  }

}
