package obvious.demo.jung;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Forest;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;
import obvious.jung.data.JungObviousForest;
import obvious.jung.view.JungObviousView;
import obvious.jung.visualization.JungObviousVisualization;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * This example rewrites the TreeLayoutDemo example of Jung in an Obvious way.
 * @author Hemery
 *
 */
public final class TreeLayoutDemo {

  /**
   * Constructor.
   */
  private TreeLayoutDemo() { };

  /**
   * Main method.
   * @param args arguments of the main
   */
  public static void main(final String[] args) {
    Forest<Node, Edge> tree = createForest();
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("LAYOUT", "edu.uci.ics.jung.algorithms.layout.TreeLayout");
    Visualization vis = new JungObviousVisualization(tree, null, null, param);
    JView view = new JungObviousView(vis, null, null, null);
    JFrame frame = new JFrame("Obvious Jung binding : tree layout demo");
    frame.add(view.getViewJComponent());
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Creates an Obvious tree based on Jung.
   * @return an Obvious tree
   */
  private static Forest<Node, Edge> createForest() {
    Schema nodeSchema = new SchemaImpl();
    Schema edgeSchema = new SchemaImpl();
    nodeSchema.addColumn("nodeName", String.class, "DefaultNode");
    edgeSchema.addColumn("edgeName", String.class, "DefaultNode");
    edgeSchema.addColumn("source", Integer.class, -1);
    edgeSchema.addColumn("target", Integer.class, -1);
    Map<String, Node> nodeMap = new HashMap<String, Node>();
    Forest<Node, Edge> tree = new JungObviousForest(nodeSchema, edgeSchema,
        "source", "target");
    for (int i = 0; i < 11; i++) {
      Node v = new NodeImpl(nodeSchema, new Object[] {"V" + i});
      nodeMap.put("V" + i, v);
      tree.addNode(v);
      if (i < 10) {
        Node b = new NodeImpl(nodeSchema, new Object[] {"B" + i});
        nodeMap.put("B" + i, b);
        tree.addNode(b);
      }
      if (i < 4) {
        Node a = new NodeImpl(nodeSchema, new Object[] {"A" + i});
        nodeMap.put("A" + i, a);
        tree.addNode(a);
      }
    }
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V0-V1", 0, 1}),
        nodeMap.get("V0"), nodeMap.get("V1"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V0-V2", 0, 2}),
        nodeMap.get("V0"), nodeMap.get("V2"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V1-V4", 1, 4}),
        nodeMap.get("V1"), nodeMap.get("V4"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V4-V6", 4, 6}),
        nodeMap.get("V4"), nodeMap.get("V6"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V4-V7", 4, 7}),
        nodeMap.get("V4"), nodeMap.get("V7"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V4-V10", 4, 10}),
        nodeMap.get("V4"), nodeMap.get("V10"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V6-V9", 6, 9}),
        nodeMap.get("V6"), nodeMap.get("V9"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V2-V5", 2, 5}),
        nodeMap.get("V2"), nodeMap.get("V5"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V2-V3", 2, 3}),
        nodeMap.get("V2"), nodeMap.get("V3"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"V3-V8", 3, 8}),
        nodeMap.get("V3"), nodeMap.get("V8"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B0-B1", 11, 12}),
        nodeMap.get("B0"), nodeMap.get("B1"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B0-B2", 11, 13}),
        nodeMap.get("B0"), nodeMap.get("B2"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B1-B4", 12, 15}),
        nodeMap.get("B1"), nodeMap.get("B4"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B4-B7", 15, 18}),
        nodeMap.get("B4"), nodeMap.get("B7"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B4-B6", 15, 17}),
        nodeMap.get("B4"), nodeMap.get("B6"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B6-B9", 17, 20}),
        nodeMap.get("B6"), nodeMap.get("B9"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B2-B5", 13, 16}),
        nodeMap.get("B2"), nodeMap.get("B5"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B2-B3", 13, 14}),
        nodeMap.get("B2"), nodeMap.get("B3"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"B3-B8", 14, 19}),
        nodeMap.get("B3"), nodeMap.get("B8"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A0-A1", 21, 22}),
        nodeMap.get("A0"), nodeMap.get("A1"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A0-A2", 21, 23}),
        nodeMap.get("A0"), nodeMap.get("A2"), EdgeType.DIRECTED);
    tree.addEdge(new EdgeImpl(edgeSchema, new Object[] {"A0-A3", 21, 24}),
        nodeMap.get("A0"), nodeMap.get("A3"), EdgeType.DIRECTED);
    return tree;
  }

}
