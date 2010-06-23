package test.obvious.jdbc;

import infovis.panel.VisualizationPanel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.SchemaImpl;
import obvious.ivtk.data.IvtkObviousNetwork;
import obvious.ivtk.viz.IvtkObviousVisualization;
import obvious.ivtk.viz.util.IvtkNodeLinkGraphVis;
import obvious.jdbc.JDBCObviousNetwork;
import obvious.prefuse.PrefuseObviousNetwork;
import obvious.prefuse.PrefuseObviousSchema;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import obvious.util.ObviousLib;
import obvious.viz.Visualization;
import prefuse.Constants;
import prefuse.Display;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.BalloonTreeLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;

public class MainPref {

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
    JDBCObviousNetwork network = new JDBCObviousNetwork(con, "CONTRIBUTOR", "BIBEDGES","ID", "EDGE_ID", "AUT1ID", "AUT2ID", nodeSchema, edgeSchema);
    Network prefNetwork = new PrefuseObviousNetwork(nodeSchema, edgeSchema,
        true, "ID", "AUT1ID", "AUT2ID");
    ObviousLib.fillNetwork(network, prefNetwork);
    for (Node node : prefNetwork.getNodes()) {
      if (prefNetwork.getNeighbors(node).size() == 0) {
        prefNetwork.removeNode(node);
      }
    }
    Statement stmt = con.createStatement();
    String request = "SELECT MAX(COMPONENTID) FROM VISUALCOMPONENT";
    ResultSet rslt = stmt.executeQuery(request);
    // Computing visual component ID
    int componentId = 0;
    while (rslt.next()) {
      componentId = rslt.getInt(1) + 1;
    }
    rslt.close();
    stmt.executeUpdate("INSERT INTO VISUALCOMPONENT (COMPONENTID) VALUES (" + componentId + ")");
    stmt.close();
    // Visualization
    // Param for the prefuse visualization (simply group name).
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");

    // Creating the visualization.
    PrefuseObviousVisualization vis = new PrefuseObviousVisualization(
        prefNetwork, null, null, param);
    // Using label renderer as in the tutorial.
    vis.getAliasMap().put(vis.VISUAL_LABEL, "AUTHOR_ID");
    LabelRenderer r = new LabelRenderer("AUTHOR_ID");
    r.setRoundedCorner(8, 8); // round the corners
    vis.setRenderer(new PrefuseObviousRenderer(new DefaultRendererFactory(r)));

   // Color for data values.
   ColorAction text = new ColorAction("graph.nodes",
       VisualItem.TEXTCOLOR, ColorLib.gray(0));
   // Color for edges.
   ColorAction edges = new ColorAction("graph.edges",
       VisualItem.STROKECOLOR, ColorLib.gray(200));

   // Creating the prefuse action list.
   ActionList color = new ActionList();
   color.add(text);
   color.add(edges);

   // Wrapping the action list around obvious
   vis.putAction("color", new PrefuseObviousAction(color));


  // Creating a Directed force Layout.
  ActionList layout = new ActionList(Activity.INFINITY);
  layout.add(new ForceDirectedLayout("graph"));
  layout.add(new RepaintAction());

  // Wrapping the layout around obvious.
  vis.putAction("layout", new PrefuseObviousAction(layout));

  // In order to display, we have to call the underlying prefuse visualization.
  // In a complete version of obvious, we don't need that step.
  prefuse.Visualization prefViz = (prefuse.Visualization)
  vis.getUnderlyingImpl(prefuse.Visualization.class);

  ArrayList<Node> validNode = new ArrayList<Node>();

  // Filling visual attributes table

  System.out.println("FILLING Visual attributes table");
  String sql = "INSERT INTO VISUALATTRIBUTES (COMPONENTID, OBJECTID, LABEL, MARKID) "
    + "VALUES (" + componentId + ", ?  , ? ,  ?)";
  PreparedStatement pStmt = con.prepareStatement(sql);
  for (Node node : prefNetwork.getNodes()) {
    String label = vis.getLabel(node);
    pStmt.setString(1, label);
    pStmt.setString(2, label);
    pStmt.setInt(3, node.getInt("ID"));
    pStmt.executeUpdate();
  }
  pStmt.close();

  sql = "INSERT INTO VISUALATTRIBUTES (COMPONENTID, OBJECTID, MARKID) "
    + "VALUES (" + componentId + ", ?  , ?)";
  PreparedStatement pStmt2 = con.prepareStatement(sql);
  for (Edge edge : prefNetwork.getEdges()) {
    pStmt2.setString(1, edge.getString("EDGE_ID"));
    pStmt2.setInt(2, edge.getInt("EDGE_ID"));
    pStmt2.executeUpdate();
  }
  pStmt2.close();

  /*
  // Displaying graph
  //Create a new display (prefuse)
  Display display = new Display(prefViz);
  display.setSize(800, 640);
  display.addControlListener(new DragControl());
  display.addControlListener(new PanControl());
  display.addControlListener(new ZoomControl());

  //create a new window to hold the visualization
  JFrame frame = new JFrame("DataModel : Obvious-prefuse"
      + " | Visu : Obvious-Prefuse | View : Prefuse");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.add(display);
  frame.pack();
  frame.setVisible(true);

  //Create a new display (prefuse)
  prefViz.run("color");
  prefViz.run("layout");
  */

  }

}
