package com.studyplatform.client.studyplatformclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        InputStream iconStream = getClass().getResourceAsStream("/images/dino.png");
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Warning: dino.png not found in resources/images/");
        }

        stage.setTitle("Study Platform");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}