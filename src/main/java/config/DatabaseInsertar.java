package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DatabaseInsertar {

    public static void insertarDatos() {
        try (Connection conn = Conexion.obtenerConexion()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                String sqlConfiguracionAuxiliares
                        = "INSERT OR IGNORE INTO configuracion_auxiliares (nombre_visual, nombre_tabla, columna_id, columna_nombre) VALUES "
                        + "('Estados | Juego', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Logro', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Moderador', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Consola', 'estados', 'id_estado', 'nombre'), "
                        + "('Dificultades', 'dificultades_logros', 'id_dificultad', 'nombre'), "
                        + "('Años - Metas Twitch', 'anios_metas_twitch', 'anio', 'anio'), "
                        + "('Años - Mejoras Canal', 'anios_mejoras_canal', 'anio', 'anio'), "
                        + "('Años - Metas Específicas', 'anios_metas_especificas', 'anio', 'anio'), "
                        + "('Cumplida | Mejora', 'estado_cumplida', 'id_estado_cumplida', 'nombre');";

                String sqlEstados
                        = "INSERT INTO estados (tipo, nombre) VALUES "
                        + "('juego', 'Pendiente'), ('juego', 'Jugando'), ('juego', 'Completado'), ('juego', 'Abandonado'), ('juego', 'Ponzoña'), "
                        + "('logro', 'Pendiente'), ('logro', 'Completado'), ('logro', 'Oculto'), "
                        + "('moderador', 'Activo'), ('moderador', 'Inactivo'), ('moderador', 'En revisión'), ('moderador', 'Dado de baja'), "
                        + "('consola', 'Con chip'), ('consola', 'Sin chip'), ('consola', 'Original'), ('consola', 'FPGA');";

                String sqlDificultades
                        = "INSERT INTO dificultades_logros (nombre, tipo) VALUES "
                        + "('Fácil', 'logro'), ('Media', 'logro'), ('Difícil', 'logro'), ('Extrema', 'logro');";

                String sqlCumplida
                        = "INSERT OR IGNORE INTO estado_cumplida (nombre) VALUES ('Sí'), ('No');";

                stmt.execute(sqlConfiguracionAuxiliares);
                stmt.execute(sqlEstados);
                stmt.execute(sqlDificultades);
                stmt.execute(sqlCumplida);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Datos insertados correctamente.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al insertar los datos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
