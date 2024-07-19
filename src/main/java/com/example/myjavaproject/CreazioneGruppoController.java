package com.example.myjavaproject;

import DAO.GruppoDAO;
import Oggetti.Gruppo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class CreazioneGruppoController {

    public Text infoPerFieldCategoria;
    public Text textTitoloSchermata;
    public Button creaGruppoButton;
    public Button tornaIndietroButton;
    @FXML
    private TextField fieldTitoloGruppo;
    @FXML
    private TextField fieldCategoria;
    private String utenteEmail;

    private final GruppoDAO gruppoDAO = new GruppoDAO();

    public void setUtenteEmail(String utenteEmail) {
        this.utenteEmail = utenteEmail;
    }

    public void CreaGruppo(ActionEvent actionEvent) {
        String titolo = fieldTitoloGruppo.getText();
        String categoria = fieldCategoria.getText();

        if (titolo.isEmpty() || categoria.isEmpty()) {
            MostraAlert("Per favore, riempi tutti i campi.");
            return;
        }

        Gruppo nuovoGruppo = new Gruppo();
        nuovoGruppo.setTitolo(titolo);
        nuovoGruppo.setCategoria(categoria);
        nuovoGruppo.setEmailAdmin(utenteEmail);
        nuovoGruppo.setDataCreazione(LocalDate.now());

        try {
            gruppoDAO.insertGruppo(nuovoGruppo);
            MostraAlert("Gruppo creato con successo!");

            // Dopo aver creato il gruppo, possiamo tornare alla schermata HomePage
            RitornaAllaSchermataHomePage(actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
            MostraAlert("Errore durante la creazione del gruppo. Per favore riprova.");
        }
    }

    private void MostraAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void RitornaAllaSchermataHomePage(ActionEvent actionEvent) {
        try {
            // Carica il file FXML della schermata HomePage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            Parent root = loader.load();

            // Passa il controller alla HomePage con l'email dell'utente
            HomePageController controller = loader.getController();
            controller.setUtenteEmail(utenteEmail);

            // Ottiene la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            MostraAlert("Errore durante il caricamento della Home Page.");
        }
    }
}
