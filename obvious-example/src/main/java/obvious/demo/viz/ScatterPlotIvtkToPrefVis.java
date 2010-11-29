package obvious.demo.viz;

import java.util.Iterator;

import javax.swing.JFrame;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisBoost;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.viz.Visualization;
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
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * A simple visualization test of a scatter plot. It uses as data model
 * an obvious-ivtk table. For visualisation, it uses obvious-prefuse in a
 * polylithic approach that's why some prefuse code is present to build
 * action and renderer.
 * @author Hemery
 *
 */
public final class ScatterPlotIvtkToPrefVis {

  /**
   * Private constructor.
   */
  private ScatterPlotIvtkToPrefVis() { }

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

    // Building the visulalization
    Visualization viz = new PrefuseObviousVisBoost(table, null, null,
        null);
    // We define all useful field and paramaters (such as group name).
    String xfield = "id";
    String yfield = "age";
    String sfield = "category";
    String group = "tupleset";
    ShapeRenderer m_shapeR = new ShapeRenderer(2);

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

    PrefuseObviousView view = new PrefuseObviousView(viz, null, "scatterplot",
        null);
    view.addListener(new PrefuseObviousControl(new ZoomControl()));
    view.addListener(new PrefuseObviousControl(new PanControl()));
    view.addListener(new PrefuseObviousControl(new DragControl()));

    // JFrame...
    JFrame frame = new JFrame("Data model : obvious-ivtk |"
        + " Visualisation : obvious-prefuse | View obvious-prefuse | Polylithic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(view.getViewJComponent());
    frame.pack();
    frame.setVisible(true);
    prefViz.run("draw");

    // Testing the listener mechanism between obvious-ivtk and obvious-prefuse.

    prefViz.cancel("draw");
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
    prefViz.run("draw");
  }
}
