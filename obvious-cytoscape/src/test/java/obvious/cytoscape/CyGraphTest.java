
package obvious.cytoscape;

import org.cytoscape.model.internal.CyNetworkFactoryImpl;

import org.cytoscape.event.CyEvent;
import org.cytoscape.event.CyListener;
import org.cytoscape.event.CyEventHelper;

import obvious.AbstractGraphTest;

import org.junit.Before;

public class CyGraphTest extends AbstractGraphTest {

	@Before
	public void setup() {
		graph = new CyGraph( new CyNetworkFactoryImpl(new DummyEventHelper()) );
	}

	private static class DummyEventHelper implements CyEventHelper {
		public <E extends CyEvent, L extends CyListener> void fireSynchronousEvent(final E event, final Class<L> listener) {}
		public <E extends CyEvent, L extends CyListener> void fireAsynchronousEvent(final E event, final Class<L> listener) {}
	}
}
