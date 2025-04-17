module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.base;

    opens main to javafx.fxml;
    opens controllers to javafx.fxml; // Abre el paquete controllers a javafx.fxml

    exports main;
    exports controllers;
}
