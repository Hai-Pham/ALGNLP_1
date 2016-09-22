package edu.berkeley.nlp.assignments.assign1.student.Utility;

/**
 * Created by Gorilla on 9/15/2016.
 */
public class Trigram<T> {

    private T word1;
    private T word2;
    private T word3;

    public Trigram(T word1, T word2, T word3) {
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
    }

    public int compareTo(Trigram that) {
        return (((this.word1 == that.word1) & (this.word2 == that.word2) & (this.word3 == that.word3)) ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        Trigram trigram = (Trigram) obj;
        return (this.word1.equals(trigram.word1) && this.word2.equals(trigram.word2) && this.word3.equals(trigram.word3));
    }

    public T getWord1() {
        return word1;
    }

    public T getWord2() {
        return word2;
    }

    public T getWord3() {
        return word3;
    }

    @Override
    public int hashCode() {
        int result = word1.hashCode();
        result = 31 * result + ((word2 == null) ? 0 : word2.hashCode());
        result = 31 * result + ((word3 == null) ? 0 : word3.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Trigram{" +
                "word1=" + word1.toString() +
                ", word2=" + word2.toString() +
                ", word3=" + word3.toString() +
                '}';
    }

    //    @Override
//    /**
//     * ref: http://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method/113600#113600
//     * ref2: http://stackoverflow.com/questions/24433184/overriding-hashcode-with-a-class-with-two-generics-fields
//     */
//    public int hashCode(){
//        return Objects.hash(this.word1, this.word2, this.word3);
//    }
}

