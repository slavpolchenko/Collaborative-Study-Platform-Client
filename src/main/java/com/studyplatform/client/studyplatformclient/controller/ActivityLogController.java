package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Activity; // Імпорт нашої нової моделі
import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

public class ActivityLogController {

    @FXML private Button backButton;
    @FXML private TableView<Activity> activityTable;
    @FXML private TableColumn<Activity, String> actionColumn;
    @FXML private TableColumn<Activity, String> timeColumn;
    @FXML private Pane backgroundPane;

    private final Random random = new Random();
    private Image themePawImage;

    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        try {
            themePawImage = new Image(getClass().getResourceAsStream("/images/paw_yellow.png"));
        } catch (Exception e) {}

        spawnPaws();
        animateTable();
        loadActivity();
    }

    private void animateTable() {
        activityTable.setOpacity(0);
        activityTable.setTranslateY(20);

        FadeTransition fade = new FadeTransition(Duration.millis(800), activityTable);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition move = new TranslateTransition(Duration.millis(800), activityTable);
        move.setFromY(20);
        move.setToY(0);

        new ParallelTransition(fade, move).play();
    }

    private void spawnPaws() {
        if (themePawImage == null) return;
        for (int i = 0; i < 10; i++) {
            ImageView paw = new ImageView(themePawImage);
            paw.setFitWidth(40);
            paw.setFitHeight(40);
            paw.setOpacity(0.4);
            paw.setRotate(random.nextInt(360));
            paw.setLayoutX(random.nextDouble() * 900);
            paw.setLayoutY(random.nextDouble() * 600);
            backgroundPane.getChildren().add(paw);
        }
    }

    private void loadActivity() {
        new Thread(() -> {
            String json = ApiClient.getActivityLog();
            Platform.runLater(() -> {
                try {
                    if (json != null && !json.equals("[]") && !json.isEmpty() && json.startsWith("[")) {
                        Gson gson = new Gson();
                        List<Activity> list = gson.fromJson(json, new TypeToken<List<Activity>>(){}.getType());
                        activityTable.setItems(FXCollections.observableArrayList(list));
                    } else {
                        activityTable.getItems().add(new Activity("User registered", "2023-11-27 10:33"));
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing activity log");
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