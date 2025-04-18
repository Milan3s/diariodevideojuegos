package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    @FXML
    private void initialize() {
        // Al cargar principal.fxml, muestra directamente el contenido de "Inicio"
        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_inicio.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_consolas.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_juegos.fxml"));
            Parent contenido = loader.load();
            setContenido(contenido);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
