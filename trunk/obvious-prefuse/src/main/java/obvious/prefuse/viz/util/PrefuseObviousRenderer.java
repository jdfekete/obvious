package obvious.prefuse.viz.util;

import obvious.viz.Renderer;

/**
 * PrefuseObviousRenderer class.
 * @author plhemery
 *
 */
public class PrefuseObviousRenderer implements Renderer {

  /**
   * Underlying prefuse renderer factory.
   */
  private prefuse.render.RendererFactory renderer;

  /**
   * Constructor.
   * @param rendFactory a prefuse renderer factory
   */
  public PrefuseObviousRenderer(prefuse.render.RendererFactory rendFactory) {
    this.renderer = rendFactory;
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return prefuse action instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type != null && type.equals(prefuse.render.RendererFactory.class)) {
      return this.renderer;
    }
    return null;
  }

}
