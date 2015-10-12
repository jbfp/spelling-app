package dk.jbfp.staveapp.level;

public interface LevelView {
    void onNextWord(Word previous, Word current);
    void onCompleted(boolean allCorrect);
}
