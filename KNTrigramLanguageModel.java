package edu.berkeley.nlp.assignments.assign1.student;

/**
 * Created by Gorilla on 9/17/2016.
 */

import edu.berkeley.nlp.assignments.assign1.student.Utility.Assignment1Utility;
import edu.berkeley.nlp.assignments.assign1.student.Utility.IntIntOpenHashMap;
import edu.berkeley.nlp.assignments.assign1.student.Utility.LongIntOpenHashMap;
import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import edu.berkeley.nlp.langmodel.NgramLanguageModel;
import edu.berkeley.nlp.math.SloppyMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive Implementation of Trigram with KN Smoothing
 *
 * For 3 ngrams, there are 3 hashmaps<int, int>. The mapping from word(string) -> int is provided by StringIndexer util
 * Note: this one use the TIntOpenHashMap but not having ranking table
 */
public class KNTrigramLanguageModel implements NgramLanguageModel {

    static final String STOP = NgramLanguageModel.STOP;
    static final String START = NgramLanguageModel.START;
    // N
    static long totalUnigram = 0;
    static long totalBigram = 0;
    static long totalTrigram = 0;
    // V
    long unigramVocabSize = 0;
    long bigramVocabSize = 0;
    long trigramVocabSize = 0;
    // Counters
    IntIntOpenHashMap unigramMap = new IntIntOpenHashMap(100000);
    LongIntOpenHashMap bigramMap = new LongIntOpenHashMap(1000000);
    LongIntOpenHashMap trigramMap = new LongIntOpenHashMap(10000000);

    // Counters for KN Smoothing
    IntIntOpenHashMap bigramStartsWithCount = new IntIntOpenHashMap(100000);
    IntIntOpenHashMap bigramEndsWithCount = new IntIntOpenHashMap(100000);
    LongIntOpenHashMap trigramStartsWithCount = new LongIntOpenHashMap(100000); // trigram starts with w3w2
    IntIntOpenHashMap trigramEndsWithCount = new IntIntOpenHashMap(100000); // w1w2w3 ends with w2w3 TODO: edit this
    /**
     * Constructor
     * Declaration about the order of Ngrams
     * E.g. if we have the bigram "w1 w2" then w1 = bigramWord, w2 = word
     * if we have "w1 w2 w3" then w1 = trigramWord, w2 = bigramWord, w3 = word
     * @param sentenceCollection: list of sentences (which is a list of strings)
     */
    public KNTrigramLanguageModel(Iterable<List<String>> sentenceCollection) {

            System.out.println("Building KNTrigramLanguageModel . . .");
            int sent = 0;
            for (List<String> sentence : sentenceCollection) {
                sent++;
                if (sent % 1000000 == 0) System.out.println("On sentence " + sent);
                List<String> stoppedSentence = new ArrayList<String>(sentence);
                stoppedSentence.add(0, START);
                stoppedSentence.add(STOP);

                for (int i=0; i<stoppedSentence.size(); i++){
                    //update stats
                    totalUnigram++;
                    // process unigram
                    String unigramWord = stoppedSentence.get(i);
                    int indexUnigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(unigramWord);
//                    System.out.println("PROCESSING " + unigramWord + " with index = " + indexUnigramWord); // DEBUG
                    unigramMap.increment(indexUnigramWord, 1);

                    if (i == 0) continue;
                    else if (i == 1) {
                        // process bigram with format w1-w2 = bigramWord-unigramWord
                        String bigramWord = stoppedSentence.get(i - 1);
                        processBigramWord(unigramWord, bigramWord, indexUnigramWord);
                    } else {
                        // process bigram with format w2-w1 = bigramWord-unigramWord
                        String bigramWord = stoppedSentence.get(i-1);
                        int indexBigramWord = processBigramWord(unigramWord, bigramWord, indexUnigramWord);
                        // process trigram with format w3-w2-w1 = tri-bi-unigramWord
                        String trigramWord = stoppedSentence.get(i - 2);
                        processTrigramWord(trigramWord, indexBigramWord, indexUnigramWord);
                    }
                }
            }
        // consolidating vocabulary for each n-gram
        consolidateStats();
        debugToConsole();
    }

