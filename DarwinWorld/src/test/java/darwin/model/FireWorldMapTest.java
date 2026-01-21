package darwin.model;

import darwin.model.maps.FireWorldMap;
import darwin.model.simulation.SimulationConfig;
import darwin.model.simulation.SimulationVariant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireWorldMapTest {

    @Test
    void givenFireProbabilityOne_whenAnimalEatsPlant_thenFireStarts() {
        // Given
        SimulationConfig config = new SimulationConfig(
                1, 1,      // mapa 1x1
                1,
                5,
                1,
                1,
                10,
                5,
                2,
                0,
                0,
                5,
                1,
                SimulationVariant.FIRE,
                1.0,
                3,
                2,
                2
        );

        FireWorldMap map = new FireWorldMap(1, 1, config);

        Vector2d pos = new Vector2d(0, 0);
        Animal animal = new Animal(pos, 10, 5);
        map.place(animal);

        // roślina pojawi się na jedynym polu
        map.growPlants();

        // When
        map.feedAnimals();

        // Then
        assertTrue(map.objectAt(pos) instanceof FireTile);
        assertEquals(10, animal.getEnergy());
    }
}
