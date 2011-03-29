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

import obvious.data.Network;
//import obvious.prefuse.utils.wrappers.WrapToPrefGraph;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;
import prefuse.data.io.AbstractGraphWriter;
import prefuse.data.io.DataIOException;

/**
 * Wrapper for Prefuse writter to be compatible with obvious.
 * @author Hemery
 *
 */
public class PrefuseObviousGraphWriter implements Exporter {

  /**
   * Target file.
   */
  private File file;

  /**
   * Exported obvious network.
   */
  private Network network;

  /**
   * Wrapped prefuse writer.
   */
  private AbstractGraphWriter prefWriter;

  /**
   * Constructor.
   * @param writer wrapped writer
   * @param inNetwork network to export
   * @param inFile target file
   */
  public PrefuseObviousGraphWriter(AbstractGraphWriter writer,
      Network inNetwork, File inFile) {
    this.prefWriter = writer;
    this.file = inFile;
  }

  @Override
  public void createFile() throws ObviousxException {
    try {
      //prefWriter.writeGraph(new WrapToPrefGraph(network), file);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public FormatFactory getFormatFactory() {
    return null;
  }

  @Override
  public void setFormatFactory(FormatFactory formatFactory) {
    return;
  }

}
