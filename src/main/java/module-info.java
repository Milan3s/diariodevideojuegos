module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    opens main to javafx.fxml, javafx.graphics;
    opens controllers to javafx.fxml;

    exports main;
    exports controllers;
}
