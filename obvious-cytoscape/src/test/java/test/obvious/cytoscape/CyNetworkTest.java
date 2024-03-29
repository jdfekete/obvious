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

package test.obvious.cytoscape;

import static org.junit.Assert.assertEquals;

import org.cytoscape.event.CyEvent;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.event.CyListener;
import org.cytoscape.model.internal.CyNetworkFactoryImpl;
import org.junit.Test;

import obvious.cytoscape.CyNetwork;
import obvious.data.Network;
import obvious.data.Schema;
import test.obvious.data.NetworkTest;

/**
 * Implementation of Obvious NetworkTest testcase for Cytoscape implementation.
 * @author Pierre-Luc
 *
 */
public class CyNetworkTest extends NetworkTest {

  @Override
  public Network newInstance(Schema nSchema, Schema eSchema) {
    return new CyNetwork(new CyNetworkFactoryImpl(new DummyEventHelper()));
  }

  /**
   * Test method for obvious.data.Network.getNodes() method.
   */
  @Test
  public void testGetEdges() {
    assertEquals(EDGENUMBER, this.network.getEdges().size());
  }

  /**
   * Test method for obvious.data.Network.getNodes() method.
   */
  @Test
  public void testGetNodes() {
    assertEquals(NODENUMBER, this.network.getNodes().size());
  }

  /**
   * A simple Cytoscape event helper.
   * @author Obvious group
   *
   */
  @SuppressWarnings("unchecked")
  private static class DummyEventHelper implements CyEventHelper {
    public <E extends CyEvent, L extends CyListener> void fireSynchronousEvent(
        final E event, final Class<L> listener) { }
    public <E extends CyEvent, L extends CyListener> void fireAsynchronousEvent(
        final E event, final Class<L> listener) { }
  }
}
