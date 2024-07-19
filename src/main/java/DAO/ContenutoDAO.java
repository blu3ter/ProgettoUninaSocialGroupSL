package DAO;

import Oggetti.Contenuto;
import Util.DBUtil;
import Util.DatabaseAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ContenutoDAO {
    public List<Contenuto> getContenutiGruppo(String gruppoApp) {

        String query = "SELECT testo, data, gruppo_app, email_utente FROM contenuto WHERE gruppo_app = ?";
        List<Contenuto> contenuti = new ArrayList<>(); //crea una lista per mettere i risultati della query
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, gruppoApp);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { //scorre i risultati, crea l'oggetto e inserisce nell'array
                contenuti.add(new Contenuto(
                        rs.getString("testo"),
                        rs.getDate("data"),
                        rs.getString("gruppo_app"),
                        rs.getString("email_utente")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante il recupero dei contenuti",e);
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
            throw new DatabaseAccessException("Errore durante l'inserimento del contenuto",e);
        }
    }

    private Contenuto getContenutoDallaQuery(String titoloGruppo, int mese, int anno, String query) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Imposta i parametri della query
            stmt.setString(1, titoloGruppo);
            stmt.setInt(2, mese);
            stmt.setInt(3, anno);

            // Esecuzione della query e recupero del risultato
            ResultSet rs = stmt.executeQuery();

            // Se c'è un risultato, crea un nuovo oggetto Contenuto con i valori ottenuti dal ResultSet
            if (rs.next()) {
                return new Contenuto(
                        rs.getString("testo"),
                        rs.getDate("data"),
                        rs.getString("gruppo_app"),
                        rs.getString("email_utente")
                );
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante il recupero del contenuto", e);
        }
        return null;
    }




    // Metodo per ottenere i contenuti con più commenti in un gruppo specifico in un mese specifico
    public Contenuto getContenutoConPiuCommenti(String titoloGruppo, int mese, int anno) {
        //la query che segue prende le info del contenuti e joina con commenti raggruppandoli, e si prende il primo più alto
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(co.testo) AS numero_commenti " +
                "FROM contenuto c " +
                "LEFT JOIN commento co ON c.id_contenuto = co.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_commenti DESC " +
                "LIMIT 1";

        return getContenutoDallaQuery(titoloGruppo, mese, anno, query);
    }



    // Metodo per ottenere i contenuti con meno commenti in un gruppo specifico in un mese specifico
    public Contenuto getContenutoConMenoCommenti(String titoloGruppo, int mese, int anno) {
        //la query che segue prende le info del contenuti e joina con commenti raggruppandoli, e si prende il primo più alto
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(co.testo) AS numero_commenti " +
                "FROM contenuto c " +
                "LEFT JOIN commento co ON c.id_contenuto = co.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_commenti ASC " +
                "LIMIT 1";
        return getContenutoDallaQuery(titoloGruppo, mese, anno, query);
    }



    // Metodo per ottenere la media dei post in un gruppo specifico in un mese specifico
    public double getMediaPostPerGruppo(String titoloGruppo, int mese, int anno) {
        String query = "SELECT AVG(media_post_count) " + //prende la media dai contenuti con la media gia fatta nel FROM
                "FROM ( " +
                "    SELECT COUNT(*) AS media_post_count " +
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
                return rs.getDouble("media_post_giornaliera");
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Errore durante il recupero dei contenuti",e);
        }
        return 0;
    }

    // Metodo per ottenere i contenuti con più Likes in un gruppo specifico in un mese specifico
    public Contenuto getContenutoConPiuLikes(String titoloGruppo, int mese, int anno) {
        //la query che segue prende le info del contenuti e joina con like raggruppandoli, e si prende il primo più alto
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(m.tipo_like) AS numero_likes " +
                "FROM contenuto c " +
                "LEFT JOIN mi_piace m ON c.id_contenuto = m.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_likes DESC " +
                "LIMIT 1";
        return getContenutoDallaQuery(titoloGruppo, mese, anno, query);
    }

    // Metodo per ottenere i contenuti con meno Likes in un gruppo specifico in un mese specifico
    public Contenuto getContenutoConMenoLikes(String titoloGruppo, int mese, int anno) {
        //la query che segue prende le info del contenuti e joina con like raggruppandoli, e si prende il primo più alto
        String query = "SELECT c.testo, c.data, c.gruppo_app, c.email_utente, COUNT(m.tipo_like) AS numero_likes " +
                "FROM contenuto c " +
                "LEFT JOIN mi_piace m ON c.id_contenuto = m.id_contenuto " +
                "WHERE c.gruppo_app = ? AND EXTRACT(MONTH FROM c.data) = ? AND EXTRACT(YEAR FROM c.data) = ? " +
                "GROUP BY c.id_contenuto " +
                "ORDER BY numero_likes ASC " +
                "LIMIT 1";
        return getContenutoDallaQuery(titoloGruppo, mese, anno, query);
    }

}
