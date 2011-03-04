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

import obvious.data.Network;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;

/**
 * A general interface to manage import from an external format to obvious
 * table.
 * @author Pierre-Luc Hemery
 *
 */
public interface GraphImporter {
  /**
   * Get the FormatFactory associated to this Importer.
   * @return the FormatFactory of this Importer
   */
  FormatFactory getFormatFactory();

  /**
   * Sets for the Importer the format factory.
   * @param formatFactory the factory to set
   */
  void setFormatFactory(FormatFactory formatFactory);

  /**
   * Reads the schema of an external medium.
   * @throws ObviousxException when a bad schema structure is used in the file.
   */
  void readSchema() throws ObviousxException;

  /**
   * Load the content of an external medium (file, db) into an Obvious Table.
   * @throws ObviousxException when an exception occurs.
   * @return an obvious Network
   */
  Network loadGraph() throws ObviousxException;

}
