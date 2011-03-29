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

package obvious.prefuse.utils.wrappers;

import java.util.Iterator;

import obvious.data.Network;
import obvious.impl.TupleImpl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

/**
 * Wrapper for obvious tuple to pref node.
 * @author Hemery
 *
 */
public class WrapToPrefNode extends WrapToPrefTuple
    implements prefuse.data.Node {

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
  public WrapToPrefNode(Network inNetwork, TupleImpl inTuple, int row) {
    super(inTuple, row);
    this.network = inNetwork;
  }

  /**
   * Gets the underlying obvious node.
   * @return the underlying obvious node
   */
  protected obvious.data.Node getObviousNode() {
    return (obvious.data.Node) getObviousTuple();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator childEdges() {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator children() {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator edges() {
    return network.getIncidentEdges(getObviousNode()).iterator();
  }

  @Override
  public Node getChild(int idx) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getChildCount() {
    return network.getIncidentEdges(getObviousNode()).size();
  }

  @Override
  public int getChildIndex(Node child) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDegree() {
    return network.getEdges().size();
  }

  @Override
  public int getDepth() {
    return 0;
  }

  @Override
  public Node getFirstChild() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Graph getGraph() {
    return new WrapToPrefGraph(network);
  }

  @Override
  public int getInDegree() {
    return network.getInEdges(getObviousNode()).size();
  }

  @Override
  public Node getLastChild() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node getNextSibling() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getOutDegree() {
    return network.getOutEdges(getObviousNode()).size();
  }

  @Override
  public Node getParent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Edge getParentEdge() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node getPreviousSibling() {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator inEdges() {
    return network.getInEdges(getObviousNode()).iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator inNeighbors() {
    return network.getPredecessors(getObviousNode()).iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator neighbors() {
    return network.getNeighbors(getObviousNode()).iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator outEdges() {
    return network.getOutEdges(getObviousNode()).iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator outNeighbors() {
    return network.getSuccessors(getObviousNode()).iterator();
  }

}
