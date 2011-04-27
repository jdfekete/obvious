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
import java.util.Map;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import obvious.ObviousRuntimeException;
import obvious.data.Data;
import obvious.data.Forest;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Edge;
import obvious.data.Tuple;
import obvious.data.util.Predicate;
import obvious.jung.utils.wrappers.WrapToJungGraph;
import obvious.jung.utils.wrappers.WrapToJungTree;
import obvious.jung.view.ObviousVisualizationServer;
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
   * Underlying JUNG visualization.
   */
   private VisualizationViewer<Node, Edge> vis;

   /**
    * A Jung graph used for this obvious implementation.
    */
   private edu.uci.ics.jung.graph.Graph<Node, Edge> jungGraph = null;

   /**
    * A Jung graph used for this obvious implementation.
    */
   private edu.uci.ics.jung.graph.Tree<Node, Edge> jungTree = null;

   /**
    * A Jung forest used for this obvious implementation.
    */
   private edu.uci.ics.jung.graph.Forest<Node, Edge> jungForest = null;

   /**
    * Constructor.
    * @param inData obvious data instance
    * @param predicate a Predicate used to filter the table
    * @param visName name of the visualization technique to used (if needed)
    * @param param parameters of the visualization
    */
   public JungObviousVisualization(Data inData, Predicate predicate,
       String visName, Map<String, Object> param) {
     super(inData, predicate, visName, param);
   }

  @Override
  protected void initVisualization(Map<String, Object> param) {
    String layoutValue = null;
    if (param != null) {
      layoutValue = (String) param.get(LAYOUT);
    }
    if (this.getData() instanceof obvious.data.Tree<?, ?>) {
      jungTree = getJungTree();
      vis = new ObviousVisualizationServer(initLayout(
          layoutValue));
    } else if (this.getData() instanceof Network) {
      jungGraph = getJungGraph();
      vis = new ObviousVisualizationServer(initLayout(
          layoutValue));
    } else if (this.getData() instanceof Forest<?, ?>) {
      jungForest = getJungForest();
      vis = new ObviousVisualizationServer(initLayout(
          layoutValue));
    } else {
      throw new ObviousRuntimeException("obvious-jung implementation"
          + "only support Network data structure");
    }
  }

  /**
   * Inits the JUNG layout.
   * @param layout classpath of the JUNG layout
   * @return a JUNG layout
   */
  @SuppressWarnings("unchecked")
  private Layout<Node, Edge> initLayout(Object layout) {
    if (layout == null) {
      return new CircleLayout<Node, Edge>(jungGraph);
    } else {
      if (layout instanceof String) {
        try {
          Layout<Node, Edge> layoutInst;
          Class<?>  layoutClass = Class.forName((String) layout);
          if (jungTree != null) {
            Constructor<?> constructor = layoutClass.getConstructor(
                new Class[] {edu.uci.ics.jung.graph.Tree.class});
            layoutInst = (Layout<Node, Edge>)
                constructor.newInstance(new Object[] {jungTree});
          } else if (jungForest != null) {
            Constructor<?> constructor = layoutClass.getConstructor(
                new Class[] {edu.uci.ics.jung.graph.Forest.class});
            layoutInst = (Layout<Node, Edge>)
                constructor.newInstance(new Object[] {jungForest});
          } else {
            Constructor<?> constructor = layoutClass.getConstructor(
                new Class[] {edu.uci.ics.jung.graph.Graph.class});
            layoutInst = (Layout<Node, Edge>)
                constructor.newInstance(new Object[] {jungGraph});
          }
          return layoutInst;
        } catch (Exception e) {
          e.printStackTrace();
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
   * Gets a Jung Tree.
   * @return a Jung Tree.
   */
  @SuppressWarnings("unchecked")
  protected edu.uci.ics.jung.graph.Tree<Node, Edge> getJungTree() {
    if (((obvious.data.Tree) this.getData()).getUnderlyingImpl(
        edu.uci.ics.jung.graph.Tree.class) != null) {
      return (edu.uci.ics.jung.graph.Tree<Node, Edge>) (
          (Network) this.getData()).getUnderlyingImpl(
          edu.uci.ics.jung.graph.Tree.class);
    }
    return new WrapToJungTree((obvious.data.Tree<Node, Edge>) this.getData());
  }

  /**
   * Gets a Jung Forest.
   * @return a Jung Forest.
   */
  @SuppressWarnings("unchecked")
  protected edu.uci.ics.jung.graph.Forest<Node, Edge> getJungForest() {
    if (((obvious.data.Forest) this.getData()).getUnderlyingImpl(
        edu.uci.ics.jung.graph.Forest.class) != null) {
          return (edu.uci.ics.jung.graph.Forest<Node, Edge>)
          ((obvious.data.Forest) this.getData()).getUnderlyingImpl(
              edu.uci.ics.jung.graph.Tree.class);
    } else {
      throw new ObviousRuntimeException("The current implementation of Forest"
          + "is unsupported");
    }
  }

  /**
   * Converts an Obvious Network to a Jung network.
   * @param network network to convert
   * @return the converted jung network
   */
  private edu.uci.ics.jung.graph.Graph<Node, Edge> convertToJungGraph(
      Network network) {
    /*
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
      */
      return new WrapToJungGraph(network);
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
      return;
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
    if (type.equals(VisualizationViewer.class)) {
      return vis;
    }
    return null;
  }

}
