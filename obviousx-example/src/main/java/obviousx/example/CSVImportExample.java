/*
* Copyright (c) 2009, INRIA
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

package obviousx.example;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.DataFactoryImpl;
import obvious.impl.SchemaImpl;
import obviousx.ObviousxException;
import obviousx.io.impl.CSVImport;
import obviousx.io.impl.ObviousTableModel;


/**
 * Example class for CVSImport.
 * @author Pierre-Luc Hemery
 *
 */
public final class CSVImportExample {

  /**
   * Constructor.
   */
  private CSVImportExample() {
  }

  /**
   * Main.
   * @param args arguments for main.
   * @throws ObviousxException  if an exceptions occurs when CSV is created
   * @throws ObviousException  if an exception occurs when table is created
   */
  public static void main(String[] args) throws ObviousxException,
    ObviousException {

    String factoryPath = "";

    try {
      factoryPath = args[0];
    } catch (ArrayIndexOutOfBoundsException e) {
      factoryPath = "obvious.impl.DataFactoryImpl";
    }

    // Load CSV File
    File inputFile = new File("src/main/resources/lri.csv");

    // Create Base Table.
    Schema schema = new SchemaImpl(true, true);
    schema.addColumn("Name", String.class, "");
    schema.addColumn("Lab", String.class, "");
    schema.addColumn("Status", String.class, "");
    System.setProperty("obvious.DataFactory", factoryPath);
    DataFactory dFactory = DataFactoryImpl.getInstance();
    Table table = dFactory.createTable(schema);


    // Create CSV Importer
    CSVImport importer = new CSVImport(inputFile, table, ';');
    importer.loadTable();

    // Display Obvious Table

    TableModel tableModeled = new ObviousTableModel(table);
    JTable jtable = new JTable(tableModeled);

    JScrollPane scrollPane = new JScrollPane(jtable);
    jtable.setFillsViewportHeight(true);

    JFrame frame = new JFrame("Obvious CSV Example implemented with "
        + table.getClass().getSimpleName());
    frame.add(scrollPane);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

}
