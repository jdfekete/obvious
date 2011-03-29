package test.obvious.prefuse;

import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.prefuse.data.PrefuseObviousTree;
import test.obvious.data.TreeTest;

/**
 * Implementation of  Table  test-case for PrefuseObviousTable implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class PrefuseTreeTest extends TreeTest {

  @Override
  public Tree<Node, Edge> newInstance(Schema nodeSchema, Schema edgeSchema) {
    String defaultSource = prefuse.data.Tree.DEFAULT_SOURCE_KEY;
    String defaultTarget = prefuse.data.Tree.DEFAULT_TARGET_KEY;
    if (!edgeSchema.hasColumn(defaultSource)) {
      edgeSchema.addColumn(defaultSource, int.class, 0);
    }
    if  (!edgeSchema.hasColumn(defaultTarget)) {
      edgeSchema.addColumn(defaultTarget, int.class, 0);
    }
    return new PrefuseObviousTree(nodeSchema, edgeSchema);
  }

}
