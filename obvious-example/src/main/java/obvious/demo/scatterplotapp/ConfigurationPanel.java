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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import obvious.data.Table;
import obviousx.io.ObviousTableModel;

/**
 * A configuration panel for view.
 * @author plhemery
 *
 */
@SuppressWarnings("serial")
public class ConfigurationPanel extends JSplitPane implements ActionListener {

  /**
   * Add point button.
   */
  private JButton addButton;

  /**
   * Remove point button.
   */
  private JButton removeButton;

  /**
   * Refresh view button.
   */
  private JButton refreshButton;

  /**
   * Obvious back table.
   */
  private Table table;

  /**
   * JFrame.
   */
  private JFrame frame;

  /**
   * Prefuse visualization.
   */
  private prefuse.Visualization prefVis;

  /**
   * Ivtk view component.
   */
  private JComponent ivtkViewComponent;

  /**
   * JTable for obvious table.
   */
  private JTable obviousJTable;

  /**
   * Scroll pane for JTable.
   */
  private JScrollPane jTablePane;
  /**
   * Constructor.
   * @param inTable an obvious table
   * @param inFrame associated frame
   * @param inPrefVis prefuse visualization
   * @param inIvtkViewComponent ivtk view component
   */
  public ConfigurationPanel(Table inTable, JFrame inFrame,
      prefuse.Visualization inPrefVis, JComponent inIvtkViewComponent) {
    super(JSplitPane.VERTICAL_SPLIT);
    this.table = inTable;
    this.frame = inFrame;
    this.prefVis = inPrefVis;
    this.ivtkViewComponent = inIvtkViewComponent;
    obviousJTable = new JTable(new ObviousTableModel(table));
    jTablePane = new JScrollPane(obviousJTable);
    JPanel editPane = new JPanel();
    addButton = new JButton("+");
    addButton.addActionListener(this);
    removeButton =  new JButton("-");
    removeButton.addActionListener(this);
    refreshButton = new JButton("refresh");
    refreshButton.addActionListener(this);
    editPane.add(addButton, 0);
    editPane.add(removeButton, 1);
    editPane.add(refreshButton, 2);
    this.add(jTablePane, 0);
    this.add(editPane);
  }

  /**
   * Calls when associated buttons are selected.
   * @param e event
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
      @SuppressWarnings("unused")
      JDialog dialog = new AddPointDialog(frame, table, prefVis,
          jTablePane, ivtkViewComponent);
    } else if (e.getSource() == removeButton) {
      int[] rows = obviousJTable.getSelectedRows();
      prefVis.cancel("draw");
      for (int i = 0; i < rows.length; i++) {
        table.removeRow(obviousJTable.convertRowIndexToModel(rows[i]));
      }
    }
    final infovis.panel.VisualizationPanel panel =
      (infovis.panel.VisualizationPanel) ivtkViewComponent;
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
      }
    });
  }

}
