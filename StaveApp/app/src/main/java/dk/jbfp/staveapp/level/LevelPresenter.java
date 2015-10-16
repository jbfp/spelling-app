package dk.jbfp.staveapp.level;

public class LevelPresenter {
    private final int step;
    private boolean perfect;
    private final Word[] words;
    private int wordIndex;
    private LevelState state;
    private LevelView view;

    public LevelPresenter(int step, String[] words) {
        this.step = step;
        this.perfect = true;
        this.state = LevelState.Full;
        this.wordIndex = 0;
        this.words = new Word[words.length];

        for (int i = 0; i < words.length; i++) {
            this.words[i] = new Word(words[i]);
        }
    }

    public void setView(LevelView view) {
        this.view = view;
        this.view.setStep(this.step);
        this.onNext();
    }

    public void onAnswerChanged() {
        this.view.playAnswerSound();
    }

    public void onPlayClicked() {
        this.view.playWordSound(0);
    }

    public void onStopClicked() {
        this.view.stopWordSound();
    }

    public void onAnswerClicked(String answer) throws Exception {
        Word word = words[wordIndex];
        word.setAnswer(answer);
        this.view.addWord(word);

        if (this.state == LevelState.Full) {
            handleFull();
        } else if (this.state == LevelState.Repetition) {
            handleRepetition();
        }
    }

    private void handleFull() throws Exception {
        if (this.wordIndex < this.words.length - 1) {
            this.wordIndex = this.wordIndex + 1;
            this.onNext();
        } else {
            boolean allCorrect = true;

            for (Word word: this.words) {
                if (word.getStatus() == Word.WordStatus.Incorrect) {
                    allCorrect = false;
                    break;
                }
            }

            if (allCorrect) {
                this.transitionToEnd();
            } else {
                this.transitionToRepetition();
            }
        }
    }

    private void transitionToEnd() throws Exception {
        this.state = LevelState.End;
        this.view.onCompleted(this.perfect);
    }

    private void transitionToRepetition() {
        this.perfect = false;
        this.state = LevelState.Repetition;
        this.view.clearList();

        for (int i = 0; i < this.words.length; i++) {
            if (this.words[i].getStatus() == Word.WordStatus.Incorrect) {
                this.wordIndex = i;
                break;
            }
        }

        this.onNext();
    }

    private void handleRepetition() {
        if (this.wordIndex < this.words.length - 1) {
            for (int i = this.wordIndex + 1; i < this.words.length; i++) {
                if (this.words[i].getStatus() == Word.WordStatus.Incorrect) {
                    this.wordIndex = i;
                    this.onNext();
                    return;
                }
            }

            this.wordIndex = this.words.length;
            this.handleRepetition();
        } else {
            boolean allCorrect = true;

            for (Word word: this.words) {
                if (word.getStatus() == Word.WordStatus.Incorrect) {
                    allCorrect = false;
                    break;
                }
            }

            if (allCorrect) {
                this.transitionToFull();
            } else {
                this.transitionToRepetition();
            }
        }
    }

    private void transitionToFull() {
        this.state = LevelState.Full;
        this.view.clearList();
        this.wordIndex = 0;
        this.onNext();
    }

    private void onNext() {
        this.view.onNextWord(words[wordIndex]);
        this.view.setLevel(wordIndex + 1, words.length);
        this.view.playWordSound(1000);
    }

    private enum LevelState {
        Full,
        Repetition,
        End
    }
}
