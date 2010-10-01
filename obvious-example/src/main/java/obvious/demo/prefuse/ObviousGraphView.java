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

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import obvious.data.Network;
import obvious.prefuse.PrefuseObviousNetwork;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousNetworkViz;
import obvious.viz.Visualization;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.util.GraphLib;
import prefuse.data.Graph;
import prefuse.demos.GraphView.GraphMenuAction;

/**
 * Adaptation of the TreeView demo from prefuse and written by Jeffrey Heer.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class ObviousGraphView extends JPanel {

  private static final String graph = "graph";
  private static final String nodes = "graph.nodes";
  private static final String edges = "graph.edges";

  private static Visualization vis;

  /**
   * Constructor.
   */
  public ObviousGraphView() {

    /*
     * Creation of the graph structure (generated with prefuse then wrapped
     * with obvious.
     */
    prefuse.data.Graph prefGraph = GraphLib.getGrid(10, 10);
    Network network = new PrefuseObviousNetwork(prefGraph);
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");
    param.put(PrefuseObviousNetworkViz.LABEL_KEY, "label");

    /*
     * Creating the visualization.
     */
    vis = new PrefuseObviousNetworkViz(network, null, "test", param);

    /*
     * Building view.
     */


    PrefuseObviousView prefView = new PrefuseObviousView(
        vis, null, "graph", param);
    prefView.addListener(new PrefuseObviousControl(new ZoomControl()));
    prefView.addListener(new PrefuseObviousControl(new PanControl()));
    prefView.addListener(new PrefuseObviousControl(new DragControl()));
    this.add(prefView.getViewJComponent());
  }

  public void setGraph(Network network) {
    ((PrefuseObviousVisualization) vis).clearVisualization();
    ((PrefuseObviousVisualization) vis).setVisualizationData(network);
    prefuse.Visualization realPrefVis = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    realPrefVis.run("color");
    realPrefVis.run("layout");
    
  }


  public static JFrame demo(ObviousGraphView view) {
    JMenu dataMenu = new JMenu("Data");

    dataMenu.add(new LoadAction("Clique", view) {
      @Override
      public Graph getGraph() {
        return GraphLib.getClique(10);
      }
    });
    dataMenu.add(new LoadAction("Grid", view) {
      @Override
      public Graph getGraph() {
        return GraphLib.getGrid(10, 10);
      }
    });
    dataMenu.add(new LoadAction("HoneyComb", view) {
      @Override
      public Graph getGraph() {
        return GraphLib.getHoneycomb(5);
      }
    });
    dataMenu.add(new LoadAction("BalancedTree", view) {
      @Override
      public Graph getGraph() {
        return GraphLib.getBalancedTree(3, 5);
      }
    });
    dataMenu.add(new LoadAction("DiamondTree", view) {
      @Override
      public Graph getGraph() {
        return GraphLib.getDiamondTree(3, 3, 5);
      }
    });

    JMenuBar menubar = new JMenuBar();
    menubar.add(dataMenu);
    JFrame frame = new JFrame("GraphView powered by Obvious");
    frame.setJMenuBar(menubar);
    frame.setContentPane(view);
    frame.pack();
    frame.setVisible(true);
    return frame;
  }

  public static void main(String[] args) {
    JFrame frame = demo(new ObviousGraphView());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    prefuse.Visualization realPrefVis = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    realPrefVis.run("color");  // assign the colors
    realPrefVis.run("layout"); // start up the animated layout
  }

  public static abstract class LoadAction extends AbstractAction {

    private ObviousGraphView oView;

    public LoadAction(String name, ObviousGraphView v) {
      this.oView = v;
      this.putValue(AbstractAction.NAME, name);
    }

    public void actionPerformed(ActionEvent e) {
      oView.setGraph(new PrefuseObviousNetwork(getGraph()));
    }

    public abstract Graph getGraph();

  }

}
