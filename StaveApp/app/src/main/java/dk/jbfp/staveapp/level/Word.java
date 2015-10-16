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
        this.answer = "";
    }

    public WordStatus getStatus() {
        if (this.answer.length() == 0) {
            return WordStatus.Incomplete;
        }

        if (this.word.equalsIgnoreCase(this.answer)) {
            return WordStatus.Correct;
        } else {
            return WordStatus.Incorrect;
        }
    }

    public void setAnswer(String answer) {
        if (answer == null) {
            throw new IllegalArgumentException();
        }

        this.answer = answer;
    }

    public String getWord() {
        return this.word;
    }

    public String getAnswer() {
        return this.answer;
    }
}
