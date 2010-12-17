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

package obviousx.wrappers.prefuse;

import obvious.data.Network;
import obvious.impl.TupleImpl;
import prefuse.data.Graph;
import prefuse.data.Node;

/**
 * Wrapper for obvious edge to pref edge.
 * @author Hemery
 *
 */
public class WrapToPrefEdge extends WrapToPrefTuple
    implements prefuse.data.Edge  {

  /**
   * Network containing the node.
   */
  private Network network;

  /**
   * Constructor.
   * @param inNetwork network containing the node
   * @param inTuple node to wrap
   * @param row index of the node
   */
  public WrapToPrefEdge(Network inNetwork, TupleImpl inTuple, int row) {
    super(inTuple, row);
    this.network = inNetwork;
  }

  /**
   * Gets the underlying obvious edge.
   * @return the underlying obvious edge
   */
  protected obvious.data.Edge getObviousEdge() {
    return (obvious.data.Edge) getObviousTuple();
  }

  @Override
  public Node getAdjacentNode(Node n) {
    return null;
  }

  @Override
  public Graph getGraph() {
    return new WrapToPrefGraph(network);
  }

  @Override
  public Node getSourceNode() {
    obvious.data.Node obviousNode = network.getSource(getObviousEdge());
    return new WrapToPrefNode(network, (TupleImpl) obviousNode,
        obviousNode.getRow());
  }

  @Override
  public Node getTargetNode() {
    obvious.data.Node obviousNode = network.getTarget(getObviousEdge());
    return new WrapToPrefNode(network, (TupleImpl) obviousNode,
        obviousNode.getRow());
  }

  @Override
  public boolean isDirected() {
    return network.getEdgeType(getObviousEdge()).equals(
        obvious.data.Graph.EdgeType.DIRECTED);
  }

}
