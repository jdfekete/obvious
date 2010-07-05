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

package obvious.improvise.data;

import oblivion.db.Data;
import oblivion.db.MemoryTable;
import oblivion.db.lex.LexicalData;
import oblivion.db.lex.LexicalSchema;
import oblivion.lp.Prototype;
import oblivion.lp.Variable;
import oblivion.lp.VariableEvent;
import oblivion.lp.VariableListener;
import oblivion.lpui.meta.World;
import obvious.data.Table;
import obvious.data.event.TableListener;

/**
 * Improvise listener for obvious.
 * @author plhemery
 *
 */
public class ImproviseObviousListener implements TableListener {

  /**
   * Surrounding oblivion variable.
   */
  private Variable var;

  /**
   * Wrapped oblivion variable listener.
   */
  private oblivion.lp.VariableListener listener;


  /**
   * The number of time beginEdit has been called minus the number of time
   * endEdit has been called.
   */
  private int inhibitNotify = 0;

  /**
   * Constructor.
   * @param table an obvious improvise table
   * @param lstnr an oblivion VariableListener instance
   */
  public ImproviseObviousListener(ImproviseObviousTable table,
      VariableListener lstnr) {
    this.listener = lstnr;
    oblivion.db.MemoryTable mTable =
      (MemoryTable) table.getUnderlyingImpl(MemoryTable.class);
    LexicalData lexData = new LexicalData(new LexicalSchema(mTable.getSchema()),
        new Data(mTable));
    var = new Variable(new Prototype(LexicalData.class, lexData));
    var.addVariableListener(listener);
    World.world(var);
  }

  @Override
  public void beginEdit(int context) {
    inhibitNotify++;
  }

  @Override
  public void endEdit(int context) {
    inhibitNotify--;
    if (inhibitNotify <= 0) {
        inhibitNotify = 0;
    }
  }

  @Override
  public void tableChanged(Table t, int start, int end, int col, int type) {
    if (inhibitNotify != 0) {
      return;
    } else {  
      VariableEvent event = new VariableEvent(var, VariableEvent.VALUE_CHANGED);
      listener.variableChanged(event);
    }
  }

}
