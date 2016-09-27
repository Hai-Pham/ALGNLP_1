package edu.berkeley.nlp.assignments.assign1.student.Test;

import edu.berkeley.nlp.assignments.assign1.student.KNTrigramLanguageModel;
import edu.berkeley.nlp.assignments.assign1.student.Utility.*;
import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gorilla on 9/15/2016.
 */
public class KNLanguageModelTest {
/*    @Test
    public void NaiveTrigramLanguageModelLogProbabilityWith_ADD_ONE_SMOOTHING_Test() {
        System.out.println("Test the log probability of Naive Trigram Language Model");
        ArrayList<List<String>> sentences = new ArrayList<>();
        String s1 = "a b c a b c";
        List<String> sentence1 = new ArrayList<>(Arrays.asList(s1.split(" ")));
        String s2 = "b c d e f a b c";
        List<String> sentence2 = new ArrayList<>(Arrays.asList(s2.split(" ")));
        sentences.add(sentence1);
        sentences.add(sentence2);

        NaiveTrigramLanguageModel languageModel = new NaiveTrigramLanguageModel(sentences);


        // test
        int[] ngram = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 0, 2, 3, 5, 7, 1, 3, 1};
        System.out.println(languageModel.getNgramLogProbability(ngram, 0, 1));
        assertEquals(languageModel.getNgramLogProbability(ngram, 0, 1), Math.log(3 / 26.0), 0.001);
        System.out.println(languageModel.getNgramLogProbability(ngram, 0, 2));
        assertEquals(languageModel.getNgramLogProbability(ngram, 0, 2), Math.log(2 / 10.0), 0.001);
        System.out.println(languageModel.getNgramLogProbability(ngram, 2, 3));
        assertEquals(languageModel.getNgramLogProbability(ngram, 2, 3), Math.log(5 / 26.0), 0.001);
        System.out.println(languageModel.getNgramLogProbability(ngram, 2, 5));
        assertEquals(languageModel.getNgramLogProbability(ngram, 2, 5), Math.log(3 / 12.0), 0.001);
        System.out.println(languageModel.getNgramLogProbability(ngram, 6, 7));
        assertEquals(languageModel.getNgramLogProbability(ngram, 6, 8), Math.log(2/9.0), 0.001);
        System.out.println(languageModel.getNgramLogProbability(ngram, 0, 3));
        assertEquals(languageModel.getNgramLogProbability(ngram, 0, 3), Math.log(2/9.0), 0.001);
    }*/

/*    @Test
    public void NaiveTrigramLanguageModelGetCountTest(){
        System.out.println("Test the log probability of Naive Trigram Language Model");
        ArrayList<List<String>> sentences = new ArrayList<>();
        String s1 = "a b c a b c";
        List<String> sentence1 = new ArrayList<>(Arrays.asList(s1.split(" ")));
        String s2 = "b c d e f a b c";
        List<String> sentence2 = new ArrayList<>(Arrays.asList(s2.split(" ")));
        sentences.add(sentence1);
        sentences.add(sentence2);

        NaiveTrigramLanguageModel languageModel = new NaiveTrigramLanguageModel(sentences);


        int[] ngram = new int[] {1, 2, 3};
        assertEquals(3, languageModel.getCount(ngram));
        int[] ngram2 = new int[] {0, 1, 2};
        assertEquals(1, languageModel.getCount(ngram2));

        // validate the size of indexer (mapping word -> int)
        //static object
        StringIndexer indexer = EnglishWordIndexer.getIndexer();
        System.out.println("Size of indexer = " + indexer.size());
    }*/

