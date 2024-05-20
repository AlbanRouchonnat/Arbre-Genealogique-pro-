module com.example.pro {
    requires java.sql;

    requires jdk.compiler;
    requires jdk.jsobject;
    requires org.json;
    opens com.example.pro to javafx.fxml;

}