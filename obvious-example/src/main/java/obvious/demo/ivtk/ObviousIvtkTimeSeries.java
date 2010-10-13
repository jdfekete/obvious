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

import obvious.data.Table;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkTimeSerieVis;
import obvious.view.JView;
import obvious.viz.Visualization;
import infovis.Column;
import infovis.DynamicTable;
import infovis.io.AbstractReader;
import infovis.table.DefaultDynamicTable;
import infovis.table.DefaultTable;
import infovis.table.io.TableReaderFactory;
import infovis.utils.RowIterator;

/**
 * Example1 of infovis.examples based on a time series visualization.
 * Build on obvious-ivtk.
 * @author Hemery
 *
 */
public class ObviousIvtkTimeSeries {

    /**
     * An obvious JView.
     */
    private JView view;

    /**
     * Constructor.
     */
    public ObviousIvtkTimeSeries() {
        /*
         * Creates the data structure.
         * We will wrap an existing ivtk table to an obvious table.
         */
        String file = "src/main/resources/salivary.tqd";
        DefaultTable t = new DefaultTable();
        AbstractReader reader = // Create a reader for the specified file
            TableReaderFactory.createTableReader(file, t);
        reader.load();

        Table table = new IvtkObviousTable(t);
        /*
         * Creates a time series visualization.
         */
        Visualization vis = new IvtkTimeSerieVis(table, null, null, null);
        /*
         * Creates the view.
         */
        view = new IvtkObviousView(vis, null, null, null);
    }

    /**
     * Gets the JView.
     * @return a JView.
     */
    public JView getJView() {
        return this.view;
    }

    /**
     * Demo method.
     */
    public static void demo() {
        ObviousIvtkTimeSeries timeSeries = new ObviousIvtkTimeSeries();
        JFrame frame = new JFrame("TimeSeries example from ivtk");
        frame.add(timeSeries.getJView().getViewJComponent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Main function.
     * @param args arguments of the main
     */
    public static void main(String[] args) {
        demo();
    }

}
