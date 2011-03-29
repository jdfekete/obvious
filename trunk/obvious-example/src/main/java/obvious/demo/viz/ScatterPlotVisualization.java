package obvious.demo.viz;

import javax.swing.JFrame;

import obvious.prefuse.data.PrefuseObviousTable;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.viz.Visualization;
import obvious.data.Table;
import prefuse.Constants;
import prefuse.Display;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.AxisLayout;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * A simple visualization test of a scatter plot. This example derived
 * from a the ScatterPlot demo of the prefuse toolkit. It uses the same
 * data file and the same actions / renderer. But it's built with Obvious data
 * model and visualization. In this version, it is displayed with prefuse
 * toolkit.
 * In this example, we use the polylithic approach, that's why some prefuse
 * code is present to build action and renderer.
 * @author Hemery
 *
 */
public final class ScatterPlotVisualization {

  /**
   * Private constructor.
   */
  private ScatterPlotVisualization() { }

  /**
   * Main function.
   * @param args arguments
   */
  public static void main(final String[] args) {

    // Load data into a prefuse table.
    prefuse.data.Table prefTable = null;
    try {
        prefTable = new DelimitedTextTableReader().readTable(
            "src/main/resources/fisher.iris.txt");
    } catch (Exception e) {
        e.printStackTrace();
    }
    // Wrapping the prefuse table to an obvious table.
    Table table = new PrefuseObviousTable(prefTable);
    // Creating the obvious visualization.
    Visualization viz = new PrefuseObviousVisualization(table, null, null,
        null);

    // We define all useful field and paramaters (such as group name).
    String xfield = "SepalLength";
    String yfield = "PetalLength";
    String sfield = "Species";
    String group = "tupleset";
    ShapeRenderer m_shapeR = new ShapeRenderer(2); // three "kinds" of tuple

    // Setting the default renderer factory then adding it to obvious vis.
    DefaultRendererFactory rf = new DefaultRendererFactory(m_shapeR);
    viz.setRenderer(new PrefuseObviousRenderer(rf));

    // Layout definition then adding it to obvious visualization.
    AxisLayout xAxis = new AxisLayout(group, xfield,
        Constants.X_AXIS, VisiblePredicate.TRUE);
    viz.putAction("x", new PrefuseObviousAction(xAxis));

    AxisLayout yAxis = new AxisLayout(group, yfield,
        Constants.Y_AXIS, VisiblePredicate.TRUE);
    viz.putAction("y", new PrefuseObviousAction(yAxis));

    ColorAction color = new ColorAction(group,
        VisualItem.STROKECOLOR, ColorLib.rgb(100,100,255));
    viz.putAction("color", new PrefuseObviousAction(color));

    DataShapeAction shape = new DataShapeAction(group, sfield);
    viz.putAction("shape", new PrefuseObviousAction(shape));
    // Building the action list, then add it to obvious visualization.
    ActionList draw = new ActionList();
    draw.add(xAxis);
    draw.add(yAxis);
    if (sfield != null) {
        draw.add(shape);
    }
    draw.add(color);
    draw.add(new RepaintAction());
    viz.putAction("draw", new PrefuseObviousAction(draw));

    // In order to display, we have to call the underlying prefuse
    // visualization.
    // In a complete version of obvious, we don't need that step.
    prefuse.Visualization prefViz = (prefuse.Visualization)
    viz.getUnderlyingImpl(prefuse.Visualization.class);

    // Building the prefuse display.
    Display display = new Display(prefViz);
    display.setSize(300, 200);
    display.addControlListener(new DragControl());
    display.addControlListener(new PanControl());
    display.addControlListener(new ZoomControl());

    // Jframe...
    JFrame frame = new JFrame("Data model : obvious-prefuse |"
        + " Visualisation : obvious-prefuse | View Prefuse | Polylithic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(display);
    frame.pack();
    frame.setVisible(true);
    prefViz.run("draw");
  }

}
