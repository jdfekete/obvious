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

package obvious.jung.visualization;

import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

import obvious.ObviousRuntimeException;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Edge;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.data.Tuple;
import obvious.data.event.NetworkListener;
import obvious.data.util.Predicate;
import obvious.impl.ObviousLinkNetworkListener;
import obvious.jung.data.JungObviousNetwork;
import obvious.util.ObviousLib;
import obvious.viz.Action;
import obvious.viz.Renderer;
import obvious.viz.Visualization;

/**
 * Visualization class for the obvious-jung implementation.
 * @author Hemery
 *
 */
public class JungObviousVisualization extends Visualization {

  /**
   * Layout key for parameter map.
   */
  public static final String LAYOUT = "LAYOUT";

  /**
   * Underlying JUNG visualization.
   */
   private BasicVisualizationServer<Node, Edge> vis;

   /**
    * A Jung graph used for this obvious implementation.
    */
   private edu.uci.ics.jung.graph.Graph<Node, Edge> jungGraph;

  /**
   * Constructor.
   * @param parentNetwork an Obvious Network
   * @param predicate a Predicate used to filter the table
   * @param visName name of the visualization technique to used (if needed)
   * @param param parameters of the visualization
   * null if custom
   */
  public JungObviousVisualization(Network parentNetwork, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(parentNetwork, predicate, visName);
    initVisualization(param);
  }

  @Override
  protected void initVisualization(Map<String, Object> param) {
    String layoutValue = null;
    if (param != null) {
      layoutValue = (String) param.get(LAYOUT);
    }
    if (this.getData() instanceof Network) {
      jungGraph = getJungGraph();
      vis = new BasicVisualizationServer<Node, Edge>(initLayout(
          layoutValue));
    } else {
      throw new ObviousRuntimeException("obvious-jung implementation"
          + "only support Network data structure");
    }
  }

  @SuppressWarnings("unchecked")
  private Layout<Node, Edge> initLayout(Object layout) {
    if (layout == null) {
      return new CircleLayout<Node, Edge>(jungGraph);
    } else {
      if (layout instanceof String) {
        try {
          Class<?> layoutClass = Class.forName((String) layout);
          Constructor<?> constructor = layoutClass.getConstructor(
              new Class[] {edu.uci.ics.jung.graph.Graph.class});
          Layout<Node, Edge> layoutInst = (Layout<Node, Edge>)
              constructor.newInstance(new Object[] {jungGraph});
          return layoutInst;
        } catch (Exception e) {
          throw new ObviousRuntimeException("Can not find layout " + layout);
        }
      }
    }
    return new CircleLayout<Node, Edge>(jungGraph);
  }


  /**
   * Gets the corresponding JUNG network.
   * @return corresponding JUNG network
   */

  @SuppressWarnings("unchecked")
  protected Graph<Node, Edge> getJungGraph() {
    if (((Network) this.getData()).getUnderlyingImpl(
        edu.uci.ics.jung.graph.Graph.class) != null) {
      return (edu.uci.ics.jung.graph.Graph<Node, Edge>) (
          (Network) this.getData()).getUnderlyingImpl(
          edu.uci.ics.jung.graph.Graph.class);
    }
    return convertToJungGraph((Network) this.getData());
  }

  /**
   * Converts an Obvious Network to a Jung network.
   * @param network network to convert
   * @return the converted jung network
   */

  @SuppressWarnings("unchecked")
  private edu.uci.ics.jung.graph.Graph<Node, Edge> convertToJungGraph(
      Network network) {
    Collection<Node> nodes = network.getNodes();
    Collection<Edge> edges = network.getEdges();
    if (edges.size() != 0 && nodes.size() != 0) {
      Schema nodeSchema = nodes.iterator().next().getSchema();
      Schema edgeSchema = edges.iterator().next().getSchema();
      Network jungNetwork = new JungObviousNetwork(nodeSchema, edgeSchema,
          network.getSourceColumnName(), network.getTargetColumnName());
      ObviousLib.fillNetwork(network, jungNetwork);
      NetworkListener listnr = new ObviousLinkNetworkListener(network);
      NetworkListener listnr2 = new ObviousLinkNetworkListener(jungNetwork);
      jungNetwork.addNetworkListener(listnr);
      network.addNetworkListener(listnr2);
      return (edu.uci.ics.jung.graph.Graph<Node, Edge>) jungNetwork
        .getUnderlyingImpl(edu.uci.ics.jung.graph.Graph.class);
    } else {
      throw new ObviousRuntimeException("Empty graph!");
    }
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
    if (renderer.getUnderlyingImpl(
        edu.uci.ics.jung.algorithms.layout.Layout.class) != null) {
    }
    throw new ObviousRuntimeException("The following renderer : "
        + renderer.toString() + " is not supported");

  }

  @Override
  public Object getAttributeValuetAt(Tuple tuple, String alias) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void initAliasMap() {
    // TODO Auto-generated method stub
  }



  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return prefuse visualization instance or null
   */

  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(BasicVisualizationServer.class)) {
      return vis;
    }
    return null;
  }

}
