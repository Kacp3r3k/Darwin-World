package darwin.model.maps;

import darwin.model.Animal;
import darwin.model.Vector2d;
import darwin.model.FireTile;
import darwin.model.WorldElement;
import darwin.model.simulation.SimulationConfig;
import darwin.model.simulation.SimulationStats;
import darwin.model.util.AnimalComparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireWorldMap extends GlobeMap {

    private final SimulationConfig config;

    private final Map<Vector2d, Integer> fires = new HashMap<>();

    public FireWorldMap(int width, int height, SimulationConfig config) {
        super(width, height);
        this.config = config;
    }

    @Override
    public void feedAnimals() {
        for (Vector2d position : animals.keySet()) {
            if (!plants.containsKey(position)) continue;

            List<Animal> candidates = animals.get(position);
            if (candidates == null || candidates.isEmpty()) continue;

            candidates.sort(new AnimalComparator());
            Animal strongest = candidates.get(0);

            if (Math.random() < config.fireProbability()) {
                fires.put(position, config.fireDuration());
                plants.remove(position); // roślina się spala
            } else {
                strongest.addEnergy(plantEnergy);
                plants.remove(position);
            }
        }
    }

    @Override
    public void growPlants() {
        spreadFires();
        burnAnimals();
        super.growPlants();
    }

    private void spreadFires() {
        Map<Vector2d, Integer> newFires = new HashMap<>();

        for (var entry : fires.entrySet()) {
            Vector2d pos = entry.getKey();
            int daysLeft = entry.getValue() - 1;

            if (daysLeft > 0) {
                newFires.put(pos, daysLeft);
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    Vector2d next = pos.add(new Vector2d(dx, dy));

                    if (next.follows(lowerLeft) && next.precedes(upperRight)) {
                        if (plants.containsKey(next) && !fires.containsKey(next)) {
                            newFires.put(next, config.fireDuration());
                            plants.remove(next);
                        }
                    }
                }
            }
        }

        fires.clear();
        fires.putAll(newFires);
    }

    private void burnAnimals() {
        for (Vector2d pos : fires.keySet()) {
            if (animals.containsKey(pos)) {
                for (Animal animal : animals.get(pos)) {
                    animal.subtractEnergy(config.animalBurnEnergyLoss());
                }
            }
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (fires.containsKey(position)) {
            return new FireTile(position);
        }
        return super.objectAt(position);
    }

    @Override
    public SimulationStats getStats(int currentDay) {
        SimulationStats base = super.getStats(currentDay);

        return new SimulationStats(
                base.day(),
                base.animalCount(),
                base.plantCount(),
                base.freeFieldsCount(),
                base.averageEnergy(),
                base.averageLifeSpan(),
                base.averageChildren(),
                base.topGenotypes(),
                fires.size()
        );
    }



}
