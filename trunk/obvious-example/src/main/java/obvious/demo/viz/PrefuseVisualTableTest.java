package obvious.demo.viz;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.util.IntIterator;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Renderer;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;
import prefuse.Display;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;

/**
 * Test example to check VisualAttributeManager implementation for
 * obvious-prefuse.
 * @author Hemery
 *
 */
public final class PrefuseVisualTableTest {

  /**
   * Private constructor.
   */
  private PrefuseVisualTableTest() { }

  /**
   * Main method.
   * @param args arguments
   * @throws ObviousException if something wrong happens
   */
  public static void main(final String[] args) throws ObviousException {
    // Building the dataset.
    Schema schema = new IvtkObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 18);
    schema.addColumn("category", String.class, "unemployed");

    Table table = new IvtkObviousTable(schema);

    table.addRow(new TupleImpl(schema, new Object[] {1, 22, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {2, 60, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {3, 32, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {4, 20, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {5, 72, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {6, 40, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {7, 52, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {8, 35, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {9, 32, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {10, 44, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {11, 27, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {12, 38, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {13, 53, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {14, 49, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {15, 21, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {16, 36, "unemployed"}));

    // Creating the parameter map for the monolithic object.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
    param.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
    param.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field


    // Using the factory to build the visualization
    System.setProperty("obvious.VisualizationFactory",
        "obvious.prefuse.viz.PrefuseVisualizationFactory");
    VisualizationFactory factory = PrefuseVisualizationFactory.getInstance();
    Visualization vis =
      factory.createVisualization(table, null, "scatterplot", param);

    // In order to display, we have to call the underlying prefuse
    // visualization.
    // In a complete version of obvious, we don't need that step.
    prefuse.Visualization prefViz = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    // Building the prefuse display.
    //Display display = new Display(prefViz);
    
    PrefuseObviousView view = new PrefuseObviousView(vis, null, "scatterplot", null);
    view.addListener(new PrefuseObviousControl(new ZoomControl()));
    view.addListener(new PrefuseObviousControl(new PanControl()));
    view.addListener(new PrefuseObviousControl(new DragControl()));
    //System.out.println(display == null);
    view.getViewJComponent().setSize(800, 600);
    //display.addControlListener(new DragControl());
    //display.addControlListener(new PanControl());
    //display.addControlListener(new ZoomControl());
    JFrame frame = new JFrame("Data model : obvious-ivtk |"
        + " Visualisation : obvious-prefuse | View Prefuse | Monolithic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(view.getViewJComponent());
    frame.pack();
    frame.setVisible(true);
    prefViz.run("draw");
    table.addRow(new TupleImpl(schema, new Object[] {17, 28, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {18, 65, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {19, 56, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {20, 19, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {21, 26, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {22, 23, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {23, 45, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {24, 38, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {25, 29, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {26, 26, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {27, 43, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {29, 35, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {30, 58, "unemployed"}));

  }

}