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

package obviousx.io.rminer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.tools.Ontology;

import obvious.data.Table;
import obvious.data.util.IntIterator;
import obviousx.io.weka.ObviousWekaUtils;

/**
 * This class allows to load an Obvious Table into a RapidMiner ExampleTable.
 * Data structures are duplicated, so this approach is not memory oriented.
 * However, it allows to directly use data structures introduced in RapidMiner
 * that are designed to be efficient with RapidMiner's algorithms.
 * This class should be used when a developer wants to optimize execution
 * time.
 * @author Hemery
 *
 */
public class ObviousRMinerLoader {

  /**
   * Obvious table.
   */
  private Table table;
  
  /**
   * RapidMiner ExampleTable instance containing data to analyze.
   */
  private ExampleTable exampleTable;
  
  /**
   * Constructor.
   * @param inTable Obvious Table to load into a RapidMiner ExampleTable
   * @param inExampleTable ReadMiner ExampleTable instance to fill if null an
   * empty MemoryExampleTable will be used by default
   */
  public ObviousRMinerLoader(Table inTable, MemoryExampleTable inExampleTable) {
    this.table = inTable;
    if (inExampleTable != null) {
      this.exampleTable = inExampleTable;
    }
  }
  
  /**
   * Constructor.
   * @param inTable Obvious Table to load into a RapidMiner ExampleTable
   */
  public ObviousRMinerLoader(Table inTable) {
    this(inTable, null);
  }
  
  /**
   * Fills the RMiner ExampleTable with the data from the Obvious Table.
   * @return
   */
  public ExampleSet loadRMinerTable() {
    if (this.exampleTable != null) {
      for (Attribute att : loadAttributes()) {
        this.exampleTable.addAttribute(att);
      }
    } else {
      this.exampleTable = new MemoryExampleTable(loadAttributes());
    }
    return this.exampleTable.createExampleSet();
  }
  
  /**
   * Returns the associated ExampleTable for the given Obvious Table.
   * @return the associated ExampleTable for the given Obvious Table
   */
  public ExampleTable getExampleTable() {
    if (exampleTable == null) {
      loadRMinerTable();
    }
    return this.exampleTable;
  }
  
  /**
   * Converts an Obvious schema into a list of RapidMiner attributes. 
   * @return a list of RapidMiner attributes
   */
  protected List<Attribute> loadAttributes() {
    List<Attribute> attributes = new LinkedList<Attribute>();
    for (int i = 0; i < table.getSchema().getColumnCount(); i++) {
      if (ObviousWekaUtils.isNumeric(table.getSchema().getColumnType(i))) {
        attributes.add(AttributeFactory.createAttribute(
            table.getSchema().getColumnName(i), Ontology.REAL));
      } else if (ObviousWekaUtils.isNominal(
          table.getSchema().getColumnType(i))) {
        attributes.add(AttributeFactory.createAttribute(
            table.getSchema().getColumnName(i), Ontology.NOMINAL));
      } else if (ObviousWekaUtils.isDate(table.getSchema().getColumnType(i))) {
        attributes.add(AttributeFactory.createAttribute(
            table.getSchema().getColumnName(i), Ontology.DATE));
      }
    }
    return attributes;
  }
  
  /**
   * Loads the data (tuples) contained by
   */
  protected void loadData() {
    IntIterator it = table.rowIterator();
    while (it.hasNext()) {
      int id = it.nextInt();
      double[] data = new double[exampleTable.getAttributeCount()];
      for (int i = 0; i < exampleTable.getAttributeCount(); i++) {
        if(exampleTable.getAttribute(i).getValueType() == Ontology.REAL) {
          data[i] = Double.valueOf(
              table.getValue(id, exampleTable.getAttribute(i).getName()).toString());
        } else if (exampleTable.getAttribute(i).getValueType() ==
          Ontology.NOMINAL) {
          data[i] = exampleTable.getAttribute(i).getMapping().mapString(
              (String) table.getValue(
                  id, exampleTable.getAttribute(i).getName()));
        } else if (exampleTable.getAttribute(i).getValueType() ==
          Ontology.DATE) {
          data[i] = Double.valueOf(((Date) table.getValue(id,
              exampleTable.getAttribute(i).getName())).getTime());
        }
        ((MemoryExampleTable) this.exampleTable).addDataRow(
            new DoubleArrayDataRow(data));
      }
    }
  }
  

}
