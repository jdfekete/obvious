package obvious.prefuse;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TableImpl;

public class PrefuseDataFactory extends DataFactory {

  @Override
  public Network createGraph(String name, Schema nodeSchema, Schema edgeSchema)
      throws ObviousException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Table createTable(String name, Schema schema) throws ObviousException {
    return new PrefuseObviousTable(schema);
  }

  @Override
  public Network wrapGraph(Object underlyingGraph) throws ObviousException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Table wrapTable(Object unerlyingTable) throws ObviousException {
    // TODO Auto-generated method stub
    return null;
  }
  
}
