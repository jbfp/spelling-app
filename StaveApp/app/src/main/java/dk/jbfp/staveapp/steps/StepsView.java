package dk.jbfp.staveapp.steps;

import java.util.List;

public interface StepsView {
    void navigateToStatsActivity();
    void navigateToLevelActivity(long stepId, int stepIndex, String[] words);
    void showSteps(List<Step> steps);
}
