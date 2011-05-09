/*
* Copyright (c) 2010, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package obvious.prefuse.viz;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Data;
import obvious.data.Network;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.util.Predicate;
import obvious.prefuse.utils.wrappers.WrapToPrefGraph;
import obvious.prefuse.utils.wrappers.WrapToPrefTable;
import obvious.viz.Action;
import obvious.viz.Renderer;
import obvious.viz.Visualization;
import obvious.data.Node;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.visual.VisualTupleSet;



/**
 * This class is an implementation for the Obvious Prefuse Toolkit of the
 * {@link Visualization Visualization interface}. Since Prefuse is polylithic
 * this class can not be directly instantiated to create an Obvious
 * visualization. It should be extended to create custom Prefuse visualizations
 * such as {@link PrefuseObviousNetworkViz PrefuseObviousNetworkViz}. In
 * addition, this class introduces several extra parameter dedicated to
 * Prefuse. Thus, additional keys for the parameters maps have been introduced:
 * GROUP_NAME keys for the prefuse group name value and DIRECTED key indicating
 * if a graph is oriented or not. Default values are provided for those values
 * (tupleset for the group name and false for directed).
 * @see Visualization
 * @author Hemery
 *
 */
public class PrefuseObviousVisualization extends Visualization {

  /**
   * Group name key field.
   */
  public static final String GROUP_NAME = "group";

  /**
   * Directed key field.
   */
  public static final String DIRECTED = "directed";

  /**
   * Node key column key field.
   */
  public static final String NODE_KEY = "nodeKey";

  /**
   * Main group name for the prefuse visualization.
   */
  protected String groupName;

  /**
   * Boolean indicating if the prefuse graph is directed.
   */
  @SuppressWarnings("unused")
  private boolean directed = false;

  /**
   * Node key for prefuse obvious graph.
   */
  @SuppressWarnings("unused")
  private String nodeKey = prefuse.data.Graph.DEFAULT_NODE_KEY;

  /**
   * Wrapped prefuse visualization.
   */
  private prefuse.Visualization vis;

  /**
   * Constructor.
   * @param inData an obvious data instance
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization
   */
  public PrefuseObviousVisualization(Data inData, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(inData, predicate, visName, param);
  }

  @Override
  protected Table applyPredToTable(Table inData) {
    return super.applyPredToTable(inData);
  }

  @Override
  protected Network applyPredToNetwork(Network inData) {
    return super.applyPredToNetwork(inData);
  }

  /**
   * Inits a standard prefuse visualization.
   * @param param param of the visualization.
   */
  protected void initVisualization(Map<String, Object> param) {
    groupName = "tupleset";
    if (param != null) {
      if (param.containsKey(GROUP_NAME)) {
        groupName = (String) param.get(GROUP_NAME);
      }
      if (param.containsKey(DIRECTED)) {
        directed = (Boolean) param.get(DIRECTED);
      }
      if (param.containsKey(NODE_KEY)) {
        nodeKey = (String) param.get(NODE_KEY);
      }
    }
    vis = new prefuse.Visualization();
    if (this.getData() instanceof Table) {
      vis.addTable(groupName, getPrefuseTable());
    } else if (this.getData() instanceof Network) {
      vis.addGraph(groupName, getPrefuseNetwork());
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
    ArrayList<Integer> ids = new ArrayList<Integer>();
    VisualTupleSet visTuples = (VisualTupleSet) vis.getVisualGroup(groupName);
    Iterator<?> it = visTuples.tuples();
    while (it.hasNext()) {
      VisualItem item = (VisualItem) it.next();
      if (hitBox.intersects(item.getBounds())) {
        ids.add(item.getRow());
      }
    }
    return ids;
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
      return new WrapToPrefTable((Table) this.getData());
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
      return new WrapToPrefGraph((Network) getData());
    }
  }

