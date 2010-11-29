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

package obvious.impl;

import java.util.NoSuchElementException;

import obvious.data.Table;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;

/**
 * An implementation of the interface IntIterator keeping row matching a given
 * predicate.
 * @author Pierre-Luc Hemery
 *
 */
public class FilterIntIterator implements IntIterator {

  /**
   * Predicate used to filter rows.
   */
  private Predicate pred;

  /**
   * Obvious Table to filter.
   */
  private Table table;

  /**
   * "Standard" IntIterator for the table to filter.
   */
  private IntIterator it;

  /**
   * Is the iteration over.
   */
  private boolean isIterOver = false;

  /**
   * Next value.
   */
  private int next = -1;

  /**
   * Constructor.
   * @param t an obvious table to filter
   * @param inPred an obvious predicate used to filter rows
   */
  public FilterIntIterator(Table t, Predicate inPred) {
    this.pred = inPred;
    this.table = t;
    this.it = table.rowIterator();
    this.next = nextValidRow();
  }

  /**
   * Gets the next valid row.
   * @return next valid row id.
   */
  private int nextValidRow() {
    while (it.hasNext()) {
      int id = it.nextInt();
      if (pred.apply(table, id)) {
        return id;
      }
    }
    isIterOver = true;
    return -1;
  }

  /**
   * Returns the next element in the iteration as an int.
   * @return the next element in iteration (as int)
   */
  public int nextInt() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more rows!");
    }
    int val = next;
    next = nextValidRow();
    return val;
  }

  /**
   * Returns true if the iteration has more elements. (In other words, returns
   * true if next would return an element rather than throwing an exception.)
   * @return true if the iterator has more elements.
   */
  public boolean hasNext() {
    return !isIterOver;
  }

  /**
   * Returns the next element in the iteration.
   * @return the next element in iteration (as Integer)
   */
  public Integer next() {
    return nextInt();
  }

  /**
   * Removes from the underlying collection the last element returned by the
   * iterator (optional operation). This method can be called only once per
   * call to next. The behavior of an iterator is unspecified if the
   * underlying collection is modified while the iteration is in progress in
   * any way other than by calling this method.
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
