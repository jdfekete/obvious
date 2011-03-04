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

import infovis.graph.DefaultGraph;
import infovis.graph.io.AbstractGraphReader;
import infovis.graph.io.GraphReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Network;
import obviousx.ObviousxException;
import obviousx.ObviousxRuntimeException;
import obviousx.util.FormatFactory;

/**
 * Wrapper for ivtk reader to be compatible with obvious.
 * @author Hemery
 *
 */
public class IvtkObviousGraphReader implements GraphImporter {

  /**
   * Obvious network to fill.
   */
  private Network network;

  /**
   * Ivtk graph reader.
   */
  private AbstractGraphReader ivtkReader;

  /**
   * Constructor.
   * @param reader prefuse table reader to wrap
   */
  public IvtkObviousGraphReader(AbstractGraphReader reader) {
    this.ivtkReader = reader;
  }

  /**
   * Constructor.
   * @param name name of the entry format
   * @param inFile file to import
   */
  public IvtkObviousGraphReader(String name, File inFile) {
    try {
      ivtkReader = (AbstractGraphReader) GraphReaderFactory.createGraphReader(
          new FileInputStream(inFile), name, new DefaultGraph());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public FormatFactory getFormatFactory() {
    return null;
  }

  @Override
  public Network loadGraph() throws ObviousxException {
    boolean success = this.ivtkReader.load();
    if (success) {
      infovis.Graph graph = this.ivtkReader.getGraph();
      String oldProperty = System.getProperty("obvious.DataFactory");
      System.setProperty("obvious.DataFactory",
          "obvious.ivtk.data.IvtkDataFactory");
      try {
        network = DataFactory.getInstance().wrapGraph(graph);
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
    return this.network;
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
