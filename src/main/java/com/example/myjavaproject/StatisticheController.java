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
    private ComboBox<String> monthComboBox;
    @FXML
    private VBox statisticsVBox;

    private String userEmail;
    private final GruppoDAO gruppoDAO = new GruppoDAO();
    private final ContenutoDAO contenutoDAO = new ContenutoDAO();

    public void initialize() {
        // Populate the monthComboBox with month names
        monthComboBox.getItems().addAll(IntStream.rangeClosed(1, 12)
                .mapToObj(month -> String.format("%02d", month))
                .toArray(String[]::new));
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @FXML
    private void MostraRisultati() {
        String selectedMonth = monthComboBox.getValue();
        if (selectedMonth == null) {
            return;
        }

        int monthValue = Integer.parseInt(selectedMonth); // Converti il mese selezionato da String a int
        int year = java.time.LocalDate.now().getYear();

        List<Gruppo> gruppi = gruppoDAO.getGruppiByAdminEmail(userEmail);
        statisticsVBox.getChildren().clear();

        for (Gruppo gruppo : gruppi) {
            Label groupTitleLabel = new Label("Gruppo: " + gruppo.getTitolo());
            statisticsVBox.getChildren().add(groupTitleLabel);

            Contenuto contenutoMaxCommenti = contenutoDAO.getContenutoConPiuCommenti(gruppo.getTitolo(), monthValue, year);
            Contenuto contenutoMinCommenti = contenutoDAO.getContenutoConMenoCommenti(gruppo.getTitolo(), monthValue, year);
            Contenuto contenutoMaxLikes = contenutoDAO.getContenutoConPiuLikes(gruppo.getTitolo(), monthValue, year);
            Contenuto contenutoMinLikes = contenutoDAO.getContenutoConMenoLikes(gruppo.getTitolo(), monthValue, year);
            double mediaPost = contenutoDAO.getMediaPostPerGruppo(gruppo.getTitolo(), monthValue, year);

            statisticsVBox.getChildren().add(new Label("Contenuto con più commenti: " +
                    (contenutoMaxCommenti != null ? contenutoMaxCommenti.getTesto() : "N/A")));
            statisticsVBox.getChildren().add(new Label("Contenuto con meno commenti: " +
                    (contenutoMinCommenti != null ? contenutoMinCommenti.getTesto() : "N/A")));
            statisticsVBox.getChildren().add(new Label("Contenuto con più mi piace: " +
                    (contenutoMaxLikes != null ? contenutoMaxLikes.getTesto() : "N/A")));
            statisticsVBox.getChildren().add(new Label("Contenuto con meno mi piace: " +
                    (contenutoMinLikes != null ? contenutoMinLikes.getTesto() : "N/A")));
            statisticsVBox.getChildren().add(new Label("Numero medio di post giornalieri: " + mediaPost));
            statisticsVBox.getChildren().add(new Label("---------------------------"));
        }
    }
}
