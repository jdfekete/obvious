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
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.DataFactoryImpl;
import obvious.impl.SchemaImpl;
import obviousx.ObviousxException;
import obviousx.io.CSVImport;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

/**
 * Second example for CVSImport use.
 * @author Pierre-Luc Hemery
 *
 */
public class CSVImportExample2 {

  /**
   * Main.
   * @param args arguments for main.
   * @throws IOException Exception risen when bad input file.
   * @throws ObviousException if DataFactory creation failed.
   * @throws ParseException when CSV file is bad formatted.
   * @throws ObviousxException  if FormatFactory creation failed.
   */
  public static void main(String[] args)
    throws IOException, ObviousException, ParseException, ObviousxException {

    // Load CSV File
    File inputFile = new File("src/main/resources/example.csv");

    // Create Reference schema
    Schema schema = new SchemaImpl(true, true);
    schema.addColumn("Name", String.class, "");
    schema.addColumn("Age", Integer.class, 0);
    schema.addColumn("Birthday", Date.class, null);

    // Create Factory
    System.setProperty("obvious.DataFactory", "obvious.impl.DataFactoryImpl");
    System.setProperty("obviousx.FormatFactory",
        "obviousx.util.FormatFactoryImpl");
    DataFactory dFactory = DataFactoryImpl.getInstance();
    FormatFactory fFactory = FormatFactoryImpl.getInstance();

    // Create CSV Importer
    CSVImport importer = new CSVImport("example", inputFile, schema,
        dFactory, fFactory);
    importer.createTable();
    Table table = importer.getTable();

    // Create a JTable for the obvious table
    String[] title = new String[table.getSchema().getColumnCount()];
    Object[][] data =
        new Object[table.getRowCount()][table.getSchema().getColumnCount()];
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      title[i] = table.getSchema().getColumnName(i);
    }
    for (int i = 0; i < table.getRowCount(); i++) {
      for (int j = 0; j < table.getSchema().getColumnCount(); j++) {
        data[i][j] = table.getValue(i, j);
      }
    }
    JTable jtable = new JTable(data, title);

    JScrollPane scrollPane = new JScrollPane(jtable);
    jtable.setFillsViewportHeight(true);

    JFrame frame = new JFrame("Obvious CSV Example!");
    frame.add(scrollPane);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }
}
