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

import obvious.data.Edge;
import obvious.data.Schema;
import obvious.data.Table;

/**
 * Example implementation of Interface Edge.
 * It simply subclasses TupleImpl interface.
 * @author Pierre-Luc Hemery.
 *
 */
public class EdgeImpl extends TupleImpl implements Edge {

  /**
   * Constructor for Node.
   * @param tableIn reference obvious table for this edge.
   * @param rowId index of the row represented by this edge
   */
  public EdgeImpl(Table tableIn, int rowId) {
    super(tableIn, rowId);
  }

  /**
   * Constructor from a schema with input values.
   * The values have to be ordered in the correct order for the schema.
   * @param schema schema for the tuple
   * @param values value for the tuple
   */
  public EdgeImpl(Schema schema, Object[] values) {
    super(schema, values);
  }

  /**
   * Indicates if the current edge is equals to this object.
   * It compares the schema, and the value for each column of the schema.
   * @param o test object
   * @return true if the two edges are equal
   */
  @Override
  public boolean equals(Object o) {
    try {
      Edge edge = (Edge) o;
      if (getSchema().getColumnCount() != edge.getSchema().getColumnCount()) {
        return false;
      } else {
        boolean equals = true;
        for (int i = 0; i < this.getSchema().getColumnCount(); i++) {
          if (!get(i).equals(edge.get(this.getSchema().getColumnName(i)))) {
            equals = false;
          }
        }
        return equals;
      }
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    final int startValue = 7;
    final int multiplier = 11;
    int result = startValue;
    for (int i = 0; i < this.getSchema().getColumnCount(); i++) {
      result = result * multiplier + this.get(i).hashCode();
    }
    return result;
  }

}
