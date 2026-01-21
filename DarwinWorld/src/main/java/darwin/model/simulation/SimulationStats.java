package darwin.model.simulation;

import darwin.model.Genotype;

import java.util.List;

public record SimulationStats(
        int day,
        int animalCount,
        int plantCount,
        int freeFieldsCount,
        double averageEnergy,
        double averageLifeSpan,
        double averageChildren,
        List<Genotype> topGenotypes,
        int fireCount
) {
}