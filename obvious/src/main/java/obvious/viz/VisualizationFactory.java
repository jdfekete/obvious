package obvious.viz;

import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousException;
import obvious.data.Table;
import obvious.data.util.Predicate;


/**
 * Abstract Class VisualizationFactory.
 * A factory that can create Visualization instances.
 * Each implementation of Obvious should implement this class.
 * @author Hemery
 */
public abstract class VisualizationFactory {

  /**
   * Static instance of DataFactory.
   */
  private static VisualizationFactory instance;

  /**
   * Constructor.
   */
  protected VisualizationFactory() { }

  /**
   * Gets an instance of the convenient VisualizationFactory.
   * @return an instance of VisualizationFactory
   * @throws ObviousException if something bad happens
   */
  public static VisualizationFactory getInstance() throws ObviousException {
    if (instance == null) {
      String className = System.getProperty("obvious.VisualizationFactory");
      if (className == null) {
        throw new ObviousException(
                "Property obvious.VisualizationFactory not set");
      }
      try {
        Class<?> c = Class.forName(className);
        instance = (VisualizationFactory) c.newInstance();
      } catch (Exception e) {
        throw new ObviousException(e);
      }
    }
    return instance;
  }

  /**
   * Gets the list of all available visualizations for this factory.
   * @return the list of all available visualizations for this factory
   */
  public abstract ArrayList<String> getAvailableVisualization();

  /**
   * Creates an instance of Visualization.
   * @param table parent table
   * @param pred a predicate to filter the table
   * @param visName visualization technique name
   * @param param parameters of the visualization
   * @return an instance of Visualization
   */
  public abstract Visualization createVisualization(
      Table table, Predicate pred, String visName, Map<String, Object> param);

}
