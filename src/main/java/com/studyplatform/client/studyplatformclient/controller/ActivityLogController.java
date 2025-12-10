package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.model.Activity;
import javafx.animation.FadeTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class ActivityLogController {

    @FXML private Button backButton;
    @FXML private TableView<Activity> activityTable;
    @FXML private TableColumn<Activity, String> actionColumn;
    @FXML private TableColumn<Activity, String> timeColumn;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        animateTable();
        loadActivity();
    }

    private void animateTable() {
        activityTable.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(800), activityTable);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void loadActivity() {
        new Thread(() -> {
            String[] users = {"Slava", "Taras", "Admin"};
            String[] actions = {
                    "logged in",
                    "updated profile",
                    "created a new group",
                    "joined the group 'Java Developers'",
                    "completed task 'Fix Bugs'",
                    "added a comment",
                    "marked task as done",
                    "uploaded a file"
            };

            ObservableList<Activity> list = FXCollections.observableArrayList();

            for (int i = 0; i < 25; i++) {
                String user = users[random.nextInt(users.length)];
                String act = actions[random.nextInt(actions.length)];
                String fullAction = user + " " + act;

                int hour = 9 + random.nextInt(12);
                int minute = random.nextInt(60);
                String time = String.format("2025-12-10 %02d:%02d", hour, minute);

                list.add(new Activity(fullAction, time));
            }

            Platform.runLater(() -> activityTable.setItems(list));
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