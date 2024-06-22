package com.example.myjavaproject;

import DAO.UtenteDAO;
import Oggetti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button AccediButton;
    @FXML
    private Button IscrivitiBluButton;
    @FXML
    private Button IscrivitiBiancoButton;
    @FXML
    private TextField FieldEmail;
    @FXML
    private PasswordField FieldPassword;

    private UtenteDAO utenteDAO = new UtenteDAO();

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
        String email = FieldEmail.getText();
        String password = FieldPassword.getText();

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            MostraAlert("Errore", "Email e password sono obbligatorie.");
            return;
        }

        Utente utente = utenteDAO.GetEmailePassword(email, password);
        if (utente != null) {
            try {
                caricaDashboard(actionEvent, email);
            } catch (IOException e) {
                e.printStackTrace();
                MostraAlert("Errore", "Impossibile caricare la dashboard: " + e.getMessage());
            }
        } else {
            MostraAlert("Errore", "Email o password errati.");
        }
    }

    private void caricaDashboard(ActionEvent actionEvent, String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        HomePage controller = loader.getController();
        controller.setUserEmail(email);
        stage.setTitle("Home Page");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    private void MostraAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void RiduciAccedi(MouseEvent mouseEvent) {
        AccediButton.setScaleX(1.0);
        AccediButton.setScaleY(1.0);
    }

    public void IngrandisciAccedi(MouseEvent mouseEvent) {
        AccediButton.setScaleX(1.1);
        AccediButton.setScaleY(1.1);
    }

    public void IngrandisciIscriviti(MouseEvent mouseEvent) {
        IscrivitiBluButton.setScaleX(1.1);
        IscrivitiBluButton.setScaleY(1.1);
    }

    public void RiduciIscriviti(MouseEvent mouseEvent) {
        IscrivitiBluButton.setScaleX(1);
        IscrivitiBluButton.setScaleY(1);
    }
}
