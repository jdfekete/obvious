package obvious.ivtk.viz;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.geom.Rectangle2D;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import obvious.ivtk.view.IvtkObviousView;

import infovis.panel.VisualizationPanel;

/**
 * An ugly wrapper for ivtk JComponent, only defined to override paint method.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class CustomIvtkJComponent extends VisualizationPanel {

  /**
   * Checks if the component has already been displayed.
   */
  @SuppressWarnings("unused")
  private boolean firstLaunch = false;

  /**
   * Translations coordinates.
   */
  @SuppressWarnings("unused")
  private double dx = 0, dy = 0, scale = 1;

  /**
   * Bounds.
   */
  private Rectangle2D bounds;

  /**
   * Ivtk obvious view.
   */
  private IvtkObviousView ivtkView;

  /**
   * Constructor.
   * @param vis ivtk visualization
   */
  public CustomIvtkJComponent(infovis.Visualization vis) {
    super(vis);
    this.addAncestorListener(new AncestorTener(this));
    this.addComponentListener(new ThisListener(this));
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    if (bounds == null) {
      bounds = getFullBounds();
    }
    this.getVisualization().paint(g2d, bounds);
    firstLaunch = true;
  }

  /**
   * Sets panning values.
   * @param x panning pitch for the x axis
   * @param y panning pitch for the y axis
   */
  public void setPanValues(double x, double y) {
    this.dx = x;
    this.dy = y;
  }

  /**
   * Sets scaling value.
   * @param scaleValue scaling value
   */
  public void setScaleValue(double scaleValue) {
    this.scale = scaleValue;
  }

  /**
   * Sets bounds.
   * @param b bounds to set
   */
  public void setBounds(Rectangle2D b) {
    this.bounds = b;
  }

  @Override
  public void update(Graphics g) {
    paint(g);
  }

  /**
   * Sets the view.
   * @param view obvious view to set
   */
  public void setView(IvtkObviousView view) {
    this.ivtkView = view;
  }

  /**
   * Internal listener.
   * @author Hemery
   *
   */
  public static class ThisListener extends ComponentAdapter {

    /**
     * Listened comp.
     */
    private CustomIvtkJComponent comp;

    /**
     * Constructor.
     * @param inComp listened comp.
     */
    public ThisListener(CustomIvtkJComponent inComp) {
      this.comp = inComp;
    }

    @Override
    public void componentResized(ComponentEvent e) {
      System.out.println("RESIZED");
      comp.ivtkView.pan(1, 1);
    }
  }
  /**
   * Internal class.
   * @author Hemery
   *
   */
  public static class AncestorTener implements AncestorListener {

    /**
     * Obvious component.
     */
    @SuppressWarnings("unused")
    private CustomIvtkJComponent comp;

    /**
     * Constructor.
     * @param inComp obvious component
     */
    public AncestorTener(CustomIvtkJComponent inComp) {
      this.comp = inComp;
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
      //comp.ivtkView.pan(50, 50);
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
    }
  }

}
