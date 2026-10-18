module com.example.practicaevaluadacorte2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.practicaevaluadacorte2 to javafx.fxml;
    opens com.example.practicaevaluadacorte2.controller to javafx.fxml;
    opens com.example.practicaevaluadacorte2.model to javafx.base;
    exports com.example.practicaevaluadacorte2;
}