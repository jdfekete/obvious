/*
* Copyright (c) 2009, INRIA
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

import java.util.Iterator;

import obvious.data.util.IntIterator;

/**
 * A simple implementation of the interface IntIterator.
 * @author Pierre-Luc Hemery
 *
 */
public class IntIteratorImpl implements IntIterator {

  /**
   * Original iterator over integer.
   */
  private Iterator<Integer> iter;

  /**
   * Constructor from an existing Iterator instance.
   * @param it an Iterator over Integer instance
   */
  public IntIteratorImpl(Iterator<Integer> it) {
    this.iter = it;
  }

  /**
   * Returns the next element in the iteration as an int.
   * @return the next element in iteration (as int)
   */
  public int nextInt() {
    return this.iter.next();
  }

  /**
   * Returns true if the iteration has more elements. (In other words, returns
   * true if next would return an element rather than throwing an exception.)
   * @return true if the iterator has more elements.
   */
  public boolean hasNext() {
    return this.iter.hasNext();
  }

  /**
   * Returns the next element in the iteration.
   * @return the next element in iteration (as Integer)
   */
  public Integer next() {
    return this.iter.next();
  }

  /**
   * Removes from the underlying collection the last element returned by the
   * iterator (optional operation). This method can be called only once per
   * call to next. The behavior of an iterator is unspecified if the
   * underlying collection is modified while the iteration is in progress in
   * any way other than by calling this method.
   */
  public void remove() {
    this.iter.remove();
  }

}
