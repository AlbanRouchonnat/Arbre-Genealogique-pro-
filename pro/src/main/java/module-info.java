module com.example.pro {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires jdk.compiler;
    requires org.json;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;  // Ajout de la directive pour lire le module org.json

    opens com.example.pro to javafx.fxml;
    exports com.example.pro;
}
