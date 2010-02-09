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


import java.io.IOException;
import java.util.Date;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.DataFactoryImpl;
import obvious.impl.SchemaImpl;
import obviousx.ObviousxException;
import obviousx.io.CSVExport;

/**
 * Example class for CVExport.
 * @author Pierre-Luc Hemery
 *
 */
public final class CSVExportExample {

  /**
   * Constructor.
   */
  private CSVExportExample() {
  }

  /**
   * Main.
   * @param args arguments for the main.
   * @throws ObviousException occurs if Table creation failed.
   * @throws IOException when CSV file creation failed.
   * @throws ObviousxException when FormatFactory creation failed
   */
  public static void main(String[] args)
      throws ObviousException, ObviousxException, IOException {

    String filePath = "";
    String factoryPath = "";

    try {
      filePath = args[0];
      factoryPath = args[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      filePath = "C:\\outputObvious\\stupidtest\\DefaultTest.csv";
      factoryPath = "obvious.impl.DataFactoryImpl";
    }

    // Create the table to export in CSV format.
    Schema schema = new SchemaImpl(true, true);
    schema.addColumn("Name", String.class, "John Doe");
    schema.addColumn("Age", Integer.class, 0);
    schema.addColumn("Birthday", Date.class, null);

    System.setProperty("obvious.DataFactory", factoryPath);
    DataFactory dFactory = DataFactoryImpl.getInstance();
    Table table = dFactory.createTable("table", schema);

    // Fill table.
    table.addRow();
    table.addRow();
    table.addRow();
    table.set(0, 0, "Mike");
    table.set(0, 1, 23);
    table.set(0, 2, new Date(256));
    table.set(1, 0, "Alice");
    table.set(1, 1, 45);
    table.set(1, 2, new Date(256000));
    table.set(2, 0, "Bob");
    table.set(2, 1, 37);
    table.set(2, 2, new Date(2560001));

    // Create the exporter to CSV.
    CSVExport exporter = new CSVExport(filePath, table);
    exporter.createFile();

  }
}
