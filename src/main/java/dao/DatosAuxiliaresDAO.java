package dao;

import config.AppLogger;
import config.Conexion;
import models.ConfiguracionAuxiliar;
import models.DatosAuxiliares;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatosAuxiliaresDAO {

    public List<String> obtenerTiposVisuales() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre_visual FROM configuracion_auxiliares "
                   + "WHERE LOWER(nombre_visual) LIKE '%estado%' "
                   + "   OR LOWER(nombre_visual) LIKE '%dificultad%' "
                   + "   OR LOWER(nombre_visual) LIKE '%año%' "
                   + "   OR LOWER(nombre_visual) LIKE '%cumplida%' "
                   + "ORDER BY nombre_visual";

        try (Connection conn = Conexion.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tipos.add(rs.getString("nombre_visual"));
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener tipos visuales: " + e.getMessage());
        }

        return tipos;
    }

    public void limpiarConfiguracionesNoUsadas() {
        String sql = "DELETE FROM configuracion_auxiliares "
                   + "WHERE LOWER(nombre_visual) NOT LIKE '%estado%' "
                   + "  AND LOWER(nombre_visual) NOT LIKE '%dificultad%' "
                   + "  AND LOWER(nombre_visual) NOT LIKE '%año%' "
                   + "  AND LOWER(nombre_visual) NOT LIKE '%cumplida%' "
                   + "  AND nombre_tabla NOT IN ("
                   + "      'estados', 'dificultades_logros', 'anios_metas_twitch', "
                   + "      'anios_metas_especificas', 'anios_mejoras_canal', 'estado_cumplida'"
                   + ")";

        try (Connection conn = Conexion.obtenerConexion(); Statement stmt = conn.createStatement()) {
            int eliminados = stmt.executeUpdate(sql);
            AppLogger.info("Configuraciones no necesarias eliminadas: " + eliminados);
        } catch (SQLException e) {
            AppLogger.severe("Error al limpiar configuraciones auxiliares: " + e.getMessage());
        }
    }

    private TablaAuxiliar obtenerConfiguracion(String nombreVisual) {
        String sql = "SELECT nombre_tabla, columna_id, columna_nombre FROM configuracion_auxiliares WHERE nombre_visual = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
            sql = String.format("SELECT DISTINCT %s, %s FROM %s WHERE tipo = ? ORDER BY %s",
                    config.columnaId, config.columnaNombre, config.tabla, config.columnaNombre);
        } else {
            sql = String.format("SELECT DISTINCT %s, %s FROM %s ORDER BY %s",
                    config.columnaId, config.columnaNombre, config.tabla, config.columnaNombre);
        }

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (config.tabla.equalsIgnoreCase("estados")) {
                stmt.setString(1, inferirTipoDesdeVisual(config.tipoVisual));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(config.columnaId);
                    String nombre = rs.getString(config.columnaNombre);
                    resultados.add(new DatosAuxiliares(id, nombre, null, config.tipoVisual));
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al listar datos auxiliares de " + config.tabla + ": " + e.getMessage());
        }

        return resultados;
    }

    public int insertarYObtenerId(String nombreVisual, String valor) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return -1;

        String sql = String.format("INSERT INTO %s (%s) VALUES (?)", config.tabla, config.columnaNombre);

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, valor);
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al insertar en " + config.tabla + ": " + e.getMessage());
        }

        return -1;
    }

    public boolean editar(String nombreVisual, int id, String nuevoValor) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) return false;

        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", config.tabla, config.columnaNombre, config.columnaId);

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

        String sql = String.format("DELETE FROM %s WHERE %s = ?", config.tabla, config.columnaId);

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.severe("Error al eliminar en " + config.tabla + ": " + e.getMessage());
            return false;
        }
    }

    public boolean existeRegistro(String tipoVisual, String nombre) {
        TablaAuxiliar config = obtenerConfiguracion(tipoVisual);
        if (config == null) return false;

        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", config.tabla, config.columnaNombre);

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al verificar existencia de registro: " + e.getMessage());
            return false;
        }
    }

    public DatosAuxiliares buscarPorNombre(String tipoVisual, String nombre) {
        TablaAuxiliar config = obtenerConfiguracion(tipoVisual);
        if (config == null) return null;

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", config.columnaNombre, config.tabla, config.columnaNombre);

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DatosAuxiliares(rs.getInt(config.columnaNombre), nombre, null, tipoVisual);
                }
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al buscar por nombre: " + e.getMessage());
        }

        return null;
    }

    public ConfiguracionAuxiliar obtenerConfiguracionVisual(String nombreVisual) {
        String sql = "SELECT id_configuracion, nombre_visual, nombre_tabla, columna_id, columna_nombre FROM configuracion_auxiliares WHERE nombre_visual = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreVisual);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ConfiguracionAuxiliar(
                            rs.getInt("id_configuracion"),
                            rs.getString("nombre_visual"),
                            rs.getString("nombre_tabla"),
                            rs.getString("columna_id"),
                            rs.getString("columna_nombre")
                    );
                }
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener configuración auxiliar para: " + nombreVisual + ": " + e.getMessage());
        }
        return null;
    }

    private String inferirTipoDesdeVisual(String visual) {
        if (visual == null) return null;
        visual = visual.toLowerCase();
        if (visual.contains("juego")) return "juego";
        if (visual.contains("logro")) return "logro";
        if (visual.contains("moderador")) return "moderador";
        if (visual.contains("consola")) return "consola";
        return "general";
    }

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
