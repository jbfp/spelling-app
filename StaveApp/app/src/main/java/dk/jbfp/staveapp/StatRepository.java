package dk.jbfp.staveapp;

import java.util.List;

public interface StatRepository {
    void insertStat(String word, boolean correct, double time, int listens);
    List<Stat> getAllStats();
}
