package obvious.prefuse.viz;

import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;

/**
 * PrefuseVisualization factory class.
 * @author Hemery
 *
 */
public class PrefuseVisualizationFactory extends VisualizationFactory {

  /**
   * List of available visualizations.
   */
  private static String[] visTech = {"scatterplot"};

  /**
   * List of available visualizations techniques for this factory.
   */
  private static ArrayList<String> availableVis =
    new ArrayList<String>();

  @Override
  public Visualization createVisualization(Table table, Predicate pred,
      String visName, Map<String, Object> param) {
    if (visName == null || availableVis.contains(visName)) {
      return new PrefuseObviousVisualization(table, pred, visName, param);
    } else if (visName.toLowerCase().equals("scatterplot")) {
      return new PrefuseScatterPlotViz(table, pred, visName, param);
    } else {
      throw new ObviousRuntimeException("Unsupported visualization technique"
          + " : " + visName);
    }
  }

  @Override
  public ArrayList<String> getAvailableVisualization() {
    if (availableVis.size() == 0) {
      for (int i = 0; i < visTech.length; i++) {
        availableVis.add(visTech[i]);
      }
    }
    return availableVis;
  }

}
