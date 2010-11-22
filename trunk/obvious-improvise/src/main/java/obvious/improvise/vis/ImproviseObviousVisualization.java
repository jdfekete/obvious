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

package obvious.improvise.vis;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import oblivion.lpui.awt.ControlComponent;
import obvious.ObviousException;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Table;
import obvious.data.Tree;
import obvious.data.Tuple;
import obvious.data.util.Predicate;
import obvious.viz.Action;
import obvious.viz.Renderer;
import obvious.viz.Visualization;

public class ImproviseObviousVisualization extends Visualization {

  /**
   * Improvise "Data tag" key for the parameter map.
   * This constant is used to specify custom tag for data. 
   * By default, the tag is "Data".
   */
  public final static String DATA_TAG = "Data";
  
  /**
   * Improvise "Filter tag" key for the parameter map.
   * This constant is used to specify custom tag for filter. 
   * By default, the tag is "Filter".
   */
  public final static String FILTER_TAG = "Filter";
  
  /**
   * Wrapped ControlComponent improvise instance.
   */
  private ControlComponent controlComp;
  
  /**
   * Constructor.
   * @param parentTable Obvious data table
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technic name
   */
  public ImproviseObviousVisualization(Table parentTable, Predicate predicate,
      String visName) {
    super(parentTable, predicate, visName);
  }

  /**
   * Constructor.
   * @param parentNetwork Obvious data network
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technique name
   */
  public ImproviseObviousVisualization(Network parentNetwork,
      Predicate predicate, String visName) {
    super(parentNetwork, predicate, visName);
  }
  
  /**
   * Constructor.
   * @param parentNetwork Obvious data tree
   * @param predicate Obvious predicate (i.e. filter)
   * @param visName Visualization technique name
   */
  public ImproviseObviousVisualization(Tree<Node, Edge> parentTree,
      Predicate predicate, String visName) {
    super(parentTree, predicate, visName);
  }
  
  @Override
  public ArrayList<Integer> pickAll(Rectangle2D hitBox, Rectangle2D bounds) {
    // TODO Auto-generated method stub
    return null;
  }
  
  protected void initVisualization() {
    try {
      controlComp = ImproviseVisFactory.getInstance().createControlComponent(
          getVisualizationTechnique());
    } catch (ObviousException e) {
      e.printStackTrace();
    }
    if (this.getData() instanceof Table) {
      if (((Table) getData()).getUnderlyingImpl(oblivion.db.MemoryTable.class)
          != null) {
        controlComp.describe();
      }
    } else if (this.getData() instanceof Network) {
      return;
    }
  }

  @Override
  public void putAction(String actionName, Action action) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setRenderer(Renderer renderer) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Object getAttributeValuetAt(Tuple tuple, String alias) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void initAliasMap() {
  }

  @Override
  public Object getUnderlyingImpl(Class<?> type) {
    if (type.equals(controlComp.equals(ControlComponent.class))) {
      return controlComp;
    }
    return null;
  }

}
