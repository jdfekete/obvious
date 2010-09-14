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

package obviousx.wrappers;

import prefuse.util.collections.IntIterator;

/**
 * Implementation for wrapped obvious table of prefuse row iterator.
 * @author Hemery
 *
 */
public class PrefRowIterator extends IntIterator {

  /**
   *Is the iterator reversed.
   */
  private boolean reversed;

  /**
   * Last column index.
   */
  private int last = -1;

  /**
   * Next column index.
   */
  private int next;

  /**
   * Table that contains the rows.
   */
  private WrapToPrefTable table;

  /**
   * Number of valid rows encountered.
   */
  private int validRowsEncountered = 0;


  /**
   * Constructor.
   * @param isReversed is this iterator reversed
   * @param inTable table that contains the rows
   */
  public PrefRowIterator(WrapToPrefTable inTable, boolean isReversed) {
    this.reversed = isReversed;
    this.table = inTable;
  }

  @Override
  public int nextInt() {
    last = next;
    return advance(
    );
  }

  /**
   * Advance the cursor of the iterator.
   * @return next value of the row index
   */
  protected int advance() {
    if (!reversed) {
      while (validRowsEncountered < table.getRowCount()) {
        next++;
        if (table.isValidRow(last)) {
          validRowsEncountered++;
          return last;
        }
      }
    } else {
      while (validRowsEncountered < table.getRowCount() && last > 0) {
        next--;
        if (table.isValidRow(last)) {
          validRowsEncountered++;
          return next;
        }
      }
    }
    return next;
  }

  @Override
  public boolean hasNext() {
    if (reversed) {
      return next >= 0;
    } else {
      return table.isValidRow(next);
    }
  }

  @Override
  public void remove() {
    table.removeRow(last);
  }

}
