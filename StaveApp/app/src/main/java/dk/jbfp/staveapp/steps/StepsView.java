package dk.jbfp.staveapp.steps;

import java.util.List;

public interface StepsView {
    void navigateToLevelActivity(String[] words);
    void showSteps(List<Step> steps);
}
