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
    public ScrollPane gruppiTrovatiScrollPane;
    public Button creaContButton;
    public ScrollPane contenutiScrollPane;
    public Button StatisticheButton;
    public Button ritornaLoginButton;
    public Button lasciaGruppoButton;
    public Button creaGruppoButton;
    @FXML
    private VBox gruppiVBox;
    @FXML
    private VBox cercaGruppi;
    @FXML
    private VBox areaContenuto;
    @FXML
    private TextField fieldCercaTesto;
    @FXML
    private TextArea areaNuovoPost;
    @FXML
    private Button iscrivitiButton;
    @FXML
    private Label labelGruppoSelezionato;

    private String utenteEmail;
    private String titoloGruppo;
    private boolean Partecipante;

    private final GruppoDAO gruppoDAO = new GruppoDAO();
    private final PartecipanteDAO partecipanteDAO = new PartecipanteDAO();
    private final ContenutoDAO contenutoDAO = new ContenutoDAO();


    public void setUtenteEmail(String utenteEmail) {
        //tutto il controlelr si prenderà l'email dell'utente che ha accesso per gestire i metodi
        this.utenteEmail = utenteEmail;
        MostraGruppiDovePartecipi();
    }

    private void MostraGruppiDovePartecipi() {
        //Prende la lista dei gruppi in cui l'useername è la stessa di chi ha accesso.
        List<Gruppo> gruppi = gruppoDAO.getGruppiDaUtenteEmail(utenteEmail);

        //pulisce la Vbox dove vengono mostrati i gruppi
        gruppiVBox.getChildren().clear();

        //per ogni gruppo chiama la funzione che sta sotto
        for (Gruppo gruppo : gruppi) {
            CaricaGruppo(gruppo.getTitolo());
        }
    }

    private void CaricaGruppo(String NomeGruppo) {
        //crea il bottone con il nome del gruppo
        javafx.scene.control.Button gruppoButton = new javafx.scene.control.Button(NomeGruppo);
        gruppoButton.setOnAction(event -> { // azioni che compie al click
            titoloGruppo = NomeGruppo;
            Partecipante = true;
            iscrivitiButton.setDisable(true); // Disabilita il bottone dato che gia partecipa in quel gruppo.
            MostraContenuti(NomeGruppo);
            labelGruppoSelezionato.setText("Gruppo selezionato: " + NomeGruppo);
        });
        gruppiVBox.getChildren().add(gruppoButton);
    }

    private void MostraContenuti(String NomeGruppo) {
        //crea una lista in cui inserisce i post di un gruppo specifico
        List<Contenuto> contenuti = contenutoDAO.getContenutiGruppo(NomeGruppo);
        areaContenuto.getChildren().clear();

        //per ogni contenuto chiama il metodo sotto
        for (Contenuto contenuto : contenuti) {
            CaricaContenuto(contenuto);
        }
    }

    private void CaricaContenuto(Contenuto contenuto) {
        String emailUtente = contenuto.getEmailUtente();//mi prende l'email dell'utente che ha creato il post
        String username = UtenteDAO.getUsernameDaEmail(emailUtente); // estrae l'username dall'email precedente.

        // Crea gli oggetti Text per l'username (in grassetto) e il contenuto (a capo)
        Text testoUsername = new Text(username + ": ");
        testoUsername.setStyle("-fx-font-weight: bold;");
        Text testoContenuto = new Text(contenuto.getTesto());

        // Crea un TextFlow per raggupprare l'username e il contenuto
        TextFlow textFlow = new TextFlow(testoUsername, testoContenuto);
        textFlow.setStyle("-fx-padding: 10;");

        // Aggiunge il TextFlow all'area dedicata ai contenuti
        areaContenuto.getChildren().add(textFlow);
    }

    @FXML
    private void CreaPost() {
        //salva nella variabile Testopost il testo scritto dall'utente
        String testoPost = areaNuovoPost.getText().trim();
        if (testoPost.isEmpty() || titoloGruppo == null) {
            MostraAlert("Il testo del post non può essere vuoto e devi selezionare un gruppo.");
            return;
        }

        //crea il contenuto, lo inserisce nel DB e ricarica i Post
        Contenuto nuovoContenuto = new Contenuto(testoPost, java.sql.Date.valueOf(LocalDate.now()), titoloGruppo, utenteEmail);
        contenutoDAO.insertContenuto(nuovoContenuto);
        areaNuovoPost.clear();
        MostraContenuti(titoloGruppo);
    }

    @FXML
    private void Cercagruppo() {
        //mi salvo nella variabile sottostante ciò che l'utente ha cercato
        String cercaTesto = fieldCercaTesto.getText().trim();
        if (cercaTesto.isEmpty()) {
            MostraAlert("Il campo di ricerca non può essere vuoto.");
            return;
        }
        //crea una lista con i gruppi estratti dalla ricerca
        List<Gruppo> gruppi = gruppoDAO.CercaGruppi(cercaTesto);
        cercaGruppi.getChildren().clear();
        for (Gruppo gruppo : gruppi) {
            MostraGruppoCercato(gruppo.getTitolo()); //li mostra attraverso i bottoni(vedi metodo sotto)
        }
    }

    private void MostraGruppoCercato(String NomeGruppo) {
        javafx.scene.control.Button BottoneGruppo = new javafx.scene.control.Button(NomeGruppo);
        BottoneGruppo.setOnAction(event -> {
            titoloGruppo = NomeGruppo;

            //se gia partecipi ti disabilità il pulsante iscrivitiButton
            Partecipante = partecipanteDAO.GiaPartecipante(utenteEmail, titoloGruppo);
            iscrivitiButton.setDisable(Partecipante);
            areaContenuto.getChildren().clear();
            if (Partecipante) {
                MostraContenuti(NomeGruppo);
            }
            labelGruppoSelezionato.setText("Gruppo selezionato: " + NomeGruppo);
        });
        //aggiunge il bottone nell'area CercaGruppi
        cercaGruppi.getChildren().add(BottoneGruppo);
    }

    private void MostraAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void IscrivitiAlGruppo() {
        if (titoloGruppo == null) {
            MostraAlert("Devi selezionare un gruppo.");
            return;
        }

        // Controlla se l'utente partecipa gia in quel gruppo
        if (partecipanteDAO.GiaPartecipante(utenteEmail, titoloGruppo)) {
            MostraAlert("Sei già iscritto a questo gruppo!");
            return;
        }
        //se non partecipa, crea il partecipante
        Partecipante nuovoPartecipante = new Partecipante(utenteEmail, titoloGruppo, Date.valueOf(LocalDate.now()));
        partecipanteDAO.insertPartecipante(nuovoPartecipante); //inserisce nel DB
        iscrivitiButton.setDisable(true); // Disabibilita il bottone dopo l'iscrizione
        MostraGruppiDovePartecipi();
        Partecipante = true;
        MostraContenuti(titoloGruppo);
        labelGruppoSelezionato.setText("Gruppo selezionato: " + titoloGruppo);
    }

    @FXML
    private void MostraStatistiche() {
        try {
            //carica la schermata per le statistiche
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistiche.fxml"));
            Parent root = loader.load();

            //passa il controller a StatisticheController con l'utente che ha accesso in quel momento
            StatisticheController controller = loader.getController();
            controller.setUtenteEmail(utenteEmail);

            //non crea una nuova scena, dunque la HomePage rimane nell sfondo
            Stage stage = new Stage();
            stage.setTitle("Statistiche Mensili");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RitornaLogin(ActionEvent actionEvent) throws IOException {
        //Carica la pagina iniziale
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    private void LasciaGruppo() {
        if (titoloGruppo == null) {
            MostraAlert("Devi selezionare un gruppo.");
            return;
        }

        Partecipante partecipante = new Partecipante(utenteEmail, titoloGruppo, null);
        partecipanteDAO.deletePartecipante(partecipante); //elimina dal DB
        iscrivitiButton.setDisable(false); // Abilita di nuovo il bottone, dato che ora non partecipi più

        //ricarica i gruppi
        MostraGruppiDovePartecipi();
        labelGruppoSelezionato.setText("Nessun gruppo selezionato");
        areaContenuto.getChildren().clear();
        Partecipante = false;
    }

    public void VaiAllaSchermataCreaGruppo(ActionEvent actionEvent) throws IOException {
        //carica la schermata per creare un nuovo gruppo
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreazioneGruppo.fxml"));
        Parent root = fxmlLoader.load();  // carica il file FXML e costruisce la scena

        //passa il controller a CreazioneGruppoController con l'utente che ha accesso in quel momento
        CreazioneGruppoController controller = fxmlLoader.getController();
        controller.setUtenteEmail(utenteEmail);

        // Ottieni la finestra corrente e imposta la nuova scena
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);  // utilizza il root appena caricato
        stage.setTitle("Creazione Gruppo");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

}
