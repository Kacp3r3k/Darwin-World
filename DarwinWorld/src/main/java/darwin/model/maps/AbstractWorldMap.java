package darwin.model.maps;

import darwin.model.*;
import darwin.model.util.AnimalComparator;
import darwin.model.util.Boundary;
import darwin.model.util.MapChangeListener;
import darwin.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;

    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();

    private final UUID id = UUID.randomUUID();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> mapChangeListeners = new ArrayList<>();

    protected final int plantEnergy = 5;
    protected final int energyToReproduce = 10;
    protected final int reproductionCost = 5;

    public AbstractWorldMap(int width, int height) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
    }

    public void registerMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.add(listener);
    }

    protected void mapChanged(String message) {
        for (MapChangeListener listener : mapChangeListeners) {
            listener.mapChanged(this, message);
        }
    }

    @Override
    public void place(Animal animal) {
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        mapChanged("Placed animal at " + animal.getPosition());
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPos = animal.getPosition();
        List<Animal> list = animals.get(oldPos);
        if(list != null) {
            list.remove(animal);
            if(list.isEmpty()) animals.remove(oldPos);
        }

        animal.move(this);

        place(animal);
    }

    @Override
    public void removeDeadAnimals() {
        List<Animal> dead = new ArrayList<>();
        for(List<Animal> list : animals.values()){
            for(Animal a : list){
                if(a.getEnergy() <= 0){
                    a.markAsDead(0);
                    dead.add(a);
                }
            }
            list.removeAll(dead);
        }
    }

    @Override
    public void feedAnimals() {
        for (Vector2d position : animals.keySet()) {
            if (plants.containsKey(position)) {
                List<Animal> candidates = animals.get(position);
                if(!candidates.isEmpty()) {
                    candidates.sort(new AnimalComparator());
                    Animal strongest = candidates.get(0);
                    strongest.addEnergy(plantEnergy);
                    plants.remove(position);
                }
            }
        }
    }

    @Override
    public void reproduceAnimals() {
        for (List<Animal> candidates : animals.values()) {
            if (candidates.size() >= 2) {
                candidates.sort(new AnimalComparator());
                Animal p1 = candidates.get(0);
                Animal p2 = candidates.get(1);

                if (p1.getEnergy() >= energyToReproduce && p2.getEnergy() >= energyToReproduce) {
                    p1.subtractEnergy(reproductionCost);
                    p2.subtractEnergy(reproductionCost);

                    Animal child = new Animal(p1.getPosition(),
                            2 * reproductionCost,
                            p1, p2, 0, 10);
                    place(child);
                }
            }
        }
    }

    @Override
    public abstract void growPlants();

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) || plants.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (animals.containsKey(position) && !animals.get(position).isEmpty()) {
            return animals.get(position).get(0);
        }
        return plants.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(plants.values());
        animals.values().forEach(elements::addAll);
        return elements;
    }

    @Override
    public int getId() { return id.hashCode(); }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    @Override
    public String toString() { return visualizer.draw(lowerLeft, upperRight); }
}