package com.example.myjavaproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    private Button AccediButton;
    private Button IscrivitiBluButton;
    private Button IscrivitiBiancoButton;


    //in questa parte apri la pagina per registrarti

    public void SwitchSchermataRegistrazione(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RegistrazioneSchermata.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registrazione");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void VaiAllaHomepage(ActionEvent actionEvent) {



    }

    public Button getIscrivitiBiancoButton() {
        return IscrivitiBiancoButton;
    }

    public void setIscrivitiBiancoButton(Button iscrivitiBiancoButton) {
        IscrivitiBiancoButton = iscrivitiBiancoButton;
    }

    public Button getAccediButton() {
        return AccediButton;
    }

    public void setAccediButton(Button accediButton) {
        AccediButton = accediButton;
    }

    public Button getIscrivitiBluButton() {
        return IscrivitiBluButton;
    }

    public void setIscrivitiBluButton(Button iscrivitiBluButton) {
        IscrivitiBluButton = iscrivitiBluButton;
    }
}
