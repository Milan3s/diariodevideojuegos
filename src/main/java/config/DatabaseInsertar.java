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

                String sqlConfiguracionAuxiliares = "INSERT OR IGNORE INTO configuracion_auxiliares (nombre_visual, nombre_tabla, columna_id, columna_nombre) VALUES "
                        + "('Estados | Juego', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Logro', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Moderador', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Consola', 'estados', 'id_estado', 'nombre'), "
                        + "('Dificultades', 'dificultades_logros', 'id_dificultad', 'nombre'), "
                        + "('Años Metas', 'anios_metas_especificas', 'anio', 'anio');";

                String sqlAsignaciones = "INSERT INTO configuracion_auxiliares_asignadas (nombre_tabla, columna_id, id_valor, fecha_asignacion) VALUES "
                        + "('metas_twitch', 'id_meta', 1, '2025-05-01'), "
                        + "('metas_especificas', 'id_meta_especifica', 1, '2025-05-01'), "
                        + "('mejoras_canal', 'id_mejora', 1, '2025-05-01'), "
                        + "('eventos_extensibles', 'id_extensible', 1, '2025-05-01');";

                String sqlEstados = "INSERT INTO estados (tipo, nombre) VALUES "
                        + "('juego', 'Pendiente'), ('juego', 'Jugando'), ('juego', 'Completado'), ('juego', 'Abandonado'), "
                        + "('logro', 'Pendiente'), ('logro', 'Completado'), ('logro', 'Oculto'), "
                        + "('moderador', 'Activo'), ('moderador', 'Inactivo'), ('moderador', 'En revisión'), ('moderador', 'Dado de baja'), "
                        + "('consola', 'Con chip'), ('consola', 'Sin chip'), ('consola', 'Original'), ('consola', 'FPGA');";

                String sqlDificultades = "INSERT INTO dificultades_logros (nombre, tipo) VALUES "
                        + "('Fácil', 'logro'), ('Media', 'logro'), ('Difícil', 'logro'), ('Extrema', 'logro');";

                String sqlModeradores = "INSERT INTO moderadores (nombre, email, fecha_alta, id_estado) VALUES "
                        + "('Carlos Pérez', 'carlos.perez@example.com', '2025-01-01', (SELECT id_estado FROM estados WHERE nombre='Activo' AND tipo='moderador')), "
                        + "('Ana Gómez', 'ana.gomez@example.com', '2025-03-10', (SELECT id_estado FROM estados WHERE nombre='Inactivo' AND tipo='moderador'));";

                String sqlConsolas = "INSERT INTO consolas (nombre, abreviatura, anio, fabricante, generacion, region, tipo, "
                        + "procesador, memoria, almacenamiento, fecha_lanzamiento, imagen, id_estado, frecuencia_mhz, chip, caracteristicas) VALUES "
                        + "('PlayStation 2', 'PS2', 2000, 'Sony', 'Sexta', 'Global', 'Sobremesa', 'Emotion Engine', '32 MB RAM', 'DVD-ROM', '2000-03-04', 'tv-ps2.png', "
                        + "(SELECT id_estado FROM estados WHERE nombre='Con chip' AND tipo='consola'), 294.91, 1, 'La más vendida de la historia'), "
                        + "('Nintendo Switch', 'NS', 2017, 'Nintendo', 'Octava', 'Global', 'Híbrida', 'NVIDIA Tegra X1', '4 GB RAM', '32 GB NAND', '2017-03-03', 'tv-switch.png', "
                        + "(SELECT id_estado FROM estados WHERE nombre='Sin chip' AND tipo='consola'), 1020.0, 0, 'Portátil y de sobremesa a la vez');";

                String sqlJuegos = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen, video) VALUES "
                        + "('Final Fantasy VII', 'RPG épico con Cloud Strife', 'Square', 'Square', 'RPG', 'Un jugador', '1997-01-31', "
                        + "(SELECT id_estado FROM estados WHERE nombre='Completado' AND tipo='juego'), 1, 'final_fantasy_vii.jpg', 'final_fantasy_vii.mp4');";

                String sqlJuegosConsolas = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (1, 1);";

                String sqlVotos = "INSERT INTO votos (id_juego, jugabilidad, dificultad, diseno_niveles, graficos, sonido, controles, adiccion, rejugabilidad, "
                        + "valor_nostalgico, innovacion, diseno_visual, multijugador_local, comentarios, fecha_voto) VALUES "
                        + "(1, 9, 8, 7, 9, 8, 9, 10, 9, 10, 8, 9, 10, 'Una obra maestra del RPG clásico', '2025-05-01');";

                String sqlLogros = "INSERT INTO logros (nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin, intentos, creditos, puntuacion, fecha_registro, "
                        + "id_juego, id_estado, id_dificultad, id_consola) VALUES "
                        + "('Super Mario 100%', 'Consigue todas las lunas', 15, 2025, '2025-04-01', '2025-04-30', 3, 150, 10, '2025-04-30', 1, "
                        + "(SELECT id_estado FROM estados WHERE nombre='Completado' AND tipo='logro'), 3, 1);";

                String sqlEventos = "INSERT INTO eventos (titulo, descripcion, fecha_inicio, fecha_fin, hora_inicio, hora_fin, color, fondo, todo_el_dia, recurrente, tipo_evento) VALUES "
                        + "('Evento Aniversario', 'Extensible especial por aniversario', '2025-06-01', '2025-06-02', '18:00', '23:00', '#0000FF', '#FFFFFF', 0, 'Anual', 'Especial');";

                String sqlMetasTwitch = "INSERT INTO metas_twitch (descripcion, meta, actual, mes, anio, fecha_inicio, fecha_fin) VALUES "
                        + "('Meta subs mayo 2025', 50, 30, 'Mayo', 2025, '2025-05-01', '2025-05-31');";

                String sqlAniosTwitch = "INSERT INTO anios_metas_twitch (id_meta, anio, tipo) VALUES (1, 2025, 'seguidores');";

                String sqlMetasEspecificas = "INSERT INTO metas_especificas (descripcion, cumplida, juegos_objetivo, juegos_completados, fabricante, id_consola, fecha_inicio, fecha_fin) VALUES "
                        + "('Juegos Nintendo 2025', 0, 10, 3, 'Nintendo', 2, '2025-01-01', '2025-12-31');";

                String sqlAniosEspecificas = "INSERT INTO anios_metas_especificas (id_meta_especifica, anio, tipo) VALUES (1, 2025, 'juegos');";

                String sqlSeguidores = "INSERT INTO seguidores (cantidad, fecha_registro) VALUES (1000, '2025-05-01');";

                String sqlMejoras = "INSERT INTO mejoras_canal (descripcion, meta, actual, fecha_inicio, fecha_fin, cumplida) VALUES "
                        + "('Nuevo overlay animado', 1, 0, '2025-05-01', '2025-05-31', 0), "
                        + "('Rediseño de paneles de información', 1, 1, '2025-04-01', '2025-04-15', 1), "
                        + "('Automatización de agradecimientos', 1, 0, '2025-06-01', '2025-06-30', 0);";

                String sqlAniosMejoras = "INSERT INTO anios_mejoras_canal (id_mejora, anio, tipo) "
                        + "SELECT id_mejora, strftime('%Y', fecha_inicio), 'canal' FROM mejoras_canal "
                        + "WHERE id_mejora NOT IN (SELECT id_mejora FROM anios_mejoras_canal);";

                String sqlExtensibles = "INSERT INTO eventos_extensibles (motivo, fecha_evento) VALUES ('Evento benéfico', '2025-06-10');";

                stmt.execute(sqlConfiguracionAuxiliares);
                stmt.execute(sqlAsignaciones);
                stmt.execute(sqlEstados);
                stmt.execute(sqlDificultades);
                stmt.execute(sqlModeradores);
                stmt.execute(sqlConsolas);
                stmt.execute(sqlJuegos);
                stmt.execute(sqlJuegosConsolas);
                stmt.execute(sqlVotos);
                stmt.execute(sqlLogros);
                stmt.execute(sqlEventos);
                stmt.execute(sqlMetasTwitch);
                stmt.execute(sqlAniosTwitch);
                stmt.execute(sqlMetasEspecificas);
                stmt.execute(sqlAniosEspecificas);
                stmt.execute(sqlSeguidores);
                stmt.execute(sqlMejoras);
                stmt.execute(sqlAniosMejoras);
                stmt.execute(sqlExtensibles);

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
