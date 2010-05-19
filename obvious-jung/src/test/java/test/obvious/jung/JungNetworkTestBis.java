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

package test.obvious.jung;

import static org.junit.Assert.assertEquals;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.jung.data.JungObviousNetwork;
import test.obvious.data.NetworkTest;

/**
 * Implementation of  Network  test-case for PrefuseObviousNetwork
 * implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class JungNetworkTestBis extends NetworkTest {

  /**
   * Implementation schema for edge.
   */
  private Schema customEdgeSchema;

  @Override
  public Network newInstance(Schema nodeSchema, Schema edgeSchema) {
    if (!edgeSchema.hasColumn("sourceKey")) {
      edgeSchema.addColumn("sourceKey", int.class, 0);
    }
    if  (!edgeSchema.hasColumn("targetKey")) {
      edgeSchema.addColumn("targetKey", int.class, 0);
    }
    this.customEdgeSchema = edgeSchema;
    return new JungObviousNetwork(nodeSchema,
        edgeSchema, "sourceKey", "targetKey");
  }

  @Override
  public void testGetOpposite() {
    assertEquals("node_1", network.getOpposite(new NodeImpl(nodeTable, 0),
        new EdgeImpl(customEdgeSchema,
            new Object[] {"edge0", 0, 1})).get("nodeName"));
    assertEquals("node_0", network.getOpposite(new NodeImpl(nodeTable, 1),
        new EdgeImpl(customEdgeSchema,
            new Object[] {"edge0", 0, 1})).get("nodeName"));
  }

  @Override
  public void testGetIncidentNodes() {
    assertEquals(2, network.getIncidentNodes(new EdgeImpl(customEdgeSchema,
        new Object[] {"edge0", 0, 1})).size());
    assertEquals(2, network.getIncidentNodes(new EdgeImpl(customEdgeSchema,
        new Object[] {"edge1", 0, 2})).size());
  }
}
