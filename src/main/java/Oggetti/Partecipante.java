package Oggetti;

import java.time.LocalDate;
import java.util.Date;

public class Partecipante {
    private String emailPartecipante;
    private String titoloGruppo;
    private Date dataIscrizione;

    public Partecipante(String userEmail, String titolo, LocalDate now) {
    }

    public String getEmailPartecipante() {
        return emailPartecipante;
    }

    public void setEmailPartecipante(String emailPartecipante) {
        this.emailPartecipante = emailPartecipante;
    }

    public String getTitoloGruppo() {
        return titoloGruppo;
    }

    public void setTitoloGruppo(String titoloGruppo) {
        this.titoloGruppo = titoloGruppo;
    }

    public Date getDataIscrizione() {
        return dataIscrizione;
    }

    public void setDataIscrizione(Date dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    public Partecipante(String emailPartecipante, String titoloGruppo, Date dataIscrizione){
        this.emailPartecipante=emailPartecipante;
        this.titoloGruppo=titoloGruppo;
        this.dataIscrizione=dataIscrizione;
    }
}
