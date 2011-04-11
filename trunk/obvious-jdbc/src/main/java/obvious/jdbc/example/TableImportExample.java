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

package obvious.jdbc.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Table;
import obvious.impl.DataFactoryImpl;
import obvious.jdbc.io.DatabaseImporter;
import obvious.jdbc.utils.FormatFactorySQL;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;

/**
 * Example from importing MySQL Database to Obvious table.
 * @author Pierre-Luc Hemery
 *
 */
public class TableImportExample {

  /**
   * Main method : inspect a database and load its tables as obvious ones.
   * @param args array of connection parameters (login, URL...).
   * @throws ObviousException if bad things happen during DataFactory creation
   * @throws ObviousxException if problems happen during FormatFactory creation
   * @throws SQLException if bad things happen during database connection
   */
  public static void main(String[] args) throws ObviousException,
        ObviousxException, SQLException {

    // Getting database connection parameters
    String url = args[0];
    String userName = args[1];
    String password;
    try {
      password = args[2];
    } catch (ArrayIndexOutOfBoundsException e) {
      password = "";
    }

    // Loading table and format factories
    System.setProperty("obvious.DataFactory", "obvious.impl.DataFactoryImpl");
    System.setProperty("obviousx.FormatFactory",
        "obvious.jdbc.FormatFactorySQL");
    DataFactory dFactory = DataFactoryImpl.getInstance();

/*
    // Create the database importer
    String driver = "com.mysql.jdbc.Driver";
    DatabaseImporter dbImporter = new DatabaseImporter(driver, url, userName,
            password, dFactory, fFactory);

    dbImporter.importTables();

    ArrayList<Table> tableList = new ArrayList<Table>();

    for (Iterator<Table> i = dbImporter.getTableMap().values().iterator();
            i.hasNext();) {
      tableList.add(i.next());
    }


    for (int k = 0; k < tableList.size(); k++) {
      // Create a JTable for the obvious table
      String[] title = new
            String[tableList.get(k).getSchema().getColumnCount()];
      Object[][] data =
            new Object[tableList.get(k).getRowCount()]
                     [tableList.get(k).getSchema().getColumnCount()];
      for (int i = 0; i < tableList.get(k).getSchema().getColumnCount(); i++) {
        title[i] = tableList.get(k).getSchema().getColumnName(i);
      }
      for (int i = 0; i < tableList.get(k).getRowCount(); i++) {
        for (int j = 0; j < tableList.get(k).getSchema().getColumnCount();
            j++) {
          data[i][j] = tableList.get(k).getValue(i, j);
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
    */
  }
}
