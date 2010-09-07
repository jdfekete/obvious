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

package obvious.ivtk.viz;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousRuntimeException;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tree;
import obvious.data.Tuple;
import obvious.data.event.NetworkListener;
import obvious.data.event.TableListener;
import obvious.data.util.Predicate;
import obvious.impl.ObviousLinkListener;
import obvious.impl.ObviousLinkNetworkListener;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.ivtk.data.IvtkObviousTree;
import obvious.util.ObviousLib;
import obvious.viz.Action;
import obvious.viz.Renderer;
import obvious.viz.Visualization;

/**
 * Infovis Toolkit implementation of obvious Visualization abstract class.
 * @author Hemery
 *
 */
public class IvtkObviousVisualization extends Visualization {

  /**
   * Wrapped ivtk visualization.
   */
  private infovis.Visualization vis;

  /**
   * Constructor.
   * @param parentTable an Obvious Table
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization (could be null)
   */
  public IvtkObviousVisualization(Table parentTable, Predicate predicate,
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
  public IvtkObviousVisualization(Network parentNetwork, Predicate predicate,
      String visName,  Map<String, Object> param) {
    super(parentNetwork, predicate, visName);
    initVisualization(param);
  }

  /**
   * Constructor.
   * @param parentTree Obvious data tree
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technique name
   * @param param parameters of the visualization
   */
  public IvtkObviousVisualization(Tree<Node, Edge> parentTree,
      Predicate predicate, String visName, Map<String, Object> param) {
    super(parentTree, predicate, visName);
    initVisualization(param);
  }

  /**
   * Initializes the visualization.
   * @param param parameters of the visualization
   */
  protected void initVisualization(Map<String, Object> param) {
    if (this.getData() instanceof Table) {
      vis = new infovis.visualization.DefaultVisualization(getIvtkTable());
    } else if (this.getData() instanceof Network) {
      vis = new infovis.graph.visualization.NodeLinkGraphVisualization(
          getIvtkGraph());
    }
  }

  /**
   * Gets the ivtk visualization attribute.
   * @return ivtk visualization attribute
   */
  protected infovis.Visualization getIvtkVisualization() {
    return this.vis;
  }

  /**
   * Sets the ivtk visualization attribute.
   * @param viz ivtk visualization to set
   */
  protected void setIvtkVisualization(infovis.Visualization viz) {
    this.vis = viz;
  }

  @Override
  public ArrayList<Integer> pickAll(Rectangle2D hitBox, Rectangle2D bounds) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void putAction(String actionName, Action action) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setRenderer(Renderer renderer) {
    // TODO Auto-generated method stub
  }


  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return Ivtk visualization instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(infovis.Visualization.class)) {
      return vis;
    }
    return null;
  }

  /**
   * Gets the corresponding ivtk table.
   * @return corresponding ivtk table
   */
  protected infovis.Table getIvtkTable() {
    if (((Table) getData()).getUnderlyingImpl(infovis.Table.class) != null) {
      return (infovis.Table)
        ((Table) getData()).getUnderlyingImpl(infovis.Table.class);
    } else {
      return convertToIvtkTable((Table) this.getData());
    }
  }

  /**
   * Gets the corresponding ivtk graph.
   * @return corresponding ivtk graph
   */
  public infovis.Graph getIvtkGraph() {
    if (((Network) getData()).getUnderlyingImpl(infovis.Graph.class) != null) {
      return (infovis.Graph)
        ((Network) getData()).getUnderlyingImpl(infovis.Graph.class);
    } else {
      return convertToIvtkGraph((Network) this.getData());
    }
  }

  /**
   * Gets the corresponding ivtk tree.
   * @return corresponding ivtk tree
   */
  @SuppressWarnings("unchecked")
  protected infovis.Tree getIvtkTree() {
    if (((Tree<Node, Edge>) getData()).getUnderlyingImpl(infovis.Tree.class)
        != null) {
      return (infovis.Tree)
        ((Tree) getData()).getUnderlyingImpl(infovis.Tree.class);
    } else {
      return convertToIvtkTree((Tree) this.getData());
    }
  }

  /**
   * Converts an Obvious Table to an infovis table.
   * @param table table to convert
   * @return the converted infovis table
   */
  private infovis.Table convertToIvtkTable(Table table) {
    Table ivtkObviousTable = new IvtkObviousTable(table.getSchema());
    ObviousLib.fillTable(table, ivtkObviousTable);
    TableListener listnr = new ObviousLinkListener(table);
    TableListener listnr2 = new ObviousLinkListener(ivtkObviousTable);
    ivtkObviousTable.addTableListener(listnr);
    table.addTableListener(listnr2);
    return (infovis.Table) ivtkObviousTable.getUnderlyingImpl(
        infovis.Table.class);
  }

  /**
   * Converts an Obvious network to an infovis graph.
   * @param network network to convert
   * @return the converted infovis network
   */
  private infovis.Graph convertToIvtkGraph(Network network) {
    if (network.getEdges().size() != 0 && network.getNodes().size() != 0) {
      Schema nodeSchema = network.getNodes().iterator().next().getSchema();
      Schema edgeSchema = network.getEdges().iterator().next().getSchema();
      Network ivtkNetwork = new IvtkObviousNetwork(nodeSchema, edgeSchema);
      ObviousLib.fillNetwork(network, ivtkNetwork);
      NetworkListener listnr = new ObviousLinkNetworkListener(network);
      NetworkListener listnr2 = new ObviousLinkNetworkListener(ivtkNetwork);
      ivtkNetwork.addNetworkListener(listnr);
      network.addNetworkListener(listnr2);
      return (infovis.Graph)
          ivtkNetwork.getUnderlyingImpl(infovis.Graph.class);
    } else {
      throw new ObviousRuntimeException("Empty graph!");
    }
  }

  /**
   * Converts an Obvious tree to an infovis tree.
   * @param tree tree to convert
   * @return the converted infovis tree
   */
  private infovis.Tree convertToIvtkTree(Tree<Node, Edge> tree) {
    if (tree.getEdges().size() != 0 && tree.getNodes().size() != 0) {
      Schema nodeSchema = tree.getNodes().iterator().next().getSchema();
      Schema edgeSchema = tree.getEdges().iterator().next().getSchema();
      Tree<Node, Edge> ivtkTree = new IvtkObviousTree(nodeSchema, edgeSchema);
      ObviousLib.fillTree(tree, ivtkTree);
      return (infovis.Tree)
        ivtkTree.getUnderlyingImpl(infovis.Tree.class);
    } else {
      throw new ObviousRuntimeException("Empty tree!");
    }
  }

  @Override
  public Object getAttributeValuetAt(Tuple tuple, String alias) {
    int colId = -1;
    for (int i = 0; i < vis.getTable().getColumnCount(); i++) {
      if (vis.getVisualColumn(getAliasMap().get(alias)) != null
          && vis.getTable().getColumnName(i).equals(
          vis.getVisualColumn(getAliasMap().get(alias)).getName())) {
        colId = i;
        break;
      }
    }
    if (colId != -1) {
      return vis.getTable().getValueAt(tuple.getRow(), colId);
    } else {
      return null;
    }
  }

  @Override
  public void initAliasMap() {
    this.getAliasMap().put(VISUAL_COLOR, infovis.Visualization.VISUAL_COLOR);
    this.getAliasMap().put(VISUAL_LABEL, infovis.Visualization.VISUAL_LABEL);
    this.getAliasMap().put(VISUAL_SHAPE, infovis.Visualization.VISUAL_SHAPE);
    this.getAliasMap().put(VISUAL_SIZE, infovis.Visualization.VISUAL_SIZE);
    this.getAliasMap().put(VISUAL_VALIDATED,
        infovis.Visualization.VISUAL_FILTER);
  }
}
