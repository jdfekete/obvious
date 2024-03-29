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

package obvious.ivtk.viz.util;

import infovis.Column;

import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.ivtk.viz.IvtkObviousVisualization;

/**
 * This class is a specialized Obvious visualization for {@link Table Table}
 * based on the scatter-plot visualization introduced in the InfoVis Toolkit.
 * This class used extra-parameters to configure the visualization. Thus, it
 * is possible to specify the X and Y axis of the scatter-plot by adding the
 * name of those axis for the key described by X_AXIS and Y_AXIS variables
 * introduced in {@link obvious.viz.Visualization Visualization}.
 * @see obvious.viz.Visualization
 * @author Hemery
 *
 */
public class IvtkScatterPlotVis extends IvtkObviousVisualization {

  /**
   * Constructor.
   * @param parentTable an Obvious Table
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization (could be null)
   */
  public IvtkScatterPlotVis(Table parentTable, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(parentTable, predicate, visName, param);
  }

  @Override
  protected void initVisualization(Map<String, Object> param) {
    String xAxis = (String) (
        param != null && param.containsKey(X_AXIS) ? param.get(X_AXIS) : null);
    String yAxis = (String) (
        param != null && param.containsKey(Y_AXIS) ? param.get(Y_AXIS) : null);
    Column xCol = null;
    Column yCol = null;
    try {
      for (int i = 0; i < getIvtkTable().getColumnCount(); i++) {
        Column tempCol = getIvtkTable().getColumnAt(i);
        if (tempCol != null && tempCol.getName().equals(xAxis)) {
          xCol = tempCol;
        } else if (tempCol != null && tempCol.getName().equals(yAxis)) {
          yCol = tempCol;
        }
      }
    } catch (Exception e) {
      throw new ObviousRuntimeException(e);
    }
    if (xCol != null && yCol != null) {
      setIvtkVisualization(new infovis.table.visualization.
        ScatterPlotVisualization(getIvtkTable(), xCol, yCol));
    } else {
      setIvtkVisualization(new infovis.table.visualization.
          ScatterPlotVisualization(getIvtkTable()));
    }
    setVisualAllColumns(param, DataModel.TABLE);
  }

}
