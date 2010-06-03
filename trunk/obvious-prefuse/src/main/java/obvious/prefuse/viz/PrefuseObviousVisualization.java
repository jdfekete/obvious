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
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.Predicate;
import obvious.impl.ObviousLinkListener;
import obvious.prefuse.PrefuseObviousNetwork;
import obvious.prefuse.PrefuseObviousTable;
import obvious.util.ObviousLib;
import obvious.viz.Action;
import obvious.viz.Renderer;
import obvious.viz.Visualization;
import obvious.data.Node;
import prefuse.visual.VisualTable;



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
   * Main group name for the prefuse visualization.
   */
  private String groupName;

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
    groupName = "tupleset";
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
    obviousPrefuseTable = new PrefuseObviousTable(otherTable.getSchema());
    ObviousLib.fillTable(otherTable, obviousPrefuseTable);
    TableListener listnr = new ObviousLinkListener(otherTable);
    TableListener listnr2 = new ObviousLinkListener(obviousPrefuseTable);
    obviousPrefuseTable.addTableListener(listnr);
    ((Table) this.getData()).addTableListener(listnr2);
    return (prefuse.data.Table)
        obviousPrefuseTable.getUnderlyingImpl(prefuse.data.Table.class);
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
      ObviousLib.fillNetwork(network, prefNetwork);
      return (prefuse.data.Graph)
          prefNetwork.getUnderlyingImpl(prefuse.data.Graph.class);
    } else {
      throw new ObviousRuntimeException("Empty graph!");
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
    return visualTable.get(tuple.getRow(), getAliasMap().get(alias));
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

}