 /*   @Test
    public void spotCheckContextNormalizes() {
        System.out.println("Test the log probability of Naive Trigram Language Model");
        ArrayList<List<String>> sentences = new ArrayList<>();
        String s1 = "a b c a b c";
        List<String> sentence1 = new ArrayList<>(Arrays.asList(s1.split(" ")));
        String s2 = "b c d e f a b c";
        List<String> sentence2 = new ArrayList<>(Arrays.asList(s2.split(" ")));
        sentences.add(sentence1);
        sentences.add(sentence2);

//        NaiveTrigramLanguageModel languageModel = new NaiveTrigramLanguageModel(sentences);
//        String[] rawContext = new String[] {"b", "c"};

        EmpiricalUnigramLanguageModel languageModel = new EmpiricalUnigramLanguageModel(sentences);
        String[] rawContext = new String[] {" "};


        StringIndexer indexer = EnglishWordIndexer.getIndexer();
        int[] context = index(rawContext);
        int[] ngram = new int[context.length + 1];
        for (int i = 0; i < context.length; i++) {
            ngram[i] = context[i];
        }
        double totalLogProb = Double.NEGATIVE_INFINITY;
        for (int wordIdx = 0; wordIdx < indexer.size(); wordIdx++) {
            // Don't include the START symbol since it is only observed in contexts
            if (wordIdx != indexer.indexOf(NgramLanguageModel.START)) {
                ngram[ngram.length - 1] = wordIdx;
                totalLogProb = SloppyMath.logAdd(totalLogProb, languageModel.getNgramLogProbability(ngram, 0, ngram.length));
            }
        }
        if (Math.abs(totalLogProb) > 0.001) {
            System.out.println("WARNING: Distribution for context " + Arrays.toString(rawContext) + " does not normalize correctly, sums to " + Math.exp(totalLogProb));
        } else {
            System.out.println("Distribution for context " + Arrays.toString(rawContext) + " normalizes correctly, sums to " + Math.exp(totalLogProb));
        }
    }

    private static int[] index(String[] arr) {
        int[] indexedArr = new int[arr.length];
        for (int i = 0; i < indexedArr.length; i++) {
            indexedArr[i] = EnglishWordIndexer.getIndexer().addAndGetIndex(arr[i]);
        }
        return indexedArr;
    }*/


/*    @Test
    public void smallStringTest() {
        String s = "";
        if (! (s == ""))
            System.out.println("S is not null");
        else
            System.out.println("s is null");

        String ss = "abc";
        String sss = ss;
        ss = "def";

        System.out.println("Test shallow or deep copy: " + ss + " " + sss);
        //assert deep copy
        assertEquals((ss==sss), false);
    }*/


