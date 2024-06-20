package DAO;

import Oggetti.Partecipante;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

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
}

