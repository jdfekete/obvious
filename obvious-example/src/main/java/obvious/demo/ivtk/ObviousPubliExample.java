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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;


import javax.swing.JComponent;
import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Graph.EdgeType;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkNodeLinkGraphVis;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * Publications graph examples for obvious-ivtk.
 * @author Hemery
 *
 */
public class ObviousPubliExample {

  /**
   * Obvious view.
   */
  private JView view;

  /**
   * Obvious visualization.
   */
  private Visualization vis;

  /**
   * Node schema.
   */
  private Schema nodeSchema;

  /**
   * Edge schema.
   */
  private Schema edgeSchema;

  /**
   * Constructor.
   */
  public ObviousPubliExample() {

    /*
     * Creating the graph structure.
     */
    nodeSchema = new PrefuseObviousSchema();
    nodeSchema.addColumn("name", String.class, "John");
    nodeSchema.addColumn("id", String.class, -1);
    nodeSchema.addColumn("#FirstEdge", Integer.class, -1);
    nodeSchema.addColumn("#LastEdge", Integer.class, -1);
    nodeSchema.addColumn("#FirstInEdge", Integer.class, -1);
    nodeSchema.addColumn("#LastInEdge", Integer.class, -1);
    edgeSchema = new PrefuseObviousSchema();
    edgeSchema.addColumn("edgeName", String.class, "John-John");
    edgeSchema.addColumn("id", String.class, -1);
    edgeSchema.addColumn("FirstVertex", Integer.class, -1);
    edgeSchema.addColumn("LastVertex", Integer.class, -1);
    edgeSchema.addColumn("NextEdge", Integer.class, -1);
    edgeSchema.addColumn("NextInEdge", Integer.class, -1);
    Network network = new IvtkObviousNetwork(nodeSchema, edgeSchema);
    initNetwork(network);

    /*
     * Initializing the visualization.
     */
    vis = new IvtkNodeLinkGraphVis(network, null, null, null);

    /*
     * Creating the view.
     */
    view = new IvtkObviousView(vis, null, null, null);
  }

  /**
   * Gets the obvious view.
   * @return the obvious view
   */
  public JView getJView() {
    return this.view;
  }

  /**
   * Gets the obvious visualization.
   * @return the obvious visualization
   */
  public Visualization getVis() {
    return this.vis;
  }

  /**
   * Build the network.
   * @param network network to build
   */
  private void initNetwork(Network network) {
    Node node0 = new NodeImpl(nodeSchema,
        new Object[] {"Robert", "0", 0, 7, 0, 7});
    Node node1 = new NodeImpl(nodeSchema,
        new Object[] {"Maurice", "1", 1, 6, 1, 6});
    Node node2 =  new NodeImpl(nodeSchema,
        new Object[] {"Claude", "2", 2, 8, 2, 8});
    Node node3 = new NodeImpl(nodeSchema,
        new Object[] {"Bernard", "3", 3, 5, 3, 5});
    Node node4 = new NodeImpl(nodeSchema,
        new Object[] {"Berangere", "4", 5, 8, 5, 8});
    Node node5 = new NodeImpl(nodeSchema,
        new Object[] {"Monique", "5", 4, 4, 4, 4});
    Node node6 = new NodeImpl(nodeSchema,
        new Object[] {"Henri", "6", 6, 6, 6, 6});

    network.addNode(node0);
    network.addNode(node1);
    network.addNode(node2);
    network.addNode(node3);
    network.addNode(node4);
    network.addNode(node5);
    network.addNode(node6);

    Edge edge0 = new EdgeImpl(edgeSchema,
        new Object[] {"0-1", "0", 0, 1, 1, 1});
    Edge edge1 = new EdgeImpl(edgeSchema,
        new Object[] {"1-2", "1", 1, 2, 2, 2});
    Edge edge2 = new EdgeImpl(edgeSchema,
        new Object[] {"2-0", "2", 2, 0, 3, 3});
    Edge edge3 = new EdgeImpl(edgeSchema,
        new Object[] {"2-3", "3", 2, 3, 4, 4});
    Edge edge4 = new EdgeImpl(edgeSchema,
        new Object[] {"3-5", "4", 3, 5, 5, 5});
    Edge edge5 = new EdgeImpl(edgeSchema,
        new Object[] {"3-4", "5", 3, 4, 6, 6});
    Edge edge6 = new EdgeImpl(edgeSchema,
        new Object[] {"1-6", "6", 1, 6, 7, 7});
    Edge edge7 = new EdgeImpl(edgeSchema,
        new Object[] {"0-4", "7", 0, 4, 8, 8});
    Edge edge8 = new EdgeImpl(edgeSchema,
        new Object[] {"2-4", "7", 2, 4, 0, 0});

    network.addEdge(edge0, node0, node1, EdgeType.DIRECTED);
    network.addEdge(edge1, node1, node2, EdgeType.DIRECTED);
    network.addEdge(edge2, node2, node0, EdgeType.DIRECTED);
    network.addEdge(edge3, node2, node3, EdgeType.DIRECTED);
    network.addEdge(edge4, node3, node5, EdgeType.DIRECTED);
    network.addEdge(edge5, node3, node4, EdgeType.DIRECTED);
    network.addEdge(edge6, node1, node6, EdgeType.DIRECTED);
    network.addEdge(edge7, node0, node4, EdgeType.DIRECTED);
    network.addEdge(edge8, node2, node4, EdgeType.DIRECTED);

  }

