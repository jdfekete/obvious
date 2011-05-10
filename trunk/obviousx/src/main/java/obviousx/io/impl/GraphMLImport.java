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

package obviousx.io.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import obvious.data.DataFactory;
import obvious.data.Edge;
import obvious.data.Graph;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.impl.EdgeImpl;
import obvious.impl.SchemaImpl;
import obvious.impl.NodeImpl;
import obviousx.ObviousxException;
import obviousx.io.GraphImporter;
import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

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
  private XMLEventReader xpp;

  /**
   * Map linking XML id to columns name.
   */
  private Map<String, String> idToName;

  /**
   * Map linking XML node id to obvious Node.
   */
  private Map<String, Node> idToNode = new HashMap<String, Node>();

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
   * Current event.
   */
  private XMLEvent event;

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
      XMLInputFactory factory = XMLInputFactory.newInstance();
      try {
        xpp = factory.createXMLEventReader(new FileReader(file));
      } catch (FileNotFoundException e) {
        System.out.println("File " + file + " is unknown!");
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      throw new ObviousxException(e);
    }
  }

  /**
   * Constructor.
   * @param inputFile external file to load
   */
  public GraphMLImport(File inputFile) {
    try {
      this.file = inputFile;
      this.formatFactory = new FormatFactoryImpl();
      this.idToName = new HashMap<String, String>();
      this.nodeSchema = new SchemaImpl();
      this.edgeSchema = new SchemaImpl();
      XMLInputFactory factory = XMLInputFactory.newInstance();
      try {
        xpp = factory.createXMLEventReader(new FileReader(file));
      } catch (FileNotFoundException e) {
        System.out.println("File " + file + " is unknown!");
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
      readSchema();
      this.sourceCol = "source";
      this.targetCol = "target";
      edgeSchema.addColumn(sourceCol, int.class, null);
      edgeSchema.addColumn(targetCol, int.class, null);
      this.network = DataFactory.getInstance().createGraph(
          nodeSchema, edgeSchema);
      this.nodeId = prefuse.data.Graph.DEFAULT_NODE_KEY;
    } catch (Exception e) {
      e.printStackTrace();
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
      event = xpp.nextEvent();
      do {
        if (event.isStartElement()) {
            if (event.asStartElement().getName().equals("graph")) {
              processGraphElement();
            }
        } else {
          event = xpp.nextEvent();
        }
      } while (event.isEndDocument());
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
      event = xpp.nextEvent();
      do {
        if (event.isStartDocument()) {
          event = xpp.nextEvent();
        } else if (event.isStartElement()) {
          if (event.asStartElement().getName().getLocalPart().equals("key")) {
            processKeyElement();
          }
        } else if (event.isEndElement()) {
          event = xpp.nextEvent();
        } else {
          event = xpp.nextEvent();
        }
      } while (!isGraphStarting(event));
      schemaLoaded = true;
    } catch (Exception e) {
      e.printStackTrace();
      //throw new ObviousxException(e);
    }
  }

  /**
   * Checks if the cursor is in the graph tag.
   * @param event current XML event
   * @return true if the cursor is in the graph tag
   */
  private boolean isGraphStarting(XMLEvent event) {
    if (event.isStartElement()) {
      if (event.asStartElement().getName().getLocalPart().equals("graph")) {
        return true;
      }
    }
    return false;
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
      Iterator attIter = event.asStartElement().getAttributes();
      while (attIter.hasNext()) {
        Attribute attribute = (Attribute) attIter.next();
        if (attribute.getName().getLocalPart().equals("id")) {
          id = attribute.getValue();
        } else if (attribute.getName().getLocalPart().equals("attr.name")) {
           columnName = attribute.getValue();
        } else if (attribute.getName().getLocalPart().equals("attr.type")) {
           type = attribute.getValue();
        } else if (attribute.getName().getLocalPart().equals("for")) {
           destinationSchema = attribute.getValue();
        }
      }
      // Harvesting default value.
      if (event.isStartElement() && event.asStartElement().getName()
          .getLocalPart().equals("default")) {
        defaultValue = xpp.getElementText();
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
      Class<?> c = format.getFormattedClass();
      if (c == Integer.class) {
        c = int.class;
      }
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
      Iterator attIter = event.asStartElement().getAttributes();
      while (attIter.hasNext()) {
        Attribute att = (Attribute) attIter.next();
        if (att.getName().getLocalPart().equals("edgedefault")) {
          if (att.getValue().equals("directed")) {
            defaultEdgeType = Graph.EdgeType.DIRECTED;
          }
        }
      }
      xpp.next();
      do {
        if (event.isStartElement()
            && event.asStartElement().getName().getLocalPart().equals("node")) {
          processNodeElement();
        } else if (event.isStartElement()
            && event.asStartElement().getName().getLocalPart().equals("edge")) {
          processEdgeElement();
        } else {
          xpp.next();
        }
      } while (event.isEndDocument());
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
      String id = "";
      Iterator attIter = event.asStartElement().getAttributes();
      while (attIter.hasNext()) {
        Attribute att = (Attribute) attIter.next();
        if (att.getName().getLocalPart().equals("id")) {
          id = att.getValue();
        }
      }
      for (int i = 0; i < nodeSchema.getColumnCount(); i++) {
        nodeAttr[i] = nodeSchema.getColumnDefault(i);
      }
      event = xpp.nextEvent();
      do {
        if (event.isStartElement()
            && event.asStartElement().getName().getLocalPart().equals("data")) {
          Iterator it = event.asStartElement().getAttributes();
          while (it.hasNext()) {
            Attribute att = (Attribute) it.next();
            if (att.getName().getLocalPart().equals("key")) {
              String colName = idToName.get(att.getValue());
              String stringValue = xpp.getElementText();
              TypedFormat format = formatFactory.getFormat(
                  nodeSchema.getColumnType(colName).getSimpleName());
              Object value = format.parseObject(stringValue,
                  new ParsePosition(0));
              nodeAttr[nodeSchema.getColumnIndex(colName)] = value;
              break;
            }
          }
          event = xpp.nextEvent();
        } else {
          event = xpp.nextEvent();
        }
      } while (event.asStartElement().getName() == null
          || !event.asStartElement().getName().getLocalPart().equals("node"));
      Node node = (Node) new NodeImpl(network.getNodeTable().getSchema(),
          nodeAttr);
      idToNode.put(id, node);
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
      String source = "", target = "";
      Graph.EdgeType currentEdgeType = defaultEdgeType;
      Iterator attIter = event.asStartElement().getAttributes();
      while (attIter.hasNext()) {
        Attribute att = (Attribute) attIter.next();
        if (att.getName().getLocalPart().equals("directed")) {
          if (att.getValue().equals("true")) {
            currentEdgeType = Graph.EdgeType.DIRECTED;
          } else {
            currentEdgeType = Graph.EdgeType.UNDIRECTED;
          }
        } else if (att.getName().getLocalPart().equals("source")) {
          att.getValue();
        } else if (att.getName().getLocalPart().equals("target")) {
          att.getValue();
        }
      }
      // Load default values.
      Object[] edgeAttr = new Object[edgeSchema.getColumnCount()];
      for (int i = 0; i < edgeSchema.getColumnCount(); i++) {
        if (edgeSchema.getColumnName(i).equals("source")) {
          edgeAttr[i] = source;
        } else if (edgeSchema.getColumnName(i).equals("target")) {
          edgeAttr[i] = target;
        } else {
          edgeAttr[i] = edgeSchema.getColumnDefault(i);
        }
      }
      // Load attribute values contained in the graphml file.
      event = xpp.nextEvent();
      do {
        if (event.isStartElement() && event.asStartElement().getName()
            .getLocalPart().equals("data")) {
          Iterator it = event.asStartElement().getAttributes();
          while (it.hasNext()) {
            Attribute att = (Attribute) it.next();
            if (att.getName().getLocalPart().equals("key")) {
              String colName = idToName.get(att.getValue());
              String stringValue = xpp.getElementText();
              TypedFormat format = formatFactory.getFormat(
                  edgeSchema.getColumnType(colName).getSimpleName());
              Object value = format.parseObject(stringValue,
                  new ParsePosition(0));
              edgeAttr[edgeSchema.getColumnIndex(colName)] = value;
              break;
            }
          }
          event = xpp.nextEvent();
        } else {
          event = xpp.nextEvent();
        }
      } while (event.asStartElement().getName() == null
          || !event.asStartElement().getName().getLocalPart().equals("edge"));
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
        return idToNode.get(id);
      }
    }
    return null;
  }

}

