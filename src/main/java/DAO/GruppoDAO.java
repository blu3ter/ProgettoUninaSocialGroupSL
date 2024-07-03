package DAO;

import Oggetti.Gruppo;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GruppoDAO {
    public List<Gruppo> getGruppiByUserEmail(String email) {
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
            e.printStackTrace();
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
            while (rs.next()) {
                Gruppo gruppo = new Gruppo();
                gruppo.setTitolo(rs.getString("titolo"));
                gruppo.setCategoria(rs.getString("categoria"));
                gruppo.setEmailAdmin(rs.getString("email_admin"));
                gruppi.add(gruppo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gruppi;
    }

    // Metodo per ottenere i gruppi in cui l'utente è admin
    public List<Gruppo> getGruppiByAdminEmail(String emailAdmin) {
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
            e.printStackTrace();
        }
        return gruppi;
    }



}
