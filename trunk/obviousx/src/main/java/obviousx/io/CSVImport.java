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

import java.io.FileReader;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;
import au.com.bytecode.opencsv.CSVReader;


/**
 * CSV Import class.
 * Allows to convert a CSV table as an obvious table.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class CSVImport implements Importer {

  /**
   * Table name.
   */
  private String name;

  /**
   * Obvious table to obtain.
   */
  private Table table;

  /**
   * Schema deduced from the CSV table.
   */
  private Schema schema;

  /**
   * Reference schema to validate, table created from CSV description.
   */
  private Schema refSchema;

  /**
   * Input stream.
   */
  private FileReader file;

  /**
   * Number of lines giving the schema in the CSV table.
   */
  private static final int HEADERSIZE = 3;

  /**
   * Constructor for CSVReader.
   * @param nameInput table name
   * @param fileCSV  Input CSV file
   * @param reference reference schema
   */
  public CSVImport(String nameInput, FileReader fileCSV, Schema reference) {
    this.name = nameInput;
    this.file = fileCSV;
    this.refSchema = reference;
  }

  /**
   * Create an obvious schema from the CVS file.
   * @throws IOException for input problems
   * @throws ObviousxException when a bad schema structure is used in CSV.
   * @throws ClassNotFoundException when a bad class name is given in CSV.
   * @throws ParseException when a bad default value is given in CSV.
   */
  public void createSchema()
      throws IOException, ObviousxException, ClassNotFoundException,
              ParseException {
    CSVReader reader = new CSVReader(file);
    ArrayList<String[]> content =  (ArrayList<String[]>) reader.readAll();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<Class<?>> type = new ArrayList<Class<?>>();
    ArrayList<Object> defaultValue = new ArrayList<Object>();
    for (int i = 0; i < HEADERSIZE; i++) {
      for (int j = 0; j < content.get(i).length - 1; j++) {
        switch(i) {
          case 0 :
            title.add(content.get(i)[j]);
            break;
          case 1 :
            try {
              Class<?> className = Class.forName(content.get(i)[j]);
              type.add(className);
            } catch (Exception e) {
              throw new IOException(e);
            }
            break;
          case 2 :
            FormatFactory factory = FormatFactory.getInstance();
            Format format =
                factory.getFormat(Class.forName(content.get(i - 1)[j]));
            Object value = format.parseObject(content.get(i)[j]);
            defaultValue.add(value);
            break;
          default :
            break;
        }
      }
    }
    for (int i = 0; i < title.size(); i++) {
      try {
        this.schema.addColumn(title.get(i), type.get(i), defaultValue.get(i));
      } catch (Exception e) {
        throw new ObviousxException(e);
      }
    }
  }

  /**
   * Validates the created schema with the reference.
   * @return true if the schema is equals to the reference.
   */
  public boolean validateSchema() {
    if (this.schema.getColumnCount() != this.refSchema.getColumnCount()) {
      return false;
    } else {
      for (int i = 0; i < this.schema.getRowCount(); i++) {
        if (!schema.getColumnName(i).equals(refSchema.getColumnName(i))
            || !schema.getColumnType(i).equals(refSchema.getColumnType(i))) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Creates table from the CVS description.
   * @throws ObviousException occurs when table cannot be created.
   * @throws IOException occurs when the CSV table has a bad format.
   * @throws ParseException  occurs when data contained in CSV have bad format.
   */
  public void createTable()
      throws ObviousException, IOException, ParseException {
    if (!this.validateSchema()) {
      return;
    }
    DataFactory factory = DataFactory.getInstance();
    this.table = factory.createTable(this.name, this.refSchema);
    CSVReader reader = new CSVReader(file);
    ArrayList<String[]> content = (ArrayList<String[]>) reader.readAll();
    for (int i = HEADERSIZE; i < content.size(); i++) {
      int rowId = this.table.addRow();
      for (int j = 0; j < content.get(i).length; j++) {
        FormatFactory formatFactory = FormatFactory.getInstance();
        Format format = formatFactory.getFormat(schema.getColumnType(j));
        Object val = format.parseObject(content.get(i)[j]);
        this.table.set(rowId, j, val);
      }
    }
  }
}