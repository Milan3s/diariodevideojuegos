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

import config.Database;
import config.Conexion;
import config.DatabaseInsertar;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
    @FXML
    private Button btnInsertar;

    // Botón: INSTALAR
    @FXML
    private void handleInstalar() {
        try {
            Files.createDirectories(basePath.resolve("Config/Database"));
            Files.createDirectories(basePath.resolve("imagenes/juegos"));
            Files.createDirectories(basePath.resolve("imagenes/consola"));
            Files.createDirectories(basePath.resolve("videos/juegos"));
            // overlays eliminado

            String url = "jdbc:sqlite:" + dbPath;

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(Database.getSqlSchema());
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
        resultado.append(Files.exists(basePath.resolve("videos/juegos")) ? "✔ videos/juegos\n" : "✘ videos/juegos\n");
        // overlays eliminado
        resultado.append(Files.exists(dbPath) ? "✔ Base de datos (midiario.db)\n" : "✘ Base de datos (midiario.db)\n");

        lblMensaje.setText(resultado.toString());
        lblRuta.setText("Ruta base: " + basePath.toString());
    }

    // Botón: BORRAR
    @FXML
    private void handleBorrar() {
        try {
            Conexion.borrarDirectorioPrincipal();
            lblMensaje.setText("Directorio 'diariodevideojuegos' y su contenido han sido eliminados.");
        } catch (Exception e) {
            lblMensaje.setText("Error al borrar el directorio.");
            e.printStackTrace();
        }
    }

    // Botón: CERRAR
    @FXML
    private void handleCerrar() {
        System.exit(0);
    }

    // Iniciar la aplicación
    @FXML
    private void handleIniciar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Inicio - Diario de Videojuegos");

            // Establecer resolución fija 1920x1080
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setResizable(true); // Opcional: evita que el usuario cambie el tamaño
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            lblMensaje.setText("Aplicación iniciada correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("Error al iniciar la aplicación.");
            e.printStackTrace();
        }
    }

    // Botón: INSERTAR DATOS
    @FXML
    private void handleInsertar() {
        DatabaseInsertar.insertarDatos();
        Alert alert = new Alert(AlertType.INFORMATION, "Los datos se han insertado correctamente.", ButtonType.OK);
        alert.showAndWait();
    }
}
