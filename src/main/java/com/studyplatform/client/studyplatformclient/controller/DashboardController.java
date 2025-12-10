package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private BorderPane rootPane;

    @FXML
    public void initialize() {
        if (UserSession.getInstance() != null && UserSession.getInstance().getUser() != null) {
            String name = UserSession.getInstance().getUser().getName();
            // Делаем первую букву заглавной для красоты
            if (name != null && !name.isEmpty()) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            welcomeLabel.setText("Welcome, " + name + "!");
        }
    }

    @FXML
    protected void onGroupsClick() {
        navigate("/view/Groups.fxml");
    }

    @FXML
    protected void onTasksClick() {
        navigate("/view/Tasks.fxml");
    }

    @FXML
    protected void onActivityClick() {
        navigate("/view/ActivityLog.fxml");
    }

    // ВОТ ЭТОТ МЕТОД ОТКРЫВАЕТ НАСТРОЙКИ
    @FXML
    protected void onSettingsClick() {
        navigate("/view/Profile.fxml");
    }

    @FXML
    protected void onLogoutClick() {
        UserSession.cleanUserSession();
        navigate("/view/Login.fxml");
    }

    private void navigate(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), 950, 650);
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}