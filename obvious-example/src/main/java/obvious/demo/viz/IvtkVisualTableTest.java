package obvious.demo.viz;

import infovis.panel.VisualizationPanel;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.util.IntIterator;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.prefuse.PrefuseObviousTable;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.viz.Visualization;

/**
 * Test example to check VisualAttributeManager implementation for obvious-ivtk.
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
   */
  public static void main(final String[] args) {

    // Building the dataset.
    Schema schema = new IvtkObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 18);
    schema.addColumn("category", String.class, "unemployed");

    Table table = new PrefuseObviousTable(schema);

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
    
    IvtkObviousView view = new IvtkObviousView(vis,  null, "scatterplot", null);

    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.getContentPane().add(view.getViewJComponent());
    frame.setVisible(true);

    vis.getAliasMap().put(Visualization.VISUAL_X ,
        infovis.table.visualization.ScatterPlotVisualization.VISUAL_X_AXIS);
    vis.getAliasMap().put(Visualization.VISUAL_Y ,
        infovis.table.visualization.ScatterPlotVisualization.VISUAL_Y_AXIS);


    /*
    for (IntIterator it = table.rowIterator(); it.hasNext();) {
      Tuple tuple = new TupleImpl(table, it.next());
      System.out.print(tuple.get("id") + " || ");
      for (Map.Entry<String, String> e : vis.getAliasMap().entrySet()) {
        System.out.print(" " + e.getKey() + " : "
            + vis.getAttributeValuetAt(tuple, e.getKey()));
      }
      System.out.println();
    }
    */
  }

}
