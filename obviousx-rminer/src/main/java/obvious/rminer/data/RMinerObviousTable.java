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

package obvious.rminer.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.MemoryExampleTable;

import obvious.ObviousException;
import obvious.data.Schema;
import obvious.data.Table;
import obvious.data.Tuple;
import obvious.data.event.TableListener;
import obvious.data.util.IntIterator;
import obvious.data.util.Predicate;
import obviousx.io.weka.ObviousWekaUtils;

public class RMinerObviousTable implements Table {
    
    /**
     * Wrapped RapidMiner ExampleTable instance.
     */
    private ExampleTable rminerTable;
    
    /**
     * Associated example set of the wrapped ExampleTable instance.
     */
    private ExampleSet rminerExSet;

    /**
     * Obvious schema.
     */
    private Schema schema;
    
    /**
     * Listeners collection.
     */
    private ArrayList<TableListener> listeners;
    
    /**
     * Is the schema being edited.
     */
    private boolean editing = false;
    
    /**
     * Constructor.
     * @param inRminerTable RapidMiner wrapped ExampleTable instance.
     */
    public RMinerObviousTable(ExampleTable inRminerTable) {
        this.rminerTable = inRminerTable;
        this.rminerExSet = this.rminerTable.createExampleSet();
    }
    
    @Override
    public int addRow() {
      if (canAddRow()) {
        MemoryExampleTable memTable = (MemoryExampleTable) rminerTable;
        double[] data = new double[schema.getColumnCount()];
        for (int i = 0; i < this.getSchema().getColumnCount(); i++) {
          Class<?> type = this.getSchema().getColumnType(i);
          if (this.getSchema().getColumnDefault(i).equals(null)) {
            data[i] = 0;
          } else if (ObviousWekaUtils.isNumeric(type)) {
            data[i] = Double.valueOf(
                this.getSchema().getColumnDefault(i).toString());
          } else if (ObviousWekaUtils.isNominal(type) ||
              ObviousWekaUtils.isString(type)) {
            data[i] = this.rminerTable.getAttribute(i).getMapping().mapString(
                this.getSchema().getColumnDefault(i).toString());
          } else if (ObviousWekaUtils.isDate(type)){
            data[i] = Double.valueOf((
                (Date) this.getSchema().getColumnDefault(i)).getTime());
          }
        }
        memTable.addDataRow(new DoubleArrayDataRow(data));
      }
      return this.getRowCount();
    }

    @Override
    public int addRow(Tuple tuple) {
       if (canAddRow()) {
         MemoryExampleTable memTable = (MemoryExampleTable) rminerTable;
         double[] data = new double[schema.getColumnCount()];
         for (int i = 0; i < this.getSchema().getColumnCount(); i++) {
           Class<?> type = this.getSchema().getColumnType(i);
           if (this.getSchema().getColumnDefault(i).equals(null)) {
             data[i] = 0;
           } else if (ObviousWekaUtils.isNumeric(type)) {
             data[i] = Double.valueOf(
                 tuple.get(i).toString());
           } else if (ObviousWekaUtils.isNominal(type) ||
               ObviousWekaUtils.isString(type)) {
             data[i] = this.rminerTable.getAttribute(i).getMapping()
               .mapString(tuple.get(i).toString());
           } else if (ObviousWekaUtils.isDate(type)){
             data[i] = Double.valueOf(((Date) tuple.get(i)).getTime());
           }
         }
         memTable.addDataRow(new DoubleArrayDataRow(data));
       }
       return this.getRowCount();
    }

    @Override
    public void addTableListener(TableListener listnr) {
        listeners.add(listnr);
    }

    @Override
    public void beginEdit(int col) throws ObviousException {
        this.editing = true;
        for (TableListener listnr : this.getTableListeners()) {
          listnr.beginEdit(col);
        }
    }

