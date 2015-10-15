package dk.jbfp.staveapp.steps;

import java.util.ArrayList;
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
    private StepRepository stepRepository;
    private StepsView view;
    private ArrayList<Step> steps;

    public StepsPresenter(User user, StepRepository stepRepository) {
        this.user = user;
        this.stepRepository = stepRepository;
        this.steps = new ArrayList<>();
        this.reloadSteps();
    }

    public void setView(StepsView view) {
        this.view = view;
        this.view.showSteps(steps);
    }

    public void onStepClicked(Step step) {
        if (step.state == Step.StepState.Locked) {
            return;
        }

        long stepId = step.id;
        int stepIndex = this.steps.indexOf(step);

        Random random = new Random(this.user.seed);
        List<String> words = Arrays.asList(twoLetterWords.clone());
        Collections.shuffle(words, random);
        String[] stepWords = new String[6];

        for (int i = 0; i < stepWords.length; i++) {
            stepWords[i] = words.get(i + stepIndex * stepWords.length);
        }

        this.view.navigateToLevelActivity(stepId, stepIndex + 1, stepWords);
    }

    public void onStepCompleted(long stepId) {
        int i;

        for (i = 0; i < this.steps.size(); i++) {
            Step step = this.steps.get(i);

            if (step.id == stepId) {
                step.state = Step.StepState.Done;
                this.stepRepository.updateStep(step);
                break;
            }
        }

        if ((++i) < this.steps.size()) {
            Step next = this.steps.get(i);
            next.state = Step.StepState.Open;
            this.stepRepository.updateStep(next);
        }

        this.reloadSteps();
        this.view.showSteps(steps);
    }

    private void reloadSteps() {
        this.steps.clear();
        this.steps.addAll(this.stepRepository.getStepsForUser(this.user.id));

        if (this.steps.isEmpty()) {
            for (int i = 0; i < twoLetterWords.length / 6; i++) {
                Step step = new Step();
                step.userId = this.user.id;
                step.state = Step.StepState.Locked;

                if (i == 0) {
                    step.state = Step.StepState.Open;
                }

                this.steps.add(this.stepRepository.addStep(step));
            }
        }
    }
}
