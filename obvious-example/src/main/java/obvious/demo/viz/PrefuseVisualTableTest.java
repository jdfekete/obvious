package obvious.demo.viz;

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
import obvious.viz.Visualization;

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
