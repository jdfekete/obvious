package obvious.demo.mix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;
import prefuse.Display;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;

/**
 * A simple visualization test of a scatter plot. It uses as data model
 * an obvious-ivtk table. For visualisation, it uses obvious-prefuse in a
 * monolithic approach.
 * @author Hemery
 *
 */
public final class ScatterPlotMonoVisualization {

  /**
   * Private constructor.
   */
  private ScatterPlotMonoVisualization() { }

  /**
   * Main method.
   * @param args arguments
   * @throws ObviousException if something bad happens
   * @throws ObviousxException if something bad happens
   */
  public static void main(final String[] args) throws ObviousException,
      ObviousxException {

    // Building the dataset.
    Schema schema = new IvtkObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 0);
    schema.addColumn("category", String.class, "unemployed");

    System.setProperty("obvious.DataFactory",
    "obvious.ivtk.data.IvtkDataFactory");

    // Creating an Obvious CSV reader and loading an Obvious table
    CSVImport csv = new CSVImport(new File(
      "src//main//resources//articlecombinedexample.csv"), ',');
    Table table = csv.loadTable();

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
    Display display = new Display(prefViz);
    display.setSize(300, 200);
    display.addControlListener(new DragControl());
    display.addControlListener(new PanControl());
    display.addControlListener(new ZoomControl());

    // JFrame...
    JFrame frame = new JFrame("Data model : obvious-ivtk |"
        + " Visualisation : obvious-prefuse | View Prefuse | Monolithic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(display);
    frame.pack();
    frame.setVisible(true);
    prefViz.run("draw");
  }

}
