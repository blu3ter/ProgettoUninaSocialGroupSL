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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public Button IscrivitiBiancoButton;
    public ImageView logo;
    @FXML
    private Button AccediButton;
    @FXML
    private Button IscrivitiBluButton;
    @FXML
    private TextField FieldEmail;
    @FXML
    private PasswordField FieldPassword;

    private final UtenteDAO utenteDAO = new UtenteDAO();

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
            MostraAlert("Email e password sono obbligatorie.");
            return;
        }

        Utente utente = utenteDAO.GetEmailePassword(email, password);
        if (utente != null) {
            try {
                CaricaHomePage(actionEvent, email);
            } catch (IOException e) {
                e.printStackTrace();
                MostraAlert("Impossibile caricare la dashboard: " + e.getMessage());
            }
        } else {
            MostraAlert("Email o password errati.");
        }
    }

    private void CaricaHomePage(ActionEvent actionEvent, String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        HomePageController controller = loader.getController();
        controller.setUserEmail(email);
        stage.setTitle("Home Page");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    private void MostraAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void RiduciAccedi() {
        AccediButton.setScaleX(1.0);
        AccediButton.setScaleY(1.0);
    }

    public void IngrandisciAccedi() {
        AccediButton.setScaleX(1.1);
        AccediButton.setScaleY(1.1);
    }

    public void IngrandisciIscriviti() {
        IscrivitiBluButton.setScaleX(1.1);
        IscrivitiBluButton.setScaleY(1.1);
    }

    public void RiduciIscriviti() {
        IscrivitiBluButton.setScaleX(1);
        IscrivitiBluButton.setScaleY(1);
    }
}
