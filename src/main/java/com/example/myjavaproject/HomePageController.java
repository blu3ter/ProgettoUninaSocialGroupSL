package com.example.myjavaproject;

import DAO.ContenutoDAO;
import DAO.GruppoDAO;
import DAO.PartecipanteDAO;
import Oggetti.Contenuto;
import Oggetti.Gruppo;
import Oggetti.Partecipante;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class HomePageController {
    public ScrollPane gruppiScrollPane;
    public ScrollPane GruppiTrovatiScrollPane;
    public Button CreaContButton;
    public ScrollPane postScrollPane;
    public Button StatisticheButton;
    @FXML
    private VBox groupVBox;
    @FXML
    private VBox CercaGruppi;
    @FXML
    private VBox AreaCont;
    @FXML
    private TextField FieldCercaTesto;
    @FXML
    private TextArea AreaNuovoPost;
    @FXML
    private Button iscrivitiButton;

    private String userEmail;
    private String titoloGruppo;
    private boolean Partecipante;

    private final GruppoDAO gruppoDAO = new GruppoDAO();
    private final PartecipanteDAO partecipanteDAO = new PartecipanteDAO();
    private final ContenutoDAO contenutoDAO = new ContenutoDAO();

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        MostraGruppi();
    }

    private void MostraGruppi() {
        List<Gruppo> gruppi = gruppoDAO.getGruppiByUserEmail(userEmail);
        groupVBox.getChildren().clear();
        for (Gruppo gruppo : gruppi) {
            PartecipanteGruppo(gruppo.getTitolo());
        }
    }

    private void PartecipanteGruppo(String NomiGruppi) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(NomiGruppi);
        groupButton.setOnAction(event -> {
            titoloGruppo = NomiGruppi;
            Partecipante = true;
            MostraPost(NomiGruppi);
            iscrivitiButton.setVisible(false);
        });
        groupVBox.getChildren().add(groupButton);
    }

    private void MostraPost(String NomiGruppi) {
        if (!Partecipante) {
            MostraAlert("Devi iscriverti al gruppo per visualizzare i contenuti.");
            return;
        }

        List<Contenuto> contenuti = contenutoDAO.getContenutiGruppo(NomiGruppi);
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
        if (TestoPost.isEmpty() || titoloGruppo == null) {
            MostraAlert("Il testo del post non può essere vuoto e devi selezionare un gruppo.");
            return;
        }

        Contenuto nuovoContenuto = new Contenuto(TestoPost, java.sql.Date.valueOf(LocalDate.now()), titoloGruppo, userEmail);
        contenutoDAO.insertContenuto(nuovoContenuto);
        AreaNuovoPost.clear();
        MostraPost(titoloGruppo);
    }

    @FXML
    private void Cercagruppo() {
        String CercaTesto = FieldCercaTesto.getText().trim();
        if (CercaTesto.isEmpty()) {
            MostraAlert("Il campo di ricerca non può essere vuoto.");
            return;
        }

        List<Gruppo> gruppi = gruppoDAO.CercaGruppi(CercaTesto);
        CercaGruppi.getChildren().clear();
        for (Gruppo gruppo : gruppi) {
            MostraGruppo(gruppo.getTitolo());
        }
    }

    private void MostraGruppo(String NomiGruppi) {
        javafx.scene.control.Button groupButton = new javafx.scene.control.Button(NomiGruppi);
        groupButton.setOnAction(event -> {
            titoloGruppo = NomiGruppi;
            Partecipante = false;
            iscrivitiButton.setVisible(true);
            AreaCont.getChildren().clear();
        });
        CercaGruppi.getChildren().add(groupButton);
    }

    private void MostraAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void iscrivitiAlGruppo() {
        if (titoloGruppo == null) {
            MostraAlert("Devi selezionare un gruppo.");
            return;
        }

        // Check if the user is already subscribed to the group
        if (partecipanteDAO.GiaPartecipante(userEmail, titoloGruppo)) {
            MostraAlert("Sei già iscritto a questo gruppo!");
            return;
        }

        Partecipante nuovoPartecipante = new Partecipante(userEmail, titoloGruppo, Date.valueOf(LocalDate.now()));
        partecipanteDAO.insertPartecipante(nuovoPartecipante);
        iscrivitiButton.setVisible(false);
        MostraGruppi();
        Partecipante = true;
        MostraPost(titoloGruppo);
    }

    @FXML
    private void MostraStatistiche() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistiche.fxml"));
            Parent root = loader.load();

            StatisticheController controller = loader.getController();
            controller.setUserEmail(userEmail);

            Stage stage = new Stage();
            stage.setTitle("Statistiche Mensili");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
