package com.example.myjavaproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrazioneSchermata {
    public TextField FieldNome;
    public TextField FieldCognome;
    public TextField FieldEmail;
    public PasswordField FieldPassword;
    public Button ProseguiButton;
    public Button LoginButton;

    public void ContinuaPrimoAccesso(ActionEvent actionEvent) {
    }

    public void RitornaLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginSchermata.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
}
