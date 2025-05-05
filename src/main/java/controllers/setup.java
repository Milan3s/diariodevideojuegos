package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import config.Database; // <- Import correcto de tu clase modularizada
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class setup {

    @FXML
    private Label lblMensaje;
    @FXML
    private Button btnInstalar;
    @FXML
    private Button btnVerificar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnCerrar;

    private final Path basePath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos");
    private final Path dbPath = basePath.resolve("Config/Database/midiario.db");
    @FXML
    private VBox rootContainer;
    @FXML
    private Label lblTitulo;
    @FXML
    private GridPane botonera;
    @FXML
    private Label lblRuta;
    @FXML
    private Button btnIniciar;

    // Botón: INSTALAR
    @FXML
    private void handleInstalar() {
        try {
            Files.createDirectories(basePath.resolve("Config/Database"));
            Files.createDirectories(basePath.resolve("imagenes/juegos"));
            Files.createDirectories(basePath.resolve("imagenes/consola"));

            String url = "jdbc:sqlite:" + dbPath;

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(Database.getSqlSchema()); // <- Aquí usas el método externo
                    lblMensaje.setText("Base de datos y estructura creada correctamente.");
                } else {
                    lblMensaje.setText("Error: no se pudo crear la base de datos.");
                }
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al crear estructura o base de datos.");
            e.printStackTrace();
        }
    }

    // Botón: VERIFICAR
    @FXML
    private void handleVerificar() {
        StringBuilder resultado = new StringBuilder();

        resultado.append(Files.exists(basePath.resolve("Config/Database")) ? "✔ Config/Database\n" : "✘ Config/Database\n");
        resultado.append(Files.exists(basePath.resolve("imagenes/juegos")) ? "✔ imagenes/juegos\n" : "✘ imagenes/juegos\n");
        resultado.append(Files.exists(basePath.resolve("imagenes/consola")) ? "✔ imagenes/consola\n" : "✘ imagenes/consola\n");
        resultado.append(Files.exists(dbPath) ? "✔ Base de datos (midiario.db)\n" : "✘ Base de datos (midiario.db)\n");

        lblMensaje.setText(resultado.toString());
        lblRuta.setText("Ruta base: " + basePath.toString());
    }

    // Botón: BORRAR
    @FXML
    private void handleBorrar() {
        try {
            if (Files.exists(dbPath)) {
                Files.delete(dbPath);
                lblMensaje.setText("Base de datos midiario.db eliminada.");
            } else {
                lblMensaje.setText("No existe midiario.db.");
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al borrar base de datos.");
            e.printStackTrace();
        }
    }

    // Botón: CERRAR
    @FXML
    private void handleCerrar() {
        System.exit(0);
    }

    @FXML
    private void handleIniciar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Inicio - Diario de Videojuegos");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();

            // Cierra la ventana actual (setup)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            lblMensaje.setText("Aplicación iniciada correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("Error al iniciar la aplicación.");
            e.printStackTrace();
        }
    }

}
