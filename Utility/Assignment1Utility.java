package edu.berkeley.nlp.assignments.assign1.student.Utility;

/**
 * Created by Gorilla on 9/15/2016.
 * Utilities for Assignment 1
 */

import java.util.*;

public class Assignment1Utility {
    /**
     * Bit packing for bigram
     * @param w1
     * @param w2
     * @return bit packing as a long (64-bit) number with 44 bits for w2, 20 for w1
     * The format is w2w1 with w2 is 40-bit context
     */

    public static long bitPackingBigram(int w1, int w2) {
        long lw2 = ((long) w2) << 20;
        long lw1 = w1 & 0xFFFFFL;

        return lw2 | lw1;
    }

    /**
     * Decode bitpacking for Bigram
     * @param encodedPacking
     * @return an array storing the numbers for w1, w2 respectively
     * Note: the order is w2w1
     */
    public static int[] bigramBitPackingDecode(long encodedPacking) {
        int[] result = new int[2];

        result[0] = (int)(encodedPacking & 0xFFFFFL); //w1
        result[1] = (int)(encodedPacking >> 20); //w2

        return result;
    }
    /**
     * Bit packing for trigram
     * @param w1
     * @param w2
     * @param w3
     * @return bit packing as a long (64-bit) number with 24 bits for w1, 20 for w2 and 20 for w3
     * The format is w3-w1w2 and w1w2 is 40-bit context
     */
    public static long bitPackingTrigram(int w1, int w2, int w3) {
        long lw3 = ((long) w3) << 40;
        long lw1 = ((long)w1 & 0xFFFFFL) << 20;
        long lw2 = w2 & 0xFFFFFL;

//        return w3 << 40 | w2 << 20 | w1;
        return lw3 | lw1 | lw2;
    }

    /**
     * Decode bitpacking for trigram
     * @param encodedPacking
     * @return an array storing the numbers for w1, w2 and w3 respectively
     * Note: the order is w3w1w2 then the order of decoding is w2 -> w1 -> w3
     */
    public static int[] trigramBitPackingDecode(long encodedPacking) {
        int[] result = new int[3];

        result[1] = (int)(encodedPacking & 0xFFFFFL); //w2
        result[0] = (int)((encodedPacking >> 20) & 0xFFFFFL); //w1
        result[2] = (int)(encodedPacking >> 40); //w3

        return result;
    }
}
