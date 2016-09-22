package edu.berkeley.nlp.assignments.assign1.student;

/**
 * Created by Gorilla on 9/19/2016.
 */

import edu.berkeley.nlp.assignments.assign1.student.Utility.*;
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
public class KNTrigramLanguageModel2 implements NgramLanguageModel {

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
    public LongIntOpenHashMap trigramMap = new LongIntOpenHashMap(1000000);

    /**
     * Constructor
     * Declaration about the order of Ngrams
     * E.g. if we have the bigram "w1 w2" then w1 = bigramWord, w2 = word
     * if we have "w1 w2 w3" then w1 = trigramWord, w2 = bigramWord, w3 = word
     * @param sentenceCollection: list of sentences (which is a list of strings)
     */
    public KNTrigramLanguageModel2(Iterable<List<String>> sentenceCollection) {

        System.out.println("Building Knesser Ney Trigram Language Model . . .");
        int sent = 0;
        for (List<String> sentence : sentenceCollection) {
            sent++;
            if (sent % 100000 == 0) System.out.println("On sentence " + sent);
            List<String> stoppedSentence = new ArrayList<String>(sentence);
//            stoppedSentence.add(0, START);
            stoppedSentence.add(0, START);
            stoppedSentence.add(STOP);

            for (int i=0; i<stoppedSentence.size(); i++){

                // process unigram word which is w3 in w1w2w3 trigram
                String w3 = stoppedSentence.get(i);
                int idxW3 = EnglishWordIndexer.getIndexer().addAndGetIndex(w3);
//                    System.out.println("PROCESSING " + unigramWord + " with index = " + indexUnigvramWord); // DEBUG
                unigramMap.incrementValue(idxW3, 1);
                totalUnigram++;

                if (i == 0) { // unigram, no duplication at the time of 0
//                    System.out.println("~Adding " + w3);
                    unigramMap.incrementStart(idxW3, 1);
                }
                else if (i == 1) { //bigram
                    int idxW2 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i-1));
                    long indexBigram = Assignment1Utility.bitPackingBigram(idxW2, idxW3);
                    if (bigramMap.getValue(indexBigram) == 0 ) //w2w3 = .w3 dup?
                        unigramMap.incrementEnd(idxW3, 1);

                    // process bigram with format w1-w2 = bigramWord-unigramWord
//                    System.out.println("**Adding + " + stoppedSentence.get(i-1) + w3);
                    bigramMap.incrementValue(indexBigram, 1);
                    totalBigram++;

                    if (i < stoppedSentence.size() - 1) {
                        int idxW4 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i+1));
                        long bigramStartsFromW3 = Assignment1Utility.bitPackingBigram(idxW3, idxW4); //w3w4

                        if (bigramMap.getValue(bigramStartsFromW3) == 0 ) //w2w3 = .w3 dup? fix this
                            unigramMap.incrementStart(idxW3, 1); // count w2.

                        long trigramStartsFromW2 = Assignment1Utility.bitPackingTrigram(idxW2, idxW3, idxW4);
                        if (trigramMap.get(trigramStartsFromW2) == 0) {
                            unigramMap.incrementBetween(idxW3, 1);
                            bigramMap.incrementStart(indexBigram, 1);
                        }
                    }
                } else if (i == stoppedSentence.size()-1) { // </s>
                    // TODO: check this
//                    System.out.println("xxxxxxxxx processing last word which is " + stoppedSentence.get(i));
                    String w2 = stoppedSentence.get(i-1);
                    int idxW2 = EnglishWordIndexer.getIndexer().addAndGetIndex(w2);
                    long indexBigram = Assignment1Utility.bitPackingBigram(idxW2, idxW3);

//                    System.out.println("xxxxxxxxx #" + w2 + "+" + w3 + "=" + bigramMap.getValue(indexBigram));
                    if (bigramMap.getValue(indexBigram) == 0) {//w2w3 -> .w3
                        unigramMap.incrementEnd(idxW3, 1);
//                        System.out.println("xxxxxxxxx Not found " + w2 + "+" + w3 + " now increasing bigram ends with " + "w3");
                    }

//                    System.out.println("xxxxxxxxx Adding " + w2 + "+" + w3);
                    bigramMap.incrementValue(indexBigram, 1);
                    totalBigram++;

                    if (i >= 2) {
                        String w1 = stoppedSentence.get(i-2);
                        int idxW1 = EnglishWordIndexer.getIndexer().addAndGetIndex(w1);
                        long indexTrigram = Assignment1Utility.bitPackingTrigram(idxW1, idxW2, idxW3);

                        // check duplication before adding
//                        System.out.println("xxxxxxxxx #" + w1 + w2 + "+" + w3 + "=" + trigramMap.get(indexTrigram));
                        if (trigramMap.get(indexTrigram) == 0) //w1w2w3 = .w2w3
                            bigramMap.incrementEnd(indexBigram, 1);

//                        System.out.println("xxxxxxxxx Adding " + w1 + "+" + w2 + "+" + w3);
                        trigramMap.increment(indexTrigram, 1);
                        totalTrigram++;
                    }
                } else {
                    String w2 = stoppedSentence.get(i-1); //w2
                    int idxW2 = EnglishWordIndexer.getIndexer().addAndGetIndex(w2);
                    long indexBigram = Assignment1Utility.bitPackingBigram(idxW2, idxW3);//w2w3
                    // process trigram with format w3-w2-w1 = tri-bi-unigramWord
                    String w1 = stoppedSentence.get(i-2); //w1
                    int idxW1 = EnglishWordIndexer.getIndexer().addAndGetIndex(w1);
                    long indexTrigram = Assignment1Utility.bitPackingTrigram(idxW1, idxW2, idxW3);//w1w2w3

                    // i >= 2 & i <= l-2
                    if (bigramMap.getValue(indexBigram) == 0 ) //w2w3 = .w3 dup?
                        unigramMap.incrementEnd(idxW3 ,1);
                    int idxW4 = EnglishWordIndexer.getIndexer().addAndGetIndex(stoppedSentence.get(i+1)); //w4
                    long indexBigramFromThis = Assignment1Utility.bitPackingBigram(idxW3, idxW4); //w3w4 = w3.

                    if (bigramMap.getValue(indexBigramFromThis) == 0) //w3w4 = w3. dup?
                        unigramMap.incrementStart(idxW3, 1);
                    long indexTrigramFromW2 = Assignment1Utility.bitPackingTrigram(idxW2, idxW3, idxW4); //w2w3w4 = .w3.
                    if (trigramMap.get(indexTrigramFromW2) == 0) //w2w1w0 = .w1. dup?
                        unigramMap.incrementBetween(idxW3, 1);

//                    System.out.println("====Adding " + w2 + "+" +  w3);
                    bigramMap.incrementValue(indexBigram, 1);
                    totalBigram++;

                    if (trigramMap.get(indexTrigram) == 0) // .w2w3 dup?
                        bigramMap.incrementEnd(indexBigram, 1); // w1w2w3 = .w2w3
                    if (trigramMap.get(indexTrigramFromW2) == 0) // w1w2w3 = w1w2. dup?
                        bigramMap.incrementStart(indexBigram, 1);

                    trigramMap.increment(indexTrigram, 1);
                    totalTrigram++;
                }
            }
        }
        // consolidating vocabulary for each n-gram
        consolidateStats();
        debugToConsole();
    }

    // BEGIN - helper methods for constructor
    private void consolidateStats(){
        unigramVocabSize = unigramMap.size();
        bigramVocabSize = bigramMap.size();
        trigramVocabSize = trigramMap.size();
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
            int count = unigramMap.getValue(key);
            if (count == -1) return 0; // not found TODO: check whether this is redundant
            return count;
        }
        if (ngram.length == 2) {
            // encode
            int idxBigramWord = ngram[0];
            int idxUnigramWord = ngram[1];
            long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxBigramWord, idxUnigramWord);
            int count = bigramMap.getValue(bigramBitPacking);
            if (count == -1) return 0; // not found
            return count;
        }

        // encode
        final int idxTrigramWord = ngram[0];
        final int idxBigramWord = ngram[1];
        final int idxUnigramWord = ngram[2];
        long trigramBitPacking = Assignment1Utility.bitPackingTrigram(idxTrigramWord, idxBigramWord, idxUnigramWord);
        int count = trigramMap.get(trigramBitPacking);
        if (count == -1) return 0; // not found
        return count;
    }

    @Override
    public double getNgramLogProbability(int[] ngram, int from, int to) {
        // swith among various methods of smoothing to investigate
//        return knSmoothingLogProb(ngram, from, to);
        return Math.log(knScoreGeneral(ngram, from, to) + 1e-20);
    }

    public double knScoreGeneral(int[] ngram, int from, int to) {
        int order = to - from;
        if (order == 1)
            return knScoreUnigram(ngram, to);
//            return baseCase(ngram, to);
        if (order == 2)
            return knScoreBigram(ngram, to);

        return knScoreTrigram(ngram, to);
    }

