package obvious.viz;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.util.Adaptable;

/**
 * Visualization interface.
 * @author Hemery
 *
 */
public abstract class Visualization implements Adaptable {

  /**
   * Visualization technic name.
   */
  private String visualizatioName;

  /**
   * Predicate used to select the data.
   */
  private Predicate pred;

  /**
   * Backing table.
   */
  private Table table;

  /**
   * Constructor.
   * @param parentTable Obvious data table
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technic name
   */
  public Visualization(Table parentTable, Predicate predicate, String visName) {
    this.table = parentTable;
    this.pred = predicate;
    this.visualizatioName = visName;
  }

  /**
   * Adds an action to the visualization.
   * @param actionName name of the action to add
   * @param action the action to add
   */
  public abstract void putAction(String actionName, Action action);

  /**
   * Sets the renderer(s) of the visualization.
   * The number of renderer to set depends of the implementation.
   * @param renderer an Obvious renderer
   */
  public abstract void setRenderer(Renderer renderer);

  /**
   * Pick all the items under a rectangle.
   * @param hitBox the bounds where the top item is searched
   * @param bounds the total bounds where the visualization is displayed
   * @return an ArrayList that will contain each row of items intersecting
   * the hitBox.
   */
  public abstract ArrayList<Integer> pickAll(
      Rectangle2D hitBox, Rectangle2D bounds);

  /**
   * Gets the obvious table associated to this visualization.
   * @return the obvious table associated to this visualization
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * Gets the obvious predicate associated to this visualization.
   * @return the obvious predicate associated to this visualization
   */
  public Predicate getPredicate() {
    return this.pred;
  }

  /**
   * Gets the visualization technique name associated to this visualization.
   * @return the visualization technique name associated to this visualization.
   */
  public String getVisualizationTechnique() {
    return this.visualizatioName;
  }
}
