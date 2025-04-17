package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Milanes
 */

public class PrincipalController {

    @FXML
    private AnchorPane contenidoCentral;

    private static PrincipalController instance;

    public PrincipalController() {
        instance = this;
    }

    public static PrincipalController getInstance() {
        return instance;
    }

    public void setContenido(Node node) {
        contenidoCentral.getChildren().setAll(node);
    }

}