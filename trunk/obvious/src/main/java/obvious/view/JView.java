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

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import obvious.view.event.ViewListener;


/**
 * A Swing based version of the view interface.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public abstract class JView implements View {

  /**
   * Collection of listeners for this view.
   */
  private ArrayList<ViewListener> listeners = new ArrayList<ViewListener>();

  /**
   * Adds a listener to the view.
   * @param lstnr listener to add
   */
  public abstract void addListener(ViewListener lstnr);

  /**
   * Removes a listener from the view.
   * @param listener listener listener to remove
   * @return true if deleted
   */
  public abstract boolean removeListener(ViewListener listener);

  /**
   * Gets all view listeners.
   * @return a list of all view listeners.
   */
  public Collection<ViewListener> getViewListeners() {
    return this.listeners;
  }

  /**
   * Gets the JComponent associated to the view.
   * @return the JComponent associated to the view
   */
  public abstract JComponent getViewJComponent();

  /**
  * Gets the corresponding underlying implementation.
  * @param type target class
  * @return an instance of the underlying implementation or null
  */
  public Object getUnderlyingImpl(Class<?> type) {
    return null;
  }

}
