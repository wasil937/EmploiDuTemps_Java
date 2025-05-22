module org.example.projetjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.projetjava to javafx.fxml;
    exports org.example.projetjava;
}