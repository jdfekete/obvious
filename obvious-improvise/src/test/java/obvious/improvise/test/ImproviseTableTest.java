package obvious.improvise.test;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.improvise.data.ImproviseObviousTable;
import test.obvious.data.TableTest;

public class ImproviseTableTest extends TableTest {

  @Override
  public Table newInstance(Schema schema) throws ObviousException {
    return new ImproviseObviousTable(schema);
  }

}
