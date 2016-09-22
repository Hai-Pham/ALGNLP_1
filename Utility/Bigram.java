package edu.berkeley.nlp.assignments.assign1.student.Utility;

/**
 * Created by Gorilla on 9/15/2016.
 *
 */
public class Bigram<T> {

    private T word1;
    private T word2;

    public Bigram(T word1, T word2) {
        this.word1 = word1;
        this.word2 = word2;
    }

    public T getWord1() {
        return word1;
    }

    public T getWord2() {
        return word2;
    }

    public int compareTo(Bigram that) {
        return (((this.word1 == that.word1) & (this.word2 == that.word2)) ? 1: 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bigram bigram = (Bigram) o;
        return (word1.equals(bigram.word1) && word2.equals(bigram.word2));
    }

    @Override
    public int hashCode() {
        int result = word1.hashCode();
        result = 31 * result + ((word2 == null) ? 0 : word2.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Bigram{" +
                "word1=" + word1 +
                ", word2=" + word2 +
                '}';
    }
    //    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) return false;
//
//        if (obj instanceof Bigram) {
//            Bigram bigram = (Bigram) obj;
//            return ((this.word1 == bigram.word1) && (this.word2 == bigram.word2));
//        }
//        return false;
//    }
//
//    @Override
//    /**
//     * ref: http://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method/113600#113600
//     * ref2: http://stackoverflow.com/questions/24433184/overriding-hashcode-with-a-class-with-two-generics-fields
//     */
//    public int hashCode(){
//        return Objects.hash(this.word1, this.word2);
//    }
}
