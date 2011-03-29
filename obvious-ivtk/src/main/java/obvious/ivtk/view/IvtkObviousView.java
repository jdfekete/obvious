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

import infovis.panel.VisualizationPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.JComponent;

import obvious.data.Network;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.ivtk.viz.CustomIvtkJComponent;
import obvious.ivtk.viz.IvtkVisualizationFactory;
import obvious.view.JView;
import obvious.view.event.ViewListener;
import obvious.viz.Visualization;

/**
 * An implementation of Obvious view interface for ivtk.
 * @author Hemery
 *
 */
public class IvtkObviousView extends JView {


  /**
   * Base ivtk visualization panel.
   */
  private infovis.panel.VisualizationPanel panel;

  /**
   * Custom ivtk visualization panel.
   */
  private CustomIvtkJComponent customPanel;

  /**
   * Visualization backing this view.
   */
  private Visualization backingVis;

  /**
   * Transform stored.
   */
  private AffineTransform trsf;

  /**
   * Constructor.
   * @param vis an obvious visualization
   * @param predicate an obvious predicate.
   * @param tech visualization technique name
   * @param param parameters for the visualization
   */
  public IvtkObviousView(Visualization vis, Predicate predicate, String tech,
      Map <String, Object> param) {
    this.trsf = new AffineTransform();
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
    ((VisualizationPanel) getViewJComponent()).setUsingGradient(false);
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
    /*
    if (customPanel == null) {
      customPanel = new CustomIvtkJComponent(panel.getVisualization());
      customPanel.setView(this);
    }
    return customPanel;
    */
    return panel;
  }

  /**
   * Gets the Visualization backing this view.
   * @return the Visualization backing this view
   */
  public Visualization getVisualization() {
    return backingVis;
  }

  @Override
  public void addListener(ViewListener lstnr) {
    this.getViewListeners().add(lstnr);
  }

  @Override
  public boolean removeListener(ViewListener listener) {
    return this.getViewListeners().remove(listener);
  }

  /**
   * Gets the corresponding underlying implementation.
   * @param type target class
   * @return an instance of the underlying implementation or null
   */
   public Object getUnderlyingImpl(Class<?> type) {
     if (type.equals(infovis.panel.VisualizationPanel.class)) {
       return getViewJComponent();
     }
     return null;
   }

  /**
  * Returns a reference to the AffineTransform used by this view.
  * @return AffineTransform used by this view.
  */
  public AffineTransform getTransform() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Paints the view.
   * @param g Graphics instance
   */
  public void paint(Graphics g) {
    Graphics2D g2D = (Graphics2D) g.create();
    this.getViewJComponent().paint(g2D);
  }

  /**
   * Pans the view provided in screen coordinates.
   * @param indx the amount to pan along the x-dimension, in pixel units
   * @param indy the amount to pan along the y-dimension, in pixel units
   */
  public void pan(float indx, float indy) {
    this.trsf.translate(indx, indy);
    Graphics2D g = ((Graphics2D) this.getViewJComponent().getGraphics());
    g.clearRect(0, 0, getViewJComponent().getSize().width,
        getViewJComponent().getSize().height);
    g.setTransform(trsf);
    Rectangle2D bounds = new Rectangle2D.Float(
        0, 0, getViewJComponent().getWidth() + indx,
        getViewJComponent().getHeight() + indy);
    this.customPanel.setBounds(bounds);
    this.customPanel.setPanValues((double) indx,  (double) indy);
    this.paint(g);
  }

  /**
   * Sets the AffineTransform used by this view.
   * @param transform AffineTransform instance to set
   */
  public void setTransform(AffineTransform transform) {
    this.trsf = transform;
  }


  /**
   * Zooms the view to the given scale.
   * @param p anchor point for the zoom
   * @param scale scale for the zoom
   */
  public void zoom(Point2D p, float scale) {
    Graphics2D g = ((Graphics2D) this.getViewJComponent().getGraphics());
    double x = p.getX(), y = p.getY();
    this.trsf.translate(x, y);
    this.trsf.scale(scale, scale);
    this.trsf.translate(-x, -y);
    g.clearRect(0, 0, getViewJComponent().getSize().width,
        getViewJComponent().getSize().height);
    g.setTransform(trsf);
    this.customPanel.setScaleValue(scale);
    this.customPanel.setPanValues(x, y);
    this.paint(g);
  }

}
