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
    private ScrollPane gruppiScrollPane;
    @FXML
    private VBox groupVBox;
    @FXML
    private ScrollPane GruppiTrovatiScrollPane;
    @FXML
    private VBox CercaGruppi;
    @FXML
    private ScrollPane postScrollPane;
    @FXML
    private VBox AreaCont;
    @FXML
    private TextField FieldCercaTesto;
    @FXML
    private TextArea AreaNuovoPost;
    @FXML
    private Button CreaContButton;
    @FXML
    private Button iscrivitiButton;

    private String userEmail;
    private String CurrNomiGruppi;
    private boolean Partecipante;

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

    private void MostraGruppi(String NomiGruppi) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(NomiGruppi);
        groupButton.setOnAction(event -> {
            CurrNomiGruppi = NomiGruppi;
            Partecipante = true;
            MostraPost(NomiGruppi);
            iscrivitiButton.setVisible(false);
        });
        groupVBox.getChildren().add(groupButton);
    }

    private void MostraPost(String NomiGruppi) {
        if (!Partecipante) {
            MostraAlert("Errore", "Devi iscriverti al gruppo per visualizzare i contenuti.");
            return;
        }

        List<Contenuto> contenuti = contenutoDAO.getContenutiByGruppo(NomiGruppi);
        AreaCont.getChildren().clear();
        for (Contenuto contenuto : contenuti) {
            CaricaNuovoContenuto(contenuto.getTesto());
        }
    }

    private void CaricaNuovoContenuto(String postText) {
        javafx.scene.control.Label postLabel = new javafx.scene.control.Label(postText);
        AreaCont.getChildren().add(postLabel);
    }

    @FXML
    private void CreaPost() {
        String TestoPost = AreaNuovoPost.getText().trim();
        if (TestoPost.isEmpty() || CurrNomiGruppi == null) {
            MostraAlert("Errore", "Il testo del post non può essere vuoto e devi selezionare un gruppo.");
            return;
        }

        Contenuto nuovoContenuto = new Contenuto(TestoPost, java.sql.Date.valueOf(LocalDate.now()), CurrNomiGruppi, userEmail);
        contenutoDAO.insertContenuto(nuovoContenuto);
        AreaNuovoPost.clear();
        MostraPost(CurrNomiGruppi);
    }

    @FXML
    private void Cercagruppo(ActionEvent actionEvent) {
        String searchText = FieldCercaTesto.getText().trim();
        if (searchText.isEmpty()) {
            MostraAlert("Errore", "Il campo di ricerca non può essere vuoto.");
            return;
        }

        List<Gruppo> gruppi = gruppoDAO.searchGruppi(searchText);
        CercaGruppi.getChildren().clear();
        for (Gruppo gruppo : gruppi) {
            MostraGruppo(gruppo.getTitolo());
        }
    }

    private void MostraGruppo(String groupName) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(groupName);
        groupButton.setOnAction(event -> {
            CurrNomiGruppi = groupName;
            Partecipante = false;
            iscrivitiButton.setVisible(true);
            AreaCont.getChildren().clear();
        });
        CercaGruppi.getChildren().add(groupButton);
    }

    @FXML
    private void iscrivitiAlGruppo() {
        if (CurrNomiGruppi == null) {
            MostraAlert("Errore", "Devi selezionare un gruppo.");
            return;
        }

        Partecipante nuovoPartecipante = new Partecipante(userEmail, CurrNomiGruppi, java.sql.Date.valueOf(LocalDate.now()));
        partecipanteDAO.insertPartecipante(nuovoPartecipante);
        iscrivitiButton.setVisible(false);
        MostraGruppi();
        Partecipante = true;
        MostraPost(CurrNomiGruppi);
    }

    private void MostraAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
