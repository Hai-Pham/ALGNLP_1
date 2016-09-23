package edu.berkeley.nlp.assignments.assign1.student.Utility;

import edu.berkeley.nlp.util.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;

/** Open Addressing HashMap from Shorts -> Integers
 *
 */
public class IntShortOpenHashMap {

    private int[] keys;

    private short[] values;

    private int size = 0;
    private int sizeInTheory = 0;

    private final int EMPTY_KEY = -1;

    private final double MAX_LOAD_FACTOR;

    public boolean put(int k, short v) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelp(k, v, keys, values);

    }

    public IntShortOpenHashMap() {
        this(10);
    }

    public IntShortOpenHashMap(int initialCapacity_) {
        this(initialCapacity_, 0.7);
    }

    public IntShortOpenHashMap(int initialCapacity_, double loadFactor) {
        int cap = Math.max(5, (int) (initialCapacity_ / loadFactor));
        MAX_LOAD_FACTOR = loadFactor;
        values = new short[cap];
        Arrays.fill(values, (short) -1);
        keys = new int[cap];
        Arrays.fill(keys, -1); // added to avoid collision with k = 0
        sizeInTheory = initialCapacity_;
    }

    private void rehash() {
        int[] newKeys = new int[keys.length * 3 / 2];
        short[] newValues = new short[values.length * 3 / 2];
        Arrays.fill(newValues, (short)-1);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            int curr = keys[i];
            if (curr != EMPTY_KEY) {
                short val = values[i];
                putHelp(curr, val, newKeys, newValues);
            }
        }
        keys = newKeys;
        values = newValues;
    }

    public void rehash(double expandedRatio) {
        int[] newKeys = new int[ (int)(keys.length * expandedRatio)];
        short[] newValues = new short[(int) (values.length * expandedRatio)];
        Arrays.fill(newValues, (short)0);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            int curr = keys[i];
            if (curr != EMPTY_KEY) {
                short val = values[i];
                putHelp(curr, val, newKeys, newValues);
            }
        }
        keys = newKeys;
        values = newValues;
    }

    private boolean putHelp(int k, short v, int[] keyArray, short[] valueArray) {
        int pos = getInitialPos(k, keyArray);
        int curr = keyArray[pos];
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keyArray.length) pos = 0;
            curr = keyArray[pos];
        }

        valueArray[pos] = v;
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
    }

    public short get(int k) {
        int pos = find(k);

        return values[pos];
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

    public void increment(int k, short c) {
        int pos = find(k);
        int currKey = keys[pos];
        if (currKey == EMPTY_KEY) {
            put(k, c);
        } else
            values[pos]++;
    }

    public static class Entry
    {
        /**
         * @param key
         * @param value
         */
        public Entry(int key, short value) {
            super();
            this.key = key;
            this.value = value;
        }

        public int key;

        public short value;

        public int getKey() {
            return key;
        }

        public short getValue() {
            return value;
        }
    }

    private class EntryIterator extends MapIterator<Entry>
    {
        public Entry next() {
            final int nextIndex = nextIndex();

            return new Entry(keys[nextIndex], values[nextIndex]);
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

    public Iterable<Entry> entrySet() {
        return CollectionUtils.iterable(new EntryIterator());
    }

    public int size() {
        return size;
    }

    public int actualSize() {
        return keys.length;
    }

    /**
     * Automatic Optimization method to free up unused entries in this map
     *
     */
    public void autoOptimizeStorage(){
        double utilization = size / (double) actualSize();
        rehash(utilization + 0.15);
    }
}

