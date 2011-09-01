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

import obvious.ObviousException;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;
import obvious.impl.TableImpl;
import obvious.impl.TupleImpl;


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
  protected Network network;

  /**
   * Number of nodes of the test network.
   */
  public static final int NODENUMBER = 6;

  /**
   * Number of edges of the test network.
   */
  public static final int EDGENUMBER = 6;

  /**
   * Node table used to build the example tree.
   */
  protected Table nodeTable;

  /**
   * Edge table uses to build the example tree.
   */
  protected Table edgeTable;

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
    // Creating the starting schema for node and edges
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("nodeName", String.class, "node_default");
    Schema edgeSchema = new SchemaImpl();
    edgeSchema.addColumn("edgeName", String.class, "edge_default");
    // Requesting a network instance corresponding to these schemas.
    this.network = this.newInstance(nodeSchema, edgeSchema);
    // Creating the table that will feed the network.
    nodeTable = new TableImpl(nodeSchema);
    edgeTable = new TableImpl(edgeSchema);
    // Adding nodes to the network
    for (int i = 0; i < NODENUMBER; i++) {
      Object[] nodeValue = new Object[nodeTable.getSchema().getColumnCount()];
      for (int j = 0; j < nodeTable.getSchema().getColumnCount(); j++) {
        if (nodeTable.getSchema().getColumnName(j).equals("nodeName")) {
          nodeValue[j] = "node_" + i;
        } else {
          nodeValue[j] = nodeTable.getSchema().getColumnDefault(j);
        }
      }
      nodeTable.addRow(new TupleImpl(nodeTable.getSchema(), nodeValue));
      this.network.addNode(new NodeImpl(nodeTable, i));
    }
    // Adding edges to the network
    for (int i = 0; i < EDGENUMBER; i++) {
      Object[] edgeValue = new Object[edgeTable.getSchema().getColumnCount()];
      for (int j = 0; j < edgeTable.getSchema().getColumnCount(); j++) {
        if (edgeTable.getSchema().getColumnName(j).equals("edgeName")) {
          edgeValue[j] = "edge_" + i;
        } else {
          edgeValue[j] = nodeTable.getSchema().getColumnDefault(j);
        }
      }
      edgeTable.addRow(new TupleImpl(edgeTable.getSchema(), edgeValue));
    }
    network.addEdge(new EdgeImpl(edgeTable, 0), new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 1), EdgeType.UNDIRECTED);
    network.addEdge(new EdgeImpl(edgeTable, 1), new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 2), EdgeType.UNDIRECTED);
    network.addEdge(new EdgeImpl(edgeTable, 2), new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 3), EdgeType.UNDIRECTED);
    network.addEdge(new EdgeImpl(edgeTable,3), new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 4), EdgeType.UNDIRECTED);
    network.addEdge(new EdgeImpl(edgeTable,4), new NodeImpl(nodeTable, 3),
        new NodeImpl(nodeTable, 4), EdgeType.UNDIRECTED);
    network.addEdge(new EdgeImpl(edgeTable,5), new NodeImpl(nodeTable, 4),
        new NodeImpl(nodeTable, 5), EdgeType.UNDIRECTED);
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @After
  public void tearDown() {
    this.network = null;
    this.edgeTable = null;
    this.nodeTable = null;
  }

  /**
   * Creates a suitable instance of network.
   * @param nSchema schema for the nodes the network
   * @param eSchema schema for the edges of the network
   * @return suitable network implementation instance
   */
  public abstract Network newInstance(Schema nSchema, Schema eSchema);

  /**
   * Test method for obvious.data.Network.getNodes() method.
   */
  @Test
  public void testGetEdges() {
    assertEquals(EDGENUMBER, this.network.getEdges().size());
    int count = 0;
    for (Edge edge : this.network.getEdges()) {
      assertEquals("edge_" + count, edge.getString("edgeName"));
      count++;
    }
  }

  /**
   * Test method for obvious.data.Network.getNodes() method.
   */
  @Test
  public void testGetNodes() {
    assertEquals(NODENUMBER, this.network.getNodes().size());
    int count = 0;
    for (Node node : this.network.getNodes()) {
      assertEquals("node_" + count, node.getString("nodeName"));
      count++;
    }
  }

  /**
   * Test method for obvious.data.Network.addNode(Node) method.
   */
  @Test
  public void testAddNode() {
    Object[] nodeValue = new Object[nodeTable.getSchema().getColumnCount()];
    for (int i = 0; i < nodeTable.getSchema().getColumnCount(); i++) {
      if (nodeTable.getSchema().getColumnName(i).equals("nodeName")) {
        nodeValue[i] = "node_" + NODENUMBER;
      } else {
        nodeValue[i] = nodeTable.getSchema().getColumnDefault(i);
      }
    }
    nodeTable.addRow(new TupleImpl(nodeTable.getSchema(), nodeValue));
    assertTrue(this.network.addNode(new NodeImpl(nodeTable, NODENUMBER)));
    assertEquals(NODENUMBER + 1, this.network.getNodes().size());
  }

  /**
   * Test method for obvious.data.Network.removeNode(Node) method.
   */
  @Test
  public void testRemoveNode() {
    Object[] nodeValue = new Object[nodeTable.getSchema().getColumnCount()];
    for (int i = 0; i < nodeTable.getSchema().getColumnCount(); i++) {
      if (nodeTable.getSchema().getColumnName(i).equals("nodeName")) {
        nodeValue[i] = "node_" + NODENUMBER;
      } else {
        nodeValue[i] = nodeTable.getSchema().getColumnDefault(i);
      }
    }
    nodeTable.addRow(new TupleImpl(nodeTable.getSchema(), nodeValue));
    // node not in the graph
    assertFalse(network.removeNode(new NodeImpl(nodeTable, NODENUMBER)));
    // node in the graph
    assertTrue(network.removeNode(new NodeImpl(nodeTable, NODENUMBER - 1)));
    assertEquals(NODENUMBER - 1, this.network.getNodes().size());
  }

  /**
   * Test method for obvious.data.Network.addEdge(Edge,Node,Node) method.
   */
  @Test
  public void testAddEge() {
    Object[] edgeValue = new Object[edgeTable.getSchema().getColumnCount()];
    for (int j = 0; j < edgeTable.getSchema().getColumnCount(); j++) {
      if (edgeTable.getSchema().getColumnName(j).equals("edgeName")) {
        edgeValue[j] = "edge_" + EDGENUMBER;
      } else {
        edgeValue[j] = nodeTable.getSchema().getColumnDefault(j);
      }
    }
    edgeTable.addRow(new TupleImpl(edgeTable.getSchema(), edgeValue));
    assertTrue(network.addEdge(new EdgeImpl(edgeTable, EDGENUMBER),
        new NodeImpl(nodeTable, 0), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.UNDIRECTED));
    assertEquals(EDGENUMBER + 1, this.network.getEdges().size());
  }

  /**
   * Test method for obvious.data.Network.removeEdge(Edge) method.
   */
  @Test
  public void testRemoveEdge() {
    Object[] edgeValue = new Object[edgeTable.getSchema().getColumnCount()];
    for (int j = 0; j < edgeTable.getSchema().getColumnCount(); j++) {
      if (edgeTable.getSchema().getColumnName(j).equals("edgeName")) {
        edgeValue[j] = "edge_" + EDGENUMBER;
      } else {
        edgeValue[j] = nodeTable.getSchema().getColumnDefault(j);
      }
    }
    edgeTable.addRow(new TupleImpl(edgeTable.getSchema(), edgeValue));
    // Edge not in the graph
    assertFalse(network.removeEdge(new EdgeImpl(edgeTable, EDGENUMBER)));
    // Edge in the graph
    assertTrue(network.removeEdge(new EdgeImpl(edgeTable, EDGENUMBER - 1)));
  }

  /**
   * Test method for obvious.data.Network.getConnectingEdge(Node, Node).
   */
  @Test
  public void testGetConnectingEdge() {
    // this method returns the first connecting edge between two nodes.
    // In this test, couple of nodes only have one edge between them. So,
    // we can predict, the edge returned by the method.
    assertEquals("edge_0", network.getConnectingEdge(new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 1)).get("edgeName"));
    assertEquals("edge_1", network.getConnectingEdge(new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 2)).get("edgeName"));
    assertEquals("edge_2", network.getConnectingEdge(new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 3)).get("edgeName"));
    assertEquals("edge_3", network.getConnectingEdge(new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 4)).get("edgeName"));
    assertEquals("edge_4", network.getConnectingEdge(new NodeImpl(nodeTable, 3),
        new NodeImpl(nodeTable, 4)).get("edgeName"));
    assertEquals("edge_5", network.getConnectingEdge(new NodeImpl(nodeTable, 4),
        new NodeImpl(nodeTable, 5)).get("edgeName"));

  }

  /**
   * Test method for obvious.data.Network.getConnectingEdges(Node, Node).
   */
  @Test
  public void testGetConnectingEdges() {
    // this method is useful for multigraphs. By default, our graph is not a
    // multigraph. So, first, this test add an edge to an existing linked nodes
    // couple to be able to test this method.
    Object[] edgeValue = new Object[edgeTable.getSchema().getColumnCount()];
    for (int j = 0; j < edgeTable.getSchema().getColumnCount(); j++) {
      if (edgeTable.getSchema().getColumnName(j).equals("edgeName")) {
        edgeValue[j] = "edge_" + EDGENUMBER;
      } else {
        edgeValue[j] = nodeTable.getSchema().getColumnDefault(j);
      }
    }
    edgeTable.addRow(new TupleImpl(edgeTable.getSchema(), edgeValue));
    network.addEdge(new EdgeImpl(edgeTable, EDGENUMBER),
        new NodeImpl(nodeTable, 1), new NodeImpl(nodeTable, 3),
        EdgeType.UNDIRECTED);
    // test begins here
    assertEquals(0, network.getConnectingEdges(new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, NODENUMBER - 1)).size());
    assertEquals(1, network.getConnectingEdges(new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 1)).size());
    assertEquals(1, network.getConnectingEdges(new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 0)).size());
    assertEquals(2, network.getConnectingEdges(new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 3)).size());
    Collection<Edge> conEdge = network.getConnectingEdges(new
        NodeImpl(nodeTable, 1), new NodeImpl(nodeTable, 3));
    for (Edge edge : conEdge) {
      assertTrue(edge.get("edgeName").equals("edge_2") || edge.get(
          "edgeName").equals("edge_" + EDGENUMBER));
    }
  }

  /**
   * Test method for obvious.data.Network.getOpposite(Node, Edge) method.
   */
  @Test
  public void testGetOpposite() {
    assertEquals("node_1", network.getOpposite(new NodeImpl(nodeTable, 0),
        new EdgeImpl(edgeTable, 0)).get("nodeName"));
    assertEquals("node_0", network.getOpposite(new NodeImpl(nodeTable, 1),
        new EdgeImpl(edgeTable, 0)).get("nodeName"));
  }

  /**
   * Test method for obvious.data.Network.getNeighbors(Node) method.
   */
  @Test
  public void testGetNeighbors() {
    assertEquals(2, network.getNeighbors(new NodeImpl(nodeTable, 0)).size());
    assertEquals(3, network.getNeighbors(new NodeImpl(nodeTable, 1)).size());
    assertEquals(1, network.getNeighbors(new NodeImpl(nodeTable, 2)).size());
    assertEquals(3, network.getNeighbors(new NodeImpl(nodeTable, 4)).size());
    Collection<Node> neighbr = network.getNeighbors(new NodeImpl(nodeTable, 0));
    for (Node node : neighbr) {
      String nodeName = node.getString("nodeName");
      assertTrue(nodeName.equals("node_1") || nodeName.equals("node_2"));
    }
  }

  /**
   * Test method for obvious.data.Network.getIncidentEdges(Node) method.
   */
  @Test
  public void testGetIncidentEdges() {
    assertEquals(2, network.getIncidentEdges(new NodeImpl(
        nodeTable, 0)).size());
    assertEquals(3, network.getIncidentEdges(new NodeImpl(
        nodeTable, 1)).size());
    assertEquals(1, network.getIncidentEdges(new NodeImpl(
        nodeTable, 2)).size());
    assertEquals(3, network.getIncidentEdges(new NodeImpl(
        nodeTable, 4)).size());
    Collection<Edge> incident = network.getIncidentEdges(
        new NodeImpl(nodeTable, 0));
    for (Edge edge : incident) {
      String edgeName = edge.getString("edgeName");
      assertTrue(edgeName.equals("edge_0") || edgeName.equals("edge_1"));
    }
  }

  /**
   * Test method for obvious.data.Network.getIncidentNodes(Edge) method.
   */
  @Test
  public void testGetIncidentNodes() {
    assertEquals(2, network.getIncidentNodes(new EdgeImpl(
        edgeTable, 0)).size());
    assertEquals(2, network.getIncidentNodes(new EdgeImpl(
        edgeTable, 1)).size());
    Collection<Node> incident = network.getIncidentNodes(
        new EdgeImpl(edgeTable, 0));
    for (Node node : incident) {
      String nodeName = node.getString("nodeName");
      //System.out.println(nodeName);
      assertTrue(nodeName.equals("node_0") || nodeName.equals("node_1"));
    }
  }

  /**
   * Test method for obvious.data.Network.getSource(Edge) method.
   */
  @Test
  public void testGetSource() {
  }

  /**
   * Test method for obvious.data.Network.getTarget(Edge) method.
   */
  @Test
  public void testGetTarget() {
  }

  /**
   * Test method for obvious.data.Network.getPredecessors(Node) method.
   */
  @Test
  public void testGetPredecessors() {
  }

  /**
   * Test method for obvious.data.Network.getSuccessors(Node) method.
   */
  @Test
  public void testGetSuccessors() {
  }

  /**
   * Test method for obvious.data.Network.getInEdges(Node) method.
   */
  @Test
  public void testGetInEdges() {
  }

  /**
   * Test method for obvious.data.Network.getOutEdges(Node) method.
   */
  @Test
  public void testGetOutEdges() {
  }

}
