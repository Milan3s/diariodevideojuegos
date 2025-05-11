package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private VBox sidebar;

    @FXML
    private Button btnInicio;
    @FXML
    private Button btnJuegos;
    @FXML
    private Button btnConsolas;
    @FXML
    private Button btnLogros;
    @FXML
    private Button btnModerador;
    @FXML
    private Button btnDatosAuxiliares;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text tituloSidebar;

    // Submenú de Inicio
    @FXML
    private VBox contenedorSubmenuInicio;
    @FXML
    private VBox submenuInicio;
    @FXML
    private Label flechaSubmenuInicio;

    @FXML
    private Button btnResumenInicio;
    @FXML
    private Button btnMetasTwitch;
    @FXML
    private Button btnMetasEspecificas;
    @FXML
    private Button btnMejorasCanal;
    @FXML
    private Button btnEventosExtensibles;

    private boolean submenuInicioVisible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarVista("/views/inicio.fxml");
    }

    // ==========================
    // Carga de vistas principales
    // ==========================
    @FXML
    private void handleJuegos() {
        cargarVista("/views/juegos.fxml");
    }

    @FXML
    private void handleConsolas() {
        cargarVista("/views/consolas.fxml");
    }

    @FXML
    private void handleLogros() {
        cargarVista("/views/logros.fxml");
    }

    @FXML
    private void handleModerador() {
        cargarVista("/views/moderadores.fxml");
    }

    @FXML
    private void handleDatosAuxiliares(ActionEvent event) {
        cargarVista("/views/datos_auxiliares.fxml");
    }

    // ==========================
    // Submenú Inicio
    // ==========================
    @FXML
    private void toggleSubmenuInicio(ActionEvent event) {
        submenuInicioVisible = !submenuInicioVisible;
        submenuInicio.setVisible(submenuInicioVisible);
        submenuInicio.setManaged(submenuInicioVisible);
        flechaSubmenuInicio.setVisible(submenuInicioVisible);
        flechaSubmenuInicio.setManaged(submenuInicioVisible);
    }

    @FXML
    private void handleResumenInicio(ActionEvent event) {
        cargarVista("/views/inicio.fxml");
    }

    @FXML
    private void handleMetasTwitch(ActionEvent event) {
        cargarVista("/views/metas_twitch.fxml");
    }

    @FXML
    private void handleMetasEspecificas(ActionEvent event) {
        cargarVista("/views/metas_especificas.fxml");
    }

    @FXML
    private void handleMejorasCanal(ActionEvent event) {
        cargarVista("/views/mejorasdelcanal.fxml");
    }

    @FXML
    private void handleEventosExtensibles(ActionEvent event) {
        cargarVista("/views/eventos_extensibles.fxml");
    }

    // ==========================
    // Utilidad: Carga de FXML
    // ==========================
    private void cargarVista(String rutaFXML) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(rutaFXML));
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            System.err.println("Error cargando la vista: " + rutaFXML);
            e.printStackTrace();
        }
    }
}
