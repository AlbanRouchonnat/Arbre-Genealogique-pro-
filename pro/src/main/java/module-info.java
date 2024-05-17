module com.example.pro {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires jdk.compiler;

    opens com.example.pro to javafx.fxml;
    exports com.example.pro;
}