package dk.jbfp.staveapp.steps;

public class Step {
    public long id;
    public long userId;
    public StepState state;

    public enum StepState {
        Open,
        Locked,
        Done,
        Perfect
    }
}
