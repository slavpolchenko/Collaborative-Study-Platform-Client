package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.model.User;
import com.studyplatform.client.studyplatformclient.service.ApiClient;
import com.studyplatform.client.studyplatformclient.utils.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ProfileController {

    @FXML private Button backButton;
    @FXML private Button saveButton;

    // Поля для редагування
    @FXML private TextField nameField;
    @FXML private TextField emailField;

    // Поля для відображення (статистика)
    @FXML private Label idLabel;
    @FXML private Label groupsCountLabel;
    @FXML private ListView<String> activityList;

    // Декор
    @FXML private Pane backgroundPane;
    @FXML private Circle profileAvatar;

    private final Random random = new Random();
    private Image themePawImage;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        User user = session.getUser();

        if (user != null) {
            // Заповнюємо поля редагування
            nameField.setText(user.getName());
            emailField.setText(user.getEmail());

            // Заповнюємо статистику
            idLabel.setText(String.valueOf(user.getId())); // Використовуємо getId()
            groupsCountLabel.setText("3"); // Фейкова статистика для краси

            // Завантажуємо аватарку (Збережено з твого старого коду)
            try {
                String path = session.getAvatarPath();
                if (path != null) {
                    InputStream stream = getClass().getResourceAsStream(path);
                    if (stream != null) {
                        Image avatarImg = new Image(stream);
                        profileAvatar.setFill(new ImagePattern(avatarImg));
                    }
                }
            } catch (Exception e) {}

            // Список активності (Збережено)
            activityList.getItems().addAll("Joined Group 'Math'", "Completed Task #5", "Updated Profile");
        }

        // Лапки (Збережено)
        try {
            themePawImage = new Image(getClass().getResourceAsStream("/images/paw_red.png"));
        } catch (Exception e) {}

        spawnPaws();
    }

    @FXML
    protected void onSaveClick() {
        String newName = nameField.getText();
        String newEmail = emailField.getText();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Fields cannot be empty").show();
            return;
        }

        saveButton.setDisable(true);
        saveButton.setText("Saving...");

        new Thread(() -> {
            User currentUser = UserSession.getInstance().getUser();
            if (currentUser != null) {
                // Викликаємо метод оновлення
                boolean success = ApiClient.updateUser(currentUser.getId(), newName, newEmail);

                Platform.runLater(() -> {
                    saveButton.setDisable(false);
                    saveButton.setText("Save Changes");

                    if (success) {
                        currentUser.setName(newName);
                        currentUser.setEmail(newEmail);
                        new Alert(Alert.AlertType.INFORMATION, "Profile Updated Successfully!").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Update Failed on Server").show();
                    }
                });
            }
        }).start();
    }

    private void spawnPaws() {
        if (themePawImage == null || backgroundPane == null) return;
        for (int i = 0; i < 8; i++) {
            ImageView paw = new ImageView(themePawImage);
            paw.setFitWidth(40);
            paw.setFitHeight(40);
            paw.setOpacity(0.15);
            paw.setRotate(random.nextInt(360));
            paw.setLayoutX(random.nextDouble() * 850);
            paw.setLayoutY(random.nextDouble() * 550);
            backgroundPane.getChildren().add(paw);
        }
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