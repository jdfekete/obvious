package obvious.demo.viz;

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
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.prefuse.data.PrefuseObviousTable;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;


/**
 * Test example to check VisualAttributeManager implementation for
 * obvious-prefuse.
 * @author Hemery
 *
 */
public final class PrefuseVisualTableTestWithIvtkView {

  /**
   * Private constructor.
   */
  private PrefuseVisualTableTestWithIvtkView() { }

  /**
   * Main method.
   * @param args arguments
   * @throws ObviousException if something wrong happens
   * @throws ObviousxException
   */
  public static void main(final String[] args) throws ObviousException,
      ObviousxException {

    System.setProperty("obvious.DataFactory",
        "obvious.prefuse.PrefuseDataFactory");

    CSVImport csv = new CSVImport(new File(
        "src//main//resources//articlecombinedexample.csv"), ',');
    Table table = csv.loadTable();
    // Creating the parameter map for the monolithic object.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(IvtkScatterPlotVis.X_AXIS, "id"); // name of the xfield
    param.put(IvtkScatterPlotVis.Y_AXIS, "age"); // name of the yfield

    // Creating the visualization.
    Visualization vis = new IvtkScatterPlotVis(table, null, null, param);

    IvtkObviousView view = new IvtkObviousView(vis,  null, "scatterplot", null);

    JFrame frame = new JFrame("Data model : obvious-prefuse |"
        + " Visualisation : obvious-ivtk | View obvious-ivtk | Monolithic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(view.getViewJComponent());
    frame.pack();
    frame.setVisible(true);


  }

}
