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
      Map<Node, double[]> position = layout.getNodePositions();
      Node targetNode = new Node(target, 1.0f);
      if (position.containsKey(targetNode) == false) {
        position.put(targetNode, new double[3]);
      }
      position.get(targetNode)[0] = Math.random() - 0.5;
      position.get(targetNode)[1] = Math.random() - 0.5;
      position.get(targetNode)[2] = 0.0f;
      layout.setNodePositions(position);
      Map<String,Node> nameToNode = layout.makeNodes(layout.getGraphStruct());
      List<Node> nodes = new ArrayList<Node>(nameToNode.values());
      List<Edge> edges = layout.makeEdges(layout.getGraphStruct(),nameToNode);
      System.out.println(nodes.size() + " : " + edges.size());
      new MinimizerBarnesHut(nodes, edges, 0.0, 1.0, 0.05).
      minimizeEnergy(layout.getNodePositions(), 10);
      layout.setNodeCluster(new OptimizerModularity().execute(nodes, edges, false));      
    }
  }

}
