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

package obvious.ivtk.data.event;

import infovis.table.MutableTableModelEvent;

import javax.swing.event.TableModelListener;

import obvious.data.Table;
import obvious.data.event.TableListener;
import obviousx.io.impl.ObviousTableModel;

/**
 * An ivtk based implementation of obvious TableListener interface.
 * @author Hemery
 *
 */
public class IvtkTableListener implements TableListener {

  /**
   * Wrapped listener.
   */
  private TableModelListener ivtkListener;

  /**
   * Constructor.
   * @param listener wrapped "ivkt" (swing) listener.
   */
  public IvtkTableListener(TableModelListener listener) {
    this.ivtkListener = listener;
  }

  @Override
  public void beginEdit(int context) {
  }

  @Override
  public boolean checkInvariants() {
    return false;
  }

  @Override
  public boolean endEdit(int context) {
    return false;
  }

  @Override
  public void tableChanged(Table t, int start, int end, int col, int type) {
    MutableTableModelEvent event = new MutableTableModelEvent(
        new ObviousTableModel(t));
    event.setValues(start, end, col, type);
    ivtkListener.tableChanged(event);
  }

}
