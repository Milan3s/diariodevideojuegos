package dao;

import config.Conexion;
import config.AppLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.ConfiguracionAuxiliar;
import models.DatosAuxiliares;
import models.Inicio;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class InicioDAO {

    public Inicio obtenerResumen() {
        int totalJuegos = 0;
        int totalLogros = 0;
        int totalConsolas = 0;
        int totalEventos = 0;
        int totalMetas = 0;
        int totalSeguidores = 0;

        String metaSeguidoresProgreso = "No disponible";
        String metaJuegosCompletadosDescripcion = "No disponible";
        String mejorasDelCanal = "Sin mejoras registradas";
        String fechaExtensible = "No registrada";
        String diasParaExtensible = "No disponible";
        String metaEspecifica = "No hay metas específicas registradas";

        try (Connection conn = Conexion.obtenerConexion()) {

            totalJuegos = ejecutarConteo(conn, "SELECT COUNT(*) FROM juegos");
            totalLogros = ejecutarConteo(conn, "SELECT COUNT(*) FROM logros");
            totalConsolas = ejecutarConteo(conn, "SELECT COUNT(*) FROM consolas");
            totalEventos = ejecutarConteo(conn, "SELECT COUNT(*) FROM eventos");
            totalMetas = ejecutarConteo(conn, "SELECT COUNT(*) FROM metas_twitch");
            totalSeguidores = ejecutarConteo(conn, "SELECT SUM(cantidad) FROM seguidores");

            // Meta seguidores desde asignación
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT mt.meta, mt.actual FROM configuracion_auxiliares_asignadas ca " +
                            "JOIN metas_twitch mt ON ca.id_valor = mt.id_meta " +
                            "WHERE ca.nombre_tabla = 'metas_twitch' ORDER BY ca.fecha_asignacion DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int meta = rs.getInt("meta");
                    int actual = rs.getInt("actual");
                    if (meta > 0) {
                        int progreso = Math.min((actual * 100) / meta, 100);
                        metaSeguidoresProgreso = actual + " / " + meta + " - " + progreso + "%";
                    } else {
                        metaSeguidoresProgreso = "Meta inválida";
                    }
                }
            }

            // Meta de juegos completados
            int juegosCompletados = 0;
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM juegos j JOIN estados e ON j.id_estado = e.id_estado " +
                            "WHERE e.tipo = 'juego' AND e.nombre = 'Completado'");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    juegosCompletados = rs.getInt(1);
                }

                if (totalJuegos < 50) {
                    metaJuegosCompletadosDescripcion = String.format(
                            "Completados: %d / %d. No hay suficientes juegos para alcanzar la meta de 50.",
                            juegosCompletados, totalJuegos);
                } else if (juegosCompletados >= 50) {
                    metaJuegosCompletadosDescripcion = String.format(
                            "Completados: %d / %d. ¡Meta alcanzada!", juegosCompletados, totalJuegos);
                } else {
                    int faltan = 50 - juegosCompletados;
                    metaJuegosCompletadosDescripcion = String.format(
                            "Completados: %d / %d. Faltan %d para la meta.", juegosCompletados, totalJuegos, faltan);
                }
            }

            // Última mejora del canal desde asignación
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT mc.descripcion FROM configuracion_auxiliares_asignadas ca " +
                            "JOIN mejoras_canal mc ON ca.id_valor = mc.id_mejora " +
                            "WHERE ca.nombre_tabla = 'mejoras_canal' ORDER BY ca.fecha_asignacion DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mejorasDelCanal = rs.getString("descripcion");
                }
            }

            // Próximo evento extensible desde asignación
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ee.fecha_evento FROM configuracion_auxiliares_asignadas ca " +
                            "JOIN eventos_extensibles ee ON ca.id_valor = ee.id_extensible " +
                            "WHERE ca.nombre_tabla = 'eventos_extensibles' ORDER BY ca.fecha_asignacion DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fechaStr = rs.getString("fecha_evento");
                    fechaExtensible = fechaStr;
                    try {
                        LocalDate fechaEvento = LocalDate.parse(fechaStr);
                        long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaEvento);
                        diasParaExtensible = dias >= 0 ? "Faltan " + dias + " días" : "Ya ocurrió";
                    } catch (DateTimeParseException e) {
                        fechaExtensible = "Fecha inválida";
                        diasParaExtensible = "Desconocido";
                    }
                }
            }

            // Última meta específica asignada
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT me.descripcion, me.juegos_completados, me.juegos_objetivo, me.fecha_fin FROM configuracion_auxiliares_asignadas ca " +
                            "JOIN metas_especificas me ON ca.id_valor = me.id_meta_especifica " +
                            "WHERE ca.nombre_tabla = 'metas_especificas' ORDER BY ca.fecha_asignacion DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String descripcion = rs.getString("descripcion");
                    int completados = rs.getInt("juegos_completados");
                    int objetivo = rs.getInt("juegos_objetivo");
                    String fechaFin = rs.getString("fecha_fin");

                    metaEspecifica = String.format("%s (%d / %d)\nFinaliza: %s",
                            descripcion, completados, objetivo, fechaFin);
                }
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener el resumen de inicio: " + e.getMessage());
        }

        return new Inicio(
                totalJuegos,
                totalLogros,
                totalConsolas,
                totalEventos,
                totalMetas,
                totalSeguidores,
                metaSeguidoresProgreso,
                metaJuegosCompletadosDescripcion,
                mejorasDelCanal,
                fechaExtensible,
                diasParaExtensible,
                metaEspecifica
        );
    }

    private int ejecutarConteo(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public ObservableList<ConfiguracionAuxiliar> obtenerConfiguracionesDeMetas() {
        ObservableList<ConfiguracionAuxiliar> lista = FXCollections.observableArrayList();
        lista.add(new ConfiguracionAuxiliar(1, "Metas Twitch", "metas_twitch", "id_meta", "descripcion"));
        lista.add(new ConfiguracionAuxiliar(2, "Metas Especificas", "metas_especificas", "id_meta_especifica", "descripcion"));
        lista.add(new ConfiguracionAuxiliar(3, "Mejoras del Canal", "mejoras_canal", "id_mejora", "descripcion"));
        lista.add(new ConfiguracionAuxiliar(4, "Eventos Extensibles", "eventos_extensibles", "id_extensible", "motivo"));
        return lista;
    }

    public ObservableList<DatosAuxiliares> obtenerDatosPorConfiguracion(ConfiguracionAuxiliar config) {
        ObservableList<DatosAuxiliares> lista = FXCollections.observableArrayList();
        if (config == null) return lista;

        String sql = "SELECT " + config.getColumnaId() + ", " + config.getColumnaNombre() +
                " FROM " + config.getNombreTabla() + " ORDER BY " + config.getColumnaNombre();

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(config.getColumnaId());
                String nombre = rs.getString(config.getColumnaNombre());
                lista.add(new DatosAuxiliares(id, nombre));
            }

        } catch (SQLException e) {
            AppLogger.warning("Error cargando datos para tabla " + config.getNombreTabla() + ": " + e.getMessage());
        }

        return lista;
    }

    public boolean asignarConfiguracionAuxiliar(String nombreTabla, String columnaId, int id) {
        String sql = "INSERT INTO configuracion_auxiliares_asignadas (nombre_tabla, columna_id, id_valor, fecha_asignacion) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreTabla);
            stmt.setString(2, columnaId);
            stmt.setInt(3, id);
            int filas = stmt.executeUpdate();

            AppLogger.info("Asignación guardada para tabla: " + nombreTabla + " ID: " + id);
            return filas > 0;

        } catch (SQLException e) {
            AppLogger.severe("Error al asignar configuración auxiliar: " + e.getMessage());
            return false;
        }
    }
}