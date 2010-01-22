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

package test.obvious.jung;

import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.jung.data.JungObviousNetwork;
import test.obvious.data.NetworkTest;

import static org.junit.Assert.*;

/**
 * Implementation of  Network  test-case for JungObviousNetwork implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class JungNetworkTest extends NetworkTest {

  @Override
  public Network newInstance(Schema nSchema, Schema eSchema) {
    if (!eSchema.hasColumn("SRCNODE")) {
      eSchema.addColumn("SRCNODE", Integer.class, new Integer(0));
    }
    if  (!eSchema.hasColumn("DESTNODE")) {
      eSchema.addColumn("DESTNODE", Integer.class, new Integer(0));
    }
    return new JungObviousNetwork(nSchema, eSchema);
  }

  @Override
  public void testGetSource() {
    assertEquals(null, getNetwork().getSource(new EdgeImpl(edgeTable, 0)));
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_" + EDGENUMBER);
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, 0), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.DIRECTED);
    assertEquals(new NodeImpl(nodeTable, 0),
        getNetwork().getSource(new EdgeImpl(edgeTable, EDGENUMBER)));
  }

  @Override
  public void testGetTarget() {
    assertEquals(null, getNetwork().getTarget(new EdgeImpl(edgeTable, 0)));
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_" + EDGENUMBER);
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, 0), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.DIRECTED);
    assertEquals(new NodeImpl(nodeTable, NODENUMBER - 1),
        getNetwork().getTarget(new EdgeImpl(edgeTable, EDGENUMBER)));
  }

  @Override
  public void testGetPredecessors() {
    // first we will test for an undirected node. For an undirected node,
    // all linked node are predecessors.
    assertEquals(getNetwork().getNeighbors(new NodeImpl(nodeTable, 1))
        .size(), getNetwork().getPredecessors(new NodeImpl(nodeTable, 1))
        .size());
    // then we add two directed edges for a same node
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, NODENUMBER - 1), new NodeImpl(nodeTable, 0),
        EdgeType.DIRECTED);
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, NODENUMBER - 1), new NodeImpl(nodeTable, 1),
        EdgeType.DIRECTED);
    assertEquals(getNetwork().getNeighbors(new NodeImpl(nodeTable,
        NODENUMBER - 1)).size() - 2, getNetwork().getPredecessors(new NodeImpl(
            nodeTable, NODENUMBER - 1)).size());
  }

  @Override
  public void testGetSuccessors() {
    // first we will test for an undirected node. For an undirected node,
    // all linked node are successors.
    assertEquals(getNetwork().getNeighbors(new NodeImpl(nodeTable, 1))
        .size(), getNetwork().getPredecessors(new NodeImpl(nodeTable, 1))
        .size());
    // then we add two directed edges for a same node
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, 0), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.DIRECTED);
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, 1), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.DIRECTED);
    assertEquals(getNetwork().getNeighbors(new NodeImpl(nodeTable,
        NODENUMBER - 1)).size() - 2, getNetwork().getSuccessors(new NodeImpl(
            nodeTable, NODENUMBER - 1)).size());
  }

  @Override
  public void testGetInEdges() {
    // first we will test for an undirected node. For an undirected node,
    // all edges are in edges.
    assertEquals(getNetwork().getIncidentEdges(new NodeImpl(nodeTable, 1))
        .size(), getNetwork().getInEdges(new NodeImpl(nodeTable, 1))
        .size());
    // then we add two directed edges for a same node
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, NODENUMBER - 1), new NodeImpl(nodeTable, 0),
        EdgeType.DIRECTED);
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, NODENUMBER - 1), new NodeImpl(nodeTable, 1),
        EdgeType.DIRECTED);
    assertEquals(getNetwork().getIncidentEdges(new NodeImpl(nodeTable,
        NODENUMBER - 1)).size() - 2, getNetwork().getInEdges(new NodeImpl(
            nodeTable, NODENUMBER - 1)).size());
  }

  @Override
  public void testGetOutEdges() {
    // first we will test for an undirected node. For an undirected node,
    // all edges are out edges.
    assertEquals(getNetwork().getIncidentEdges(new NodeImpl(nodeTable, 1))
        .size(), getNetwork().getOutEdges(new NodeImpl(nodeTable, 1))
        .size());
    // then we add two directed edges for a same node
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, 0), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.DIRECTED);
    edgeTable.addRow();
    edgeTable.set(EDGENUMBER, "edgeName", "edge_"
        + new Integer(edgeTable.getRowCount() - 1));
    getNetwork().addEdge(new EdgeImpl(edgeTable, edgeTable.getRowCount() - 1),
        new NodeImpl(nodeTable, 1), new NodeImpl(nodeTable, NODENUMBER - 1),
        EdgeType.DIRECTED);
    assertEquals(getNetwork().getIncidentEdges(new NodeImpl(nodeTable,
        NODENUMBER - 1)).size() - 2, getNetwork().getOutEdges(new NodeImpl(
            nodeTable, NODENUMBER - 1)).size());
  }
}
