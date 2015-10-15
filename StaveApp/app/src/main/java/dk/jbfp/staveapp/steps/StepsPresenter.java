package dk.jbfp.staveapp.steps;

public class StepsPresenter {
    private StepsView view;

    public void setView(StepsView view) {
        this.view = view;
    }

    public void onStepClicked(Step step) {
        String[] words = {
                "by",
                "bo",
                "da"
        };

        this.view.navigateToLevelActivity(words);
    }
}
