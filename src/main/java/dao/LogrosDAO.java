package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.sql.*;

public class LogrosDAO {

    public ObservableList<Logros> obtenerLogros() {
        ObservableList<Logros> lista = FXCollections.observableArrayList();
        String sql = "SELECT l.*, "
                + "j.id_juegos AS id_juego, j.nombre AS nombre_juego, j.imagen AS imagen_juego, "
                + "e.id_estado AS id_estado, e.nombre AS nombre_estado, "
                + "d.id_dificultad AS id_dificultad, d.nombre AS nombre_dificultad, "
                + "c.id_consola AS id_consola, c.nombre AS nombre_consola "
                + "FROM logros l "
                + "LEFT JOIN juegos j ON l.id_juego = j.id_juegos "
                + "LEFT JOIN estados e ON l.id_estado = e.id_estado "
                + "LEFT JOIN dificultades_logros d ON l.id_dificultad = d.id_dificultad "
                + "LEFT JOIN consolas c ON l.id_consola = c.id_consola "
                + "ORDER BY l.nombre ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Juego juego = null;
                if (rs.getObject("id_juego") != null) {
                    juego = new Juego(rs.getInt("id_juego"), rs.getString("nombre_juego"));
                    juego.setImagen(rs.getString("imagen_juego"));
                }

                Estado estado = null;
                if (rs.getObject("id_estado") != null) {
                    estado = new Estado(rs.getInt("id_estado"), rs.getString("nombre_estado"));
                }

                Dificultad dificultad = null;
                if (rs.getObject("id_dificultad") != null) {
                    dificultad = new Dificultad(rs.getInt("id_dificultad"), rs.getString("nombre_dificultad"));
                }

                Consola consola = null;
                if (rs.getObject("id_consola") != null) {
                    consola = new Consola(rs.getInt("id_consola"), rs.getString("nombre_consola"), null);
                }

                Logros logro = new Logros(
                        rs.getInt("id_logro"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("horas_estimadas"),
                        rs.getInt("anio"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin"),
                        rs.getInt("intentos"),
                        rs.getInt("creditos"),
                        rs.getInt("puntuacion"),
                        rs.getString("fecha_registro"),
                        juego,
                        estado,
                        dificultad,
                        consola
                );

                lista.add(logro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Integer insertarLogroYDevolverId(Logros logro) {
        String sql = "INSERT INTO logros (nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin, "
                + "intentos, creditos, puntuacion, id_juego, id_estado, id_dificultad, id_consola) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setLogroParams(stmt, logro);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarLogro(Logros logro) {
        String sql = "UPDATE logros SET nombre = ?, descripcion = ?, horas_estimadas = ?, anio = ?, fecha_inicio = ?, "
                + "fecha_fin = ?, intentos = ?, creditos = ?, puntuacion = ?, id_juego = ?, id_estado = ?, "
                + "id_dificultad = ?, id_consola = ? WHERE id_logro = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            setLogroParams(stmt, logro);
            stmt.setInt(14, logro.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarLogro(int id) {
        String sql = "DELETE FROM logros WHERE id_logro = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setLogroParams(PreparedStatement stmt, Logros logro) throws SQLException {
        stmt.setString(1, logro.getNombre());
        stmt.setString(2, emptyToNull(logro.getDescripcion()));
        stmt.setInt(3, logro.getHorasEstimadas());
        stmt.setInt(4, logro.getAnio());
        stmt.setString(5, emptyToNull(logro.getFechaInicio()));
        stmt.setString(6, emptyToNull(logro.getFechaFin()));
        stmt.setInt(7, logro.getIntentos());
        stmt.setInt(8, logro.getCreditos());
        stmt.setInt(9, logro.getPuntuacion());

        if (logro.getJuego() != null) {
            stmt.setInt(10, logro.getJuego().getId());
        } else {
            stmt.setNull(10, Types.INTEGER);
        }

        if (logro.getEstado() != null) {
            stmt.setInt(11, logro.getEstado().getId());
        } else {
            stmt.setNull(11, Types.INTEGER);
        }

        if (logro.getDificultad() != null) {
            stmt.setInt(12, logro.getDificultad().getId());
        } else {
            stmt.setNull(12, Types.INTEGER);
        }

        if (logro.getConsola() != null) {
            stmt.setInt(13, logro.getConsola().getId());
        } else {
            stmt.setNull(13, Types.INTEGER);
        }
    }

    private String emptyToNull(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }
}
