package DAO;

import Oggetti.Contenuto;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentoDAO {

    public List<Contenuto> getContenutiByGruppo(String nomeGruppo) {
        String query = "SELECT testo FROM commento WHERE gruppo_app = ?";
        List<Contenuto> contenuti = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomeGruppo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String testo = rs.getString("testo");
                Contenuto contenuto = new Contenuto(testo); // Creazione di un oggetto Contenuto
                contenuti.add(contenuto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contenuti;
    }
}
