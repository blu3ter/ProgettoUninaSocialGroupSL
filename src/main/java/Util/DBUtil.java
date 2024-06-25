package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/progetto_sg_oo_bdd";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        Connection connection;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET search_path TO social_group");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

        return connection;
    }
}

