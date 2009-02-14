
package obvious;

import obvious.Graph;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public abstract class AbstractGraphTest {

	protected Graph<String,String> graph;

	@Test
	public void testAddNode() {
		assertTrue( graph.addNode("homer") );
		assertTrue( graph.addNode("marge") );
	}

	@Test
	public void testAddEdge() {
		graph.addNode("a");
		graph.addNode("b");
		graph.addNode("c");
		assertTrue( graph.addEdge("ab-dir","a","b",Graph.EdgeType.DIRECTED) );
		assertTrue( graph.addEdge("bc-undir","b","c",Graph.EdgeType.UNDIRECTED) );
	}
}

