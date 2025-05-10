package dao;

import config.Conexion;
import config.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO genérico para operaciones sobre tablas de datos auxiliares simples.
 */
public class DatosAuxiliaresDAO {

    public List<String[]> listar(String tabla, String columnaNombre, String columnaId) {
        List<String[]> resultados = new ArrayList<>();

        String sql = "SELECT " + columnaId + ", " + columnaNombre + " FROM " + tabla + " ORDER BY " + columnaNombre;

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString(columnaId);
                String nombre = rs.getString(columnaNombre);
                resultados.add(new String[]{id, nombre});
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al listar datos auxiliares de " + tabla + ": " + e.getMessage());
        }

        return resultados;
    }

    public List<String[]> buscar(String tabla, String columnaNombre, String columnaId, String filtro) {
        List<String[]> resultados = new ArrayList<>();

        String sql = "SELECT " + columnaId + ", " + columnaNombre + " FROM " + tabla +
                     " WHERE " + columnaNombre + " LIKE ? ORDER BY " + columnaNombre;

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + filtro + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new String[]{rs.getString(columnaId), rs.getString(columnaNombre)});
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al buscar en " + tabla + ": " + e.getMessage());
        }

        return resultados;
    }

    public boolean insertar(String tabla, String columnaNombre, String valor) {
        String sql = "INSERT INTO " + tabla + " (" + columnaNombre + ") VALUES (?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, valor);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al insertar en " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    public boolean editar(String tabla, String columnaNombre, String columnaId, int id, String nuevoValor) {
        String sql = "UPDATE " + tabla + " SET " + columnaNombre + " = ? WHERE " + columnaId + " = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoValor);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al editar en " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String tabla, String columnaId, int id) {
        String sql = "DELETE FROM " + tabla + " WHERE " + columnaId + " = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al eliminar en " + tabla + ": " + e.getMessage());
            return false;
        }
    }
}
