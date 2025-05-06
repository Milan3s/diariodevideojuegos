package dao;

import config.AppLogger;
import config.Conexion;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Consola;
import models.Estado;
import models.Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JuegoDAO {

    // Insertar un juego con su relación a consola, videos y overlays
    public boolean insertarJuego(Juego juego) {
        String sqlJuego = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlRelacion = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (?, ?)";
        String sqlVideo = "INSERT INTO videos (video, id_juego) VALUES (?, ?)";  // Se cambió video_url por video
        String sqlOverlay = "INSERT INTO overlays (overlay, id_juego) VALUES (?, ?)";  // Se cambió overlay_url por overlay

        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego, PreparedStatement.RETURN_GENERATED_KEYS)) {
                // Insertar juego
                stmt.setString(1, juego.getNombre());
                stmt.setString(2, juego.getDescripcion());
                stmt.setString(3, juego.getDesarrollador());
                stmt.setString(4, juego.getEditor());
                stmt.setString(5, juego.getGenero());
                stmt.setString(6, juego.getModoJuego());
                stmt.setString(7, juego.getFechaLanzamiento());
                if (juego.getEstado() != null) {
                    stmt.setInt(8, juego.getEstado().getId()); // Obtener el ID del estado
                } else {
                    stmt.setNull(8, java.sql.Types.INTEGER);
                }
                stmt.setBoolean(9, juego.isEsRecomendado());

                // Solo el nombre de la imagen, no la ruta completa
                String nombreImagen = juego.getImagen() != null ? new File(juego.getImagen()).getName() : null;
                stmt.setString(10, nombreImagen); // Guardar solo el nombre de la imagen

                int filas = stmt.executeUpdate(); // Ejecuta la inserción

                if (filas > 0) {
                    // Obtener el ID generado del juego
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idJuego = rs.getInt(1);

                            // Relación con consola
                            try (PreparedStatement relStmt = conn.prepareStatement(sqlRelacion)) {
                                if (juego.getConsola() != null) {
                                    relStmt.setInt(1, idJuego);
                                    relStmt.setInt(2, juego.getConsola().getId());  // Obtener el ID de la consola
                                    relStmt.executeUpdate();
                                }
                            }

                            // Insertar video del juego si existe
                            if (juego.getVideo() != null) {
                                try (PreparedStatement videoStmt = conn.prepareStatement(sqlVideo)) {
                                    videoStmt.setString(1, juego.getVideo()); // Ruta del video
                                    videoStmt.setInt(2, idJuego);  // Relacionar con el juego
                                    videoStmt.executeUpdate();
                                }
                            }

                            // Insertar overlay del juego si existe
                            if (juego.getOverlay() != null) {
                                try (PreparedStatement overlayStmt = conn.prepareStatement(sqlOverlay)) {
                                    overlayStmt.setString(1, juego.getOverlay()); // Ruta del overlay
                                    overlayStmt.setInt(2, idJuego);  // Relacionar con el juego
                                    overlayStmt.executeUpdate();
                                }
                            }

                        }
                    }

                    conn.commit(); // Si todo va bien, commit de la transacción
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback si algo falla
                AppLogger.severe("Error al insertar juego: " + e.getMessage());
            }

        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Obtener todos los juegos junto con su estado, consola, videos y overlays
    public ObservableList<Juego> obtenerTodos() {
        ObservableList<Juego> lista = FXCollections.observableArrayList();

        String sql = "SELECT j.id_juegos, j.nombre, j.descripcion, j.desarrollador, j.editor, "
                + "j.genero, j.modo_juego, j.fecha_lanzamiento, j.es_recomendado, j.imagen, "
                + "e.id_estado, e.nombre AS nombre_estado, "
                + "c.id_consola, c.nombre AS nombre_consola, c.abreviatura AS abreviatura_consola, "
                + "v.video, o.overlay, "  // Se cambió video_url y overlay_url por video y overlay
                + "CONCAT(j.nombre, ' (', c.abreviatura, ')') AS juego_consola "
                + "FROM juegos j "
                + "LEFT JOIN estados e ON j.id_estado = e.id_estado "
                + "LEFT JOIN juegos_consolas jc ON j.id_juegos = jc.id_juego "
                + "LEFT JOIN consolas c ON jc.id_consola = c.id_consola "
                + "LEFT JOIN videos v ON j.id_juegos = v.id_juego "
                + "LEFT JOIN overlays o ON j.id_juegos = o.id_juego "
                + "ORDER BY j.nombre ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Juego juego = new Juego();
                juego.setId(rs.getInt("id_juegos"));
                juego.setNombre(rs.getString("nombre"));
                juego.setDescripcion(rs.getString("descripcion"));
                juego.setDesarrollador(rs.getString("desarrollador"));
                juego.setEditor(rs.getString("editor"));
                juego.setGenero(rs.getString("genero"));
                juego.setModoJuego(rs.getString("modo_juego"));
                juego.setFechaLanzamiento(rs.getString("fecha_lanzamiento"));
                juego.setEsRecomendado(rs.getBoolean("es_recomendado"));
                juego.setImagen(rs.getString("imagen")); // Solo el nombre de la imagen

                // Relacionados con Estado y Consola
                Estado estado = new Estado(rs.getInt("id_estado"), rs.getString("nombre_estado"));
                Consola consola = new Consola(
                        rs.getInt("id_consola"),
                        rs.getString("nombre_consola"),
                        rs.getString("abreviatura_consola")
                );

                juego.setEstado(estado);
                juego.setConsola(consola);
                juego.setNombreConsola(rs.getString("juego_consola"));

                // Asignamos los datos de video y overlay
                juego.setVideo(rs.getString("video"));
                juego.setOverlay(rs.getString("overlay"));

                lista.add(juego);
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener juegos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    // Eliminar un juego y sus videos y overlays relacionados
    public boolean eliminarJuego(int idJuego) {
        String sqlJuego = "SELECT imagen FROM juegos WHERE id_juegos = ?";
        String sqlEliminarJuego = "DELETE FROM juegos WHERE id_juegos = ?";
        String sqlEliminarRelacionConsola = "DELETE FROM juegos_consolas WHERE id_juego = ?";
        String sqlEliminarVideos = "DELETE FROM videos WHERE id_juego = ?";  // Eliminar video relacionado
        String sqlEliminarOverlays = "DELETE FROM overlays WHERE id_juego = ?";  // Eliminar overlay relacionado

        try (Connection conn = Conexion.obtenerConexion()) {
            // Iniciar transacción
            conn.setAutoCommit(false);

            // Primero, obtenemos la imagen asociada al juego
            String imagenJuego = null;
            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego)) {
                stmt.setInt(1, idJuego);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        imagenJuego = rs.getString("imagen");
                    }
                }
            }

            // Eliminar las relaciones con las consolas
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarRelacionConsola)) {
                stmt.setInt(1, idJuego);
                stmt.executeUpdate();  // Eliminar la relación existente
            }

            // Eliminar videos relacionados
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarVideos)) {
                stmt.setInt(1, idJuego);
                stmt.executeUpdate();  // Eliminar el video asociado
            }

            // Eliminar overlays relacionados
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarOverlays)) {
                stmt.setInt(1, idJuego);
                stmt.executeUpdate();  // Eliminar el overlay asociado
            }

            // Eliminar el juego
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarJuego)) {
                stmt.setInt(1, idJuego);
                int filas = stmt.executeUpdate();

                // Si el juego se eliminó correctamente, también eliminamos la imagen
                if (filas > 0 && imagenJuego != null && !imagenJuego.isEmpty()) {
                    // Eliminar la imagen del disco
                    File archivoImagen = new File(Conexion.imagenesPath, imagenJuego);
                    if (archivoImagen.exists()) {
                        if (archivoImagen.delete()) {
                            AppLogger.info("Imagen eliminada correctamente: " + archivoImagen.getAbsolutePath());
                        } else {
                            AppLogger.warning("No se pudo eliminar la imagen: " + archivoImagen.getAbsolutePath());
                        }
                    }
                }

                conn.commit();  // Commit de la transacción
                return true;  // Juego, video, overlay y su relación eliminados correctamente
            } catch (SQLException e) {
                conn.rollback();  // Rollback si algo falla
                AppLogger.severe("Error al eliminar el juego: " + e.getMessage());
            }
        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return false;  // Si no se pudo eliminar el juego, video o overlay
    }

    // Método para actualizar un juego con video y overlay
    public boolean actualizarJuego(Juego juego) {
        String sqlJuego = "UPDATE juegos SET nombre = ?, descripcion = ?, desarrollador = ?, editor = ?, genero = ?, modo_juego = ?, fecha_lanzamiento = ?, id_estado = ?, es_recomendado = ?, imagen = ? WHERE id_juegos = ?";
        String sqlEliminarRelacionConsola = "DELETE FROM juegos_consolas WHERE id_juego = ?";  // Eliminar la relación actual
        String sqlRelacionConsola = "INSERT INTO juegos_consolas (id_juego, id_consola) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM juegos_consolas WHERE id_juego = ? AND id_consola = ?)";  // Evitar duplicados
        String sqlEliminarVideos = "DELETE FROM videos WHERE id_juego = ?";  // Eliminar videos antiguos
        String sqlEliminarOverlays = "DELETE FROM overlays WHERE id_juego = ?";  // Eliminar overlays antiguos

        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego)) {
                // Actualizar los campos del juego
                stmt.setString(1, juego.getNombre());
                stmt.setString(2, juego.getDescripcion());
                stmt.setString(3, juego.getDesarrollador());
                stmt.setString(4, juego.getEditor());
                stmt.setString(5, juego.getGenero());
                stmt.setString(6, juego.getModoJuego());
                stmt.setString(7, juego.getFechaLanzamiento());
                if (juego.getEstado() != null) {
                    stmt.setInt(8, juego.getEstado().getId()); // Obtener el ID del estado
                } else {
                    stmt.setNull(8, java.sql.Types.INTEGER);
                }
                stmt.setBoolean(9, juego.isEsRecomendado());

                // Solo el nombre de la imagen, no la ruta completa
                String nombreImagen = juego.getImagen() != null ? new File(juego.getImagen()).getName() : null;
                stmt.setString(10, nombreImagen); // Guardar solo el nombre de la imagen

                stmt.setInt(11, juego.getId());  // ID del juego a actualizar

                int filas = stmt.executeUpdate(); // Ejecuta la actualización

                if (filas > 0) {
                    // Eliminar las relaciones anteriores con las consolas
                    try (PreparedStatement stmtEliminarRelacion = conn.prepareStatement(sqlEliminarRelacionConsola)) {
                        stmtEliminarRelacion.setInt(1, juego.getId());
                        stmtEliminarRelacion.executeUpdate();  // Eliminar la relación existente
                    }

                    // Insertar la nueva relación consola-juego solo si no existe
                    try (PreparedStatement stmtRelacion = conn.prepareStatement(sqlRelacionConsola)) {
                        if (juego.getConsola() != null) {
                            stmtRelacion.setInt(1, juego.getId());
                            stmtRelacion.setInt(2, juego.getConsola().getId());  // Obtener el ID de la consola
                            stmtRelacion.setInt(3, juego.getId());  // Compara con el ID del juego
                            stmtRelacion.setInt(4, juego.getConsola().getId());  // Compara con el ID de la consola
                            stmtRelacion.executeUpdate();
                        }
                    }

                    // Eliminar videos antiguos y agregar el nuevo video
                    try (PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminarVideos)) {
                        stmtEliminar.setInt(1, juego.getId());
                        stmtEliminar.executeUpdate();  // Eliminar los videos anteriores
                    }
                    if (juego.getVideo() != null) {
                        try (PreparedStatement stmtVideo = conn.prepareStatement("INSERT INTO videos (video, id_juego) VALUES (?, ?)")) {
                            stmtVideo.setString(1, juego.getVideo());
                            stmtVideo.setInt(2, juego.getId());
                            stmtVideo.executeUpdate();
                        }
                    }

                    // Eliminar overlays antiguos y agregar el nuevo overlay
                    try (PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminarOverlays)) {
                        stmtEliminar.setInt(1, juego.getId());
                        stmtEliminar.executeUpdate();  // Eliminar los overlays anteriores
                    }
                    if (juego.getOverlay() != null) {
                        try (PreparedStatement stmtOverlay = conn.prepareStatement("INSERT INTO overlays (overlay, id_juego) VALUES (?, ?)")) {
                            stmtOverlay.setString(1, juego.getOverlay());
                            stmtOverlay.setInt(2, juego.getId());
                            stmtOverlay.executeUpdate();
                        }
                    }

                    conn.commit();  // Commit de la transacción
                    return true;  // La actualización fue exitosa
                }

            } catch (SQLException e) {
                conn.rollback(); // Rollback si algo falla
                AppLogger.severe("Error al actualizar juego: " + e.getMessage());
            }

        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
