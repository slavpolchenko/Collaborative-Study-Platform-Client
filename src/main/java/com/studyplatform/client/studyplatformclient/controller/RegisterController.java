package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private StackPane rootPane;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label infoLabel;
    @FXML private Button registerButton;

    @FXML
    public void initialize() {
    }

    @FXML
    private void onEyeClicked(MouseEvent event) {
    }

    @FXML
    protected void onRegisterClick() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            infoLabel.setText("Please fill in all fields!");
            infoLabel.setTextFill(Color.RED);
            infoLabel.setVisible(true);
            return;
        }

        registerButton.setDisable(true);
        infoLabel.setText("Creating account...");
        infoLabel.setTextFill(Color.GRAY);
        infoLabel.setVisible(true);

        new Thread(() -> {
            boolean success = ApiClient.register(name, email, password);

            Platform.runLater(() -> {
                registerButton.setDisable(false);
                if (success) {
                    infoLabel.setText("Success! Please login.");
                    infoLabel.setTextFill(Color.GREEN);
                } else {
                    infoLabel.setText("Account already exists or error!");
                    infoLabel.setTextFill(Color.RED);
                }
            });
        }).start();
    }

    @FXML
    protected void onBackToLoginClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}