package edu.berkeley.nlp.assignments.assign1.student.Test;

import edu.berkeley.nlp.assignments.assign1.student.Utility.RankIndexerInteger;
import edu.berkeley.nlp.assignments.assign1.student.Utility.RankIndexerLong;
import edu.berkeley.nlp.assignments.assign1.student.Utility.TrigramCountIndexer;
import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import edu.berkeley.nlp.util.StringIndexer;
import org.junit.Test;

/**
 * Created by Gorilla on 9/22/2016.
 */
public class RankIndexerTest {
    @Test
    public void rankIndexerLongTest() {
        RankIndexerInteger testIndexer = TrigramCountIndexer.getIndexer();
        StringIndexer testStringIndexer = EnglishWordIndexer.getIndexer();

        for (int i=50; i<100; i++) {
            testIndexer.addAndGetIndex(i);
            System.out.println("new index: " + testStringIndexer.addAndGetIndex(String.valueOf(i)));
        }

        System.out.println("Size of Indexer is " + testIndexer.size());
        System.out.println("Size of String Indexer is " + testStringIndexer.size());

        for (short i=0; i<50; i++) {
            System.out.println(testIndexer.get(i));
        }

    }
}