//    private double baseCase(int[] ngram, int to) {
//        int count = unigramMap.getValue(ngram[to-1]); //w3
//        System.out.println("count=" + count + " over " + totalUnigram);
//        return (count + 1) / (double)(totalUnigram + unigramMap.size());
//    }

    private double knScoreUnigram(int[] ngram, int to) {
//        int count = unigramMap.getValue(ngram[to-1]); //w3
        int count = unigramMap.getBigramEndsWithThis(ngram[to-1]); //w3
        if (count == 0)
            return 1e-20;
        else
//            return unigramMap.getBigramEndsWithThis(ngram[to-1]) / (double) bigramMap.size();
//            return unigramMap.getBigramEndsWithThis(ngram[to-1]) / (double) totalBigram; //24.378
            return unigramMap.getBigramEndsWithThis(ngram[to-1]) / (double) unigramMap.getTotalBigramEndsWithThis();
    }
    private double knScoreBigram(int[] ngram, int to) {
        // count denominator to back-off in edge case
        int countInBetweenW2 = unigramMap.getBigramWithThisInBetween(ngram[to-2]); // count(.w2.)
        if (countInBetweenW2 == 0)
            return knScoreUnigram(ngram, to); // complete back-off

        double bigramKNScore = 0.0;

        //encode bigram (as a continuation) for numerator W2W3
        long bigramBitPacking = Assignment1Utility.bitPackingBigram(ngram[to-2], ngram[to-1]);
        double numerator = SloppyMath.max((double)bigramMap.gettrigramEndsWithThis(bigramBitPacking) - D, 0.0);

        bigramKNScore += numerator / (double) countInBetweenW2;
        bigramKNScore += calculateAlphaUnigram(ngram[to-2]) * knScoreUnigram(ngram, to);

//        System.out.println("Bigram calc: numer=" + numerator + " denor=" + countInBetweenW2 + " knScore unigram");
        return bigramKNScore;
    }
    private double knScoreTrigram(int[] ngram, int to) {

        // edge case -> back off
        long bigramBitPacking = Assignment1Utility.bitPackingBigram(ngram[to-3], ngram[to-2]); //w1w2
        int countW1W2 = bigramMap.getValue(bigramBitPacking);
        if (countW1W2 == 0)
            return knScoreBigram(ngram, to); // complete back off

        double trigramKNScore = 0.0;
        //encode trigram
        long trigramBitPacking = Assignment1Utility.bitPackingTrigram(ngram[to-3], ngram[to-2], ngram[to-1]);
        double numerator = SloppyMath.max((double)trigramMap.get(trigramBitPacking) - D, 0.0);
        trigramKNScore += numerator / (double) countW1W2;
        trigramKNScore += calculateAlphaBigram(ngram[to-3], ngram[to-2]) * knScoreBigram(ngram, to);
//        System.out.println("Calc Trigram: numer=" + numerator + " denor=" + countW1W2 + " bigram score");
        return trigramKNScore;
    }
    // alpha(w3)
    // parameter is w3 but processing on w2, index is passed correctly
    private double calculateAlphaUnigram(int idxW2) {
        double alpha = D;
        alpha *= unigramMap.getBigramStartsWithThis(idxW2); //fertility
        alpha /= (double) unigramMap.getBigramWithThisInBetween(idxW2);
//        System.out.println("----alpha uni calc:  numer=" + unigramMap.getBigramStartsWithThis(idxW2) + " denor=" +
//                unigramMap.getBigramWithThisInBetween(idxW2));
        return alpha;
    }
    // alpha(w2w3)
    // parameters are w2, w3 but processing on w1w2
    // indexes of w1 and w2 is passed correctly
    private double calculateAlphaBigram(int idxW1, int idxW2) {
        double alpha = D;
        //encode w1w2
        long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxW1, idxW2);
        alpha *= bigramMap.gettrigramStartsWithThis(bigramBitPacking); //fertility
        alpha /= (double) bigramMap.getValue(bigramBitPacking);
