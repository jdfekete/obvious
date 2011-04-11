package obvious.demo.mix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.viz.Visualization;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;

/**
 * This class shows an Obvious example based on the IVTK and Prefuse bindings.
 * It illustrates combination of different bindings to create a scatter-plot
 * visualization.
 * @author Hemery
 *
 */
public final class IvtkVisualTableTest {

  /**
   * Constructor.
   */
  private IvtkVisualTableTest() { }

  /**
   * Main method.
   * @param args arguments
   * @throws ObviousxException if something bad happens
   */
  public static void main(final String[] args) throws ObviousxException {

    // Building the dataset.
    Schema schema = new IvtkObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 0);
    schema.addColumn("category", String.class, "unemployed");

    // Sets the factory.
    System.setProperty("obvious.DataFactory",
    "obvious.prefuse.data.PrefuseDataFactory");

    // Creates an Obvious CSV reader and loading an Obvious table
    CSVImport csv = new CSVImport(new File(
        "src//main//resources//articlecombinedexample.csv"), ',');
    Table table = csv.loadTable();

    Map<String, Object> param = new HashMap<String, Object>();
    param.put(IvtkScatterPlotVis.X_AXIS, "id"); // x axis of the scatter-plot
    param.put(IvtkScatterPlotVis.Y_AXIS, "age"); // y axis of the scatter-plot
    Visualization vis = new IvtkScatterPlotVis(
        table, null, "scatterplot", param);

    IvtkObviousView view = new IvtkObviousView(vis,  null, "scatterplot", null);

    // Standard window creation.
    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final int dim = 500;
    frame.setSize(dim, dim);
    frame.getContentPane().add(view.getViewJComponent());
    frame.setVisible(true);

    vis.getAliasMap().put(Visualization.VISUAL_X ,
        infovis.table.visualization.ScatterPlotVisualization.VISUAL_X_AXIS);
    vis.getAliasMap().put(Visualization.VISUAL_Y ,
        infovis.table.visualization.ScatterPlotVisualization.VISUAL_Y_AXIS);

  }

}
