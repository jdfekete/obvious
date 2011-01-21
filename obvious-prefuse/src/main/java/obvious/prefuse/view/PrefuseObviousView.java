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
