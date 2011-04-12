package obvious.demo.mix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.util.IntIterator;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;

/**
 * Test example to check VisualAttributeManager implementation for obvious-ivtk.
 * @author Hemery
 *
 */
public final class IvtkVisualTableTestWithPrefView {

  /**
   * Constructor.
   */
  private IvtkVisualTableTestWithPrefView() { }

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

    System.setProperty("obvious.DataFactory",
    "obvious.prefuse.data.PrefuseDataFactory");

    // Creating an Obvious CSV reader and loading an Obvious table
    CSVImport csv = new CSVImport(new File(
    "src//main//resources//articlecombinedexample.csv"), ',');
    Table table = csv.loadTable();

    Map<String, Object> param = new HashMap<String, Object>();
    param.put(IvtkScatterPlotVis.X_AXIS, "id");
    param.put(IvtkScatterPlotVis.Y_AXIS, "age");


    Visualization vis = new IvtkScatterPlotVis(
        table, null, "scatterplot", param);
    /*
    infovis.Visualization ivtkVis = (infovis.Visualization)
        vis.getUnderlyingImpl(infovis.Visualization.class);
    VisualizationPanel panel = new VisualizationPanel(ivtkVis);
    */

    // Creating the parameter map for the monolithic object.
    Map<String, Object> paramView = new HashMap<String, Object>();
    paramView.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
    paramView.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
    //paramView.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field

    PrefuseObviousView view = new PrefuseObviousView(
        vis, null, "scatterplot", paramView);
    ((prefuse.Visualization) view.getVisualization().getUnderlyingImpl(
        prefuse.Visualization.class)).run("draw");
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


    for (IntIterator it = table.rowIterator(); it.hasNext();) {
      Tuple tuple = new TupleImpl(table, it.next());
      System.out.print(tuple.get("id") + " || ");
      for (Map.Entry<String, String> e : vis.getAliasMap().entrySet()) {
        System.out.print(" " + e.getKey() + " : "
            + vis.getAttributeValuetAt(tuple, e.getKey()));
      }
      System.out.println();
    }

  }

}
