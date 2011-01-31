/*
* Copyright (c) 2011, INRIA
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

package obvious.jung.view;

import java.awt.Graphics;
import java.awt.Graphics2D;


import obvious.data.Edge;
import obvious.data.Node;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;




/**
 * Extended Jung view to support pan and zoom mechanismn in obvious.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class ObviousVisualizationServer
    extends VisualizationViewer<Node, Edge> {

  /**
   * Checks if the component has already been displayed.
   */
  private boolean firstLaunch = false;

  /**
   * Translations coordinates.
   */
  private double dx = -1, dy = -1, scale = 0;

  /**
   * Constructor.
   * @param layout Jung layout used for the current network
   */
  public ObviousVisualizationServer(Layout<Node, Edge> layout) {
    super(layout);
    setDoubleBuffered(true);
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    if (firstLaunch && g2d.getTransform().getTranslateX() == 0
        && g2d.getTransform().getTranslateY() == 0) {
      g2d.translate(dx, dy);
      g2d.scale(scale, scale);
    }
    dx = g2d.getTransform().getTranslateX();
    dy = g2d.getTransform().getTranslateY();
    scale = g2d.getTransform().getScaleY();
    super.paint(g2d);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    firstLaunch = true;
  }

}
