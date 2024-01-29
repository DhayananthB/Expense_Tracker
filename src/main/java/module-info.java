module com.example.myproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.myproject to javafx.fxml;
    exports com.example.myproject;
}