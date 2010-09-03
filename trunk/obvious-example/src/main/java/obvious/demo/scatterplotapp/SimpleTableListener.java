package obvious.demo.scatterplotapp;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

import obvious.impl.TupleImpl;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class SimpleTableListener implements KeyListener {

  private JTable table;
  
  private JComponent view;
  
  private prefuse.Visualization prefViz;
  
  private int row, col;
  
  public SimpleTableListener(JTable table, prefuse.Visualization prefViz, JComponent view) {
    this.table = table;
    this.view = view;
    this.prefViz = prefViz;
    }

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

  public void keyPressed(KeyEvent e) {
    System.out.println("pressed");
    final infovis.panel.VisualizationPanel panel = (infovis.panel.VisualizationPanel) view;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        panel.getVisualization().invalidate();
        panel.getVisualization().repaint();
      }
    });
  }

  public void keyReleased(KeyEvent e) {
    System.out.println("released");
  }

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
