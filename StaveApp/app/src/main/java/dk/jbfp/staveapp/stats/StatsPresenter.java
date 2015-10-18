package dk.jbfp.staveapp.stats;

import android.os.AsyncTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.jbfp.staveapp.Stat;
import dk.jbfp.staveapp.StatRepository;

public class StatsPresenter {
    private final StatRepository statRepository;
    private StatsView view;

    public StatsPresenter(StatRepository stats) {
        this.statRepository = stats;
    }

    public void setView(StatsView view) {
        this.view = view;
        new GetStatsAsyncTask().execute();
    }

    private class GetStatsAsyncTask extends AsyncTask<Void, Void, Collection<StatAggregate>> {
        @Override
        protected Collection<StatAggregate> doInBackground(Void... params) {
            List<Stat> stats = statRepository.getAllStats();

            Map<String, StatAggregate> aggregates = new HashMap<>();

            for (Stat stat : stats) {
                String key = stat.word;
                StatAggregate aggregate = aggregates.get(key);

                if (aggregate == null) {
                    aggregate = new StatAggregate(key);
                    aggregates.put(key, aggregate);
                }

                aggregate.addEntry(stat.correct, stat.time, stat.listens);
            }

            return aggregates.values();
        }

        @Override
        protected void onPostExecute(Collection<StatAggregate> stats) {
            view.setEntries(stats);
        }
    }
}
