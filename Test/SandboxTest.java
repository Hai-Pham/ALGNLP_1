package edu.berkeley.nlp.assignments.assign1.student.Test;
import edu.berkeley.nlp.assignments.assign1.student.Utility.Bigram;
import edu.berkeley.nlp.assignments.assign1.student.Utility.Trigram;
import edu.berkeley.nlp.langmodel.NgramLanguageModel;
import org.junit.Test;

import java.util.*;

/**
 * Created by Gorilla on 9/19/2016.
 */
public class SandboxTest {
    @Test
    public void stringSandbox() {
//        String s = "this is a test sentence this is a";
        String s = "a b c a b c";
        List<String> sentence = new ArrayList<>(Arrays.asList(s.split(" ")));

        List<String> stoppedSentence = new ArrayList<String>(sentence);
        stoppedSentence.add(0, NgramLanguageModel.START);
        stoppedSentence.add(NgramLanguageModel.STOP);
        System.out.println(stoppedSentence);

        java.util.HashMap<String, Integer> unigramMap = new HashMap<>();
        java.util.HashMap<Bigram, Integer> bigramMap = new HashMap<>();
        java.util.HashMap<Trigram, Integer> trigramMap = new HashMap<>();

        for (int i=0; i<stoppedSentence.size(); i++){
            // process unigram
            String word = stoppedSentence.get(i);
            if (!unigramMap.containsKey(word))
                unigramMap.put(word, 1);
            else
                unigramMap.put(word, unigramMap.get(word) + 1 );

            if (i == 0) continue;
            else if (i == 1) {
                // process bigram
                String bigramWord = stoppedSentence.get(i-1);
                Bigram bigram = new Bigram(bigramWord, word);
                if (! bigramMap.containsKey(bigram))
                    bigramMap.put(bigram,1);
                else
                    bigramMap.put(bigram, bigramMap.get(bigram) + 1);

                System.out.println(bigramWord + " " + word);
            }
            else {
                //process bigram and trigram
                String bigramWord = stoppedSentence.get(i-1);
                String trigramWord = stoppedSentence.get(i-2);

                Bigram bigram = new Bigram(bigramWord, word);
                if (! bigramMap.containsKey(bigram))
                    bigramMap.put(bigram,1);
                else
                    bigramMap.put(bigram, bigramMap.get(bigram) + 1);

                Trigram trigram = new Trigram(trigramWord, bigramWord, word);
                if (! trigramMap.containsKey(trigram))
                    trigramMap.put(trigram, 1);
                else
                    trigramMap.put(trigram, trigramMap.get(trigram) + 1);

                System.out.println(trigramWord + " " + bigramWord + " " + word);
            }
        }
        // test
        Set<String> unigramKeys = unigramMap.keySet();
        System.out.println(unigramKeys);
        System.out.println("Size of unigram map is: " + unigramMap.size());

        Set<Bigram> bigramKeys = bigramMap.keySet();
        System.out.println(bigramKeys);
        System.out.println("Size of bigram map is: " + bigramMap.size());

        Set<Trigram> trigramKeys = trigramMap.keySet();
        System.out.println(trigramKeys);
        System.out.println("Size of trigram map is: " + trigramMap.size());
    }
//    @Test
//    public void TIntOpenHashMapTest(){
//        TIntOpenHashMap<Integer> testMap = new TIntOpenHashMap<>(2);
//        testMap.increment(0, 1);
//        testMap.increment(1, 1);
//        testMap.increment(2, 1);
//        testMap.increment(3, 1);
//        testMap.increment(4, 1);
//        testMap.increment(5, 1);
//
//        System.out.println("Size of test Map is " + testMap.size());
//    }
//    @Test
//    public void maxTest() {
//        assertEquals(SloppyMath.max(1, 2), 2, 0.0001);
//        assertEquals(SloppyMath.max(10.0, 15.0), 15.0, 0.000001);
//    }
//    @Test
//    public void testJavaArray() {
//        int[] x = new int[5];
//
//        System.out.println("Array content is :");
//        for (int i: x)
//            System.out.println(i);
//        java.util.Arrays.fill(x, -1);
//        System.out.println("Array content updated is :");
//        for (int i: x)
//            System.out.println(i);
//
//    }

}
