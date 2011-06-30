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

import obvious.data.Table;
import obviousx.io.impl.CSVImport;
import obviousx.io.rminer.ObviousRMinerExTable;


import com.rapidminer.RapidMiner;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleReader;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SimpleExampleReader;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.ModelApplier;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.io.ExampleSource;
import com.rapidminer.operator.learner.Learner;
import com.rapidminer.operator.visualization.SOMModelVisualization;
import com.rapidminer.tools.OperatorService;

public class ObviousRminerApp {

  public static void main(String[] args) {

    try {
      System.setProperty("rapidminer.init.weka", "true");
      RapidMiner.init();
      System.out.println(System.getProperty("rapidminer.home"));
      System.setProperty("rapidminer.init.weka", "true");
      // Loading the source table
      System.setProperty("obvious.DataFactory",
      "obvious.prefuse.data.PrefuseDataFactory");
      CSVImport importer = new CSVImport(new File(
          "src//main/resources//bank-data.csv"), ',');
      Table table = importer.loadTable();
      ObviousRMinerExTable exTable = new ObviousRMinerExTable(table);

      CSVImport importer2 = new CSVImport(new File(
      "src//main/resources//bank-data-training.csv"), ',');
      Table bisTable = importer2.loadTable();
      ObviousRMinerExTable trainingTable = new ObviousRMinerExTable(bisTable);

      // learn
      Operator exampleSource =
          OperatorService .createOperator(ExampleSource.class);
      exampleSource.setParameter("attributes",
          "src//main/resources//bankatt.aml");
      IOContainer container = exampleSource.apply(
          new IOContainer(trainingTable.createExampleSet()));
      ExampleSet exampleSet = container.get(ExampleSet.class);

      // here the string based creation must be used since the J48 operator
      // do not have an own class ( derived from the Weka library ).
      Learner learner = (Learner) OperatorService.createOperator("J48");
      Model model = learner.learn(exampleSet);

   // loading the test set (plus adding the model to result container )
      IOContainer containerSource = new IOContainer(
          exTable.createExampleSet());
      Operator testSource =
        OperatorService .createOperator(ExampleSource.class);
      testSource.setParameter("attributes",
          "src//main/resources//bank-data.csv");
      container = testSource.apply(containerSource);
      container = container.append(model);

      Operator modelApp =
        OperatorService.createOperator(ModelApplier.class);
        container = modelApp.apply(container);
        // print results
        ExampleSet resultSet = container.get(ExampleSet.class);
        ExampleReader reader = new SimpleExampleReader(
            exTable.getDataRowReader(), resultSet);
        while (reader.hasNext()) {
          Example ex = reader.next();
          for (int i = 0; i < exTable.getAttributeCount(); i++) {
            try {
            System.out.print(
                ex.getValueAsString(exTable.getAttribute(i)) + " : ");
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          System.out.println();
        }
        SOMModelVisualization viz = new SOMModelVisualization(
            modelApp.getOperatorDescription());
        viz.apply();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
