package dk.jbfp.staveapp.steps;

public class Step {
    public StepState state;

    public enum StepState {
        Open,
        Locked,
        Done
    }
}
