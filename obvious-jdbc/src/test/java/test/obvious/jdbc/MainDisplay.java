package test.obvious.jdbc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;
import obvious.prefuse.data.PrefuseObviousNetwork;
import obvious.prefuse.data.PrefuseObviousSchema;
import obvious.prefuse.viz.PrefuseObviousVisualization;
import obvious.prefuse.viz.util.PrefuseObviousAction;
import obvious.prefuse.viz.util.PrefuseObviousRenderer;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.GridLayout;
import prefuse.action.layout.RandomLayout;
import prefuse.action.layout.graph.BalloonTreeLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.FruchtermanReingoldLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.force.ForceSimulator;
import prefuse.util.ui.JForcePanel;
import prefuse.util.ui.JSearchPanel;
import prefuse.util.ui.JValueSlider;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

public class MainDisplay extends Thread {
  
  private static final String graph = "graph";
  private static final String nodes = "graph.nodes";
  private static final String edges = "graph.edges";

  public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
    MainDisplay main = new MainDisplay();
    // Setting up the connection to the database
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("AUTHOR_ID", String.class, "John Doe");
    nodeSchema.addColumn("ID", int.class, 0);
    nodeSchema.addColumn("LAST_NAME", String.class, "Jane Doe");
    Schema edgeSchema = new PrefuseObviousSchema();
    edgeSchema.addColumn("EDGE_ID", int.class, 0);
    edgeSchema.addColumn("AUT1ID", int.class, 0);
    edgeSchema.addColumn("AUT2ID", int.class, 15555);
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@193.55.250.213:1521:orcl", "ediflow", "waeldouda");

    PrefuseObviousNetwork displayNetwork = new PrefuseObviousNetwork(nodeSchema, edgeSchema,
    true, "ID", "AUT1ID", "AUT2ID");

    /*
    String visualNodeRequest = "SELECT * FROM VISUALATTRIBUTES WHERE LABEL is not NULL AND MARKID < 2635";
    String nodeFillRequest = "SELECT * FROM CONTRIBUTOR WHERE ID = ?";

    Statement nodeStmt = con.createStatement();
    PreparedStatement nodeFillStmt = con.prepareStatement(nodeFillRequest);

    ResultSet nodeRequestRslt = nodeStmt.executeQuery(visualNodeRequest);
    System.out.println("NODE");
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
    */
    System.out.println("NODE");
    String nodeRequest = "SELECT DISTINCT C.AUTHOR_ID, C.ID, C.LAST_NAME"
      + " FROM CONTRIBUTOR C, VISUALATTRIBUTES V" + " WHERE C.ID = V.MARKID"
      + " AND V.LABEL IS NOT NULL";

    Statement nodeStmt = con.createStatement();
    ResultSet nodeRslt = nodeStmt.executeQuery(nodeRequest);
    while (nodeRslt.next()) {
      Object[] values = new Object[nodeSchema.getColumnCount()];
      values[0] = nodeRslt.getString("AUTHOR_ID");
      values[1] = nodeRslt.getString("ID");
      values[2] = nodeRslt.getString("LAST_NAME");
      Node node = new NodeImpl(nodeSchema, values);
      displayNetwork.addNode(node);
    }
    nodeRslt.close();
    nodeStmt.close();
    System.out.println("END NODE");

    System.out.println("EDGE");
    String edgeRequest = "SELECT DISTINCT B.EDGE_ID, B.AUT1ID, B.AUT2ID"
      + " FROM BIBEDGES B, VISUALATTRIBUTES V" + " WHERE B.EDGE_ID = V.MARKID"
      + " AND V.LABEL IS NULL"
      + " AND B.AUT2ID <> B.AUT1ID";
    Statement edgeStmt = con.createStatement();
    ResultSet edgeRslt = edgeStmt.executeQuery(edgeRequest);
    while (edgeRslt.next()) {
      Object[] values = new Object[edgeSchema.getColumnCount()];
      //int node1 = edgeRslt.getInt(2);
      //int node2 = edgeRslt.getInt(3);
      values[0] = edgeRslt.getInt(1);
      values[1] = edgeRslt.getInt(2);
      values[2] = edgeRslt.getInt(3);
      Edge edge = new EdgeImpl(edgeSchema, values);
      Node source = null, target = null;
      boolean sourceFound = false, targetFound = false;
      for (Node node : displayNetwork.getNodes()) {
        if (sourceFound && targetFound) {
          break;
        }
        if ((Integer) values[1] == node.getInt("ID")) {
          source = node;
          sourceFound = true;
        }
        if ((Integer) values[2] == node.getInt("ID")) {
         target = node;
         targetFound = true;
        }
      }
      displayNetwork.addEdge(edge, source, target, Graph.EdgeType.DIRECTED);
    }
    edgeRslt.close();
    edgeStmt.close();
    System.out.println("END EDGE");

