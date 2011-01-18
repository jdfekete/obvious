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

package obvious.ivtk.viz;

import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Table;
import obvious.data.Tree;
import obvious.data.util.Predicate;
import obvious.ivtk.viz.util.IvtkNodeLinkGraphVis;
import obvious.ivtk.viz.util.IvtkScatterPlotVis;
import obvious.ivtk.viz.util.IvtkTimeSerieVis;
import obvious.ivtk.viz.util.IvtkTreeMapVis;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;

/**
 * Ivtk obvious visualization factory.
 * @author Hemery
 *
 */
public class IvtkVisualizationFactory extends VisualizationFactory {

  /**
   * List of available visualizations.
   */
  private static String[] visTech = {"scatterplot"};

  /**
   * List of available visualizations techniques for this factory.
   */
  private static ArrayList<String> availableVis =
    new ArrayList<String>();

  @Override
  public Visualization createVisualization(Table table, Predicate pred,
      String visName, Map<String, Object> param) {
    if (visName == null) {
      return new IvtkObviousVisualization(table, pred, visName, param);
    } else if (visName.toLowerCase().equals("scatterplot")) {
      return new IvtkScatterPlotVis(table, pred, visName, param);
    } else if (visName.toLowerCase().equals("timeseries")) {
      return new IvtkTimeSerieVis(table, pred, visName, param);
    } else {
      throw new ObviousRuntimeException("Unsupported visualization technique"
          + " : " + visName);
    }
  }

  @Override
  public ArrayList<String> getAvailableVisualization() {
    if (availableVis.size() == 0) {
      for (int i = 0; i < visTech.length; i++) {
        availableVis.add(visTech[i]);
      }
    }
    return availableVis;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Visualization createVisualization(Network network, Predicate pred,
      String visName, Map<String, Object> param) {
    if (visName == null) {
      System.out.print("coucou");
      return new IvtkObviousVisualization(network, pred, visName, param);
    } else if (visName.toLowerCase().equals("network")) {
      return new IvtkNodeLinkGraphVis(network, pred, visName, param);
    } else if (visName.toLowerCase().equals("treemap")) {
      try {
          return new IvtkTreeMapVis((Tree<Node, Edge>) network, pred,
                  visName, param);
      } catch (ClassCastException e) {
         e.printStackTrace(); 
         return new 
                 IvtkNodeLinkGraphVis(network, pred, visName, param);
      }
    } else {
      throw new ObviousRuntimeException("Unsupported visualization technique"
          + " : " + visName);
    }
  }

}
