package darwin.model.util;

import darwin.model.maps.WorldMap;
import darwin.model.simulation.SimulationStats;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, SimulationStats stats);
}