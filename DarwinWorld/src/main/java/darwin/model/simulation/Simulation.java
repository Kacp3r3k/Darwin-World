package darwin.model.simulation;

import darwin.model.Animal;
import darwin.model.Vector2d;
import darwin.model.maps.AbstractWorldMap;
import darwin.model.maps.WorldMap;
import java.util.Random;

public class Simulation implements Runnable {
    private final WorldMap map;
    private final SimulationConfig config;
    private volatile boolean running = true;
    private final Random random = new Random();
    private int day = 0;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public Simulation(WorldMap map, SimulationConfig config) {
        this.map = map;
        this.config = config;
        populate();
    }

    private void populate() {
        for (int i = 0; i < config.startAnimals(); i++) {
            Vector2d pos = getRandomPosition();
            Animal animal = new Animal(pos, config.startEnergy(), config.genomeLength());
            map.place(animal);
        }
        map.growPlants();
    }

    private Vector2d getRandomPosition() {
        int x = random.nextInt(config.width());
        int y = random.nextInt(config.height());
        return new Vector2d(x, y);
    }

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            day++;
            if (map instanceof AbstractWorldMap abstractMap) {
                abstractMap.removeDeadAnimals(day);
            } else {
                map.removeDeadAnimals(day);
            }

            var allAnimals = map.getElements().stream()
                    .filter(e -> e instanceof Animal)
                    .map(e -> (Animal) e)
                    .toList();

            for (Animal animal : allAnimals) {
                map.move(animal);
            }

            map.feedAnimals();
            map.reproduceAnimals();
            map.growPlants();

            SimulationStats stats = map.getStats(day);
            map.mapChanged(stats);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void pause() {
        paused = true;
    }
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notify();
        }
    }

}