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
import java.io.FileReader;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.SchemaImpl;
import obvious.impl.NodeImpl;
import obviousx.ObviousxException;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * GraphML Import class.
 * Allows to convert a GraphML simple graph as an Obvious Network.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class GraphMLImport implements GraphImporter {

  /**
   * Input file.
   */
  private File file;

  /**
   * Schema for nodes of graph.
   */
  private Schema nodeSchema;

  /**
   * Schema for edges of graph.
   */
  private Schema edgeSchema;

  /**
   * Input network, that will be loaded with content of the external medium.
   */
  private Network network;

  /**
   * FormatFactory of this Importer.
   */
  private FormatFactory formatFactory;

  /**
   * XML parser to process GraphML description.
   */
  private XmlPullParser xpp;

  /**
   * Map linking XML id to columns name.
   */
  private Map<String, String> idToName;

  /**
   * Encountered edges.
   */
  private Collection<Edge> edges = new ArrayList<Edge>();

  /**
   * Map linking edges to their edge type.
   */
  private Map<Edge, Graph.EdgeType> edgeType =
    new HashMap<Edge, Graph.EdgeType>();

  /**
   * Default edge type for the graph.
   */
  private Graph.EdgeType defaultEdgeType = Graph.EdgeType.UNDIRECTED;

  /**
   * Column used to spot source node in edgeSchema.
   */
  private String sourceCol;

  /**
   * Column used to spot target node in edgeSchema.
   */
  private String targetCol;

  /**
   * Node id column name for nodeSchema.
   */
  private String nodeId;

  /**
   * Is the schema loaded?
   */
  private boolean schemaLoaded = false;

  /**
   * Constructor for GraphMLImport.
   * @param inputFile external file to load
   * @param inputNetwork network to fill with the content of the file
   * @param source sourceNode column name in edgeSchema
   * @param target targetNode column name in edgeSchema
   * @param inNodeId node id column name in nodeSchema
   * @throws ObviousxException if failed to create XML Pull parser
   */
  public GraphMLImport(File inputFile, Network inputNetwork, String source,
      String target, String inNodeId) throws ObviousxException {
    try {
      this.file = inputFile;
      this.nodeSchema = new SchemaImpl();
      this.edgeSchema = new SchemaImpl();
      this.network = inputNetwork;
      this.formatFactory = new FormatFactoryImpl();
      this.sourceCol = source;
      this.targetCol = target;
      this.nodeId = inNodeId;
      this.idToName = new HashMap<String, String>();
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      this.xpp = factory.newPullParser();
      this.xpp.setInput(new FileReader(file));
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Returns node schema.
   * @return node schema
   */
  public Schema getNodeSchema() {
    return nodeSchema;
  }

  /**
   * Gets the FormatFactory associated to this Importer.
   * @return the FormatFactory of this Importer
   */
  public FormatFactory getFormatFactory() {
    return this.formatFactory;
  }

  /**
   * Loads the table with the data of the external file.
   * @return an obvious network.
   * @throws ObviousxException when exception occurs
   */
  public Network loadGraph() throws ObviousxException {
    try {
      if (!schemaLoaded) {
        readSchema();
      }
      do {
        if (xpp.getEventType() == XmlPullParser.START_TAG
            && xpp.getName().equals("graph")) {
          processGraphElement();
        } else {
          xpp.next();
        }
      } while (xpp.getEventType() != XmlPullParser.END_DOCUMENT);
      for (Edge edge : edges) {
        Node source = getNode(edge.get(sourceCol));
        Node target = getNode(edge.get(targetCol));
        network.addEdge(edge, source, target, edgeType.get(edge));
      }
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
    return this.network;
  }

  /**
   * Reads the schema for nodes and edges described the GraphML file.
   * @throws ObviousxException if an exception occurs.
   */
  public void readSchema() throws ObviousxException {
    try {
      int eventType = xpp.getEventType();
      do {
        if (eventType == XmlPullParser.START_DOCUMENT) {
          eventType = xpp.nextTag();
        } else if (eventType == XmlPullParser.START_TAG
            && xpp.getName().equals("key")) {
          processKeyElement();
        } else if (eventType == XmlPullParser.END_TAG) {
          eventType = xpp.nextTag();
        } else {
          eventType = xpp.nextTag();
        }
      } while (!xpp.getName().equals("graph"));
      schemaLoaded = true;
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Sets the FormatFactory of the importer.
   * @param inputFormatFactory the factory to set
   */
  public void setFormatFactory(FormatFactory inputFormatFactory) {
    this.formatFactory = inputFormatFactory;
  }

  /**
   * Process a "key" element in GraphML file.
   * It will load the metadata (e.g. name, type, default value) in the
   * corresponding schema.
   * @throws ObviousxException when parsing failed
   */
  private void processKeyElement() throws ObviousxException {
    try {
      String id = "N/A";
      String columnName = "N/A";
      String type = "N/A";
      String  destinationSchema = "N/A";
      String defaultValue = "";
      // Harvesting columnName, id, destination schema and type.
      for (int i = 0; i < xpp.getAttributeCount(); i++) {
        if (xpp.getAttributeName(i).equals("id")) {
         id = xpp.getAttributeValue(i);
        } else if (xpp.getAttributeName(i).equals("attr.name")) {
          columnName = xpp.getAttributeValue(i);
        } else if (xpp.getAttributeName(i).equals("attr.type")) {
          type = xpp.getAttributeValue(i);
        } else if (xpp.getAttributeName(i).equals("for")) {
          destinationSchema = xpp.getAttributeValue(i);
        }
      }
      // Harvesting default value.
      if (!xpp.isEmptyElementTag()) {
        int eventType = xpp.nextTag();
        if (eventType == XmlPullParser.START_TAG
            && xpp.getName().equals("default")) {
          defaultValue = xpp.nextText();
        }
      }
      // Creating new entry in the corresponding schema.
      Schema currentSchema;
      if (destinationSchema.equals("node")) {
        currentSchema = nodeSchema;
      } else {
        currentSchema = edgeSchema;
      }
      TypedFormat format = formatFactory.getFormat(type);
      Object value = format.parseObject(defaultValue, new ParsePosition(0));
      currentSchema.addColumn(columnName, format.getFormattedClass(), value);
      idToName.put(id, columnName);
      xpp.nextTag();
      xpp.nextTag();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Process a "graph" element in the graph.
   * @throws ObviousxException when parsing failed
   */
  private void processGraphElement() throws ObviousxException {
    try {
      for (int i = 0; i < xpp.getAttributeCount(); i++) {
        if (xpp.getAttributeName(i).equals("edgedefault")) {
          if (xpp.getAttributeValue(i).equals("directed")) {
            defaultEdgeType = Graph.EdgeType.DIRECTED;
          }
        }
      }
      xpp.next();
      do {
        if (xpp.getEventType() == XmlPullParser.START_TAG
            && xpp.getName().equals("node")) {
          processNodeElement();
        } else if (xpp.getEventType() == XmlPullParser.START_TAG
            && xpp.getName().equals("edge")) {
          processEdgeElement();
        } else {
          xpp.next();
        }
      } while (xpp.getEventType() != XmlPullParser.END_DOCUMENT);
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Process a "node" element in the graph.
   * @throws ObviousxException when parsing failed
   */
  private void processNodeElement() throws ObviousxException {
    try {
      Object[] nodeAttr = new Object[nodeSchema.getColumnCount()];
      for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
        nodeAttr[i] = nodeSchema.getColumnDefault(i);
      }
      int eventType = xpp.next();
      do {
        if (eventType == XmlPullParser.START_TAG
            && xpp.getName().equals("data")) {
          for (int i = 0; i < xpp.getAttributeCount(); i++) {
            if (xpp.getAttributeName(i).equals("key")) {
              String colName = idToName.get(xpp.getAttributeValue(i));
              String stringValue = xpp.nextText();
              TypedFormat format = formatFactory.getFormat(
                  nodeSchema.getColumnType(colName).getSimpleName());
              Object value = format.parseObject(stringValue,
                  new ParsePosition(0));
              nodeAttr[nodeSchema.getColumnIndex(colName)] = value;
              break;
            }
          }
          eventType = xpp.next();
        } else {
          eventType = xpp.next();
        }
      } while (xpp.getName() == null || !xpp.getName().equals("node"));
      Node node = (Node) new NodeImpl(network.getNodeTable().getSchema(),
          nodeAttr);
      network.addNode(node);
      xpp.nextTag();
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Process an "edge" element of the graph.
   * @throws ObviousxException when parsing failed
   */
  private void processEdgeElement() throws ObviousxException {
    try {
      // Determine the correct edge type.
      Graph.EdgeType currentEdgeType = defaultEdgeType;
      for (int i = 0; i < xpp.getAttributeCount(); i++) {
        if (xpp.getAttributeName(i).equals("directed")) {
          if (xpp.getAttributeValue(i).equals("true")) {
            currentEdgeType = Graph.EdgeType.DIRECTED;
          } else {
            currentEdgeType = Graph.EdgeType.UNDIRECTED;
          }
        }
      }
      // Load default values.
      Object[] edgeAttr = new Object[edgeSchema.getColumnCount()];
      for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
        edgeAttr[i] = edgeSchema.getColumnDefault(i);
      }
      // Load attribute values contained in the graphml file.
      int eventType = xpp.next();
      do {
        if (eventType == XmlPullParser.START_TAG
            && xpp.getName().equals("data")) {
          for (int i = 0; i < xpp.getAttributeCount(); i++) {
            if (xpp.getAttributeName(i).equals("key")) {
              String colName = idToName.get(xpp.getAttributeValue(i));
              String stringValue = xpp.nextText();
              TypedFormat format = formatFactory.getFormat(
                  edgeSchema.getColumnType(colName).getSimpleName());
              Object value = format.parseObject(stringValue,
                  new ParsePosition(0));
              edgeAttr[edgeSchema.getColumnIndex(colName)] = value;
              break;
            }
          }
          eventType = xpp.next();
        } else {
          eventType = xpp.next();
        }
      } while (xpp.getName() == null || !xpp.getName().equals("edge"));
      // Update the list of edges and edge typ map.
      // Edge addition to the network are done at the end, when it is
      // sure that all nodes have been added to the graph.
      Edge currentEdge = (Edge) new EdgeImpl(network.getEdgeTable().getSchema()
          , edgeAttr);
      edges.add(currentEdge);
      edgeType.put(currentEdge, currentEdgeType);
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Gets a node in the network by its id.
   * @param id id of the node
   * @return searched node
   */
  private Node getNode(Object id) {
    Collection<Node> nodes = network.getNodes();
    for (Node node : nodes) {
      if (nodeId != null) {
        if (node.get(nodeId).toString().equals(id.toString())) {
          return node;
        }
      } else {
        if (Integer.toString(node.getRow()).equals(id.toString())) {
        return node;
        }
      }
    }
    return null;
  }

}