//        System.out.println("--alpha bigram calc:  numer=" +  bigramMap.gettrigramStartsWithThis(bigramBitPacking) + " denor=" + bigramMap.getValue(bigramBitPacking));
        return alpha;
    }








//    public double knSmoothingLogProb(int[] ngram, int from, int to){
//        // To calculate P(w1w2w3)
//        double knScore = 0L;
//        int order = to - from;
//
//        if (order == 1) {
//            // P(w3)
//            knScore += knScoreUnigram(ngram[to-1]);
//        } else if (order == 2) {
//            // P(w2w3)
//            knScore += knScoreBigram(ngram[to-2], ngram[to-1]);
//            // alpha(w3) * P_kn(w3)
//            // alpha for w3 but processing on w2
//            knScore += (calculateAlphaUnigram(ngram[to-2]) * knScoreUnigram(ngram[to-1]));
//        } else { // order == 3
//            // P(w1w2w3)
//            knScore += knScoreTrigram(ngram[to-3], ngram[to-2], ngram[to-1]);
//            // alpha(w2w3) * P_kn(w2w3)
//            // alpha for w2, w3 but processing on w1, w2
//            knScore  += (calculateAlphaBigram(ngram[to-3], ngram[to-2]) * knScoreBigram(ngram[to-2], ngram[to-1]));
//        }
//        return Math.log(knScore + 1e-20);
//    }
//    // P_kn(w3)
//    private double knScoreUnigram(int idxW3) {
//        int count = unigramMap.getValue(idxW3);
//
//        if (count == 0)
//            return 1e-20;
//        else
//            return unigramMap.getBigramEndsWithThis(idxW3) / (double) bigramMap.size();
//    }
//    // P_kn(w2 w3)
//    private double knScoreBigram(int idxW2, int idxW3) {
//        //encode bigram (as a continuation) for numerator
//        long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxW2, idxW3);
//        double numerator = SloppyMath.max(bigramMap.gettrigramEndsWithThis(bigramBitPacking) - D, 0);
//
//        // count numerator to back-off in edge case
//        int countInBetweenW2 = unigramMap.getBigramWithThisInBetween(idxW2);
//        if (countInBetweenW2 == 0)
//            return knScoreUnigram(idxW3);
//
//        return numerator / (double) countInBetweenW2;
//    }
//    // P_kn(w1 w2 w3)
//    private double knScoreTrigram(int idxW1, int idxW2, int idxW3) {
//        //encode bigram as a context (w1w2)
//        long bigramBitPacking = Assignment1Utility.bitPackingBigram(idxW1, idxW2);
//
//        // edge case -> back off
//        int countW1W2 = bigramMap.getValue(bigramBitPacking);
//        if (countW1W2 == 0)
//            return knScoreBigram(idxW2, idxW3);
//
//        //encode trigram
//        long trigramBitPacking = Assignment1Utility.bitPackingTrigram(idxW1, idxW2, idxW3);
//        double numerator = SloppyMath.max(trigramMap.get(trigramBitPacking) - D, 0);
//
//        return numerator / (double) countW1W2;
//    }
//







}