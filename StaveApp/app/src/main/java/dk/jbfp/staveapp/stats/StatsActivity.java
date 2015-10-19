package dk.jbfp.staveapp.stats;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collection;
import java.util.Map;

import dk.jbfp.staveapp.R;
import dk.jbfp.staveapp.data.Database;

public class StatsActivity extends Activity implements StatsView {
    private TableLayout table;
    private TableLayout hof;
    private StatsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_stats);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.table = (TableLayout) findViewById(R.id.stats_table);
        this.hof = (TableLayout) findViewById(R.id.stats_hof_table);
        this.presenter = new StatsPresenter(new Database(this));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        this.presenter.setView(this);
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void setEntries(Collection<StatAggregate> stats) {
        final double NANOSECONDS_PER_SECOND = 1000000000.0d;

        for (StatAggregate stat : stats) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.layout_stat_row, null);
            ((TextView) row.findViewById(R.id.stat_word)).setText(stat.getWord());
            ((TextView) row.findViewById(R.id.stat_correct)).setText(String.valueOf(stat.getCorrectAnswers()));
            ((TextView) row.findViewById(R.id.stat_incorrect)).setText(String.valueOf(stat.getIncorrectAnswers()));
            ((TextView) row.findViewById(R.id.stat_average_time)).setText(String.format("%1$,.2f s", stat.getAverageTimeSpent() / NANOSECONDS_PER_SECOND));
            ((TextView) row.findViewById(R.id.stat_total_time)).setText(String.format("%1$,.2f s", stat.getTotalTimeSpent() / NANOSECONDS_PER_SECOND));
            ((TextView) row.findViewById(R.id.stat_average_listens)).setText(String.format("%1$,.2f gange", stat.getAverageListens()));
            ((TextView) row.findViewById(R.id.stat_total_listens)).setText(String.format("%d gange", stat.getTotalListens()));
            table.addView(row);
        }

        table.requestLayout();
    }

    @Override
    public void setHallOfFame(Map<String, String> map) {
        for (String key : map.keySet()) {
            String value = map.get(key);

            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.layout_stat_hof_row, null);
            ((TextView) row.findViewById(R.id.stats_hof_key)).setText(key);
            ((TextView) row.findViewById(R.id.stats_hof_value)).setText(value);
            hof.addView(row);
        }

        hof.requestLayout();
    }
}
