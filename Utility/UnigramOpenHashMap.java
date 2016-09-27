package edu.berkeley.nlp.assignments.assign1.student.Utility;

import edu.berkeley.nlp.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * key => value : end : start: between
 * w => c(w) : c(.w): c(w.): c(.w.)
 * Created by Gorilla on 9/19/2016.
 */
public class UnigramOpenHashMap {
    private int[] keys;

    private int[] values;
    private int[] bigramEndsWithThis;
    private int[] bigramStartsWithThis;
    private int[] bigramWithThisInBetween;

    private int size = 0;
    private int sizeInTheory = 0;
    private int actualSize = 0;
    private int totalBigramEndsWithThis; // TODO: this one is correctly retrieved if using increment() only

    private final int EMPTY_KEY = -1;

    private final double MAX_LOAD_FACTOR;

    // order: value -> end -> start -> between
    public boolean put(int k, int v, int end, int start, int between) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelp(k, v, end, start, between, keys, values, bigramEndsWithThis,
                bigramStartsWithThis, bigramWithThisInBetween);
    }

    public boolean putValue(int k, int v) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpValue(k, v, keys, values);
    }
    public boolean putEnd(int k, int e) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpEnd(k, e, keys, bigramEndsWithThis);
    }
    public boolean putStart(int k, int s) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpStart(k, s, keys, bigramStartsWithThis);
    }
    public boolean putBetween(int k, int b) {
        if (size / (double) keys.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        return putHelpBetween(k, b, keys, bigramWithThisInBetween);
    }

    public UnigramOpenHashMap() {
        this(10);
    }

    public UnigramOpenHashMap(int initialCapacity_) {
        this(initialCapacity_, 0.7);
    }

    public UnigramOpenHashMap(int initialCapacity_, double loadFactor) {
        int cap = Math.max(5, (int) (initialCapacity_ / loadFactor));
        MAX_LOAD_FACTOR = loadFactor;

        values = new int[cap];
        bigramEndsWithThis = new int[cap];
        bigramStartsWithThis = new int[cap];
        bigramWithThisInBetween = new int[cap];
        Arrays.fill(values, 0);
        Arrays.fill(bigramEndsWithThis, 0);
        Arrays.fill(bigramStartsWithThis, 0);
        Arrays.fill(bigramWithThisInBetween, 0);

        keys = new int[cap];
        Arrays.fill(keys, -1); // added to avoid collision with k = 0

        sizeInTheory = initialCapacity_;
    }

    private void rehash() {
        int[] newKeys = new int[keys.length * 3 / 2];
        int[] newValues = new int[values.length * 3 / 2];
        int[] newBigramEndsWithThis = new int[values.length * 3 / 2];
        int[] newBigramStartsWithThis = new int[values.length * 3 / 2];
        int[] newBigramWithThisInBetween = new int[values.length * 3 / 2];

        Arrays.fill(newValues, 0);
        Arrays.fill(newBigramEndsWithThis, 0);
        Arrays.fill(newBigramStartsWithThis, 0);
        Arrays.fill(newBigramWithThisInBetween, 0);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            int curr = keys[i];
            if (curr != EMPTY_KEY) {
                int val = values[i];
                int end = bigramEndsWithThis[i];
                int start = bigramStartsWithThis[i];
                int between = bigramWithThisInBetween[i];
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
        int[] newValues = new int[(int) (values.length * expandedRatio)];
        int[] newBigramEndsWithThis = new int[(int) (values.length * expandedRatio)];
        int[] newBigramStartsWithThis = new int[(int) (values.length * expandedRatio)];
        int[] newBigramWithThisInBetween = new int[(int) (values.length * expandedRatio)];

        Arrays.fill(newValues, 0);
        Arrays.fill(newBigramEndsWithThis, 0);
        Arrays.fill(newBigramStartsWithThis, 0);
        Arrays.fill(newBigramWithThisInBetween, 0);
        Arrays.fill(newKeys, -1);
        size = 0;
        for (int i = 0; i < keys.length; ++i) {
            int curr = keys[i];
            if (curr != EMPTY_KEY) {
                int val = values[i];
                int end = bigramEndsWithThis[i];
                int start = bigramStartsWithThis[i];
                int between = bigramWithThisInBetween[i];
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
    private boolean putHelp(int k, int v, int end, int start, int between,
                            int[] keyArray, int[] valueArray, int[] ends, int[] starts, int[] betweens) {
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
    private boolean putHelpValue(int k, int v, int[] keyArray, int[] valueArray) {
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
    private boolean putHelpEnd(int k, int e, int[] keyArray, int[] endArray) {
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
    private boolean putHelpStart(int k, int s, int[] keyArray, int[] startArray) {
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
    private boolean putHelpBetween(int k, int b, int[] keyArray, int[] betweenArray) {
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
//        return hash;
    }

    // 4 getters
    public int getValue(int k) {
        int pos = find(k);

        return values[pos];
    }
    public int getBigramEndsWithThis(int k) {
        int pos = find(k);

        return bigramEndsWithThis[pos];
    }
    public int getBigramStartsWithThis(int k) {
        int pos = find(k);

        return bigramStartsWithThis[pos];
    }
    public int getBigramWithThisInBetween(int k) {
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

    public void incrementValue(int k, int v) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putValue(k, v);
        } else
            values[pos]++;
    }
    public void incrementEnd(int k, int e) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putEnd(k, e);
        } else {
            bigramEndsWithThis[pos]++;
            totalBigramEndsWithThis++;
        }
    }
    public void incrementStart(int k, int s) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putStart(k, s);
        } else
            bigramStartsWithThis[pos]++;

    }
    public void incrementBetween(int k, int b) {
        int pos = find(k);
        int currKey = keys[pos];
        // key is new
        if (currKey == EMPTY_KEY) {
            putBetween(k, b);
        } else
            bigramWithThisInBetween[pos]++;
    }


    public static class Entry
    {
        /**
         * @param key
         * @param value
         */
        public Entry(int key, int value, int end, int start, int between) {
            super();
            this.key = key;
            this.value = value;
            this.end = end;
            this.start = start;
            this.between = between;
        }

        public int key;
        public int value;
        public int end;
        public int start;
        public int between;

        public int getKey() {
            return key;
        }
        public int getValue() {
            return value;
        }
        public int getEnd() {
            return end;
        }
        public int getStart() {
            return start;
        }
        public int getBetween() {
            return between;
        }
        public int[] getAllValues() {
            return new int[] {value, end, start, between};
        }
    }

    private class EntryIterator extends UnigramOpenHashMap.MapIterator<UnigramOpenHashMap.Entry>
    {
        public UnigramOpenHashMap.Entry next() {
            final int nextIndex = nextIndex();
            //access arrays of values from mother class
            return new UnigramOpenHashMap.Entry(keys[nextIndex], values[nextIndex],
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

    public Iterable<UnigramOpenHashMap.Entry> entrySet() {
        return CollectionUtils.iterable(new UnigramOpenHashMap.EntryIterator());
    }
    public int[] getKeys() {
        int[] k = new int[size];
        int j = 0;
        for (int i = 0; i<keys.length; i++) {
            if (keys[i] != -1)
                k[j++] = keys[i];
        }
        return k;
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
    /**
     * Optimization method to free up unused entries in this map
     *
     */
    public void optimizeStorage(double expandedRatio){
        System.out.println("This map has the utilization of " + 100 * size() / (double) actualSize() + "%. Now optimizing...");

        int[] newKeys = new int[size];
        int[] newValues = new int[size];
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
    public void autoOptimizeStorage(){
        double utilization = size / (double) actualSize();
        rehash(utilization + 0.2);
    }
}
