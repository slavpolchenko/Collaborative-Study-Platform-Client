package com.studyplatform.client.studyplatformclient.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studyplatform.client.studyplatformclient.model.Task;
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

public class TasksController {

    @FXML private Button backButton;
    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> deadlineColumn;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        loadTasks();
    }

    private void loadTasks() {
        new Thread(() -> {
            String json = ApiClient.getTasks(1L);
            Platform.runLater(() -> {
                try {
                    if (json != null && !json.equals("[]")) {
                        List<Task> list = new Gson().fromJson(json, new TypeToken<List<Task>>(){}.getType());
                        tasksTable.setItems(FXCollections.observableArrayList(list));
                    }
                } catch (Exception e) {}
            });
        }).start();
    }

    @FXML
    protected void onAddTaskClick() {
        System.out.println("КНОПКА ADD TASK НАЖАТА!"); // ПРОВЕРКА

        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Add Task");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField tfTitle = new TextField(); tfTitle.setPromptText("Title");
        TextField tfDesc = new TextField(); tfDesc.setPromptText("Description");
        TextField tfDate = new TextField(); tfDate.setPromptText("2025-12-31");
        dialog.getDialogPane().setContent(new VBox(10, new Label("Title:"), tfTitle, new Label("Desc:"), tfDesc, new Label("Date:"), tfDate));

        dialog.setResultConverter(b -> b == ButtonType.OK ? new Task(null, tfTitle.getText(), tfDesc.getText(), tfDate.getText()) : null);

        Optional<Task> res = dialog.showAndWait();
        res.ifPresent(t -> {
            new Thread(() -> {
                if (ApiClient.createTask(1L, t.getTitle(), t.getDescription(), t.getDeadline())) {
                    Platform.runLater(this::loadTasks);
                }
            }).start();
        });
    }

    @FXML
    protected void onMarkDoneClick() {
        System.out.println("КНОПКА MARK DONE НАЖАТА!"); // ПРОВЕРКА
        Task t = tasksTable.getSelectionModel().getSelectedItem();
        if (t != null) {
            t.setDescription(t.getDescription() + " [DONE]");
            new Thread(() -> {
                if (ApiClient.updateTask(t)) {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.INFORMATION, "Done!").show();
                        loadTasks();
                    });
                }
            }).start();
        }
    }

    @FXML
    protected void onBackClick() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")), 950, 650));
        } catch (IOException e) {}
    }
}