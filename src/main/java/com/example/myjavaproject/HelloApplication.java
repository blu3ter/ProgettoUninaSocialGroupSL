package com.example.myjavaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carica l'immagine dell'icona
        Image ImmagineIcona = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/logo unina social group.png")));
        // Imposta l'icona dell'applicazione
        stage.getIcons().add(ImmagineIcona);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UninaSocialGroup");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}