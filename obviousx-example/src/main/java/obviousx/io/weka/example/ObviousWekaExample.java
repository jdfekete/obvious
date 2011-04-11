package obviousx.io.weka.example;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;
import obviousx.io.weka.ObviousWekaInstances;
import obviousx.io.weka.ObviousWekaLoader;
import weka.classifiers.trees.J48;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Range;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveType;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class ObviousWekaExample {

  public static void main(String[] args) throws Exception {

    System.setProperty("obvious.DataFactory",
    "obvious.prefuse.data.PrefuseDataFactory");
    CSVImport importer = new CSVImport(new File(
        "src//main/resources//bank-data.csv"), ',');
    Table table = importer.loadTable();

   /*
   ObviousWekaLoader loader = new ObviousWekaLoader(table);
   Instances inst = loader.loadInstances();
   inst.setClassIndex(6); */
   //System.out.println(inst.toSummaryString());

   /*
   System.out.println(inst.toSummaryString());
   System.out.println(inst.meanOrMode(2));
   System.out.println(inst.variance(2));
   System.out.println(inst.kthSmallestValue(2, 50));
   */

   /*
   StringToNominal sToN = new StringToNominal();
   sToN.setInputFormat(inst);
  */

   Instances inst = new ObviousWekaInstances(table, "test",
        new FastVector(), table.getRowCount());
   //inst.setClassIndex(2);
   System.out.println(inst.toSummaryString());
   System.out.println(inst.meanOrMode(1));
   System.out.println(inst.variance(1));
   System.out.println(inst.kthSmallestValue(1, 1));
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
   Instances newInst = Filter.useFilter(inst, remove);
   System.out.println(newInst.toSummaryString());
   SimpleKMeans sKM = new SimpleKMeans();
   sKM.setNumClusters(5);
   ClusterEvaluation eval = new ClusterEvaluation();
   sKM.buildClusterer(newInst);
   eval.setClusterer(sKM);
   eval.evaluateClusterer(new Instances(newInst));
   System.out.println(eval.clusterResultsToString());
   System.out.println(sKM.getSquaredError());

  }
}
