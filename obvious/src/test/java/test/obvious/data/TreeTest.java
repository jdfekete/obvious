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

import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tree;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;
import obvious.impl.TableImpl;

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

public abstract class TreeTest {

  /**
   * Tree to test.
   */
  private Tree<Node, Edge> tree;

  /**
   * Height of the test tree.
   */
  private static final int HEIGHT = 3;

  /**
   * Number of nodes of the test tree.
   */
  private static final int NODENUMBER = 7;

  /**
   * Number of edges of the test tree.
   */
  private static final int EDGENUMBER = 5;

  /**
   * Node table used to build the example tree.
   */
  private Table nodeTable;

  /**
   * Edge table uses to build the example tree.
   */
  private Table edgeTable;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("nodeName", String.class, "node_default");
    Schema edgeSchema = new SchemaImpl();
    edgeSchema.addColumn("edgeName", String.class, "edge_default");
    nodeTable = new TableImpl(nodeSchema);
    edgeTable = new TableImpl(edgeSchema);
    this.tree = this.newInstance(nodeSchema, edgeSchema);
    for (int i = 0; i < NODENUMBER; i++) {
      nodeTable.addRow();
      nodeTable.set(i, 0, "node_" + String.valueOf(i));
      this.tree.addNode(new NodeImpl(nodeTable, i));
    }
    for (int i = 0; i < EDGENUMBER; i++) {
      edgeTable.addRow();
      edgeTable.set(i, "edgeName", "edge_" + String.valueOf(i));
    }
    tree.addEdge(new EdgeImpl(edgeTable, 0), new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 1), EdgeType.UNDIRECTED);
    tree.addEdge(new EdgeImpl(edgeTable, 1), new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 2), EdgeType.UNDIRECTED);
    tree.addEdge(new EdgeImpl(edgeTable, 2), new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 3), EdgeType.UNDIRECTED);
    tree.addEdge(new EdgeImpl(edgeTable,3), new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 4), EdgeType.UNDIRECTED);
    tree.addEdge(new EdgeImpl(edgeTable,4), new NodeImpl(nodeTable, 4),
        new NodeImpl(nodeTable, 5), EdgeType.UNDIRECTED);
  }

  /**
   * Creates a suitable instance of tree.
   * @param nSchema schema for the nodes the tree
   * @param eSchema schema for the edges of the tree
   * @return suitable tree implementation instance
   */
  public abstract Tree<Node, Edge> newInstance(Schema nSchema, Schema eSchema);

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() {
    this.tree = null;
    this.edgeTable = null;
    this.nodeTable = null;
  }

  /**
   * Gets the tree test instance.
   * @return tree test instance
   */
  public Tree<Node, Edge> getTree() {
    return this.tree;
  }

  /**
   * Test method for obvious.data.Tree.getRoot() method.
   */
  @Test
  public void testGetRoot() {
    assertEquals("node_0", this.tree.getRoot().getString("nodeName"));
  }

  /**
   * Test method for obvious.data.Tree.getHeight() method.
   */
  @Test
  public void testGetHeight() {
    assertEquals(HEIGHT, this.tree.getHeight());
  }

  /**
   * Test method for obvious.data.Tree.getChildNodes(Node) method.
   */
  @Test
  public void testGetChildNodes() {
    assertEquals(2, this.tree.getChildNodes(this.tree.getRoot()).size());
    assertEquals(2, this.tree.getChildNodes(new NodeImpl(nodeTable, 1)).size());
    assertEquals(0, this.tree.getChildNodes(new NodeImpl(nodeTable, 2)).size());
    assertEquals(1, this.tree.getChildNodes(new NodeImpl(nodeTable, 4)).size());
    Collection<Node> childNode = this.tree.getChildNodes(this.tree.getRoot());
    for (Node node : childNode) {
      String nodeName = node.getString("nodeName");
      assertTrue(nodeName.equals("node_1") || nodeName.equals("node_2"));
    }
  }

  /**
   * Test method for obvious.data.Tree.getChildEdges(Node) method.
   */
  @Test
  public void testGetChildEdges() {
    assertEquals(2, this.tree.getChildEdges(this.tree.getRoot()).size());
    assertEquals(2, this.tree.getChildEdges(new NodeImpl(nodeTable, 1)).size());
    assertEquals(0, this.tree.getChildEdges(new NodeImpl(nodeTable, 2)).size());
    assertEquals(1, this.tree.getChildEdges(new NodeImpl(nodeTable, 4)).size());
    Collection<Edge> childEdge = this.tree.getChildEdges(this.tree.getRoot());
    for (Edge edge : childEdge) {
      String edgeName = edge.getString("edgeName");
      assertTrue(edgeName.equals("edge_0") || edgeName.equals("edge_1"));
    }
  }

  /**
   * Test method for obvious.data.Tree.getDepth(V) method.
   */
  @Test
  public void testGetDepth() {
    assertEquals(0, this.tree.getDepth(this.tree.getRoot()));
    assertEquals(1, this.tree.getDepth(new NodeImpl(nodeTable, 1)));
    assertEquals(1, this.tree.getDepth(new NodeImpl(nodeTable, 2)));
    assertEquals(2, this.tree.getDepth(new NodeImpl(nodeTable, 3)));
    assertEquals(2, this.tree.getDepth(new NodeImpl(nodeTable, 4)));
    assertEquals(3, this.tree.getDepth(new NodeImpl(nodeTable, 5)));
  }

  /**
   * Test method for obvious.data.Tree.getParentNode(V) method.
   */
  @Test
  public void testGetParentNode() {
    assertEquals(null, tree.getParentNode(tree.getRoot()));
    assertEquals("node_0", tree.getParentNode(new NodeImpl(nodeTable, 1)).get(
        "nodeName"));
    assertEquals("node_0", tree.getParentNode(new NodeImpl(nodeTable, 2)).get(
        "nodeName"));
    assertEquals("node_1", tree.getParentNode(new NodeImpl(nodeTable, 3)).get(
        "nodeName"));
    assertEquals("node_1", tree.getParentNode(new NodeImpl(nodeTable, 4)).get(
        "nodeName"));
    assertEquals("node_4", tree.getParentNode(new NodeImpl(nodeTable, 5)).get(
        "nodeName"));
  }

  /**
   * Test method for obvious.data.Tree.getParentEdge(V) method.
   */
  @Test
  public void testGetParentEdge() {
    assertEquals("edge_0", tree.getParentEdge(new NodeImpl(nodeTable, 1)).get(
        "edgeName"));
    assertEquals("edge_1", tree.getParentEdge(new NodeImpl(nodeTable, 2)).get(
        "edgeName"));
    assertEquals("edge_2", tree.getParentEdge(new NodeImpl(nodeTable, 3)).get(
        "edgeName"));
    assertEquals("edge_3", tree.getParentEdge(new NodeImpl(nodeTable, 4)).get(
        "edgeName"));
    assertEquals("edge_4", tree.getParentEdge(new NodeImpl(nodeTable, 5)).get(
        "edgeName"));
  }

  /**
   * Test method for obvious.data.Tree.getTrees() method.
   */
  @Test
  public void testGetTrees() {
    assertEquals(this.tree, this.tree.getTrees().iterator().next());
  }

}
