package darwin.model.simulation;

public record SimulationConfig(
        int width,
        int height,
        int startPlants,
        int plantEnergy,
        int plantsPerDay,
        int startAnimals,
        int startEnergy,
        int energyToReproduce,
        int reproductionCost,
        int minMutations,
        int maxMutations,
        int genomeLength
) {
    public static SimulationConfig createDefault() {
        return new SimulationConfig(
                10, 10,
                5,
                3,
                1,
                5,
                10,
                5,
                2,
                0,
                2,
                5
        );
    }
}