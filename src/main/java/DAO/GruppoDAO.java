package DAO;

import Oggetti.Gruppo;
import Util.DBUtil;
import Util.DatabaseAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GruppoDAO {
    public List<Gruppo> getGruppiDaUtenteEmail(String email) {
        //la query prendi i gruppi dove partecipa l'utente
        String query = "SELECT g.titolo, g.categoria, g.email_admin FROM gruppo g JOIN partecipante p ON g.titolo = p.titolo_gruppo WHERE p.email_partecipante = ?";
        List<Gruppo> gruppi = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Gruppo gruppo = new Gruppo();
                gruppo.setTitolo(rs.getString("titolo"));
                gruppo.setCategoria(rs.getString("categoria"));
                gruppo.setEmailAdmin(rs.getString("email_admin"));
                gruppi.add(gruppo);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante il recupero dei gruppi", e);
        }
        return gruppi;
    }

    public List<Gruppo> CercaGruppi(String searchText) {
        String query = "SELECT titolo, categoria, email_admin FROM gruppo WHERE titolo ILIKE ? OR categoria ILIKE ?";
        List<Gruppo> gruppi = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { //per ogni risultato crea l'oggetto gruppo e ci inserisce i valori
                Gruppo gruppo = new Gruppo();
                gruppo.setTitolo(rs.getString("titolo"));
                gruppo.setCategoria(rs.getString("categoria"));
                gruppo.setEmailAdmin(rs.getString("email_admin"));
                gruppi.add(gruppo); //inserisce nella listaù
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante la ricerca dei gruppi", e);
        }
        return gruppi;
    }

    // Metodo per ottenere i gruppi in cui l'utente è admin
    public List<Gruppo> getGruppiDaAdminEmail(String emailAdmin) {
        String query = "SELECT titolo, categoria, email_admin FROM gruppo WHERE email_admin = ?";
        List<Gruppo> gruppi = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, emailAdmin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Gruppo gruppo = new Gruppo();
                gruppo.setTitolo(rs.getString("titolo"));
                gruppo.setCategoria(rs.getString("categoria"));
                gruppo.setEmailAdmin(rs.getString("email_admin"));
                gruppi.add(gruppo);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante il recupero dei gruppi", e);
        }
        return gruppi;
    }


    // Metodo per inserire un nuovo gruppo nel database
    public void insertGruppo(Gruppo gruppo) {
        String query = "INSERT INTO gruppo (titolo, categoria, email_admin, data_creazione) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, gruppo.getTitolo());
            stmt.setString(2, gruppo.getCategoria());
            stmt.setString(3, gruppo.getEmailAdmin());
            stmt.setDate(4, Date.valueOf(gruppo.getDataCreazione()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante l'inserimento di un nuovo gruppo", e);
        }
    }


}
