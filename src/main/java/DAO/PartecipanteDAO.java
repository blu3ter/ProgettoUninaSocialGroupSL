package DAO;

import Oggetti.Partecipante;
import Util.DBUtil;

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
            e.printStackTrace();
        }
    }

    public boolean GiaPartecipante(String email, String titoloGruppo) {
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
            e.printStackTrace();
        }
        return false;
    }
}
