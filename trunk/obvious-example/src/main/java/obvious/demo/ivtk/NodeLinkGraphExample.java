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

package obvious.demo.ivtk;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import infovis.graph.Algorithms;
import obvious.data.Network;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.IvtkVisualizationFactory;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * NodeLinkGraph example for obvious-ivtk.
 * @author Hemery
 *
 */
public class NodeLinkGraphExample {

    /**
     * An obvious JView.
     */
    private JView view;

    /**
     * Constructor.
     */
    public NodeLinkGraphExample() {
        /*
         * Creates the graph structure.
         */
        infovis.Graph g = Algorithms.getGridGraph(10, 10);
        Network network = new IvtkObviousNetwork(g);

        /*
         * Creates the associated visualization.
         */
        Visualization vis = new IvtkVisualizationFactory().createVisualization(
                network, null, "network", null);

        /*
         * Creates the view.
         */
        view = new IvtkObviousView(vis, null, null, null);
    }

    /**
     * Demo method.
     */
    public static void demo() {
      NodeLinkGraphExample nodeLink = new NodeLinkGraphExample();
      JFrame frame = new JFrame("NodeLinkGraph example from ivtk based"
              + " on obvious");
      JScrollPane panel = new JScrollPane(
              nodeLink.getJView().getViewJComponent());
      frame.add(panel);
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
    }

    /**
     * Gets the view associated to the NodeLinkGraph visualization.
     * @return
     */
    public JView getJView() {
        return this.view;
    }

    /**
     * Main method.
     * @param args arguments of the main
     */
    public static void main(String[] args) {
        demo();
    }
}
