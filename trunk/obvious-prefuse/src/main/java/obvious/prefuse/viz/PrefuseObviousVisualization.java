package obvious.prefuse.viz;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Table;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.ObviousLinkListener;
import obvious.impl.TupleImpl;
import obvious.prefuse.PrefuseObviousTable;
import obvious.viz.Action;
import obvious.viz.Renderer;
import obvious.viz.Visualization;


/**
 * PrefuseObviousVisualization class.
 * @author Hemery
 *
 */
public class PrefuseObviousVisualization extends Visualization {

  /**
   * Group name key field.
   */
  public static final String GROUP_NAME = "group";

  /**
   * Wrapped prefuse visualization.
   */
  private prefuse.Visualization vis;

  /**
   * Obvious prefuse table.
   */
  private Table obviousPrefuseTable;

  /**
   * Constructor.
   * @param parentTable an Obvious Table
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization
   * null if custom
   */
  public PrefuseObviousVisualization(Table parentTable, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(parentTable, predicate, visName);
    initVisualization(param);
  }

  /**
   * Inits a standard prefuse visualization.
   * @param param param of the visualization.
   */
  protected void initVisualization(Map<String, Object> param) {
    String groupName = "tupleset";
    if (param != null && param.containsKey(GROUP_NAME)) {
      groupName = (String) param.get(GROUP_NAME);
    }
    vis = new prefuse.Visualization();
    vis.add(groupName, getPrefuseTable());
  }

  /**
   * Gets the prefuse visualization attribute.
   * @return a prefuse visualization instance
   */
  protected prefuse.Visualization getPrefVisualization() {
    return this.vis;
  }

  /**
   * Sets the prefuse visualization attribute.
   * @param preVisualization prefuse visualization to set
   */
  protected void setPrefVisualization(prefuse.Visualization preVisualization) {
    this.vis = preVisualization;
  }

  @Override
  public ArrayList<Integer> pickAll(Rectangle2D hitBox, Rectangle2D bounds) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void putAction(String name, Action action) {
    if (action.getUnderlyingImpl(prefuse.action.Action.class) != null) {
      vis.putAction(name, (prefuse.action.Action)
          action.getUnderlyingImpl(prefuse.action.Action.class));
    } else {
      throw new ObviousRuntimeException("The following action : "
          + action.toString() + " is not supported");
    }
  }

  @Override
  public void setRenderer(Renderer renderer) {
    if (renderer.getUnderlyingImpl(prefuse.render.RendererFactory.class)
        != null) {
      vis.setRendererFactory((prefuse.render.RendererFactory)
          renderer.getUnderlyingImpl(prefuse.render.RendererFactory.class));
    } else {
      throw new ObviousRuntimeException("The following renderer : "
          + renderer.toString() + " is not supported");
    }
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return prefuse visualization instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(prefuse.Visualization.class)) {
      return vis;
    }
    return null;
  }

  /**
   * Gets the corresponding prefuse table.
   * @return corresponding prefuse table
   */
  protected prefuse.data.Table getPrefuseTable() {
    if (this.getTable().getUnderlyingImpl(prefuse.data.Table.class) != null) {
      return (prefuse.data.Table)
          this.getTable().getUnderlyingImpl(prefuse.data.Table.class);
    } else {
      return convertToPrefuseTable(this.getTable());
    }
  }

  /**
   * Converts an Obvious Table to a prefuse table.
   * @param otherTable table to convert
   * @return the converted prefuse table
   */
  private prefuse.data.Table convertToPrefuseTable(Table otherTable) {
    Table obvPrefTable = new PrefuseObviousTable(otherTable.getSchema());
    obviousPrefuseTable = obvPrefTable;
    for (IntIterator it = otherTable.rowIterator(); it.hasNext();) {
      int rowId = it.nextInt();
      obvPrefTable.addRow(new TupleImpl(otherTable, rowId));
    }
    TableListener listnr = new ObviousLinkListener(otherTable);
    TableListener listnr2 = new ObviousLinkListener(obviousPrefuseTable);
    obviousPrefuseTable.addTableListener(listnr);
    getTable().addTableListener(listnr2);
    return (prefuse.data.Table)
        obvPrefTable.getUnderlyingImpl(prefuse.data.Table.class);
  }

}
