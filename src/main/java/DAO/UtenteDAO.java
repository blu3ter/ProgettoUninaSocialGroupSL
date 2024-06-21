package DAO;

import Oggetti.Utente;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {
    public Utente getUtenteByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM utente WHERE email = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("username"),
                        rs.getString("bio")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM utente WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM utente WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertUtente(Utente utente) {
        String query = "INSERT INTO utente (nome, cognome, email, password, username, bio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getPassword());
            stmt.setString(5, utente.getUsername());
            stmt.setString(6, utente.getBio()); // Bio può essere null
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
