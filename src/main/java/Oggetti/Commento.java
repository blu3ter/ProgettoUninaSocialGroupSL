package Oggetti;

import java.util.Date;

public class Commento {
    private String emailPartecipante;
    private String gruppoRiferimento;
    private String testo;
    private Date data;


    public void setEmailPartecipante(String emailPartecipante) {
        this.emailPartecipante = emailPartecipante;
    }

    public String getEmailPartecipante() {
        return emailPartecipante;
    }

    public void setGruppoRiferimento(String gruppoRiferimento) {
        this.gruppoRiferimento = gruppoRiferimento;
    }

    public String getGruppoRiferimento() {
        return gruppoRiferimento;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getTesto() {
        return testo;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return data;
    }
}
