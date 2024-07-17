module com.example.myjavaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.myjavaproject to javafx.fxml;
    exports com.example.myjavaproject;
}