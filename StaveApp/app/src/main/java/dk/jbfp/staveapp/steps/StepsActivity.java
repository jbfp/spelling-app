package dk.jbfp.staveapp.steps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dk.jbfp.staveapp.R;
import dk.jbfp.staveapp.User;
import dk.jbfp.staveapp.data.Database;
import dk.jbfp.staveapp.level.LevelActivity;
import dk.jbfp.staveapp.stats.StatsActivity;
import dk.jbfp.staveapp.steps.Step.StepState;

public class StepsActivity extends Activity implements StepsView {
    public static final String USER_KEY = "dk.jbfp.staveapp.USER";
    private static final int LEVEL_REQUEST_CODE = 42;

    private ListView stepsListView;

    private StepAdapter stepAdapter;
    private StepsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        this.stepAdapter = new StepAdapter(this);
        this.stepsListView = (ListView) findViewById(R.id.steps_list_view);
        this.stepsListView.setAdapter(this.stepAdapter);
        this.stepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Step step = stepAdapter.getItem(position);
                presenter.onStepClicked(step);
            }
        });

        User user = getIntent().getParcelableExtra(USER_KEY);
        this.presenter = new StepsPresenter(user, new Database(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.presenter.setView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_steps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }

            case R.id.action_stats: {
                this.presenter.onStatsClicked();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void navigateToStatsActivity() {
        startActivity(new Intent(this, StatsActivity.class));
    }

    @Override
    public void navigateToLevelActivity(long stepId, int stepIndex, String[] words) {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LevelActivity.STEP_ID_KEY, stepId);
        intent.putExtra(LevelActivity.STEP_KEY, stepIndex);
        intent.putExtra(LevelActivity.WORDS_KEY, words);
        startActivityForResult(intent, LEVEL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LEVEL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                long stepId = data.getLongExtra(LevelActivity.STEP_ID_KEY, -1);
                boolean perfect = data.getBooleanExtra(LevelActivity.GAME_RESULT_KEY, false);
                this.presenter.onStepCompleted(stepId, perfect);
            }
        }
    }

    @Override
    public void showSteps(List<Step> steps) {
        this.stepAdapter.setSteps(steps);
    }

    private final class StepAdapter extends BaseAdapter {
        private final Context context;
        private final List<Step> steps;

        public StepAdapter(Context context) {
            this.context = context;
            this.steps = new ArrayList<>();
        }

        public void setSteps(List<Step> steps) {
            this.steps.clear();
            this.steps.addAll(steps);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.steps.size();
        }

        @Override
        public Step getItem(int position) {
            return this.steps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.layout_step_item, null, true);
            }

            Step step = getItem(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.step_item_image);
            int imageId = 0;

            if (step.state == StepState.Locked) {
                imageId = R.drawable.ic_lock_outline_black_48dp;
            } else if (step.state == StepState.Done || step.state == StepState.Perfect) {
                imageId = R.drawable.ic_done_black_48dp;
            }

            imageView.setImageResource(imageId);

            TextView textView = (TextView) view.findViewById(R.id.step_item_text);
            textView.setText("Niveau " + (position + 1));

            ImageView starImageView = (ImageView) view.findViewById(R.id.step_item_star);
            int starImageId = 0;

            if (step.state == StepState.Perfect) {
                starImageId = R.drawable.ic_star_black_48dp;
            } else {
                starImageId = R.drawable.ic_star_border_black_48dp;
            }

            starImageView.setImageResource(starImageId);

            return view;
        }
    }
}
