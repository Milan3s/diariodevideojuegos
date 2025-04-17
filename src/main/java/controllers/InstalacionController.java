package controllers;

import config.Database;
import config.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de instalación
 */
public class InstalacionController implements Initializable {

    @FXML
    private Button btnCrearBD;
    @FXML
    private Button btnComprobarBD;
    @FXML
    private Button btnCerrar;
    @FXML
    private Label sms_servidor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sms_servidor.setText("");
    }

    @FXML
    private void accion_crearBD(ActionEvent event) {
        Connection conn = Database.connect();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();

                // Crear tablas
                String sqlConsolas = "CREATE TABLE IF NOT EXISTS consolas ("
                        + "id_consolas INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL);";

                String sqlEstados = "CREATE TABLE IF NOT EXISTS estados ("
                        + "id_estados INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL);";

                String sqlJuegos = "CREATE TABLE IF NOT EXISTS juegos ("
                        + "id_juegos INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL,"
                        + "anio INTEGER,"
                        + "id_estados INTEGER NOT NULL,"
                        + "id_consolas INTEGER NOT NULL,"
                        + "FOREIGN KEY (id_estados) REFERENCES estados(id_estados),"
                        + "FOREIGN KEY (id_consolas) REFERENCES consolas(id_consolas));";

                String sqlJuegosInfo = "CREATE TABLE IF NOT EXISTS juegos_informacion ("
                        + "id_juegos_informacion INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "id_juegos INTEGER NOT NULL,"
                        + "titulo TEXT,"
                        + "descripcion TEXT,"
                        + "fecha_inicio TEXT,"
                        + "fecha_fin TEXT,"
                        + "intentos INTEGER,"
                        + "creditos INTEGER,"
                        + "puntuacion INTEGER,"
                        + "id_consolas INTEGER,"
                        + "FOREIGN KEY (id_juegos) REFERENCES juegos(id_juegos),"
                        + "FOREIGN KEY (id_consolas) REFERENCES consolas(id_consolas));";

                String sqlLogros = "CREATE TABLE IF NOT EXISTS logros ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL,"
                        + "fecha_registro TEXT,"
                        + "id_juegos INTEGER NOT NULL,"
                        + "FOREIGN KEY (id_juegos) REFERENCES juegos(id_juegos));";

                String sqlModeradores = "CREATE TABLE IF NOT EXISTS moderadores ("
                        + "id_moderadores INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL);";

                String sqlModeradoresBaja = "CREATE TABLE IF NOT EXISTS moderadores_baja ("
                        + "id_moderadores_eliminados INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "id_moderadores INTEGER NOT NULL,"
                        + "nombre TEXT NOT NULL,"
                        + "fecha_eliminacion TEXT,"
                        + "FOREIGN KEY (id_moderadores) REFERENCES moderadores(id_moderadores));";

                String sqlSeguidores = "CREATE TABLE IF NOT EXISTS seguidores ("
                        + "id_seguidores INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL);";

                String sqlPlataformas = "CREATE TABLE IF NOT EXISTS plataformas ("
                        + "id_plataforma INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nombre TEXT NOT NULL);";

                String sqlEventos = "CREATE TABLE IF NOT EXISTS eventos ("
                        + "id_evento INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "titulo TEXT NOT NULL,"
                        + "descripcion TEXT,"
                        + "fecha_inicio TEXT NOT NULL,"
                        + "fecha_fin TEXT,"
                        + "hora TEXT,"
                        + "id_plataforma INTEGER NOT NULL,"
                        + "FOREIGN KEY (id_plataforma) REFERENCES plataformas(id_plataforma));";

                stmt.executeUpdate(sqlConsolas);
                stmt.executeUpdate(sqlEstados);
                stmt.executeUpdate(sqlJuegos);
                stmt.executeUpdate(sqlJuegosInfo);
                stmt.executeUpdate(sqlLogros);
                stmt.executeUpdate(sqlModeradores);
                stmt.executeUpdate(sqlModeradoresBaja);
                stmt.executeUpdate(sqlSeguidores);
                stmt.executeUpdate(sqlPlataformas);
                stmt.executeUpdate(sqlEventos);

                sms_servidor.setText("Base de datos creada correctamente ✅");
                Logger.success("Base de datos creada correctamente");

                stmt.close();
                conn.close();
            } catch (Exception e) {
                Logger.error("Error al crear la base de datos: " + e.getMessage());
                sms_servidor.setText("Error al crear las tablas ❌");
            }
        } else {
            sms_servidor.setText("No se pudo crear/conectar a la base de datos ❌");
        }
    }

    @FXML
    private void accion_comprobarBD(ActionEvent event) {
        Path dbPath = Database.getDatabasePath();
        if (Files.exists(dbPath)) {
            Logger.success("Base de datos encontrada: " + dbPath);
            sms_servidor.setText("Base de datos detectada correctamente ✅");
        } else {
            Logger.warn("No se encontró la base de datos en: " + dbPath);
            sms_servidor.setText("Base de datos no encontrada ❌");
        }
    }

    @FXML
    private void accion_cerrar(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void accion_sms_servidor(MouseEvent event) {
    }
} 