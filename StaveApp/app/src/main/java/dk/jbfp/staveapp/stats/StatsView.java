package dk.jbfp.staveapp.stats;

import java.util.Collection;
import java.util.Map;

public interface StatsView {
    void setEntries(Collection<StatAggregate> stats);
    void setHallOfFame(Map<String, String> map);
}
