package dk.jbfp.staveapp.level;

public interface LevelView {
    void onNextWord(Word next);
    void onCompleted();

    void addWord(Word word);
    void clearList();

    void setLevel(int level, int total);
    void playWordSound(int delay);
    void stopWordSound();
    void playAnswerSound();
}
