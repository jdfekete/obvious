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

import javax.swing.JFrame;

import obvious.data.Network;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkNodeLinkGraphVis;
import obvious.view.JView;
import obvious.view.control.PanControl;
import obvious.view.control.ZoomControl;
import obvious.viz.Visualization;

/**
 * Simple test based on an ivtk NodeLinKGraph Visualization.
 * @author Hemery
 *
 */
public final class IvtkNodeLinkGraph {

  /**
   * Private constructor.
   */
  private IvtkNodeLinkGraph() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    // Creating the example network.
    final int gridDim = 10;
    infovis.Graph graph = infovis.graph.Algorithms.getGridGraph(
        gridDim, gridDim);
    Network ivtkNetwork = new IvtkObviousNetwork(graph);

    Visualization vis = new IvtkNodeLinkGraphVis(ivtkNetwork, null, null, null);
    JView view = new IvtkObviousView(vis, null, "network", null);
    PanControl control = new PanControl(view);
    ZoomControl zoomcontrol = new ZoomControl(view);
    view.getViewJComponent().addMouseListener(control);
    view.getViewJComponent().addMouseMotionListener(control);
    view.getViewJComponent().addMouseListener(zoomcontrol);
    view.getViewJComponent().addMouseMotionListener(zoomcontrol);
    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final int dim = 500;
    frame.setSize(dim, dim);
    frame.getContentPane().add(view.getViewJComponent());
    frame.setVisible(true);
  }
}