  /**
   * Demo method.
   */
  public static void demo() {
    ObviousPubliExample publication = new ObviousPubliExample();
    JFrame frame = new JFrame("Publications graph example");
    JComponent component = publication.getJView().getViewJComponent();
    MousePubliListener listener = new MousePubliListener(publication.getVis(),
        publication.getJView());
    component.addMouseListener(listener);
    component.addMouseMotionListener(listener);
    component.add(listener);
    frame.add(component);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Main method.
   * @param args arguments of the main.
   */
  public static void main(String[] args) {
    demo();
  }

  /**
   * A simple mouse listener.
   * @author Hemery
   *
   */
  @SuppressWarnings("serial")
  public static class MousePubliListener extends JComponent
      implements MouseListener, MouseMotionListener {

    /**
     * Obvious visualization.
     */
    private Visualization vis;

    /**
     * Obvious view.
     */
    private JView view;

    /**
     * Rectangle to draw.
     */
    private Rectangle rectangle;

    /**
     * Graphics variable.
     */
    private Graphics graphic;

    /**
     * Selected index.
     */
    private ArrayList<Integer> selectedIds = null;


    /**
     * Constructor.
     * @param inVis obvious visualization.
     * @param inView obvious view.
     */
    public MousePubliListener(Visualization inVis, JView inView) {
      this.vis = inVis;
      this.view = inView;
      this.graphic = view.getViewJComponent().getGraphics();
    }

    /**
     * Starts when a mouse button is clicked.
     * @param e mouse event.
     */
    public void mouseClicked(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON3) {
        rectangle = null;
        view.getViewJComponent().repaint();
      }
    }

    /**
     * Not implemented.
     * @param e mouse event.
     */
    public void mouseEntered(MouseEvent e) {
      return;
    }

    /**
     * Not implemented.
     * @param e mouse event.
     */
    public void mouseExited(MouseEvent e) {
      return;
    }

    /**
     * Not implemented.
     * @param e mouse event.
     */
    public void mousePressed(MouseEvent e) {
      if (selectedIds != null) {
        Table table = ((Network) vis.getData()).getNodeTable();
        for (int i = 0; i < selectedIds.size(); i++) {
          table.set(selectedIds.get(i), "#selection", null);
        }
        infovis.panel.VisualizationPanel panel =
          (infovis.panel.VisualizationPanel) view.getViewJComponent();
        panel.invalidate();
        panel.repaint();
        selectedIds = null;
      }
      rectangle = new Rectangle(e.getX(), e.getY(), 0, 0);
    }

    /**
     * Not implemented.
     * @param e mouse event.
     */
    public void mouseReleased(MouseEvent e) {
      Graphics g = view.getViewJComponent().getGraphics();
      rectangle.setSize(Math.abs(e.getX() - rectangle.x),
          Math.abs(e.getY() - rectangle.y));
      g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
      selectedIds = vis.pickAll(rectangle,
          view.getViewJComponent().getBounds());
      Table table = ((Network) vis.getData()).getNodeTable();
      for (int i = 0; i < selectedIds.size(); i++) {
        table.set(selectedIds.get(i), "#selection", true);
      }
      if (selectedIds.size() == 0) {
        rectangle = null;
        view.getViewJComponent().repaint();
      }
    }

    /**
     * Starts when the mouse is dragged.
     * @param e mouse event.
     */
    public void mouseDragged(MouseEvent e) {
      Graphics g = view.getViewJComponent().getGraphics();
      rectangle.setSize(Math.abs(e.getX() - rectangle.x),
          Math.abs(e.getY() - rectangle.y));
      view.getViewJComponent().repaint();
      g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    /**
     * Not implemented.
     * @param e mouse event.
     */
    public void mouseMoved(MouseEvent e) {
      return;
    }
  }

}
