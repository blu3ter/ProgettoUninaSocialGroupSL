module com.example.myjavaproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.myjavaproject to javafx.fxml;
    exports com.example.myjavaproject;
}