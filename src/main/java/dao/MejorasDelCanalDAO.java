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
        String sql = "SELECT * FROM mejoras_canal ORDER BY fecha_inicio DESC";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MejorasDelCanal mejora = new MejorasDelCanal(
                        rs.getInt("id_mejora"),
                        rs.getString("descripcion"),
                        rs.getInt("meta"),
                        rs.getInt("actual"),
                        LocalDate.parse(rs.getString("fecha_inicio")),
                        LocalDate.parse(rs.getString("fecha_fin")),
                        rs.getBoolean("cumplida")
                );
                lista.add(mejora);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Integer insertarMejoraYDevolverId(MejorasDelCanal mejora) {
        String sql = "INSERT INTO mejoras_canal "
                   + "(descripcion, meta, actual, fecha_inicio, fecha_fin, cumplida) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, mejora.getDescripcion());
            stmt.setInt(2, mejora.getMeta());
            stmt.setInt(3, mejora.getActual());
            stmt.setString(4, mejora.getFechaInicio().toString());
            stmt.setString(5, mejora.getFechaFin().toString());
            stmt.setBoolean(6, mejora.isCumplida());

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

    public boolean actualizarMejora(MejorasDelCanal mejora) {
        String sql = "UPDATE mejoras_canal SET "
                   + "descripcion = ?, meta = ?, actual = ?, "
                   + "fecha_inicio = ?, fecha_fin = ?, cumplida = ? "
                   + "WHERE id_mejora = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mejora.getDescripcion());
            stmt.setInt(2, mejora.getMeta());
            stmt.setInt(3, mejora.getActual());
            stmt.setString(4, mejora.getFechaInicio().toString());
            stmt.setString(5, mejora.getFechaFin().toString());
            stmt.setBoolean(6, mejora.isCumplida());
            stmt.setInt(7, mejora.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarMejora(int id) {
        String sql = "DELETE FROM mejoras_canal WHERE id_mejora = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
