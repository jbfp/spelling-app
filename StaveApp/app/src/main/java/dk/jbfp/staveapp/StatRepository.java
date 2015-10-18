package dk.jbfp.staveapp;

import java.util.List;

public interface StatRepository {
    Stat insertStat(Stat stat);
    List<Stat> getAllStats();
}
