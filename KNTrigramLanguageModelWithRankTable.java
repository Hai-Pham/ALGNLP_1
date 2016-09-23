package edu.berkeley.nlp.assignments.assign1.student;

import edu.berkeley.nlp.assignments.assign1.student.Utility.*;
import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import edu.berkeley.nlp.langmodel.NgramLanguageModel;
import edu.berkeley.nlp.math.SloppyMath;
import edu.berkeley.nlp.util.StringIndexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gorilla on 9/22/2016.
 * TODO: optimize automatically
 */

public class KNTrigramLanguageModelWithRankTable implements NgramLanguageModel {

    static final String STOP = NgramLanguageModel.STOP;
    static final String START = NgramLanguageModel.START;

    // constant D for smoothing
    static final double D = 0.75;

    // N - made public for small test
    public long totalUnigram = 0;
    public long totalBigram = 0;
    public long totalTrigram = 0;
    // V - made public for small test
    public long unigramVocabSize = 0;
    public long bigramVocabSize = 0;
    public long trigramVocabSize = 0;
    // Counters - made public for small test
    public UnigramOpenHashMap unigramMap = new UnigramOpenHashMap(50000);
    public BigramOpenHashMap bigramMap = new BigramOpenHashMap(100000);
    public TrigramOpenHashMap trigramMap = new TrigramOpenHashMap(1000000);

    public UnigramOpenHashMapWithRank unigramMapWithRank = new UnigramOpenHashMapWithRank(50000);
    public BigramOpenHashMapWithRank bigramMapWithRank = new BigramOpenHashMapWithRank(100000);
    public TrigramOpenHashMapWithRank trigramMapWithRank = new TrigramOpenHashMapWithRank(1000000);

    /**
     * Constructor
     * Declaration about the order of Ngrams
     * E.g. if we have the bigram "w1 w2" then w1 = bigramWord, w2 = word
     * if we have "w1 w2 w3" then w1 = trigramWord, w2 = bigramWord, w3 = word
     * @param sentenceCollection: list of sentences (which is a list of strings)
     */
    public KNTrigramLanguageModelWithRankTable(Iterable<List<String>> sentenceCollection) {

        System.out.println("Building Knesser Ney Trigram Language Model . . .");
        int sent = 0;
        for (List<String> sentence : sentenceCollection) {
            sent++;
            if (sent % 100000 == 0) System.out.println("On sentence " + sent);
            List<String> stoppedSentence = new ArrayList<String>(sentence);
            stoppedSentence.add(0, START);
            stoppedSentence.add(STOP);

            for (int i=0; i<stoppedSentence.size(); i++){
                // process unigram word which is w3 in w1w2w3 trigram
                String w3 = stoppedSentence.get(i);
                int idxW3 = incrementValueUnigramMap(w3);

                if (i == 0) { // unigram, no duplication at the time of 0
                    unigramMap.incrementStart(idxW3, 1);
                } else if (i == 1) { //bigram
                    calculateAtSecondTokenOfSentence(stoppedSentence, i, idxW3);
                } else if (i == stoppedSentence.size()-1) { // </s>
                    calculateAtTheLastTokenOfSentence(stoppedSentence, i, idxW3);
                } else {
                    calculateTokensInTheMiddleOfSentence(stoppedSentence, i, idxW3);
                }
            }
        }
        // consolidating vocabulary for each n-gram
        consolidateStats();
        debugToConsole();
//        optimizeStorage();
        // compare with ones after optimizing
        rebuildNgramMapsWithRanks();
        consolidateStatsAfterCleaning();
        debugToConsoleAfterCleaning();
    }
    // BEGIN - helper methods for constructor
    private void rebuildNgramMapsWithRanks(){
        rebuildUnigramMapWithRanks();
        rebuildBigramMapWithRanks();
        rebuildTrigramMapWithRanks();
        cleanUpAfterRebuildingHashMaps();
        optimizeStorageAfterClearning();
    }
    private void rebuildUnigramMapWithRanks() {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();
        // iterate though unigram maps to get all keys
        // get ranks and update to unigram Map
        Iterable<UnigramOpenHashMap.Entry> unigramEntrySet = unigramMap.entrySet();
        System.out.println("Rebuilding Unigram Map ...");
        for (UnigramOpenHashMap.Entry entryKV : unigramEntrySet) {
            int key = entryKV.getKey();
            int[] values = entryKV.getAllValues();
            int value = values[0];
            int end = values[1];
            int start = values[2];
            int between = values[3];

            short valueRank = indexer.addAndGetIndex(value);
            short endRank = indexer.addAndGetIndex(end);
            short startRank = indexer.addAndGetIndex(start);
            short betweenRank = indexer.addAndGetIndex(between);

            unigramMapWithRank.putRanks(key, valueRank, endRank, startRank, betweenRank);
            //important
            unigramMapWithRank.setTotalBigramEndsWithThis(unigramMap.getTotalBigramEndsWithThis());
        }
    }
    private void rebuildBigramMapWithRanks() {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();
        // iterate though unigram maps to get all keys
        // get ranks and update to unigram Map
        Iterable<BigramOpenHashMap.Entry> bigramEntrySet = bigramMap.entrySet();
        System.out.println("Rebuilding Bigram Map ...");
        for (BigramOpenHashMap.Entry entryKV : bigramEntrySet) {
            long key = entryKV.getKey();
            int[] values = entryKV.getAllValues();
            int value = values[0];
            int end = values[1];
            int start = values[2];

            short valueRank = indexer.addAndGetIndex(value);
            short endRank = indexer.addAndGetIndex(end);
            short startRank = indexer.addAndGetIndex(start);

            bigramMapWithRank.putRanks(key, valueRank, endRank, startRank);
        }
    }
    private void rebuildTrigramMapWithRanks() {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();
        // iterate though unigram maps to get all keys
        // get ranks and update to unigram Map
        Iterable<TrigramOpenHashMap.Entry> trigramEntrySet = trigramMap.entrySet();
        System.out.println("Rebuilding Trigram Map ...");
        for (TrigramOpenHashMap.Entry entryKV : trigramEntrySet) {
            long key = entryKV.getKey();
            int value = entryKV.getValue();

            short valueRank = indexer.addAndGetIndex(value);
            trigramMapWithRank.putRank(key, valueRank);
        }
    }

