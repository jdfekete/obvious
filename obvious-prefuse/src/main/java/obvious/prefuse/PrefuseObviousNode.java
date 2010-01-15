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

package obvious.prefuse;

import obvious.data.Node;

/**
 * Implementation of an Obvious Node based on Prefuse toolkit.
 * It subclasses PrefuseObviousTuple.
 * This class is mainly a factory to build Obvious compatible node from
 * Prefuse node.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseObviousNode extends PrefuseObviousTuple implements Node {

  /**
   * Constructor for PrefuseObviousNode.
   * @param node a prefuse Node
   */
  public PrefuseObviousNode(prefuse.data.Node node) {
    super(node);
  }

  /*
   * Indicates if the current node is equals to this object.
   * For a PrefuseObviousNode its means that their node id/key are the same.
   * @param obj test object
   * @return true if the two nodes id/keys are equal
   */
  public boolean equals(Object obj) {
    try {
      return this.getRow() == ((PrefuseObviousNode) obj).getRow();
    } catch (ClassCastException e) {
      return false;
    }
  }

}
