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

package obvious.demo.paper;

import java.io.File;
import java.util.ArrayList;

import obvious.data.Table;
import obviousx.io.CSVImport;
import obviousx.io.weka.ObviousWekaInstances;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 * Using weka with Obvious.
 * This is an example from evaluation section of the paper.
 * @author Hemery
 *
 */
public final class WekaArticleExample {

  /**
   * Constructor.
   */
  private WekaArticleExample() {
  }

  /**
   * Main method.
   * @param args arguments of the main
   * @throws Exception if something bad happens when playing with data
   */
  public static void main(final String[] args) throws Exception {

    // Creating the obvious data structure.
    System.setProperty("obvious.DataFactory",
    "obvious.prefuse.data.PrefuseDataFactory");
    CSVImport importer = new CSVImport(new File(
        "src//main/resources//bank-data.csv"), ',');
    Table table = importer.loadTable();

    // Wrapping obvious table into the weka instances.
    Instances inst = new ObviousWekaInstances(table, "test",
        new FastVector(), table.getRowCount());
    // Giving some stats about the Instances and the age column...
    System.out.println(inst.toSummaryString());
    System.out.println("Age mean : " + inst.meanOrMode(1));
    System.out.println("Age variance : " + inst.variance(1));
    System.out.println(" Smallest value : " + inst.kthSmallestValue(1, 1));

    // Since the ID column has no meaning for clustering this instances,
    // we retrieve id values and remove them from the Instances.
    ArrayList<Integer> indicesArray = new ArrayList<Integer>();
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      if (table.getSchema().getColumnType(i).equals(String.class)) {
        indicesArray.add(i);
      }
    }

    int[] indices = new int[indicesArray.size()];
    for (int i = 0; i < indicesArray.size(); i++) {
      indices[i] = indicesArray.get(i);
    }

    Remove remove = new Remove();
    remove.setAttributeIndicesArray(new int[] {0});
    remove.setInputFormat(inst);
    Instances newInst = Filter.useFilter(inst, remove); // new instance
    System.out.println(newInst.toSummaryString());  // presenting filtered inst

    // Clustering
    SimpleKMeans sKM = new SimpleKMeans();
    final int numberOfClusters = 5;
    sKM.setNumClusters(numberOfClusters);
    ClusterEvaluation eval = new ClusterEvaluation();
    sKM.buildClusterer(newInst);
    eval.setClusterer(sKM);
    eval.evaluateClusterer(new Instances(newInst));
    System.out.println(eval.clusterResultsToString());
    System.out.println(sKM.getSquaredError());

  }

}
