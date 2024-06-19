package com.example.myjavaproject;

import Util.DBUtil;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            showAlert("Errore", "Email e password sono obbligatorie.");
            return;
        }

        String query = "SELECT * FROM utente WHERE email = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loadDashboard(actionEvent, email);
            } else {
                showAlert("Errore", "Email o password errati.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile effettuare il login: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


        private void loadDashboard(ActionEvent actionEvent, String email) throws IOException {
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



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
