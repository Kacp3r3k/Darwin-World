package darwin.model;

import darwin.model.maps.GlobeMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobeMapTest {

    @Test
    void givenAnimalAtRightEdge_whenMovesEast_thenAppearsOnLeftEdge() {
        // Given
        GlobeMap map = new GlobeMap(10, 10);
        Animal animal = new Animal(new Vector2d(9, 5), 10, 1);
        map.place(animal);

        // Wymuszamy kierunek wschodni
        animal.setPosition(new Vector2d(9, 5));

        // When
        map.move(animal);

        // Then
        assertEquals(0, animal.getPosition().getX());
    }
}
