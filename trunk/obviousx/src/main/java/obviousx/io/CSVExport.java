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

package obviousx.io;

import java.io.File;
import java.io.FileWriter;
import java.text.Format;

import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Export an obvious table in CSV format.
 * @author Pierre-Luc Hemery
 *
 */
public class CSVExport implements Exporter {

  /**
   * Name to give to the CSV file.
   */
  private String name;

  /**
   * Table to export in CSV.
   */
  private Table table;

  /**
   * CSV file to create.
   */
  private File file;

  /**
   * CSVWriter to create CSVFile.
   */
  private CSVWriter writer;

  /**
   * Format Factory to create string from object.
   */
  private FormatFactory formatFactory;

  /**
   * CSVExport constructor.
   * @param nameInput name for the CSV file
   * @param tableInput table to build in CSV
   * @throws ObviousxException when file creation failed.
   */
  public CSVExport(String nameInput, Table tableInput)
      throws ObviousxException {
    try {
    this.name = nameInput;
    this.table = tableInput;
    file = new File(name + ".csv");
    FileWriter fileWriter = new FileWriter(file);
    writer = new CSVWriter(fileWriter);
    this.formatFactory = new FormatFactoryImpl();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Gets the FormatFactory of the importer.
   * @return the FormatFactory attribute
   */
  public FormatFactory getFormatFactory() {
    return this.formatFactory;
  }

  /**
   * Gets the CSV file.
   * @return the CSV file for the table attribute
   */
  public Table getTable() {
    return this.getTable();
  }

  /**
   * Sets the FormatFactory of the importer.
   * @param inputFormatFactory the factory to set
   */
  public void setFormatFactory(FormatFactory inputFormatFactory) {
    this.formatFactory = inputFormatFactory;
  }

  /**
   * Create a CSV file from the obvious table.
   * @throws ObviousxException if an exception occurs
   */
  public void createFile() throws ObviousxException {
    try {
    int numberColumn = this.table.getSchema().getColumnCount();
    int numberRow = this.table.getRowCount();
    String[] title =  new String[numberColumn];
    String[] type = new String[numberColumn];
    String[] defaultValue = new String[numberColumn];
    // fill the schema in the csv file.
    for (int i = 0; i < numberColumn; i++) {
      title[i] = this.table.getSchema().getColumnName(i);
      type[i] = this.table.getSchema().getColumnType(i).getSimpleName();
      try {
        TypedFormat format = formatFactory.getFormat(
            table.getSchema().getColumnDefault(i).getClass().getSimpleName());
        defaultValue[i] =
          ((Format) format).format(this.table.getSchema().getColumnDefault(i));
      } catch (NullPointerException e) {
        defaultValue[i] = "null";
      }
    }
    writer.writeNext(title);
    writer.writeNext(type);
    writer.writeNext(defaultValue);
    // fill data in the csv file.
    for (int i = 0; i < numberRow; i++) {
      String[] currentRow = new String[numberColumn];
      for (int j = 0; j < numberColumn; j++) {
        currentRow[j] = this.table.getValue(i, j).toString();
      }
      writer.writeNext(currentRow);
    }
    writer.close();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }
}
