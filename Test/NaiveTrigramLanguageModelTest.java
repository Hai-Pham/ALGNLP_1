package edu.berkeley.nlp.assignments.assign1.student.Test;

/**
 * Created by Gorilla on 9/19/2016.
 */
public class NaiveTrigramLanguageModelTest {

    /** 2 sentences
     * "a b c a b c"
     * "b c d e f a b c"
     * Unigram map is like so
     * word     idx #
     <s>	    0	2
     a	    1	3
     b	    2	4
     c	    3	4
     </s>	    4	2
     d	    5	1
     e	    6	1
     f	    7	1

     {Bigram{word1=0, word2=1}=1,
     Bigram{word1=1, word2=2}=3,
     Bigram{word1=2, word2=3}=4,
     Bigram{word1=3, word2=4}=2,
     Bigram{word1=5, word2=6}=1,
     Bigram{word1=6, word2=7}=1,
     Bigram{word1=0, word2=2}=1,
     Bigram{word1=3, word2=5}=1,
     Bigram{word1=7, word2=1}=1,
     Bigram{word1=3, word2=1}=1}

     {Trigram{word1=2, word2=3, word3=1}=1, Trigram{word1=6, word2=7, word3=1}=1, Trigram{word1=0, word2=1, word3=2}=1,
     Trigram{word1=0, word2=2, word3=3}=1, Trigram{word1=1, word2=2, word3=3}=3, Trigram{word1=2, word2=3, word3=4}=2,
     Trigram{word1=3, word2=1, word3=2}=1, Trigram{word1=2, word2=3, word3=5}=1, Trigram{word1=3, word2=5, word3=6}=1,
     Trigram{word1=5, word2=6, word3=7}=1, Trigram{word1=7, word2=1, word3=2}=1}
     */
//    @Test
//    public void NaiveTrigramLanguageModelConstructorTest() {
//        System.out.println("Test the construction of Naive Trigram Language Modeling");
//        ArrayList<List<String>> sentences = new ArrayList<>();
//        String s1 = "a b c a b c";
//        List<String> sentence1 = new ArrayList<>(Arrays.asList(s1.split(" ")));
//        String s2 = "b c d e f a b c";
//        List<String> sentence2 = new ArrayList<>(Arrays.asList(s2.split(" ")));
//        sentences.add(sentence1);
//        sentences.add(sentence2);
//
//        System.out.println(sentences);
//
//        NaiveTrigramLanguageModel languageModel = new NaiveTrigramLanguageModel(sentences);
//
//        System.out.println("Unigram Map with size " + languageModel.totalUnigram + " and vocab " + languageModel.unigramVocabSize);
//        System.out.println(languageModel.unigramMap);
//        System.out.println("Bigram Map with size " + languageModel.totalBigram + " and vocab " + languageModel.bigramVocabSize);
//        System.out.println(languageModel.bigramMap);
//        System.out.println("Trigram Map with size " + languageModel.totalTrigram + " and vocab " + languageModel.trigramVocabSize);
//        System.out.println(languageModel.trigramMap);
//
//        assertEquals(languageModel.totalUnigram, 18);
//        assertEquals(languageModel.totalBigram, 16);
//        assertEquals(languageModel.totalTrigram, 14);
//        assertEquals(languageModel.unigramVocabSize, 8);
//        assertEquals(languageModel.bigramVocabSize, 10);
//        assertEquals(languageModel.trigramVocabSize, 11);
//    }
}
