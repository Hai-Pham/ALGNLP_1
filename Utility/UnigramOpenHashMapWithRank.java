package edu.berkeley.nlp.assignments.assign1.student.Utility;

import edu.berkeley.nlp.util.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * key => value : end : start: between
 * w => c(w) : c(.w): c(w.): c(.w.)
 * Created by Gorilla on 9/19/2016.
 */
public class UnigramOpenHashMapWithRank {
    private int[] keys;

    private short[] values;
    private short[] bigramEndsWithThis;
    private short[] bigramStartsWithThis;
    private short[] bigramWithThisInBetween;

    private int size = 0;
    private int sizeInTheory = 0;
    private int totalBigramEndsWithThis; // TODO: this one is correctly retrieved if using increment() only

    private final int EMPTY_KEY = -1;

    private final double MAX_LOAD_FACTOR;

    // order: value -> end -> start -> between
    public boolean putRanks(int k, short v, short end, short start, short between) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelp(k, v, end, start, between, keys, values, bigramEndsWithThis,
                bigramStartsWithThis, bigramWithThisInBetween);
    }

    public boolean putValueRank(int k, short v) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpValue(k, v, keys, values);
    }
    public boolean putEndRank(int k, short e) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpEnd(k, e, keys, bigramEndsWithThis);
    }
    public boolean putStartRank(int k, short s) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpStart(k, s, keys, bigramStartsWithThis);
    }
    public boolean putBetweenRank(int k, short b) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpBetween(k, b, keys, bigramWithThisInBetween);
    }

    public UnigramOpenHashMapWithRank() {
        this(10);
    }

    public UnigramOpenHashMapWithRank(int initialCapacity_) {
        this(initialCapacity_, 0.7);
    }

    public UnigramOpenHashMapWithRank(int initialCapacity_, double loadFactor) {
        int cap = Math.max(5, (int) (initialCapacity_ / loadFactor));
        MAX_LOAD_FACTOR = loadFactor;

        values = new short[cap];
        bigramEndsWithThis = new short[cap];
        bigramStartsWithThis = new short[cap];
        bigramWithThisInBetween = new short[cap];
        Arrays.fill(values, (short)0);
        Arrays.fill(bigramEndsWithThis, (short)0);
        Arrays.fill(bigramStartsWithThis, (short)0);
        Arrays.fill(bigramWithThisInBetween, (short)0);

        keys = new int[cap];
        Arrays.fill(keys, -1); // added to avoid collision with k = 0

        sizeInTheory = initialCapacity_;
    }

    private void rehash() {
        int[] newKeys = new int[keys.length * 3 / 2];
        short[] newValues = new short[values.length * 3 / 2];
        short[] newBigramEndsWithThis = new short[values.length * 3 / 2];
        short[] newBigramStartsWithThis = new short[values.length * 3 / 2];
        short[] newBigramWithThisInBetween = new short[values.length * 3 / 2];

        Arrays.fill(newValues, (short)0);
        Arrays.fill(newBigramEndsWithThis, (short)0);
        Arrays.fill(newBigramStartsWithThis, (short)0);
        Arrays.fill(newBigramWithThisInBetween, (short)0);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            int curr = keys[i];
            if (curr != EMPTY_KEY) {
                short val = values[i];
                short end = bigramEndsWithThis[i];
                short start = bigramStartsWithThis[i];
                short between = bigramWithThisInBetween[i];
                // TODO: fix this
                putHelp(curr, val, end, start, between, newKeys, newValues, newBigramEndsWithThis,
                        newBigramStartsWithThis, newBigramWithThisInBetween);
            }
        }
        // overwrite
        keys = newKeys;
        values = newValues;
        bigramEndsWithThis = newBigramEndsWithThis;
        bigramStartsWithThis = newBigramStartsWithThis;
        bigramWithThisInBetween = newBigramWithThisInBetween;
    }
    public void rehash(double expandedRatio) {
        int[] newKeys = new int[(int) (keys.length * expandedRatio)];
        short[] newValues = new short[(int) (values.length * expandedRatio)];
        short[] newBigramEndsWithThis = new short[(int) (values.length * expandedRatio)];
        short[] newBigramStartsWithThis = new short[(int) (values.length * expandedRatio)];
        short[] newBigramWithThisInBetween = new short[(int) (values.length * expandedRatio)];

        Arrays.fill(newValues, (short)0);
        Arrays.fill(newBigramEndsWithThis, (short)0);
        Arrays.fill(newBigramStartsWithThis, (short)0);
        Arrays.fill(newBigramWithThisInBetween, (short)0);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            int curr = keys[i];
            if (curr != EMPTY_KEY) {
                short val = values[i];
                short end = bigramEndsWithThis[i];
                short start = bigramStartsWithThis[i];
                short between = bigramWithThisInBetween[i];
                // TODO: fix this
                putHelp(curr, val, end, start, between, newKeys, newValues, newBigramEndsWithThis,
                        newBigramStartsWithThis, newBigramWithThisInBetween);
            }
        }
        // overwrite
        keys = newKeys;
        values = newValues;
        bigramEndsWithThis = newBigramEndsWithThis;
        bigramStartsWithThis = newBigramStartsWithThis;
        bigramWithThisInBetween = newBigramWithThisInBetween;
    }

    // order: value -> end -> start -> between
    private boolean putHelp(int k, short v, short end, short start, short between,
                            int[] keyArray, short[] valueArray, short[] ends, short[] starts, short[] betweens) {
        int pos = getInitialPos(k, keyArray);
        int curr = keyArray[pos];
        // find proper key first
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        valueArray[pos] = v;
        starts[pos] = start;
        ends[pos] = end;
        betweens[pos] = between;

        // found a key, let's insert into it
        if (curr == EMPTY_KEY) {
            size++;
            keyArray[pos] = k;
            return true;
        }
        return false;
    }
    private boolean putHelpValue(int k, short v, int[] keyArray, short[] valueArray) {
        int pos = getInitialPos(k, keyArray);
        int curr = keyArray[pos];
        // find proper key first
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        valueArray[pos] = v;
        // found a key, let's insert into it
        if (curr == EMPTY_KEY) {
            size++;
            keyArray[pos] = k;
            return true;
        }
        return false;
    }
    private boolean putHelpEnd(int k, short e, int[] keyArray, short[] endArray) {
        int pos = getInitialPos(k, keyArray);
        int curr = keyArray[pos];
        // find proper key first
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        endArray[pos] = e;
        // found a key, let's insert into it
        if (curr == EMPTY_KEY) {
            size++;
            keyArray[pos] = k;
            return true;
        }
        return false;
    }
    private boolean putHelpStart(int k, short s, int[] keyArray, short[] startArray) {
        int pos = getInitialPos(k, keyArray);
        int curr = keyArray[pos];
        // find proper key first
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        startArray[pos] = s;
        // found a key, let's insert into it
        if (curr == EMPTY_KEY) {
            size++;
            keyArray[pos] = k;
            return true;
        }
        return false;
    }
    private boolean putHelpBetween(int k, short b, int[] keyArray, short[] betweenArray) {
        int pos = getInitialPos(k, keyArray);
        int curr = keyArray[pos];
        // find proper key first
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        betweenArray[pos] = b;
        // found a key, let's insert into it
        if (curr == EMPTY_KEY) {
            size++;
            keyArray[pos] = k;
            return true;
        }
        return false;
    }

    private int getInitialPos(int k, int[] keyArray) {
        int hash = getHashCode(k);
        int pos = hash % keyArray.length;
        if (pos < 0) pos += keyArray.length;
        // N.B. Doing it this old way causes Integer.MIN_VALUE to be
        // handled incorrect since -Integer.MIN_VALUE is still
        // Integer.MIN_VALUE
//		if (hash < 0) hash = -hash;
//		int pos = hash % keyArray.length;
        return pos;
    }
    // helper for hash code
    private int getHashCode(int n) {
        return (int) ((131111L*n)^n^(1973*n)%sizeInTheory);
//        int hash = ((int) (n ^ (n >>> 32)) * 3875239);
//        return hash%sizeInTheory;
    }

    // 5 getters
    public short[] getAllRanks(int k) {
        int pos = find(k);

        short[] results = new short[4];
        results[0] = values[pos];
        results[1] = bigramEndsWithThis[pos];
        results[2] = bigramStartsWithThis[pos];
        results[3] = bigramWithThisInBetween[pos];

        return results;
    }

    public short getValueRank(int k) {
        int pos = find(k);

        return values[pos];
    }
    public short getBigramEndsWithThisRank(int k) {
        int pos = find(k);

        return bigramEndsWithThis[pos];
    }
    public short getBigramStartsWithThisRank(int k) {
        int pos = find(k);

        return bigramStartsWithThis[pos];
    }
    public short getBigramWithThisInBetweenRank(int k) {
        int pos = find(k);

        return bigramWithThisInBetween[pos];
    }

    private int find(int k) {
        int pos = getInitialPos(k, keys);
        int curr = keys[pos];
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keys.length) pos = 0;
            curr = keys[pos];
        }
        return pos;
    }

    public void incrementValue(int k, short v) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putValueRank(k, v);
        } else
            values[pos]++;
    }
    public void incrementEnd(int k, short e) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putEndRank(k, e);
        } else {
            bigramEndsWithThis[pos]++;
            totalBigramEndsWithThis++;
        }
    }
    public void incrementStart(int k, short s) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putStartRank(k, s);
        } else
            bigramStartsWithThis[pos]++;

    }
    public void incrementBetween(int k, short b) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putBetweenRank(k, b);
        } else
            bigramWithThisInBetween[pos]++;
    }


    public static class Entry
    {
        /**
         * @param key
         * @param value
         */
        public Entry(int key, short value, short end, short start, short between) {
            super();
            this.key = key;
            this.value = value;
            this.end = end;
            this.start = start;
            this.between = between;
        }

        public int key;
        public short value;
        public short end;
        public short start;
        public short between;

        public int getKey() {
            return key;
        }
        public short getValue() {
            return value;
        }
        public short getEnd() {
            return end;
        }
        public short getStart() {
            return start;
        }
        public short getBetween() {
            return between;
        }
        public short[] getAllRanks() {
            return new short[] {value, end, start, between};
        }
    }

    private class EntryIterator extends UnigramOpenHashMapWithRank.MapIterator<UnigramOpenHashMapWithRank.Entry>
    {
        public UnigramOpenHashMapWithRank.Entry next() {
            final int nextIndex = nextIndex();
            //access arrays of values from mother class
            return new UnigramOpenHashMapWithRank.Entry(keys[nextIndex], values[nextIndex],
                    bigramEndsWithThis[nextIndex], bigramStartsWithThis[nextIndex], bigramWithThisInBetween[nextIndex]);
        }
    }

    private abstract class MapIterator<E> implements Iterator<E>
    {
        public MapIterator() {
            end = keys.length;
            next = -1;
            nextIndex();
        }

        public boolean hasNext() {
            return next < end;
        }

        int nextIndex() {
            int curr = next;
            do {
                next++;
            } while (next < end && keys[next] == EMPTY_KEY);
            return curr;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private int next, end;
    }

    public Iterable<UnigramOpenHashMapWithRank.Entry> entrySet() {
        return CollectionUtils.iterable(new UnigramOpenHashMapWithRank.EntryIterator());
    }

    public int size() {
        return size;
    }

    public int actualSize() {
        return keys.length;
    }

    public int getTotalBigramEndsWithThis() {
        return totalBigramEndsWithThis;
    }
    public void setTotalBigramEndsWithThis(int v) {
        totalBigramEndsWithThis = v;
    }
    /**
     * Optimization method to free up unused entries in this map
     *
     */
    public void optimizeStorage(double expandedRatio){
        System.out.println("This map has the utilization of " + 100 * size() / (double) actualSize() + "%. Now optimizing...");

        int[] newKeys = new int[size];
        short[] newValues = new short[size];
        int j = 0;

        for (int i=0; i<values.length; i++) {
            if (keys[i] != -1) {
                newKeys[j] = keys[i];
                newValues[j] = values[i];
                j++;
            }
        }
        // free up
        keys = newKeys;
        values = newValues;

        rehash(expandedRatio);
    }
}
