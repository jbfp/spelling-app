package dk.jbfp.staveapp.level;

public class Word {
    public enum WordStatus {
        Incomplete,
        Incorrect,
        Correct
    }

    private String word;
    private String answer;

    public Word(String word) {
        this.word = word;
        this.answer = null;
    }

    public WordStatus getStatus() {
        if (this.answer == null) {
            return WordStatus.Incomplete;
        }

        if (this.word.equalsIgnoreCase(this.answer)) {
            return WordStatus.Correct;
        } else {
            return WordStatus.Incorrect;
        }
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getWord() {
        return this.word;
    }

    public String getAnswer() {
        return this.answer;
    }
}
