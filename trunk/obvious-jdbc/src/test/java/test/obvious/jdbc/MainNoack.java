package test.obvious.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import noack.LinLogLayoutJDBC;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
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
      //System.out.println("coucou");
      Object[] values = new Object[5];
      //int node1 = edgeRslt.getInt(2);
      //int node2 = edgeRslt.getInt(3);
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

    JFrame frame = layout.getFrame();
    frame.setTitle("Noack test");
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  private static void toto() {
    System.out.println("toto");
  }
}
