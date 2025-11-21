package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.ClientApplication;
import com.studyplatform.client.studyplatformclient.model.User;
import com.studyplatform.client.studyplatformclient.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label userNameLabel;
    @FXML private Circle avatarCircle;
    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        User user = UserSession.getInstance().getUser();
        if (user != null) {
            userNameLabel.setText(user.getName());
        }
    }

    @FXML
    protected void onSettingsClick() {
        System.out.println("Settings clicked");
    }

    @FXML
    protected void onAvatarClick() {
        System.out.println("Avatar clicked");
    }

    @FXML
    protected void onGroupsClick() {
        System.out.println("Navigating to Groups");
    }

    @FXML
    protected void onActivityClick() {
        System.out.println("Navigating to Activity Log");
    }

    @FXML
    protected void onLogoutClick() {
        UserSession.cleanUserSession();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("/view/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}