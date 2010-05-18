package obvious.prefuse.viz;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.ObviousLinkListener;
import obvious.impl.TupleImpl;
import obvious.prefuse.PrefuseObviousNetwork;
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
   * Constructor.
   * @param parentNetwork an Obvious Network
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization
   * null if custom
   */
  public PrefuseObviousVisualization(Network parentNetwork, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(parentNetwork, predicate, visName);
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
    if (this.getData() instanceof Table) {
      vis.add(groupName, getPrefuseTable());
    } else if (this.getData() instanceof Network) {
      vis.add(groupName, getPrefuseNetwork());
    }
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
    if (((Table) this.getData()).getUnderlyingImpl(prefuse.data.Table.class)
        != null) {
      return (prefuse.data.Table)
          ((Table) this.getData()).getUnderlyingImpl(prefuse.data.Table.class);
    } else {
      return convertToPrefuseTable((Table) this.getData());
    }
  }

  /**
   * Gets the corresponding prefuse network.
   * @return corresponding prefuse network
   */
  protected prefuse.data.Graph getPrefuseNetwork() {
    if (((Network) this.getData()).getUnderlyingImpl(prefuse.data.Graph.class)
        != null) {
      return (prefuse.data.Graph)
          ((Network) getData()).getUnderlyingImpl(prefuse.data.Graph.class);
    } else {
      return convertToPrefuseGraph((Network) this.getData());
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
    ((Table) this.getData()).addTableListener(listnr2);
    return (prefuse.data.Table)
        obvPrefTable.getUnderlyingImpl(prefuse.data.Table.class);
  }

  /**
   * Converts an Obvious Network to a prefuse network.
   * @param network network to convert
   * @return the converted prefuse network
   */
  private prefuse.data.Graph convertToPrefuseGraph(Network network) {
    if (network.getEdges().size() != 0 && network.getNodes().size() != 0) {
      Schema nodeSchema = network.getNodes().iterator().next().getSchema();
      Schema edgeSchema = network.getEdges().iterator().next().getSchema();
      Network prefNetwork = new PrefuseObviousNetwork(nodeSchema, edgeSchema);
      for (Node node : network.getNodes()) {
        prefNetwork.addNode(node);
      }
      for (Edge edge : network.getEdges()) {
        if (network.getEdgeType(edge) == Graph.EdgeType.DIRECTED) {
          prefNetwork.addEdge(edge, network.getSource(edge),
              network.getTarget(edge), network.getEdgeType(edge));
        } else {
          Node firstNode = null;
          Node secondNode = null;
          int count = 0;
          System.out.println(network.getIncidentNodes(edge).size());
          for (Node node : network.getIncidentNodes(edge)) {
            if (count == 0) {
              firstNode = node;
            } else if (count == 1) {
              secondNode = node;
              break;
            }
            count++;
          }
          System.out.println(firstNode.get("name") + " : " + secondNode.get("name"));
          prefNetwork.addEdge(edge, firstNode, secondNode,
              network.getEdgeType(edge));
        }
      }
      return (prefuse.data.Graph)
          prefNetwork.getUnderlyingImpl(prefuse.data.Graph.class);
    } else {
      throw new ObviousRuntimeException("Empty graph!");
    }
  }

}
