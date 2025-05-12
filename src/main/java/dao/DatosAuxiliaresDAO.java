package dao;

import config.Conexion;
import config.AppLogger;
import models.DatosAuxiliares;

import java.sql.*;
import java.util.*;
import models.ConfiguracionAuxiliar;

public class DatosAuxiliaresDAO {

    public List<String> obtenerTiposVisuales() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre_visual FROM configuracion_auxiliares ORDER BY nombre_visual";

        try (Connection conn = Conexion.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tipos.add(rs.getString("nombre_visual"));
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener tipos visuales: " + e.getMessage());
        }

        return tipos;
    }

    private TablaAuxiliar obtenerConfiguracion(String nombreVisual) {
        String sql = "SELECT nombre_tabla, columna_id, columna_nombre FROM configuracion_auxiliares WHERE nombre_visual = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreVisual);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tabla = rs.getString("nombre_tabla");
                    String columnaId = rs.getString("columna_id");
                    String columnaNombre = rs.getString("columna_nombre");

                    // Verificar si la columna nombre existe, si no, usar descripcion
                    String columnaMostrar = existeColumna(conn, tabla, columnaNombre) ? columnaNombre
                            : existeColumna(conn, tabla, "descripcion") ? "descripcion" : columnaNombre;

                    return new TablaAuxiliar(
                            tabla,
                            columnaId,
                            columnaMostrar,
                            nombreVisual
                    );
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener configuración para '" + nombreVisual + "': " + e.getMessage());
        }
        return null;
    }

    private boolean existeColumna(Connection conn, String tabla, String columna) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, tabla, columna)) {
                return columns.next();
            }
        } catch (SQLException e) {
            AppLogger.warning("Error al verificar columna " + columna + " en tabla " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    public List<DatosAuxiliares> listar(String nombreVisual) {
        List<DatosAuxiliares> resultados = new ArrayList<>();
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) {
            return resultados;
        }

        String sql;
        if (config.tabla.equalsIgnoreCase("estados")) {
            sql = String.format("SELECT %s, %s, tipo FROM %s WHERE tipo = ? ORDER BY %s",
                    config.columnaId, config.columnaNombre, config.tabla, config.columnaNombre);
        } else {
            sql = String.format("SELECT %s, %s FROM %s ORDER BY %s",
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
                    String tipo = config.tabla.equalsIgnoreCase("estados") ? rs.getString("tipo") : null;
                    resultados.add(new DatosAuxiliares(id, nombre, tipo, config.tipoVisual));
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al listar datos auxiliares de " + config.tabla + ": " + e.getMessage());
        }

        return resultados;
    }

    public int insertarYObtenerId(String nombreVisual, String valor) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) {
            return -1;
        }

        String sql;
        boolean esEstado = config.tabla.equalsIgnoreCase("estados");

        if (esEstado) {
            sql = String.format("INSERT INTO %s (%s, tipo) VALUES (?, ?)",
                    config.tabla, config.columnaNombre);
        } else {
            sql = String.format("INSERT INTO %s (%s) VALUES (?)",
                    config.tabla, config.columnaNombre);
        }

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, valor);
            if (esEstado) {
                pstmt.setString(2, inferirTipoDesdeVisual(config.tipoVisual));
            }

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al insertar en " + config.tabla + ": " + e.getMessage());
        }

        return -1;
    }

    public boolean editar(String nombreVisual, int id, String nuevoValor) {
        TablaAuxiliar config = obtenerConfiguracion(nombreVisual);
        if (config == null) {
            return false;
        }

        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                config.tabla, config.columnaNombre, config.columnaId);

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
        if (config == null) {
            return false;
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                config.tabla, config.columnaId);

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
        if (config == null) {
            return false;
        }

        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                config.tabla, config.columnaNombre);

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
        if (config == null) {
            return null;
        }

        String sql = String.format("SELECT %s, %s%s FROM %s WHERE %s = ?",
                config.columnaId, config.columnaNombre,
                config.tabla.equalsIgnoreCase("estados") ? ", tipo" : "",
                config.tabla, config.columnaNombre);

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(config.columnaId);
                    String tipo = config.tabla.equalsIgnoreCase("estados") ? rs.getString("tipo") : null;
                    return new DatosAuxiliares(id, nombre, tipo, tipoVisual);
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

    private String inferirTipoDesdeVisual(String visual) {
        if (visual == null) {
            return null;
        }
        visual = visual.toLowerCase();
        if (visual.contains("juego")) {
            return "juego";
        }
        if (visual.contains("logro")) {
            return "logro";
        }
        if (visual.contains("moderador")) {
            return "moderador";
        }
        if (visual.contains("consola")) {
            return "consola";
        }
        return "general";
    }
}
