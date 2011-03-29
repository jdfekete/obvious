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

package obvious.demo.viz;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.jung.data.JungObviousNetwork;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisBoost;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import prefuse.Display;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * This demo used as data model for a simple network the obvious-jung
 * implementation. Then, the obvious-prefuse implementation is used for
 * visualization. The standard prefuse implementation displays it, in the
 * current version of this example.
 * This example is based on the example used in the prefuse manual explaining
 * a way to simply visualize graph.
 * @author Hemery
 *
 */
public final class VizJungGraphTest {

  /**
   * Private constructor.
   */
  private VizJungGraphTest() { }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {

    // Creating the example network.
    Schema nodeSchema = new PrefuseObviousSchema();
    Schema edgeSchema = new PrefuseObviousSchema();

    nodeSchema.addColumn("name", String.class, "bob");
    nodeSchema.addColumn("id", int.class, 0);

    edgeSchema.addColumn("source", int.class, 0);
    edgeSchema.addColumn("target", int.class, 0);

    // Creating nodes and edges
    Node node1 = new NodeImpl(nodeSchema, new Object[] {"1", 0});
    Node node2 = new NodeImpl(nodeSchema, new Object[] {"2", 1});
    Node node3 = new NodeImpl(nodeSchema, new Object[] {"3", 2});
    Node node4 = new NodeImpl(nodeSchema, new Object[] {"4", 3});

    Edge edge1 = new EdgeImpl(edgeSchema, new Object[] {0, 1});
    Edge edge2 = new EdgeImpl(edgeSchema, new Object[] {1, 2});
    Edge edge3 = new EdgeImpl(edgeSchema, new Object[] {2, 0});
    Edge edge4 = new EdgeImpl(edgeSchema, new Object[] {3, 1});

    // Building the network
    Network jungNetwork = new JungObviousNetwork(nodeSchema, edgeSchema,
        "source", "target");
    jungNetwork.addNode(node1);
    jungNetwork.addNode(node2);
    jungNetwork.addNode(node3);
    jungNetwork.addNode(node4);

    jungNetwork.addEdge(edge1, node1, node2, Graph.EdgeType.UNDIRECTED);
    jungNetwork.addEdge(edge2, node2, node3, Graph.EdgeType.UNDIRECTED);
    jungNetwork.addEdge(edge3, node3, node1, Graph.EdgeType.UNDIRECTED);
    jungNetwork.addEdge(edge4, node4, node2, Graph.EdgeType.UNDIRECTED);

    // Param for the prefuse visualization (simply group name).
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");

    // Creating the visualization.
    PrefuseObviousVisualization vis = new PrefuseObviousVisBoost(
        jungNetwork, null, null, param);

    // Using label renderer as in the tutorial.
    LabelRenderer r = new LabelRenderer("name");
    r.setRoundedCorner(8, 8); // round the corners
    vis.setRenderer(new PrefuseObviousRenderer(new DefaultRendererFactory(r)));

   // Color for data values.
   ColorAction text = new ColorAction("graph.nodes",
       VisualItem.TEXTCOLOR, ColorLib.gray(0));
   // Color for edges.
   ColorAction edges = new ColorAction("graph.edges",
       VisualItem.STROKECOLOR, ColorLib.gray(200));

   // Creating the prefuse action list.
   ActionList color = new ActionList();
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

  // In order to display, we have to call the underlying prefuse visualization.
  // In a complete version of obvious, we don't need that step.
  prefuse.Visualization prefViz = (prefuse.Visualization)
  vis.getUnderlyingImpl(prefuse.Visualization.class);

  PrefuseObviousView view = new PrefuseObviousView(vis, null, "scatterplot",
      null);
  view.addListener(new PrefuseObviousControl(new ZoomControl()));
  view.addListener(new PrefuseObviousControl(new PanControl()));
  view.addListener(new PrefuseObviousControl(new DragControl()));

  //JFrame...
  JFrame frame = new JFrame("DataModel : Obvious-Jung"
      + " | Visu : Obvious-Prefuse | View : Obvious-Prefuse");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.add(view.getViewJComponent());
  frame.pack();
  frame.setVisible(true);

  prefViz.run("color");
  prefViz.run("layout");

  Node node5 = new NodeImpl(nodeSchema, new Object[] {"5", 4});
  jungNetwork.addNode(node5);
  Edge edge5 = new EdgeImpl(edgeSchema, new Object[] {5, 2});
  jungNetwork.addEdge(edge5, node5, node2, Graph.EdgeType.UNDIRECTED);
  }
}
