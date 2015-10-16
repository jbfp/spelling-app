package dk.jbfp.staveapp.steps;

public class Step {
    public long id;
    public long userId;
    public StepState state;
    public int length;
    public int offset;

    public enum StepState {
        Open,
        Locked,
        Done,
        Perfect
    }
}
