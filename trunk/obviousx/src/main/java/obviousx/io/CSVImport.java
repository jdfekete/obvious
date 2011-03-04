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
import java.io.FileReader;
import java.text.Format;
import java.util.ArrayList;

import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;
import au.com.bytecode.opencsv.CSVReader;


/**
 * CSV Import class.
 * Allows to convert a CSV table as an obvious table.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class CSVImport implements TableImporter {

  /**
   * Input file.
   */
  private File file;

  /**
   * Input table, that will be loaded with content of the external medium.
   */
  private Table table;

  /**
   * Schema of the table described by the file.
   */
  private Schema fileSchema;

  /**
   * Separator used in CSV.
   */
  private char separator;

  /**
   * FormatFactory of this Importer.
   */
  private FormatFactory formatFactory;

  /**
   * Number of lines giving the schema in the CSV table.
   */
  private static final int HEADERSIZE = 3;


  /**
   * Constructor.
   * @param inputFile external file to load
   * @param sep the separator char used for CSV
   */
  public CSVImport(File inputFile, Table inputTable, char sep) {
    this.file = inputFile;
    this.table = inputTable;
    this.separator = sep;
    this.formatFactory = new FormatFactoryImpl();
  }
  
  public CSVImport(File inputFile, char sep) {
    this(inputFile, null, sep);
  }

  /**
   * Gets the FormatFactory of the importer.
   * @return the FormatFactory attribute
   */
  public FormatFactory getFormatFactory() {
    return this.formatFactory;
  }

  /**
   * Sets the FormatFactory of the importer.
   * @param inputFormatFactory the factory to set
   */
  public void setFormatFactory(FormatFactory inputFormatFactory) {
    this.formatFactory = inputFormatFactory;
  }

  /**
   * Gets the schema attribute of the importer. Returns null when the schema was
   * not read yet.
   * @return the schema attribute
   */
  public Schema getSchema() {
    return this.fileSchema;
  }

  /**
   * Reads the schema of the file.
   * @throws ObviousxException if an exception occurs.
   */
  public void readSchema() throws ObviousxException {
    try {
      CSVReader reader = new CSVReader(new FileReader(file), separator);
      ArrayList<String> title = new ArrayList<String>();
      ArrayList<String> type = new ArrayList<String>();
      ArrayList<Object> defaultValue = new ArrayList<Object>();
      String[] nextline = null;
      Integer lineCount = 0;
      while (lineCount < HEADERSIZE) {
        nextline = reader.readNext();
        for (int j = 0; j < nextline.length; j++) {
          switch(lineCount) {
            case 0 :
              title.add(nextline[j]);
              break;
            case 1 :
              type.add(nextline[j]);
              break;
            case 2 :
              defaultValue.add(nextline[j]);
              break;
            default :
              break;
          }
        }
        lineCount++;
      }
      
      // Lazely initialize the schema here, it also avoids throwing exceptions from the ctor.
      this.fileSchema = DataFactory.getInstance().createSchema();
      
      for (int i = 0; i < title.size(); i++) {
        TypedFormat format = formatFactory.getFormat(type.get(i));
        Class<?> spottedClass = format.getFormattedClass();
        this.fileSchema.addColumn(title.get(i), spottedClass,
            defaultValue.get(i));

      }
      reader.close();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Loads the table with the data of the external file.
   * @throws ObviousxException when exception occurs
   */
  public Table loadTable() throws ObviousxException {
    try {
      this.readSchema();
      if (this.table == null) {
      	this.table = DataFactory.getInstance().createTable(fileSchema);
      }
      
      CSVReader reader = new CSVReader(new FileReader(file), separator);
      String[] nextline;
      Integer lineCount = 0;
      while ((nextline = reader.readNext()) != null) {
        if (lineCount >= HEADERSIZE) {
          this.table.addRow();
          int rowId = this.table.getRowCount();
          for (int j = 0; j < nextline.length; j++) {
            TypedFormat format = formatFactory
              .getFormat(this.fileSchema.getColumnType(j).getSimpleName());
            Object val = ((Format) format).parseObject(nextline[j]);
            this.table.set(rowId - 1, j, val);
          }
        }
        lineCount++;
      }
      reader.close();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
    
    return this.table;
  }

}
