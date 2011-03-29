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

package obvious.prefuse.view;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Map;

import javax.swing.JComponent;

import obvious.ObviousRuntimeException;
import obvious.data.Network;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.view.JView;
import obvious.view.event.ViewListener;
import obvious.viz.Visualization;

/**
 * An implementation of JView interface for prefuse.
 * @author Hemery
 *
 */
public class PrefuseObviousView extends JView {

  /**
   * Backing prefuse "view".
   */
  private prefuse.Display display;

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
  public PrefuseObviousView(Visualization vis, Predicate predicate,
      String tech, Map<String, Object> param) {
    if (vis.getUnderlyingImpl(prefuse.Visualization.class) != null) {
      this.backingVis = vis;
    } else {
      PrefuseVisualizationFactory factory = new PrefuseVisualizationFactory();
      if (vis.getData() instanceof Table) {
        this.backingVis = factory.createVisualization(
          (Table) vis.getData(), predicate, tech, param);
      } else if (vis.getData() instanceof Network) {
        this.backingVis = factory.createVisualization(
            (Network) vis.getData(), predicate, "default", param);
      }
    }
    display = new prefuse.Display(
        (prefuse.Visualization) backingVis.getUnderlyingImpl(
            prefuse.Visualization.class));
  }

  /**
   * Gets the Visualization backing this view.
   * @return the Visualization backing this view
   */
  public Visualization getVisualization() {
    return backingVis;
  }

  @Override
  public JComponent getViewJComponent() {
    return display;
  }

  @Override
  public void addListener(ViewListener lstnr) {
    if (lstnr.getUnderlyingImpl(prefuse.controls.Control.class) != null) {
      this.getViewListeners().add(lstnr);
      this.display.addControlListener(
          (prefuse.controls.Control) lstnr.getUnderlyingImpl(
              prefuse.controls.Control.class));
    } else {
      throw new ObviousRuntimeException("The following renderer : "
          + lstnr.toString() + " is not supported");
    }
  }

  @Override
  public boolean removeListener(ViewListener listener) {
    return this.getViewListeners().remove(listener);
  }

  /**
   * Returns a reference to the AffineTransform used by this view.
   * @return AffineTransform used by this view.
   */
  public AffineTransform getTransform() {
    return display.getTransform();
  }

  /**
   * Paints the view.
   * @param g Graphics instance
   */
  public void paint(Graphics g) {
    display.paint(g);
  }

  /**
   * Pans the view provided in screen coordinates.
   * @param dx the amount to pan along the x-dimension, in pixel units
   * @param dy the amount to pan along the y-dimension, in pixel units
   */
  public void pan(float dx, float dy) {
    display.pan(dx, dy);
  }

  /**
   * Sets the AffineTransform used by this view.
   * @param transform AffineTransform instance to set
   */
  public void setTransform(AffineTransform transform) {
    try {
      display.setTransform(transform);
    } catch (NoninvertibleTransformException e) {
      e.printStackTrace();
    }
  }

  /**
   * Zooms the view to the given scale.
   * @param p anchor point for the zoom
   * @param scale scale for the zoom
   */
  public void zoom(Point2D p, float scale) {
    display.zoom(p, scale);
  }

}
