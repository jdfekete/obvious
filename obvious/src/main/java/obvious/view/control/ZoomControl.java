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

package obvious.view.control;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import obvious.view.View;

/**
 * A zoom control for obvious views based on swing.
 * @author Hemery
 *
 */
public class ZoomControl extends MouseAdapter {


  /**
   * Reference y position.
   */
  private int refY = -1;

  /**
   * Obvious view.
   */
  private View view;

  /**
   * Constructor.
   * @param inView obvious view
   */
  public ZoomControl(View inView) {
    this.view = inView;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      e.getComponent().setCursor(Cursor.getPredefinedCursor(
          Cursor.MOVE_CURSOR));
      refY = e.getY();
    }

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      e.getComponent().setCursor(Cursor.getPredefinedCursor(
          Cursor.DEFAULT_CURSOR));
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
      int y = e.getY();
      int dy = y - refY;
      final int scaleFactor = 100;
      double scale = 1 + ((double) dy) / scaleFactor;
      view.zoom(e.getPoint(), (float) scale);
      refY = y;
    }
  }
}

