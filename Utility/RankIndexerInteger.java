package edu.berkeley.nlp.assignments.assign1.student.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Maintains a two-way map between a set of objects and contiguous integers from
 * 0 to the number of objects. Use get(i) to look up object i, and
 * indexOf(object) to look up the index of an object.
 *
 * @author Hai
 * @author Dan Klein
 */
public class RankIndexerInteger implements Serializable
{
    private static final long serialVersionUID = -8769544079136550516L;

    List<Integer> objects;

    IntShortOpenHashMap indexes;

    /**
     * Return the object with the given index
     *
     * @param index
     */
    public Integer get(int index) {
        if (index <0)
            return 0;
        return objects.get(index);
    }

    /**
     * Returns the number of objects indexed.
     */
    public int size() {
        return objects.size();
    }

    /**
     * Returns the index of the given object, or -1 if the object is not present
     * in the indexer.
     *
     * @param o
     * @return
     */
    public short indexOf(int o) {
        short index = indexes.get(o);
        return index;
    }

    /**
     * Add an element to the indexer if not already present. In either case,
     * returns the index of the given object.
     *
     * @param e
     * @return
     */
    public short addAndGetIndex(int e) {
        short index = indexes.get(e);
        if (index >= 0) { return index; }
        //  Else, add
        short newIndex = (short) size();
        objects.add(e);
        indexes.put(e, newIndex);
        return newIndex;
    }

    /**
     * Add an element to the indexer. If the element is already in the indexer,
     * the indexer is unchanged (and false is returned).
     *
     * @param e
     * @return
     */

    public boolean add(int e) {
        return addAndGetIndex(e) == size() - 1;
    }

    public RankIndexerInteger() {
        objects = new ArrayList<Integer>();
        indexes = new IntShortOpenHashMap();
    }
}
