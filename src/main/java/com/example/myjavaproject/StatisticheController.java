package com.example.myjavaproject;

import DAO.ContenutoDAO;
import DAO.GruppoDAO;
import Oggetti.Contenuto;
import Oggetti.Gruppo;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.IntStream;

public class StatisticheController {

    @FXML
    private ComboBox<String> meseComboBox;
    @FXML
    private VBox statisticheVBox;

    private String utenteEmail;
    private final GruppoDAO gruppoDAO = new GruppoDAO();
    private final ContenutoDAO contenutoDAO = new ContenutoDAO();

    public void initialize() {
        // Popola la combobox con i numeri dei mesi dell'anno
        meseComboBox.getItems().addAll(IntStream.rangeClosed(1, 12)
                .mapToObj(month -> String.format("%02d", month))
                .toArray(String[]::new));
    }

    public void setUtenteEmail(String utenteEmail) {
        this.utenteEmail = utenteEmail;
    }

    @FXML
    private void MostraRisultati() {
        String mese = meseComboBox.getValue(); //assegna alla variabile appena creata il valore della ComboBox
        if (mese == null) {
            return;
        }

        int meseInInt = Integer.parseInt(mese); // Converti il mese selezionato da String a int
        int Anno = java.time.LocalDate.now().getYear();

        //prende i gruppi in cui l'utente è Admin
        List<Gruppo> gruppi = gruppoDAO.getGruppiDaAdminEmail(utenteEmail);
        statisticheVBox.getChildren().clear();

        for (Gruppo gruppo : gruppi) {
            //per ogni gruppo crea il suo Label
            Label LabelTitoloGruppo = new Label("Gruppo: " + gruppo.getTitolo());
            statisticheVBox.getChildren().add(LabelTitoloGruppo);

            //si fa le 5 operazioni per le statistiche
            Contenuto contenutoMaxCommenti = contenutoDAO.getContenutoConPiuCommenti(gruppo.getTitolo(), meseInInt, Anno);
            Contenuto contenutoMinCommenti = contenutoDAO.getContenutoConMenoCommenti(gruppo.getTitolo(), meseInInt, Anno);
            Contenuto contenutoMaxLikes = contenutoDAO.getContenutoConPiuLikes(gruppo.getTitolo(), meseInInt, Anno);
            Contenuto contenutoMinLikes = contenutoDAO.getContenutoConMenoLikes(gruppo.getTitolo(), meseInInt, Anno);
            double mediaPost = contenutoDAO.getMediaPostPerGruppo(gruppo.getTitolo(), meseInInt, Anno);

            //mette nell'interfaccia ogni risultato con le Label
            statisticheVBox.getChildren().add(new Label("Contenuto con più commenti: " +
                    (contenutoMaxCommenti != null ? contenutoMaxCommenti.getTesto() : "N/A")));
            statisticheVBox.getChildren().add(new Label("Contenuto con meno commenti: " +
                    (contenutoMinCommenti != null ? contenutoMinCommenti.getTesto() : "N/A")));
            statisticheVBox.getChildren().add(new Label("Contenuto con più mi piace: " +
                    (contenutoMaxLikes != null ? contenutoMaxLikes.getTesto() : "N/A")));
            statisticheVBox.getChildren().add(new Label("Contenuto con meno mi piace: " +
                    (contenutoMinLikes != null ? contenutoMinLikes.getTesto() : "N/A")));
            statisticheVBox.getChildren().add(new Label("Numero medio di post giornalieri: " + mediaPost));
            statisticheVBox.getChildren().add(new Label("---------------------------"));
        }
    }
}
