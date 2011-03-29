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

package obvious.prefuse.data;

import obvious.data.Edge;

/**
 * Implementation of an Obvious Edge based on Prefuse toolkit.
 * It subclasses PrefuseObviousTuple.
 * This class is mainly a factory to build Obvious compatible edge from
 * Prefuse edge.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousEdge extends PrefuseObviousTuple implements Edge {

  /**
   * Constructor for PrefuseObviousEdge.
   * @param edge a prefuse Edge
   */
  public PrefuseObviousEdge(prefuse.data.Edge edge) {
    super(edge);
  }

  /*
   * Indicates if the current edge is equals to this object.
   * For a PrefuseObviousEdge its means that their node id/key are the same.
   * @param obj test object
   * @return true if the two nodes id/keys are equal
   */
  @Override
  public boolean equals(Object obj) {
    try {
      boolean rowEqual = this.getRow() == ((PrefuseObviousEdge) obj).getRow();
      return rowEqual;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    final int startValue = 7;
    int result = startValue;
    final int multiplier = 11;
    return result * multiplier + this.getRow();
  }

}
