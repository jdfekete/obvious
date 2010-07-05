package obvious.improvise.test;

import oblivion.lp.VariableEvent;
import oblivion.lp.VariableListener;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.impl.TupleImpl;
import obvious.improvise.data.ImproviseObviousListener;
import obvious.improvise.data.ImproviseObviousSchema;
import obvious.improvise.data.ImproviseObviousTable;

public class ImproviseListenerTest {

  public class SimpleVariableListener implements VariableListener {

    @Override
    public void variableChanged(VariableEvent arg0) {
      System.out.println("Something changed");
    }
    
  }
  
  public static void main(String[] args) {
    System.out.println("BEGIN OF TEST");
    
    Schema schema = new ImproviseObviousSchema();
    schema.addColumn("name", String.class, "John Doe");
    
    Table table = new ImproviseObviousTable(schema);
    
    
    VariableListener varLstnr = new ImproviseListenerTest().new SimpleVariableListener();
    
    ImproviseObviousListener lstnr = new ImproviseObviousListener((ImproviseObviousTable) table, varLstnr);
    
    Tuple tuple = new TupleImpl(schema, new Object[] {"bob"});
    table.addTableListener(lstnr);
    table.addRow(tuple);
    
    System.out.println("END OF TEST");
  }
  
}
