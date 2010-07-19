package noack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
  
  private Map<String,Map<String,Double>> graph;
  
  private ArrayList<GraphListener> listener;

  public Graph() {
    this.graph = new HashMap<String, Map<String, Double>>();
    this.listener = new ArrayList<GraphListener>();
  }
  
  public Map<String, Map<String, Double>> getGraph() {
    return this.graph;
  }
  
  public List<Node> getNodes() {
    Map<String,Node> nameToNode = makeNodes(graph);
    return new ArrayList<Node>(nameToNode.values());
  }
  
  public List<Edge> getEdges() {
    Map<String,Node> nameToNode = makeNodes(graph);
    return makeEdges(graph, nameToNode);
  }
  
  public void addNode(String name) {
    if (graph.get(name) == null) {
      graph.put(name, new HashMap<String, Double>());
      this.fireGraphEvent(name, null, GraphListener.NODE_ADDED);
    }
  }
  
  public void addEdge (String source, String target, double weight) {
    if (graph.get(source) == null) {
      this.addNode(source);
    }
    graph.get(source).put(target, new Double(weight));
    this.fireGraphEvent(source, target, GraphListener.EDGE_ADDED);
  }
  
  public void addEdge (String source, String target) {
    this.addEdge(source, target, 1.0f);
  }
  
  /**
   * Gets all table listener.
   * @return a collection of table listeners.
   */
  public Collection<GraphListener> getTableListeners() {
    return listener;
  }
  
  /**
   * Adds a table listener.
   * @param listnr a graph listener
   */
  public void addTableListener(GraphListener listnr) {
    listener.add(listnr);
  }

  /**
   * Removes a table listener.
   * @param listnr an Obvious TableListener
   */
  public void removeTableListener(GraphListener listnr) {
    listener.remove(listnr);
  }
  
  /**
   * Notifies changes to listener.
   * @param type the type of modification
   */
  protected void fireGraphEvent(String source, String target, int type) {
    if (this.getTableListeners().isEmpty()) {
      return;
    }
    for (GraphListener listnr : this.getTableListeners()) {
      listnr.graphChanged(this, source, target, type);
    }
  }
   
  /**
   * Construct a map from node names to nodes for a given graph, 
   * where the weight of each node is set to its degree,
     * i.e. the total weight of its edges. 
   * 
   * @param graph the graph.
   * @return map from each node names to nodes.
   */
  private static Map<String,Node> makeNodes(Map<String,Map<String,Double>> graph) {
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
  private static List<Edge> makeEdges(Map<String,Map<String,Double>> g, 
          Map<String,Node> nameToNode) {
      List<Edge> result = new ArrayList<Edge>();
      for (String sourceName : g.keySet()) {
          for (String targetName : g.get(sourceName).keySet()) {
              Node sourceNode = nameToNode.get(sourceName);
              Node targetNode = nameToNode.get(targetName);
              double weight = g.get(sourceName).get(targetName).doubleValue();
              result.add( new Edge(sourceNode, targetNode, weight) );
          }
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
  private static Map<String,Map<String,Double>> makeSymmetricGraph
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
}
