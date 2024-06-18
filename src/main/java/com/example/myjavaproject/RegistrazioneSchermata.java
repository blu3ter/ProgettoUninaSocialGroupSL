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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrazioneSchermata {
    public TextField FieldNome;
    public TextField FieldCognome;
    public TextField FieldEmail;
    public PasswordField FieldPassword;
    public Button ProseguiButton;
    public Button LoginButton;
    public TextField FieldUsername;

    public void ContinuaPrimoAccesso(ActionEvent actionEvent) {
        String nome = FieldNome.getText();
        String cognome = FieldCognome.getText();
        String email = FieldEmail.getText();
        String password = FieldPassword.getText();
        String username = FieldUsername.getText();

        String sql = "INSERT INTO  utente (nome, cognome, email, password, username) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, username);

            pstmt.executeUpdate();

            showAlert("Successo", "Utente registrato con successo!");

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
