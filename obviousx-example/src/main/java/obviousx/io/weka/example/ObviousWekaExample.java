package obviousx.io.weka.example;

import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.improvise.data.ImproviseObviousTable;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.prefuse.PrefuseObviousSchema;
import obvious.prefuse.PrefuseObviousTable;
import obviousx.io.weka.ObviousWekaLoader;
import weka.core.Instances;

public class ObviousWekaExample {

  public static void main(String[] args) {

    Schema schema = new PrefuseObviousSchema();
    schema.addColumn("id", int.class, 0);
    schema.addColumn("name", String.class, "Subject -1");
    schema.addColumn("age", int.class, 0);

    Table table = new ImproviseObviousTable(schema);

    java.util.Random randomGen = new java.util.Random();

    Object[] values = new Object[schema.getColumnCount()];
    for (int i = 0; i < 100; i++) {
      values[0] = i;
      values[1] = "Subject" + i;
      values[2] = randomGen.nextInt(110);
      table.addRow(new TupleImpl(schema, values));
    }

   ObviousWekaLoader loader = new ObviousWekaLoader(table);
   Instances inst = loader.loadInstances();
   
   System.out.println(inst.toSummaryString());

  }
}
