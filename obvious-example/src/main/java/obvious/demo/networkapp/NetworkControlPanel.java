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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;

/**
 * NetworkControlPanel class.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class NetworkControlPanel extends JPanel implements ActionListener {

  /**
   * Add button.
   */
  private JButton addNodeButton;

  /**
   * Remove node button.
   */
  private JButton removeNodeButton;

  /**
   * Refresh view button.
   */
  private JButton refreshViewButton;

  /**
   * Current frame.
   */
  private JFrame frame;

  /**
   * Obvious network.
   */
  private Network network;

  /**
   * Schema used for the node.
   */
  private Schema nodeSchema;

  /**
   * Underlying prefuse visualization.
   */
  private prefuse.Visualization prefViz;

  /**
   * Underlying infovis view.
   */
  private JComponent ivtkview;

  /**
   * Field dimension for the interface.
   */
  private static final Dimension FIELD_SIZE = new Dimension(150, 100);

  /**
   * Constructor.
   * @param inFrame current frame
   * @param inNetwork obvious network
   * @param inSchema schema used for the node
   * @param inPrefViz underlying prefuse visualization
   * @param inIvtkView ivtk view
   */
  public NetworkControlPanel(JFrame inFrame, Network inNetwork, Schema inSchema,
      prefuse.Visualization inPrefViz, JComponent inIvtkView) {
    super(new BorderLayout());
    this.frame = inFrame;
    this.nodeSchema = inSchema;
    this.network = inNetwork;
    this.prefViz = inPrefViz;
    this.ivtkview = inIvtkView;
    addNodeButton = new JButton("+");
    addNodeButton.addActionListener(this);
    refreshViewButton = new JButton("refresh");
    refreshViewButton.addActionListener(this);
    removeNodeButton = new JButton("-");
    removeNodeButton.addActionListener(this);
    JPanel buttonPane = new JPanel();
    buttonPane.add(addNodeButton, 0);
    buttonPane.add(refreshViewButton, 1);
    buttonPane.add(removeNodeButton, 2);
    this.add(buttonPane, BorderLayout.CENTER);
    final int borderParam = 20;
    setBorder(BorderFactory.createEmptyBorder(borderParam, borderParam,
        borderParam, borderParam));
  }

  /**
   * Calls when associated buttons are selected.
   * @param e event
   */
  public void actionPerformed(final ActionEvent e) {
    new Thread(new Runnable() {
      public void run() {
    if (e.getSource() == addNodeButton) {
      final JDialog dialog = new JDialog(frame,
          "Add a node and/or an edge", true);
      JPanel nodePanel = new JPanel();
      JPanel edgePanel = new JPanel();
      JLabel nodeLabel = new JLabel("  New node name  ");
      JLabel edgeLabel = new JLabel(" Select node to link with an edge");
      final JTextField addNodeField = new JTextField(20);
      final JComboBox nodeList1 = new JComboBox(network.getNodes().toArray());
      nodeList1.setRenderer(new NodeCellRenderer("name"));
      final JComboBox nodeList2 = new JComboBox(network.getNodes().toArray());
      nodeList2.setRenderer(new NodeCellRenderer("name"));
      addNodeField.setSize(FIELD_SIZE);
      JButton okNodeButton = new JButton("Add node");
      final infovis.panel.VisualizationPanel panel =
        (infovis.panel.VisualizationPanel) ivtkview;
      okNodeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              String nodeName = addNodeField.getText();
              prefViz.cancel("color");
              prefViz.cancel("layout");
              network.addNode(
                  new NodeImpl(nodeSchema, new Object[] {nodeName, "male"}));
              panel.invalidate();
              panel.repaint();
              panel.getVisualization().invalidate();
              panel.getVisualization().repaint();
              prefViz.run("color");
              prefViz.run("layout");
              dialog.dispose();
            }
          });
        }
      });
      JButton okEdgeButton = new JButton("Add edge");
      okEdgeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              Node node1 = (Node) nodeList1.getSelectedObjects()[0];
              Node node2 = (Node) nodeList2.getSelectedObjects()[0];
              System.out.println(node1.getRow() + " " + node2.getRow());
              Edge newEdge = new EdgeImpl(
                  network.getEdges().iterator().next().getSchema(),
                  new Object[] {node1.getRow(), node2.getRow()});
              prefViz.cancel("color");
              prefViz.cancel("layout");
              network.addEdge(newEdge, node1, node2, Graph.EdgeType.UNDIRECTED);
              prefViz.run("color");
              prefViz.run("layout");
              panel.getVisualization().invalidate();
              panel.getVisualization().repaint();
              dialog.dispose();
            }
          });
        }
      });
      nodePanel.add(nodeLabel, 0);
      nodePanel.add(addNodeField, 1);
      nodePanel.add(okNodeButton, 2);
      edgePanel.add(edgeLabel, 0);
      edgePanel.add(nodeList1, 1);
      edgePanel.add(nodeList2, 2);
      edgePanel.add(okEdgeButton, 3);
      dialog.add(nodePanel, BorderLayout.CENTER);
      dialog.add(edgePanel, BorderLayout.SOUTH);
      dialog.pack();
      dialog.setVisible(true);
    }
    if (e.getSource() == removeNodeButton) {
        final JDialog dialog =
          new JDialog(frame, "Remove a node or an edge");
        JPanel nodePanel = new JPanel();
        JLabel nodeLabel = new JLabel("Node to remove : ");
        JButton removeNodeOk = new JButton("Remove");
        final JComboBox nodeList1 = new JComboBox(network.getNodes().toArray());
        nodeList1.setRenderer(new NodeCellRenderer("name"));
        final infovis.panel.VisualizationPanel panel =
          (infovis.panel.VisualizationPanel) ivtkview;
        removeNodeOk.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                Node node1 = (Node) nodeList1.getSelectedObjects()[0];
                prefViz.cancel("color");
                prefViz.cancel("layout");
                network.removeNode(node1);
                prefViz.invalidate("graph");
                prefViz.run("color");
                prefViz.run("layout");
                panel.getVisualization().invalidate();
                panel.getVisualization().repaint();
                dialog.dispose();
              }
            });
          }
        });
        nodePanel.add(nodeLabel, 0);
        nodePanel.add(nodeList1, 1);
        nodePanel.add(removeNodeOk);
        dialog.add(nodePanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
      }
    }).start();
  }

}
