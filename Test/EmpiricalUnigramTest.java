package edu.berkeley.nlp.assignments.assign1.student.Test;
import edu.berkeley.nlp.langmodel.EmpiricalUnigramLanguageModel;
import org.junit.Test;

import java.util.*;

/**
 * Created by Gorilla on 9/19/2016.
 */
public class EmpiricalUnigramTest {
    @Test
    public void EmpiricalUnigramLanguageModelTest() {
        System.out.println("Test the log probability of Naive Trigram Language Model");
        ArrayList<List<String>> sentences = new ArrayList<>();
        String s1 = "a b c a b c";
        List<String> sentence1 = new ArrayList<>(Arrays.asList(s1.split(" ")));
        String s2 = "b c d e f a b c";
        List<String> sentence2 = new ArrayList<>(Arrays.asList(s2.split(" ")));
        sentences.add(sentence1);
        sentences.add(sentence2);

        EmpiricalUnigramLanguageModel languageModel = new EmpiricalUnigramLanguageModel(sentences);

        // test
        int[] ngramArray = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 0, 2, 3, 5, 7, 1, 3, 1};
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 0, 1));
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 1, 2));
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 2, 3));
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 3, 4));
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 4, 5));
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 5, 6));
//        System.out.println(languageModel.getNgramLogProbability(new int[] {0, 1}, 6, 7));



    }
}
