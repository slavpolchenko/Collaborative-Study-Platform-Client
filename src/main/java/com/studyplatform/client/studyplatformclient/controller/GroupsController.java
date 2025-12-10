package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Group;
import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GroupsController {

    @FXML private TableView<Group> groupsTable;
    @FXML private TableColumn<Group, Long> idColumn;
    @FXML private TableColumn<Group, String> nameColumn;
    @FXML private TableColumn<Group, String> descColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("groupId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadGroups();
    }

    private void loadGroups() {
        new Thread(() -> {
            String json = ApiClient.getGroups();
            Platform.runLater(() -> {
                try {
                    if (json != null && !json.equals("[]")) {
                        List<Group> list = new Gson().fromJson(json, new TypeToken<List<Group>>(){}.getType());
                        groupsTable.setItems(FXCollections.observableArrayList(list));
                    }
                } catch (Exception e) {}
            });
        }).start();
    }

    @FXML
    protected void onCreateGroupClick() {
        System.out.println("КНОПКА CREATE GROUP НАЖАТА!"); // ПРОВЕРКА

        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Create Group");
        dialog.setHeaderText("New Group");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameF = new TextField(); nameF.setPromptText("Name");
        TextField descF = new TextField(); descF.setPromptText("Description");
        dialog.getDialogPane().setContent(new VBox(10, new Label("Name:"), nameF, new Label("Desc:"), descF));

        dialog.setResultConverter(b -> b == ButtonType.OK ? new Group(null, nameF.getText(), descF.getText()) : null);

        Optional<Group> res = dialog.showAndWait();
        res.ifPresent(g -> {
            new Thread(() -> {
                if (ApiClient.createGroup(g.getName(), g.getDescription())) {
                    Platform.runLater(() -> {
                        loadGroups();
                        new Alert(Alert.AlertType.INFORMATION, "Created!").show();
                    });
                }
            }).start();
        });
    }

    @FXML
    protected void onJoinGroupClick() {
        System.out.println("КНОПКА JOIN GROUP НАЖАТА!"); // ПРОВЕРКА

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Join Group");
        dialog.setHeaderText("Enter Group ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            try {
                Long groupId = Long.parseLong(idStr);
                new Thread(() -> {
                    boolean success = ApiClient.joinGroup(groupId);
                    Platform.runLater(() -> {
                        if (success) new Alert(Alert.AlertType.INFORMATION, "SUCCESS: User joined group!").show();
                        else new Alert(Alert.AlertType.ERROR, "ERROR: Could not join group.").show();
                    });
                }).start();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
            }
        });
    }

    @FXML
    protected void onBackClick() {
        try {
            Stage stage = (Stage) groupsTable.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")), 950, 650));
        } catch (IOException e) {}
    }
}