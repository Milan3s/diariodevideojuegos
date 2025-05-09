package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.sql.*;

public class LogrosDAO {

    public ObservableList<Logros> obtenerLogros() {
        ObservableList<Logros> lista = FXCollections.observableArrayList();
        String sql = "SELECT l.*, j.nombre AS nombre_juego, j.imagen AS imagen_juego, " +
                     "e.nombre AS nombre_estado, d.nombre AS nombre_dificultad, c.nombre AS nombre_consola " +
                     "FROM logros l " +
                     "JOIN juegos j ON l.id_juego = j.id_juegos " +
                     "JOIN estados e ON l.id_estado = e.id_estado " +
                     "LEFT JOIN dificultades_logros d ON l.id_dificultad = d.id_dificultad " +
                     "JOIN consolas c ON l.id_consola = c.id_consola " +
                     "ORDER BY l.nombre ASC";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Juego juego = new Juego(rs.getInt("id_juego"), rs.getString("nombre_juego"));
                juego.setImagen(rs.getString("imagen_juego"));

                Estado estado = new Estado(rs.getInt("id_estado"), rs.getString("nombre_estado"));

                Dificultad dificultad = null;
                if (rs.getString("nombre_dificultad") != null) {
                    dificultad = new Dificultad(rs.getInt("id_dificultad"), rs.getString("nombre_dificultad"));
                }

                Consola consola = new Consola(rs.getInt("id_consola"), rs.getString("nombre_consola"), null);

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

    public boolean insertarLogro(Logros logro) {
        String sql = "INSERT INTO logros (nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin, " +
                     "intentos, creditos, puntuacion, id_juego, id_estado, id_dificultad, id_consola) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, logro.getNombre());
            stmt.setString(2, logro.getDescripcion());
            stmt.setInt(3, logro.getHorasEstimadas());
            stmt.setInt(4, logro.getAnio());
            stmt.setString(5, logro.getFechaInicio());
            stmt.setString(6, logro.getFechaFin());
            stmt.setInt(7, logro.getIntentos());
            stmt.setInt(8, logro.getCreditos());
            stmt.setInt(9, logro.getPuntuacion());
            stmt.setInt(10, logro.getJuego().getId());
            stmt.setInt(11, logro.getEstado().getId());

            if (logro.getDificultad() != null) {
                stmt.setInt(12, logro.getDificultad().getId());
            } else {
                stmt.setNull(12, Types.INTEGER);
            }

            stmt.setInt(13, logro.getConsola().getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarLogro(Logros logro) {
        String sql = "UPDATE logros SET nombre = ?, descripcion = ?, horas_estimadas = ?, anio = ?, fecha_inicio = ?, " +
                     "fecha_fin = ?, intentos = ?, creditos = ?, puntuacion = ?, id_juego = ?, id_estado = ?, id_dificultad = ?, id_consola = ? " +
                     "WHERE id_logro = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, logro.getNombre());
            stmt.setString(2, logro.getDescripcion());
            stmt.setInt(3, logro.getHorasEstimadas());
            stmt.setInt(4, logro.getAnio());
            stmt.setString(5, logro.getFechaInicio());
            stmt.setString(6, logro.getFechaFin());
            stmt.setInt(7, logro.getIntentos());
            stmt.setInt(8, logro.getCreditos());
            stmt.setInt(9, logro.getPuntuacion());
            stmt.setInt(10, logro.getJuego().getId());
            stmt.setInt(11, logro.getEstado().getId());

            if (logro.getDificultad() != null) {
                stmt.setInt(12, logro.getDificultad().getId());
            } else {
                stmt.setNull(12, Types.INTEGER);
            }

            stmt.setInt(13, logro.getConsola().getId());
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
}