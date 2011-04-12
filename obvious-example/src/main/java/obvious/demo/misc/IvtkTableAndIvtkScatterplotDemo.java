package obvious.demo.misc;

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
 * This demo used the Obvious IVTK binding for the whole visualization
 * pipeline.
 * It intends to demonstrate how to view a scatter-plot with an Obvious binding
 * from the CSV import to the standard java window creation.
 *
 * @author Hemery
 *
 */
public final class IvtkTableAndIvtkScatterplotDemo {

  /**
   * Constructor.
   */
  private IvtkTableAndIvtkScatterplotDemo() { }

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
