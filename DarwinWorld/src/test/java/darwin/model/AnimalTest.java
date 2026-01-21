package darwin.model;

import darwin.model.maps.GlobeMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void givenAnimal_whenMove_thenPositionChangesAndEnergyDecreases() {
        // Given
        GlobeMap map = new GlobeMap(10, 10);
        Animal animal = new Animal(new Vector2d(5, 5), 10, 5);
        map.place(animal);

        Vector2d startPos = animal.getPosition();
        int startEnergy = animal.getEnergy();

        // When
        map.move(animal);

        // Then
        assertNotEquals(startPos, animal.getPosition(), "Animal should move");
        assertEquals(startEnergy - 1, animal.getEnergy(), "Animal should lose 1 energy after move");
    }
}
