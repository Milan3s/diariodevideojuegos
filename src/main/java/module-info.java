module main {
    requires javafx.controls;
    requires javafx.fxml;
    opens main to javafx.fxml;
    exports main;
    requires de.jensd.fx.glyphs.fontawesome;
}
