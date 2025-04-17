package controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 *
 * @author Milanes
 */
public class SidebarController {

    @FXML
    private Pane panel_principal;
    @FXML
    private Button btnIInicio;
    @FXML
    private Button btnJuegos;
    @FXML
    private Button btnConsolas;
    @FXML
    private Button btnModeradores;
    @FXML
    private Button btnAjustes;
    @FXML
    private Button btnLogros;

    @FXML
    private void accion_inicio(ActionEvent event) {
        try {
            // CARGAS contenido_inicio.fxml en lugar de principal.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_inicio.fxml"));
            Parent contenido = loader.load();

            // Lo insertas en el panel central del principal
            PrincipalController.getInstance().setContenido(contenido);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void marcarBotonActivo(Button botonActivo) {
        // Reinicia estilos de todos los botones si lo deseas
        btnIInicio.setStyle(""); // limpio todos

        // Aplica estilo de activo
        botonActivo.setStyle("-fx-background-color: #cce5ff;"); // ejemplo azul claro
    }

    @FXML
    private void accion_juegos(ActionEvent event) {
    }

    @FXML
    private void accion_consolas(ActionEvent event) {
    }

    @FXML
    private void accion_moderadores(ActionEvent event) {
    }

    @FXML
    private void accion_ajustes(ActionEvent event) {
    }

    @FXML
    private void accion_logros(ActionEvent event) {
    }

}
