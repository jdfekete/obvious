package obvious;

import java.util.Collection;


/**
 * Table have columns and rowIds.
 * 
 */
public interface Table extends Data {
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
    
    public void set(int rowId, String field, Object val); 
    public void set(int rowId, int col, Object val);
}

