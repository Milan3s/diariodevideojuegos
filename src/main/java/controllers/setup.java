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
    private final Path recursosPath = basePath.resolve("recursos");
    private final Path diarioPath = recursosPath.resolve("diario");
    private final Path votosPath = recursosPath.resolve("votos");

    private final Path imagenesJuegosDiarioPath = diarioPath.resolve("imagenes/juegos");
    private final Path imagenesConsolaDiarioPath = diarioPath.resolve("imagenes/consola");
    private final Path videosJuegosDiarioPath = diarioPath.resolve("videos/juegos");
    private final Path imagenesVotosPath = votosPath.resolve("imagenes");
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

    @FXML
    private void handleInstalar() {
        try {
            Files.createDirectories(dbPath.getParent());
            Files.createDirectories(imagenesJuegosDiarioPath);
            Files.createDirectories(imagenesConsolaDiarioPath);
            Files.createDirectories(videosJuegosDiarioPath);
            Files.createDirectories(imagenesVotosPath);

            String url = "jdbc:sqlite:" + dbPath.toString();

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

    @FXML
    private void handleVerificar() {
        StringBuilder resultado = new StringBuilder();

        resultado.append(verificarRuta(dbPath.toString(), "Base de datos (midiario.db)\n"));
        resultado.append(verificarRuta(imagenesJuegosDiarioPath.toString(), "diario/imagenes/juegos\n"));
        resultado.append(verificarRuta(imagenesConsolaDiarioPath.toString(), "diario/imagenes/consola\n"));
        resultado.append(verificarRuta(videosJuegosDiarioPath.toString(), "diario/videos/juegos\n"));
        resultado.append(verificarRuta(imagenesVotosPath.toString(), "votos/imagenes\n"));

        lblMensaje.setText(resultado.toString());
        lblRuta.setText("Ruta base: " + basePath.toString());
    }

    private String verificarRuta(String pathStr, String nombre) {
        String rutaLimpia = pathStr.startsWith("jdbc:sqlite:") ? pathStr.replace("jdbc:sqlite:", "") : pathStr;
        Path path = Paths.get(rutaLimpia);
        return (Files.exists(path) ? "✔ " : "✘ ") + nombre;
    }

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

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            lblMensaje.setText("Aplicación iniciada correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("Error al iniciar la aplicación.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInsertar() {
        DatabaseInsertar.insertarDatos();
        Alert alert = new Alert(AlertType.INFORMATION, "Los datos se han insertado correctamente.", ButtonType.OK);
        alert.showAndWait();
    }
}
