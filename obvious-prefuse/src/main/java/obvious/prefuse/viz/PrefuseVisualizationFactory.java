/*
* Copyright (c) 2009, INRIA
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

package obvious.prefuse.viz;

import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Network;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;

/**
 * PrefuseVisualization factory class.
 * @author Hemery
 *
 */
public class PrefuseVisualizationFactory extends VisualizationFactory {

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
      return new PrefuseObviousVisualization(table, pred, visName, param);
    } else if (visName.toLowerCase().equals("scatterplot")) {
      return new PrefuseScatterPlotViz(table, pred, visName, param);
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

  @Override
  public Visualization createVisualization(Network network, Predicate pred,
      String visName, Map<String, Object> param) {
    if (visName == null) {
      return new PrefuseObviousVisualization(network, pred, visName, param);
    } else {
      throw new ObviousRuntimeException("Unsupported visualization technique"
          + " : " + visName);
    }
  }

}
