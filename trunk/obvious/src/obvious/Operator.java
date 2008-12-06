package obvious;

import java.util.Properties;

public interface Operator {
	
	// basic operation
	// need arguments? e.g., transitioner to collect value updates?
	void operate();

	// handle progress?
	double progress();

	// handle interruption?
	// it might make sense to put this at the level of an operator execution manager?
	void pause();
	void resume();
	void yield();

	// handle serialization?
	Properties getState();
	void setState(Properties state);
}
