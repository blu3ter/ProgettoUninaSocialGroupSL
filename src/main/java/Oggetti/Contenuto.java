package Oggetti;

import java.util.Date;

public class Contenuto {
    private String testo;
    private Date data;
    private String gruppoApp;
    private String emailUtente;

    // set e get
    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getGruppoApp() {
        return gruppoApp;
    }

    public void setGruppoApp(String gruppoApp) {
        this.gruppoApp = gruppoApp;
    }

    public String getEmailUtente() {
        return emailUtente;
    }

    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }

    public Contenuto(String testo,Date data,String gruppoApp,String emailUtente){
        this.testo=testo;
        this.data=data;
        this.gruppoApp=gruppoApp;
        this.emailUtente=emailUtente;
    }

    public Contenuto(){

    }
}
