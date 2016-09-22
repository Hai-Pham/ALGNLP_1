package edu.berkeley.nlp.assignments.assign1.student;

/**
 * Created by Gorilla on 9/15/2016.
 */

import edu.berkeley.nlp.assignments.assign1.student.Utility.Bigram;
import edu.berkeley.nlp.assignments.assign1.student.Utility.Trigram;
import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import edu.berkeley.nlp.langmodel.NgramLanguageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Naive Implementation of Trigram
 * For baseline purpose
 *
 * For 3 ngrams, there are 3 hashmaps<int, int>. The mapping from word(string) -> int is provided by StringIndexer util
 */
public class NaiveTrigramLanguageModel implements NgramLanguageModel {

    static final String STOP = NgramLanguageModel.STOP;
    static final String START = NgramLanguageModel.START;

    // N
    long totalUnigram = 0;
    long totalBigram = 0;
    long totalTrigram = 0;

    // V
    int unigramVocabSize = 0;
    int bigramVocabSize = 0;
    int trigramVocabSize = 0;

    long[] trigramCounter = new long[10];
    long[] bigramCounter = new long[10];
    long[] unigramCounter = new long[10];

    java.util.HashMap<Integer, Integer> unigramMap = new HashMap<>();
    HashMap<Bigram<Integer>, Integer> bigramMap = new HashMap<Bigram<Integer>, Integer>();
    HashMap<Trigram<Integer>, Integer> trigramMap = new HashMap<Trigram<Integer>, Integer>();

