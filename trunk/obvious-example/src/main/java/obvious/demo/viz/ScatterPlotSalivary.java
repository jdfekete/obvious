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

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;

import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.viz.Visualization;
import obvious.data.Schema;
import obvious.data.Table;
import infovis.panel.VisualizationPanel;

/**
 * Simple scatter plot test based on obvious-ivtk.
 * @author Hemery
 *
 */
public final class ScatterPlotSalivary {

  /**
   * Private constructor.
   */
  private ScatterPlotSalivary() {
  }

  /**
   * Main method.
   * @param args arguments
   */
  public static void main(final String[] args) {
    Schema schema = new PrefuseObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 18);
    Table table = new IvtkObviousTable(schema);

    table.addRow(new TupleImpl(schema, new Object[] {1, 22}));
    table.addRow(new TupleImpl(schema, new Object[] {2, 60}));
    table.addRow(new TupleImpl(schema, new Object[] {3, 32}));
    table.addRow(new TupleImpl(schema, new Object[] {4, 20}));
    table.addRow(new TupleImpl(schema, new Object[] {5, 72}));
    table.addRow(new TupleImpl(schema, new Object[] {6, 40}));
    table.addRow(new TupleImpl(schema, new Object[] {7, 52}));
    table.addRow(new TupleImpl(schema, new Object[] {8, 35}));
    table.addRow(new TupleImpl(schema, new Object[] {9, 32}));
    table.addRow(new TupleImpl(schema, new Object[] {10, 44}));
    table.addRow(new TupleImpl(schema, new Object[] {11, 2}));
    table.addRow(new TupleImpl(schema, new Object[] {12, 38}));
    table.addRow(new TupleImpl(schema, new Object[] {13, 53}));
    table.addRow(new TupleImpl(schema, new Object[] {14, 49}));
    table.addRow(new TupleImpl(schema, new Object[] {15, 21}));
    table.addRow(new TupleImpl(schema, new Object[] {16, 36}));

    Map<String, Object> param = new HashMap<String, Object>();
    param.put(IvtkScatterPlotVis.X_AXIS, "id");
    param.put(IvtkScatterPlotVis.Y_AXIS, "age");
    Visualization vis = new IvtkScatterPlotVis(
        table, null, "scatterplot", param);
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
