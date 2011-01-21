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

package obvious.view;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;

import obvious.util.Adaptable;
import obvious.view.event.ViewListener;
import obvious.viz.Visualization;

/**
 * View interface for obvious toolkit.
 * @author Hemery
 *
 */
public interface View extends Adaptable {

  /**
   * Adds a listener to the view.
   * @param listener listener to add
   */
  void addListener(ViewListener listener);

  /**
   * Removes a listener from the view.
   * @param listener listener listener to remove
   * @return true if deleted
   */
  boolean removeListener(ViewListener listener);

  /**
   * Gets all view listeners.
   * @return a list of all view listeners.
   */
  Collection<ViewListener> getViewListeners();

  /**
   * Gets the Visualization backing this view.
   * @return the Visualization backing this view
   */
  Visualization getVisualization();

  /**
   * Returns a reference to the AffineTransform used by this view.
   * @return AffineTransform used by this view.
   */
  AffineTransform getTransform();

  /**
   * Sets the AffineTransform used by this view.
   * @param transform AffineTransform instance to set
   */
  void setTransform(AffineTransform transform);

  /**
   * Zooms the view to the given scale.
   * @param p anchor point for the zoom
   * @param scale scale for the zoom
   */
  void zoom(Point2D p, float scale);

  /**
   * Pans the view provided in screen coordinates.
   * @param dx the amount to pan along the x-dimension, in pixel units
   * @param dy the amount to pan along the y-dimension, in pixel units
   */
  void pan(float dx, float dy);

  /**
   * Paints the view.
   * @param g Graphics instance
   */
  void paint(Graphics g);

}
