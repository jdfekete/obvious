package obvious.data.util;

import java.util.Properties;
import obvious.data.DataSet;

public interface Operator {
	// the data set the operator operates on
	DataSet getDataSet();
	void setDataSet(DataSet set);
	
	// basic operation
	// need arguments? e.g., transitionner to collect value updates?
	void operate();

	// handle progress?
	double progress();

	// TODO: get/set dependencies (as path names?)
	
	// handle interruption?
	// it might make sense to put this at the level of an operator execution manager?
	void pause();
	void resume();
	void yield();

	// handle serialization?
	Properties getState();
	void setState(Properties state);
}
