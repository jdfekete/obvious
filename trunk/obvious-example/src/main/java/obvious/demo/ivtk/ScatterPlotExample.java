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

import infovis.io.AbstractReader;
import infovis.table.DefaultTable;
import infovis.table.io.TableReaderFactory;
import obvious.data.Table;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * ScatterPlot example for obvious-ivtk.
 * @author Hemery
 *
 */
public class ScatterPlotExample {

    /**
     * Obvious Jview.
     */
    private JView view;

    /**
     * Constructor.
     */
    public ScatterPlotExample() {
        /*
         * Creates the table data structure. For this example,
         * the ivtk table created from salivary.tqd file is wrapped
         * into an obvious table.
         */
        String file = "src/main/resources/salivary.tqd";
        DefaultTable t = new DefaultTable();
        AbstractReader reader =
            TableReaderFactory.createTableReader(file, t);
        reader.load();
        Table table = new IvtkObviousTable(t);
        /*
         * Creates a visualization.
         */
        Visualization vis = new IvtkScatterPlotVis(table,
                null, null, null);
        /*
         * Creates a view.
         */
        view = new IvtkObviousView(vis, null, null, null);
    }

    /**
     * Gets the obvious view associated to the scatter plot.
     * @return an obvious view.
     */
    public JView getJView() {
        return this.view;
    }

    /**
     * Demo method.
     */
    public static void demo() {
        ScatterPlotExample scatterPlotExample = new ScatterPlotExample();
        JFrame frame = new JFrame("ScatterPlot Example from ivtk"
                + "based on obvious");
        frame.add(scatterPlotExample.getJView().getViewJComponent());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method.
     * @param args arguments of the main
     */
    public static void main(String[] args) {
        demo();
    }
}
