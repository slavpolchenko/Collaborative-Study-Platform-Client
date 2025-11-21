package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.service.ApiClient;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class RegisterController {

    @FXML private StackPane rootPane;
    @FXML private AnchorPane dinoHead;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label infoLabel;
    @FXML private Button registerButton;

    @FXML private Circle leftPupil;
    @FXML private Circle rightPupil;
    @FXML private Circle leftEye;
    @FXML private Circle rightEye;

    private int clickCount = 0;
    private boolean isAngry = false;

    @FXML
    public void initialize() {
        rootPane.setOnMouseMoved(this::moveEyes);
    }

    @FXML
    private void onEyeClicked(MouseEvent event) {
        blink();
        clickCount++;
        if (clickCount >= 5 && !isAngry) {
            becomeAngry();
        }
    }

    private void playFocusAnimation() {
        leftPupil.setTranslateX(0); leftPupil.setTranslateY(0);
        rightPupil.setTranslateX(0); rightPupil.setTranslateY(0);
        animateSquint(leftEye, 0.8);
        animateSquint(rightEye, 0.8);
    }

    private void playSuccessAnimation() {
        leftEye.setFill(Color.LIMEGREEN);
        rightEye.setFill(Color.LIMEGREEN);
        animateSquint(leftEye, 0.6);
        animateSquint(rightEye, 0.6);
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(e -> animateWink(rightEye));
        pause.play();
    }

    private void becomeAngry() {
        isAngry = true;
        leftEye.setFill(Color.RED);
        rightEye.setFill(Color.RED);
        leftEye.setScaleY(1.0);
        rightEye.setScaleY(1.0);
        shakeHeadNo();
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> calmDown());
        pause.play();
    }

    private void shakeHeadNo() {
        dinoHead.setTranslateX(0);

        TranslateTransition tt = new TranslateTransition(Duration.millis(50), dinoHead);
        tt.setFromX(0);
        tt.setToX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void animateWink(Circle target) {
        ScaleTransition close = new ScaleTransition(Duration.millis(150), target);
        close.setToY(0.1);
        ScaleTransition open = new ScaleTransition(Duration.millis(150), target);
        open.setToY(0.6);
        new SequentialTransition(close, open).play();
    }

    private void animateSquint(Circle target, double value) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), target);
        st.setFromY(1.0);
        st.setToY(value);
        st.setCycleCount(1);
        st.play();
    }

    private void blink() {
        animateBlinkFull(leftEye);
        animateBlinkFull(rightEye);
        animateBlinkFull(leftPupil);
        animateBlinkFull(rightPupil);
    }

    private void animateBlinkFull(Circle target) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), target);
        st.setFromY(1.0);
        st.setToY(0.1);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private void calmDown() {
        isAngry = false;
        clickCount = 0;
        leftEye.setFill(Color.WHITE);
        rightEye.setFill(Color.WHITE);
        ScaleTransition openLeft = new ScaleTransition(Duration.millis(200), leftEye); openLeft.setToY(1.0); openLeft.play();
        ScaleTransition openRight = new ScaleTransition(Duration.millis(200), rightEye); openRight.setToY(1.0); openRight.play();
    }

    private void moveEyes(MouseEvent event) {
        double centerX = rootPane.getWidth() / 2;
        double centerY = rootPane.getHeight() / 2 - 230;
        updatePupil(leftPupil, event.getX(), event.getY(), centerX - 50, centerY);
        updatePupil(rightPupil, event.getX(), event.getY(), centerX + 50, centerY);
    }

    private void updatePupil(Circle pupil, double mouseX, double mouseY, double eyeX, double eyeY) {
        double deltaX = mouseX - eyeX;
        double deltaY = mouseY - eyeY;
        double angle = Math.atan2(deltaY, deltaX);
        double distance = Math.min(Math.sqrt(deltaX * deltaX + deltaY * deltaY), 20);
        pupil.setTranslateX(Math.cos(angle) * distance);
        pupil.setTranslateY(Math.sin(angle) * distance);
    }

    @FXML
    protected void onRegisterClick() {
        playFocusAnimation();

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
                    playSuccessAnimation();
                    infoLabel.setText("Success! Please login.");
                    infoLabel.setTextFill(Color.GREEN);
                } else {
                    becomeAngry();
                    infoLabel.setText("Registration failed.");
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