package dk.jbfp.staveapp.steps;

import java.util.List;

public interface StepsView {
    void navigateToLevelActivity(long stepId, String[] words);
    void showSteps(List<Step> steps);
}
