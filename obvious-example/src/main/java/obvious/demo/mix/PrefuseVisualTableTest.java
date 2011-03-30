package obvious.demo.mix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.viz.Visualization;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;

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
    param.put(IvtkScatterPlotVis.X_AXIS, "id"); // name of the xfield
    param.put(IvtkScatterPlotVis.Y_AXIS, "age"); // name of the yfield
    //param.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field


    // Using the factory to build the visualization
    Visualization vis = new IvtkScatterPlotVis(table, null, null, null);
    IvtkObviousView view = new IvtkObviousView(vis,  null,
        "scatterplot", param);

    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final int dim = 500;
    frame.setSize(dim, dim);
    frame.getContentPane().add(view.getViewJComponent());
    frame.setVisible(true);

  }

}
