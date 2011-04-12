package obvious.demo.mix;

import java.io.File;

import javax.swing.JFrame;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisBoost;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.viz.Visualization;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;
import prefuse.Constants;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.AxisLayout;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
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
public final class IvtkTableAndPrefuseScatterplotDemo {

  /**
   * Private constructor.
   */
  private IvtkTableAndPrefuseScatterplotDemo() { }

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
    "obvious.ivtk.data.IvtkDataFactory");

    // Creating an Obvious CSV reader and loading an Obvious table
    CSVImport csv = new CSVImport(new File(
      "src//main//resources//articlecombinedexample.csv"), ',');
    Table table = csv.loadTable();

    // Building the visulalization
    Visualization viz = new PrefuseObviousVisBoost(table, null, null,
        null);
    // We define all useful field and paramaters (such as group name).
    String xfield = "id";
    String yfield = "age";
    String sfield = "category";
    String group = "tupleset";
    ShapeRenderer mshapeR = new ShapeRenderer(2);

    // Setting the default renderer factory then adding it to obvious vis.
    DefaultRendererFactory rf = new DefaultRendererFactory(mshapeR);
    viz.setRenderer(new PrefuseObviousRenderer(rf));

    // Layout definition then adding it to obvious visualization.
    AxisLayout xAxis = new AxisLayout(group, xfield,
        Constants.X_AXIS, VisiblePredicate.TRUE);
    viz.putAction("x", new PrefuseObviousAction(xAxis));

    AxisLayout yAxis = new AxisLayout(group, yfield,
        Constants.Y_AXIS, VisiblePredicate.TRUE);
    viz.putAction("y", new PrefuseObviousAction(yAxis));

    final int color1 = 100, color2 = 255;
    ColorAction color = new ColorAction(group,
        VisualItem.STROKECOLOR, ColorLib.rgb(color1, color1, color2));
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
        + " Visualisation : obvious-prefuse"
        + "| View obvious-prefuse | Polylithic");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(view.getViewJComponent());
    frame.pack();
    frame.setVisible(true);
    prefViz.run("draw");

  }
}
