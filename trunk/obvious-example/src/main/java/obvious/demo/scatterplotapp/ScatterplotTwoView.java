package obvious.demo.scatterplotapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TupleImpl;
import obvious.ivtk.data.IvtkObviousSchema;
import obvious.ivtk.data.IvtkObviousTable;
import obvious.ivtk.view.IvtkObviousView;
import obvious.prefuse.PrefuseObviousTable;
import obvious.prefuse.view.PrefuseObviousControl;
import obvious.prefuse.view.PrefuseObviousView;
import obvious.prefuse.viz.PrefuseVisualizationFactory;
import obvious.prefuse.viz.util.PrefuseScatterPlotViz;
import obvious.viz.Visualization;
import obvious.viz.VisualizationFactory;
import obviousx.io.ObviousTableModel;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;

public class ScatterplotTwoView {

  public static void main(String[] args) throws ObviousException {
    
    final Schema schema = new IvtkObviousSchema();
    schema.addColumn("id", Integer.class, 0);
    schema.addColumn("age", Integer.class, 18);
    schema.addColumn("category", String.class, "unemployed");

    final Table table = new PrefuseObviousTable(schema);

    table.addRow(new TupleImpl(schema, new Object[] {1, 22, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {2, 60, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {3, 32, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {4, 20, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {5, 72, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {6, 40, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {7, 52, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {8, 35, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {9, 32, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {10, 44, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {11, 27, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {12, 38, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {13, 53, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {14, 49, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {15, 21, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {16, 36, "unemployed"}));

    // Creating the parameter map for the monolithic object.
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
    param.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
    param.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field
    
    // Creating the parameter map for the monolithic object.
    Map<String, Object> paramIvtk = new HashMap<String, Object>();
    paramIvtk.put(PrefuseScatterPlotViz.X_AXIS, "id"); // name of the xfield
    paramIvtk.put(PrefuseScatterPlotViz.Y_AXIS, "age"); // name of the yfield
    paramIvtk.put(PrefuseScatterPlotViz.SHAPE, "category"); // category field
   
    // Using the factory to build the visualization
    System.setProperty("obvious.VisualizationFactory",
        "obvious.prefuse.viz.PrefuseVisualizationFactory");
    VisualizationFactory factory = PrefuseVisualizationFactory.getInstance();
    Visualization vis =
      factory.createVisualization(table, null, "scatterplot", param);

    // In order to display, we have to call the underlying prefuse
    // visualization.
    // In a complete version of obvious, we don't need that step.
    final prefuse.Visualization prefViz = (prefuse.Visualization)
    vis.getUnderlyingImpl(prefuse.Visualization.class);
    // Building the prefuse display.
    //Display display = new Display(prefViz);
    
    PrefuseObviousView view = new PrefuseObviousView(vis, null, "scatterplot", null);
    view.addListener(new PrefuseObviousControl(new ZoomControl()));
    view.addListener(new PrefuseObviousControl(new PanControl()));
    view.addListener(new PrefuseObviousControl(new DragControl()));
    
    //System.out.println(display == null);
    view.getViewJComponent().setSize(800, 600);

    IvtkObviousView view2 = new IvtkObviousView(vis,  null, "scatterplot", null);

    Dimension minimumSize = new Dimension(0, 0);
    view.getViewJComponent().setMinimumSize(minimumSize);
    view2.getViewJComponent().setMinimumSize(minimumSize);

    JFrame frame = new JFrame("Obvious : scatterplot demonstrator");
    JSplitPane globalsplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane mainsplitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JSplitPane viewsplitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    JTable obviousJTable = new JTable(new ObviousTableModel(table));
    obviousJTable.addKeyListener(new SimpleTableListener(obviousJTable, prefViz, view2.getViewJComponent()));
    JScrollPane scrollpane = new JScrollPane(obviousJTable);
    viewsplitpane.add(view.getViewJComponent(), 0);
    viewsplitpane.add(view2.getViewJComponent(),1);
    mainsplitpane.add(viewsplitpane, 0);
    mainsplitpane.add(new ScatterplotTwoView().new ControlPanel(table, prefViz, view2.getViewJComponent(), scrollpane, obviousJTable));
    globalsplitPane.add(mainsplitpane, 0);
    globalsplitPane.add(scrollpane, 1);
    frame.getContentPane().add(globalsplitPane);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    prefViz.run("draw");

    /*
    prefViz.cancel("draw");
    table.addRow(new TupleImpl(schema, new Object[] {17, 28, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {18, 65, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {19, 56, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {20, 19, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {21, 26, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {22, 23, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {23, 45, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {24, 38, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {25, 29, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {26, 26, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {27, 43, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {29, 35, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {30, 58, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {17, 28, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {18, 65, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {19, 56, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {20, 19, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {21, 26, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {22, 23, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {23, 45, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {24, 38, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {25, 29, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {26, 26, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {27, 43, "unemployed"}));
    table.addRow(new TupleImpl(schema, new Object[] {29, 35, "worker"}));
    table.addRow(new TupleImpl(schema, new Object[] {30, 58, "unemployed"}));
    for (int i = 0; i < 30; i++) {
      table.addRow(new TupleImpl(schema, new Object[] {i, i - 20, "unemployed"}));
    }
    prefViz.run("draw");
    */
  }

  /**
   * Panel for test.
   * @author Hemery
   *
   */
  public class ControlPanel extends JPanel implements ActionListener {

    private final String xLabelString = "X";
    private final String yLabelString = "Y";

    //Labels to identify the fields
    private JLabel xLabel;
    private JLabel yLabel;

    //Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    //Fields for data entry
    private JFormattedTextField xField;
    private JFormattedTextField yField;

    //Formats to format and parse numbers
    private NumberFormat numberFormat;

    
    private Table table;
    private prefuse.Visualization prefViz;
    
    private JComponent view;
    
    private JScrollPane scrollpane;
    
    private JTable jtable;
    
    /**
     * Constructor.
     */
    public ControlPanel(Table table, prefuse.Visualization visualization, JComponent view,
        JScrollPane scrollpane, JTable jtable) {
      super(new BorderLayout());
      this.table = table;
      this.jtable = jtable;
      this.scrollpane = scrollpane;
      this.view = view;
      this.prefViz = visualization;
      xLabel = new JLabel(xLabelString);
      yLabel = new JLabel(yLabelString);

      xField = new JFormattedTextField(numberFormat);
      xField.setValue(new Double(0));
      xField.setColumns(10);

      yField = new JFormattedTextField(numberFormat);
      yField.setValue(new Double(0));
      yField.setColumns(10);

      xLabel.setLabelFor(xField);
      yLabel.setLabelFor(yField);

      //Lay out the labels in a panel.
      JPanel labelPane = new JPanel(new GridLayout(0,1));
      labelPane.add(xLabel);
      labelPane.add(yLabel);

      // Button for Update
      addButton = new JButton("Add point");
      addButton.addActionListener(this);
      
      updateButton = new JButton("Update views");
      updateButton.addActionListener(this);
      
      deleteButton = new JButton("Delete selected rows");
      deleteButton.addActionListener(this);

      JPanel fieldPane = new JPanel(new GridLayout(0,1));
      fieldPane.add(xField);
      fieldPane.add(yField);
      
      JPanel buttonPane = new JPanel(new GridLayout(0,1));
      buttonPane.add(addButton);
      buttonPane.add(updateButton);
      buttonPane.add(deleteButton);
      
      //Put the panels in this panel, labels on left,
      //text fields on right.
      setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      add(labelPane, BorderLayout.CENTER);
      add(fieldPane, BorderLayout.LINE_END);
      add(buttonPane, BorderLayout.SOUTH);
    }

    /**
     * Calls when associated buttons are selected.
     * @param e event
     */
    public void actionPerformed(final ActionEvent e) {
      new Thread(new Runnable() {
        public void run () {
          if (e.getSource() == addButton) {
            int x = ((Number)xField.getValue()).intValue();
            int y = ((Number)yField.getValue()).intValue();
            prefViz.cancel("draw");
            table.addRow(new TupleImpl(table.getSchema(), new Object[] {x, y, "unemployed"}));
            //prefViz.run("draw");
          } else if (e.getSource() == deleteButton) {
            int[] rows = jtable.getSelectedRows();
            prefViz.cancel("draw");
            for (int i = 0; i < rows.length; i++) {
              table.removeRow(jtable.convertRowIndexToModel(rows[i]));
            }
          }
          final infovis.panel.VisualizationPanel panel = (infovis.panel.VisualizationPanel) view;
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              prefViz.run("draw");
              prefViz.getDisplay(0).revalidate();
              prefViz.getDisplay(0).repaint();
              panel.getVisualization().invalidate();
              panel.getVisualization().repaint();
              scrollpane.revalidate();
              scrollpane.repaint();
              scrollpane.getComponent(0).invalidate();
              scrollpane.getComponent(0).repaint();
            }
          });
        }
      }).start();
    }
  }
}
