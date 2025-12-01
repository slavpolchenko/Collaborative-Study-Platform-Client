package com.studyplatform.client.studyplatformclient.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class DinoStoreController {
    @FXML
    protected void onBackClick() throws IOException {
        Stage stage = (Stage) new Button().getScene().getWindow(); // Hack for simplicity or use specific button ID
        // Лучше добавить fx:id кнопке, но для теста пока сойдет
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
        stage.setScene(new Scene(loader.load(), 950, 650));
    }
}