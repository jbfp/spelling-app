package dk.jbfp.staveapp.steps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import dk.jbfp.staveapp.R;

public class StepsActivity extends Activity implements StepsView {
    public static final String USER_ID_KEY = "dk.jbfp.staveapp.USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
}