    /**
     * Constructor
     * Declaration about the order of Ngrams
     * E.g. if we have the bigram "w1 w2" then w1 = bigramWord, w2 = word
     * if we have "w1 w2 w3" then w1 = trigramWord, w2 = bigramWord, w3 = word
     * @param sentenceCollection: list of sentences (which is a list of strings)
     */
    public NaiveTrigramLanguageModel(Iterable<List<String>> sentenceCollection) {
        System.out.println("Building NaiveTrigramLanguageModel . . .");
        int sent = 0;
        for (List<String> sentence : sentenceCollection) {
            sent++;
            if (sent % 1000000 == 0) System.out.println("On sentence " + sent);
            List<String> stoppedSentence = new ArrayList<String>(sentence);
            stoppedSentence.add(0, START);
            stoppedSentence.add(STOP);

            for (int i=0; i<stoppedSentence.size(); i++){
                // process unigram
                String unigramWord = stoppedSentence.get(i);
//                System.out.println("Solving the case idx =" + i);
                int indexUnigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(unigramWord);
                incrementNgramMap(unigramMap, indexUnigramWord);
                //TODO: double the size in empirical implementation

                if (i == 0) continue;
                if (i == 1) {
                    // process bigram
                    String bigramWord = stoppedSentence.get(i-1);
                    int indexBigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(bigramWord);
                    Bigram<Integer> bigram = new Bigram<Integer>(indexBigramWord, indexUnigramWord);
                    incrementNgramMap(bigramMap, bigram);
//                    System.out.println(indexBigramWord + " " + indexUnigramWord + ":" + bigramWord + " " + unigramWord);
                }
                if (i >=2) {
                    // process bigram
                    String bigramWord = stoppedSentence.get(i-1);
                    int indexBigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(bigramWord);
                    Bigram<Integer> bigram = new Bigram<Integer>(indexBigramWord, indexUnigramWord);
                    incrementNgramMap(bigramMap, bigram);
//                    System.out.println(bigramWord + " " + unigramWord);
                    //process trigram
                    String trigramWord = stoppedSentence.get(i-2);
                    int indexTrigramWord = EnglishWordIndexer.getIndexer().addAndGetIndex(trigramWord);
                    Trigram<Integer> trigram = new Trigram<Integer>(indexTrigramWord, indexBigramWord, indexUnigramWord);
                    incrementNgramMap(trigramMap, trigram);
//                    System.out.println(indexTrigramWord + " " + indexBigramWord + " " + indexUnigramWord + ":" + trigramWord + " " + bigramWord + " " + unigramWord);
                }
            }
        }
        System.out.println("Done building NaiveTrigramLanguageModel.");

        // TODO: Free up unused memory space in empirical implementation
//			wordCounter = CollectionUtils.copyOf(wordCounter, EnglishWordIndexer.getIndexer().size());

        // Consolidate all numbers
        totalUnigram = totalUnigram(unigramMap);
        totalBigram = totalBigram(bigramMap);
        totalTrigram = totalTrigram(trigramMap);

        unigramVocabSize = unigramMap.size();
        bigramVocabSize = bigramMap.size();
        trigramVocabSize = trigramMap.size();
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public double getNgramLogProbability(int[] ngram, int from, int to) {
        // swith among various methods of smoothing to investigate
        return addOneSmoothing(ngram, from, to);
    }

    public double addOneSmoothing(int[] ngram, int from, int to) {
        if ((to - from > 3) || (to == from) || (to < 1)) {
            System.out.println("WARNING: to - from > 3 for NaiveTrigramLanguageModel");
        }
        int indexUnigramWord = ngram[to-1];

        if (to - from == 1){
            if (!unigramMap.containsKey(indexUnigramWord))
                return 0.0;
            else {
                Integer freqUnigramWord = unigramMap.get(indexUnigramWord);
//                System.out.println("Frequency of idx " + indexUnigramWord + " is " + freqUnigramWord);
//                System.out.println("Unigram word = " + indexUnigramWord + " with freq = " + freqUnigramWord);
                return Math.log((freqUnigramWord + 1) / (totalUnigram + (float) unigramVocabSize));
            }
        }
        else if (to - from == 2) {
            // e.g. getNgramLogProbability([17,15,18,19,16,12],1,3) = P(w_i=18 | w_{i-1} = 15)
            int indexBigramWord = ngram[to-2];
            Bigram<Integer> bigram = new Bigram<Integer>(indexBigramWord, indexUnigramWord);
//            System.out.println(bigram);
            // TODO: this can be shorten by just checking bigram only
            if ((! bigramMap.containsKey(bigram)) || (! unigramMap.containsKey(indexBigramWord)))
                return 0.0;
            else {
                Integer freqUnigramWord = unigramMap.get(indexBigramWord);
                Integer freqBigramWord = bigramMap.get(bigram);
                // note on denominator: retrieve the frequency of BigramWord, i.e. 'a b' then retrieve freq(a)
                return Math.log((freqBigramWord + 1)/(freqUnigramWord + (float)unigramVocabSize));
            }
        }
        else {
            // e.g. getNgramLogProbability([17,15,18,19,16,12],1,4) = P(w_i=19 | w_{i-2} = 15, w_{i-1} = 18)
            int indexBigramWord = ngram[to-2];
            int indexTrigramWord = ngram[to-3];
            // e.g. w3 w2 w1 -> Bigram(w3, w2); Trigram(w3, w2, w1)
            Bigram<Integer> bigram = new Bigram<Integer>(indexTrigramWord, indexBigramWord);
            Trigram<Integer> trigram = new Trigram<Integer>(indexTrigramWord, indexBigramWord, indexUnigramWord);
//            System.out.println(trigram);

            // TODO: this can be shorten by just checking trigram only
            if ((! bigramMap.containsKey(bigram)) || (! trigramMap.containsKey(trigram)))
                return 0.0;
            else {
                Integer freqTrigramWord = trigramMap.get(trigram);
                Integer freqBigramWord = bigramMap.get(bigram);
//                System.out.println(freqTrigramWord);
//                System.out.println(freqBigramWord + (float)unigramVocabSize);
                return Math.log((freqTrigramWord + 1)/(freqBigramWord + (float)unigramVocabSize));
            }
        }
    }

    @Override
    // Count the number of occurrences given the index array
    // index retrieved by EnglishWordIndexer.getIndexer();
    public long getCount(int[] ngram) {
        if ((ngram.length > 3) || (ngram.length < 1))return 0;

        if (ngram.length == 1) {
            int key = ngram[0];
            if (! unigramMap.containsKey(key))
                return 0;
            return unigramMap.get(ngram[0]);
        }
        if (ngram.length == 2) {
            Bigram bigram = new Bigram(ngram[0], ngram[1]);
            if (! bigramMap.containsKey(bigram))
                return 0;
            return bigramMap.get(bigram);
        }

        final int word1 = ngram[0];
        final int word2 = ngram[1];
        final int word3 = ngram[2];

        Trigram<Integer> trigram = new Trigram<Integer>(word1, word2, word3);
        if (! trigramMap.containsKey(trigram))
            return 0;
        return trigramMap.get(trigram);
    }

    /**
     * 3 Helper methods to insert and increment by one for a specific key
     * Variations for different types of keys
     * Avoid template to this class which will be called by a test case
     * @param map
     * @param k
     * @return
     */
    private int incrementNgramMap(HashMap<Integer, Integer> map, Integer k) {
        if (! map.containsKey(k)) {
            map.put(k, 1);
            return 1;
        }
        else {
            int v = map.get(k);
            map.put(k, v+1);
            return v+1;
        }
    }
    private int incrementNgramMap(HashMap<Bigram<Integer>, Integer> map, Bigram<Integer> k) {
        if (! map.containsKey(k)) {
            map.put(k, 1);
            return 1;
        }
        else {
            int v = map.get(k);
            map.put(k, v+1);
            return v+1;
        }
    }
    private int incrementNgramMap(HashMap<Trigram<Integer>, Integer> map, Trigram<Integer> k) {
        if (! map.containsKey(k)) {
            map.put(k, 1);
            return 1;
        }
        else {
            int v = map.get(k);
            map.put(k, v+1);
            return v+1;
        }
    }

    /**
     * Helper methods to calculate total of values (N)
     */
    private long totalUnigram(HashMap<Integer, Integer> unigramMap){
        long count = 0;
        Set<Integer> unigramKeySet = unigramMap.keySet();
        for (Integer k: unigramKeySet)
            count += unigramMap.get(k);

        return count;
    }
    private long totalBigram(HashMap<Bigram<Integer>, Integer> bigramMap){
        long count = 0;
        Set<Bigram<Integer>> bigramKeySet = bigramMap.keySet();
        for (Bigram<Integer> k: bigramKeySet)
            count += bigramMap.get(k);

        return count;
    }
    private long totalTrigram(HashMap<Trigram<Integer>, Integer> trigramMap){
        long count = 0;
        Set<Trigram<Integer>> trigramKeySet = trigramMap.keySet();
        for (Trigram<Integer> k: trigramKeySet)
            count += trigramMap.get(k);

        return count;
    }

}