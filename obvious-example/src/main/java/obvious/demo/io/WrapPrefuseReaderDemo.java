/*
* Copyright (c) 2011, INRIA
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

package obvious.demo.io;

import java.io.File;

import obvious.data.Network;
import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.io.PrefuseObviousGraphReader;
import obviousx.io.PrefuseObviousTableReader;

import prefuse.data.io.CSVTableReader;
import prefuse.data.io.GraphMLReader;

/**
 * Example for wrapped prefuse reader.
 * @author Hemery
 *
 */
public final class WrapPrefuseReaderDemo {

  /**
   * Constructor.
   */
  private WrapPrefuseReaderDemo() { };

  /**
   * Main method.
   * @param args arguments
   * @throws ObviousxException if something bad happens
   */
  public static void main(final String[] args) throws ObviousxException {
    File file = new File("src//main//resources//state.txt");
    PrefuseObviousTableReader tableReader = new PrefuseObviousTableReader(
        new CSVTableReader(), file);
    tableReader.loadTable();
    Table table = (Table) tableReader.loadTable();
    for (int i = 0; i < table.getRowCount(); i++) {
      for (int j = 0; j < table.getSchema().getColumnCount(); j++) {
        System.out.print(table.getValue(i, j) + " : ");
      }
      System.out.println();
    }

    File graphFile = new File("src//main//resources//socialnet.xml");
    PrefuseObviousGraphReader graphReader = new PrefuseObviousGraphReader(
        new GraphMLReader(), graphFile);

    Network network = (Network) graphReader.loadGraph();
    System.out.println(
        "number of nodes for imported graph " + network.getNodes().size()
        + " : " + "number of edges for imported graph"
        + network.getEdges().size());
  }

}
