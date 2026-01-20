package darwin.presenter;

import darwin.model.*;
import darwin.model.maps.WorldMap;
import darwin.model.simulation.SimulationStats;
import darwin.model.util.MapChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import darwin.model.maps.GlobeMap;
import darwin.model.simulation.Simulation;
import darwin.model.simulation.SimulationConfig;

public class SimulationPresenter implements MapChangeListener {

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label infoLabel;

    @FXML private Label dayLabel;
    @FXML private Label animalCountLabel;
    @FXML private Label plantCountLabel;
    @FXML private Label avgEnergyLabel;
    @FXML private Label avgLifeLabel;
    @FXML private Label avgChildrenLabel;
    @FXML private Label popularGenotypeLabel;

    private WorldMap worldMap;
    private Simulation simulation;

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    @FXML
    public void onSimulationStart() {
        SimulationConfig config = SimulationConfig.createDefault();
        this.worldMap = new GlobeMap(config.width(), config.height());

        worldMap.registerMapChangeListener(this);

        this.simulation = new Simulation(worldMap, config);
        Thread thread = new Thread(simulation);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void onSimulationStop() {
        if(simulation != null) simulation.stop();
    }

    @Override
    public void mapChanged(WorldMap worldMap, SimulationStats stats) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            updateStats(stats);
        });
    }

    private void updateStats(SimulationStats stats) {
        dayLabel.setText(String.valueOf(stats.day()));
        animalCountLabel.setText(String.valueOf(stats.animalCount()));
        plantCountLabel.setText(String.valueOf(stats.plantCount()));
        avgEnergyLabel.setText(String.format("%.2f", stats.averageEnergy()));
        avgLifeLabel.setText(String.format("%.2f", stats.averageLifeSpan()));
        avgChildrenLabel.setText(String.format("%.2f", stats.averageChildren()));

        if (stats.topGenotypes() == null || stats.topGenotypes().isEmpty()) {
            popularGenotypeLabel.setText("-");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Genotype g : stats.topGenotypes()) {
                sb.append(g.toString()).append("\n");
            }
            popularGenotypeLabel.setText(sb.toString().trim());
        }
    }

    private void drawMap(WorldMap worldMap) {
        mapGrid.getChildren().clear();

        int width = worldMap.getCurrentBounds().upperRight().getX();
        int height = worldMap.getCurrentBounds().upperRight().getY();

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                Vector2d pos = new Vector2d(x, y);
                WorldElement object = worldMap.objectAt(pos);

                if (object instanceof Animal) {
                    Circle animalShape = new Circle(15, Color.ORANGE);
                    mapGrid.add(animalShape, x, height - y);
                } else if (object instanceof Plant) {
                    Rectangle plantShape = new Rectangle(30, 30, Color.GREEN);
                    mapGrid.add(plantShape, x, height - y);
                } else {
                    // Puste pole
                }
            }
        }
    }
}