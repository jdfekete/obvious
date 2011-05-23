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

import java.util.Map;

import obvious.data.Network;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.Constants;

/**
 * This class is a specialized Obvious visualization for
 * {@link Network Network} based on the scatter-plot visualization introduced
 * in the Prefuse Toolkit.
 * This class do not use extra-parameters to configure the visualization.
 * However, parameters defined in
 * {@link PrefuseObviousVisualization PrefuseObviousVisualization}
 * should also be considered: mainly if the developer wants to use a custom
 * prefuse group name ("graph" by default for this visualization).
 * @see PrefuseObviousVisualization
 * @see obvious.viz.Visualization
 * @author Hemery
 *
 */
public class PrefuseObviousNetworkViz extends PrefuseObviousVisualization {

  /**
   * Constructor.
   * @param parentNetwork parent network
   * @param predicate a predicate
   * @param visName visualization technique name
   * @param param parameters
   */
  public PrefuseObviousNetworkViz(Network parentNetwork,
      Predicate predicate, String visName, Map<String, Object> param) {
    super(parentNetwork, predicate, visName, param);
  }

  @Override
  protected void initVisualization(Map<String, Object> param) {
    prefuse.Visualization prefVis = new prefuse.Visualization();
    groupName = "graph";
    String label = "name";
    if (param != null) {
      if (param.containsKey(GROUP_NAME)) {
        groupName = (String) param.get(GROUP_NAME);
      }
      if (param.containsKey(LABEL_KEY)) {
        label = (String) param.get(LABEL_KEY);
      }
    }
    this.setPrefVisualization(prefVis);
    this.getPrefVisualization().add(groupName, getPrefuseNetwork());
    LabelRenderer r = new LabelRenderer(label);
    final int cornerRoundDim = 8;
    r.setRoundedCorner(cornerRoundDim, cornerRoundDim); // round the corners
    this.setRenderer(new PrefuseObviousRenderer(
        new DefaultRendererFactory(r)));
    // Color for data values.
    final int rgb1 = 255, rgb2 = 180, rgb3 = 190;
    int[] palette = new int[] {
        ColorLib.rgb(rgb1, rgb2, rgb2), ColorLib.rgb(rgb3, rgb3, rgb1)
    };
    DataColorAction fill = new DataColorAction(groupName + ".nodes", label,
        Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
    ColorAction text = new ColorAction(groupName + ".nodes",
        VisualItem.TEXTCOLOR, ColorLib.gray(0));
    // Color for edges
    final int edgeGrayLevel = 200;
    ColorAction edges = new ColorAction(groupName + ".edges",
        VisualItem.STROKECOLOR, ColorLib.gray(edgeGrayLevel));

    // Creating the prefuse action list.
    ActionList color = new ActionList();
    color.add(fill);
    color.add(text);
    color.add(edges);
    // Wrapping the action list around obvious
    this.putAction("color", new PrefuseObviousAction(color));

    // Creating a Directed force Layout.
    ActionList layout = new ActionList(Activity.INFINITY);
    layout.add(new ForceDirectedLayout(groupName));
    layout.add(new RepaintAction());
    // Wrapping the layout around obvious.
    this.putAction("layout", new PrefuseObviousAction(layout));
  }

}
