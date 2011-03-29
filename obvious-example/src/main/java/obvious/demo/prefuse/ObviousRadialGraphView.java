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

package obvious.demo.prefuse;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Network;
import obvious.prefuse.data.PrefuseObviousNetwork;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.view.JView;
import obvious.viz.Visualization;
import prefuse.Constants;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.demos.RadialGraphView.NodeColorAction;
import prefuse.demos.RadialGraphView.TextColorAction;
import prefuse.demos.RadialGraphView.TreeRootAction;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * Adaptation of the TreeView demo from prefuse and written by Jeffrey Heer.
 * We use obvious-prefuse implementation in a polylithic way. That's why
 * prefuse code from the original radial graph view is directly reused.
 * @author Hemery
 *
 */
public class ObviousRadialGraphView {

  /**
   * Obvious view.
   */
  private JView view;

  /**
   * Obvious visualization.
   */
  private Visualization vis;

  /**
   * Graph group in prefuse.
   */
  private static String graph = "graph";

  /**
   * Nodes group in prefuse.
   */
  private static String nodes = "graph.nodes";

  /**
   * Edges group in prefuse.
   */
  private static String edges = "graph.edges";

  /**
   * Constructor.
   * @param network obvious network to visualize.
   */
  public ObviousRadialGraphView(Network network) {
    //Creation of the visualization
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, graph);
    vis = new PrefuseObviousVisualization(network, null, null,
        param);

    // Setting the renderers.
    prefuse.render.LabelRenderer nodeRenderer = new LabelRenderer("name");
    nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
    nodeRenderer.setHorizontalAlignment(Constants.CENTER);
    nodeRenderer.setRoundedCorner(8,8);
    prefuse.render.EdgeRenderer edgeRenderer = new EdgeRenderer();

    DefaultRendererFactory rf = new DefaultRendererFactory(nodeRenderer);
    rf.add(new InGroupPredicate(edges), edgeRenderer);
    vis.setRenderer(new PrefuseObviousRenderer(rf));

    // Setting actions.
    // colors
    ItemAction nodeColor = new NodeColorAction(nodes);
    ItemAction textColor = new TextColorAction(nodes);
    vis.putAction("textColor", new PrefuseObviousAction(textColor));

    ItemAction edgeColor = new ColorAction(edges,
            VisualItem.STROKECOLOR, ColorLib.rgb(200,200,200));

    FontAction fonts = new FontAction(nodes,
            FontLib.getFont("Tahoma", 10));
    fonts.add("ingroup('_focus_')", FontLib.getFont("Tahoma", 11));

    // recolor
    ActionList recolor = new ActionList();
    recolor.add(nodeColor);
    recolor.add(textColor);
    vis.putAction("recolor", new PrefuseObviousAction(recolor));

    // repaint
    ActionList repaint = new ActionList();
    repaint.add(recolor);
    repaint.add(new RepaintAction());
    vis.putAction("repaint", new PrefuseObviousAction(repaint));

    // animate paint change
    ActionList animatePaint = new ActionList(400);
    animatePaint.add(new ColorAnimator(nodes));
    animatePaint.add(new RepaintAction());
    vis.putAction("animatePaint", new PrefuseObviousAction(animatePaint));

    // create the tree layout action
    RadialTreeLayout treeLayout = new RadialTreeLayout(graph);
    vis.putAction("graphLayout", new PrefuseObviousAction(treeLayout));

    CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(graph);
    vis.putAction("subLayout", new PrefuseObviousAction(subLayout));

    // create the filtering and layout
    ActionList filter = new ActionList();
    filter.add(new TreeRootAction(graph));
    filter.add(fonts);
    filter.add(treeLayout);
    filter.add(subLayout);
    filter.add(textColor);
    filter.add(nodeColor);
    filter.add(edgeColor);
    vis.putAction("filter", new PrefuseObviousAction(filter));

    // animated transition
    ActionList animate = new ActionList(1250);
    animate.setPacingFunction(new SlowInSlowOutPacer());
    animate.add(new QualityControlAnimator());
    animate.add(new VisibilityAnimator(graph));
    animate.add(new PolarLocationAnimator(nodes, "linear"));
    animate.add(new ColorAnimator(nodes));
    animate.add(new RepaintAction());
    vis.putAction("animate", new PrefuseObviousAction(animate));

    view = new PrefuseObviousView(vis, null, null, null);
    view.addListener(new PrefuseObviousControl(new DragControl()));
    view.addListener(new PrefuseObviousControl(new ZoomControl()));
    view.addListener(new PrefuseObviousControl(new PanControl()));


  }

  /**
   * Gets the obvious view.
   * @return obvious view
   */
  public JView getObviousView() {
    return view;
  }

  /**
   * Runs the visualization.
   */
  public void run() {
    prefuse.Visualization realPrefVis = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    realPrefVis.run("filter");
    realPrefVis.run("animate");
  }

  /**
   * Main function.
   * @param args arguments.
   */
  public static void main(String[] args) {
    // Create the prefuse graph.
    prefuse.data.Graph prefGraph = null;
    try {
      prefGraph = new GraphMLReader().readGraph(
          "src/main/resources/socialnet.xml");
    } catch (DataIOException e) {
      e.printStackTrace();
      System.err.println("Error loading graph. Exiting...");
      System.exit(1);
    }

    Network network = new PrefuseObviousNetwork(prefGraph);
    ObviousRadialGraphView radialView = new ObviousRadialGraphView(network);
    JFrame frame = new JFrame("RadialGraphView powered by obvious");
    frame.setContentPane(radialView.getObviousView().getViewJComponent());
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    radialView.run();
  }

}
