package edu.berkeley.nlp.assignments.assign1.student.Utility;

import edu.berkeley.nlp.util.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Similar to Unigram but the key type is primitively of "long"
 * And has no 'in-between' element
 * key => value : end : start
 * w1w2 => c(w1w2) : c(.w1w2): c(w1w2.)
 * Created by Gorilla on 9/19/2016.
 */
public class BigramOpenHashMapWithRank {
    private long[] keys;

    private short[] values;
    private short[] trigramEndsWithThis;
    private short[] trigramStartsWithThis;

    private int size = 0;
    private int sizeInTheory = 0;

    private final int EMPTY_KEY = -1;

    private final double MAX_LOAD_FACTOR;

    // order: value -> end -> start -> between
    public boolean putRanks(long k, short v, short end, short start) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelp(k, v, end, start, keys, values, trigramEndsWithThis, trigramStartsWithThis);
    }
    public boolean putValueRank(long k, short v) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpValue(k, v, keys, values);
    }
    public boolean putEndRank(long k, short e) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpEnd(k, e, keys, trigramEndsWithThis);
    }
    public boolean putStartRank(long k, short s) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpStart(k, s, keys, trigramStartsWithThis);
    }

    public BigramOpenHashMapWithRank() {
        this(10);
    }

    public BigramOpenHashMapWithRank(int initialCapacity_) {
        this(initialCapacity_, 0.7);
    }

    public BigramOpenHashMapWithRank(int initialCapacity_, double loadFactor) {
        int cap = Math.max(5, (int) (initialCapacity_ / loadFactor));
        MAX_LOAD_FACTOR = loadFactor;

        values = new short[cap];
        trigramEndsWithThis = new short[cap];
        trigramStartsWithThis = new short[cap];
        Arrays.fill(values, (short)-1);
        Arrays.fill(trigramEndsWithThis, (short)-1);
        Arrays.fill(trigramStartsWithThis, (short)-1);

        keys = new long[cap];
        Arrays.fill(keys, -1); // added to avoid collision with k = 0

        sizeInTheory = initialCapacity_;
    }

    private void rehash() {
        long[] newKeys = new long[keys.length * 3 / 2];
        short[] newValues = new short[values.length * 3 / 2];
        short[] newtrigramEndsWithThis = new short[values.length * 3 / 2];
        short[] newtrigramStartsWithThis = new short[values.length * 3 / 2];

        Arrays.fill(newValues, (short)-1);
        Arrays.fill(newtrigramEndsWithThis, (short)-1);
        Arrays.fill(newtrigramStartsWithThis, (short)-1);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            long curr = keys[i];
            if (curr != EMPTY_KEY) {
                short val = values[i];
                short end = trigramEndsWithThis[i];
                short start = trigramStartsWithThis[i];
                // TODO: fix this
                putHelp(curr, val, end, start, newKeys, newValues, newtrigramEndsWithThis, newtrigramStartsWithThis);
            }
        }
        // overwrite
        keys = newKeys;
        values = newValues;
        trigramEndsWithThis = newtrigramEndsWithThis;
        trigramStartsWithThis = newtrigramStartsWithThis;
    }

    public void rehash(double expandedRatio) {
        long[] newKeys = new long[(int)(keys.length * expandedRatio)];
        short[] newValues = new short[(int)(values.length * expandedRatio)];
        short[] newtrigramEndsWithThis = new short[(int)(values.length * expandedRatio)];
        short[] newtrigramStartsWithThis = new short[(int)(values.length * expandedRatio)];

        Arrays.fill(newValues, (short)-1);
        Arrays.fill(newtrigramEndsWithThis, (short)-1);
        Arrays.fill(newtrigramStartsWithThis, (short)-1);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            long curr = keys[i];
            if (curr != EMPTY_KEY) {
                short val = values[i];
                short end = trigramEndsWithThis[i];
                short start = trigramStartsWithThis[i];
                // TODO: fix this
                putHelp(curr, val, end, start, newKeys, newValues, newtrigramEndsWithThis, newtrigramStartsWithThis);
            }
        }
        // overwrite
        keys = newKeys;
        values = newValues;
        trigramEndsWithThis = newtrigramEndsWithThis;
        trigramStartsWithThis = newtrigramStartsWithThis;
    }

    // order: value -> end -> start -> between
    private boolean putHelp(long k, short v, short end, short start,
                            long[] keyArray, short[] valueArray, short[] ends, short[] starts) {
        int pos = getInitialPos(k, keyArray);
        long curr = keyArray[pos];
        // find proper key first
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        valueArray[pos] = v;
        starts[pos] = start;
        ends[pos] = end;

        // found a key, let's insert into it
        if (curr == EMPTY_KEY) {
            size++;
            keyArray[pos] = k;
            return true;
        }
        return false;
    }
    private boolean putHelpValue(long k, short v, long[] keyArray, short[] valueArray) {
        int pos = getInitialPos(k, keyArray);
        long curr = keyArray[pos];
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
    private boolean putHelpEnd(long k, short e, long[] keyArray, short[] endArray) {
        int pos = getInitialPos(k, keyArray);
        long curr = keyArray[pos];
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
    private boolean putHelpStart(long k, short s, long[] keyArray, short[] startArray) {
        int pos = getInitialPos(k, keyArray);
        long curr = keyArray[pos];
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

    private int getInitialPos(long k, long[] keyArray) {
        int hash = getHashCode(k);
        int pos = (int) hash % keyArray.length;
        if (pos < 0) pos += keyArray.length;
        // N.B. Doing it this old way causes Integer.MIN_VALUE to be
        // handled incorrect since -Integer.MIN_VALUE is still
        // Integer.MIN_VALUE
//		if (hash < 0) hash = -hash;
//		int pos = hash % keyArray.length;
        return pos;
    }
    // helper for hash code
    private int getHashCode(long n) {
        return (int)((131111L*n)^n^(1973*n)%sizeInTheory);
//        int hash = ((int) (n ^ (n >>> 32)) * 3875239);
//        return hash%sizeInTheory;
    }

    // 5 getters
    public short[] getAllRanks(int k) {
        int pos = find(k);

        short[] results = new short[3];
        results[0] = values[pos];
        results[1] = trigramEndsWithThis[pos];
        results[2] = trigramStartsWithThis[pos];

        return results;
    }

    public short getValueRank(long k) {
        int pos = find(k);

        return values[pos];
    }
    public short gettrigramEndsWithThisRank(long k) {
        int pos = find(k);

        return trigramEndsWithThis[pos];
    }
    public short gettrigramStartsWithThisRank(long k) {
        int pos = find(k);

        return trigramStartsWithThis[pos];
    }

    private int find(long k) {
        int pos = getInitialPos(k, keys);
        long curr = keys[pos];
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keys.length) pos = 0;
            curr = keys[pos];
        }
        return pos;
    }

    public void incrementValue(long k, short v) {
        int pos = find(k);
        long currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putValueRank(k, v);
        } else
            values[pos]++;
    }
    public void incrementEnd(long k, short e) {
        int pos = find(k);
        long currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putEndRank(k, e);
        } else
            trigramEndsWithThis[pos]++;
    }
    public void incrementStart(long k, short s) {
        int pos = find(k);
        long currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putEndRank(k, s);
        } else
            trigramStartsWithThis[pos]++;
    }

    public static class Entry
    {
        /**
         * @param key
         * @param value
         */
        public Entry(long key, short value, short end, short start) {
            super();
            this.key = key;
            this.value = value;
            this.end = end;
            this.start = start;
        }

        public long key;
        public short value;
        public short end;
        public short start;

        public long getKey() {
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
    }

    private class EntryIterator extends BigramOpenHashMapWithRank.MapIterator<BigramOpenHashMapWithRank.Entry>
    {
        public BigramOpenHashMapWithRank.Entry next() {
            final int nextIndex = nextIndex();
            //access arrays of values from mother class
            return new BigramOpenHashMapWithRank.Entry(keys[nextIndex], values[nextIndex],
                    trigramEndsWithThis[nextIndex], trigramStartsWithThis[nextIndex]);
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

    public Iterable<BigramOpenHashMapWithRank.Entry> entrySet() {
        return CollectionUtils.iterable(new BigramOpenHashMapWithRank.EntryIterator());
    }

    public int size() {
        return size;
    }

    public int actualSize() {
        return keys.length;
    }
    /**
     * Optimization method to free up unused entries in this map
     *
     */
    public void optimizeStorage(double expandedRatio){
        System.out.println("This map has the utilization of " + 100 * size() / (double) actualSize() + "%. Now optimizing...");

        long[] newKeys = new long[size];
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
