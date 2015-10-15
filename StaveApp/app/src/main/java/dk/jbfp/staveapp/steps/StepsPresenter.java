package dk.jbfp.staveapp.steps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dk.jbfp.staveapp.StepRepository;
import dk.jbfp.staveapp.User;

public class StepsPresenter {
    private static final String[] twoLetterWords = {
            "bi",
            "bo",
            "by",
            "bæ",
            "da",
            "du",
            "dø",
            "en",
            "fe",
            "fy",
            "fæ",
            "få",
            "gø",
            "gå",
            "ha",
            "hi",
            "ho",
            "hæ",
            "hø",
            "is",
            "ja",
            "jo",
            "ko",
            "kø",
            "le",
            "lo",
            "lå",
            "må",
            "ni",
            "nu",
            "ny",
            "nå",
            "på",
            "ro",
            "rå",
            "se",
            "si",
            "so",
            "sy",
            "sø",
            "så",
            "te",
            "ti",
            "to",
            "tø",
            "tå",
            "uf",
            "vi",
            "æg",
            "øl",
            "ål",
            "Bo",
            "Ea",
            "Ib"
    };

    private User user;
    private StepRepository steps;
    private StepsView view;

    public StepsPresenter(User user, StepRepository steps) {
        this.user = user;
        this.steps = steps;
    }

    public void setView(StepsView view) {
        this.view = view;

        List<Step> steps = this.steps.getStepsForUser(this.user.id);

        if (steps.isEmpty()) {
            for (int i = 0; i < twoLetterWords.length / 6; i++) {
                Step step = new Step();
                step.userId = this.user.id;
                step.state = Step.StepState.Locked;

                if (i == 0) {
                    step.state = Step.StepState.Open;
                }

                steps.add(this.steps.addStep(step));
            }
        }

        this.view.showSteps(steps);
    }

    public void onStepClicked(Step step) {
        if (step.state == Step.StepState.Locked) {
            return;
        }
        
        Random random = new Random(this.user.seed);
        List<String> words = Arrays.asList(twoLetterWords);
        Collections.shuffle(words, random);
        String[] stepWords = new String[6];

        for (int i = 0; i < stepWords.length; i++) {
            stepWords[i] = words.get(i);
        }

        this.view.navigateToLevelActivity(stepWords);
    }
}
