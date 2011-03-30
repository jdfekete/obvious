/*
* Copyright (c) 2010, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package obvious.demo.scatterplotapp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import obvious.data.Table;
import obvious.impl.TupleImpl;

/**
 * Dialog box for adding a point.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class AddPointDialog extends JDialog implements ActionListener {

  /**
   * X field.
   */
  private JFormattedTextField xField;

  /**
   * Y Field.
   */
  private JFormattedTextField yField;

  /**
   * OK button.
   */
  private JButton okButton;

  /**
   * Formats to format and parse numbers.
   */
  private NumberFormat numberFormat;

  /**
   * Obvious table.
   */
  private Table table;

  /**
   * Prefuse visualization.
   */
  private prefuse.Visualization prefVis;

  /**
   * Ivtk view component.
   */
  private JComponent ivtkViewComponent;

  /**
   * Scroll pane for JTable.
   */
  private JScrollPane jTablePane;

  /**
   * Constructor.
   * @param frame associated frame
   * @param inTable an Obvious table
   * @param inPrefVis a prefuse Visualization
   * @param jtablePanel a scrollPane
   * @param inIvtkViewComponent an Ivtk view (as a JComponent)
   */
  public AddPointDialog(JFrame frame, Table inTable,
      prefuse.Visualization inPrefVis, JScrollPane jtablePanel,
      JComponent inIvtkViewComponent) {
    super(frame, "Add a point (X,Y)", true);
    this.setLocationRelativeTo(frame);
    this.table = inTable;
    this.jTablePane = jtablePanel;
    this.prefVis = inPrefVis;
    this.ivtkViewComponent = inIvtkViewComponent;
    final int colNum = 10;
    xField = new JFormattedTextField(numberFormat);
    xField.setValue(new Double(0));
    xField.setColumns(colNum);
    yField = new JFormattedTextField(numberFormat);
    yField.setValue(new Double(0));
    yField.setColumns(colNum);
    JLabel xLabel = new JLabel("X");
    JLabel yLabel = new JLabel("Y");
    JPanel xPanel = new JPanel();
    JPanel yPanel = new JPanel();
    okButton = new JButton("OK");
    okButton.addActionListener(this);
    xPanel.add(xLabel, 0);
    xPanel.add(xField, 1);
    yPanel.add(yLabel, 0);
    yPanel.add(yField, 1);
    this.add(xPanel, BorderLayout.NORTH);
    this.add(yPanel, BorderLayout.CENTER);
    this.add(okButton, BorderLayout.SOUTH);
    this.pack();
    this.setVisible(true);
  }

  /**
   * Calls when associated buttons are selected.
   * @param e event
   */
  public void actionPerformed(final ActionEvent e) {
    final JDialog dialog = this;
    new Thread(new Runnable() {
      public void run() {
      if (e.getSource() == okButton) {
        System.out.println("OK");
        int x = ((Number) xField.getValue()).intValue();
        int y = ((Number) yField.getValue()).intValue();
        prefVis.cancel("draw");
        table.addRow(new TupleImpl(
            table.getSchema(), new Object[] {x, y, "unemployed"}));
        prefVis.run("draw");
        final infovis.panel.VisualizationPanel panel = (
            infovis.panel.VisualizationPanel) ivtkViewComponent;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            prefVis.run("draw");
            prefVis.getDisplay(0).revalidate();
            prefVis.getDisplay(0).repaint();
            panel.getVisualization().invalidate();
            panel.getVisualization().repaint();
            jTablePane.revalidate();
            jTablePane.repaint();
            jTablePane.getComponent(0).invalidate();
            jTablePane.getComponent(0).repaint();
            dialog.dispose();
          }
        });
      }
      }
    }).start();
  }

}
