package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.MejorasDelCanal;

import java.sql.*;
import java.time.LocalDate;

public class MejorasDelCanalDAO {

    public ObservableList<MejorasDelCanal> obtenerMejoras() {
        ObservableList<MejorasDelCanal> lista = FXCollections.observableArrayList();
        String sql = "SELECT m.*, ec.nombre AS estado, m.id_estado_cumplida "
                + "FROM mejoras_canal m "
                + "LEFT JOIN estado_cumplida ec ON m.id_estado_cumplida = ec.id_estado_cumplida "
                + "ORDER BY m.fecha_inicio ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String estado = rs.getString("estado");
                boolean cumplida = "Sí".equalsIgnoreCase(estado);

                MejorasDelCanal mejora = new MejorasDelCanal(
                        rs.getInt("id_mejora"),
                        rs.getString("descripcion"),
                        rs.getInt("meta"),
                        rs.getInt("actual"),
                        LocalDate.parse(rs.getString("fecha_inicio")),
                        LocalDate.parse(rs.getString("fecha_fin")),
                        cumplida,
                        rs.getInt("id_estado_cumplida"),
                        estado
                );
                lista.add(mejora);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Integer insertarMejoraYDevolverId(MejorasDelCanal mejora) {
        String sql = "INSERT INTO mejoras_canal (descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_cumplida) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int idEstado = mejora.getIdEstadoCumplida();

            stmt.setString(1, mejora.getDescripcion());
            stmt.setInt(2, mejora.getMeta());
            stmt.setInt(3, mejora.getActual());
            stmt.setString(4, mejora.getFechaInicio().toString());
            stmt.setString(5, mejora.getFechaFin().toString());
            stmt.setInt(6, idEstado);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println(">>> Error al insertar mejora: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarMejora(MejorasDelCanal mejora) {
        String sql = "UPDATE mejoras_canal SET descripcion = ?, meta = ?, actual = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, id_estado_cumplida = ? WHERE id_mejora = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mejora.getDescripcion());
            stmt.setInt(2, mejora.getMeta());
            stmt.setInt(3, mejora.getActual());
            stmt.setString(4, mejora.getFechaInicio().toString());
            stmt.setString(5, mejora.getFechaFin().toString());
            stmt.setInt(6, mejora.getIdEstadoCumplida());
            stmt.setInt(7, mejora.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarMejora(int id) {
        String sql = "DELETE FROM mejoras_canal WHERE id_mejora = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<String> obtenerFechasUnicas(String columnaFecha) {
        ObservableList<String> fechas = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT " + columnaFecha + " FROM mejoras_canal "
                + "WHERE " + columnaFecha + " IS NOT NULL ORDER BY " + columnaFecha + " ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fechas.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fechas;
    }

    public ObservableList<String> obtenerValoresCumplida() {
        ObservableList<String> valores = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT nombre FROM estado_cumplida ORDER BY nombre ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                valores.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valores;
    }

    public ObservableList<String> obtenerAniosDisponibles() {
        ObservableList<String> anios = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT anio FROM anios_mejoras_canal ORDER BY anio DESC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                anios.add(rs.getString("anio"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return anios;
    }

    public int obtenerIdEstadoCumplidaDesdeNombre(String nombre) {
        String sql = "SELECT id_estado_cumplida FROM estado_cumplida WHERE LOWER(nombre) = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre.toLowerCase());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_estado_cumplida");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
