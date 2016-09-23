package edu.berkeley.nlp.assignments.assign1.student.Utility;

import edu.berkeley.nlp.util.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;

/** Open Addressing HashMap from Shorts -> Integers
 *
 */
public class ShortIntOpenHashMap {

    private short[] keys;

    private int[] values;

    private int size = 0;
    private int sizeInTheory = 0;

    private final short EMPTY_KEY = -1;

    private final double MAX_LOAD_FACTOR;

    public boolean put(short k, int v) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelp(k, v, keys, values);

    }

    public ShortIntOpenHashMap() {
        this(10);
    }

    public ShortIntOpenHashMap(int initialCapacity_) {
        this(initialCapacity_, 0.7);
    }

    public ShortIntOpenHashMap(int initialCapacity_, double loadFactor) {
        int cap = Math.max(5, (int) (initialCapacity_ / loadFactor));
        MAX_LOAD_FACTOR = loadFactor;
        values = new int[cap];
        Arrays.fill(values, -1);
        keys = new short[cap];
        Arrays.fill(keys, (short) -1); // added to avoid collision with k = 0
        sizeInTheory = initialCapacity_;
    }

    private void rehash() {
        short[] newKeys = new short[keys.length * 3 / 2];
        int[] newValues = new int[values.length * 3 / 2];
        Arrays.fill(newValues, -1);
        Arrays.fill(newKeys, (short) -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            short curr = keys[i];
            if (curr != EMPTY_KEY) {
                int val = values[i];
                putHelp(curr, val, newKeys, newValues);
            }
        }
        keys = newKeys;
        values = newValues;
    }

    public void rehash(double expandedRatio) {
        short[] newKeys = new short[ (int)(keys.length * expandedRatio)];
        int[] newValues = new int[(int) (values.length * expandedRatio)];
        Arrays.fill(newValues, 0);
        Arrays.fill(newKeys, (short) -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            short curr = keys[i];
            if (curr != EMPTY_KEY) {
                int val = values[i];
                putHelp(curr, val, newKeys, newValues);
            }
        }
        keys = newKeys;
        values = newValues;
    }

    private boolean putHelp(short k, int v, short[] keyArray, int[] valueArray) {
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

    private int getInitialPos(short k, short[] keyArray) {
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

    public int get(short k) {
        int pos = find(k);

        return values[pos];
    }

    private int find(short k) {
        int pos = getInitialPos(k, keys);
        short curr = keys[pos];
        while (curr != EMPTY_KEY && curr != k) {
            pos++;
            if (pos == keys.length) pos = 0;
            curr = keys[pos];
        }
        return pos;
    }

    public void increment(short k, int c) {
        int pos = find(k);
        short currKey = keys[pos];
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
        public Entry(short key, int value) {
            super();
            this.key = key;
            this.value = value;
        }

        public short key;

        public int value;

        public short getKey() {
            return key;
        }

        public int getValue() {
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

