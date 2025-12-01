package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Task;
import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class TasksController {

    @FXML private Button backButton;
    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> deadlineColumn;
    @FXML private Pane backgroundPane;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        spawnPaws();
        loadTasks();
    }

    private void spawnPaws() {
        Image pawImage = new Image(getClass().getResourceAsStream("/images/paw.png"));

        for (int i = 0; i < 12; i++) {
            ImageView paw = new ImageView(pawImage);
            paw.setFitWidth(40);
            paw.setFitHeight(40);
            paw.setOpacity(0.4);
            paw.setRotate(random.nextInt(360));

            placePawRandomly(paw);

            paw.setOnMouseClicked(e -> handlePawClick(paw));

            backgroundPane.getChildren().add(paw);
        }
    }

    private void placePawRandomly(ImageView paw) {
        double x = random.nextDouble() * 900;
        double y = random.nextDouble() * 600;
        paw.setLayoutX(x);
        paw.setLayoutY(y);
    }

    private void handlePawClick(ImageView paw) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), paw);
        fadeOut.setFromValue(0.4);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            paw.setVisible(false);

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> respawnPaw(paw));
            pause.play();
        });
        fadeOut.play();
    }

    private void respawnPaw(ImageView paw) {
        placePawRandomly(paw);
        paw.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), paw);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.4);
        fadeIn.play();
    }

    private void loadTasks() {
        new Thread(() -> {
            String json = ApiClient.getTasks(1L);

            Platform.runLater(() -> {
                try {
                    Gson gson = new Gson();
                    List<Task> taskList = gson.fromJson(json, new TypeToken<List<Task>>(){}.getType());
                    if (taskList != null) {
                        ObservableList<Task> data = FXCollections.observableArrayList(taskList);
                        tasksTable.setItems(data);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing tasks");
                }
            });
        }).start();
    }

    @FXML
    protected void onBackClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 950, 650);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}