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

package obvious.viz;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import obvious.data.Data;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Table;
import obvious.data.Tree;
import obvious.data.util.Predicate;
import obvious.util.Adaptable;

/**
 * Visualization interface.
 * @author Hemery
 *
 */
public abstract class Visualization extends VisualAttributeManager
    implements Adaptable {

  /**
   * Visualization technique name.
   */
  private String visualizatioName;

  /**
   * Predicate used to select the data.
   */
  private Predicate pred;

  /**
   * Backing table (if needed).
   */
  private Data data;

  /**
   * Constructor.
   * @param parentTable Obvious data table
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technic name
   */
  public Visualization(Table parentTable, Predicate predicate, String visName) {
    this.data = parentTable;
    this.pred = predicate;
    this.visualizatioName = visName;
  }

  /**
   * Constructor.
   * @param parentNetwork Obvious data network
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technique name
   */
  public Visualization(Network parentNetwork, Predicate predicate,
      String visName) {
    this.data = parentNetwork;
    this.pred = predicate;
    this.visualizatioName = visName;
  }

  /**
   * Constructor.
   * @param parentTree Obvious data tree
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technique name
   */
  public Visualization(Tree<Node, Edge> parentTree, Predicate predicate,
      String visName) {
    this.data = parentTree;
    this.pred = predicate;
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
   * Gets the obvious data associated to this visualization.
   * @return the obvious data associated to this visualization
   */
  public Data getData() {
    return this.data;
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
