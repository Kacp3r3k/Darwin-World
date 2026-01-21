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
        int genomeLength,
        int moveCost,
        SimulationVariant variant,
        double fireProbability,
        int fireDuration,
        int animalBurnDuration,
        int animalBurnEnergyLoss


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
                5,
                1,
                SimulationVariant.STANDARD,
                0.01,
                3,
                2,
                2
        );
    }
    public SimulationVariant getVariant() {
        return variant;
    }

}