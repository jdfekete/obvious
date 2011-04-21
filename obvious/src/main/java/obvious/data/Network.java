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

package obvious.data;

import java.util.Collection;

import obvious.ObviousException;
import obvious.data.event.NetworkListener;


/**
 * This interface subclasses Graph Interface with Obvious compatible
 * edge and node.
 * @see Graph
 * @see Node
 * @see Edge
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Network extends obvious.data.Graph<Node, Edge> {

  /**
   * Gets the collection of all listeners added to this Network instance.
   * @return collection of all listeners added to a Network instance
   */
  Collection<NetworkListener> getNetworkListeners();

  /**
   * Removes a given listener from this Network instance.
   * @param l listener to remove
   */
  void removeNetworkListener(NetworkListener l);

  /**
   * Adds a given listener to this Network instance.
   * @param l listener to add
   */
  void addNetworkListener(NetworkListener l);

  /**
   * Gets an obvious Table containing the nodes of this Network instance.
   * @return an obvious Table containing the nodes of this Network instance
   */
  Table getNodeTable();

  /**
   * Gets an obvious Table containing the edges of this Network instance.
   * @return an obvious Table containing the edges of this Network instance
   */
  Table getEdgeTable();

  /**
   * Gets the name of the column used to spot the source node for an edge in
   * this Network instance.
   * @return name of the column used to spot the source node for an edge
   */
  String getSourceColumnName();

  /**
   * Gets the name of the column used to spot the target node for an edge in
   * this Network instance.
   * @return name of the column used to spot the target node for an edge
   */
  String getTargetColumnName();

  /**
   * Indicates the beginning of a column edit.
   * <p>
   * This function could be used to create a context when a large number
   * of modifications happens to a same column to avoid time wasting with
   * plenty of notifications. In this context, TableListeners could ignore
   * notifications if wanted.
   * </p>
   * @param col column index
   * @throws ObviousException if edition is not supported.
   */
  void beginEdit(int col) throws ObviousException;

  /**
   * Indicates the end of a column edit.
   * <p>
   * This function indicates, if notifications were disabled, that now they
   * are enabled. It could also call a mechanism to replay the sequence of
   * ignored events if wanted.
   * </p>
   * @param col column index
   * @return true if transaction succeed
   * @throws ObviousException if edition is not supported.
   */
  boolean endEdit(int col) throws ObviousException;

  /**
   * Notifies changes to listener.
   * @param start the starting row index of the changed network region
   * @param end the ending row index of the changed network region
   * @param col the column that has changed
   * @param type the type of modification
   */
  void fireNetworkEvent(int start, int end, int col, int type);

}
