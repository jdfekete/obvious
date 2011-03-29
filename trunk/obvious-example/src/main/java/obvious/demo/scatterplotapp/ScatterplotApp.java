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

package obvious.demo.scatterplotapp;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.view.IvtkObviousView;
import obvious.prefuse.data.PrefuseObviousTable;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;

/**
 * A demo application for obvious.
 * @author Hemery
 *
 */
public final class ScatterplotApp {

  /**
   * Constructor.
   */
  private ScatterplotApp() {
  }

  /**
   * Main method.
   * @param args arguments of the main
   * @throws ObviousException if something bad happens
   */
  public static void main(final String[] args) throws ObviousException {

    final Schema schema = new IvtkObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 18);
    schema.addColumn("category", String.class, "unemployed");

    final Table table = new PrefuseObviousTable(schema);

    table.addRow(new TupleImpl(schema, new Object[] {1, 22, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {2, 60, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {3, 32, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {4, 20, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {5, 72, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {6, 40, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {7, 52, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {8, 35, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {9, 32, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {10, 44, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {11, 27, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {12, 38, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {13, 53, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {14, 49, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {15, 21, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {16, 36, "unemployed"}));

    // Creating the parameter map for the monolithic object.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
    param.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
    param.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field

    // Creating the parameter map for the monolithic object.
    Map<String, Object> paramIvtk = new HashMap<String, Object>();
    paramIvtk.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
    paramIvtk.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
    paramIvtk.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field

    // Using the factory to build the visualization
    System.setProperty("obvious.VisualizationFactory",
        "obvious.prefuse.viz.PrefuseVisualizationFactory");
    VisualizationFactory factory = PrefuseVisualizationFactory.getInstance();
    Visualization vis =
      factory.createVisualization(table, null, "scatterplot", param);

    // In order to display, we have to call the underlying prefuse
    // visualization.
    // In a complete version of obvious, we don't need that step.
    final prefuse.Visualization prefViz = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    // Building the prefuse display.


    PrefuseObviousView view = new PrefuseObviousView(
        vis, null, "scatterplot", null);
    view.addListener(new PrefuseObviousControl(new ZoomControl()));
    view.addListener(new PrefuseObviousControl(new PanControl()));
    view.addListener(new PrefuseObviousControl(new DragControl()));

    IvtkObviousView view2 = new IvtkObviousView(
        vis,  null, "scatterplot", null);

    Dimension minimumSize = new Dimension(0, 0);
    view.getViewJComponent().setMinimumSize(minimumSize);
    view2.getViewJComponent().setMinimumSize(minimumSize);

    JFrame frame = new JFrame("Obvious : scatterplot demonstrator");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane scatterplotViews = new ScatterPlotViewsPanel(
        view.getViewJComponent(), view2.getViewJComponent());
    JSplitPane configurationPanel = new ConfigurationPanel(
        table, frame, prefViz, view2.getViewJComponent());
    mainPane.add(scatterplotViews, 0);
    mainPane.add(configurationPanel, 1);
    frame.add(mainPane);
    frame.pack();
    frame.setVisible(true);

    prefViz.run("draw");

  }

}
