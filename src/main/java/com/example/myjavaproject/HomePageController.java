package com.example.myjavaproject;

import DAO.ContenutoDAO;
import DAO.GruppoDAO;
import DAO.PartecipanteDAO;
import DAO.UtenteDAO;
import Oggetti.Contenuto;
import Oggetti.Gruppo;
import Oggetti.Partecipante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class HomePageController {
    public ScrollPane gruppiScrollPane;
    public ScrollPane GruppiTrovatiScrollPane;
    public Button CreaContButton;
    public ScrollPane postScrollPane;
    public Button StatisticheButton;
    public Button RitornaLoginButton;
    public Button LaciaGruppoButton;
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
    @FXML
    private Label selectedGroupLabel;

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
            iscrivitiButton.setDisable(true); // Disable the button if the user is already a participant
            MostraPost(NomiGruppi);
            selectedGroupLabel.setText("Gruppo selezionato: " + NomiGruppi);
        });
        groupVBox.getChildren().add(groupButton);
    }

    private void MostraPost(String NomiGruppi) {
        List<Contenuto> contenuti = contenutoDAO.getContenutiGruppo(NomiGruppi);
        AreaCont.getChildren().clear();
        for (Contenuto contenuto : contenuti) {
            CaricaContenuto(contenuto);
        }
    }

    private void CaricaContenuto(Contenuto contenuto) {
        String emailUtente = contenuto.getEmailUtente();
        String username = UtenteDAO.getUsernameDaEmail(emailUtente); // Ottieni l'username

        // Crea gli oggetti Text per l'username (in grassetto) e il contenuto (a capo)
        Text usernameText = new Text(username + ": ");
        usernameText.setStyle("-fx-font-weight: bold;");
        Text contenutoText = new Text(contenuto.getTesto());

        // Crea un TextFlow per contenere l'username e il contenuto
        TextFlow textFlow = new TextFlow(usernameText, contenutoText);
        textFlow.setStyle("-fx-padding: 10;");

        // Aggiungi il TextFlow al contenitore
        AreaCont.getChildren().add(textFlow);
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
            Partecipante = partecipanteDAO.GiaPartecipante(userEmail, titoloGruppo);
            iscrivitiButton.setDisable(Partecipante);
            AreaCont.getChildren().clear();
            if (Partecipante) {
                MostraPost(NomiGruppi);
            }
            selectedGroupLabel.setText("Gruppo selezionato: " + NomiGruppi);
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
        iscrivitiButton.setDisable(true); // Disable the button after subscription
        MostraGruppi();
        Partecipante = true;
        MostraPost(titoloGruppo);
        selectedGroupLabel.setText("Gruppo selezionato: " + titoloGruppo);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RitornaLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    private void LaciaGruppo() {
        if (titoloGruppo == null) {
            MostraAlert("Devi selezionare un gruppo.");
            return;
        }

        Partecipante partecipante = new Partecipante(userEmail, titoloGruppo, (java.util.Date) null);
        partecipanteDAO.deletePartecipante(partecipante);
        iscrivitiButton.setDisable(false); // Enable the button after leaving the group
        MostraGruppi();
        selectedGroupLabel.setText("Nessun gruppo selezionato");
        AreaCont.getChildren().clear();
        Partecipante = false;
    }

}
