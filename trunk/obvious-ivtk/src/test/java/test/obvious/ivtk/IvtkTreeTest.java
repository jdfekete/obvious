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

import org.junit.Test;

import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.ivtk.data.IvtkObviousTree;
import test.obvious.data.TreeTest;

/**
 * Implementation of the Tree testcase for IvtkObviousTree implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class IvtkTreeTest extends TreeTest {

  @Override
  public Tree<Node, Edge> newInstance(Schema nSchema, Schema eSchema) {
    return new IvtkObviousTree(nSchema, eSchema);
  }

  @Test
  public void testGetNodes() {
    assertEquals(6, this.getTree().getNodes().size());
    int count = 0;
    for (Node node : this.getTree().getNodes()) {
      assertEquals("node_" + count, node.getString("nodeName"));
      count++;
    }
  }

  @Test
  public void testGetEdges() {
    assertEquals(5, this.getTree().getEdges().size());
    int count = 0;
    System.out.println("SIZE " + getTree().getEdges().size());
    for (Edge edge : this.getTree().getEdges()) {
      System.out.println("edge_" + count + " : " + edge.getString("edgeName"));
      assertEquals("edge_" + count, edge.getString("edgeName"));
      count++;
    }
  }

}
