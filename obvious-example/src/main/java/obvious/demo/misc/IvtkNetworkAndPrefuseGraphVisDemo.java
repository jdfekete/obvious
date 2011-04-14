package obvious.demo.misc;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.util.PrefuseObviousNetworkViz;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * This class shows an Obvious example based on the IVTK and Prefuse binding. It
 * illustrates how visualizing a wrapped IVTK graph with Prefuse.
 *
 * @author Hemery
 *
 */
public final class IvtkNetworkAndPrefuseGraphVisDemo {

  /**
   * Constructor.
   */
  private IvtkNetworkAndPrefuseGraphVisDemo() { }

  /**
   * Main method.
   * @param args arguments of the main
   */
  public static void main(final String[] args) {

    // Creating the example network.
    Schema nodeSchema = new PrefuseObviousSchema();
    Schema edgeSchema = new PrefuseObviousSchema();

    nodeSchema.addColumn("name", String.class, "bob");
    nodeSchema.addColumn("id", int.class, 0);

    edgeSchema.addColumn("source", int.class, 0);
    edgeSchema.addColumn("target", int.class, 0);

    // Creating nodes and edges
    Node node1 = new NodeImpl(nodeSchema, new Object[] {"1", 0});
    Node node2 = new NodeImpl(nodeSchema, new Object[] {"2", 1});
    Node node3 = new NodeImpl(nodeSchema, new Object[] {"3", 2});
    Node node4 = new NodeImpl(nodeSchema, new Object[] {"4", 3});

    Edge edge1 = new EdgeImpl(edgeSchema, new Object[] {0, 1});
    Edge edge2 = new EdgeImpl(edgeSchema, new Object[] {1, 2});
    Edge edge3 = new EdgeImpl(edgeSchema, new Object[] {2, 0});
    Edge edge4 = new EdgeImpl(edgeSchema, new Object[] {3, 1});

    // Building the network
    Network network = new IvtkObviousNetwork(nodeSchema, edgeSchema);
    network.addNode(node1);
    network.addNode(node2);
    network.addNode(node3);
    network.addNode(node4);

    network.addEdge(edge1, node1, node2, Graph.EdgeType.UNDIRECTED);
    network.addEdge(edge2, node2, node3, Graph.EdgeType.UNDIRECTED);
    network.addEdge(edge3, node3, node1, Graph.EdgeType.UNDIRECTED);
    network.addEdge(edge4, node4, node2, Graph.EdgeType.UNDIRECTED);

    // Directly creates the visualization with the appropriate constructor.
    // No extra parameters are given.
    Visualization vis = new PrefuseObviousNetworkViz(
        network, null, null, null);

    // Creating the view.
    JView view = new PrefuseObviousView(vis, null, null, null);

    // Standard java window creation.
    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final int dim = 500;
    frame.setSize(dim, dim);
    frame.getContentPane().add(view.getViewJComponent());
    frame.setVisible(true);

    // In order to display, we have to call the underlying prefuse
    // visualization.
    // In a complete version of obvious, we should avoid that step.
    prefuse.Visualization prefViz = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    prefViz.run("color");
    prefViz.run("layout");
  }

}
