package obvious.ivtk.data;

import java.util.ArrayList;
import java.util.Collection;

import obvious.data.Edge;
import obvious.data.Forest;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;

/**
 * Implementation of an Obvious Forest based on Ivtk toolkit.
 * @author Hemery
 *
 */
public class IvtkObviousForest extends IvtkObviousNetwork
    implements Forest<Node, Edge> {

  /**
   * Schema for the nodes.
   */
  private Schema nodeSchema;

  /**
   * Schema for the edges.
   */
  private Schema edgeSchema;

  /**
   * Constructor.
   * @param nodeSchema an Obvious schema
   * @param edgeSchema an Obvious schema
   */
  public IvtkObviousForest(Schema nodeSchema, Schema edgeSchema) {
    super(nodeSchema, edgeSchema, true);
    this.nodeSchema = nodeSchema;
    this.edgeSchema = edgeSchema;
  }

  @Override
  public Collection<Tree<Node, Edge>> getTrees() {
    Collection<Tree<Node, Edge>> trees = new ArrayList<Tree<Node, Edge>>();
    Collection<Node> rootNodes = new ArrayList<Node>();
    for (Node node : getNodes()) {
      if (getPredecessors(node) == null || getPredecessors(node).size() == 0) {
        rootNodes.add(node);
      }
    }
    for (Node node : rootNodes) {
      Tree<Node, Edge> tree = new IvtkObviousTree(nodeSchema, edgeSchema);
      tree.addNode(node);
      populateNetwork(tree, node);
      trees.add(tree);
    }
    return trees;
  }

  /**
   * Internal method.
   * @param tree a Jung tree
   * @param node an Obvious node
   */
  private void populateNetwork(Tree<Node, Edge> tree, Node node) {
    if (this.getSuccessors(node) != null
        || this.getSuccessors(node).size() > 0) {
      for (Node otherNode : this.getSuccessors(node)) {
        tree.addNode(otherNode);
        tree.addEdge(getConnectingEdge(node, otherNode), node, otherNode,
            EdgeType.DIRECTED);
        populateNetwork(tree, otherNode);
      }
    }
  }

}
