package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Group;
import com.studyplatform.client.studyplatformclient.service.ApiClient;
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

import java.io.IOException;
import java.util.List;

public class GroupsController {

    @FXML private TableView<Group> groupsTable;
    @FXML private TableColumn<Group, Long> idColumn;
    @FXML private TableColumn<Group, String> nameColumn;
    @FXML private TableColumn<Group, String> descColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadGroups();
    }

    private void loadGroups() {
        new Thread(() -> {
            String json = ApiClient.getGroups();

            Platform.runLater(() -> {
                try {
                    Gson gson = new Gson();
                    List<Group> groupList = gson.fromJson(json, new TypeToken<List<Group>>(){}.getType());
                    ObservableList<Group> data = FXCollections.observableArrayList(groupList);
                    groupsTable.setItems(data);
                } catch (Exception e) {
                    System.out.println("Error parsing groups: " + e.getMessage());
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