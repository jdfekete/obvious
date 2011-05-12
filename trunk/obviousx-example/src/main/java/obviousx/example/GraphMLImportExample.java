/*
* Copyright (c) 2010, INRIA
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
import java.util.HashMap;
import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.DataFactoryImpl;
import obvious.impl.SchemaImpl;
import obviousx.ObviousxException;
import obviousx.io.impl.GraphMLImport;

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
   * @throws ObviousException when importation failed
   */
  public static void main(String[] args) throws ObviousxException,
      ObviousException {

    String factoryPath = "";

    try {
      factoryPath = args[0];
    } catch (ArrayIndexOutOfBoundsException e) {
      factoryPath = "obvious.prefuse.data.PrefuseDataFactory";
    }

    // Load GraphML File
    File inputFile = new File("src//main//resources//example.graphML//");
    System.out.println(inputFile.getAbsolutePath());
    // Create network
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("nodeId", int.class, 0);
    nodeSchema.addColumn("color", String.class, "yellow");

    Schema edgeSchema = new SchemaImpl();
    edgeSchema.addColumn("#FirstVertex", int.class, 0);
    edgeSchema.addColumn("#SecondVertex", int.class, 0);

    System.setProperty("obvious.DataFactory", factoryPath);
    DataFactory dFactory = DataFactoryImpl.getInstance();

    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("sourceKey", "#FirstVertex");
    paramMap.put("targetKey", "#SecondVertex");
    paramMap.put("nodeKey", "nodeId");
    Network network = dFactory.createGraph(nodeSchema,
        edgeSchema, paramMap);
    // Create GraphMLImporter
    GraphMLImport importer = new GraphMLImport(inputFile, network,
        "#FirstVertex", "#SecondVertex", "nodeId");
    importer.loadGraph();

    System.out.println("Nodes : ");
    for (Node node : network.getNodes()) {
      for (int i = 0; i < node.getSchema().getColumnCount(); i++) {
        System.out.print(node.get(i) + ", ");
      }
      System.out.println();
    }

    System.out.println("Edges : ");

    for (Edge edge : network.getEdges()) {
      System.out.println();
      for (int i = 0; i < edge.getSchema().getColumnCount(); i++) {
        System.out.print(edge.get(i) + ", ");
      }
      System.out.println();
    }
  }

}
