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
import java.util.HashMap;
import java.util.Map;

import obvious.data.Data;
import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.EdgeImpl;
import obvious.impl.NodeImpl;
import obvious.impl.TableImpl;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

/**
 * CSV to graph Import class.
 * Allows to convert a graph described by two CSV files, one for nodes other
 * for edges, as an Obvious Network.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class CSVGraphImport implements GraphImporter {

  /**
   * CSV file describing nodes.
   */
  private File nodeFile;

  /**
   * CSV file describing edges.
   */
  private File edgeFile;

  /**
   * Input network, that will be loaded with content of the external medium.
   */
  private Network network;

  /**
   * Schema for nodes of graph.
   */
  private Schema nodeSchema;

  /**
   * Schema for edges of graph.
   */
  private Schema edgeSchema;

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
   * Linked node Id to node.
   */
  private Map<Object, Node > idToNode = new HashMap<Object, Node>();

  /**
   * Separator used in CSV.
   */
  private char separator;

  /**
   * FormatFactory of this Importer.
   */
  private FormatFactory formatFactory;

  /**
   * Constructor for GraphMLImport.
   * @param inNodeFile external CSV file describing nodes
   * @param inEdgeFile external CSV file describing edges
   * @param inNetwork network to fill with the content of the file
   * @param source sourceNode column name in edgeSchema
   * @param target targetNode column name in edgeSchema
   * @param inNodeId node id column name in nodeSchema
   * @param sep separator used in CSV
   */
  public CSVGraphImport(File inNodeFile, File inEdgeFile, Network inNetwork,
      String source, String target, String inNodeId, char sep) {
    this.nodeFile = inNodeFile;
    this.edgeFile = inEdgeFile;
    this.network = inNetwork;
    this.sourceCol = source;
    this.targetCol = target;
    this.nodeId = inNodeId;
    this.separator = sep;
    this.formatFactory = new FormatFactoryImpl();
  }

  /**
   * Gets the FormatFactory associated to this Importer.
   * @return the FormatFactory of this Importer
   */
  public FormatFactory getFormatFactory() {
    return this.formatFactory;
  }

  /**
   * Loads the table with the data of external CSV files.
   * @throws ObviousxException when exception occurs
   */
  public void loadTable() throws ObviousxException {
    CSVImport nodeImport = new CSVImport(nodeFile, new TableImpl(nodeSchema),
        separator);
    CSVImport edgeImport = new CSVImport(edgeFile, new TableImpl(edgeSchema),
        separator);
    nodeImport.loadTable();
    edgeImport.loadTable();
    Table nodeTable = nodeImport.getTable();
    Table edgeTable = edgeImport.getTable();
    for (int i = 0; i < nodeTable.getRowCount(); i++) {
      Node node = new NodeImpl(nodeTable, i);
      network.addNode(node);
      if (nodeId == null) {
        idToNode.put(node.getRow(), node);
      } else {
        idToNode.put(node.get(nodeId), node);
      }
    }
    for (int i = 0; i < edgeTable.getRowCount(); i++) {
      Edge edge = new EdgeImpl(edgeTable, i);
      network.addEdge(edge, idToNode.get(edge.get(sourceCol)),
          idToNode.get(edge.get(targetCol)),
          network.getEdgeType(edge));
    }
  }

  /**
   * Reads the schema for nodes and edges described CSV files.
   * @throws ObviousxException when reading fails
   */
  public void readSchema() throws ObviousxException {
    CSVImport nodeImport = new CSVImport(nodeFile, null, separator);
    nodeImport.readSchema();
    nodeSchema = nodeImport.getSchema();
    CSVImport edgeImport = new CSVImport(edgeFile, null, separator);
    edgeImport.readSchema();
    edgeSchema = edgeImport.getSchema();
  }

  /**
   * Sets the FormatFactory of the importer.
   * @param inputFormatFactory the factory to set
   */
  public void setFormatFactory(FormatFactory inputFormatFactory) {
    this.formatFactory = inputFormatFactory;
  }

  /**
   * Returns the imported obvious Data instance.
   * @return the imported obvious Data instance
   */
  public Data getData() {
    return this.network;
  }

}
