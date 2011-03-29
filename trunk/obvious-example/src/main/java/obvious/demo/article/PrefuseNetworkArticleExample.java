package obvious.demo.article;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Network;
import obvious.prefuse.data.PrefuseObviousNetwork;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousNetworkViz;
import obvious.view.control.PanControl;
import obvious.view.control.ZoomControl;
import obvious.viz.Visualization;
import obviousx.ObviousxException;
import obviousx.io.GraphMLImport;
import prefuse.controls.DragControl;
import prefuse.util.GraphLib;

public class PrefuseNetworkArticleExample {

  /**
   * Main method.
   * @param args arguments of the main
   * @throws ObviousxException if something bad happens
   */
  public static void main(String[] args) throws ObviousxException {
    /*
     * Creation of the graph structure.
     */
   System.setProperty("obvious.DataFactory",
    "obvious.prefuse.PrefuseDataFactory");
   GraphMLImport importer = new GraphMLImport(new File(
       "src//main//resources//example.graphML"));
   Network network = importer.loadGraph();

   /*
    * Creation the extra map parameters, to customize the
    * “monolithic prefuse component”.
    * We specify a custom group label for prefuse and a custom label column for
    */
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");
    param.put(PrefuseObviousNetworkViz.LABEL_KEY, "color");

    /*
     * Creating the visualization from a predefined Obvious-prefuse
     * visualization component.
     */
    Visualization vis = new PrefuseObviousNetworkViz(
        network, null, null, param);


    /*
     * Building view.
     */

    PrefuseObviousView view = new PrefuseObviousView(
        vis, null, "graph", param);
     view.getViewJComponent().addMouseListener(new PanControl(view));
     view.getViewJComponent().addMouseMotionListener(new PanControl(view));
     view.getViewJComponent().addMouseMotionListener(new ZoomControl(view));
    JFrame frame = new JFrame("GraphView example from prefuse based"
              + " on obvious");
     frame.add(view.getViewJComponent());
     frame.pack();
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setVisible(true);
     // Mandatory to launch the visualization, but should be done
     // in a nicer way.
    // Actions are really specific of Prefuse...
    prefuse.Visualization realPrefVis = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    realPrefVis.run("color");  // assign the colors
    realPrefVis.run("layout"); // start up the animated layout
  }

}