    //BEGIN - helper methods for constructor
    private int processBigramWord(String unigramWord, String bigramWord, int indexUnigramWord) {
        int indexBigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(bigramWord);
//        System.out.println("--BIGRAM PROCESSING " + bigramWord + " with index = " + indexBigramWord); // DEBUG
        //encode w2w1 -> encoded to w1w2 with w2 is the context
        long bigramEncoded = Assignment1Utility.bitPackingBigram(indexBigramWord, indexUnigramWord);
        bigramMap.increment(bigramEncoded, 1);
        //count N1+ for KN Smoothing
        bigramEndsWithCount.increment(indexUnigramWord, 1);
        bigramStartsWithCount.increment(indexBigramWord, 1);
        //update stats
        totalBigram++;
        return indexBigramWord;
    }
    private int processTrigramWord(String trigramWord, int indexBigramWord, int indexUnigramWord) {
        int indexTrigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(trigramWord);
//        System.out.println("---TRIGRAM PROCESSING " + trigramWord + " with index = " + indexTrigramWord); // DEBUG
        //encode w3w2w1 ->encoded to w1w3w2 with w3w2 is the context
        long trigramEncoded = Assignment1Utility.bitPackingTrigram(indexTrigramWord, indexBigramWord, indexUnigramWord);
        trigramMap.increment(trigramEncoded, 1);
        //count N1+ for KN Smoothing
        trigramEndsWithCount.increment(indexUnigramWord, 1);
        //encode
        long triBigramBitPacking = Assignment1Utility.bitPackingBigram(indexTrigramWord, indexBigramWord);
//        trigramStartsWithCount.increment(indexTrigramWord, 1);
        trigramStartsWithCount.increment(triBigramBitPacking, 1);
        // update stats
        totalTrigram++;
        return indexTrigramWord;
    }
    private void consolidateStats(){
        unigramVocabSize = unigramMap.size();
        bigramVocabSize = bigramMap.size();
        trigramVocabSize = trigramMap.size();
    }
    private void debugToConsole() {
        System.out.println("Done building KNNaiveTrigramLanguageModel.");
        System.out.println("-------------STATS------------");
        System.out.println("Number of unigram vocab=" + unigramVocabSize + " and total unigram=" + totalUnigram);
        System.out.println("Number of bigram vocab=" + bigramVocabSize + " and total bigram=" + totalBigram);
        System.out.println("Number of trigram vocab=" + trigramVocabSize + " and total trigram=" + totalTrigram);
        System.out.println("-------------END OF STATS------------");
    }
    // END - helper methods for constructor

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public long getCount(int[] ngram) {
        if ((ngram.length > 3) || (ngram.length < 1))
            return 0;

        if (ngram.length == 1) {
            int key = ngram[0];
            int count = unigramMap.get(key);
            if (count == -10) return 0; // not found
            return count;
        }
        if (ngram.length == 2) {
            // encode
            int idxBigramWord = ngram[0];
            int idxUnigramWord = ngram[1];
            long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxBigramWord, idxUnigramWord);
            int count = bigramMap.get(bigramBitPacking);
            if (count == -10) return 0; // not found
            return count;
        }

