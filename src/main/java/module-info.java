module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires javafx.media;
    requires de.jensd.fx.glyphs.fontawesome; // <- necesario para usar FontAwesomeIconView

    opens main to javafx.fxml, javafx.graphics;
    opens controllers to javafx.fxml;

    exports main;
    exports controllers;
}
