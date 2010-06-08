package obvious.improvise.test;

import obvious.data.Schema;
import obvious.improvise.data.ImproviseObviousSchema;
import test.obvious.data.SchemaTest;

public class ImproviseSchemaTest extends SchemaTest {

  @Override
  public Schema newInstance() {
    return new ImproviseObviousSchema();
  }

}
