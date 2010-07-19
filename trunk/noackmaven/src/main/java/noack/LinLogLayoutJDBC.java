package noack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

public class LinLogLayoutJDBC {

  private Map<Node,double[]> nodeToPosition;
  
  private Map<Node,Integer> nodeToCluster;
  
  private Graph graph;
  
  private int iter;
  
  private JFrame frame;
  
  private Map<String, Map<String, Double>> graphStruct;
  
  public LinLogLayoutJDBC(Graph graph, int iter) {
    this.graph = graph;
    this.iter = iter;
    this.graphStruct =  makeSymmetricGraph(graph.getGraph());
  }
  
  public void computeFrame() {
    Map<String,Node> nameToNode = makeNodes(graphStruct);
    List<Node> nodes = new ArrayList<Node>(nameToNode.values());
    List<Edge> edges = makeEdges(graphStruct,nameToNode);
    nodeToPosition = makeInitialPositions(nodes, false);
    new MinimizerBarnesHut(nodes, edges, 0.0, 1.0, 0.05).
        minimizeEnergy(nodeToPosition, iter);
    nodeToCluster = 
      new OptimizerModularity().execute(nodes, edges, false);
    frame = new GraphFrame(nodeToPosition, nodeToCluster);
  }
  
  public JFrame getFrame() {
    if (frame == null) {
      computeFrame();
    }
    return this.frame;
  }
  
  public Map<Node, double[]> getNodePositions() {
    return this.nodeToPosition;
  }
  
  public Map<Node, Integer> getNodeCluster() {
    return this.nodeToCluster;
  }
  
  public Map<String, Map<String, Double>> getGraphStruct() {
    return this.graphStruct;
  }
  
  public void setGraphStruct(Map<String, Map<String, Double>> g) {
    this.graphStruct = g;
  }
  
  public void setNodePositions(Map<Node, double[]> positions) {
    this.nodeToPosition = positions;
  }
  
  public void setNodeCluster(Map<Node, Integer> cluster) {
    this.nodeToCluster = cluster;
  }
  
  /**
   * Returns, for each node in a given list,
   * a random initial position in two- or three-dimensional space. 
   * 
   * @param nodes node list.
     * @param is3d initialize 3 (instead of 2) dimension with random numbers.
   * @return map from each node to a random initial positions.
   */
  protected static Map<Node,double[]> makeInitialPositions(List<Node> nodes, boolean is3d) {
      Map<Node,double[]> result = new HashMap<Node,double[]>();
    for (Node node : nodes) {
            double[] position = { Math.random() - 0.5,
                                  Math.random() - 0.5,
                                  is3d ? Math.random() - 0.5 : 0.0 };
            result.put(node, position);
    }
    return result;
  }
  
  /**
   * Returns a symmetric version of the given graph.
   * A graph is symmetric if and only if for each pair of nodes,
   * the weight of the edge from the first to the second node
   * equals the weight of the edge from the second to the first node.
   * Here the symmetric version is obtained by adding to each edge weight
   * the weight of the inverse edge.
   * 
   * @param graph  possibly unsymmetric graph.
   * @return symmetric version of the given graph.
   */
  protected static Map<String,Map<String,Double>> makeSymmetricGraph
      (Map<String,Map<String,Double>> graph) {
    Map<String,Map<String,Double>> result = new HashMap<String,Map<String,Double>>();
    for (String source : graph.keySet()) {
      for (String target : graph.get(source).keySet()) {
        double weight = graph.get(source).get(target).doubleValue();
        double revWeight = 0.0f;
        if (graph.get(target) != null && graph.get(target).get(source) != null) {
          revWeight = graph.get(target).get(source).doubleValue();
        }
        if (result.get(source) == null) result.put(source, new HashMap<String,Double>());
        result.get(source).put(target, new Double(weight+revWeight));
        if (result.get(target) == null) result.put(target, new HashMap<String,Double>());
        result.get(target).put(source, new Double(weight+revWeight));
      }
    }
    return result;
  }
  
  /**
   * Construct a map from node names to nodes for a given graph, 
   * where the weight of each node is set to its degree,
     * i.e. the total weight of its edges. 
   * 
   * @param graph the graph.
   * @return map from each node names to nodes.
   */
  protected static Map<String,Node> makeNodes(Map<String,Map<String,Double>> graph) {
    Map<String,Node> result = new HashMap<String,Node>();
    for (String nodeName : graph.keySet()) {
            double nodeWeight = 0.0;
            for (double edgeWeight : graph.get(nodeName).values()) {
                nodeWeight += edgeWeight;
            }
      result.put(nodeName, new Node(nodeName, nodeWeight));
    }
    return result;
  }
  
    /**
     * Converts a given graph into a list of edges.
     * 
     * @param graph the graph.
     * @param nameToNode map from node names to nodes.
     * @return the given graph as list of edges.
     */
    protected static List<Edge> makeEdges(Map<String,Map<String,Double>> graph, 
            Map<String,Node> nameToNode) {
        List<Edge> result = new ArrayList<Edge>();
        for (String sourceName : graph.keySet()) {
            for (String targetName : graph.get(sourceName).keySet()) {
                Node sourceNode = nameToNode.get(sourceName);
                Node targetNode = nameToNode.get(targetName);
                double weight = graph.get(sourceName).get(targetName).doubleValue();
                result.add( new Edge(sourceNode, targetNode, weight) );
            }
        }
        return result;
    }
  
}
