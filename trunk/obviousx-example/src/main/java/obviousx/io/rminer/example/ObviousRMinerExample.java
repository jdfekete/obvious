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

package obviousx.io.rminer.example;

import java.io.File;
import java.io.IOException;

import com.rapidminer.RapidMiner;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleReader;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SimpleExampleReader;
import com.rapidminer.example.table.DataRowReader;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.tools.Ontology;

import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.io.impl.CSVTableImport;
import obviousx.io.rminer.ObviousRMinerExTable;
import obviousx.io.rminer.ObviousRMinerLoader;

/**
 * An example based on the ObviousX-RMiner implementation.
 * @author Hemery
 *
 */
public final class ObviousRMinerExample {

  /**
   * Constructor.
   */
  private ObviousRMinerExample() {
  }

  /**
   * Main method.
   * @param args arguments of the main
   * @throws ObviousxException if something bad happens when CSV import failed
   */
  public static void main(String[] args) throws ObviousxException {
    System.setProperty("obvious.DataFactory",
    "obvious.prefuse.data.PrefuseDataFactory");
    CSVTableImport importer = new CSVTableImport(new File(
        "src//main/resources//bank-data.csv"), ',');
    Table table = importer.loadTable();

    /*
    ObviousRMinerLoader rminerLoader = new ObviousRMinerLoader(table);
    ExampleTable exTable = rminerLoader.getExampleTable();
    */

    ObviousRMinerExTable exTable = new ObviousRMinerExTable(table);

    try {
      RapidMiner.init();
    } catch (IOException e) {
      e.printStackTrace();
    }

    ExampleSet resultSet = exTable.createExampleSet();
    ExampleReader reader = new SimpleExampleReader(
        exTable.getDataRowReader(), resultSet);
    //System.out.println(exTable.toDataString());
    while (reader.hasNext()) {
      Example ex = reader.next();
      for (int i = 0; i < exTable.getAttributeCount(); i++) {
        try {
        System.out.print(ex.getValueAsString(exTable.getAttribute(i)) + " : ");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      System.out.println();
    }

  }

}
