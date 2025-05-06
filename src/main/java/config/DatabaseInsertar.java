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
                // =========================
                // INSERTAR ESTADOS UNIFICADOS
                // =========================
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
                        + "('moderador', 'Dado de baja');";

                // =========================
                // DIFICULTADES DE LOGROS
                // =========================
                String sqlDificultades = "INSERT INTO dificultades_logros (nombre) VALUES "
                        + "('Fácil'), "
                        + "('Media'), "
                        + "('Difícil'), "
                        + "('Extrema');";

                // =========================
                // CONSOLAS
                // =========================
                String sqlConsolas = "INSERT INTO consolas (nombre, abreviatura, anio, fabricante, generacion, region, tipo, procesador, memoria, almacenamiento, fecha_lanzamiento, imagen, id_estado) VALUES "
                        + "('Atari 2600', 'AT2600', 1977, 'Atari', 'Primera', 'Global', 'Sobremesa', 'Custom MOS 6507', '128 bytes RAM', 'Cartridge', '1977-09-11', 'tv-atari.png', 1), "
                        + "('Neo Geo', 'NG', 1990, 'SNK', 'Cuarta', 'Global', 'Arcade/Sobremesa', 'Motorola 68000', '64 KB RAM', 'Cartridge', '1990-04-26', 'tv-neo-geo.png', 1), "
                        + "('Super Nintendo Entertainment System', 'SNES', 1990, 'Nintendo', 'Cuarta', 'Global', 'Sobremesa', 'Ricoh 5A22', '128 KB RAM', 'Cartridge', '1990-08-23', 'tv-snes.png', 1), "
                        + "('Sega Saturn', 'SS', 1994, 'Sega', 'Quinta', 'Global', 'Sobremesa', 'SH-2', '2 MB RAM', 'CD-ROM', '1994-11-22', 'tv-saturn.png', 1), "
                        + "('Nintendo 64', 'N64', 1996, 'Nintendo', 'Sexta', 'Global', 'Sobremesa', 'MIPS R4300i', '4 MB RAM', 'Cartridge', '1996-09-29', 'tv-n64.png', 1), "
                        + "('PlayStation 2', 'PS2', 2000, 'Sony', 'Sexta', 'Global', 'Sobremesa', 'Emotion Engine', '32 MB RAM', 'DVD-ROM', '2000-03-04', 'tv-ps2.png', 1), "
                        + "('Nintendo Switch', 'NS', 2017, 'Nintendo', 'Octava', 'Global', 'Híbrida', 'NVIDIA Tegra X1', '4 GB RAM', '32 GB NAND', '2017-03-03', 'tv-switch.png', 1);";

                // =========================
                // JUEGOS
                // =========================
                String sqlJuegos = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen, video) VALUES "
                        + "('The Legend of Zelda: Ocarina of Time', 'Aventura épica en Hyrule', 'Nintendo EAD', 'Nintendo', 'Aventura', 'Un jugador', '1998-11-21', 3, 1, 'zelda_ocarina_of_time.jpg', 'zelda_ocarina_of_time.mp4'), "
                        + "('Shadow of the Colossus', 'Derrota colosos en un mundo desolado', 'Team Ico', 'Sony', 'Acción/Aventura', 'Un jugador', '2005-10-18', 2, 0, 'shadow_of_the_colossus.jpg', 'shadow_of_the_colossus.mp4'), "
                        + "('Super Mario World', 'Plataformas clásico de SNES', 'Nintendo', 'Nintendo', 'Plataformas', 'Un jugador', '1990-11-21', 3, 0, 'super_mario_world.jpg', 'super_mario_world.mp4'), "
                        + "('Virtua Fighter 2', 'Lucha 3D técnica', 'Sega AM2', 'Sega', 'Lucha', 'Multijugador', '1994-11-01', 3, 0, 'virtua_fighter_2.jpg', 'virtua_fighter_2.mp4'), "
                        + "('Final Fantasy VII', 'RPG épico con Cloud Strife', 'Square', 'Square', 'RPG', 'Un jugador', '1997-01-31', 3, 1, 'final_fantasy_vii.jpg', 'final_fantasy_vii.mp4'), "
                        + "('Super Mario Odyssey', 'Aventura 3D con Cappy', 'Nintendo', 'Nintendo', 'Plataformas', 'Un jugador', '2017-10-27', 3, 1, 'super_mario_odyssey.jpg', 'super_mario_odyssey.mp4'), "
                        + "('Chrono Trigger', 'Viajes en el tiempo en un RPG clásico', 'Square', 'Square', 'RPG', 'Un jugador', '1995-03-11', 3, 0, 'chrono_trigger.jpg', 'chrono_trigger.mp4');";

                // =========================
                // RELACIÓN JUEGOS - CONSOLAS
                // =========================
                String sqlJuegosConsolas = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES "
                        + "(1, 5), (2, 6), (3, 3), (4, 4), (5, 4), (6, 7), (7, 3);";

                // =========================
                // VOTOS
                // =========================
                String sqlVotos = "INSERT INTO votos (id_juego, voto, criterio, fecha_voto) VALUES "
                        + "(1, 9, 'Jugabilidad', '2025-04-30'), "
                        + "(2, 6, 'Historia', '2025-04-30'), "
                        + "(3, 10, 'Nostalgia', '2025-04-30');";

                // =========================
                // LOGROS
                // =========================
                String sqlLogros = "INSERT INTO logros (nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin, intentos, creditos, puntuacion, fecha_registro, id_juego, id_estado, id_dificultad, id_consola) VALUES "
                        + "('Logro 1', 'Completa el juego en modo difícil', 10, 2025, '2025-05-01', '2025-06-01', 3, 100, 10, '2025-04-30', 1, 3, 2, 5), "
                        + "('Logro 2', 'Derrota a los jefes en modo normal', 5, 2025, '2025-05-01', '2025-06-01', 2, 50, 8, '2025-04-30', 2, 2, 3, 6);";

                // =========================
                // EVENTOS
                // =========================
                String sqlEventos = "INSERT INTO eventos (titulo, descripcion, fecha_inicio, fecha_fin, hora_inicio, hora_fin, color, fondo, todo_el_dia, recurrente, tipo_evento) VALUES "
                        + "('Evento 1', 'Evento para celebrar el lanzamiento de Zelda', '2025-05-01', '2025-05-02', '10:00', '18:00', '#FF5733', '#C70039', 0, 'Mensual', 'Lanzamiento'), "
                        + "('Evento 2', 'Evento retro de juegos clásicos', '2025-05-10', '2025-05-10', '09:00', '17:00', '#DAF7A6', '#FFC300', 0, 'Anual', 'Retro');";

                // =========================
                // METAS TWITCH
                // =========================
                String sqlMetasTwitch = "INSERT INTO metas_twitch (descripcion, meta, fecha_inicio, fecha_fin) VALUES "
                        + "('Meta de seguidores en Twitch', 1000, '2025-05-01', '2025-06-01'), "
                        + "('Meta de visualizaciones en Twitch', 5000, '2025-05-01', '2025-06-01');";

                // =========================
                // SEGUIDORES
                // =========================
                String sqlSeguidores = "INSERT INTO seguidores (cantidad) VALUES "
                        + "(100), (250);";

                // Ejecutar todas las consultas
                conn.createStatement().execute(sqlEstados);
                conn.createStatement().execute(sqlDificultades);
                conn.createStatement().execute(sqlConsolas);
                conn.createStatement().execute(sqlJuegos);
                conn.createStatement().execute(sqlJuegosConsolas);
                conn.createStatement().execute(sqlVotos);
                conn.createStatement().execute(sqlLogros);
                conn.createStatement().execute(sqlEventos);
                conn.createStatement().execute(sqlMetasTwitch);
                conn.createStatement().execute(sqlSeguidores);

                // Mostrar alerta de éxito
                Alert alert = new Alert(AlertType.INFORMATION, "Datos insertados correctamente.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Mostrar error si ocurre
            Alert alert = new Alert(AlertType.ERROR, "Error al insertar los datos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
