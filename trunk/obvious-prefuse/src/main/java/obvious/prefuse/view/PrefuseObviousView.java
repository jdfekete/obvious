package obvious.prefuse.view;

import java.util.Map;

import javax.swing.JComponent;

import obvious.data.Network;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * An implementation of JView interface for prefuse.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
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

}
