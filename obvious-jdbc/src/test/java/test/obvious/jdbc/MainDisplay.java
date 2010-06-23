package test.obvious.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;
import obvious.prefuse.PrefuseObviousNetwork;
import obvious.prefuse.PrefuseObviousSchema;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import prefuse.Display;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class MainDisplay {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    
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

    PrefuseObviousNetwork displayNetwork = new PrefuseObviousNetwork(nodeSchema, edgeSchema,
    true, "ID", "AUT1ID", "AUT2ID");

    String visualNodeRequest = "SELECT * FROM VISUALATTRIBUTES WHERE LABEL is not NULL";
    String nodeFillRequest = "SELECT * FROM CONTRIBUTOR WHERE ID = ?";

    Statement nodeStmt = con.createStatement();
    PreparedStatement nodeFillStmt = con.prepareStatement(nodeFillRequest);

    ResultSet nodeRequestRslt = nodeStmt.executeQuery(visualNodeRequest);

    while (nodeRequestRslt.next()) {
      int markId = nodeRequestRslt.getInt("MARKID");
      nodeFillStmt.setInt(1, markId);
      ResultSet nodeFillRslt = nodeFillStmt.executeQuery();
      while (nodeFillRslt.next()) {
        Object[] values = new Object[nodeSchema.getColumnCount()];
        values[0] = nodeFillRslt.getString("AUTHOR_ID");
        values[1] = nodeFillRslt.getString("ID");
        Node node = new NodeImpl(nodeSchema, values);
        displayNetwork.addNode(node);
      }
      nodeFillRslt.close();
      nodeFillStmt.clearParameters();
    }
    nodeRequestRslt.close();
    nodeFillStmt.close();
    nodeStmt.close();

    String visualEdgeRequest = "SELECT * FROM VISUALATTRIBUTES WHERE LABEL is NULL";
    String edgeFillRequest = "SELECT * FROM BIBEDGES WHERE EDGE_ID = ?";

    Statement edgeStmt = con.createStatement();
    PreparedStatement edgeFillStmt = con.prepareStatement(edgeFillRequest);

    ResultSet edgeRequestRslt = edgeStmt.executeQuery(visualEdgeRequest);

    while (edgeRequestRslt.next()) {
      int node1, node2;
      int markId = edgeRequestRslt.getInt("MARKID");
      edgeFillStmt.setInt(1, markId);
      ResultSet edgeFillRslt = edgeFillStmt.executeQuery();
      while (edgeFillRslt.next()) {
        Object[] values = new Object[edgeSchema.getColumnCount()];
        node1 = edgeFillRslt.getInt("AUT1ID");
        node2 = edgeFillRslt.getInt("AUT2ID");
        values[0] = edgeFillRslt.getInt("EDGE_ID");
        values[1] = node1;
        values[2] = node2;
        Edge edge = new EdgeImpl(edgeSchema, values);
        Node source = null, target = null;
        boolean sourceFound = false, targetFound = false;
        for (Node node : displayNetwork.getNodes()) {
          if (sourceFound && targetFound) {
            break;
          }
          if (node1 == node.getInt("ID")) {
            source = node;
            sourceFound = true;
          }
          if (node2 == node.getInt("ID")) {
           target = node;
           targetFound = true;
          }
        }
        displayNetwork.addEdge(edge, source, target, Graph.EdgeType.DIRECTED);
      }
      edgeFillRslt.close();
      edgeFillStmt.clearParameters();
    }
    edgeRequestRslt.close();
    edgeStmt.close();
    
    // Visualization
    // Param for the prefuse visualization (simply group name).
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");

    // Creating the visualization.
    PrefuseObviousVisualization vis = new PrefuseObviousVisualization(
        displayNetwork, null, null, param);
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
  }

}