        // encode
        final int idxTrigramWord = ngram[0];
        final int idxBigramWord = ngram[1];
        final int idxUnigramWord = ngram[2];
        long trigramBitPacking = Assignment1Utility.bitPackingTrigram(idxTrigramWord, idxBigramWord, idxUnigramWord);
        int count = trigramMap.get(trigramBitPacking);
        if (count == -10) return 0; // not found
        return count;
    }

    @Override
    public double getNgramLogProbability(int[] ngram, int from, int to) {
        // swith among various methods of smoothing to investigate
        return knSmoothing(ngram, from, to);
    }

    /**
     *
     * @param ngram: an array containing IDs
     * @param from
     * @param to
     * @return
     */
    public double knSmoothing(int[] ngram, int from, int to){
        double D = 0.75;
        int order = to - from;
        int contextCount = 0;
        double knScore = 0L;

        if ((order > 3) || (order == 0) || (to < 1)) {
            System.out.println("WARNING: to - from > 3 for NaiveTrigramLanguageModel");
        }
        // Base case: Unigram
        if (order == 1) return baseCaseKN(ngram, to);

        contextCount = contextCountKN(ngram, from, order);
        //recursively back off
        if (contextCount == 0) {
//            System.out.println("Backing off 1 from idx=" + to + "..."); // DEBUG
//            return knSmoothing(context, from, to - 1);
            return knSmoothing(ngram, from+1, to);
        }
        else {
            long ngramCount = getExactCountNgram(ngram, from, order);
            if (order == 3)
                knScore += SloppyMath.max(ngramCount - D, 0) / (double) contextCount;
            else {
                // order == 2

            }
            //TODO: calculate these 2
            double alpha = calculateAlpha(ngram, from, order, D);
            double continuationProb = calculateContinuationProb(ngram, from, order);
            // interpolation version of KN smoothing
            knScore += alpha * continuationProb;
        }
//        System.out.println("KN sum = " + knScore + " and score = " + Math.log(knScore)); // DEBUG

        if (knScore == 0)
            return 0;
        return Math.log(knScore);
    }
    public double knSmoothing2(int[] ngram, int from, int to){
        return 0L;
    }
    // BEGIN - helper methods for knsmoothing
    private double baseCaseKN(int[] ngram, int to) {
//        int countUnigram = unigramMap.get(ngram[from]);
        int countUnigram = unigramMap.get(ngram[to-1]);
//        System.out.println("Base case, count for worddx=" + ngram[from] + " is " + countUnigram + " over " + totalUnigram); // DEBUG
        if (countUnigram == 0)
            //TODO: use Good Turing?
            return -20; // after Log
        // TODO: consider to add 1 or not, pick from the mass for unseen word ?
        return Math.log( countUnigram / ((double)totalUnigram + 1));
    }
    private int[] retrieveContext(int[] ngram) {
        // retrieve context by copying from ngram (except the last id)
        int[] context = new int[ngram.length - 1];
        for (int i=0; i<ngram.length-1; i++)
            context[i] = ngram[i];
        return context;
    }
    private int contextCountKN(int[] ngram, int from, int order) {
        int contextCount = 0;
        //TODO: join uni-, bi- and tri-grams table at one and query at one, regardless the order
        if (order ==3) {
            //encode
            long bigramEncoded = Assignment1Utility.bitPackingBigram(ngram[from], ngram[from+1]);
            contextCount = bigramMap.get(bigramEncoded);
        } else {
            // order == 2, encode
            int idxUnigramWord = ngram[from];
            contextCount = unigramMap.get(idxUnigramWord);
        }
        return contextCount;
    }
    private double calculateAlpha(int [] ngram, int from, int order, double D) {
        if (order ==2) {
            // normalizing over the bigram word (w2w1 => bigramWord = w2, unigramword = w1)
            int idxBigramWord = ngram[from];
            int countBigramWord = unigramMap.get(idxBigramWord);
            long numerator = (long) (D * bigramStartsWithCount.get(idxBigramWord));
            return numerator / (double) countBigramWord;
        } else {
            // order == 3
            // normalizing over the trigram word (w3w2w1 => trigramWord = w3, bigramWord = w2, unigramword = w1)
            int idxTrigramWord = ngram[from];
            int idxBigramWord = ngram[from+1];
            //encode
            long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxTrigramWord, idxBigramWord);
            int countTriBigram = bigramMap.get(bigramBitPacking); // denominator
            long numerator = (long) (D * trigramStartsWithCount.get(bigramBitPacking));
            return numerator / (double) countTriBigram;
        }
    }
    private double calculateContinuationProb(int[] ngram, int from, int order) {
        if (order == 2) {
            // number of context follows ngram[1]
            int idxBigramWord = ngram[from];
            int idxUnigramWord = ngram[from+1];
            //encode
            long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxBigramWord, idxUnigramWord);
            long numerator = bigramEndsWithCount.get(idxUnigramWord);
            return numerator / (double) bigramMap.get(bigramBitPacking);
        } else {
            // order == 3
            int idxTrigramWord = ngram[from];
            int idxBigramWord = ngram[from+1];
            int idxUnigramWord = ngram[from+2];
            //encode
            long trigramBitPacking = Assignment1Utility.bitPackingTrigram(idxTrigramWord, idxBigramWord, idxUnigramWord);
            long numerator = trigramEndsWithCount.get(idxUnigramWord);
            return numerator / (double) trigramMap.get(trigramBitPacking);
        }
    }
    /**
     *
     * @param ngram
     * @param from
     * @param order
     */
    private long getExactCountNgram(int[] ngram, int from, int order) {
        int [] exactNgram = new int[order];
        for (int i=0; i<order; i++)
            exactNgram[i] = ngram[from+i];
        long count = getCount(exactNgram);
        return count;
    }
    // END - helper methods for knsmoothing
}