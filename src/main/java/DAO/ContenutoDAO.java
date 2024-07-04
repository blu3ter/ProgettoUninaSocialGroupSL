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
    public List<Contenuto> getContenutiGruppo(String gruppoApp) {
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

    public Contenuto getContenutoConPiuCommenti(String titoloGruppo, int mese, int anno) {
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(co.testo) AS numero_commenti " +
                "FROM contenuto c " +
                "LEFT JOIN commento co ON c.id_contenuto = co.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_commenti DESC " +
                "LIMIT 1";
        return getContenutoByQuery(titoloGruppo, mese, anno, query);
    }

    // Metodo per ottenere i contenuti con meno commenti in un gruppo specifico in un mese specifico
    public Contenuto getContenutoConMenoCommenti(String titoloGruppo, int mese, int anno) {
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(co.testo) AS numero_commenti " +
                "FROM contenuto c " +
                "LEFT JOIN commento co ON c.id_contenuto = co.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_commenti ASC " +
                "LIMIT 1";
        return getContenutoByQuery(titoloGruppo, mese, anno, query);
    }




    private Contenuto getContenutoByQuery(String titoloGruppo, int mese, int anno, String query) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, titoloGruppo);
            stmt.setInt(2, mese);
            stmt.setInt(3, anno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Contenuto(
                        rs.getString("testo"),
                        rs.getDate("data"),
                        rs.getString("gruppo_app"),
                        rs.getString("email_utente")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere la media dei post in un gruppo specifico in un mese specifico
    public double getMediaPostPerGruppo(String titoloGruppo, int mese, int anno) {
        String query = "SELECT AVG(daily_post_count) AS average_posts_per_day " +
                "FROM ( " +
                "    SELECT COUNT(*) AS daily_post_count " +
                "    FROM contenuto " +
                "    WHERE gruppo_app = ? AND EXTRACT(MONTH FROM data) = ? AND EXTRACT(YEAR FROM data) = ? " +
                "    GROUP BY DATE(data) " +
                ") AS daily_counts";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, titoloGruppo);
            stmt.setInt(2, mese);
            stmt.setInt(3, anno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average_posts_per_day");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public Contenuto getContenutoConPiuLikes(String titoloGruppo, int mese, int anno) {
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(m.tipo_like) AS numero_likes " +
                "FROM contenuto c " +
                "LEFT JOIN mi_piace m ON c.id_contenuto = m.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_likes DESC " +
                "LIMIT 1";
        return getContenutoByQuery(titoloGruppo, mese, anno, query);
    }

    public Contenuto getContenutoConMenoLikes(String titoloGruppo, int mese, int anno) {
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(m.tipo_like) AS numero_likes " +
                "FROM contenuto c " +
                "LEFT JOIN mi_piace m ON c.id_contenuto = m.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_likes ASC " +
                "LIMIT 1";
        return getContenutoByQuery(titoloGruppo, mese, anno, query);
    }

}
