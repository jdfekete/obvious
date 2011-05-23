/*
* Copyright (c) 2010, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package obvious.prefuse.viz.util;

import java.awt.geom.Rectangle2D;
import java.util.Map;

import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import prefuse.Constants;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.AxisLabelLayout;
import prefuse.action.layout.AxisLayout;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * This class is a specialized Obvious visualization for {@link Table Table}
 * based on the scatter-plot visualization introduced in the Prefuse Toolkit.
 * This class used extra-parameters to configure the visualization. Thus, it
 * is possible to specify the X and Y axis of the scatter-plot by adding the
 * name of those axis for the key described by X_AXIS and Y_AXIS variables
 * introduced in {@link obvious.viz.Visualization Visualization}. Parameters
 * defined in {@link PrefuseObviousVisualization PrefuseObviousVisualization}
 * should also be considered: mainly if the developer wants to use a custom
 * prefuse group name ("default" by default for this visualization).
 * @see PrefuseObviousVisualization
 * @see obvious.viz.Visualization
 * @author Hemery
 *
 */
public class PrefuseScatterPlotViz extends PrefuseObviousVisualization {

  /**
   * Default group name.
   */
  public static final String DEF_NAME = "default";

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
    prefuse.Visualization prefVis = new prefuse.Visualization();
    this.setPrefVisualization(prefVis);
    groupName = DEF_NAME;
    if  (param != null && param.containsKey(GROUP_NAME)) {
      /*
      this.getPrefVisualization().add((String) param.get(GROUP_NAME),
          getPrefuseTable());*/
      groupName = (String) param.get(GROUP_NAME);
    }
    this.getPrefVisualization().add(groupName, getPrefuseTable());
    ShapeRenderer mShapeR = new ShapeRenderer(2);
    DefaultRendererFactory rf = new DefaultRendererFactory(mShapeR);
    this.setRenderer(new PrefuseObviousRenderer(rf));

    AxisLayout xAxis = new AxisLayout(groupName,
        (String) param.get(X_AXIS), Constants.X_AXIS, VisiblePredicate.TRUE);
    this.putAction("x", new PrefuseObviousAction(xAxis));
    AxisLayout yAxis = new AxisLayout(groupName,
        (String) param.get(Y_AXIS), Constants.Y_AXIS, VisiblePredicate.TRUE);
    this.putAction("y", new PrefuseObviousAction(yAxis));
    final int labelDim = 15;
    AxisLabelLayout xlabels = new AxisLabelLayout("xlabel", xAxis,
        new Rectangle2D.Double(), labelDim);
    this.putAction("xlabel", new PrefuseObviousAction(xlabels));
    AxisLabelLayout ylabels = new AxisLabelLayout("ylabel", yAxis,
        new Rectangle2D.Double(), labelDim);
    this.putAction("ylabel", new PrefuseObviousAction(ylabels));
    final int rgb1 = 100, rgb2 = 255;
    ColorAction color = new ColorAction(groupName,
        VisualItem.STROKECOLOR, ColorLib.rgb(rgb1, rgb1, rgb2));
    this.putAction("color", new PrefuseObviousAction(color));
    DataShapeAction shape = new DataShapeAction(groupName,
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
