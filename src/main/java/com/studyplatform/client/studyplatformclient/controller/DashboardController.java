package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.ClientApplication;
import com.studyplatform.client.studyplatformclient.model.User;
import com.studyplatform.client.studyplatformclient.utils.UserSession;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardController {

    @FXML private Label userNameLabel;
    @FXML private Circle avatarCircle;
    @FXML private Pane backgroundPane; // Если его нет в FXML, программа не упадет, но лапок не будет

    private final Random random = new Random();
    private final List<Image> pawImages = new ArrayList<>();

    @FXML
    public void initialize() {
        // Загружаем юзера (безопасно)
        User user = UserSession.getInstance().getUser();
        if (user != null) {
            userNameLabel.setText(user.getName());
        } else {
            userNameLabel.setText("Guest");
        }

        // Пытаемся загрузить лапки (безопасно)
        loadPawImages();

        // Спавним лапки только если есть куда (backgroundPane != null)
        if (backgroundPane != null && !pawImages.isEmpty()) {
            spawnPaws();
        }
    }

    private void loadPawImages() {
        try {
            // Проверяем каждую картинку отдельно
            if (getClass().getResourceAsStream("/images/paw.png") != null)
                pawImages.add(new Image(getClass().getResourceAsStream("/images/paw.png")));

            if (getClass().getResourceAsStream("/images/paw_red.png") != null)
                pawImages.add(new Image(getClass().getResourceAsStream("/images/paw_red.png")));

            if (getClass().getResourceAsStream("/images/paw_yellow.png") != null)
                pawImages.add(new Image(getClass().getResourceAsStream("/images/paw_yellow.png")));

        } catch (Exception e) {
            System.out.println("Warning: Could not load some paw images.");
        }
    }

    private void spawnPaws() {
        for (int i = 0; i < 15; i++) {
            Image randomImage = pawImages.get(random.nextInt(pawImages.size()));

            ImageView paw = new ImageView(randomImage);
            paw.setFitWidth(45);
            paw.setFitHeight(45);
            paw.setOpacity(0.5);
            paw.setRotate(random.nextInt(360));

            placePawRandomly(paw);

            paw.setOnMouseClicked(e -> handlePawClick(paw));

            backgroundPane.getChildren().add(paw);
        }
    }

    private void placePawRandomly(ImageView paw) {
        double x = random.nextDouble() * 900;
        double y = random.nextDouble() * 600;
        paw.setLayoutX(x);
        paw.setLayoutY(y);
    }

    private void handlePawClick(ImageView paw) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), paw);
        fadeOut.setFromValue(0.5);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            paw.setVisible(false);

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> respawnPaw(paw));
            pause.play();
        });
        fadeOut.play();
    }

    private void respawnPaw(ImageView paw) {
        placePawRandomly(paw);

        if (!pawImages.isEmpty()) {
            paw.setImage(pawImages.get(random.nextInt(pawImages.size())));
        }

        paw.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), paw);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.5);
        fadeIn.play();
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
        navigateTo("/view/Groups.fxml");
    }

    @FXML
    protected void onActivityClick() {
        navigateTo("/view/ActivityLog.fxml");
    }

    @FXML
    protected void onTasksClick() {
        navigateTo("/view/Tasks.fxml");
    }

    @FXML
    protected void onDinoStoreClick() {
        navigateTo("/view/DinoStore.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), 950, 650);
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
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