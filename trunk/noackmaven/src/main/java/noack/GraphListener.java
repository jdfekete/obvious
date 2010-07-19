package noack;

public interface GraphListener {

  public static final int NODE_ADDED = 0;
  public static final int EDGE_ADDED = 1;
  
  /**
   * Notification that a graph has changed.
   */
  void graphChanged(Graph g, String source, String target, int type);
  
  void beginEdit();
  
  void endEdit();
  
}
