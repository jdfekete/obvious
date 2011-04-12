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

package obvious.demo.mix;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Schema;
import obvious.impl.NodeImpl;
import obvious.prefuse.data.PrefuseObviousNetwork;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisBoost;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousNetworkViz;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;

import prefuse.Constants;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * Simple test for a graph visualization with obvious-prefuse.
 * Obvious-prefuse implementation is used for data model and visualization.
 * Prefuse standard implementation is used to display.
 * This demo is based on the example of the prefuse manual and the data file
 * comes from it too.
 * @author Hemery
 *
 */
public final class VizGraphTest {

  /**
   * Private constructor.
   */
  private VizGraphTest() { }

  /**
   * Main method.
   * @param args args
   */
  public static void main(final String[] args) {

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

    // Create the parameter for the visualization.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");
    param.put(PrefuseObviousNetworkViz.LABEL_KEY, "name");

    // Create the obvious-prefuse graph.
    PrefuseObviousNetwork network = new PrefuseObviousNetwork(prefGraph);

    // Create the obvious-prefuse visualization.
    PrefuseObviousVisualization vis = new PrefuseObviousVisBoost(
        network, null, null, param);

    // Using label renderer as in the tutorial of prefuse.
    LabelRenderer r = new LabelRenderer("name");
    r.setRoundedCorner(8, 8); // round the corners
    vis.setRenderer(new PrefuseObviousRenderer(new DefaultRendererFactory(r)));

    // Color for data values.
   int[] palette = new int[] {
       ColorLib.rgb(255,180,180), ColorLib.rgb(190,190,255)
   };
   DataColorAction fill = new DataColorAction("graph.nodes", "gender",
       Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
   ColorAction text = new ColorAction("graph.nodes",
       VisualItem.TEXTCOLOR, ColorLib.gray(0));
   // Color for edges
   ColorAction edges = new ColorAction("graph.edges",
       VisualItem.STROKECOLOR, ColorLib.gray(200));

   // Creating the prefuse action list.
   ActionList color = new ActionList();
   color.add(fill);
   color.add(text);
   color.add(edges);
   // Wrapping the action list around obvious
   vis.putAction("color", new PrefuseObviousAction(color));


   // Creating a Directed force Layout.
  ActionList layout = new ActionList(Activity.INFINITY);
  layout.add(new ForceDirectedLayout("graph"));
  layout.add(new RepaintAction());
  // Wrapping the layout around obvious.
  vis.putAction("layout", new PrefuseObviousAction(layout));


  

  /*
  PrefuseObviousVisualization vis = new PrefuseObviousNetworkViz(network, null, "test", param);
  */
  // In order to display, we have to call the underlying prefuse visualization.
  // In a complete version of obvious, we don't need that step.
  prefuse.Visualization prefViz = (prefuse.Visualization)
  vis.getUnderlyingImpl(prefuse.Visualization.class);
  
  PrefuseObviousView view = new PrefuseObviousView(vis, null, "scatterplot", null);
  //view.addListener(new PrefuseObviousControl(new ZoomControl()));
  //view.addListener(new PrefuseObviousControl(new PanControl()));
  //view.addListener(new PrefuseObviousControl(new DragControl()));
  obvious.view.control.PanControl control = new obvious.view.control.PanControl(view);
  obvious.view.control.ZoomControl zoomcontrol = new obvious.view.control.ZoomControl(view);
  view.getViewJComponent().addMouseListener(control);
  view.getViewJComponent().addMouseMotionListener(control);
  view.getViewJComponent().addMouseListener(zoomcontrol);
  view.getViewJComponent().addMouseMotionListener(zoomcontrol);
  //create a new window to hold the visualization
  JFrame frame = new JFrame("DataModel : Obvious-prefuse"
      + " | Visu : Obvious-Prefuse | View : Obvious-Prefuse");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.add(view.getViewJComponent());
  frame.pack();
  frame.setVisible(true);

  prefViz.run("color");  // assign the colors
  prefViz.run("layout"); // start up the animated layout

  Schema nodeSchema = new PrefuseObviousSchema();
  nodeSchema.addColumn("name", String.class, "bob");
  nodeSchema.addColumn("gender", String.class, "male");
  network.addNode(new NodeImpl(nodeSchema, new Object[] {"marc", "male"}));
  }

}
