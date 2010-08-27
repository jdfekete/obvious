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

package obvious.ivtk.view;

import java.util.Map;

import javax.swing.JComponent;

import obvious.data.Network;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.ivtk.viz.IvtkVisualizationFactory;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * An implementation of Obvious view interface for ivtk.
 * @author Hemery
 *
 */
public class IvtkObviousView extends JView {


  /**
   * ivtk visualization panel.
   */
  private infovis.panel.VisualizationPanel panel;

  /**
   * Visualization backing this view.
   */
  private Visualization backingVis;

  /**
   * Constructor.
   * @param vis an obvious visualization
   * @param predicate an obvious predicate.
   * @param tech visualization technique name
   * @param param parameters for the visualization
   */
  public IvtkObviousView(Visualization vis, Predicate predicate, String tech,
      Map <String, Object> param) {
    if (vis.getUnderlyingImpl(infovis.Visualization.class) != null) {
      this.backingVis = vis;
    } else {
      IvtkVisualizationFactory factory = new IvtkVisualizationFactory();
      if (vis.getData() instanceof Table) {
        this.backingVis = factory.createVisualization(
          (Table) vis.getData(), predicate, tech, param);
      } else if (vis.getData() instanceof Network) {
        this.backingVis = factory.createVisualization(
            (Network) vis.getData(), predicate, tech, param);
      }
    }
    panel = new infovis.panel.VisualizationPanel(
        (infovis.Visualization) backingVis.getUnderlyingImpl(
            infovis.Visualization.class));
  }

  /**
   * Constructor.
   * @param vis an obvious visualization
   * @param predicate an obvious predicate.
   */
  public IvtkObviousView(Visualization vis, Predicate predicate) {
    this(vis, predicate, null, null);
  }

  @Override
  public JComponent getViewJComponent() {
    return panel;
  }

  /**
   * Gets the Visualization backing this view.
   * @return the Visualization backing this view
   */
  public Visualization getVisualization() {
    return backingVis;
  }

}
