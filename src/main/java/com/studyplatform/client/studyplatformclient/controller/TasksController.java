package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Task;
import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
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
import java.util.ArrayList;
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
    private final List<Image> pawImages = new ArrayList<>();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        loadPawImages();
        spawnPaws();
        animateTable();
        loadTasks();
    }

    private void loadPawImages() {
        try {
            pawImages.add(new Image(getClass().getResourceAsStream("/images/paw.png")));
        } catch (Exception e) {
            System.out.println("Could not load paw images");
        }
    }

    private void spawnPaws() {
        if (pawImages.isEmpty()) return;
        for (int i = 0; i < 10; i++) {
            Image randomImage = pawImages.get(random.nextInt(pawImages.size()));
            ImageView paw = new ImageView(randomImage);
            paw.setFitWidth(40);
            paw.setFitHeight(40);
            paw.setOpacity(0.4);
            paw.setRotate(random.nextInt(360));
            placePawRandomly(paw);
            backgroundPane.getChildren().add(paw);
        }
    }

    private void placePawRandomly(ImageView paw) {
        paw.setLayoutX(random.nextDouble() * 900);
        paw.setLayoutY(random.nextDouble() * 600);
    }

    private void animateTable() {
        tasksTable.setOpacity(0);
        tasksTable.setTranslateY(20);

        FadeTransition fade = new FadeTransition(Duration.millis(800), tasksTable);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition move = new TranslateTransition(Duration.millis(800), tasksTable);
        move.setFromY(20);
        move.setToY(0);

        new ParallelTransition(fade, move).play();
    }

    private void loadTasks() {
        new Thread(() -> {
            try {
                String json = ApiClient.getTasks(1L);
                Platform.runLater(() -> {
                    if (json != null && !json.equals("[]") && !json.isEmpty()) {
                        try {
                            Gson gson = new Gson();
                            List<Task> taskList = gson.fromJson(json, new TypeToken<List<Task>>(){}.getType());
                            ObservableList<Task> data = FXCollections.observableArrayList(taskList);
                            tasksTable.setItems(data);
                        } catch (Exception e) {
                            System.out.println("Error parsing JSON");
                        }
                    } else {
                        tasksTable.setItems(FXCollections.observableArrayList(
                                new Task(1L, "Test Task", "This is a demo task", "2023-12-01")
                        ));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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