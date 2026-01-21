package darwin.model;

import darwin.model.maps.GlobeMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReproductionTest {

    @Test
    void givenTwoAnimalsWithEnoughEnergy_whenReproduce_thenChildIsCreated() {
        // Given
        GlobeMap map = new GlobeMap(10, 10);

        Animal p1 = new Animal(new Vector2d(3, 3), 20, 5);
        Animal p2 = new Animal(new Vector2d(3, 3), 20, 5);

        map.place(p1);
        map.place(p2);

        // When
        map.reproduceAnimals();

        // Then
        long animalCount = map.getElements().stream()
                .filter(e -> e instanceof Animal)
                .count();

        assertEquals(3, animalCount, "One child should be created");
        assertEquals(15, p1.getEnergy(), "Parent 1 energy reduced");
        assertEquals(15, p2.getEnergy(), "Parent 2 energy reduced");
    }
}
