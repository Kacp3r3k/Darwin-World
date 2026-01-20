package darwin.model.maps;

import darwin.model.*;
import darwin.model.simulation.SimulationStats;
import darwin.model.util.Boundary;
import darwin.model.util.MapChangeListener;

import java.util.List;

public interface WorldMap extends MoveValidator {
    void place(Animal animal);
    void move(Animal animal);
    boolean isOccupied(Vector2d position);
    WorldElement objectAt(Vector2d position);
    List<WorldElement> getElements();
    int getId();


    void removeDeadAnimals(int currentDay);
    void feedAnimals();
    void reproduceAnimals();
    void growPlants();

    void registerMapChangeListener(MapChangeListener listener);
    Boundary getCurrentBounds();
    void mapChanged(SimulationStats stats);
    SimulationStats getStats(int currentDay);
}