package darwin;

import darwin.model.maps.GlobeMap;
import darwin.model.maps.WorldMap;
import darwin.model.simulation.Simulation;
import darwin.model.simulation.SimulationConfig;

public class DarwinWorld {
    static void main() {
        SimulationConfig config = SimulationConfig.createDefault();

        WorldMap map = new GlobeMap(config.width(), config.height());

        map.registerMapChangeListener((worldMap, message) -> {
            System.out.println(message);
            System.out.println(worldMap);
        });

        Simulation simulation = new Simulation(map, config);

        Thread simulationThread = new Thread(simulation);
        simulationThread.start();

    }
}
