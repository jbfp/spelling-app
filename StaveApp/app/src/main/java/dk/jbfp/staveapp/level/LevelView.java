package dk.jbfp.staveapp.level;

import java.util.List;

public interface LevelView {
    void onNextWord(Word next);
    void onCompleted(boolean perfect);
    void showWords(List<Word> words);

    void setLevel(int level, int total);
    void playWordSound(int delay);
    void stopWordSound();
    void playAnswerSound();

    void setStep(int step);
}
