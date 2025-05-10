package dao;

import config.Conexion;
import config.AppLogger;
import models.DatosAuxiliares;

import java.sql.*;
import java.util.*;

/**
 * DAO genérico mejorado para datos auxiliares dinámicos.
 */
public class DatosAuxiliaresDAO {

    /**
     * Recupera todas las opciones disponibles desde configuracion_auxiliares.
     * Se usa para poblar el ComboBox en el controlador.
     */
    public List<String> obtenerTiposVisuales() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre_visual FROM configuracion_auxiliares ORDER BY nombre_visual";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tipos.add(rs.getString("nombre_visual"));
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener tipos visuales: " + e.getMessage());
        }

        return tipos;
    }

    /**
     * Recupera la configuración para una clave visual (ej. "Estados", "Dificultades", etc.)
     */
    private TablaAuxiliar obtenerConfiguracion(String nombreVisual) {
        String sql = "SELECT nombre_tabla, columna_id, columna_nombre FROM configuracion_auxiliares WHERE nombre_visual = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreVisual);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TablaAuxiliar(
                        rs.getString("nombre_tabla"),
                        rs.getString("columna_id"),
                        rs.getString("columna_nombre"),
                        nombreVisual
                    );
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener configuración para '" + nombreVisual + "': " + e.getMessage());
        }
        return null;
    }

    public List<DatosAuxiliares> listar(String nombreVisual) {
        List<DatosAuxiliares> resultados = new ArrayList<>();
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return resultados;

        String sql;
        if (config.tabla.equalsIgnoreCase("estados")) {
            sql = "SELECT " + config.columnaId + ", " + config.columnaNombre + ", tipo FROM " + config.tabla +
                  " ORDER BY " + config.columnaNombre;
        } else {
            sql = "SELECT " + config.columnaId + ", " + config.columnaNombre + " FROM " + config.tabla +
                  " ORDER BY " + config.columnaNombre;
        }

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt(config.columnaId);
                String nombre = rs.getString(config.columnaNombre);
                String tipo = config.tabla.equalsIgnoreCase("estados") ? rs.getString("tipo") : null;
                resultados.add(new DatosAuxiliares(id, nombre, tipo, config.tipoVisual));
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al listar datos auxiliares de " + config.tabla + ": " + e.getMessage());
        }

        return resultados;
    }

    public List<DatosAuxiliares> buscar(String nombreVisual, String filtro) {
        List<DatosAuxiliares> resultados = new ArrayList<>();
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return resultados;

        String sql;
        if (config.tabla.equalsIgnoreCase("estados")) {
            sql = "SELECT " + config.columnaId + ", " + config.columnaNombre + ", tipo FROM " + config.tabla +
                  " WHERE " + config.columnaNombre + " LIKE ? ORDER BY " + config.columnaNombre;
        } else {
            sql = "SELECT " + config.columnaId + ", " + config.columnaNombre + " FROM " + config.tabla +
                  " WHERE " + config.columnaNombre + " LIKE ? ORDER BY " + config.columnaNombre;
        }

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + filtro + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(config.columnaId);
                    String nombre = rs.getString(config.columnaNombre);
                    String tipo = config.tabla.equalsIgnoreCase("estados") ? rs.getString("tipo") : null;
                    resultados.add(new DatosAuxiliares(id, nombre, tipo, config.tipoVisual));
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al buscar en " + config.tabla + ": " + e.getMessage());
        }

        return resultados;
    }

    public boolean insertar(String nombreVisual, String valor) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return false;

        String sql = "INSERT INTO " + config.tabla + " (" + config.columnaNombre + ") VALUES (?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, valor);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al insertar en " + config.tabla + ": " + e.getMessage());
            return false;
        }
    }

    public boolean editar(String nombreVisual, int id, String nuevoValor) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return false;

        String sql = "UPDATE " + config.tabla + " SET " + config.columnaNombre + " = ? WHERE " + config.columnaId + " = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoValor);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al editar en " + config.tabla + ": " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String nombreVisual, int id) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return false;

        String sql = "DELETE FROM " + config.tabla + " WHERE " + config.columnaId + " = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al eliminar en " + config.tabla + ": " + e.getMessage());
            return false;
        }
    }

    /** Clase interna para mapear la configuración de cada tipo visual */
    private static class TablaAuxiliar {
        final String tabla;
        final String columnaId;
        final String columnaNombre;
        final String tipoVisual;

        public TablaAuxiliar(String tabla, String columnaId, String columnaNombre, String tipoVisual) {
            this.tabla = tabla;
            this.columnaId = columnaId;
            this.columnaNombre = columnaNombre;
            this.tipoVisual = tipoVisual;
        }
    }
}
