package dk.jbfp.staveapp.level;

public class Word {
    public enum WordStatus {
        Incomplete,
        Incorrect,
        Correct
    }

    private final String word;
    private WordStatus status;

    public Word(String word) {
        this.word = word;
        this.status = WordStatus.Incomplete;
    }

    public WordStatus getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return word;
    }

    public void markAsIncorrect() {
        this.status = WordStatus.Incorrect;
    }

    public void markAsCorrect() {
        this.status = WordStatus.Correct;
    }
}
