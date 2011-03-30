/*
* Copyright (c) 2011, INRIA
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

package obvious.demo.paper;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.IvtkVisualizationFactory;
import obvious.view.JView;
import obvious.viz.Visualization;
import obviousx.ObviousxException;

/**
 * Creation of an Obvious network visualization.
 * This is an example from evaluation section of the paper (listing 1).
 * @author Hemery
 *
 */
public final class NetworkVisualizationArticleExample {

  /**
   * Constructor.
   */
  private NetworkVisualizationArticleExample() {
  }

  /**
   * Main method.
   * @param args arguments of the main
   * @throws ObviousxException if something bad happens
   * @throws ObviousException if something else bad happens
   */
  public static void main(final String[] args) throws ObviousxException,
      ObviousException {

    final int gridDim = 10;
    /*
     * Creation of the graph structure.
     */
    System.setProperty("obvious.DataFactory",
        "obvious.ivtk.data.IvtkDataFactory");
    infovis .Graph g = infovis.graph.Algorithms.getGridGraph(gridDim, gridDim);
    DataFactory factory = DataFactory.getInstance();
    Network network = factory.wrapGraph(g);

    // Creates the associated visualization using the
    // factory for visualization . No predicate and extra
    // parameters are given to the constructor .
    Visualization vis = new IvtkVisualizationFactory().createVisualization(
        network, null , "network", null);

    // Creates the view. No predicates and extra parameters are given to
    // the constructor .
    JView view = new IvtkObviousView(vis, null , "graphview", null);
    // Standard Java window creation
    JFrame frame = new JFrame("Network visualization (article example)");
    JScrollPane panel = new JScrollPane(view.getViewJComponent());
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);

  }

}
