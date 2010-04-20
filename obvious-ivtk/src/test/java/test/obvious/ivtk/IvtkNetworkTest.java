/*
* Copyright (c) 2010, INRIA
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


package test.obvious.ivtk;

import static org.junit.Assert.assertEquals;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousNetwork;
import test.obvious.data.NetworkTest;

/**
 * Implementation of the Network testcase for IvtkObviousNetwork implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkNetworkTest extends NetworkTest {

  @Override
  public Network newInstance(Schema nSchema, Schema eSchema) {
    return new IvtkObviousNetwork(nSchema, eSchema);
  }

  @Override
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
    this.getNetwork().addEdge(new EdgeImpl(edgeTable, EDGENUMBER),
        new NodeImpl(nodeTable, 1), new NodeImpl(nodeTable, 3),
        EdgeType.UNDIRECTED);
    // test begins here
    assertEquals(0, network.getConnectingEdges(new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, NODENUMBER - 1)).size());
    assertEquals(1, network.getConnectingEdges(new NodeImpl(nodeTable, 0),
        new NodeImpl(nodeTable, 1)).size());
    assertEquals(1, network.getConnectingEdges(new NodeImpl(nodeTable, 1),
        new NodeImpl(nodeTable, 0)).size());
  }

}
