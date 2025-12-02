package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Group;
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

public class GroupsController {

    @FXML private TableView<Group> groupsTable;
    @FXML private TableColumn<Group, Long> idColumn;
    @FXML private TableColumn<Group, String> nameColumn;
    @FXML private TableColumn<Group, String> descColumn;
    @FXML private Pane backgroundPane;

    private final Random random = new Random();
    private Image themePawImage;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // ТІЛЬКИ СИНЯ ЛАПКА ДЛЯ ГРУП
        try {
            themePawImage = new Image(getClass().getResourceAsStream("/images/paw.png"));
        } catch (Exception e) {
            System.out.println("Could not load theme paw image");
        }

        spawnPaws();
        animateTable();
        loadGroups();
    }

    private void spawnPaws() {
        if (themePawImage == null) return;
        for (int i = 0; i < 10; i++) {
            ImageView paw = new ImageView(themePawImage);
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
        groupsTable.setOpacity(0);
        groupsTable.setTranslateY(20);

        FadeTransition fade = new FadeTransition(Duration.millis(800), groupsTable);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition move = new TranslateTransition(Duration.millis(800), groupsTable);
        move.setFromY(20);
        move.setToY(0);

        new ParallelTransition(fade, move).play();
    }

    private void loadGroups() {
        new Thread(() -> {
            String json = ApiClient.getGroups();
            Platform.runLater(() -> {
                try {
                    Gson gson = new Gson();
                    List<Group> groupList = gson.fromJson(json, new TypeToken<List<Group>>(){}.getType());
                    if (groupList != null) {
                        ObservableList<Group> data = FXCollections.observableArrayList(groupList);
                        groupsTable.setItems(data);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing groups");
                }
            });
        }).start();
    }

    @FXML
    protected void onBackClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 950, 650);
            Stage stage = (Stage) groupsTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}