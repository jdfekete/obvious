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


import obvious.data.Edge;
import obvious.data.Network;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.impl.TableImpl;
import obviousx.ObviousxException;
import obviousx.io.Exporter;
import obviousx.util.FormatFactory;
import obviousx.util.FormatFactoryImpl;

/**
 * CSV to graph Import class.
 * Allows to convert an obvious Network instance into two CSV files one for
 * nodes the other for edges.
 *
 * @author Pierre-Luc Hemery
 *
 */
public class CSVGraphExport implements Exporter {

  /**
   * Name given to the CSV node file.
   */
  private String nodeName;

  /**
   * Name given to the CSV edge file.
   */
  private String edgeName;

  /**
   * Network to export in CSV.
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
   * FormatFactory of this Importer.
   */
  private FormatFactory formatFactory;

  /**
   * Constructor for exporter for Obvious Network to CSV.
   * @param node name of the node CSV file
   * @param edge name of the edge CSV file
   * @param inNetwork network to convert in CSV
   * @param inNodeSchema node schema of the network
   * @param inEdgeSchema edge schema of the network
   */
  public CSVGraphExport(String node, String edge, Network inNetwork,
      Schema inNodeSchema, Schema inEdgeSchema) {
    this.nodeName = node;
    this.edgeName = edge;
    this.network = inNetwork;
    this.nodeSchema = inNodeSchema;
    this.edgeSchema = inEdgeSchema;
    this.formatFactory = new FormatFactoryImpl();
  }

  /**
   * Create a node CSV file and an edge CSV file from the obvious network.
   * @throws ObviousxException when file creation failed
   */
  public void createFile() throws ObviousxException {
    Table nodeTable = new TableImpl(nodeSchema);
    Table edgeTable = new TableImpl(edgeSchema);
    for (Node node : network.getNodes()) {
      nodeTable.addRow(node);
    }
    for (Edge edge : network.getEdges()) {
      edgeTable.addRow(edge);
    }
    CSVExport nodeExport = new CSVExport(nodeName, nodeTable);
    CSVExport edgeExport = new CSVExport(edgeName, edgeTable);

    nodeExport.createFile();
    edgeExport.createFile();
  }

  /**
   * Gets the FormatFactory associated to this Importer.
   * @return the FormatFactory of this Importer
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

}
