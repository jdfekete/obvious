package obvious.prefuse.viz.util;

import java.util.Map;

import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import prefuse.Constants;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.AxisLayout;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * PrefuseScatterPlotViz class.
 * @author Hemery
 *
 */
public class PrefuseScatterPlotViz extends PrefuseObviousVisualization {

  /**
   * X field key.
   */
  public static final String X_AXIS = "x";

  /**
   * Y field key.
   */
  public static final String Y_AXIS = "y";

  /**
   * Shape field key.
   */
  public static final String SHAPE = "shape";

  /**
   * Constructor.
   * @param parentTable an Obvious Table
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization
   * null if custom
   */
  public PrefuseScatterPlotViz(Table parentTable, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(parentTable, predicate, visName, param);
  }

  @Override
  protected void initVisualization(Map<String, Object> param) {
    this.setPrefVisualization(new prefuse.Visualization());
    this.getPrefVisualization().add((String) param.get(GROUP_NAME),
        getPrefuseTable());

    ShapeRenderer mShapeR = new ShapeRenderer(2);
    DefaultRendererFactory rf = new DefaultRendererFactory(mShapeR);
    this.setRenderer(new PrefuseObviousRenderer(rf));

    AxisLayout xAxis = new AxisLayout((String) param.get(GROUP_NAME),
        (String) param.get(X_AXIS), Constants.X_AXIS, VisiblePredicate.TRUE);
    this.putAction("x", new PrefuseObviousAction(xAxis));
    AxisLayout yAxis = new AxisLayout((String) param.get(GROUP_NAME),
        (String) param.get(Y_AXIS), Constants.Y_AXIS, VisiblePredicate.TRUE);
    this.putAction("y", new PrefuseObviousAction(yAxis));
    ColorAction color = new ColorAction((String) param.get(GROUP_NAME),
        VisualItem.STROKECOLOR, ColorLib.rgb(100,100,255));
    this.putAction("color", new PrefuseObviousAction(color));
    DataShapeAction shape = new DataShapeAction((String) param.get(GROUP_NAME),
        (String) param.get(SHAPE));
    this.putAction("shape", new PrefuseObviousAction(shape));

    ActionList draw = new ActionList();
    draw.add(xAxis);
    draw.add(yAxis);
    if (param.get(SHAPE) != null) {
        draw.add(shape);
    }
    draw.add(color);
    draw.add(new RepaintAction());
    this.putAction("draw", new PrefuseObviousAction(draw));
  }

}
