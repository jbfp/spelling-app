package dk.jbfp.staveapp.stats;

public class StatAggregate {
    private final String word;
    private int correctAnswers;
    private int incorrectAnswers;
    private long totalTimeSpent;
    private int totalListens;
    private int samples;

    public StatAggregate(String word) {
        this.word = word;
    }

    public void addEntry(boolean correct, long time, int listens) {
        if (correct) {
            this.correctAnswers++;
        } else {
            this.incorrectAnswers++;
        }

        this.totalTimeSpent += time;
        this.totalListens += listens;
        this.samples++;
    }

    public String getWord() {
        return this.word;
    }

    public int getCorrectAnswers() {
        return this.correctAnswers;
    }

    public int getIncorrectAnswers() {
        return this.incorrectAnswers;
    }

    public long getTotalTimeSpent() {
        return this.totalTimeSpent;
    }

    public long getAverageTimeSpent() {
        return this.totalTimeSpent / this.samples;
    }

    public int getTotalListens() {
        return this.totalListens;
    }

    public double getAverageListens() {
        return this.totalListens / (double) samples;
    }
}
