/*****************************************************************************
 * Copyright (C) 2008 Jean-Daniel Fekete and INRIA, France                  *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license.txt file.                                                         *
 *****************************************************************************/
package obvious;

import java.util.Collection;


/**
 * Table have columns and rowIds.
 * 
 */
public interface Table {
    static final Object MISSING_VALUE = new Object();//FIXME make it serializabe
    /**
     * Returns this Table's schema. 
     * @return a copy of this Table's schema
     */
    public Schema getSchema();
    
    /**
     * Get the number of rows in the table.
     * @return the number of rows
     */
    public int getRowCount();

    /**
     * Get an interator over the row numbers of this table.
     * @return an iterator over the rows of this table
     */
    public IntIterator rowIterator();
    
    /**
     * Indicates if the given row number corresponds to a valid table row.
     * @param rowId the row number to check for validity
     * @return true if the row is valid, false if it is not
     */
    public boolean isValidRow(int rowId);
//    /**
//     * Get the minimum row index currently in use by this Table.
//     * 
//     * @return the minimum row index
//     */
//    public int getMinimumRow();
//    
//    /**
//     * Get the maximum row index currently in use by this Table.
//     * @return the maximum row index
//     */
//    public int getMaximumRow();
    
//    /**
//     * Get the Tuple instance providing object-oriented access to the given
//     * table row.
//     * @param rowId the table row
//     * @return the Tuple for the given table row
//     */
//    public Tuple getTuple(int rowId);
//    
//    /**
//     * Indicates if this table contains the given Tuple instance.
//     * @param t the Tuple to check for containment
//     * @return true if the Tuple represents a row of this table, false if
//     * it does not
//     * @see prefuse.data.tuple.TupleSet#containsTuple(prefuse.data.Tuple)
//     */
//    public boolean containsTuple(Tuple t);
    
    public Object getValue(int rowId, String field); 
    public Object getValue(int rowId, int col);
    public boolean isValueValid(int rowId, int col);
    
    void beginEdit(int col);
    void endEdit(int col);
    boolean isEditing(int col);
    
    public void addTableListener(TableListener listnr);
    public void removeTableListener(TableListener listnr);
    public Collection<TableListener> getTableListeners();
    
    //Mutable methods
    boolean canAddRow();//FIXME
    boolean canRemoveRow();//FIXME
    
    public int addRow();
    public boolean removeRow(int row); 

    /**
     * Removes all the rows.
     * 
     * <p>After this method, the table is almost in the same state as if 
     * it had been created afresh except it contains the same columns as before 
     * but they are all cleared.
     * 
     */
    void removeAllRows();
    
    /**
     * Add a Tuple to this table. If the Tuple is already a member of this
     * table, nothing is done and null is returned. If the Tuple is not
     * a member of this Table but has a compatible data schema, as
     * determined by {@link Schema#isAssignableFrom(Schema)}, a new row
     * is created, the Tuple's values are copied, and the new Tuple that
     * is a member of this Table is returned. If the data schemas are not
     * compatible, nothing is done and null is returned.
     * @param t the Tuple to "add" to this table
     * @return the actual Tuple instance added to this table, or null if
     * no new Tuple has been added
     * @see prefuse.data.tuple.TupleSet#addTuple(prefuse.data.Tuple)
     */
    public Tuple addTuple(Tuple t);
    
    /**
     * Get the number of times this Table has been modified. Adding rows,
     * deleting rows, and updating table cell values all contribute to
     * this count.
     * @return the number of modifications to this table
     */
    public int getModificationCount();
    
    public void set(int rowId, String field, Object val); 
    public void set(int rowId, int col, Object val);
}

