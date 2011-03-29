package obvious.demo.viz;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.NodeImpl;
import obvious.prefuse.data.PrefuseObviousNetwork;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisBoost;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousNetworkViz;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.view.View;
import obvious.view.JView;

import prefuse.Constants;
import prefuse.Display;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * Simple test for a graph visualization with obvious-prefuse.
 * Obvious-prefuse implementation is used for data model and visualization.
 * Prefuse standard implementation is used to display.
 * This demo is based on the example of the prefuse manual and the data file
 * comes from it too.
 * @author Hemery
 *
 */
public final class VizGraphTest {

  /**
   * Private constructor.
   */
  private VizGraphTest() { }

  /**
   * Main method.
   * @param args args
   */
  public static void main(final String[] args) {

    // Create the prefuse graph.
    prefuse.data.Graph prefGraph = null;
    try {
      prefGraph = new GraphMLReader().readGraph(
          "src/main/resources/socialnet.xml");
    } catch (DataIOException e) {
      e.printStackTrace();
      System.err.println("Error loading graph. Exiting...");
      System.exit(1);
    }

    // Create the parameter for the visualization.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");
    param.put(PrefuseObviousNetworkViz.LABEL_KEY, "name");

    // Create the obvious-prefuse graph.
    PrefuseObviousNetwork network = new PrefuseObviousNetwork(prefGraph);

    // Create the obvious-prefuse visualization.
    PrefuseObviousVisualization vis = new PrefuseObviousVisBoost(
        network, null, null, param);

    // Using label renderer as in the tutorial of prefuse.
    LabelRenderer r = new LabelRenderer("name");
    r.setRoundedCorner(8, 8); // round the corners
    vis.setRenderer(new PrefuseObviousRenderer(new DefaultRendererFactory(r)));

    // Color for data values.
   int[] palette = new int[] {
       ColorLib.rgb(255,180,180), ColorLib.rgb(190,190,255)
   };
   DataColorAction fill = new DataColorAction("graph.nodes", "gender",
       Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
   ColorAction text = new ColorAction("graph.nodes",
       VisualItem.TEXTCOLOR, ColorLib.gray(0));
   // Color for edges
   ColorAction edges = new ColorAction("graph.edges",
       VisualItem.STROKECOLOR, ColorLib.gray(200));

   // Creating the prefuse action list.
   ActionList color = new ActionList();
   color.add(fill);
   color.add(text);
   color.add(edges);
   // Wrapping the action list around obvious
   vis.putAction("color", new PrefuseObviousAction(color));


   // Creating a Directed force Layout.
  ActionList layout = new ActionList(Activity.INFINITY);
  layout.add(new ForceDirectedLayout("graph"));
  layout.add(new RepaintAction());
  // Wrapping the layout around obvious.
  vis.putAction("layout", new PrefuseObviousAction(layout));


  

  /*
  PrefuseObviousVisualization vis = new PrefuseObviousNetworkViz(network, null, "test", param);
  */
  // In order to display, we have to call the underlying prefuse visualization.
  // In a complete version of obvious, we don't need that step.
  prefuse.Visualization prefViz = (prefuse.Visualization)
  vis.getUnderlyingImpl(prefuse.Visualization.class);
  
  PrefuseObviousView view = new PrefuseObviousView(vis, null, "scatterplot", null);
  //view.addListener(new PrefuseObviousControl(new ZoomControl()));
  //view.addListener(new PrefuseObviousControl(new PanControl()));
  //view.addListener(new PrefuseObviousControl(new DragControl()));
  obvious.view.control.PanControl control = new obvious.view.control.PanControl(view);
  obvious.view.control.ZoomControl zoomcontrol = new obvious.view.control.ZoomControl(view);
  view.getViewJComponent().addMouseListener(control);
  view.getViewJComponent().addMouseMotionListener(control);
  view.getViewJComponent().addMouseListener(zoomcontrol);
  view.getViewJComponent().addMouseMotionListener(zoomcontrol);
  //create a new window to hold the visualization
  JFrame frame = new JFrame("DataModel : Obvious-prefuse"
      + " | Visu : Obvious-Prefuse | View : Obvious-Prefuse");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.add(view.getViewJComponent());
  frame.pack();
  frame.setVisible(true);

  prefViz.run("color");  // assign the colors
  prefViz.run("layout"); // start up the animated layout

  Schema nodeSchema = new PrefuseObviousSchema();
  nodeSchema.addColumn("name", String.class, "bob");
  nodeSchema.addColumn("gender", String.class, "male");
  network.addNode(new NodeImpl(nodeSchema, new Object[] {"marc", "male"}));
  }

}
