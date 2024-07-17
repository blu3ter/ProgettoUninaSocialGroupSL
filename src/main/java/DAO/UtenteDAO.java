package DAO;

import Oggetti.Utente;
import Util.DBUtil;
import Util.DatabaseAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {
    public Utente GetEmailePassword(String email, String password) {
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
            throw new DatabaseAccessException("Errore durante l'accesso al database per verificare l'esistenza dell'Utente",e);
        }
        return null;
    }

    public boolean EmailEsistente(String email) {
        String query = "SELECT COUNT(*) FROM utente WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante l'accesso al database per verificare l'esistenza dell'email",e);
        }
        return false;
    }

    public boolean UsernameEsistente(String username) {
        String query = "SELECT COUNT(*) FROM utente WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante l'accesso al database per verificare l'esistenza dell'Username",e);

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
            throw new DatabaseAccessException("Errore durante l'inserimento di un nuovo utente",e);
        }
    }

    public static String getUsernameDaEmail(String email) {
        String query = "SELECT username FROM utente WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante il recupero dell'username",e);
        }
        return null;
    }
}
