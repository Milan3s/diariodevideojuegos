package dao;

import config.AppLogger;
import config.Conexion;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Consola;
import models.Estado;
import models.Juego;

public class JuegoDAO {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_SQLITE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public boolean insertarJuego(Juego juego) {
        String sqlJuego = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen, video) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlRelacion = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (?, ?)";

        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, juego.getNombre());
                stmt.setString(2, juego.getDescripcion());
                stmt.setString(3, juego.getDesarrollador());
                stmt.setString(4, juego.getEditor());
                stmt.setString(5, juego.getGenero());
                stmt.setString(6, juego.getModoJuego());

                try {
                    LocalDate fecha = LocalDate.parse(juego.getFechaLanzamiento(), FORMATO_FECHA);
                    stmt.setString(7, fecha.format(FORMATO_SQLITE));
                } catch (DateTimeParseException e) {
                    stmt.setNull(7, java.sql.Types.VARCHAR);
                }

                stmt.setObject(8, juego.getEstado() != null ? juego.getEstado().getId() : null, java.sql.Types.INTEGER);
                stmt.setBoolean(9, juego.isEsRecomendado());
                stmt.setString(10, juego.getImagen() != null ? new File(juego.getImagen()).getName() : null);
                stmt.setString(11, juego.getVideo());

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idJuego = rs.getInt(1);
                            try (PreparedStatement relStmt = conn.prepareStatement(sqlRelacion)) {
                                if (juego.getConsola() != null) {
                                    relStmt.setInt(1, idJuego);
                                    relStmt.setInt(2, juego.getConsola().getId());
                                    relStmt.executeUpdate();
                                }
                            }
                        }
                    }
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                AppLogger.severe("Error al insertar juego: " + e.getMessage());
            }
        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

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

                String fecha = rs.getString("fecha_lanzamiento");
                try {
                    LocalDate fechaParsed = LocalDate.parse(fecha, FORMATO_SQLITE);
                    juego.setFechaLanzamiento(fechaParsed.format(FORMATO_FECHA));
                } catch (DateTimeParseException e) {
                    juego.setFechaLanzamiento("Fecha inválida");
                }

                juego.setEsRecomendado(rs.getBoolean("es_recomendado"));
                juego.setImagen(rs.getString("imagen"));
                juego.setVideo(rs.getString("video"));

                Estado estado = new Estado(
                        rs.getInt("id_estado"),
                        rs.getString("nombre_estado")
                );
                juego.setEstado(estado);

                if (rs.getString("nombre_consola") != null) {
                    Consola consola = new Consola(
                            rs.getInt("id_consola"),
                            rs.getString("nombre_consola"),
                            rs.getString("abreviatura_consola")
                    );
                    juego.setConsola(consola);
                    juego.setNombreConsola(rs.getString("juego_consola"));
                } else {
                    juego.setConsola(null);
                    juego.setNombreConsola(juego.getNombre());
                }

                lista.add(juego);
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener juegos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminarJuego(int idJuego) {
        String sqlJuego = "SELECT imagen, video FROM juegos WHERE id_juegos = ?";
        String sqlEliminarJuego = "DELETE FROM juegos WHERE id_juegos = ?";
        String sqlEliminarRelacionConsola = "DELETE FROM juegos_consolas WHERE id_juego = ?";

        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false);

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

            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarRelacionConsola)) {
                stmt.setInt(1, idJuego);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarJuego)) {
                stmt.setInt(1, idJuego);
                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    if (imagenJuego != null && !imagenJuego.isEmpty()) {
                        File archivoImagen = new File(Conexion.imagenesPath, imagenJuego);
                        if (archivoImagen.exists()) archivoImagen.delete();
                    }
                    if (videoJuego != null && !videoJuego.isEmpty()) {
                        File archivoVideo = new File(Conexion.videosPath, videoJuego);
                        if (archivoVideo.exists()) archivoVideo.delete();
                    }
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                AppLogger.severe("Error al eliminar el juego: " + e.getMessage());
            }
        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarJuego(Juego juego) {
        String sqlJuego = "UPDATE juegos SET nombre = ?, descripcion = ?, desarrollador = ?, editor = ?, genero = ?, modo_juego = ?, fecha_lanzamiento = ?, id_estado = ?, es_recomendado = ?, imagen = ?, video = ? WHERE id_juegos = ?";
        String sqlEliminarRelacionConsola = "DELETE FROM juegos_consolas WHERE id_juego = ?";
        String sqlRelacionConsola = "INSERT INTO juegos_consolas (id_juego, id_consola) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM juegos_consolas WHERE id_juego = ? AND id_consola = ?)";

        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego)) {
                stmt.setString(1, juego.getNombre());
                stmt.setString(2, juego.getDescripcion());
                stmt.setString(3, juego.getDesarrollador());
                stmt.setString(4, juego.getEditor());
                stmt.setString(5, juego.getGenero());
                stmt.setString(6, juego.getModoJuego());

                try {
                    LocalDate fecha = LocalDate.parse(juego.getFechaLanzamiento(), FORMATO_FECHA);
                    stmt.setString(7, fecha.format(FORMATO_SQLITE));
                } catch (DateTimeParseException e) {
                    stmt.setNull(7, java.sql.Types.VARCHAR);
                }

                stmt.setObject(8, juego.getEstado() != null ? juego.getEstado().getId() : null, java.sql.Types.INTEGER);
                stmt.setBoolean(9, juego.isEsRecomendado());
                stmt.setString(10, juego.getImagen() != null ? new File(juego.getImagen()).getName() : null);
                stmt.setString(11, juego.getVideo());
                stmt.setInt(12, juego.getId());

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    try (PreparedStatement stmtEliminarRelacion = conn.prepareStatement(sqlEliminarRelacionConsola)) {
                        stmtEliminarRelacion.setInt(1, juego.getId());
                        stmtEliminarRelacion.executeUpdate();
                    }

                    try (PreparedStatement stmtRelacion = conn.prepareStatement(sqlRelacionConsola)) {
                        if (juego.getConsola() != null) {
                            stmtRelacion.setInt(1, juego.getId());
                            stmtRelacion.setInt(2, juego.getConsola().getId());
                            stmtRelacion.setInt(3, juego.getId());
                            stmtRelacion.setInt(4, juego.getConsola().getId());
                            stmtRelacion.executeUpdate();
                        }
                    }

                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                AppLogger.severe("Error al actualizar juego: " + e.getMessage());
            }
        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}