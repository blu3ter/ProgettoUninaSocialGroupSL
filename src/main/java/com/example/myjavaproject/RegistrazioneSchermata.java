package com.example.myjavaproject;

import Util.DBUtil;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void ContinuaPrimoAccesso(ActionEvent actionEvent) {
        String nome = FieldNome.getText();
        String cognome = FieldCognome.getText();
        String email = FieldEmail.getText();
        String password = FieldPassword.getText();
        String username = FieldUsername.getText();
        String bio = FieldBio.getText();

        // Verifico che tutti i campi obbligatori siano non nulli e non vuoti
        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                username == null || username.trim().isEmpty()) {

            showAlert("Errore", "Tutti i campi obbligatori devono essere riempiti.");
            return;
        }

        String checkEmailSql = "SELECT COUNT(*) FROM utente WHERE email = ?";
        String checkUsernameSql = "SELECT COUNT(*) FROM utente WHERE username = ?";
        String insertSql = "INSERT INTO utente (nome, cognome, email, password, username, bio) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
             PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Verifica se l'email esiste già
            checkEmailStmt.setString(1, email);
            ResultSet rsEmail = checkEmailStmt.executeQuery();
            if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                showAlert("Errore", "Esiste già un'email uguale.");
                return;
            }

            // Verifica se l'username esiste già
            checkUsernameStmt.setString(1, username);
            ResultSet rsUsername = checkUsernameStmt.executeQuery();
            if (rsUsername.next() && rsUsername.getInt(1) > 0) {
                showAlert("Errore", "Esiste già un username uguale.");
                return;
            }

            // Inserimento dell'utente
            insertStmt.setString(1, nome);
            insertStmt.setString(2, cognome);
            insertStmt.setString(3, email);
            insertStmt.setString(4, password);
            insertStmt.setString(5, username);
            insertStmt.setString(6, bio);

            insertStmt.executeUpdate();

            RegistrazioneAvvenuta.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile registrare l'utente: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
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
