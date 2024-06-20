package com.example.myjavaproject;

import DAO.ContenutoDAO;
import DAO.GruppoDAO;
import DAO.PartecipanteDAO;
import Oggetti.Contenuto;
import Oggetti.Gruppo;
import Oggetti.Partecipante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.List;

public class HomePage {
    @FXML
    private ScrollPane groupScrollPane;
    @FXML
    private VBox groupVBox;
    @FXML
    private ScrollPane searchResultsScrollPane;
    @FXML
    private VBox searchResultsVBox;
    @FXML
    private ScrollPane postScrollPane;
    @FXML
    private VBox postVBox;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextArea newPostTextArea;
    @FXML
    private Button createPostButton;
    @FXML
    private Button iscrivitiButton;

    private String userEmail;
    private String currentGroupName;
    private boolean isUserParticipant;

    private GruppoDAO gruppoDAO = new GruppoDAO();
    private PartecipanteDAO partecipanteDAO = new PartecipanteDAO();
    private ContenutoDAO contenutoDAO = new ContenutoDAO();

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        MostraGruppi();
    }

    private void MostraGruppi() {
        List<Gruppo> gruppi = gruppoDAO.getGruppiByUserEmail(userEmail);
        groupVBox.getChildren().clear();
        for (Gruppo gruppo : gruppi) {
            MostraGruppi(gruppo.getTitolo());
        }
    }

    private void MostraGruppi(String groupName) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(groupName);
        groupButton.setOnAction(event -> {
            currentGroupName = groupName;
            isUserParticipant = true;
            MostraPost(groupName);
            iscrivitiButton.setVisible(false);
        });
        groupVBox.getChildren().add(groupButton);
    }

    private void MostraPost(String groupName) {
        if (!isUserParticipant) {
            MostraAlert("Errore", "Devi iscriverti al gruppo per visualizzare i contenuti.");
            return;
        }

        List<Contenuto> contenuti = contenutoDAO.getContenutiByGruppo(groupName);
        postVBox.getChildren().clear();
        for (Contenuto contenuto : contenuti) {
            addPostToView(contenuto.getTesto());
        }
    }

    private void addPostToView(String postText) {
        javafx.scene.control.Label postLabel = new javafx.scene.control.Label(postText);
        postVBox.getChildren().add(postLabel);
    }

    @FXML
    private void CreaPost() {
        String postText = newPostTextArea.getText().trim();
        if (postText.isEmpty() || currentGroupName == null) {
            MostraAlert("Errore", "Il testo del post non può essere vuoto e devi selezionare un gruppo.");
            return;
        }

        Contenuto nuovoContenuto = new Contenuto(postText, java.sql.Date.valueOf(LocalDate.now()), currentGroupName, userEmail);
        contenutoDAO.insertContenuto(nuovoContenuto);
        newPostTextArea.clear();
        MostraPost(currentGroupName);
    }

    @FXML
    private void Cercagruppo(ActionEvent actionEvent) {
        String searchText = searchTextField.getText().trim();
        if (searchText.isEmpty()) {
            MostraAlert("Errore", "Il campo di ricerca non può essere vuoto.");
            return;
        }

        List<Gruppo> gruppi = gruppoDAO.searchGruppi(searchText);
        searchResultsVBox.getChildren().clear();
        for (Gruppo gruppo : gruppi) {
            MostraGruppo(gruppo.getTitolo());
        }
    }

    private void MostraGruppo(String groupName) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(groupName);
        groupButton.setOnAction(event -> {
            currentGroupName = groupName;
            isUserParticipant = false;
            iscrivitiButton.setVisible(true);
            postVBox.getChildren().clear();
        });
        searchResultsVBox.getChildren().add(groupButton);
    }

    @FXML
    private void iscrivitiAlGruppo() {
        if (currentGroupName == null) {
            MostraAlert("Errore", "Devi selezionare un gruppo.");
            return;
        }

        Partecipante nuovoPartecipante = new Partecipante(userEmail, currentGroupName, java.sql.Date.valueOf(LocalDate.now()));
        partecipanteDAO.insertPartecipante(nuovoPartecipante);
        iscrivitiButton.setVisible(false);
        MostraGruppi();
        isUserParticipant = true;
        MostraPost(currentGroupName);
    }

    private void MostraAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
