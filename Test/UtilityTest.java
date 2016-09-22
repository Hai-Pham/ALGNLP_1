package edu.berkeley.nlp.assignments.assign1.student.Test;


import edu.berkeley.nlp.assignments.assign1.student.Utility.*;
import edu.berkeley.nlp.mt.decoder.Logger;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorilla on 9/19/2016.
 */
public class UtilityTest {
    @Test
    public void testBigramBitPacking() {
//        int w1 = (int) (Math.pow(2.0, 19.0) + 123);
//        int w2 = 1048570;
//
//        System.out.println(w1 + " " + w2);
//        assertEquals((w1 < Math.pow(2.0, 20.0)), true);
//        assertEquals((w2 < Math.pow(2.0, 20.0)), true);
//
//        long encodedBigramBitPacking = Assignment1Utility.bitPackingBigram(w1, w2);
//        int[] result = Assignment1Utility.bigramBitPackingDecode(encodedBigramBitPacking);
//
//        System.out.println(result[0] + " " + result[1]);
//        assertEquals(w1, result[0]);
//        assertEquals(w2, result[1]);

        Random random = new Random();
        random.setSeed(1);
        for (int i =0; i < 10; i++) {
            int w1 = random.nextInt((int)Math.pow(2.0, 20.0));
            int w2 = random.nextInt((int)Math.pow(2.0, 20.0));
            int w3 = random.nextInt((int)Math.pow(2.0, 20.0));
            System.out.println(w1 + " " + w2);
            long encodedBigramBitPacking = Assignment1Utility.bitPackingBigram(w1, w2);
            long encodedTrigramBitPacking = Assignment1Utility.bitPackingTrigram(w1, w2, w3);
            int[] result = Assignment1Utility.bigramBitPackingDecode(encodedBigramBitPacking);
            int[] result2 = Assignment1Utility.trigramBitPackingDecode(encodedTrigramBitPacking);
            assertEquals(w1, result[0]);
            assertEquals(w2, result[1]);
            assertEquals(w1, result2[0]);
            assertEquals(w2, result2[1]);
            assertEquals(w3, result2[2]);
        }
    }
//
//    @Test
//    public void testTrigramBitPacking() {
//        int w1 = (int) (Math.pow(2.0, 19.0) + 123);
//        int w2 = 1048570;
//        int w3 = (int) (Math.pow(2, 17.0)  + 12342);
//
//        System.out.println(w1 + " " + w2 + " " + w3);
//        assertEquals((w1 < Math.pow(2.0, 20.0)), true);
//        assertEquals((w2 < Math.pow(2.0, 20.0)), true);
//        assertEquals((w3 < Math.pow(2.0, 20.0)), true);
//
//        long encodedTrigramBitPacking = Assignment1Utility.bitPackingTrigram(w1, w2, w3);
//        int[] result = Assignment1Utility.trigramBitPackingDecode(encodedTrigramBitPacking);
//
//        System.out.println(result[0] + " " + result[1] + " " + result[2]);
//        assertEquals(w1, result[0]);
//        assertEquals(w2, result[1]);
//        assertEquals(w3, result[2]);
//    }
//
//    @Test
//    public void testBigram() {
//        System.out.println("Testing bigram ...");
//        Bigram bigram1 = new Bigram(123, 456);
//        Bigram bigram2 = new Bigram(123, 789);
//        Bigram bigram3 = new Bigram(123, 456);
//
//        assertEquals(bigram1, bigram3);
//        assertEquals(bigram1.hashCode(), bigram3.hashCode());
//        System.out.println(bigram1.hashCode());
//        System.out.println(bigram2.hashCode());
//
//        Bigram<String> bigram4 = new Bigram<>("this", "is");
//        Bigram<String> bigram5 = new Bigram<>("a", "test");
//        Bigram<String> bigram6 = new Bigram<>("this", "is");
//
//        assertEquals(bigram4.hashCode(), bigram6.hashCode());
//        assertEquals(bigram4, bigram6);
//        assertNotEquals(bigram4, bigram5);
//        System.out.println(bigram4.hashCode());
//        System.out.println(bigram5.hashCode());
//        System.out.println(bigram6.hashCode());
//    }
//
//    @Test
//    public void testTrigram() {
//        System.out.println("Testing Trigram....");
//        Trigram trigram1 = new Trigram(1, 2, 3);
//        Trigram trigram2 = new Trigram(2, 3, 4);
//        Trigram trigram3 = new Trigram(1, 2, 3);
//        assertFalse(trigram1 == trigram2);
//        assertFalse(trigram1 == trigram3);
//        // always use equals to compare 2 objects
//        assertTrue(trigram1.equals(trigram3));
//        assertEquals(trigram1, trigram3);
//        System.out.println(trigram1.getWord3());
//    }

//    @Test
//    public void testIntIntOpenHashMap() {
//        Random rand = new Random();
//        IntIntOpenHashMap testMap = new IntIntOpenHashMap(20);
//        for (int i=0; i<1000; i++) {
//            testMap.put(rand.nextInt(50), rand.nextInt(50));
//        }
//        System.out.println("size of test Map is " + testMap.size());
//
//        Iterable<IntIntOpenHashMap.Entry> entrySet = testMap.entrySet();
//        System.out.println("Map content: ");
//        for (IntIntOpenHashMap.Entry entryKV: entrySet) {
//            System.out.println(entryKV.getKey() + ":" + entryKV.getValue());
//        }
//    }

//    @Test
//    public void testLongIntOpenHashMap() {
//        Random rand = new Random();
//        LongIntOpenHashMap testMap = new LongIntOpenHashMap(20);
//        for (int i=0; i<10000; i++) {
//            testMap.put(rand.nextLong(), rand.nextInt());
//        }
//        System.out.println("size of test Map is " + testMap.size());
//
//        Iterable<LongIntOpenHashMap.Entry> entrySet = testMap.entrySet();
//        System.out.println("Map content: ");
//        for (LongIntOpenHashMap.Entry entryKV: entrySet) {
//            System.out.println(entryKV.getKey() + ":" + entryKV.getValue());
//        }
//    }

//    @Test
//    public void testUnigramOpenHashMap() {
//        Random rand = new Random();
//        UnigramOpenHashMap testMap = new UnigramOpenHashMap(20);
//        for (int i=0; i<100; i++) {
////            testMap.put(i, i, i, i, i);
//            testMap.put(i, i, 0, 0, 0);
//            testMap.incrementEnd(i, 1);
//            testMap.incrementStart(i, 20);//just increment 1
//            testMap.incrementBetween(i, 30);
//            testMap.incrementBetween(i, 30);
//            testMap.incrementBetween(i, 30);
//            testMap.incrementEnd(i, 1);
//            testMap.putEnd(i, 10);
//            testMap.putValue(i, i*2);
//            testMap.putBetween(i, i-1);
//            testMap.incrementValue(i, 1);
//        }
//        Iterable<UnigramOpenHashMap.Entry> entrySet = testMap.entrySet();
//        System.out.println("Map content: ");
//        for (UnigramOpenHashMap.Entry entryKV: entrySet) {
//            System.out.println(entryKV.getKey() + " : " + entryKV.getValue() + " " + entryKV.getEnd() + " " +
//                    entryKV.getStart() + " " + entryKV.getBetween());
//        }
//        System.out.println("Hash map size is " + testMap.size());
//
//        // find an unknown key
//        System.out.println("Unknown key gives a result = " + testMap.getValue(1000));
//    }

//    @Test
//    public void testBigramOpenHashMap() {
//        Random rand = new Random();
//        BigramOpenHashMap testMap = new BigramOpenHashMap(20);
//        for (int i=0; i<100; i++) {
//            testMap.put(i, i, 0, 0);
//            testMap.incrementEnd(i, 1);
//            testMap.incrementStart(i, 20);//just increment 1
//            testMap.incrementStart(i, 20);//just increment 1
//            testMap.incrementStart(i, 20);//just increment 1
//            testMap.incrementEnd(i, 1);
//            testMap.incrementValue(i, 1);
//            testMap.putEnd(i, 10);
//            testMap.putValue(i, i*2);
//
//        }
//        Iterable<BigramOpenHashMap.Entry> entrySet = testMap.entrySet();
//        System.out.println("Map content: ");
//        for (BigramOpenHashMap.Entry entryKV: entrySet) {
//            System.out.println(entryKV.getKey() + " : " + entryKV.getValue() + " " + entryKV.getEnd() + " " +
//                    entryKV.getStart());
//        }
//
//        System.out.println("Size of hashmap is " + testMap.size());
//    }

    @Test
    public void smallArrayCopyTest() {
        int[] k = new int[20];
        for (int i=0; i<k.length; i++) {
            k[i] = i;
            System.out.print(k[i] + " ");
        }
        System.out.println();

        int[] kk = new int[10];
        int j = 0;
        for (int i=0; i<k.length; i++) {
            if (k[i]%2 != 0) {
                kk[j] = k[i];
                j++;
            }
        }
        k = null;
        System.gc();

        for (int i: kk)
            System.out.print(i + " ");
        System.out.println();
        System.out.println(kk.length);
        System.out.println("Done!");
    }

}
