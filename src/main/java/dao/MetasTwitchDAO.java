package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.MetasTwitch;

import java.sql.*;
import java.time.LocalDate;

public class MetasTwitchDAO {

    public ObservableList<MetasTwitch> obtenerMetas() {
        ObservableList<MetasTwitch> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM metas_twitch ORDER BY fecha_inicio ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MetasTwitch meta = new MetasTwitch(
                        rs.getInt("id_meta"),
                        rs.getString("descripcion"),
                        rs.getInt("meta"),
                        rs.getInt("actual"),
                        LocalDate.parse(rs.getString("fecha_inicio").substring(0, 10)),
                        LocalDate.parse(rs.getString("fecha_fin").substring(0, 10)),
                        rs.getString("fecha_registro") != null
                        ? LocalDate.parse(rs.getString("fecha_registro").substring(0, 10))
                        : null,
                        rs.getInt("anio")
                );
                lista.add(meta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Integer insertarMetaYDevolverId(MetasTwitch meta) {
        String sql = "INSERT INTO metas_twitch (descripcion, meta, actual, fecha_inicio, fecha_fin, anio) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setInt(2, meta.getMeta());
            stmt.setInt(3, meta.getActual());
            stmt.setString(4, meta.getFechaInicio().toString());
            stmt.setString(5, meta.getFechaFin().toString());
            stmt.setInt(6, meta.getAnio());

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarMeta(MetasTwitch meta) {
        String sql = "UPDATE metas_twitch SET descripcion = ?, meta = ?, actual = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, anio = ? WHERE id_meta = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setInt(2, meta.getMeta());
            stmt.setInt(3, meta.getActual());
            stmt.setString(4, meta.getFechaInicio().toString());
            stmt.setString(5, meta.getFechaFin().toString());
            stmt.setInt(6, meta.getAnio());
            stmt.setInt(7, meta.getIdMeta());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarMeta(int idMeta) {
        String sql = "DELETE FROM metas_twitch WHERE id_meta = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMeta);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<String> obtenerAniosDisponibles() {
        ObservableList<String> anios = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT anio FROM anios_metas_twitch ORDER BY anio DESC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                anios.add(rs.getString("anio"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return anios;
    }

}
