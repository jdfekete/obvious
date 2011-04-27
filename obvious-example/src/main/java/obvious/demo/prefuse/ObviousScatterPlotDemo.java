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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.data.PrefuseObviousTable;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.view.JView;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;
import prefuse.visual.VisualItem;

/**
 * This class demonstrates how to use scatter plot visualization with obvious
 * prefuse.
 * It is also an illustration of the method "pickAll" introduced in
 * obvious-prefuse Visualization Class. In Prefuse, this method does not exists
 * natively.
 * @author Hemery
 *
 */
public class ObviousScatterPlotDemo {

    /**
     * An obvious view of the scatterplot.
     */
    private JView view;

    /**
     * An obvious visualization of the scatterplot.
     */
    private Visualization vis;

    /**
     * Constructor.
     */
    public ObviousScatterPlotDemo() {
        // Building the scatterplot structure.
        Schema schema = new PrefuseObviousSchema();
        schema.addColumn("id", Integer.class, 0);
        schema.addColumn("age", Integer.class, 18);
        schema.addColumn("category", String.class, "unemployed");

        Table table = new PrefuseObviousTable(schema);

        table.addRow(new TupleImpl(schema, new Object[] {1, 22, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {2, 60, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {3, 32, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {4, 20, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {5, 72, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {6, 40, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {7, 52, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {8, 35, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {9, 32, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {
                10, 44, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {11, 27, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {
                12, 38, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {13, 53, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {
                14, 49, "unemployed"}));
        table.addRow(new TupleImpl(schema, new Object[] {15, 21, "worker"}));
        table.addRow(new TupleImpl(schema, new Object[] {
                16, 36, "unemployed"}));

        // Creating the parameter map for the monolithic visualization.
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(PrefuseObviousVisualization.GROUP_NAME, "scatter");
        param.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
        param.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
        param.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field

        // Using the factory to build the visualization
        System.setProperty("obvious.VisualizationFactory",
            "obvious.prefuse.viz.PrefuseVisualizationFactory");
        VisualizationFactory factory = null;
        try {
            factory = PrefuseVisualizationFactory.getInstance();
        } catch (ObviousException e) {
            e.printStackTrace();
        }
        vis =
          factory.createVisualization(table, null, "scatterplot", param);

        // Creating the view.
        view = new PrefuseObviousView(vis, null, null, null);
    }

    /**
     * Gets the obvious view for the scatterplot.
     * @return an obvious view
     */
    public JView getJView() {
        return this.view;
    }

    /**
     * Gets the obvious visualization for the scatterplot.
     * @return an obvious visualization
     */
    public Visualization getVisualization() {
        return this.vis;
    }

    /**
     * Demo method.
     */
    public static void demo() {
        ObviousScatterPlotDemo scatterPlot = new ObviousScatterPlotDemo();
        JFrame frame = new JFrame("obvious-prefuse ScatterPlot demo");
        JComponent scatterView = scatterPlot.getJView().getViewJComponent();
        SelectionListener selListener = new SelectionListener(
                scatterPlot.getVisualization(), scatterPlot.getJView());
        //scatterView.addMouseListener(selListener);
        //scatterView.addMouseMotionListener(selListener);
        frame.add(scatterView);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prefuse.Visualization prefViz = (prefuse.Visualization)
            scatterPlot.getVisualization().getUnderlyingImpl(
                    prefuse.Visualization.class);
        prefViz.run("draw");
        prefViz.run("xlabel");
        prefViz.run("ylabel");
    }

    /**
     * Main method.
     * @param args arguments of the main
     */
    public static void main(String[] args) {
        demo();
    }

    /**
     * Selection Listener class.
     * @author Hemery
     *
     */
    public static class SelectionListener implements MouseListener,
            MouseMotionListener {
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
         * Selected index.
         */
        private ArrayList<Integer> selectedIds = null;

        /**
         * Constructor.
         * @param inVis an obvious visualization
         * @param inView an obvious view
         */
        public SelectionListener(Visualization inVis, JView inView) {
            this.vis = inVis;
            this.view = inView;
        }

        /**
         * Starts when a mouse button is clicked.
         * @param e mouse event.
         */
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                rectangle = null;
                uncolorNodes(selectedIds);
              } else if (e.getButton() == MouseEvent.BUTTON1) {
                rectangle = new Rectangle(e.getPoint(), new Dimension(1, 1));
                colorSelectedNodes();
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
         * Starts when a mouse button is pressed.
         * @param e mouse event.
         */
        public void mousePressed(MouseEvent e) {
            rectangle = new Rectangle(e.getX(), e.getY(), 0, 0);
            view.getViewJComponent().invalidate();
            view.getViewJComponent().repaint();
        }

        /**
         * Starts when a mouse button is released.
         * @param e mouse event.
         */
        public void mouseReleased(MouseEvent e) {
          rectangle.setSize(Math.abs(e.getX() - rectangle.x),
              Math.abs(e.getY() - rectangle.y));
          if (e.getButton() != MouseEvent.BUTTON3) {
              colorSelectedNodes();
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
          g.drawRect(rectangle.x, rectangle.y, rectangle.width,
                  rectangle.height);
        }

        /**
         * Not implemented.
         * @param e mouse event
         */
        public void mouseMoved(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        /**
         * Colors selected nodes.
         */
        private void colorSelectedNodes() {
            selectedIds = vis.pickAll(rectangle,
                    view.getViewJComponent().getBounds());
            Table table = (Table) vis.getData();
            for (int i = 0; i < selectedIds.size(); i++) {
                System.out.println("Selected tuple "
                        + table.getValue(selectedIds.get(i), "id"));
                table.set(selectedIds.get(i), VisualItem.STROKECOLOR,
                        prefuse.util.ColorLib.color(Color.RED));
                view.getViewJComponent().invalidate();
                view.getViewJComponent().repaint();
            }
            if (selectedIds.size() == 0) {
                selectedIds = vis.pickAll(view.getViewJComponent().getBounds(),
                        view.getViewJComponent().getBounds());
                rectangle = null;
                uncolorNodes(selectedIds);
            }
        }

        /**
         * Uncolors selected nodes.
         */
        private void uncolorNodes(ArrayList<Integer> ids) {
            Table table = (Table) vis.getData();
            for (int i = 0; i < ids.size(); i++) {
                table.set(ids.get(i), VisualItem.STROKECOLOR,
                        prefuse.util.ColorLib.color(Color.BLUE));
                view.getViewJComponent().invalidate();
                view.getViewJComponent().repaint();
            }
        }

    }

}
