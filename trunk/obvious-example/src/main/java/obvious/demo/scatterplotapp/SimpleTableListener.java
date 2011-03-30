package obvious.demo.scatterplotapp;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

/**
 * A Key listener for JTable.
 * @author Hemery
 *
 */
public class SimpleTableListener implements KeyListener {

  /**
   * A JTable.
   */
  private JTable table;

  /**
   * A view (as a JComponent).
   */
  private JComponent view;

  /**
   * A prefuse visualization.
   */
  private prefuse.Visualization prefViz;

  /**
   * Row and col index.
   */
  private int row, col;

  /**
   * Constructor.
   * @param inTable a JTable instance.
   * @param inPrefViz a prefuse Visualization
   * @param inView a View as a JComponent.
   */
  public SimpleTableListener(JTable inTable, prefuse.Visualization inPrefViz,
      JComponent inView) {
    this.table = inTable;
    this.view = inView;
    this.prefViz = inPrefViz;
  }

  /**
   * Call when the mouse is clicked.
   * @param e a MouseEvent instance
   */
  public void mouseClicked(MouseEvent e) {
    if (e.getComponent().isEnabled() && e.getButton() == MouseEvent.BUTTON1) {
        Point p = e.getPoint();
        row = table.rowAtPoint(p);
        col = table.columnAtPoint(p);
        Object value = table.getValueAt(row, col);
        System.out.println(value);
        System.out.println("a");
        //table.setValueAt(value, row, col);
        System.out.println("b");
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println("pressed");
    final infovis.panel.VisualizationPanel panel =
       (infovis.panel.VisualizationPanel) view;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        panel.getVisualization().invalidate();
        panel.getVisualization().repaint();
      }
    });
  }

  @Override
  public void keyReleased(KeyEvent e) {
    System.out.println("released");
  }

  @Override
  public void keyTyped(KeyEvent e) {
    System.out.println("typed");
  }

  /*
  public void tableChanged(TableModelEvent e) {
    new Thread(new Runnable() {
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
           table.revalidate();
           table.repaint();
          }
        });
      }
  }).start();
  }
  */

}
