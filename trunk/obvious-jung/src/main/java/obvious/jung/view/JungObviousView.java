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

package obvious.jung.view;

import java.util.Map;

import javax.swing.JComponent;

import edu.uci.ics.jung.visualization.BasicVisualizationServer;

import obvious.ObviousRuntimeException;
import obvious.data.util.Predicate;
import obvious.view.JView;
import obvious.view.event.ViewListener;
import obvious.viz.Visualization;

/**
 * An implementation of JView interface for prefuse.
 * @author Hemery
 *
 */

public class JungObviousView extends JView {

  /**
   * Visualization backing this view.
   */
  private Visualization backingVis;

  /**
   * Constructor.
   * @param vis an Obvious Visualization
   * @param predicate an obvious predicate
   * @param tech visualization technique name
   * @param param parameters for the visualization
   */
  public JungObviousView(Visualization vis, Predicate predicate,
      String tech, Map<String, Object> param) {
    if (vis.getUnderlyingImpl(BasicVisualizationServer.class) != null) {
      this.backingVis = vis;
    } else {
      throw new ObviousRuntimeException("Unsupported visualization"
          + "implementation : " + vis.getClass().getSimpleName());
    }
  }

  @Override
  public void addListener(ViewListener lstnr) {
  }

  @Override
  public JComponent getViewJComponent() {
    return (BasicVisualizationServer) backingVis.getUnderlyingImpl(
        BasicVisualizationServer.class);
  }

  @Override
  public boolean removeListener(ViewListener listener) {
    return false;
  }

  @Override
  public Visualization getVisualization() {
    return this.backingVis;
  }

}
