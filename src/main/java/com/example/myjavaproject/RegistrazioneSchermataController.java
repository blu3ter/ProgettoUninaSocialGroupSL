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
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrazioneSchermataController {
    public Button proseguiButton;
    public Button ritornaLoginButton;
    @FXML
    private TextField fieldNome;
    @FXML
    private TextField fieldCognome;
    @FXML
    private TextField fieldEmail;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextField fieldBio;


    private final UtenteDAO utenteDAO = new UtenteDAO();

    public void ContinuaPrimoAccesso() {
        String nome = fieldNome.getText();
        String cognome = fieldCognome.getText();
        String email = fieldEmail.getText();
        String password = fieldPassword.getText();
        String username = fieldUsername.getText();
        String bio = fieldBio.getText(); // Bio può essere null

        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                username == null || username.trim().isEmpty()) {
            MostraAlert("Tutti i campi obbligatori devono essere riempiti.");
            return;
        }

        if ( password.length() < 8){
            MostraAlert("La password deve essere lunga di almeno 8 caratteri");
            return;
        }

        if (utenteDAO.EmailEsistente(email)) {
            MostraAlert("Esiste già un'email uguale.");
            return;
        }

        if (utenteDAO.UsernameEsistente(username)) {
            MostraAlert("Esiste già un username uguale.");
            return;
        }

        // In caso di campo bio vuoto, impostalo a null
        if (bio == null || bio.trim().isEmpty()) {
            bio = null;
        }

        //crea un utente nuovo e lo inserisci nel DB
        Utente nuovoUtente = new Utente(nome, cognome, email, password, username, bio);
        utenteDAO.insertUtente(nuovoUtente);
        MostraAlertPositivo();
    }

    private void MostraAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void MostraAlertPositivo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrazione completata");
        alert.setHeaderText(null);
        alert.setContentText("Registrazione avvenuta con successo!\n Ritorna alla schermata di Login per accedere.");
        alert.showAndWait();
    }

    public void RitornaLogin(ActionEvent actionEvent) throws IOException {
        //crea un oggetto FXMLLoader per ritornare poi alla finestra del Login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }



    public void IngrandisciCreaAccount() {
        proseguiButton.setScaleX(1.1);
        proseguiButton.setScaleY(1.1);
    }

    public void RiduciCreaAccount() {
        proseguiButton.setScaleX(1);
        proseguiButton.setScaleY(1);
    }

    public void IngrandisciRitornaLogin() {
        ritornaLoginButton.setScaleX(1.1);
        ritornaLoginButton.setScaleY(1.1);
    }

    public void RiduciRitornaLogin() {
        ritornaLoginButton.setScaleX(1);
        ritornaLoginButton.setScaleY(1);
    }
}