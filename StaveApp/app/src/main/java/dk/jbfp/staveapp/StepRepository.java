package dk.jbfp.staveapp;

import java.util.List;

import dk.jbfp.staveapp.steps.Step;

public interface StepRepository {
    List<Step> getStepsForUser(long userId);
    Step addStep(Step step);
    Step updateStep(Step step);
}
