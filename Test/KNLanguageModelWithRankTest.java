package edu.berkeley.nlp.assignments.assign1.student.Test;

import edu.berkeley.nlp.assignments.assign1.student.KNTrigramLanguageModel;
import edu.berkeley.nlp.assignments.assign1.student.KNTrigramLanguageModelWithRankTable;
import edu.berkeley.nlp.assignments.assign1.student.Utility.*;
import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorilla on 9/15/2016.
 */
public class KNLanguageModelWithRankTest {
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

    @Test
    public void KNTrigramLanguageModelWithRankCONSTRUCTORTest(){
        ArrayList<List<String>> sentences = prepareSmallCorpus();
        KNTrigramLanguageModelWithRankTable languageModel = new KNTrigramLanguageModelWithRankTable(sentences);

        assertEquals(languageModel.unigramVocabSize, 8);
        assertEquals(languageModel.bigramVocabSize, 10);
        assertEquals(languageModel.trigramVocabSize, 11);
        assertEquals(languageModel.totalUnigram, 18);
        assertEquals(languageModel.totalBigram, 16);
        assertEquals(languageModel.totalTrigram, 14);

        Iterable<UnigramOpenHashMapWithRank.Entry> unigramEntrySet = languageModel.unigramMapWithRank.entrySet();
        System.out.println("Unigram Map: ");
        for (UnigramOpenHashMapWithRank.Entry entryKV: unigramEntrySet) {
            System.out.println(entryKV.getKey() + ":" + entryKV.getValue());
            System.out.println(EnglishWordIndexer.getIndexer().get(entryKV.getKey()) + ":" + entryKV.getValue() +
                    "==" + entryKV.getEnd() + ":" + entryKV.getStart() +  ":" + entryKV.getBetween());
        }
        Iterable<BigramOpenHashMapWithRank.Entry> bigramEntrySet = languageModel.bigramMapWithRank.entrySet();
        System.out.println("Bigram Map: ");
        for (BigramOpenHashMapWithRank.Entry entryKV: bigramEntrySet) {
            // Decode
            int[] bigram = Assignment1Utility.bigramBitPackingDecode(entryKV.getKey());
            int idxBigram = bigram[0];
            int idxUnigram = bigram[1];
            System.out.println(EnglishWordIndexer.getIndexer().get(idxBigram) + "+" +
                    EnglishWordIndexer.getIndexer().get(idxUnigram) + ":" + entryKV.getValue() +
                    "==" + entryKV.getEnd() + ":" + entryKV.getStart());
        }
        Iterable<TrigramOpenHashMapWithRank.Entry> trigramEntrySet = languageModel.trigramMapWithRank.entrySet();
        System.out.println("Trigram Map: ");
        for (TrigramOpenHashMapWithRank.Entry entryKV: trigramEntrySet) {
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
    public void KNNaiveTrigramLanguageModelWithRankCOUNTTest() {
        ArrayList<List<String>> sentences = prepareSmallCorpus();
        KNTrigramLanguageModelWithRankTable languageModel = new KNTrigramLanguageModelWithRankTable(sentences);

        // count unigram
        assertEquals(languageModel.getCount(new int[]{1}), 3); // a
        assertEquals(languageModel.getCount(new int[]{0}), 2); // <s>
        assertEquals(languageModel.getCount(new int[]{3}), 4); // c
        assertEquals(languageModel.getCount(new int[]{99}), 0); // a

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
        KNTrigramLanguageModelWithRankTable languageModel = new KNTrigramLanguageModelWithRankTable(sentences);

        System.out.println("Testing LOG PROBABILITY BEGINS");
        int[] unigramArray = new int[] {0, 1, 2, 3, 4, 5, 6, 7};

        System.out.println(languageModel.knScoreGeneral(unigramArray, 0, 1));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 1, 2));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 2, 3));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 3, 4));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 4, 5));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 5, 6));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 6, 7));
        System.out.println(languageModel.knScoreGeneral(unigramArray, 7, 8));

        System.out.println(languageModel.knScoreGeneral(new int[] {3, 0}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 1}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 2}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 3}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 4}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 5}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 6}, 0, 2));
        System.out.println(languageModel.knScoreGeneral(new int[] {3, 7}, 0, 2));

        System.out.println();
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 0}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 1}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 2}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 3}, 0, 3)); // unsmoothing = 1
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 4}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 5}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 6}, 0, 3));
        System.out.println(languageModel.knScoreGeneral(new int[] {1, 2, 7}, 0, 3));
    }
}

