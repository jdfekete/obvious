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

package obviousx.io;

import java.io.File;
import java.io.FileWriter;
import java.text.FieldPosition;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obviousx.ObviousxException;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

/**
 * GraphML Export class.
 * Allows to convert an Obvious Network into GraphML.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class GraphMLExport implements Exporter {

  /**
   * Name to give to the GraphML file.
   */
  private String name;

  /**
   * Network to export in GraphML.
   */
  private Network network;

  /**
   * Node schema for the graph.
   */
  private Schema nodeSchema;

  /**
   * Edge schema for the graph.
   */
  private Schema edgeSchema;

  /**
   * Linked column name in schema to XML id.
   */
  private Map<String, String> colNameToId = new HashMap<String, String>();

  /**
   * Linked node to their XML id.
   */
  private Map<Node, String> nodeToId = new HashMap<Node, String>();

  /**
   * Format Factory to create string from object.
   */
  private FormatFactory formatFactory;

  /**
   * GraphML file to create.
   */
  private File file;

  /**
   * Filewriter.
   */
  private FileWriter writer;

  /**
   * XML serializer.
   */
  private XmlSerializer serializer;

  /**
   * GraphMLExport constructor.
   * @param fileName name for the GraphML file
   * @param inNetwork network to convert in GraphML
   * @param inNodeSchema node schema of the network
   * @param inEdgeSchema edge schema of the network
   * @throws ObviousxException when exporter creation failed
   */
  public GraphMLExport(String fileName, Network inNetwork,
      Schema inNodeSchema, Schema inEdgeSchema)
      throws ObviousxException {
    try {
      this.name = fileName;
      this.network = inNetwork;
      this.nodeSchema = inNodeSchema;
      this.edgeSchema = inEdgeSchema;
      this.formatFactory = new FormatFactoryImpl();
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      this.serializer = factory.newSerializer();
      this.file = new File(name);
      this.writer = new FileWriter(file);
      this.serializer.setOutput(writer);
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Create a GraphML file from the obvious network.
   * @throws ObviousxException when file creation failed
   */
  public void createFile() throws ObviousxException {
    try {
      serializer.startDocument("UTF-8", null);
      createXmlSchemDecl();
      createGraphSchema();
      createGraph();
      serializer.flush();
      writer.close();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Gets the FormatFactory of the importer.
   * @return the FormatFactory attribute
   */
  public FormatFactory getFormatFactory() {
    return this.formatFactory;
  }

  /**
   * Sets the FormatFactory of the importer.
   * @param inputFormatFactory the factory to set
   */
  public void setFormatFactory(FormatFactory inputFormatFactory) {
    this.formatFactory = inputFormatFactory;
  }

  /**
   * Puts in the serializer Graphml XML schema declaration.
   * @throws ObviousxException when writing failed
   */
  private void createXmlSchemDecl() throws ObviousxException {
    try {
      writer.append('\n');
      serializer.startTag(null, "graphml");
      serializer.attribute(null, "xmlns",
          "http://graphml.graphdrawing.org/xmlns");
      writer.append('\n');
      writer.append('\t');
      serializer.attribute(null, "xmlns:xsi",
          "http://www.w3.org/2001/XMLSchema-instance");
      writer.append('\n');
      writer.append('\t');
      serializer.attribute(null, "xsi:schemaLocation",
          "http://graphml.graphdrawing.org/xmlns "
          + "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
      serializer.flush();
      writer.append('\t');
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Puts in the serializer the node and edge schemas.
   * @throws ObviousxException when writing failed
   */
  private void createGraphSchema() throws ObviousxException {
    try {
      createSchema(nodeSchema, "node");
      createSchema(edgeSchema, "edge");
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Puts the graph description in GraphML in the serializer.
   * @throws ObviousxException when writing failed
   */
  private void createGraph() throws ObviousxException {
    try {
      writer.append("\n\t");
      serializer.startTag(null, "graph");
      serializer.attribute(null, "id", "graph0");
      serializer.attribute(null, "edgedefault", "undirected");
      serializer.flush();
      createNode();
      createEdge();
      writer.append("\n\t");
      serializer.endTag(null, "graph");
      writer.append("\n");
      serializer.endTag(null, "graphml");
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Puts a schema in the serializer.
   * @param schema schema to convert in GraphML
   * @param type "type" of the schema : "node" or "edge"
   * @throws ObviousxException when writing failed
   */
  private void createSchema(Schema schema, String type)
      throws ObviousxException {
    try {
      for (int i = 0; i < schema.getColumnCount(); i++) {
        writer.append("\n\t");
        serializer.startTag(null, "key");
        serializer.attribute(null, "id", "attr" + type + i);
        serializer.attribute(null, "for", type);
        serializer.attribute(null, "attr.name", schema.getColumnName(i));
        serializer.attribute(null, "attr.type",
            schema.getColumnType(i).getSimpleName());
        colNameToId.put(schema.getColumnName(i), "attr" + type + i);
        serializer.flush();
        if (schema.getColumnDefault(i) != null) {
          writer.append("\n\t\t");
          serializer.startTag(null, "default");
          TypedFormat format = formatFactory.getFormat(
              schema.getColumnType(i).getSimpleName());
          StringBuffer value = format.format(schema.getColumnDefault(i),
              new StringBuffer(), new FieldPosition(0));
          serializer.text(value.toString());
          serializer.endTag(null, "default");
          writer.append("\n\t");
        }
        serializer.endTag(null, "key");
      }
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Puts nodes in the serializer.
   * @throws ObviousxException when writing failed
   */
  private void createNode() throws ObviousxException {
    try {
      int nodeCount = 0;
      for (Node node : network.getNodes()) {
        writer.append("\n\t\t");
        serializer.startTag(null, "node");
        serializer.attribute(null, "id", "node" + nodeCount);
        nodeToId.put(node, "node" + nodeCount);
        serializer.flush();
        for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
          writer.append("\n\t\t\t");
          serializer.startTag(null, "data");
          serializer.attribute(null, "key", colNameToId.get(
              nodeSchema.getColumnName(i)));
          TypedFormat format = formatFactory.getFormat(
              nodeSchema.getColumnType(i).getSimpleName());
          StringBuffer value = format.format(node.get(i),
              new StringBuffer(), new FieldPosition(0));
          serializer.text(value.toString());
          serializer.endTag(null, "data");
        }
        writer.append("\n\t\t");
        serializer.endTag(null, "node");
        nodeCount++;
      }
      writer.append("\n\t");
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Puts edges in the serializer.
   * @throws ObviousxException when writing failed
   */
  private void createEdge() throws ObviousxException {
    try {
      int edgeCount = 0;
      for (Edge edge : network.getEdges()) {
        writer.append("\n\t\t");
        serializer.startTag(null, "edge");
        serializer.attribute(null, "id", "edge" + edgeCount);
        Node source = null, target = null;
        if (network.getEdgeType(edge).equals(Graph.EdgeType.DIRECTED)) {
          source = network.getSource(edge);
          target = network.getTarget(edge);
        } else {
          if (network.getIncidentNodes(edge).size() > 2) {
            throw new ObviousxException("Hyperedge aren't supported"
                + "by the exporter!");
          } else {
            Iterator<Node> it = network.getIncidentNodes(edge).iterator();
            source = it.next();
            target = it.next();
          }
          serializer.attribute(null, "source", nodeToId.get(source));
          serializer.attribute(null, "target", nodeToId.get(target));
        }
        serializer.flush();
        for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
          writer.append("\n\t\t\t");
          serializer.startTag(null, "data");
          serializer.attribute(null, "key", colNameToId.get(
              edgeSchema.getColumnName(i)));
          TypedFormat format = formatFactory.getFormat(
              edgeSchema.getColumnType(i).getSimpleName());
          StringBuffer value = format.format(edge.get(i),
              new StringBuffer(), new FieldPosition(0));
          serializer.text(value.toString());
          serializer.endTag(null, "data");
        }
        writer.append("\n\t\t");
        serializer.endTag(null, "edge");
        edgeCount++;
      }
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }
}
