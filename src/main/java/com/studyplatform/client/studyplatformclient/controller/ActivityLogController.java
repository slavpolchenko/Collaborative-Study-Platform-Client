package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

class Activity {
    String action;
    String timestamp;

    public Activity(String action, String timestamp) {
        this.action = action;
        this.timestamp = timestamp;
    }
    public String getAction() { return action; }
    public String getTimestamp() { return timestamp; }
}

public class ActivityLogController {

    @FXML private Button backButton;
    @FXML private TableView<Activity> activityTable;
    @FXML private TableColumn<Activity, String> actionColumn;
    @FXML private TableColumn<Activity, String> timeColumn;
    @FXML private Pane backgroundPane;

    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadActivity();
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