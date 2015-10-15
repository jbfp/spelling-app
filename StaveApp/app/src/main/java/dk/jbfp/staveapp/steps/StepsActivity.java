package dk.jbfp.staveapp.steps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
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
import dk.jbfp.staveapp.level.LevelActivity;
import dk.jbfp.staveapp.steps.Step.StepState;

public class StepsActivity extends Activity implements StepsView {
    public static final String USER_ID_KEY = "dk.jbfp.staveapp.USER_ID";

    private ListView stepsListView;

    private StepAdapter stepAdapter;
    private StepsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Step> steps = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Step step = new Step();

            if (i < 5) {
                step.state = StepState.Done;
            } else if (i < 6) {
                step.state = StepState.Open;
            } else {
                step.state = StepState.Locked;
            }

            steps.add(step);
        }

        this.stepAdapter = new StepAdapter(this, steps);

        this.stepsListView = (ListView) findViewById(R.id.steps_list_view);
        this.stepsListView.setAdapter(this.stepAdapter);
        this.stepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Step step = stepAdapter.getItem(position);
                presenter.onStepClicked(step);
            }
        });

        this.presenter = new StepsPresenter();
        this.presenter.setView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void navigateToLevelActivity(String[] words) {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LevelActivity.WORDS_KEY, words);
        startActivity(intent);
    }

    private final class StepAdapter extends BaseAdapter {
        private final Context context;
        private final List<Step> steps;

        public StepAdapter(Context context, List<Step> steps) {
            this.context = context;
            this.steps = steps;
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
            } else if (step.state == StepState.Done) {
                imageId = R.drawable.ic_done_black_48dp;
            }

            imageView.setImageResource(imageId);

            TextView textView = (TextView) view.findViewById(R.id.step_item_text);
            textView.setText("Niveau " + (position + 1));

            return view;
        }
    }
}
