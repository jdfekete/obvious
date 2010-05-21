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

package obvious.util;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Node;
import obvious.data.Table;
import obvious.data.Network;
import obvious.data.Tree;
import obvious.data.util.IntIterator;
import obvious.impl.TupleImpl;

/**
 * A class that contains convenient methods for Obvious.
 * @author Hemery
 *
 */
public final class ObviousLib {

  /**
   * Private constructor.
   */
  private ObviousLib() { }

  /**
   * Fills an obvious table with the content of an another table.
   * Tables have to have the same schema.
   * @param sourceTable content table
   * @param filledTable table to fill
   */
  public static void fillTable(Table sourceTable, Table filledTable) {
    for (IntIterator it = sourceTable.rowIterator(); it.hasNext();) {
      int rowId = it.nextInt();
      filledTable.addRow(new TupleImpl(sourceTable, rowId));
    }
  }

  /**
   * Fills an obvious network with the content of an another network.
   * Networks have to have the same schema.
   * @param sourceNet content network
   * @param filledNet network to fill
   */
  public static void fillNetwork(Network sourceNet, Network filledNet) {
    for (Node node : sourceNet.getNodes()) {
      filledNet.addNode(node);
    }
    for (Edge edge : sourceNet.getEdges()) {
      if (sourceNet.getEdgeType(edge) == Graph.EdgeType.DIRECTED) {
        filledNet.addEdge(edge, filledNet.getSource(edge),
            sourceNet.getTarget(edge), sourceNet.getEdgeType(edge));
      } else {
        Node firstNode = null;
        Node secondNode = null;
        int count = 0;
        for (Node node : sourceNet.getIncidentNodes(edge)) {
          if (count == 0) {
            firstNode = node;
          } else if (count == 1) {
            secondNode = node;
            break;
          }
          count++;
        }
        filledNet.addEdge(edge, firstNode, secondNode,
            sourceNet.getEdgeType(edge));
      }
    }
  }

  /**
   * Fills an obvious tree with the content of an another tree.
   * Trees have to have the same schema.
   * @param sourceTree content tree
   * @param filledTree tree to fill
   */
  public static void fillTree(Tree<Node, Edge> sourceTree,
      Tree<Node, Edge> filledTree) {
    for (Node node : sourceTree.getNodes()) {
      filledTree.addNode(node);
    }
    for (Edge edge : sourceTree.getEdges()) {
      filledTree.addEdge(edge, sourceTree.getSource(edge),
          sourceTree.getTarget(edge), Graph.EdgeType.DIRECTED);
    }
  }

}
