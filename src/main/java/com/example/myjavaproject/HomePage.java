package com.example.myjavaproject;

import Util.DBUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePage {
    @FXML
    private ScrollPane groupScrollPane;
    @FXML
    private VBox groupVBox;
    @FXML
    private ScrollPane postScrollPane;
    @FXML
    private VBox postVBox;
    @FXML
    private TextField searchTextField;

    private String userEmail;

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        loadUserGroups();
    }

    private void loadUserGroups() {
        String query = "SELECT g.titolo " +
                "FROM gruppo g " +
                "JOIN partecipante p ON g.titolo = p.titolo_gruppo " +
                "JOIN utente u ON p.email_partecipante = u.email " +
                "WHERE u.email = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String groupName = rs.getString("titolo");
                addGroupToView(groupName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile caricare i gruppi: " + e.getMessage());
        }
    }

    private void addGroupToView(String groupName) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(groupName);
        groupButton.setOnAction(event -> loadGroupPosts(groupName));
        groupVBox.getChildren().add(groupButton);
    }

    private void loadGroupPosts(String groupName) {
        String query = "SELECT c.testo " +
                "FROM contenuto c " +
                "JOIN gruppo g ON c.gruppo_app = g.titolo " +
                "WHERE g.titolo = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();

            postVBox.getChildren().clear();
            while (rs.next()) {
                String postText = rs.getString("testo");
                addPostToView(postText);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile caricare i post: " + e.getMessage());
        }
    }

    private void addPostToView(String postText) {
        javafx.scene.control.Label postLabel = new javafx.scene.control.Label(postText);
        postVBox.getChildren().add(postLabel);
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
