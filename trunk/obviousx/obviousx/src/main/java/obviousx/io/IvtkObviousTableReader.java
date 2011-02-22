/*
* Copyright (c) 2011, INRIA
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import infovis.io.AbstractReader;
import infovis.table.DefaultTable;
import infovis.table.io.AbstractTableReader;
import infovis.table.io.TableReaderFactory;
import obvious.ObviousException;
import obvious.data.Data;
import obvious.data.DataFactory;
import obvious.data.Table;
import obviousx.ObviousxException;
import obviousx.ObviousxRuntimeException;
import obviousx.util.FormatFactory;

/**
 * Wrapper for ivtk reader to be compatible with obvious.
 * @author Hemery
 *
 */
public class IvtkObviousTableReader implements Importer {

  /**
   * Obvious table.
   */
  private Table table;

  /**
   * Wrapped ivtk table reader.
   */
  private AbstractTableReader ivtkReader;

  /**
   * Constructor.
   * @param reader ivtk reader to wrap
   */
  public IvtkObviousTableReader(AbstractTableReader reader) {
    this.ivtkReader = reader;
  }

  /**
   * Constructor.
   * @param name name of the entry format
   * @param file file to import
   */
  public IvtkObviousTableReader(String name, File file) {
    AbstractReader reader = TableReaderFactory.createTableReader(name,
        new DefaultTable());
    System.out.println(reader == null);
    try {
      reader.setIn(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    this.ivtkReader = (AbstractTableReader) reader;
  }

  @Override
  public Data getData() {
    return this.table;
  }

  @Override
  public FormatFactory getFormatFactory() {
    return null;
  }

  @Override
  public void loadTable() throws ObviousxException {
    boolean success = ivtkReader.load();
    if (success) {
      infovis.Table ivtkTable = ivtkReader.getTable();
      String oldProperty = System.getProperty("obvious.DataFactory");
      System.setProperty("obvious.DataFactory",
          "obvious.ivtk.data.IvtkDataFactory");
      try {
        table = DataFactory.getInstance().wrapTable(ivtkTable);
      } catch (ObviousException e) {
        e.printStackTrace();
      }
      if (oldProperty != null) {
        System.setProperty("obvious.DataFactory", oldProperty);
      }
    } else {
      throw new ObviousxRuntimeException(
          "Can't import the given file " + ivtkReader.getName());
    }
  }

  @Override
  public void readSchema() throws ObviousxException {
    return;
  }

  @Override
  public void setFormatFactory(FormatFactory formatFactory) {
    return;
  }

}
