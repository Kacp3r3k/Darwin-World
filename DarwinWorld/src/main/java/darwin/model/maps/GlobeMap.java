package darwin.model.maps;

import darwin.model.Animal;
import darwin.model.Plant;
import darwin.model.Vector2d;

import java.util.List;
import java.util.Random;

public class GlobeMap extends AbstractWorldMap {

    public GlobeMap(int width, int height) {
        super(width, height);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() >= lowerLeft.getY() && position.getY() <= upperRight.getY();
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

        Vector2d pos = animal.getPosition();
        int width = upperRight.getX() + 1;

        if (pos.getX() < 0 || pos.getX() >= width) {
            int newX = (pos.getX() % width + width) % width;
            animal.setPosition(new Vector2d(newX, pos.getY()));
        }

        place(animal);
    }

    @Override
    public void growPlants() {
        Random rand = new Random();
        for(int i=0; i<10; i++) {
            int y = rand.nextInt(upperRight.getY() + 1);
            int x = rand.nextInt(upperRight.getX() + 1);
            Vector2d pos = new Vector2d(x, y);

            if(!isOccupied(pos)) {
                plants.put(pos, new Plant(pos));
            }
        }
    }
}