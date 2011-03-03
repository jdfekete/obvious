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

import java.util.HashMap;
import java.util.Map;

import obvious.ObviousException;
import obvious.data.DataFactory;
import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.DataFactoryImpl;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.SchemaImpl;
import obviousx.ObviousxException;
import obviousx.io.GraphMLExport;

/**
 * Example class for GraphMLExport.
 * @author Pierre-Luc Hemery
 *
 */
public final class GraphMLExportExample {

  /**
   * Number of nodes.
   */
  public static final int NODENUMBER = 6;

  /**
   * Number of nodes.
   */
  public static final int EDGENUMBER = 6;

  /**
   * Constructor.
   */
  private GraphMLExportExample() {
  }

  /**
   * Main.
   * @param args arguments for the main.
   * @throws ObviousException when Network creation failed
   * @throws ObviousxException when export failed
   */
  public static void main(String[] args) throws ObviousException,
      ObviousxException {

    // Preparing file creation
    String filePath = "";
    String factoryPath = "";

    try {
      filePath = args[0];
      factoryPath = args[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      filePath = "C:\\outputObvious\\DefaultTest.graphml";
      factoryPath = "obvious.prefuse.PrefuseDataFactory";
    }

    // Preparing network to export in GraphML
    Schema nodeSchema = new SchemaImpl();
    nodeSchema.addColumn("nodeId", int.class, 0);
    nodeSchema.addColumn("color", String.class, "yellow");

    Schema edgeSchema = new SchemaImpl();
    edgeSchema.addColumn("sourceNode", int.class, 0);
    edgeSchema.addColumn("targetNode", int.class, 0);

    System.setProperty("obvious.DataFactory", factoryPath);
    DataFactory dFactory = DataFactoryImpl.getInstance();

    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("sourceKey", "sourceNode");
    paramMap.put("targetKey", "targetNode");
    paramMap.put("nodeKey", "nodeId");
    Network network = dFactory.createGraph("net", nodeSchema,
        edgeSchema, paramMap);

    // Creating and adding nodes to the network
    String[] color = {"blue", "red", "green", "indigo", "pink", "grey"};

    for (int i = 0; i < NODENUMBER; i++) {
      Object[] values = {i, color[i]};
      Node node = new NodeImpl(network.getNodeTable().getSchema(), values);
      network.addNode(node);
    }

    // Creating and adding edges to the network
    Object[][] edgeValue = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {3, 4}, {4, 5}};

    for (int i = 0; i < EDGENUMBER; i++) {
      Edge edge = new EdgeImpl(network.getEdgeTable().getSchema(),
          edgeValue[i]);
      Node sourceNode = null, targetNode = null;
      for (Node node : network.getNodes()) {
        if (node.get("nodeId").equals(edgeValue[i][0])) {
          sourceNode = node;
        }
        if (node.get("nodeId").equals(edgeValue[i][1])) {
          targetNode = node;
        }
      }
      network.addEdge(edge, sourceNode, targetNode, Graph.EdgeType.UNDIRECTED);
    }

    // Creating exporter
    GraphMLExport exporter = new GraphMLExport(filePath, network,
        nodeSchema, edgeSchema);
    exporter.createFile();
  }
}
