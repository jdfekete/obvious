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

package obvious.impl;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.event.NetworkListener;

/**
 * This listener has been written to allow two identical obvious networks using
 * different implementations to be synchronized. Both networks should add a
 * listener to the other to listen. This is largely used in visualization and
 * view package to synchronize data model and visualization / view that does
 * not used the same obvious implementation.
 * @author  Hemery
 */
public class ObviousLinkNetworkListener implements NetworkListener {

    /**
     * The number of time beginEdit has been called minus the number of time
     * endEdit has been called.
    */
  private int inhibitNotify = 0;

    /**
     * Source table.
     * @uml.property  name="source"
     */
    private Network source;

    /**
     * Target table.
     * @uml.property  name="target"
     */
    private Network target;

    /**
     * Constructor.
     * @param inTarget targetted network.
     */
    public ObviousLinkNetworkListener(Network inTarget) {
      this.target = inTarget;
    }

    /**
     * Specifies that the following calls to tableChanged belong to the same
     * transaction.
     * <p>
     * This method could be used when a large number of modification
     * are made on a Table and notifying everything would be time expansive.
     * For instance, it could disable notifications. The behavior of this
     * method clearly depends of the purpose of the Obvious implementation.
     * </p>
     * @param context an integer used, if needed, to identify the edition
     * context of the edit.
     *
     */
    public void beginEdit(int context) {
      inhibitNotify++;
    }

    /**
     * Specifies that the calls to tableChanged belonging to the same
     * transaction are finished.
     * <p>
     * This function could be used to re-enabled notifications for the current
     * TableListener if they were disabled. It could also replay a stored
     * context of the period where the notifications where disabled. The
     * behavior of this method clearly depends of the purpose of the Obvious
     * implementation.
     * </p>
     * @param context an integer used, if needed, to retrieve the edition
     * context It could be used to execute further operations on the table
     * (for instance replaying sequence of events).
     * @return true if transaction succeed
     */
    public boolean endEdit(int context) {
      inhibitNotify--;
      if (inhibitNotify <= 0) {
        inhibitNotify = 0;
      }
      return true;
    }

    /**
     * Checks if the table meets criteria defined by invariant(s). If no
     * invariant is defined for this structure, the methods has to return
     * true.
     * @return true if the invariant is checked
     */
    public boolean checkInvariants() {
      return true;
    }

    /**
     * Notification that a network has changed.
     * @param t the network that has changed
     * @param start the starting row index of the changed network region
     * @param end the ending row index of the changed network region
     * @param col the column that has changed, or
     * {@link EventConstants#ALL_COLUMNS} if the operation affects all
     * columns
     * @param type the type of modification
     */
    public void networkChanged(Network t, int start, int end, int col,
        int type) {
      source = t;
      if (inhibitNotify != 0) {
        return;
      } else if (type == NetworkListener.DELETE_NODE) {
        deleteNode(target, start, end, col);
      } else if (type == NetworkListener.DELETE_EDGE) {
        deleteEdge(target, start, end, col);
      } else if (type == NetworkListener.INSERT_NODE) {
        insertNode(target, start, end, col);
      } else if (type == NetworkListener.INSERT_EDGE) {
        insertEdge(target, start, end, col);
      } else if (type == NetworkListener.UPDATE_NODE) {
        updateNode(target, start, end, col);
      } else if (type == NetworkListener.UPDATE_EDGE) {
        updateEdge(target, start, end, col);
      } else {
        return;
      }
    }

    /**
     * Edge(s) have been updated.
     * @param target2 network that has changed
     * @param start first changed edge (id)
     * @param end last changed edge (id)
     * @param col columns that have changed
     */
    private void updateEdge(Network target2, int start, int end, int col) {
      if (inhibitNotify != 0) {
        return;
      }
      try {
        //TODO
        System.out.println("unimplemented");
      } catch (Exception e) {
        new ObviousRuntimeException(e);
      }
    }

    /**
     * Node(s) have been updated.
     * @param target2 network that has changed
     * @param start first changed node (id)
     * @param end last changed node (id)
     * @param col columns that have changed
     */
    private void updateNode(Network target2, int start, int end, int col) {
      if (inhibitNotify != 0) {
        return;
      }
      try {
        //TODO
        System.out.println("unimplemented");
      } catch (Exception e) {
        new ObviousRuntimeException(e);
      }
    }

    /**
     * Edge(s) have been inserted.
     * @param target2 network that has changed
     * @param start first changed edge (id)
     * @param end last changed edge (id)
     * @param col columns that have changed
     */
    private void insertEdge(Network target2, int start, int end, int col) {
      if (inhibitNotify != 0) {
        return;
      }
      try {
        for (int i = start; i <= end; i++) {
          Edge currentEdge = findEdge(i, source);
          Node sourceNode = source.getSource(currentEdge);
          Node targetNode = source.getTarget(currentEdge);
          target.addEdge(currentEdge, sourceNode, targetNode,
              Graph.EdgeType.DIRECTED);
        }
      } catch (Exception e) {
        new ObviousRuntimeException(e);
      }
    }

    /**
     * Node(s) have been inserted.
     * @param target2 network that has changed
     * @param start first changed node (id)
     * @param end last changed node (id)
     * @param col columns that have changed
     */
    private void insertNode(Network target2, int start, int end, int col) {
      if (inhibitNotify != 0) {
        return;
      }
      try {
        for (int i = start; i <= end; i++) {
          Node currentNode = findNode(i, source);
          target.addNode(currentNode);
        }
      } catch (Exception e) {
        new ObviousRuntimeException(e);
      }
    }

    /**
     * Edge(s) have been deleted.
     * @param target2 network that has changed
     * @param start first changed edge (id)
     * @param end last changed edge (id)
     * @param col columns that have changed
     */
    private void deleteEdge(Network target2, int start, int end, int col) {
      if (inhibitNotify != 0) {
        return;
      }
      try {
        //TODO
        System.out.println("unimplemented");
      } catch (Exception e) {
        new ObviousRuntimeException(e);
      }
    }

    /**
     * Node(s) have been deleted.
     * @param target2 network that has changed
     * @param start first changed node (id)
     * @param end last changed node (id)
     * @param col columns that have changed
     */
    private void deleteNode(Network target2, int start, int end, int col) {
      if (inhibitNotify != 0) {
        return;
      }
      try {
        for (int i = start; i <= end; i++) {
          Node currentNode = findNode(i, target);
          target.removeNode(currentNode);
        }
      } catch (Exception e) {
        new ObviousRuntimeException(e);
      }
    }

    /**
     * Find a node in the network.
     * @param i id of the node to find
     * @param network network where to find the node
     * @return a node
     */
    private Node findNode(int i, Network network) {
      int row = 0;
      for (Node node : network.getNodes()) {
        if (row == i) {
          return node;
        }
        row++;
      }
      return null;
    }

    /**
     * Find an edge in the network.
     * @param i id of the edge to find
     * @param network network where to find the edge
     * @return an edge
     */
    private Edge findEdge(int i, Network network) {
      int row = 0;
      for (Edge edge : network.getEdges()) {
        if (row == i) {
          return edge;
        }
        row++;
      }
      return null;
    }

}
