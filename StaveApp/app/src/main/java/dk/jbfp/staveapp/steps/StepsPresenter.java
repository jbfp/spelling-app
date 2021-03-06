package dk.jbfp.staveapp.steps;

import android.os.AsyncTask;

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
            "te",
            "ti",
            "to",
            "tø",
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
    }

    public void setView(StepsView view) {
        this.view = view;
        this.reloadSteps();
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
        String[] stepWords = new String[step.length];

        for (int i = 0; i < stepWords.length; i++) {
            stepWords[i] = words.get(i + step.offset);
        }

        this.view.navigateToLevelActivity(stepId, stepIndex + 1, stepWords);
    }

    public void onStepCompleted(long stepId, boolean perfect) {
        int i;

        for (i = 0; i < this.steps.size(); i++) {
            Step step = this.steps.get(i);

            if (step.id == stepId) {
                if (step.state == Step.StepState.Perfect) {
                    // Don't overwrite an already perfect step with a mere 'done'.
                    break;
                }

                if (perfect) {
                    step.state = Step.StepState.Perfect;
                } else {
                    step.state = Step.StepState.Done;
                }

                this.stepRepository.updateStep(step);
                break;
            }
        }

        if ((++i) < this.steps.size()) {
            Step next = this.steps.get(i);

            if (next.state == Step.StepState.Locked) {
                next.state = Step.StepState.Open;
                this.stepRepository.updateStep(next);
            }
        }

        this.reloadSteps();
        this.view.showSteps(steps);
    }

    private void reloadSteps() {
        new LoadStepsAsyncTask().execute();
    }

    public void onStatsClicked() {
        this.view.navigateToStatsActivity();
    }

    private class LoadStepsAsyncTask extends AsyncTask<Void, Void, List<Step>> {

        @Override
        protected List<Step> doInBackground(Void... params) {
            List<Step> steps = stepRepository.getStepsForUser(user.id);

            if (steps.isEmpty()) {
                for (int i = 0; i < twoLetterWords.length;) {
                    int numWordsForLevel = i + 1;

                    if (numWordsForLevel > 6) {
                        numWordsForLevel = 6;
                    }

                    Step step = new Step();
                    step.userId = user.id;
                    step.state = Step.StepState.Locked;
                    step.length = numWordsForLevel;
                    step.offset = i;

                    if (i == 0) {
                        step.state = Step.StepState.Open;
                    }

                    steps.add(stepRepository.addStep(step));
                    i += numWordsForLevel;
                }
            }

            return steps;
        }

        @Override
        protected void onPostExecute(List<Step> result) {
            steps.clear();
            steps.addAll(result);
            view.showSteps(steps);
        }
    }
}
