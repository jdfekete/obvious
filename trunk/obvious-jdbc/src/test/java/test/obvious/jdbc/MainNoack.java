package test.obvious.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import noack.LinLogLayoutJDBC;
import noack.GraphListener;
import noack.NoackListener;

import obvious.data.Schema;
import obvious.impl.SchemaImpl;
import obvious.prefuse.PrefuseObviousSchema;

public class MainNoack {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {

    // Setting up the connection to the database
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("AUTHOR_ID", String.class, "John Doe");
    nodeSchema.addColumn("ID", int.class, 0);
    Schema edgeSchema = new PrefuseObviousSchema();
    edgeSchema.addColumn("EDGE_ID", int.class, 0);
    edgeSchema.addColumn("AUT1ID", int.class, 0);
    edgeSchema.addColumn("AUT2ID", int.class, 15555);
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@193.55.250.213:1521:orcl", "ediflow", "waeldouda");

    noack.Graph graph = new noack.Graph();
    
    System.out.println("EDGE");
    String edgeRequest = "SELECT DISTINCT B.EDGE_ID, B.AUT1ID, B.AUT2ID, B.AUT1, B.AUT2"
      + " FROM BIBEDGES B, VISUALATTRIBUTES V" + " WHERE B.EDGE_ID = V.MARKID"
      + " AND V.LABEL IS NULL";
    Statement edgeStmt = con.createStatement();
    ResultSet edgeRslt = edgeStmt.executeQuery(edgeRequest);
    while (edgeRslt.next()) {
      Object[] values = new Object[5];
      values[0] = edgeRslt.getInt(1);
      values[1] = edgeRslt.getInt(2);
      values[2] = edgeRslt.getInt(3);
      values[3] = edgeRslt.getString(4);
      values[4] = edgeRslt.getString(5);
      graph.addEdge((String) values[3], (String) values[4], 1.0f);
    }
    edgeRslt.close();
    edgeStmt.close();
    System.out.println("END EDGE");

    LinLogLayoutJDBC layout = new LinLogLayoutJDBC(graph, 10);
    GraphListener listener = new NoackListener(layout);
    
    graph.addGraphListener(listener);
    
    JFrame frame = layout.getFrame();
    frame.setTitle("Noack test");
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    graph.beginEdit();
    for (int i = 0; i < 1000; i++) {
    if (i%5 == 0) {
      graph.addEdge("lognet-2008-id18194", "milou" + i);
    } else if (i%5 == 1){
      graph.addEdge("artis-2005-id18433", "milou" + i);
    } else if (i%5 == 2) {
      graph.addEdge("smash-2005-id18238", "milou" + i);
    } else if (i%5 == 3) {
      graph.addEdge("mascotte-2005-id18392", "milou" + i);
    } else {
      graph.addEdge("micmac-2007-id2244379", "milou" + i);
    }
    }
    //graph.addEdge("lognet-2008-id18194", "tintin");
    //graph.addEdge("lognet-2008-id18194", "babar");
    //graph.addEdge("lognet-2008-id18194", "asterix");
    graph.endEdit();
    graph.fireGraphEvent(null, null, 1);

  }
}
