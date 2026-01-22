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

    @FXML private Label animalGenomeLabel;
    @FXML private Label animalActiveGeneLabel;
    @FXML private Label animalEnergyLabel;
    @FXML private Label animalPlantsEatenLabel;
    @FXML private Label animalChildrenLabel;
    @FXML private Label animalDescendantsLabel;
    @FXML private Label animalAgeLabel;
    @FXML private Label animalDeathLabel;


    @FXML private StackPane mapContainer;
    @FXML private Canvas mapCanvas;

    private WorldMap worldMap;
    private Simulation simulation;
    private Genotype dominantGenotype;
    private Animal selectedAnimal;
    private double lastCellSize;
    private double lastXOffset;
    private double lastYOffset;

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

        this.simulation = new Simulation(config);
        this.worldMap = simulation.getMap();
        worldMap.registerMapChangeListener(this);

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

            if (selectedAnimal != null) {
                updateSelectedAnimalPanel();
            }
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
        if (stats.topGenotypes() != null && !stats.topGenotypes().isEmpty()) {
            dominantGenotype = stats.topGenotypes().get(0);
        } else {
            dominantGenotype = null;
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

        mapCanvas.setOnMouseClicked(event -> {

            if (worldMap == null || simulation == null || !simulation.isPaused()) {
                return;
            }

            if (lastCellSize <= 0) return;

            int x = (int) ((event.getX() - lastXOffset) / lastCellSize);
            int y = config.height() - 1
                    - (int) ((event.getY() - lastYOffset) / lastCellSize);

            if (x < 0 || y < 0 || x >= config.width() || y >= config.height()) {
                return;
            }

            WorldElement element = worldMap.objectAt(new Vector2d(x, y));

            if (element instanceof Animal animal) {
                selectedAnimal = animal;
                updateSelectedAnimalPanel();
            }
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

        this.lastCellSize = cellSize;
        this.lastXOffset = xOffset;
        this.lastYOffset = yOffset;

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d pos = new Vector2d(x, y);
                WorldElement object = worldMap.objectAt(pos);

                double pixelX = xOffset + (x * cellSize);
                double pixelY = yOffset + ((mapHeight - 1 - y) * cellSize);

                // Preferowane pola roślin
                int jungleHeight = (int) (mapHeight * 0.2);
                int jungleLowerY = (mapHeight - jungleHeight) / 2;
                int jungleUpperY = jungleLowerY + jungleHeight - 1;

                if (y >= jungleLowerY && y <= jungleUpperY) {
                    gc.setFill(Color.rgb(180, 255, 180)); // jasna zieleń
                    gc.fillRect(pixelX, pixelY, cellSize, cellSize);
                }

                if (object instanceof Animal animal) {
                    // Zwierzak
                    gc.setFill(Color.BROWN);
                    gc.fillOval(pixelX, pixelY, cellSize, cellSize);

                    // Pasek energii
                    int energy = animal.getEnergy();
                    int maxEnergy = config.startEnergy(); // punkt odniesienia

                    double energyRatio = Math.max(0, Math.min(1.0, (double) energy / maxEnergy));
                    double barWidth = cellSize * energyRatio;
                    double barHeight = cellSize * 0.15;

                    double barX = pixelX;
                    double barY = pixelY + cellSize + 2;

                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(barX, barY, cellSize, barHeight);

                    gc.setFill(Color.color(1 - energyRatio, energyRatio, 0));
                    gc.fillRect(barX, barY, barWidth, barHeight);

                    // Dominujący genom
                    if (dominantGenotype != null &&
                            animal.getGenotype().equals(dominantGenotype)) {

                        gc.setStroke(Color.BLUE);
                        gc.setLineWidth(cellSize * 0.12);
                        gc.strokeOval(pixelX, pixelY, cellSize, cellSize);
                    }
                }
                else if (object instanceof Plant) {
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

    private void updateSelectedAnimalPanel() {
        if (selectedAnimal == null) return;

        animalGenomeLabel.setText("Genom: " + selectedAnimal.getGenotype());
        animalEnergyLabel.setText("Energia: " + selectedAnimal.getEnergy());
        animalPlantsEatenLabel.setText(
                "Zjedzone rośliny: " + selectedAnimal.getPlantsEaten()
        );
        animalChildrenLabel.setText(
                "Dzieci: " + selectedAnimal.getChildrenCount()
        );

        animalDescendantsLabel.setText(
                "Potomkowie: " + selectedAnimal.getChildrenCount() // uproszczone
        );

        if (selectedAnimal.isDead()) {
            animalAgeLabel.setText("Długość życia: " + selectedAnimal.getAge());
            animalDeathLabel.setText("Dzień śmierci: " + selectedAnimal.getDeathDay());
        } else {
            animalAgeLabel.setText("Wiek: " + selectedAnimal.getAge());
            animalDeathLabel.setText("Status: żyje");
        }
    }

}