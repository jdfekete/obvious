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

import com.rapidminer.example.Attribute;
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
    }
    
    @Override
    public int addRow() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addRow(Tuple tuple) {
        // TODO Auto-generated method stub
        return 0;
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
        } else if (ObviousWekaUtils.isNominal(c)) {
            // TODO
        } else if (ObviousWekaUtils.isDate(c)) {
         // TODO   
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IntIterator rowIterator(Predicate pred) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void set(int rowId, String field, Object val) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void set(int rowId, int col, Object val) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object getUnderlyingImpl(Class<?> type) {
        if (type.equals(ExampleTable.class)) {
            return this.rminerTable;
        }
        return null;
    }

}