    System.out.println("FILTER");
    for (Node node : displayNetwork.getNodes()) {
      if (displayNetwork.getNeighbors(node).size() == 0) {
        displayNetwork.removeNode(node);
      }
    }
    System.out.println("ENDFILTER");

    // Visualization
    // Param for the prefuse visualization (simply group name).
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseObviousVisualization.GROUP_NAME, "graph");
    System.out.println("VISU PREF");
    // Creating the visualization.
    PrefuseObviousVisualization vis = new PrefuseObviousVisualization(
        displayNetwork, null, null, param);
    // Using label renderer as in the tutorial.
    vis.getAliasMap().put(vis.VISUAL_LABEL, "LAST_NAME");

    LabelRenderer r = new LabelRenderer("LAST_NAME");
    r.setRoundedCorner(8, 8); // round the corners
    r.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
    r.setHorizontalAlignment(Constants.CENTER);
    vis.setRenderer(new PrefuseObviousRenderer(new DefaultRendererFactory(r)));

   // Color for data values.
   ColorAction text = new ColorAction("graph.nodes",
       VisualItem.TEXTCOLOR, ColorLib.gray(0));
   // Color for edges.
   ColorAction edges = new ColorAction("graph.edges",
       VisualItem.STROKECOLOR, ColorLib.color(Color.black));

   // Creating the prefuse action list.
   ActionList color = new ActionList();
   color.add(text);
   color.add(edges);

   // Wrapping the action list around obvious
   vis.putAction("color", new PrefuseObviousAction(color));

  // Creating a Directed force Layout.
  ActionList layout = new ActionList(10);
  layout.add(new RandomLayout("graph.nodes"));

  // Wrapping the layout around obvious.
  vis.putAction("layout", new PrefuseObviousAction(layout));

  ActionList force = new ActionList(Activity.INFINITY);
  force.add(new ForceDirectedLayout("graph"));
  force.add(new RepaintAction());
  vis.putAction("force", new PrefuseObviousAction(force));


  // In order to display, we have to call the underlying prefuse visualization.
  // In a complete version of obvious, we don't need that step.
  final prefuse.Visualization prefViz = (prefuse.Visualization)
  vis.getUnderlyingImpl(prefuse.Visualization.class);
  prefViz.runAfter("color", "layout");
  prefViz.runAfter("layout", "force");

  TupleSet set = new DefaultTupleSet();
  prefViz.addFocusGroup("added", set);
  ColorAction textReColor = new ColorAction("added",
      VisualItem.TEXTCOLOR, ColorLib.color(Color.RED));
  prefViz.putAction("nodeAdded", textReColor);
  
  /*
  ActionList nodeAdded = new ActionList(Activity.INFINITY);
  ColorAction textReColor = new ColorAction("graph.nodes",
      VisualItem.TEXTCOLOR, ColorLib.color(Color.RED));
  nodeAdded.add(textReColor);
  prefViz.putAction("nodeAdded", nodeAdded);

  SearchTupleSet searchTuple = new PrefixSearchTupleSet();
  prefViz.addFocusGroup("added", searchTuple);
  searchTuple.addTupleSetListener(new TupleSetListener() {
      public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
        prefViz.cancel("color");
        prefViz.cancel("nodeSearched");
        prefViz.run("color");
        prefViz.run("nodeSearched");
      }
  });

//  prefViz.getVisualGroup("graph").addTupleSetListener(new MainDisplay().new NodeAddedListener(prefViz, searchTuple));

  // create a search panel for the tree map
  SearchQueryBinding sq = new SearchQueryBinding(
       (Table)prefViz.getGroup("graph.nodes"), "AUTHOR_ID",
       searchTuple);
  JSearchPanel searchPanel = sq.createSearchPanel();
  searchPanel.setShowResultCount(true);
  searchPanel.setBorder(BorderFactory.createEmptyBorder(5,5,4,0));
  searchPanel.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));
  */

  Display display = new Display(prefViz);
  display.addControlListener(new DragControl());
  display.addControlListener(new PanControl());
  display.addControlListener(new ZoomControl());

  /*
  JPanel panel = new JPanel(new BorderLayout());
  panel.add(BorderLayout.CENTER, display);
  panel.add(BorderLayout.SOUTH, searchPanel);
*/
  GraphicsEnvironment ge = GraphicsEnvironment.
  getLocalGraphicsEnvironment();
  GraphicsDevice[] gs = ge.getScreenDevices();

  prefuse.data.Table nodeTable = (prefuse.data.Table) prefViz.getVisualGroup("graph.nodes");

  
  for (int j = 0; j < gs.length; j++) {
    Display currentDisplay = new Display(prefViz);
    currentDisplay.addControlListener(new DragControl());
    currentDisplay.addControlListener(new PanControl());
    currentDisplay.addControlListener(new ZoomControl());
    GraphicsDevice gd = gs[j];
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    JFrame frame = new JFrame(gc);
    frame.setTitle("Exemple...");
    frame.add(currentDisplay);
    frame.pack();
    //frame.setUndecorated(true);
    frame.setSize(gc.getBounds().getSize());
    frame.setVisible(true);
    //frame.setSize(gc.getBounds().width, gc.getBounds().height);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    prefViz.run("color");
    currentDisplay.panAbs(gc.getBounds().getX(), gc.getBounds().getY());
}

  nodeTable.addTupleSetListener(new MainDisplay().new NodeAddedListener(prefViz, set));
    main.sleep(10000);
  /*
  //create a new window to hold the visualization
  UILib.setPlatformLookAndFeel();
  JFrame frame = new JFrame("DataModel : Obvious-prefuse"
      + " | Visu : Obvious-Prefuse | View : Prefuse");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setContentPane(panel);
  frame.pack();
  frame.setVisible(true);



  //Create a new display (prefuse)
  prefViz.run("color");
  */
  /*
  GraphicsEnvironment ge = GraphicsEnvironment.
  getLocalGraphicsEnvironment();
  GraphicsDevice[] gs = ge.getScreenDevices();
  for (int j = 0; j < gs.length; j++) { 
     Display currentDisplay = new Display(prefViz);
     currentDisplay.addControlListener(new DragControl());
     currentDisplay.addControlListener(new PanControl());
     currentDisplay.addControlListener(new ZoomControl());
     GraphicsDevice gd = gs[j];
        System.out.println(gd.getDefaultConfiguration().getBounds());
        JFrame f = new
        JFrame(gd.getDefaultConfiguration());
        GraphicsConfiguration config = gd.getDefaultConfiguration();
        f.setTitle("Test...");
        Predicate xPredMax = (Predicate) ExpressionParser.parse("_x < " + String.valueOf(config.getBounds().x));
        Predicate xPredMin = (Predicate) ExpressionParser.parse("_x > " + String.valueOf(display.getBounds().x));
        Predicate xPred = new AndPredicate(xPredMin, xPredMax);
        Predicate yPredMax = (Predicate) ExpressionParser.parse("_y <"  + String.valueOf(display.getBounds().getMaxY()/2));
        Predicate yPredMin = (Predicate) ExpressionParser.parse("_y >"  + String.valueOf(config.getBounds().y));
        Predicate yPred = new AndPredicate(xPredMin, yPredMin);
        Predicate andPredicate = new AndPredicate(xPred, yPred);
        //Border border = BorderFactory.createEmptyBorder(config.getBounds().height, config.getBounds().x, config.getBounds().y, config.getBounds().width);
        currentDisplay.setPredicate(yPred);
        //f.setBounds(config.getBounds().x, config.getBounds().y, config.getBounds().width, config.getBounds().height);
        f.add(currentDisplay);
        //display.repaint(config.getBounds().x, config.getBounds().y, config.getBounds().width, config.getBounds().height);
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
*/

  /*
  System.out.println("DISPLAY");
  Display display = new Display(prefViz);
  display.setSize(700,700);
  display.pan(350, 350);
  display.setForeground(Color.GRAY);
  display.setBackground(Color.WHITE);
  
  // main display controls
  display.addControlListener(new FocusControl(1));
  display.addControlListener(new DragControl());
  display.addControlListener(new PanControl());
  display.addControlListener(new ZoomControl());
  display.addControlListener(new WheelZoomControl());
  display.addControlListener(new ZoomToFitControl());
  display.addControlListener(new NeighborHighlightControl());

  // overview display
//  Display overview = new Display(vis);
//  overview.setSize(290,290);
//  overview.addItemBoundsListener(new FitOverviewListener());
  
  display.setForeground(Color.GRAY);
  display.setBackground(Color.WHITE);
  
  // --------------------------------------------------------------------        
  // launch the visualization
  
  // create a panel for editing force values
  ForceSimulator fsim = ((ForceDirectedLayout)force.get(0)).getForceSimulator();
  JForcePanel fpanel = new JForcePanel(fsim);
  
//  JPanel opanel = new JPanel();
//  opanel.setBorder(BorderFactory.createTitledBorder("Overview"));
//  opanel.setBackground(Color.WHITE);
//  opanel.add(overview);
  
  final JValueSlider slider = new JValueSlider("Distance", 0, 30, 30);
  slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
          prefViz.run("color");
      }
  });
  slider.setBackground(Color.WHITE);
  slider.setPreferredSize(new Dimension(300,30));
  slider.setMaximumSize(new Dimension(300,30));
  
  //fpanel.add(opanel);

  fpanel.add(Box.createVerticalGlue());
  
  // create a search panel for the tree map
  SearchQueryBinding sq = new SearchQueryBinding(
       (Table)prefViz.getGroup("graph.nodes"), "ID",
       searchTuple);
  JSearchPanel search = sq.createSearchPanel();
  search.setShowResultCount(true);
  search.setBorder(BorderFactory.createEmptyBorder(5,5,4,0));
  search.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));

  
  // create a new JSplitPane to present the interface
  JSplitPane split = new JSplitPane();
  split.setLeftComponent(display);
  split.setRightComponent(fpanel);
  split.setOneTouchExpandable(true);
  split.setContinuousLayout(false);
  split.setDividerLocation(700);
  
  // now we run our action list
  prefViz.run("color");
  
  JPanel panel = new JPanel(new BorderLayout());
  panel.add(split);
  panel.setVisible(true);
  JFrame frame = new JFrame("Example");
  frame.setContentPane(panel);
  frame.pack();
  frame.setVisible(true);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  */
  
  System.out.println("coco veut un noeud" + displayNetwork.getNodes().size());
  //Node tintin = new NodeImpl(nodeSchema, new Object[] {"tintin", 25058, "tintin"});
  //Node milou = new NodeImpl(nodeSchema, new Object[] {"milou", 25059});
  //Edge tintinmilou = new EdgeImpl(edgeSchema, new Object[] {200000, 25058, 25059});
  for (int i = 0; i < 100; i++) {
    displayNetwork.addNode(new NodeImpl(nodeSchema, new Object[] {"milou" + i, 25060 + i, "milou" + i}));
  }
  //displayNetwork.addNode(new NodeImpl(nodeSchema, new Object[] {"tintin", 25058, "tintin"}));
  //displayNetwork.addNode(new NodeImpl(nodeSchema, new Object[] {"milou", 25059, "milou"}));
  //displayNetwork.addNode(new NodeImpl(nodeSchema, new Object[] {"milou2010", 25060, "milou2010"}));
  //displayNetwork.addEdge(tintinmilou, tintin, milou, Graph.EdgeType.DIRECTED);
  prefuse.data.tuple.TupleSet tupleSet = prefViz.getVisualGroup("graph");
  prefuse.data.Graph graph = (prefuse.data.Graph) tupleSet;
  System.out.println("coco a eu ses noeuds : " + graph.getNodeCount());
  
  }
  
  public class NodeAddedListener implements TupleSetListener {

    private prefuse.Visualization prefViz;
    private TupleSet set;

    
    public NodeAddedListener(prefuse.Visualization prefViz, TupleSet set) {
      this.prefViz = prefViz;
      this.set = set;
    }
    
    @Override
    public void tupleSetChanged(TupleSet arg0, Tuple[] arg1, Tuple[] arg2) {
      for (int i = 0; i < arg1.length; i++) {
        set.addTuple(arg1[i]);
      }
      System.out.println("Something has changed");
      System.out.println(arg1.length);
      prefViz.run("color");
      prefViz.run("nodeAdded");
      System.out.println("Changes performed");

    }
    
  }

}
