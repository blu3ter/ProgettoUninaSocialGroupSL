package DAO;

import Oggetti.Partecipante;
import Util.DBUtil;
import Util.DatabaseAccessException;

import java.sql.*;

public class PartecipanteDAO {
    public void insertPartecipante(Partecipante partecipante) {
        String query = "INSERT INTO partecipante (email_partecipante, titolo_gruppo, data_iscrizione) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, partecipante.getEmailPartecipante());
            stmt.setString(2, partecipante.getTitoloGruppo());
            stmt.setDate(3, new Date(partecipante.getDataIscrizione().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante l'inserimento di un nuovo partecipante",e);
        }
    }
    public void deletePartecipante(Partecipante partecipante){
        String query = "DELETE FROM partecipante WHERE email_partecipante = ? AND titolo_gruppo = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, partecipante.getEmailPartecipante());
            stmt.setString(2, partecipante.getTitoloGruppo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante la cancellazione del partecipante",e);
        }

    }

    public boolean GiaPartecipante(String email, String titoloGruppo) {
        //controlla se esite l'utente partecipa gia in quel gruppo
        String query = "SELECT COUNT(*) FROM partecipante WHERE email_partecipante = ? AND titolo_gruppo = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, titoloGruppo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante la verifica del partecipante",e);
        }
        return false;
    }
}