    @Override
    public boolean canAddRow() {
        if (this.rminerTable instanceof MemoryExampleTable) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canRemoveRow() {
        return false;
    }

    @Override
    public boolean endEdit(int col) throws ObviousException {
        this.editing = false;
        for (TableListener listnr : this.getTableListeners()) {
          listnr.endEdit(col);
        }
        return true;
    }

    @Override
    public void fireTableEvent(int start, int end, int col, int type) {
        if (listeners.isEmpty()) {
            return;
        }
        for (TableListener listnr : listeners) {
            listnr.tableChanged(this, start, end, col, type);
        }
    }

    @Override
    public int getRowCount() {
        return this.rminerTable.size();
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public Collection<TableListener> getTableListeners() {
        return this.listeners;
    }

    @Override
    public Object getValue(int rowId, String field) {
        return getValue(rowId, getSchema().getColumnIndex(field));
    }

    @Override
    public Object getValue(int rowId, int col) {
        if (!isValueValid(rowId, col)) {
            return null;
        }
        Attribute att = this.rminerTable.getAttribute(col);
        Class<?> c = getSchema().getColumnType(col);
        double value = this.rminerTable.getDataRow(rowId).get(att);
        if (ObviousWekaUtils.isNumeric(c)) {
            return value;
        } else if (ObviousWekaUtils.isNominal(c)
            || ObviousWekaUtils.isString(c)) {
          return this.rminerExSet.getExample(rowId).getValueAsString(
              this.rminerTable.getAttribute(col)); 
        } else if (ObviousWekaUtils.isDate(c)) {
          return new Date((long) this.rminerExSet.getExample(rowId)
              .getValue(this.rminerTable.getAttribute(col)));
        }
        return null;
    }

    @Override
    public boolean isEditing(int col) {
        return this.editing;
    }

    @Override
    public boolean isValidRow(int rowId) {
        return !this.rminerTable.getDataRow(rowId).equals(null);
    }

    @Override
    public boolean isValueValid(int rowId, int col) {
        if (col >= this.rminerTable.getAttributeCount()) {
            return false;
        }
        if (!isValidRow(rowId)) {
            return false;
        }
        return true;
    }

    @Override
    public void removeAllRows() {
        return;
    }

    @Override
    public boolean removeRow(int row) {
        return false;
    }

    @Override
    public void removeTableListener(TableListener listnr) {
        listeners.remove(listnr);
        
    }

    @Override
    public IntIterator rowIterator() {
        return new RMinerObviousIntIterator(rminerExSet);
    }

    @Override
    public IntIterator rowIterator(Predicate pred) {
      return new RMinerObviousIntIterator(rminerExSet);
    }

    @Override
    public void set(int rowId, String field, Object val) {
      set(rowId, this.getSchema().getColumnIndex(field), val);
    }

    @Override
    public void set(int rowId, int col, Object val) {
      double wrappedVal = 0;
      Class<?> c = this.getSchema().getColumnType(col);
      if (ObviousWekaUtils.isNumeric(c)) {
        wrappedVal = Double.valueOf(
            val.toString());
      } else if (ObviousWekaUtils.isNominal(c)
        || ObviousWekaUtils.isString(c)) {
        wrappedVal = this.rminerTable.getAttribute(col).getMapping()
          .mapString(val.toString());
      } else if (ObviousWekaUtils.isDate(c)) {
        wrappedVal = Double.valueOf(((Date) val).getTime());
      }
      this.rminerTable.getDataRow(rowId).set(
          this.rminerTable.getAttribute(col), wrappedVal);
    }

    @Override
    public Object getUnderlyingImpl(Class<?> type) {
        if (type.equals(ExampleTable.class)) {
            return this.rminerTable;
        }
        return null;
    }
    
    /**
     * An Obvious IntIterator based on rapidminer.
     * @author plhemery
     *
     */
    public class RMinerObviousIntIterator implements IntIterator {

      /**
       * Wrapped iterator.
       */
      private Iterator<Example> it;
      
      /**
       * Current index.
       */
      private int currentIndex = -1;
      
      /**
       * Constructor
       * @param set a rapidminer ExampleSet instance.
       */
      public RMinerObviousIntIterator(ExampleSet set) {
        this.it = set.iterator();
      }
      
      @Override
      public int nextInt() {
        if (hasNext()) {
          currentIndex++;
        }
        return currentIndex;
      }

      @Override
      public boolean hasNext() {
        return it.hasNext();
      }

      @Override
      public Integer next() {
        return nextInt();
      }

      @Override
      public void remove() {
        it.remove();
      }
      
    }

}
