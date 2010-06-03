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

import infovis.panel.VisualizationPanel;
import infovis.tree.DefaultTree;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Tree;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.ivtk.data.IvtkObviousTree;
import obvious.ivtk.viz.util.IvtkNodeLinkGraphVis;
import obvious.ivtk.viz.util.IvtkNodeLinkTreeVis;
import obvious.viz.Visualization;

/**
 * Simple test based on an ivtk NodeLinKGraph Visualization.
 * @author Hemery
 *
 */
public final class IvtkNodeLinkTree {

  /**
   * Private constructor.
   */
  private IvtkNodeLinkTree() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    // Creating the example network.
    infovis.Tree t = new DefaultTree();
    Tree<Node, Edge> ivtkTree = new IvtkObviousTree(t); 

    Visualization vis = new IvtkNodeLinkTreeVis(ivtkTree, null, null, null);

    infovis.Visualization ivtkVis = (infovis.Visualization)
    vis.getUnderlyingImpl(infovis.Visualization.class);
    VisualizationPanel panel = new VisualizationPanel(ivtkVis);
    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.getContentPane().add(panel);
    frame.setVisible(true);
  }
}