    private ArrayList<List<String>> prepareSmallCorpus() {
        ArrayList<List<String>> sentences = new ArrayList<>();
        String s1 = "a b c a b c";
        List<String> sentence1 = new ArrayList<>(Arrays.asList(s1.split(" ")));
        String s2 = "b c d e f a b c";
        List<String> sentence2 = new ArrayList<>(Arrays.asList(s2.split(" ")));
        sentences.add(sentence1);
        sentences.add(sentence2);
        return sentences;
    }
//    @Test
//    public void KNNaiveTrigramLanguageModelCONSTRUCTORTest(){
//        ArrayList<List<String>> sentences = prepareSmallCorpus();
//        KNNaiveTrigramLanguageModel languageModel = new KNNaiveTrigramLanguageModel(sentences);
//
//        assertEquals(languageModel.unigramVocabSize, 8);
//        assertEquals(languageModel.totalUnigram, 18);
//        assertEquals(languageModel.totalBigram, 16);
//        assertEquals(languageModel.totalTrigram, 14);
//
//        Iterable<TIntOpenHashMap.Entry<Integer>> unigramEntrySet = languageModel.unigramMap.entrySet();
//        System.out.println("Unigram Map: ");
//        for (TIntOpenHashMap.Entry<Integer> entryKV: unigramEntrySet) {
//            System.out.println(entryKV.getKey() + "--" + entryKV.getValue());
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//        Iterable<TIntOpenHashMap.Entry<Long>> bigramEntrySet = languageModel.bigramMap.entrySet();
//        System.out.println("Bigram Map: ");
//        for (TIntOpenHashMap.Entry<Long> entryKV: bigramEntrySet) {
//            // Decode
//            int[] bigram = Assignment1Utility.bigramBitPackingDecode(entryKV.getKey());
//            int idxBigram = bigram[0];
//            int idxUnigram = bigram[1];
//            System.out.println(EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
//        }
//        Iterable<TIntOpenHashMap.Entry<Long>> trigramEntrySet = languageModel.trigramMap.entrySet();
//        System.out.println("Trigram Map: ");
//        for (TIntOpenHashMap.Entry<Long> entryKV: trigramEntrySet) {
//            // Decode
//            int[] trigram = Assignment1Utility.trigramBitPackingDecode(entryKV.getKey());
//            int idxTrigram = trigram[0];
//            int idxBigram = trigram[1];
//            int idxUnigram = trigram[2];
//            System.out.println(EnglishWordIndexer.getIndexer().get(idxTrigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
//        }
//        Iterable<TIntOpenHashMap.Entry<Integer>> bigramStartsWithCountEntrySet = languageModel.bigramStartsWithCount.entrySet();
//        System.out.println("Bigram Starts with Count Map: ");
//        for (TIntOpenHashMap.Entry<Integer> entryKV: bigramStartsWithCountEntrySet) {
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//        Iterable<TIntOpenHashMap.Entry<Integer>> bigramEndsWithCountEntrySet = languageModel.bigramEndsWithCount.entrySet();
//        System.out.println("Bigram Ends with Count Map: ");
//        for (TIntOpenHashMap.Entry<Integer> entryKV: bigramEndsWithCountEntrySet) {
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//        Iterable<TIntOpenHashMap.Entry<Long>> trigramStartsWithCountEntrySet = languageModel.trigramStartsWithCount.entrySet();
//        System.out.println("Trigram Starts with Count Map: ");
//        for (TIntOpenHashMap.Entry<Long> entryKV: trigramStartsWithCountEntrySet) {
//            // Decode
//            int[] bigram = Assignment1Utility.bigramBitPackingDecode(entryKV.getKey());
//            int idxBigram = bigram[0];
//            int idxUnigram = bigram[1];
//            System.out.println(EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
//
//        }
//        Iterable<TIntOpenHashMap.Entry<Integer>> trigramEndsWithCountEntrySet = languageModel.trigramEndsWithCount.entrySet();
//        System.out.println("Trigram Ends with Count Map: ");
//        for (TIntOpenHashMap.Entry<Integer> entryKV: trigramEndsWithCountEntrySet) {
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//    }

//    @Test
//    public void KNNaiveTrigramLanguageModelCOUNTTest() {
//        ArrayList<List<String>> sentences = prepareSmallCorpus();
//        KNNaiveTrigramLanguageModel languageModel = new KNNaiveTrigramLanguageModel(sentences);
//
//        // count unigram
//        assertEquals(languageModel.getCount(new int[]{1}), 3); // a
//        assertEquals(languageModel.getCount(new int[]{0}), 2); // <s>
//        assertEquals(languageModel.getCount(new int[]{3}), 4); // c
//
//        // count bigram
//        assertEquals(languageModel.getCount(new int[]{0, 1}), 1); // <s> a
//        assertEquals(languageModel.getCount(new int[]{1, 2}), 3); // a b
//        assertEquals(languageModel.getCount(new int[]{2, 3}), 4); // b c
//
//        // count trigram
//        assertEquals(languageModel.getCount(new int[]{0, 1, 2}), 1); // <s> a b
//        assertEquals(languageModel.getCount(new int[]{1, 2, 3}), 3); // a b c
//        assertEquals(languageModel.getCount(new int[]{2, 3, 4}), 2); // b c d
//
//        // edge cases
//        assertEquals(languageModel.getCount(new int[]{}), 0);
//        assertEquals(languageModel.getCount(new int[]{1, 2, 3, 4}), 0);
//    }
//
//    @Test
//    public void KNNaiveTrigramLanguageModelLOG_PROBABILITYTest() {
//        ArrayList<List<String>> sentences = prepareSmallCorpus();
//        KNNaiveTrigramLanguageModel languageModel = new KNNaiveTrigramLanguageModel(sentences);
//
//        System.out.println("Testing LOG PROBABILITY BEGINS");
//        // base case: unigram
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 0, 1));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 1, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{2, 3, 4}, 1, 2));
//
//        // bigram case
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 0, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{1, 2, 3, 4, 5}, 0, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{5, 4, 3, 2, 1}, 1, 3));
//
//        //trigram case
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2, 3, 4, 5}, 0, 3));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{1, 2, 3, 4, 5}, 0, 3));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{5, 4, 3, 2, 1}, 1, 4));
//        System.out.println("Testing LOG PROBABILITY ENDS");
//    }