  @Override
  public Object getAttributeValuetAt(Tuple tuple, String alias) {
    prefuse.data.tuple.TupleSet tupleSet = vis.getVisualGroup(groupName);
    prefuse.visual.VisualTable visualTable;
    if (tupleSet instanceof prefuse.visual.VisualTable) {
      visualTable = (prefuse.visual.VisualTable) tupleSet;
    } else if (tupleSet instanceof prefuse.data.Graph) {
      prefuse.data.Graph g = (prefuse.data.Graph) tupleSet;
      visualTable = (VisualTable)(tuple instanceof Node ? g.getNodeTable()
          : g.getEdgeTable());
    } else {
      return null;
    }
    if (visualTable.getSchema().getColumnIndex(getAliasMap().get(alias))
        != -1) {
      synchronized (visualTable) {
        int schemaSize = visualTable.getColumnCount() - 1;
        Boolean[] schemaBoolean = new Boolean[schemaSize + 1];
        String[] schemaCol = new String[schemaSize + 1];
        for (int i = schemaSize; i >= 0; i--) {
          schemaBoolean[i] = tuple.getSchema().hasColumn(
              visualTable.getColumnName(i));
          schemaCol[i] = visualTable.getColumnName(i);
        }
        for (int i = 0; i < visualTable.getRowCount(); i++) {
          boolean find = true;
          for (int j = schemaSize; j >= 0; j--) {
            if (schemaBoolean[i] &&  !visualTable.get(i, schemaCol[j]).equals(
                  tuple.get(schemaCol[j]))) {
                find = false;
                break;
            }
          }
          if (find) {
            return visualTable.get(i, alias);
          }
        }
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Ediflow dedicated method.
   * @param tuple an obvious tuple
   * @param alias an alias
   * @return an attribute value
   */
  public Object getAttributeValueAtOptimized(Tuple tuple, String alias) {
    prefuse.data.tuple.TupleSet tupleSet = vis.getVisualGroup(groupName);
    prefuse.visual.VisualTable visualTable;
    if (tupleSet instanceof prefuse.visual.VisualTable) {
      visualTable = (prefuse.visual.VisualTable) tupleSet;
    } else if (tupleSet instanceof prefuse.data.Graph) {
      prefuse.data.Graph g = (prefuse.data.Graph) tupleSet;
      visualTable = (VisualTable)(tuple instanceof Node ? g.getNodeTable()
          : g.getEdgeTable());
    } else {
      return null;
    }
    if (visualTable.getSchema().getColumnIndex(getAliasMap().get(alias))
        != -1) {
      return visualTable.get(tuple.getRow(), alias);
    } else {
      return null;
    }
  }

  @Override
  public void initAliasMap() {
    this.getAliasMap().put(VISUAL_COLOR, prefuse.visual.VisualItem.FILLCOLOR);
    this.getAliasMap().put(VISUAL_LABEL, prefuse.visual.VisualItem.LABEL);
    this.getAliasMap().put(VISUAL_SHAPE, prefuse.visual.VisualItem.SHAPE);
    this.getAliasMap().put(VISUAL_SIZE, prefuse.visual.VisualItem.SIZE);
    this.getAliasMap().put(VISUAL_VALIDATED,
        prefuse.visual.VisualItem.VALIDATED);
    this.getAliasMap().put(VISUAL_X, prefuse.visual.VisualItem.X);
    this.getAliasMap().put(VISUAL_Y, prefuse.visual.VisualItem.Y);
  }

  /**
   * Clear the visualization.
   */
  public void clearVisualization() {
    vis.removeGroup(groupName);
  }

  /**
   * Loads data into the visualization.
   * @param data data to load.
   */
  public void setVisualizationData(Data data) {
    if (data instanceof Table) {
      vis.addTable(groupName, getPrefuseTable());
    } else if (data instanceof Network) {
      vis.addGraph(groupName, ((prefuse.data.Graph) ((Network) data)
          .getUnderlyingImpl(prefuse.data.Graph.class)));
    }
  }

  /*
  @Override
  public Data getData() {
      if (vis.getGroup(groupName) != null && super.getData() instanceof Table) {
          return new PrefuseObviousTable(
                  (prefuse.data.Table) vis.getGroup(groupName));
      } else if (vis.getGroup(groupName) != null && super.getData() instanceof Network) {
          return new PrefuseObviousNetwork (
                  (prefuse.data.Graph) vis.getGroup(groupName));
      } else {
          return super.getData();
      }
  }
  */

}
