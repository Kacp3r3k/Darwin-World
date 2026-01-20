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
        int plantsToGrow = 10;

        int height = upperRight.getY() + 1;
        int width = upperRight.getX() + 1;

        int jungleHeight = (int) (height * 0.2);
        int jungleLowerY = (height - jungleHeight) / 2;
        int jungleUpperY = jungleLowerY + jungleHeight;

        Random random = new Random();

        for (int i = 0; i < plantsToGrow; i++) {
            Vector2d pos;
            boolean placed = false;
            int attempts = 0;

            while (!placed && attempts < 10) {
                attempts++;

                boolean preferEquator = random.nextDouble() < 0.8;

                if (preferEquator) {
                    int y = jungleLowerY + random.nextInt(jungleHeight + 1);
                    int x = random.nextInt(width);
                    pos = new Vector2d(x, y);
                } else {
                    boolean topPart = random.nextBoolean();
                    int y;
                    if (topPart) {
                        if (height - 1 > jungleUpperY) {
                            y = jungleUpperY + 1 + random.nextInt(height - 1 - jungleUpperY);
                        } else {
                            y = height - 1;
                        }
                    } else {
                        if (jungleLowerY > 0) {
                            y = random.nextInt(jungleLowerY);
                        } else {
                            y = 0;
                        }
                    }
                    int x = random.nextInt(width);
                    pos = new Vector2d(x, y);
                }

                if (!isOccupied(pos)) {
                    plants.put(pos, new Plant(pos));
                    placed = true;
                }
            }
        }
    }
}