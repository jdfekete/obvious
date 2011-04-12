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

package obvious.demo.misc;

import infovis.panel.VisualizationPanel;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.ivtk.data.IvtkObviousTree;
import obvious.ivtk.viz.util.IvtkNodeLinkTreeVis;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.viz.Visualization;

/**
 * This class shows an Obvious example based on the IVTK binding. It
 * illustrates how visualizing a wrapped IVTK tree with Obvious. The default
 * layout of IVTK for tree is used.
 *
 * @author Hemery
 *
 */
public final class IvtkTreeAndIvtkTreeVisDemo {

  /**
   * Private constructor.
   */
  private IvtkTreeAndIvtkTreeVisDemo() {
  }

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
    Edge edge4 = new EdgeImpl(edgeSchema, new Object[] {3, 1});

    Tree<Node, Edge> ivtkTree = new IvtkObviousTree(nodeSchema, edgeSchema);

    ivtkTree.addNode(node1);
    ivtkTree.addNode(node2);
    ivtkTree.addNode(node3);
    ivtkTree.addNode(node4);
    ivtkTree.addEdge(edge1, node1, node2, Graph.EdgeType.DIRECTED);
    ivtkTree.addEdge(edge2, node2, node3, Graph.EdgeType.DIRECTED);
    ivtkTree.addEdge(edge4, node2, node4, Graph.EdgeType.DIRECTED);

    Visualization vis = new IvtkNodeLinkTreeVis(ivtkTree, null, null, null);


    infovis.Visualization ivtkVis = (infovis.Visualization)
    vis.getUnderlyingImpl(infovis.Visualization.class);
    VisualizationPanel panel = new VisualizationPanel(ivtkVis);

    //JView view = new IvtkObviousView(vis, null, "tree", null);

    final int dim = 500;
    JFrame frame = new JFrame("EXAMPLE");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(dim, dim);
    frame.getContentPane().add(panel);
    frame.setVisible(true);
  }
}
