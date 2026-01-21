package darwin.presenter;

import darwin.model.simulation.SimulationConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ParametersPresenter {

    @FXML private TextField widthField;
    @FXML private TextField heightField;
    @FXML private TextField startAnimalsField;
    @FXML private TextField startEnergyField;
    @FXML private TextField plantEnergyField;
    @FXML private TextField moveCostField;
    @FXML private TextField plantsPerDayField;
    @FXML private TextField reproduceEnergyField;
    @FXML private TextField reproductionCostField;
    @FXML private TextField minMutationField;
    @FXML private TextField maxMutationField;
    @FXML private TextField genomeLengthField;

    @FXML
    public void initialize() {
        setupLimit(widthField, 500);
        setupLimit(heightField, 500);
    }

    private void setupLimit(TextField field, int max) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    int value = Integer.parseInt(newValue);

                    if (value > max) {
                        field.setText(String.valueOf(max));
                    }
                }
            } catch (NumberFormatException e) {
            }
        });
    }

    @FXML
    private void onSimulationStart() throws IOException {
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        int animals = Integer.parseInt(startAnimalsField.getText());
        int startEnergy = Integer.parseInt(startEnergyField.getText());
        int plantEnergy = Integer.parseInt(plantEnergyField.getText());
        int moveCost = Integer.parseInt(moveCostField.getText());
        int plantsPerDay = Integer.parseInt(plantsPerDayField.getText());
        int reproduceEnergy = Integer.parseInt(reproduceEnergyField.getText());
        int reproductionCost = Integer.parseInt(reproductionCostField.getText());
        int minMutation = Integer.parseInt(minMutationField.getText());
        int maxMutation = Integer.parseInt(maxMutationField.getText());
        int genomeLength = Integer.parseInt(genomeLengthField.getText());


        SimulationConfig config = new SimulationConfig(
                width, height,
                plantsPerDay * 5,
                plantEnergy,
                plantsPerDay,
                animals,
                startEnergy,
                reproduceEnergy,
                reproductionCost,
                minMutation,
                maxMutation,
                genomeLength,
                moveCost
        );

        startSimulationWindow(config);
    }

    private void startSimulationWindow(SimulationConfig config) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        presenter.setSimulationConfig(config);

        Stage stage = new Stage();
        stage.setTitle("Symulacja " + config.width() + "x" + config.height());
        stage.setScene(new Scene(viewRoot));
        stage.show();
    }
}