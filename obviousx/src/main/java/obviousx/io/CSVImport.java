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
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.SchemaImpl;
import obviousx.ObviousxException;
import obviousx.text.TypedFormat;
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
  private File file;

  /**
   * DataFactory used to create Table.
   */
  private DataFactory dataFactory;

  /**
   * FormatFactory.
   */
  private FormatFactory formatFactory;

  /**
   * Number of lines giving the schema in the CSV table.
   */
  private static final int HEADERSIZE = 3;

  /**
   * Separator for CSV.
   */
  private char separator;

  /**
   * Constructor for CSVReader.
   * @param nameInput table name
   * @param fileCSV  Input CSV file
   * @param reference reference schema
   * @param sep separator for CSV data
   * @param dFactory DataFactory to use to build the table.
   * @param fFactory FormatFactory to use to parse data in the CSV file.
   */
  public CSVImport(String nameInput, File fileCSV, Schema reference,
          char sep, DataFactory dFactory, FormatFactory fFactory) {
    this.name = nameInput;
    this.file = fileCSV;
    this.refSchema = reference;
    this.schema = new SchemaImpl(true, true);
    this.dataFactory = dFactory;
    this.formatFactory = fFactory;
    this.separator = sep;
  }

  /**
   * Constructor for CSVReader.
   * @param nameInput table name
   * @param fileCSV  Input CSV file
   * @param reference reference schema
   * @param dFactory DataFactory to use to build the table.
   * @param fFactory FormatFactory to use to parse data in the CSV file.
   */
  public CSVImport(String nameInput, File fileCSV, Schema reference,
      DataFactory dFactory, FormatFactory fFactory) {
    this(nameInput, fileCSV, reference, ',', dFactory, fFactory);
  }

  /**
   * Returns the table.
   * @return the table associated to the CSV file.
   */
  public Table getTable() {
    return this.table;
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
    CSVReader reader = new CSVReader(new FileReader(file), separator);
    ArrayList<String[]> content =  (ArrayList<String[]>) reader.readAll();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> type = new ArrayList<String>();
    ArrayList<Object> defaultValue = new ArrayList<Object>();
    for (int i = 0; i < HEADERSIZE; i++) {
      for (int j = 0; j < content.get(i).length; j++) {
        switch(i) {
          case 0 :
            title.add(content.get(i)[j]);
            break;
          case 1 :
              type.add(content.get(i)[j]);
            break;
          case 2 :
            TypedFormat format =
              formatFactory.getFormat(content.get(i - 1)[j]);
            Object value = ((Format) format).parseObject(content.get(i)[j]);
            defaultValue.add(value);
            break;
          default :
            break;
        }
      }
    }
    for (int i = 0; i < title.size(); i++) {
      TypedFormat format = formatFactory.getFormat(type.get(i));
      Class<?> spottedClass = format.getFormattedClass();
      this.schema.addColumn(title.get(i), spottedClass, defaultValue.get(i));
    }
    reader.close();
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
    try {
      this.createSchema();
    } catch (ObviousxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (this.validateSchema()) {
      this.table = dataFactory.createTable(this.name, this.refSchema);
      CSVReader readerBis = new CSVReader(new FileReader(file), separator);
      ArrayList<String[]> content = (ArrayList<String[]>) readerBis.readAll();
      for (int i = HEADERSIZE; i < content.size(); i++) {
        int rowId = this.table.addRow();
        for (int j = 0; j < content.get(i).length; j++) {
          TypedFormat format = formatFactory
            .getFormat(schema.getColumnType(j).getSimpleName());
          Object val = ((Format) format).parseObject(content.get(i)[j]);
          this.table.set(rowId - 1, j, val);
        }
      }
      readerBis.close();
    } else {
      throw new IOException("Reference schema doesn't match CSV schema!");
    }
  }
}
