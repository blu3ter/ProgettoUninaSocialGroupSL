package com.example.myjavaproject;

import DAO.UtenteDAO;
import Oggetti.Utente;
import javafx.event.ActionEvent;
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
    public TextField FieldNome;
    public TextField FieldCognome;
    public TextField FieldEmail;
    public PasswordField FieldPassword;
    public Button ProseguiButton;
    public Button LoginButton;
    public TextField FieldUsername;
    public TextField FieldBio;
    public Text RegistrazioneAvvenuta;

    private UtenteDAO utenteDAO = new UtenteDAO();

    public void ContinuaPrimoAccesso(ActionEvent actionEvent) {
        String nome = FieldNome.getText();
        String cognome = FieldCognome.getText();
        String email = FieldEmail.getText();
        String password = FieldPassword.getText();
        String username = FieldUsername.getText();
        String bio = FieldBio.getText();

        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                username == null || username.trim().isEmpty()) {
            MostraAlert("Errore", "Tutti i campi obbligatori devono essere riempiti.");
            return;
        }

        if (utenteDAO.emailExists(email)) {
            MostraAlert("Errore", "Esiste già un'email uguale.");
            return;
        }

        if (utenteDAO.usernameExists(username)) {
            MostraAlert("Errore", "Esiste già un username uguale.");
            return;
        }

        Utente nuovoUtente = new Utente(nome, cognome, email, password, username, bio);
        utenteDAO.insertUtente(nuovoUtente);
        RegistrazioneAvvenuta.setVisible(true);
    }

    private void MostraAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
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
}
