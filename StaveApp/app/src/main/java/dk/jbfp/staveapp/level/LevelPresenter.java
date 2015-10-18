package dk.jbfp.staveapp.level;

import android.os.AsyncTask;

import java.util.Arrays;

import dk.jbfp.staveapp.Stat;
import dk.jbfp.staveapp.StatRepository;

public class LevelPresenter {
    private final int step;
    private final Word[] words;
    private final StatRepository stats;
    private int wordIndex;
    private boolean perfect;
    private LevelState state;
    private LevelView view;

    // Stats.
    private double startTime;
    private int listens;

    public LevelPresenter(int step, String[] words, StatRepository stats) {
        this.step = step;
        this.stats = stats;
        this.state = LevelState.Full;
        this.perfect = true;
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
        this.listens++;
    }

    public void onStopClicked() {
        this.view.stopWordSound();
    }

    public void onAnswerClicked(String answer) throws Exception {
        Word word = words[wordIndex];
        word.setAnswer(answer);

        // Stats.
        Stat stat = new Stat();
        stat.word = word.getWord();
        stat.correct = word.getStatus() == Word.WordStatus.Correct;
        stat.time = System.nanoTime() - startTime;
        stat.listens = this.listens;
        new InsertStatAsyncTask(this.stats).execute(stat);

        if (this.state == LevelState.Full) {
            handleFull();
        } else if (this.state == LevelState.Repetition) {
            handleRepetition();
        }

        this.view.showWords(Arrays.asList(this.words));
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
        if (this.perfect) {
            this.view.displayMessage("Perfekt!");
        } else {
            this.view.displayMessage("Godt arbejde!");
        }

        this.state = LevelState.End;
        this.view.onCompleted(this.perfect);
    }

    private void transitionToRepetition() {
        this.perfect = false;
        this.state = LevelState.Repetition;

        for (int i = 0; i < this.words.length; i++) {
            if (this.words[i].getStatus() == Word.WordStatus.Incorrect) {
                this.wordIndex = i;
                break;
            }
        }

        this.view.displayMessage("Prøv igen.");
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
        for (Word word: this.words) {
            word.setAnswer("");
        }

        this.state = LevelState.Full;
        this.wordIndex = 0;
        this.view.displayMessage("En gang mere!");
        this.onNext();
    }

    private void onNext() {
        // Reset stats.
        this.startTime = System.nanoTime();
        this.listens = 0;

        this.view.onNextWord(words[wordIndex]);
        this.view.setLevel(wordIndex + 1, words.length);
        this.view.playWordSound(1000);
    }

    private enum LevelState {
        Full,
        Repetition,
        End
    }

    private class InsertStatAsyncTask extends AsyncTask<Stat, Void, Void> {
        private final StatRepository stats;

        private InsertStatAsyncTask(StatRepository stats) {
            this.stats = stats;
        }

        @Override
        protected Void doInBackground(Stat... params) {
            for (Stat stat : params) {
                stats.insertStat(stat);
            }

            return null;
        }
    }
}
