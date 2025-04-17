package controllers;

import config.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

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
    private Button btnAjustes;
    @FXML
    private Button btnLogros;
    @FXML
    private Button btnCerrar;
    @FXML
    private Button btnEventos;

    @FXML
    private void accion_inicio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_inicio.fxml"));
            Parent contenido = loader.load();
            PrincipalController.getInstance().setContenido(contenido);
            Logger.info("Vista 'contenido_inicio.fxml' cargada correctamente");

        } catch (IOException e) {
            Logger.error("Error al cargar 'contenido_inicio.fxml': " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void accion_eventos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_eventos.fxml"));
            Parent contenido = loader.load();
            PrincipalController.getInstance().setContenido(contenido);
            Logger.info("Vista 'contenido_eventos.fxml' cargada correctamente");

        } catch (IOException e) {
            Logger.error("Error al cargar 'contenido_eventos.fxml': " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accion_juegos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_juegos.fxml"));
            Parent contenido = loader.load();
            PrincipalController.getInstance().setContenido(contenido);
            Logger.info("Vista 'contenido_juegos.fxml' cargada correctamente");

        } catch (IOException e) {
            Logger.error("Error al cargar 'contenido_juegos.fxml': " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accion_consolas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_consolas.fxml"));
            Parent contenido = loader.load();
            PrincipalController.getInstance().setContenido(contenido);
            Logger.info("Vista 'contenido_consolas.fxml' cargada correctamente");

        } catch (IOException e) {
            Logger.error("Error al cargar 'contenido_consolas.fxml': " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accion_moderadores(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_moderadores.fxml"));
            Parent contenido = loader.load();
            PrincipalController.getInstance().setContenido(contenido);
            Logger.info("Vista 'contenido_moderadores.fxml' cargada correctamente");

        } catch (IOException e) {
            Logger.error("Error al cargar 'contenido_moderadores.fxml': " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void accion_logros(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/contenido_logros.fxml"));
            Parent contenido = loader.load();
            PrincipalController.getInstance().setContenido(contenido);
            Logger.info("Vista 'contenido_logros.fxml' cargada correctamente");

        } catch (IOException e) {
            Logger.error("Error al cargar 'contenido_logros.fxml': " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void accion_cerrar(ActionEvent event) {
    }

    public void marcarBotonActivo(Button botonActivo) {
        // Reset estilos
        btnIInicio.setStyle("");
        btnJuegos.setStyle("");
        btnConsolas.setStyle("");
        btnModeradores.setStyle("");
        btnAjustes.setStyle("");
        btnLogros.setStyle("");

        // Estilo botón activo
        botonActivo.setStyle("-fx-background-color: #cce5ff;");
        Logger.info("Botón activo: " + botonActivo.getText());
    }

    

    
}
