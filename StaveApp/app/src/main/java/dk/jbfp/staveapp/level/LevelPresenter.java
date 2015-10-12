package dk.jbfp.staveapp.level;

public class LevelPresenter {
    private final Word[] words;
    private int wordIndex;
    private LevelView view;

    public LevelPresenter() {
        this.words = new Word[] {
                new Word("da"),
                new Word("en"),
                new Word("to")
        };
    }

    public void setView(LevelView view) {
        this.view = view;
        this.view.onNextWord(null, words[wordIndex]);
    }

    public int getLength() {
        return words.length;
    }

    public int getIndex() {
        return wordIndex;
    }

    public Word getCurrentWord() {
        return words[wordIndex];
    }

    public void answer(String answer) {
        if (wordIndex >= words.length) {
            return;
        }

        Word word = words[wordIndex++];

        if (answer.equalsIgnoreCase(word.toString())) {
            word.markAsCorrect();
        } else {
            word.markAsIncorrect();
        }

        if (wordIndex < words.length) {
            view.onNextWord(word, words[wordIndex]);
        } else {
            boolean allCorrect = true;

            for (Word w: words) {
                if (w.getStatus() != Word.WordStatus.Correct) {
                    allCorrect = false;
                    break;
                }
            }

            view.onCompleted(allCorrect);

            if (allCorrect) {
                // TODO: Navigate to level screen.
                // TODO: Mark level as completed.
            } else {
                // TODO: Do all those words that are incorrect.
                // TODO: Start over.
            }
        }
    }
}
