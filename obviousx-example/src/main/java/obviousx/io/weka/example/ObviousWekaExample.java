package obviousx.io.weka.example;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;
import obviousx.io.weka.ObviousWekaLoader;
import weka.classifiers.trees.J48;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.Range;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveType;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class ObviousWekaExample {

  public static void main(String[] args) throws Exception {

    System.setProperty("obvious.DataFactory",
    "obvious.prefuse.PrefuseDataFactory");
    CSVImport importer = new CSVImport(new File(
        "src//main/resources//bank-data.csv"), ',');
    Table table = importer.loadTable();

   ObviousWekaLoader loader = new ObviousWekaLoader(table);
   Instances inst = loader.loadInstances();
   inst.setClassIndex(6);
   System.out.println(inst.toSummaryString());
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

   StringToNominal sToN = new StringToNominal();
   sToN.setAttributeRange(Range.indicesToRangeList(indices));
   sToN.setInputFormat(inst);
   Instances sToNInst = Filter.useFilter(inst, sToN);

   RemoveType removeFilter = new RemoveType();
   String[] options = new String[2];
   options[0] = "-T";
   options[1] = "string";
   removeFilter.setOptions(options);
   removeFilter.setInputFormat(sToNInst);

   Instances newInst = Filter.useFilter(sToNInst, removeFilter);
   newInst.deleteAttributeAt(0);
   System.out.println(newInst.toSummaryString());
   //System.out.println(newInst);
   
   /*
   SimpleKMeans sKM = new SimpleKMeans();
   sKM.setMaxIterations(16);
   sKM.setNumClusters(6);
   ClusterEvaluation eval = new ClusterEvaluation();
   sKM.buildClusterer(newInst);
   eval.setClusterer(sKM);
   eval.evaluateClusterer(new Instances(newInst));
   System.out.println(eval.clusterResultsToString());
   System.out.println(sKM.getSquaredError());
   */
   J48 cls = new J48();
   cls.buildClassifier(newInst);
   newInst.setClassIndex(4);
// display tree
   TreeVisualizer tv = new TreeVisualizer(
   null, cls.graph(), new PlaceNode2());
   JFrame jf = new JFrame("Weka Classifier Tree Visualizer: J48");
   jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   jf.setSize(800, 600);
   jf.getContentPane().setLayout(new BorderLayout());
   jf.getContentPane().add(tv, BorderLayout.CENTER);
   jf.setVisible(true);
   // adjust tree
   tv.fitToScreen();
  }
}
