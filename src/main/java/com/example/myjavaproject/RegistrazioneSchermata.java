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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrazioneSchermata {
    public Button ProseguiButton1;
    public Button LoginButton1;
    @FXML
    private TextField FieldNome;
    @FXML
    private TextField FieldCognome;
    @FXML
    private TextField FieldEmail;
    @FXML
    private PasswordField FieldPassword;
    @FXML
    private TextField FieldUsername;
    @FXML
    private TextField FieldBio;
    @FXML
    public Text RegistrazioneAvvenuta;

    private final UtenteDAO utenteDAO = new UtenteDAO();

    public void ContinuaPrimoAccesso() {
        String nome = FieldNome.getText();
        String cognome = FieldCognome.getText();
        String email = FieldEmail.getText();
        String password = FieldPassword.getText();
        String username = FieldUsername.getText();
        String bio = FieldBio.getText(); // Bio può essere null

        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                username == null || username.trim().isEmpty()) {
            MostraAlert("Tutti i campi obbligatori devono essere riempiti.");
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

        Utente nuovoUtente = new Utente(nome, cognome, email, password, username, bio);
        utenteDAO.insertUtente(nuovoUtente);
        RegistrazioneAvvenuta.setVisible(true);
    }

    private void MostraAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void RitornaLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }



    public void IngrandisciApriUnAccount() {
        ProseguiButton1.setScaleX(1.1);
        ProseguiButton1.setScaleY(1.1);
    }

    public void RiduciApriUnAccount() {
        ProseguiButton1.setScaleX(1);
        ProseguiButton1.setScaleY(1);
    }
}
