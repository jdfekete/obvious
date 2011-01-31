package obvious.ivtk.viz;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
  private boolean firstLaunch = false;

  /**
   * Translations coordinates.
   */
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

  public void setView(IvtkObviousView view) {
    this.ivtkView = view;
  }

  public static class ThisListener extends ComponentAdapter {

    private CustomIvtkJComponent comp;

    public ThisListener(CustomIvtkJComponent inComp) {
      this.comp = inComp;
    }

    @Override
    public void componentResized(ComponentEvent e) {
      System.out.println("RESIZED");
      comp.ivtkView.pan(1, 1);
    }
  }
  public static class AncestorTener implements AncestorListener {

    private CustomIvtkJComponent comp;
    
    public AncestorTener(CustomIvtkJComponent inComp) {
      this.comp = inComp;
    }
    
    public void ancestorAdded(AncestorEvent event) {
      System.out.println("ADDED");
    }

    public void ancestorMoved(AncestorEvent event) {
      System.out.println("MOVED");
      comp.ivtkView.pan(50, 50);
    }

    public void ancestorRemoved(AncestorEvent event) {
      System.out.println("REMOVED");
    }
    
  }

}
