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

package obviousx.wrappers.ivtk;

import infovis.utils.RowIterator;

/**
 * Wrapper for obvious row iterator to ivtk row iterator.
 * @author Hemery
 *
 */
public class WrapToIvtkIterator implements infovis.utils.RowIterator {

  /**
   * Backing iterator.
   */
  private obvious.data.util.IntIterator it;

  /**
   * Backing row index.
   */
  private int currentRow = 0;

  /**
   * Constructor.
   * @param iterator an obvious iterator
   */
  public WrapToIvtkIterator(obvious.data.util.IntIterator iterator) {
    this.it = iterator;
  }

  @Override
  public RowIterator copy() {
    return new WrapToIvtkIterator(it);
  }

  @Override
  public int nextRow() {
    currentRow = it.nextInt();
    return currentRow;
  }

  @Override
  public int peekRow() {
    return currentRow + 1;
  }

  @Override
  public boolean hasNext() {
    return it.hasNext();
  }

  @Override
  public Object next() {
    return it.next();
  }

  @Override
  public void remove() {
    it.remove();
  }
}

