module com.example.practicaevaluadacorte2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.practicaevaluadacorte2 to javafx.fxml;
    opens com.example.practicaevaluadacorte2.controller to javafx.fxml;
    exports com.example.practicaevaluadacorte2;
}