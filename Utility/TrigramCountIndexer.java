package edu.berkeley.nlp.assignments.assign1.student.Utility;


/**
 * Created by Gorilla on 9/22/2016.
 */
public class TrigramCountIndexer {
    /**
     * Class responsible for maintaining a global mapping between values (count of ngrams) and
     * unique shorts.
     *
     * @author adampauls
     *
     */
    private static RankIndexerLong indexer = new RankIndexerLong();

    public static RankIndexerLong getIndexer() {
        return indexer;
    }
}
