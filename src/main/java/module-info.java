module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.base;

    requires com.jfoenix; // ✅ JFoenix (Material Design for JavaFX)
    requires de.jensd.fx.glyphs.fontawesome; // FontAwesomeFX

    opens main to javafx.fxml, javafx.graphics;
    opens controllers to javafx.fxml;
    opens models to javafx.base; // Si usas modelos en bindings o TableView
    opens dao to javafx.base;    // Opcional: si accedes desde FXML

    exports main;
    exports controllers;
}
