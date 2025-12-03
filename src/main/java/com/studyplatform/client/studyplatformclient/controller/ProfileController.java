package com.studyplatform.client.studyplatformclient.controller;

import com.studyplatform.client.studyplatformclient.model.User;
import com.studyplatform.client.studyplatformclient.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label idLabel;
    @FXML private Label groupsCountLabel;
    @FXML private ListView<String> activityList;
    @FXML private Pane backgroundPane;
    @FXML private Circle profileAvatar;

    private final Random random = new Random();
    private Image themePawImage;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        User user = session.getUser();

        if (user != null) {
            nameLabel.setText(user.getName());
            emailLabel.setText(user.getEmail());
            idLabel.setText(String.valueOf(user.getUserId()));
            groupsCountLabel.setText("3");

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

            activityList.getItems().addAll("Joined Group 'Math'", "Completed Task #5", "Updated Profile");
        }

        try {
            themePawImage = new Image(getClass().getResourceAsStream("/images/paw_red.png"));
        } catch (Exception e) {}

        spawnPaws();
    }

    private void spawnPaws() {
        if (themePawImage == null) return;
        for (int i = 0; i < 8; i++) {
            ImageView paw = new ImageView(themePawImage);
            paw.setFitWidth(40);
            paw.setFitHeight(40);
            paw.setOpacity(0.3);
            paw.setRotate(random.nextInt(360));

            paw.setLayoutX(random.nextDouble() * 900);
            paw.setLayoutY(random.nextDouble() * 600);

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