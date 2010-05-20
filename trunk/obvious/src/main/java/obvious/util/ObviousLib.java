package obvious.util;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Node;
import obvious.data.Table;
import obvious.data.Network;
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

}
