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
        String sql = "SELECT * FROM mejoras_canal ORDER BY fecha DESC";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MejorasDelCanal mejora = new MejorasDelCanal(
                        rs.getInt("id_mejora"),
                        rs.getString("descripcion"),
                        LocalDate.parse(rs.getString("fecha"))
                );
                lista.add(mejora);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Integer insertarMejoraYDevolverId(MejorasDelCanal mejora) {
        String sql = "INSERT INTO mejoras_canal (descripcion, fecha) VALUES (?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, mejora.getDescripcion());
            stmt.setString(2, mejora.getFecha().toString());

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
        String sql = "UPDATE mejoras_canal SET descripcion = ?, fecha = ? WHERE id_mejora = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mejora.getDescripcion());
            stmt.setString(2, mejora.getFecha().toString());
            stmt.setInt(3, mejora.getId());

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
