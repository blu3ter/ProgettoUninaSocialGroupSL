package Oggetti;

import java.time.LocalDate;

public class Gruppo {
    private String titolo;
    private String categoria;
    private String emailAdmin;
    private LocalDate dataCreazione; // New attribute

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Gruppo() {
    }

    public Gruppo(String titolo, String categoria, String emailAdmin, LocalDate dataCreazione) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.emailAdmin = emailAdmin;
        this.dataCreazione = dataCreazione;
    }
}
