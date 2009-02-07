package obvious;

public interface DataSet {

	Data get(String name);
	Data get(String name, Class type);
	void set(String name, Data data);
	
	// TODO: iterators?
	// could enable easy iteration over mutiple Data instances
	
}
