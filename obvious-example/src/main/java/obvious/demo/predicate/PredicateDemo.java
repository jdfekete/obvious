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

package obvious.demo.predicate;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obvious.impl.TupleImpl;
//import obvious.improvise.data.ImproviseObviousTable;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.prefuse.data.PrefuseObviousPredicate;
import obvious.prefuse.data.PrefuseObviousTable;
import prefuse.data.expression.parser.ExpressionParser;

/**
 * Demo for the Predicate support in Obvious.
 * @author Hemery
 *
 */
public final class PredicateDemo {

  /**
   * Constructor.
   */
  private PredicateDemo() { }

  /**
   * Main method.
   * @param args arguments of the main.
   */
  public static void main(final String[] args) {

    // Create schema.
    Schema simpleSchema = new IvtkObviousSchema();
    simpleSchema.addColumn("value", Integer.class, new Integer(0));

    // Create and fill tables.
    Table prefTable = new PrefuseObviousTable(simpleSchema);
    Table ivtkTable = new IvtkObviousTable(simpleSchema);
    //Table improviseTable = new ImproviseObviousTable(simpleSchema);

    final int endLoop = 10;

    for (int i = 0; i < endLoop; i++) {
      prefTable.addRow(new TupleImpl(simpleSchema, new Object[] {i}));
      ivtkTable.addRow(new TupleImpl(simpleSchema, new Object[] {i}));
      //improviseTable.addRow(new TupleImpl(simpleSchema, new Object[] {i}));
    }

    // Create the predicate.
    String predString = "value < 5";
    prefuse.data.expression.Predicate p = (prefuse.data.expression.Predicate)
        ExpressionParser.parse(predString);
    Predicate pred = new PrefuseObviousPredicate(p);

    // Filtering columns...
    IntIterator prefIt = prefTable.rowIterator(pred);
    System.out.print("PrefuseObviousTable filtered rows : ");
    while (prefIt.hasNext()) {
      System.out.print(prefIt.nextInt() + " ");
    }
    System.out.print("\n");

    IntIterator ivtkIt = ivtkTable.rowIterator(pred);
    System.out.print("IvtkObviousTable filtered rows : ");
    while (ivtkIt.hasNext()) {
      System.out.print(ivtkIt.nextInt() + " ");
    }
    System.out.print("\n");

    /*
    IntIterator improviseIt = improviseTable.rowIterator(pred);
    System.out.print("ImproviseObviousTable filtered rows : ");
    while (improviseIt.hasNext()) {
      System.out.print(improviseIt.nextInt() + " ");
    }
    System.out.print("\n");
    */
  }

}
