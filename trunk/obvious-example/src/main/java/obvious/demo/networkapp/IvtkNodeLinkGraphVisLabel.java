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

package obvious.demo.networkapp;

import infovis.visualization.VisualColumnDescriptor;
import infovis.visualization.render.VisualSize;

import java.util.Iterator;
import java.util.Map;

import obvious.data.Network;
import obvious.data.util.Predicate;
import obvious.ivtk.viz.util.IvtkNodeLinkGraphVis;

/**
 * Custom IvtkNodeLinkGraphVis with labels.
 * @author Hemery
 *
 */
public class IvtkNodeLinkGraphVisLabel extends IvtkNodeLinkGraphVis {

  public IvtkNodeLinkGraphVisLabel(Network parentNetwork, Predicate predicate,
      String visName, Map<String, Object> param) {
    super(parentNetwork, predicate, visName, param);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void setVisualAllColumns(Map<String, Object> param, DataModel m) {
    infovis.Visualization vis = (infovis.Visualization) getUnderlyingImpl(
        infovis.Visualization.class);
      if (param == null) {
          return;
        }
        infovis.Column labelColumn = null;
        boolean hasLabel = param.containsKey("LABEL_COLUMN");
        boolean hasSize = param.containsKey("DEFAULT_SIZE");
        if (m.equals(DataModel.TABLE)) {
          infovis.Table table = getIvtkTable();
          labelColumn = hasLabel ? table.getColumn((String) param.get(
              "LABEL_COLUMN")) : null;
        } else if  (m.equals(DataModel.NETWORK)) {
          infovis.Graph graph = getIvtkGraph();
          labelColumn = hasLabel ? graph.getVertexTable().getColumn((String)
              param.get("LABEL_COLUMN")) : null;
        } else if (m.equals(DataModel.TREE)) {
          infovis.Tree tree = getIvtkTree();
          labelColumn = hasLabel ? tree.getColumn((String) param.get(
          "LABEL_COLUMN")) : null;
        }
        vis.setVisualColumn(infovis.Visualization.VISUAL_LABEL, labelColumn);
        for (Iterator iter = vis.getVisualColumnIterator(); iter.hasNext();) {
          String name = (String) iter.next();
          if (name.equals("size")) {
            VisualColumnDescriptor vcd = vis.getVisualColumnDescriptor(name);
            if (vcd instanceof VisualSize && hasSize) {
              ((VisualSize) vcd).setDefaultSize((Integer) param.get(
                  "DEFAULT_SIZE"));
            }
          }
        }
  }
  
}
