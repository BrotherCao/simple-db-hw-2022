package simpledb.storage;

import simpledb.common.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TupleDesc describes the schema of a tuple.
 * 描述turple的模式
 */
public class TupleDesc implements Serializable {

    /**
     * 存储TDItem
     */
    private TDItem[] tdItems;
    /**
     * A help class to facilitate organizing the information of each field
     */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         */
        public final Type fieldType;

        /**
         * The name of the field
         */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return An iterator which iterates over all the field TDItems
     *         that are included in this TupleDesc
     */
    public Iterator<TDItem> iterator() {
        // some code goes here
        Iterator<TDItem> iterator = Arrays.asList(tdItems).iterator();
        return iterator;
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr  array specifying the number of and types of fields in this
     *                TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may
     *                be null.
     */

    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        if(typeAr == null || typeAr.length < 1){
            throw new IllegalArgumentException("typeAr must contains at least one entry");
        }
        int len = typeAr.length;
        tdItems = new TDItem[len];
        for(int i = 0;i < len;i++){
            tdItems[i] = new TDItem(typeAr[i],fieldAr[i]);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in this
     *               TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        if(typeAr == null || typeAr.length < 1){
            throw new IllegalArgumentException("typeAr must contains at least one entry");
        }
        int len = typeAr.length;
        tdItems = new TDItem[len];
        for(int i = 0;i < len;i++){
            tdItems[i] = new TDItem(typeAr[i],"");
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItems.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        //  some code goes here
        int len = tdItems.length;
        if(i < 0 || i >= len){
            throw  new NoSuchElementException("i is not valid!");
        }
        return tdItems[i].fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid
     *          index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        //  some code goes here
        int len = tdItems.length;
        if(i < 0 || i >= len){
            throw  new NoSuchElementException("i is not valid!");
        }
        return tdItems[i].fieldType;
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int indexForFieldName(String name) throws NoSuchElementException {
        // some code goes here
        for(int i = 0;i < tdItems.length;i++){
            if(tdItems[i].fieldName.equals(name)){
                return i;
            }
        }
        throw new NoSuchElementException("no field with a matching name is found!");
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        for(int i = 0;i < tdItems.length;i++){
            size += tdItems[i].fieldType.getLen();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     *
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        Type[] typeAr = new Type[td1.tdItems.length + td2.tdItems.length];
        String[] fieldAr = new String[td1.tdItems.length + td2.tdItems.length];
        for(int i = 0;i < td1.tdItems.length;i++){
            typeAr[i] = td1.tdItems[i].fieldType;
            fieldAr[i] = td1.tdItems[i].fieldName;
        }
        for(int j = 0;j < td2.tdItems.length;j++){
            typeAr[j + td1.tdItems.length] = td2.tdItems[j].fieldType;
            fieldAr[j + td1.tdItems.length] = td2.tdItems[j].fieldName;
        }
        return new TupleDesc(typeAr,fieldAr);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // some code goes here
        if(this.getClass().isInstance(o)){
            TupleDesc compare = (TupleDesc) o;
            if(this.numFields() == compare.numFields()){
                for(int i = 0;i < compare.numFields();i++){
                    if(!this.getFieldType(i).equals(compare.getFieldType(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuilder str = new StringBuilder();
        for(int i = 0;i < this.numFields() - 1;i++){
            str.append(this.tdItems[0].fieldType + "(" + this.tdItems[0].fieldName + "), ");
        }
        str.append(this.tdItems[this.numFields() - 1] + "(" + this.tdItems[this.numFields() - 1].fieldName + ")");
        return str.toString();
    }
}
