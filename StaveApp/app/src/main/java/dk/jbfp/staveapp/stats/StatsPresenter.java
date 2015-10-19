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

            if (stats.isEmpty()) {
                return;
            }

            StatAggregate first = null;
            for (StatAggregate stat : stats) {
                first = stat;
                break;
            }

            Map<String, String> map = new HashMap<>();

            StatAggregate mostDifficult = first;
            StatAggregate leastDifficult = first;
            StatAggregate mostTimeConsuming = first;
            StatAggregate leastTimeConsuming = first;
            StatAggregate mostListens = first;
            StatAggregate fewestListens = first;

            for (StatAggregate stat : stats) {
                if (stat.getIncorrectAnswers() / (double) stat.getSamples() > mostDifficult.getIncorrectAnswers() / (double) mostDifficult.getSamples()) {
                    mostDifficult = stat;
                }

                if (stat.getCorrectAnswers() / (double) stat.getSamples() < leastDifficult.getCorrectAnswers() / (double) leastDifficult.getSamples()) {
                    leastDifficult = stat;
                }

                if (stat.getAverageTimeSpent() > mostTimeConsuming.getAverageTimeSpent()) {
                    mostTimeConsuming = stat;
                }

                if (stat.getAverageTimeSpent() < leastTimeConsuming.getAverageTimeSpent()) {
                    leastTimeConsuming = stat;
                }

                if (stat.getAverageListens() > mostListens.getAverageListens()) {
                    mostListens = stat;
                }

                if (stat.getAverageListens() < fewestListens.getAverageListens()) {
                    fewestListens = stat;
                }
            }

            final double NANOSECONDS_PER_SECOND = 1000000000.0d;
            map.put("Sværeste ord:", String.format("%s (%.2f forkerte svar per bruger)", mostDifficult.getWord(), mostDifficult.getIncorrectAnswers() / (double) mostDifficult.getSamples()));
            map.put("Nemmeste ord:", String.format("%s (%.2f korrekte svar per bruger)", leastDifficult.getWord(), leastDifficult.getCorrectAnswers() / (double) leastDifficult.getSamples()));
            map.put("Mest tidskrævende ord:", String.format("%s (%.2f s per bruger)", mostTimeConsuming.getWord(), mostTimeConsuming.getAverageTimeSpent() / NANOSECONDS_PER_SECOND));
            map.put("Mindst tidskrævende ord:", String.format("%s (%.2f s per bruger)", leastTimeConsuming.getWord(), leastTimeConsuming.getAverageTimeSpent() / NANOSECONDS_PER_SECOND));
            map.put("Utydeligste ord:", String.format("%s (%.2f lyt per bruger)", mostListens.getWord(), mostListens.getAverageListens()));
            map.put("Tydeligste ord:", String.format("%s (%.2f lyt per bruger)", fewestListens.getWord(), fewestListens.getAverageListens()));

            view.setHallOfFame(map);
        }
    }
}