    private void cleanUpAfterRebuildingHashMaps() {
        System.out.println("Cleaning up old Hash Maps...");
        unigramMap = null;
        bigramMap = null;
        trigramMap = null;
        System.gc();
        System.gc();
        System.out.println("Cleaning is done!");
    }

    private void optimizeStorageAfterClearning() {
        unigramMapWithRank.rehash(0.7);
        bigramMapWithRank.rehash(0.8);
        trigramMapWithRank.rehash(0.7);
    }
    private void calculateTokensInTheMiddleOfSentence(List<String> stoppedSentence, int i, int idxW3) {
        int idxW2 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i-1));
        long indexBigram = Assignment1Utility.bitPackingBigram(idxW2, idxW3);//w2w3
        long indexTrigram = getIndexTrigram(stoppedSentence, i, idxW3, idxW2); //w1w2w3
        incrementEndUnigramMap(idxW3, indexBigram);

        int idxW4 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i+1)); //w4
        long indexBigramFromThis = Assignment1Utility.bitPackingBigram(idxW3, idxW4); //w3w4 = w3.
        incrementStartUnigramMap(idxW3, indexBigramFromThis);

        long trigramStartsFromW2 = Assignment1Utility.bitPackingTrigram(idxW2, idxW3, idxW4); //w2w3w4 = .w3.
        incrementBetweenUnigramMap(idxW3, trigramStartsFromW2);
        incrementValueBigramMap(indexBigram);
        incrementEndBigramMap(indexBigram, indexTrigram);
        incrementStartBigramMap(indexBigram, trigramStartsFromW2);
        incrementValueTrigramMap(indexTrigram);
    }

    private void calculateAtTheLastTokenOfSentence(List<String> stoppedSentence, int i, int idxW3) {
        String w2 = stoppedSentence.get(i-1);
        int idxW2 = EnglishWordIndexer.getIndexer().addAndGetIndex(w2);
        long indexBigram = Assignment1Utility.bitPackingBigram(idxW2, idxW3);
        incrementEndUnigramMap(idxW3, indexBigram);
        incrementValueBigramMap(indexBigram);

        if (i >= 2) {
            long indexTrigram = getIndexTrigram(stoppedSentence, i, idxW3, idxW2);
            incrementEndBigramMap(indexBigram, indexTrigram);
            incrementValueTrigramMap(indexTrigram);
        }
    }

    private void calculateAtSecondTokenOfSentence(List<String> stoppedSentence, int i, int idxW3) {
        int idxW2 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i-1));
        long indexBigram = Assignment1Utility.bitPackingBigram(idxW2, idxW3);
        incrementEndUnigramMap(idxW3, indexBigram);
        incrementValueBigramMap(indexBigram);

        if (i < stoppedSentence.size() - 1) {
            int idxW4 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i+1));
            long bigramStartsFromW3 = Assignment1Utility.bitPackingBigram(idxW3, idxW4); //w3w4
            incrementStartUnigramMap(idxW3, bigramStartsFromW3);

            long trigramStartsFromW2 = Assignment1Utility.bitPackingTrigram(idxW2, idxW3, idxW4);
            incrementBetweenUnigramMap(idxW3, trigramStartsFromW2);
            incrementStartBigramMap(indexBigram, trigramStartsFromW2);
        }
    }

    private long getIndexTrigram(List<String> stoppedSentence, int i, int idxW3, int idxW2) {
        String w1 = stoppedSentence.get(i-2);
        int idxW1 = EnglishWordIndexer.getIndexer().addAndGetIndex(w1);
        return Assignment1Utility.bitPackingTrigram(idxW1, idxW2, idxW3);
    }

    private void incrementBetweenUnigramMap(int idxW3, long trigramStartsFromW2) {
        if (trigramMap.get(trigramStartsFromW2) == 0) {
            unigramMap.incrementBetween(idxW3, 1);
        }
    }

    private void incrementStartUnigramMap(int idxW3, long indexBigramFromThis) {
        if (bigramMap.getValue(indexBigramFromThis) == 0) //w3w4 = w3. dup?
            unigramMap.incrementStart(idxW3, 1);
    }

    private void incrementStartBigramMap(long indexBigram, long trigramStartsFromW2) {
        if (trigramMap.get(trigramStartsFromW2) == 0) // w1w2w3 = w1w2. dup?
            bigramMap.incrementStart(indexBigram, 1);
    }

    private void incrementEndBigramMap(long indexBigram, long indexTrigram) {
        // check duplication before adding
        if (trigramMap.get(indexTrigram) == 0) //w1w2w3 = .w2w3
            bigramMap.incrementEnd(indexBigram, 1);
    }

    private void incrementEndUnigramMap(int idxW3, long indexBigram) {
        if (bigramMap.getValue(indexBigram) == 0 ) //w2w3 = .w3 dup?
            unigramMap.incrementEnd(idxW3, 1);
    }

    private void incrementValueTrigramMap(long indexTrigram) {
        trigramMap.increment(indexTrigram, 1);
        totalTrigram++;
    }

    private void incrementValueBigramMap(long indexBigram) {
        // process bigram with format w1-w2 = bigramWord-unigramWord
        bigramMap.incrementValue(indexBigram, 1);
        totalBigram++;
    }

    private int incrementValueUnigramMap(String w3) {
        int idxW3 = EnglishWordIndexer.getIndexer().addAndGetIndex(w3);
        unigramMap.incrementValue(idxW3, 1);
        totalUnigram++;
        return idxW3;
    }

    private void optimizeStorage() {
        System.out.println("Optimizing storage for hash maps...");
//        unigramMap.optimizeStorage(1.2);
//        bigramMap.optimizeStorage(1.2);
//        trigramMap.optimizeStorage(1.2);
        unigramMap.rehash(0.7);
        bigramMap.rehash(0.8);
        trigramMap.rehash(0.75);

        System.out.println("Optimization complete!");
    }
    private void consolidateStats(){
        unigramVocabSize = unigramMap.size();
        bigramVocabSize = bigramMap.size();
        trigramVocabSize = trigramMap.size();
    }
    private void consolidateStatsAfterCleaning(){
        unigramVocabSize = unigramMapWithRank.size();
        bigramVocabSize = bigramMapWithRank.size();
        trigramVocabSize = trigramMapWithRank.size();
    }
    private void debugToConsole() {
        System.out.println("Done building KNNaiveTrigramLanguageModel.");
        System.out.println("-------------STATS------------");
        System.out.println("Number of unigram vocab=" + unigramVocabSize + " and total unigram=" + totalUnigram + " and actual size=" + unigramMap.actualSize());
        System.out.println("Number of total BigramEndsWith a word is " + unigramMap.getTotalBigramEndsWithThis());
        System.out.println("Number of bigram vocab=" + bigramVocabSize + " and total bigram=" + totalBigram + " and actual size=" + bigramMap.actualSize());
        System.out.println("Number of trigram vocab=" + trigramVocabSize + " and total trigram=" + totalTrigram + " and actual size=" + trigramMap.actualSize());
        System.out.println("-------------END OF STATS------------");
    }
    private void debugToConsoleAfterCleaning() {
        System.out.println("After Cleaning up For KNNaiveTrigramLanguageModel.");
        System.out.println("-------------STATS------------");
        System.out.println("Number of unigram vocab=" + unigramVocabSize + " and total unigram=" + totalUnigram + " and actual size=" + unigramMapWithRank.actualSize());
        System.out.println("Number of total BigramEndsWith a word is " + unigramMapWithRank.getTotalBigramEndsWithThis());
        System.out.println("Number of bigram vocab=" + bigramVocabSize + " and total bigram=" + totalBigram + " and actual size=" + bigramMapWithRank.actualSize());
        System.out.println("Number of trigram vocab=" + trigramVocabSize + " and total trigram=" + totalTrigram + " and actual size=" + trigramMapWithRank.actualSize());
        System.out.println("-------------END OF STATS------------");
    }
    // END - helper methods for constructor

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public long getCount(int[] ngram) {
        //interpreter
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();

        if ((ngram.length > 3) || (ngram.length < 1))
            return 0;

        if (ngram.length == 1) {
            int key = ngram[0];
            return indexer.get(unigramMapWithRank.getValueRank(key));
        }
        if (ngram.length == 2) {
            // encode
            int idxBigramWord = ngram[0];
            int idxUnigramWord = ngram[1];
            long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxBigramWord, idxUnigramWord);
            return indexer.get(bigramMapWithRank.getValueRank(bigramBitPacking));
        }

        // encode
        final int idxTrigramWord = ngram[0];
        final int idxBigramWord = ngram[1];
        final int idxUnigramWord = ngram[2];
        long trigramBitPacking = Assignment1Utility.bitPackingTrigram(idxTrigramWord, idxBigramWord, idxUnigramWord);
        return indexer.get(trigramMapWithRank.getValueRank(trigramBitPacking));
    }

    @Override
    public double getNgramLogProbability(int[] ngram, int from, int to) {
        // switch among various methods of smoothing to investigate
        return Math.log(knScoreGeneral(ngram, from, to) + 1e-20);
    }

    public double knScoreGeneral(int[] ngram, int from, int to) {
        int order = to - from;
        if (order == 1)
            return knScoreUnigram(ngram, to);
        if (order == 2)
            return knScoreBigram(ngram, to);

        return knScoreTrigram(ngram, to);
    }

    private double knScoreUnigram(int[] ngram, int to) {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();
        int count = indexer.get(unigramMapWithRank.getBigramEndsWithThisRank(ngram[to-1])); //w3

        if (count == 0L)
            return 1e-20;
        else {
            int numerator = indexer.get(unigramMapWithRank.getBigramEndsWithThisRank(ngram[to-1]));
            return numerator / (double) unigramMapWithRank.getTotalBigramEndsWithThis();
        }
    }
    private double knScoreBigram(int[] ngram, int to) {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();

        // count denominator to back-off in edge case
        int countInBetweenW2 = indexer.get(unigramMapWithRank.getBigramWithThisInBetweenRank(ngram[to-2])); // count(.w2.)
        if (countInBetweenW2 == 0L) {
            return knScoreUnigram(ngram, to); // complete back-off
        }

        double bigramKNScore = 0.0;

        //encode bigram (as a continuation) for numerator W2W3
        long bigramBitPacking = Assignment1Utility.bitPackingBigram(ngram[to-2], ngram[to-1]);
        double numerator = SloppyMath.max((double)indexer.get(bigramMapWithRank.gettrigramEndsWithThisRank(bigramBitPacking)) - D, 0.0);
        bigramKNScore += numerator / (double) countInBetweenW2;
        bigramKNScore += calculateAlphaUnigram(ngram[to-2]) * knScoreUnigram(ngram, to);

        return bigramKNScore;
    }
    private double knScoreTrigram(int[] ngram, int to) {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();

        // edge case -> back off
        long bigramBitPacking = Assignment1Utility.bitPackingBigram(ngram[to-3], ngram[to-2]); //w1w2
        int countW1W2 = indexer.get(bigramMapWithRank.getValueRank(bigramBitPacking));
        if (countW1W2 == 0L)
            return knScoreBigram(ngram, to); // complete back off

        double trigramKNScore = 0.0;
        //encode trigram
        long trigramBitPacking = Assignment1Utility.bitPackingTrigram(ngram[to-3], ngram[to-2], ngram[to-1]);
        double numerator = SloppyMath.max((double)indexer.get(trigramMapWithRank.getValueRank(trigramBitPacking)) - D, 0.0);
        trigramKNScore += numerator / (double) countW1W2;
        trigramKNScore += calculateAlphaBigram(ngram[to-3], ngram[to-2]) * knScoreBigram(ngram, to);
        return trigramKNScore;
    }
    // alpha(w2)
    private double calculateAlphaUnigram(int idxW2) {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();
        double alpha = D;
        alpha *= indexer.get(unigramMapWithRank.getBigramStartsWithThisRank(idxW2)); //fertility
        alpha /= (double) indexer.get(unigramMapWithRank.getBigramWithThisInBetweenRank(idxW2));
        return alpha;
    }
    // alpha(w1w2)
    private double calculateAlphaBigram(int idxW1, int idxW2) {
        RankIndexerInteger indexer = TrigramCountIndexer.getIndexer();
        double alpha = D;
        //encode w1w2
        long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxW1, idxW2);
        alpha *= indexer.get(bigramMapWithRank.gettrigramStartsWithThisRank(bigramBitPacking)); //fertility
        alpha /= (double) indexer.get(bigramMapWithRank.getValueRank(bigramBitPacking));
        return alpha;
    }

}
