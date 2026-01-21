package darwin.presenter;

import darwin.model.*;
import darwin.model.maps.WorldMap;
import darwin.model.simulation.SimulationStats;
import darwin.model.util.MapChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import darwin.model.maps.GlobeMap;
import darwin.model.simulation.Simulation;
import darwin.model.simulation.SimulationConfig;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class SimulationPresenter implements MapChangeListener {

    private SimulationConfig config;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label infoLabel;

    @FXML private Label dayLabel;
    @FXML private Label animalCountLabel;
    @FXML private Label plantCountLabel;
    @FXML private Label freeFieldsLabel;
    @FXML private Label avgEnergyLabel;
    @FXML private Label avgLifeLabel;
    @FXML private Label avgChildrenLabel;
    @FXML private Label popularGenotypeLabel;
    @FXML private Label fireCountLabel;

    @FXML private StackPane mapContainer;
    @FXML private Canvas mapCanvas;

    private WorldMap worldMap;
    private Simulation simulation;

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }
    public void setSimulationConfig(SimulationConfig config) {
        this.config = config;
    }

    @FXML
    public void onSimulationStart() {
        if (simulation != null) {
            simulation.resume();
            return;
        }

        if (this.config == null) {
            this.config = SimulationConfig.createDefault();
        }

        this.simulation = new Simulation(this.config);
        this.simulation.getMap().registerMapChangeListener(this);

        Thread thread = new Thread(simulation);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void onSimulationStop() {
        if (simulation != null) {
            simulation.pause();
        }
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

        if (freeFieldsLabel != null) {
            freeFieldsLabel.setText(String.valueOf(stats.freeFieldsCount()));
        }

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
        if (fireCountLabel != null) {
            fireCountLabel.setText(String.valueOf(stats.fireCount()));
        }
    }
    @FXML
    public void initialize() {
        mapCanvas.widthProperty().bind(mapContainer.widthProperty());
        mapCanvas.heightProperty().bind(mapContainer.heightProperty());

        mapContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (worldMap != null) drawMap(worldMap);
        });
        mapContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (worldMap != null) drawMap(worldMap);
        });
    }

    private void drawMap(WorldMap worldMap) {
        if (mapCanvas == null || mapCanvas.getGraphicsContext2D() == null) return;

        double w = mapCanvas.getWidth();
        double h = mapCanvas.getHeight();

        if (w <= 0 || h <= 0) return;

        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.LIGHTGRAY); // Tło
        gc.fillRect(0, 0, w, h);

        int mapWidth = config.width();
        int mapHeight = config.height();

        if (mapWidth == 0 || mapHeight == 0) return;

        double cellWidth = w / mapWidth;
        double cellHeight = h / mapHeight;

        double cellSize = Math.min(cellWidth, cellHeight);

        double xOffset = (w - (cellSize * mapWidth)) / 2;
        double yOffset = (h - (cellSize * mapHeight)) / 2;

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d pos = new Vector2d(x, y);
                WorldElement object = worldMap.objectAt(pos);

                double pixelX = xOffset + (x * cellSize);
                double pixelY = yOffset + ((mapHeight - 1 - y) * cellSize);

                if (object instanceof Animal) {
                    gc.setFill(Color.BROWN);
                    gc.fillOval(pixelX, pixelY, cellSize, cellSize);
                } else if (object instanceof Plant) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(pixelX, pixelY, cellSize, cellSize);
                }
                else if (object instanceof FireTile) {
                    gc.setFill(Color.RED);
                    gc.fillRect(pixelX, pixelY, cellSize, cellSize);
                }
            }
        }
    }
}