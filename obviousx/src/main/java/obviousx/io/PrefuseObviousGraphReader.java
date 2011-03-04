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
import java.io.InputStream;

import obvious.ObviousException;
import obvious.data.Data;
import obvious.data.DataFactory;
import obvious.data.Network;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;
import prefuse.data.io.AbstractGraphReader;
import prefuse.data.io.DataIOException;

/**
 * Wrapper for Prefuse reader to be compatible with obvious.
 * @author Hemery
 *
 */
public class PrefuseObviousGraphReader implements GraphImporter {

  /**
   * Wrapped prefuse graph reader.
   */
  private AbstractGraphReader prefReader;

  /**
   * Obvious Network.
   */
  private Network network;

  /**
   * Input stream.
   */
  private InputStream stream;

  /**
   * Constructor.
   * @param reader prefuse table reader to wrap
   * @param file input file
   */
  public PrefuseObviousGraphReader(AbstractGraphReader reader, File file) {
    this.prefReader = reader;
    try {
      this.stream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Data getData() {
    return network;
  }

  @Override
  public FormatFactory getFormatFactory() {
    return null;
  }

  @Override
  public void loadTable() throws ObviousxException {
    try {
      prefuse.data.Graph prefGraph = prefReader.readGraph(stream);
      String oldProperty = System.getProperty("obvious.DataFactory");
      System.setProperty("obvious.DataFactory",
          "obvious.prefuse.PrefuseDataFactory");
      network = DataFactory.getInstance().wrapGraph(prefGraph);
      if (oldProperty != null) {
        System.setProperty("obvious.DataFactory", oldProperty);
      }
    } catch (DataIOException e) {
      e.printStackTrace();
    } catch (ObviousException e) {
      e.printStackTrace();
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
