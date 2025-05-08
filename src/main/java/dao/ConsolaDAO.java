package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Consola;
import models.Estado;

import java.sql.*;

public class ConsolaDAO {

    public ObservableList<Consola> obtenerTodas() {
        ObservableList<Consola> consolas = FXCollections.observableArrayList();
        String sql = "SELECT c.*, e.id_estado AS estado_id, e.nombre AS estado_nombre " +
                     "FROM consolas c " +
                     "LEFT JOIN estados e ON c.id_estado = e.id_estado AND e.tipo = 'consola' " +
                     "ORDER BY c.nombre";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estado estado = null;
                int idEstado = rs.getInt("estado_id");
                if (!rs.wasNull()) {
                    estado = new Estado(idEstado, rs.getString("estado_nombre"));
                }

                Consola consola = new Consola(
                    rs.getInt("id_consola"),
                    rs.getString("nombre"),
                    rs.getString("abreviatura"),
                    rs.getObject("anio") != null ? rs.getInt("anio") : null,
                    rs.getString("fabricante"),
                    rs.getString("generacion"),
                    rs.getString("region"),
                    rs.getString("tipo"),
                    rs.getString("procesador"),
                    rs.getString("memoria"),
                    rs.getString("almacenamiento"),
                    rs.getString("fecha_lanzamiento"),
                    rs.getString("imagen"),
                    estado
                );
                consolas.add(consola);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consolas;
    }

    public boolean insertar(Consola consola) {
        String sql = "INSERT INTO consolas (nombre, abreviatura, anio, fabricante, generacion, region, tipo, " +
                     "procesador, memoria, almacenamiento, fecha_lanzamiento, imagen, id_estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl());
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, consola.getNombre());
            stmt.setString(2, consola.getAbreviatura());

            if (consola.getAnio() != null) {
                stmt.setInt(3, consola.getAnio());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, consola.getFabricante());
            stmt.setString(5, consola.getGeneracion());
            stmt.setString(6, consola.getRegion());
            stmt.setString(7, consola.getTipo());
            stmt.setString(8, consola.getProcesador());
            stmt.setString(9, consola.getMemoria());
            stmt.setString(10, consola.getAlmacenamiento());
            stmt.setString(11, consola.getFechaLanzamiento());
            stmt.setString(12, consola.getImagen());

            if (consola.getEstado() != null) {
                stmt.setInt(13, consola.getEstado().getId());
            } else {
                stmt.setNull(13, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean actualizar(Consola consola) {
        String sql = "UPDATE consolas SET nombre=?, abreviatura=?, anio=?, fabricante=?, generacion=?, " +
                     "region=?, tipo=?, procesador=?, memoria=?, almacenamiento=?, fecha_lanzamiento=?, " +
                     "imagen=?, id_estado=? WHERE id_consola=?";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl());
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, consola.getNombre());
            stmt.setString(2, consola.getAbreviatura());

            if (consola.getAnio() != null) {
                stmt.setInt(3, consola.getAnio());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, consola.getFabricante());
            stmt.setString(5, consola.getGeneracion());
            stmt.setString(6, consola.getRegion());
            stmt.setString(7, consola.getTipo());
            stmt.setString(8, consola.getProcesador());
            stmt.setString(9, consola.getMemoria());
            stmt.setString(10, consola.getAlmacenamiento());
            stmt.setString(11, consola.getFechaLanzamiento());
            stmt.setString(12, consola.getImagen());

            if (consola.getEstado() != null) {
                stmt.setInt(13, consola.getEstado().getId());
            } else {
                stmt.setNull(13, Types.INTEGER);
            }

            stmt.setInt(14, consola.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM consolas WHERE id_consola = ?";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl());
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