//    @Test
//    public void KNTrigramLanguageModelCONSTRUCTORTest(){
//        ArrayList<List<String>> sentences = prepareSmallCorpus();
//        KNTrigramLanguageModel languageModel = new KNTrigramLanguageModel(sentences);
//
////        assertEquals(languageModel.unigramVocabSize, 8);
//        assertEquals(languageModel.totalUnigram, 18);
//        assertEquals(languageModel.totalBigram, 16);
//        assertEquals(languageModel.totalTrigram, 14);
//
//        Iterable<IntIntOpenHashMap.Entry> unigramEntrySet = languageModel.unigramMap.entrySet();
//        System.out.println("Unigram Map: ");
//        for (IntIntOpenHashMap.Entry entryKV: unigramEntrySet) {
//            System.out.println(entryKV.getKey() + ":" + entryKV.getValue());
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//        Iterable<LongIntOpenHashMap.Entry> bigramEntrySet = languageModel.bigramMap.entrySet();
//        System.out.println("Bigram Map: ");
//        for (LongIntOpenHashMap.Entry entryKV: bigramEntrySet) {
//            // Decode
//            int[] bigram = Assignment1Utility.bigramBitPackingDecode(entryKV.getKey());
//            int idxBigram = bigram[0];
//            int idxUnigram = bigram[1];
//            System.out.println(EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
//        }
//        Iterable<LongIntOpenHashMap.Entry> trigramEntrySet = languageModel.trigramMap.entrySet();
//        System.out.println("Trigram Map: ");
//        for (LongIntOpenHashMap.Entry entryKV: trigramEntrySet) {
//            // Decode
//            int[] trigram = Assignment1Utility.trigramBitPackingDecode(entryKV.getKey());
//            int idxTrigram = trigram[0];
//            int idxBigram = trigram[1];
//            int idxUnigram = trigram[2];
//            System.out.println(EnglishWordIndexer.getIndexer().get(idxTrigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
//        }
//        Iterable<IntIntOpenHashMap.Entry> bigramStartsWithCountEntrySet = languageModel.bigramStartsWithCount.entrySet();
//        System.out.println("Bigram Starts with Count Map: ");
//        for (IntIntOpenHashMap.Entry entryKV: bigramStartsWithCountEntrySet) {
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//        Iterable<IntIntOpenHashMap.Entry> bigramEndsWithCountEntrySet = languageModel.bigramEndsWithCount.entrySet();
//        System.out.println("Bigram Ends with Count Map: ");
//        for (IntIntOpenHashMap.Entry entryKV: bigramEndsWithCountEntrySet) {
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//        Iterable<LongIntOpenHashMap.Entry> trigramStartsWithCountEntrySet = languageModel.trigramStartsWithCount.entrySet();
//        System.out.println("Trigram Starts with Count Map: ");
//        for (LongIntOpenHashMap.Entry entryKV: trigramStartsWithCountEntrySet) {
//            // Decode
//            int[] bigram = Assignment1Utility.bigramBitPackingDecode(entryKV.getKey());
//            int idxBigram = bigram[0];
//            int idxUnigram = bigram[1];
//            System.out.println(EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
//                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
//
//        }
//        Iterable<IntIntOpenHashMap.Entry> trigramEndsWithCountEntrySet = languageModel.trigramEndsWithCount.entrySet();
//        System.out.println("Trigram Ends with Count Map: ");
//        for (IntIntOpenHashMap.Entry entryKV: trigramEndsWithCountEntrySet) {
//            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue());
//        }
//    }
//
//    @Test
//    public void KNNaiveTrigramLanguageModelCOUNTTest() {
//        ArrayList<List<String>> sentences = prepareSmallCorpus();
//        KNNaiveTrigramLanguageModel languageModel = new KNNaiveTrigramLanguageModel(sentences);
//
//        // count unigram
//        assertEquals(languageModel.getCount(new int[]{1}), 3); // a
//        assertEquals(languageModel.getCount(new int[]{0}), 2); // <s>
//        assertEquals(languageModel.getCount(new int[]{3}), 4); // c
//
//        // count bigram
//        assertEquals(languageModel.getCount(new int[]{0, 1}), 1); // <s> a
//        assertEquals(languageModel.getCount(new int[]{1, 2}), 3); // a b
//        assertEquals(languageModel.getCount(new int[]{2, 3}), 4); // b c
//
//        // count trigram
//        assertEquals(languageModel.getCount(new int[]{0, 1, 2}), 1); // <s> a b
//        assertEquals(languageModel.getCount(new int[]{1, 2, 3}), 3); // a b c
//        assertEquals(languageModel.getCount(new int[]{2, 3, 4}), 2); // b c d
//
//        // edge cases
//        assertEquals(languageModel.getCount(new int[]{}), 0);
//        assertEquals(languageModel.getCount(new int[]{1, 2, 3, 4}), 0);
//    }
//
//    @Test
//    public void KNNaiveTrigramLanguageModelLOG_PROBABILITYTest() {
//        ArrayList<List<String>> sentences = prepareSmallCorpus();
//        KNNaiveTrigramLanguageModel languageModel = new KNNaiveTrigramLanguageModel(sentences);
//
//        System.out.println("Testing LOG PROBABILITY BEGINS");
//        // base case: unigram
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 0, 1));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 1, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{2, 3, 4}, 1, 2));
//
//        // bigram case
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 0, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{1, 2, 3, 4, 5}, 0, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{5, 4, 3, 2, 1}, 1, 3));
//
//        //trigram case
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2, 3, 4, 5}, 0, 3));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{1, 2, 3, 4, 5}, 0, 3));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{5, 4, 3, 2, 1}, 1, 4));
//        System.out.println("Testing LOG PROBABILITY ENDS");
//    }

    @Test
    public void KNTrigramLanguageModel2CONSTRUCTORTest(){
        ArrayList<List<String>> sentences = prepareSmallCorpus();
        KNTrigramLanguageModel languageModel = new KNTrigramLanguageModel(sentences);

//        assertEquals(languageModel.unigramVocabSize, 8);
//        assertEquals(languageModel.bigramVocabSize, 10);
//        assertEquals(languageModel.trigramVocabSize, 11);
//        assertEquals(languageModel.totalUnigram, 18);
//        assertEquals(languageModel.totalBigram, 16);
//        assertEquals(languageModel.totalTrigram, 14);

        Iterable<UnigramOpenHashMap.Entry> unigramEntrySet = languageModel.unigramMap.entrySet();
        System.out.println("Unigram Map: ");
        for (UnigramOpenHashMap.Entry entryKV: unigramEntrySet) {
            System.out.println(entryKV.getKey() + ":" + entryKV.getValue());
            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue() +
                    "==" + entryKV.getEnd() + ":" + entryKV.getStart() +  ":" + entryKV.getBetween());
        }
        Iterable<BigramOpenHashMap.Entry> bigramEntrySet = languageModel.bigramMap.entrySet();
        System.out.println("Bigram Map: ");
        for (BigramOpenHashMap.Entry entryKV: bigramEntrySet) {
            // Decode
            int[] bigram = Assignment1Utility.bigramBitPackingDecode(entryKV.getKey());
            int idxBigram = bigram[0];
            int idxUnigram = bigram[1];
            System.out.println(EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue() +
                    "==" + entryKV.getEnd() + ":" + entryKV.getStart());
        }
        Iterable<TrigramOpenHashMap.Entry> trigramEntrySet = languageModel.trigramMap.entrySet();
        System.out.println("Trigram Map: ");
        for (TrigramOpenHashMap.Entry entryKV: trigramEntrySet) {
            // Decode
            int[] trigram = Assignment1Utility.trigramBitPackingDecode(entryKV.getKey());
            int idxTrigram = trigram[0];
            int idxBigram = trigram[1];
            int idxUnigram = trigram[2];
            System.out.println(EnglishWordIndexer.getIndexer().get(idxTrigram) + "+" +
                    EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue());
        }
    }

    @Test
    public void KNNaiveTrigramLanguageModelCOUNTTest() {
        ArrayList<List<String>> sentences = prepareSmallCorpus();
        KNTrigramLanguageModel languageModel = new KNTrigramLanguageModel(sentences);

        // count unigram
        assertEquals(languageModel.getCount(new int[]{1}), 3); // a
        assertEquals(languageModel.getCount(new int[]{0}), 2); // <s>
        assertEquals(languageModel.getCount(new int[]{3}), 4); // c

        // count bigram
        assertEquals(languageModel.getCount(new int[]{0, 1}), 1); // <s> a
        assertEquals(languageModel.getCount(new int[]{1, 2}), 3); // a b
        assertEquals(languageModel.getCount(new int[]{2, 3}), 4); // b c

        // count trigram
        assertEquals(languageModel.getCount(new int[]{0, 1, 2}), 1); // <s> a b
        assertEquals(languageModel.getCount(new int[]{1, 2, 3}), 3); // a b c
        assertEquals(languageModel.getCount(new int[]{2, 3, 4}), 2); // b c d

        // edge cases
        assertEquals(languageModel.getCount(new int[]{}), 0);
        assertEquals(languageModel.getCount(new int[]{1, 2, 3, 4}), 0);
    }

    @Test
    public void KNNaiveTrigramLanguageModelLOG_PROBABILITYTest() {
        ArrayList<List<String>> sentences = prepareSmallCorpus();
        KNTrigramLanguageModel languageModel = new KNTrigramLanguageModel(sentences);

        System.out.println("Testing LOG PROBABILITY BEGINS");
        int[] unigramArray = new int[] {0, 1, 2, 3, 4, 5, 6, 7};

        System.out.println("unigram LOG");
        System.out.println(languageModel.knScoreGeneral(unigramArray, 0, 1));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 1, 2));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 2, 3));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 3, 4));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 4, 5));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 5, 6));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 6, 7));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 7, 8));

        System.out.println("bigram LOG");
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 0}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 1}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 2}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 3}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 4}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 5}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 6}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 7}, 0, 2));

        System.out.println("triigram LOG");
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 0}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 1}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 2}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 3}, 0, 3)); // unsmoothing = 1
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 4}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 5}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 6}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 7}, 0, 3));

        System.out.println("LOG SCORE");
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 0, 1));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 1, 2));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 2, 3));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 3, 4));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 4, 5));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 5, 6));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 6, 7));
        System.out.println(languageModel.getNgramLogProbability(unigramArray, 7, 8));

        System.out.println();
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 0}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 1}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 2}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 3}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 4}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 5}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 6}, 0, 2));
        System.out.println(languageModel.getNgramLogProbability(new int[] {3, 7}, 0, 2));

        System.out.println();
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 0}, 0, 3));
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 1}, 0, 3));
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 2}, 0, 3));
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 3}, 0, 3)); // unsmoothing = 1
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 4}, 0, 3));
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 5}, 0, 3));
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 6}, 0, 3));
        System.out.println(languageModel.getNgramLogProbability(new int[] {1, 2, 7}, 0, 3));

//        // base case: unigram
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 0, 1));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 1, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{2, 3, 4}, 1, 2));
//
//        // bigram case
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2}, 0, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{1, 2, 3, 4, 5}, 0, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{5, 4, 3, 2, 1}, 1, 3));
//
//        //trigram case
//        System.out.println(languageModel.getNgramLogProbability(new int[]{0, 1, 2, 3, 4, 5}, 1, 4));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{1, 2, 3, 4, 5}, 0, 3));
//        System.out.println(languageModel.getNgramLogProbability(new int[]{5, 4, 3, 2, 1}, 1, 4));
//        System.out.println("Testing LOG PROBABILITY ENDS");
    }
}
