package noack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoackListener implements GraphListener {


  /**
   * The number of time beginEdit has been called minus the number of time
   * endEdit has been called.
   */
  private int inhibitNotify = 0;

  private LinLogLayoutJDBC layout;
  
  public NoackListener(LinLogLayoutJDBC layout) {
    this.layout = layout;
  }
  
  @Override
  public void beginEdit() {
    inhibitNotify++;
  }


  @Override
  public void endEdit() {
    inhibitNotify--;
    if (inhibitNotify <= 0) {
        inhibitNotify = 0;
    }
  }

  /**
   * Exemple function : it works in a very particular case, it tests
   * severals mechanisms when the new node is in the target position.
   * Do not use in a generic case.
   */
  @Override
  public void graphChanged(Graph g, String source, String target, int type) {
    if (inhibitNotify != 0) {
      return;
    }
    if (type == EDGE_ADDED) {
      System.out.println("FIRING CHANGE");
      System.out.println("HOUBA !!!");
      Map<String, Map<String, Double>> g2 = layout.makeSymmetricGraph(g.getGraph());
      Map<String,Node> nameToNode = layout.makeNodes(g2);
      List<Node> nodes = new ArrayList<Node>(nameToNode.values());
      List<Edge> edges = layout.makeEdges(g2,nameToNode);
      Map<Node, double[]> position = layout.makeInitialPositions(nodes, false);
      new MinimizerBarnesHut(nodes, edges, 0.0, 1.0, 0.05).
      minimizeEnergy(position, 10);
      Map<Node, Integer> cluster = new OptimizerModularity().execute(nodes, edges, false);
      ((GraphFrame) layout.getFrame()).getCanvas().setPositionAndCluster(position, cluster);
      
    }
  }

}
