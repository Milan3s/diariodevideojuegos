package config;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DatabaseInsertar {

    public static void insertarDatos() {
        try (Connection conn = Conexion.obtenerConexion()) {
            if (conn != null) {
                String sqlEstados = "INSERT INTO estados (tipo, nombre) VALUES "
                        + "('juego', 'Pendiente'), "
                        + "('juego', 'Jugando'), "
                        + "('juego', 'Completado'), "
                        + "('juego', 'Abandonado'), "
                        + "('logro', 'Pendiente'), "
                        + "('logro', 'Completado'), "
                        + "('logro', 'Oculto'), "
                        + "('moderador', 'Activo'), "
                        + "('moderador', 'Inactivo'), "
                        + "('moderador', 'En revisión'), "
                        + "('moderador', 'Dado de baja'), "
                        + "('consola', 'Con chip'), "
                        + "('consola', 'Sin chip'), "
                        + "('consola', 'Original');";

                String sqlDificultades = "INSERT INTO dificultades_logros (nombre) VALUES "
                        + "('Fácil'), ('Media'), ('Difícil'), ('Extrema');";

                String sqlConsolas = "INSERT INTO consolas (nombre, abreviatura, anio, fabricante, generacion, region, tipo, "
                        + "procesador, memoria, almacenamiento, fecha_lanzamiento, imagen, id_estado, frecuencia_mhz, chip, caracteristicas) VALUES "
                        + "('PlayStation 2', 'PS2', 2000, 'Sony', 'Sexta', 'Global', 'Sobremesa', 'Emotion Engine', '32 MB RAM', 'DVD-ROM', '2000-03-04', 'tv-ps2.png', 12, 294.91, 1, 'La más vendida de la historia'), "
                        + "('Nintendo Switch', 'NS', 2017, 'Nintendo', 'Octava', 'Global', 'Híbrida', 'NVIDIA Tegra X1', '4 GB RAM', '32 GB NAND', '2017-03-03', 'tv-switch.png', 12, 1020.0, 0, 'Portátil y de sobremesa a la vez');";

                String sqlJuegos = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen, video) VALUES "
                        + "('Final Fantasy VII', 'RPG épico con Cloud Strife', 'Square', 'Square', 'RPG', 'Un jugador', '1997-01-31', 3, 1, 'final_fantasy_vii.jpg', 'final_fantasy_vii.mp4'), "
                        + "('Super Mario Odyssey', 'Aventura 3D con Cappy', 'Nintendo', 'Nintendo', 'Plataformas', 'Un jugador', '2017-10-27', 3, 1, 'super_mario_odyssey.jpg', 'super_mario_odyssey.mp4');";

                String sqlJuegosConsolas = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (1, 1), (2, 2);";

                String sqlVotos = "INSERT INTO votos (id_juego, voto, criterio, fecha_voto) VALUES "
                        + "(1, 10, 'Historia', '2025-05-01'), (2, 9, 'Diversión', '2025-05-02');";

                String sqlLogros = "INSERT INTO logros (nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin, intentos, creditos, puntuacion, fecha_registro, id_juego, id_estado, id_dificultad, id_consola) VALUES "
                        + "('Super Mario 100%', 'Consigue todas las lunas', 15, 2025, '2025-04-01', '2025-04-30', 3, 150, 10, '2025-04-30', 2, 3, 3, 2);";

                String sqlEventos = "INSERT INTO eventos (titulo, descripcion, fecha_inicio, fecha_fin, hora_inicio, hora_fin, color, fondo, todo_el_dia, recurrente, tipo_evento) VALUES "
                        + "('Evento Aniversario', 'Extensible especial por aniversario', '2025-06-01', '2025-06-02', '18:00', '23:00', '#0000FF', '#FFFFFF', 0, 'Anual', 'Especial');";

                String sqlMetas = "INSERT INTO metas_twitch (descripcion, meta, actual, mes, anio, fecha_inicio, fecha_fin, fecha_registro) VALUES "
                        + "('Meta: 100 Seguidores', 100, 60, 5, 2025, '2025-05-01', '2025-05-31', CURRENT_TIMESTAMP), "
                        + "('Meta: 50 Juegos Completados', 50, 50, 5, 2025, '2025-05-01', '2025-05-31', CURRENT_TIMESTAMP), "
                        + "('Mejoras del canal', 0, 0, 5, 2025, '2025-05-01', '2025-05-01', CURRENT_TIMESTAMP), "
                        + "('Próximo extensible por aniversario del canal', 0, 0, 6, 2025, '2025-06-01', '2025-06-01', CURRENT_TIMESTAMP);";

                String sqlSeguidores = "INSERT INTO seguidores (cantidad) VALUES (350);";

                conn.createStatement().execute(sqlEstados);
                conn.createStatement().execute(sqlDificultades);
                conn.createStatement().execute(sqlConsolas);
                conn.createStatement().execute(sqlJuegos);
                conn.createStatement().execute(sqlJuegosConsolas);
                conn.createStatement().execute(sqlVotos);
                conn.createStatement().execute(sqlLogros);
                conn.createStatement().execute(sqlEventos);
                conn.createStatement().execute(sqlMetas);
                conn.createStatement().execute(sqlSeguidores);

                Alert alert = new Alert(AlertType.INFORMATION, "Datos insertados correctamente.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, "Error al insertar los datos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
