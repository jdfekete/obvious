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

package obvious.prefuse.data;

import obvious.data.Table;
import obvious.data.util.Predicate;
import obvious.impl.TupleImpl;
import obvious.prefuse.utils.wrappers.WrapToPrefTuple;
import prefuse.data.expression.parser.ExpressionParser;

/**
 * This class is an implementation of the Obvious
 * {@link obvious.data.util.Predicate Predicate class} based on the Obvious
 * Prefuse binding. To create and apply Predicate, this class uses the Prefuse
 * predicates engine. A PrefuseObviousPredicate can be created directly by
 * wrapping an existing Prefuse Predicate or with the String description of
 * it.
 * This class enables predicates support for all Obvious bindings since
 * predicates produce by this class can be apply to whatever Obvious table.
 * @see obvious.data.util.Predicate Predicate
 * @author Hemery
 *
 */
public class PrefuseObviousPredicate implements Predicate {


  /**
   * Wrapped prefuse Predicate.
   */
  private prefuse.data.expression.Predicate prefPred;

  /**
   * Constructor.
   * @param pred prefuse predicate to wrap
   */
  public PrefuseObviousPredicate(prefuse.data.expression.Predicate pred) {
    this.prefPred = pred;
  }

  /**
   * Constructor.
   * @param expression a String expression used to build the underlying obvious
   * predicate.
   */
  public PrefuseObviousPredicate(String expression) {
    this.prefPred = (prefuse.data.expression.Predicate)
      ExpressionParser.parse(expression);
  }

  /**
   * Applies a predicate on a table's row.
   * @param table an obvious table
   * @param rowId row index
   * @return true if it applies
   */
  public boolean apply(Table table, int rowId) {
    WrapToPrefTuple tuple = new WrapToPrefTuple(new TupleImpl(table, rowId));
    return prefPred.getBoolean(tuple);
  }

}
