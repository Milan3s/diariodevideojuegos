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
        String sqlJuego = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen, video) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlRelacion = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (?, ?)";

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

                // Insertar video del juego si existe
                stmt.setString(11, juego.getVideo()); // Insertar el nombre del video directamente en la tabla juegos

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

    // Obtener todos los juegos junto con su estado, consola y video
    public ObservableList<Juego> obtenerTodos() {
        ObservableList<Juego> lista = FXCollections.observableArrayList();

        String sql = "SELECT j.id_juegos, j.nombre, j.descripcion, j.desarrollador, j.editor, "
                + "j.genero, j.modo_juego, j.fecha_lanzamiento, j.es_recomendado, j.imagen, j.video, "
                + "e.id_estado, e.nombre AS nombre_estado, "
                + "c.id_consola, c.nombre AS nombre_consola, c.abreviatura AS abreviatura_consola, "
                + "CONCAT(j.nombre, ' (', c.abreviatura, ')') AS juego_consola "
                + "FROM juegos j "
                + "LEFT JOIN estados e ON j.id_estado = e.id_estado "
                + "LEFT JOIN juegos_consolas jc ON j.id_juegos = jc.id_juego "
                + "LEFT JOIN consolas c ON jc.id_consola = c.id_consola "
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
                juego.setVideo(rs.getString("video")); // Obtener el video directamente desde la tabla juegos

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

                lista.add(juego);
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener juegos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    // Eliminar un juego y sus relaciones con consola y video
    public boolean eliminarJuego(int idJuego) {
        String sqlJuego = "SELECT imagen, video FROM juegos WHERE id_juegos = ?";
        String sqlEliminarJuego = "DELETE FROM juegos WHERE id_juegos = ?";
        String sqlEliminarRelacionConsola = "DELETE FROM juegos_consolas WHERE id_juego = ?";  // Eliminar la relación actual

        try (Connection conn = Conexion.obtenerConexion()) {
            // Iniciar transacción
            conn.setAutoCommit(false);

            // Primero, obtenemos la imagen y el video asociados al juego
            String imagenJuego = null;
            String videoJuego = null;
            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego)) {
                stmt.setInt(1, idJuego);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        imagenJuego = rs.getString("imagen");
                        videoJuego = rs.getString("video");
                    }
                }
            }

            // Eliminar las relaciones con las consolas
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarRelacionConsola)) {
                stmt.setInt(1, idJuego);
                stmt.executeUpdate();  // Eliminar la relación existente
            }

            // Eliminar el juego
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarJuego)) {
                stmt.setInt(1, idJuego);
                int filas = stmt.executeUpdate();

                // Si el juego se eliminó correctamente, también eliminamos la imagen y el video
                if (filas > 0) {
                    if (imagenJuego != null && !imagenJuego.isEmpty()) {
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

                    if (videoJuego != null && !videoJuego.isEmpty()) {
                        // Eliminar el video del disco
                        File archivoVideo = new File(Conexion.videosPath, videoJuego);
                        if (archivoVideo.exists()) {
                            if (archivoVideo.delete()) {
                                AppLogger.info("Video eliminado correctamente: " + archivoVideo.getAbsolutePath());
                            } else {
                                AppLogger.warning("No se pudo eliminar el video: " + archivoVideo.getAbsolutePath());
                            }
                        }
                    }

                    conn.commit();  // Commit de la transacción
                    return true;  // Juego, video y su relación eliminados correctamente
                }
            } catch (SQLException e) {
                conn.rollback();  // Rollback si algo falla
                AppLogger.severe("Error al eliminar el juego: " + e.getMessage());
            }
        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return false;  // Si no se pudo eliminar el juego
    }

    // Método para actualizar un juego con video
    public boolean actualizarJuego(Juego juego) {
        String sqlJuego = "UPDATE juegos SET nombre = ?, descripcion = ?, desarrollador = ?, editor = ?, genero = ?, modo_juego = ?, fecha_lanzamiento = ?, id_estado = ?, es_recomendado = ?, imagen = ?, video = ? WHERE id_juegos = ?";
        String sqlEliminarRelacionConsola = "DELETE FROM juegos_consolas WHERE id_juego = ?";  // Eliminar la relación actual
        String sqlRelacionConsola = "INSERT INTO juegos_consolas (id_juego, id_consola) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM juegos_consolas WHERE id_juego = ? AND id_consola = ?)";  // Evitar duplicados

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

                // Actualizar video
                stmt.setString(11, juego.getVideo());  // Insertar el nombre del video directamente en la tabla juegos

                stmt.setInt(12, juego.getId());  // ID del juego a actualizar

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
