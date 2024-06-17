package Util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {

    public Connection conn(String dbname, String user, String pass){

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres" = dbname, user, pass);
            if (conn != null) {
                System.out.println("Connessione andata bene");
            }
            else {
                System.out.println("Connessione fallita");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

}
