package DAO;

import Oggetti.Contenuto;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContenutoDAO {
    public List<Contenuto> getContenutiByGruppo(String gruppoApp) {
        String query = "SELECT testo, data, gruppo_app, email_utente FROM contenuto WHERE gruppo_app = ?";
        List<Contenuto> contenuti = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, gruppoApp);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contenuti.add(new Contenuto(
                        rs.getString("testo"),
                        rs.getDate("data"),
                        rs.getString("gruppo_app"),
                        rs.getString("email_utente")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contenuti;
    }

    public void insertContenuto(Contenuto contenuto) {
        String query = "INSERT INTO contenuto (testo, data, gruppo_app, email_utente) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, contenuto.getTesto());
            stmt.setDate(2, new java.sql.Date(contenuto.getData().getTime()));
            stmt.setString(3, contenuto.getGruppoApp());
            stmt.setString(4, contenuto.getEmailUtente());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
