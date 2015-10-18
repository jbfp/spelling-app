package dk.jbfp.staveapp.stats;

import java.util.Collection;

public interface StatsView {
    void setEntries(Collection<StatAggregate> stats);
}
