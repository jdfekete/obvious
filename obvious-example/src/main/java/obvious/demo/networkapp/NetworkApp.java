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

package obvious.demo.networkapp;

import infovis.DynamicTable;
import infovis.graph.visualization.NodeLinkGraphVisualization;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import obvious.data.Schema;
import obvious.ivtk.view.IvtkObviousView;
import obvious.prefuse.data.PrefuseObviousNetwork;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousNetworkViz;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

/**
 * NetworkApp class.
 * @author Hemery
 *
 */
public final class NetworkApp {

  /**
   * Constructor.
   */
  private NetworkApp() {
  }

  /**
   * Main method.
   * @param args arguments of the main
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

    Schema nodeSchema = new PrefuseObviousSchema();
    nodeSchema.addColumn("name", String.class, "bob");
    nodeSchema.addColumn("gender", String.class, "male");

    // Create the parameter for the visualization.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");
    param.put(PrefuseObviousNetworkViz.LABEL_KEY, "name");

    // Create the obvious-prefuse graph.
    PrefuseObviousNetwork network = new PrefuseObviousNetwork(prefGraph);

    PrefuseObviousVisualization prefVisu = new PrefuseObviousNetworkViz(
        network, null, "test", param);

    prefuse.Visualization realPrefVis = (prefuse.Visualization)
    prefVisu.getUnderlyingImpl(prefuse.Visualization.class);

    PrefuseObviousView prefView = new PrefuseObviousView(
        prefVisu, null, "graph", param);
    prefView.addListener(new PrefuseObviousControl(new ZoomControl()));
    prefView.addListener(new PrefuseObviousControl(new PanControl()));
    prefView.addListener(new PrefuseObviousControl(new DragControl()));

    Map<String, Object> ivtkParam = new HashMap<String, Object>();
    ivtkParam.put("LABEL_COLUMN", "name");
    ivtkParam.put("DEFAULT_SIZE", 0);

    IvtkNodeLinkGraphVisLabel ivtkVis = new IvtkNodeLinkGraphVisLabel(network, null, null, ivtkParam);
    
    IvtkObviousView ivtkView = new IvtkObviousView(
        ivtkVis, null, "network", param);

    infovis.panel.VisualizationPanel debugPanel
      = (infovis.panel.VisualizationPanel)
      ivtkView.getUnderlyingImpl(infovis.panel.VisualizationPanel.class);

    NodeLinkGraphVisualization debugView = (NodeLinkGraphVisualization)
        debugPanel.getVisualization();

    DynamicTable nodeTable = debugView.getVertexTable();

    debugView.setVisualColumn(
        infovis.Visualization.VISUAL_LABEL, nodeTable.getColumn("name"));

    /*
    ControlPanel control = ControlPanelFactory
    .createControlPanel(debugView);

    JFrame debugFrame = new JFrame("debugIvtk graph struct");
    JSplitPane split = ControlPanelFactory
    .createScrollVisualization(control);
    split.setResizeWeight(1.0);
    debugFrame.getContentPane().add(split);
    debugFrame.pack();
    debugFrame.setVisible(true);
    */

    JSplitPane viewPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    viewPane.add(prefView.getViewJComponent(), 0);
    viewPane.add(new JScrollPane(ivtkView.getViewJComponent()), 1);
    final int height = 800;
    final int width = 640;
    Dimension viewDim = new Dimension(height, width);
    viewPane.setMinimumSize(viewDim);
    viewPane.setPreferredSize(viewDim);


    JSplitPane globalPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    globalPane.add(viewPane, 0);

    JFrame frame = new JFrame("Obvious : network demonstrator");
    JPanel networkControlPanel = new NetworkControlPanel(
        frame, network, nodeSchema, realPrefVis, ivtkView.getViewJComponent());
    globalPane.add(networkControlPanel, 1);
    final int controlwidth = 80;
    Dimension controlDim = new Dimension(height, controlwidth);
    networkControlPanel.setMinimumSize(controlDim);
    networkControlPanel.setPreferredSize(controlDim);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(globalPane);
    frame.pack();
    frame.setVisible(true);


    realPrefVis.run("color");  // assign the colors
    realPrefVis.run("layout"); // start up the animated layout

  }

}
