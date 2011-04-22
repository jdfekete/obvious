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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import obvious.data.Table;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.view.util.IvtkScatterPlotView;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.view.JView;
import obvious.viz.Visualization;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;

/**
 * Creation of an Obvious scatter-plot visualization.
 * This is an example from evaluation section of the paper (listing 2).
 * @author Hemery
 *
 */
public final class ScatterPlotArticleExample {

  /**
   * Constructor.
   */
  private ScatterPlotArticleExample() {
  }

  /**
   * Main method.
   * @param args arguments of the main.
   * @throws ObviousxException if something bad happens when reading the files.
   */
  public static void main(final String[] args) throws ObviousxException {
    // Defining the data factory to use,
    // obvious-prefuse will be used for the data structure.
    System.setProperty("obvious.DataFactory",
        "obvious.prefuse.data.PrefuseDataFactory");

    // Creating an Obvious CSV reader and loading an Obvious table
    CSVImport csv = new CSVImport(new File(
        "src//main//resources//articlecombinedexample.csv"), ',');
    Table table = csv.loadTable();

    // Creating the parameter map for the monolithic object .
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(IvtkScatterPlotVis.X_AXIS, "id"); // xfield
    param.put(IvtkScatterPlotVis.Y_AXIS, "age"); // yfield

    // Creating the visualization then the view. No predicates are given to
    // the constructor .
    Visualization vis = new IvtkScatterPlotVis(table , null , "plot", param);

    JView view = new IvtkScatterPlotView(vis, null);

    // Standard Java window creation
    JFrame frame = new JFrame("Scatter-plot visualization (article example)");
    JScrollPane panel = new JScrollPane(view.getViewJComponent());
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);

  }

}
