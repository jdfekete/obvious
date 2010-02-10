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

import java.io.File;

import obvious.data.Schema;
import obviousx.ObviousxException;
import obviousx.io.GraphMLImport;

import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Example of GraphMLImport usage.
 * @author Pierre-Luc Hemery
 *
 */
public final class GraphMLImportExample {

  /**
   * Constructor.
   */
  private GraphMLImportExample() {
  }

  /**
   * Main method.
   * @param args argument of the main
   * @throws ObviousxException when importation failed
   */
  public static void main(String[] args) throws ObviousxException {

    // Load GraphML File
    File inputFile = new File("src/main/resources/example.graphML");
    System.setProperty(XmlPullParserFactory.PROPERTY_NAME,
        "org.kxml2.io.KXmlParser");

    // Create GraphMLImporter
    GraphMLImport importer = new GraphMLImport(inputFile, null, "source",
        "target");
    importer.readSchema();
    Schema nodeSchema = importer.getNodeSchema();

    System.out.println("toto");

    for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
      System.out.print(nodeSchema.getColumnName(i) + ", ");
      System.out.print(nodeSchema.getColumnType(i) + ", ");
      System.out.print(nodeSchema.getColumnDefault(i));
      System.out.println();
    }
  }

}
