package dao;

import config.Conexion;
import config.AppLogger;
import models.EventoExtensible;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventosExtensiblesDAO {

    public static boolean insertar(EventoExtensible evento) {
        String sql = "INSERT INTO eventos_extensibles (motivo, fecha_evento) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, evento.getMotivo());
            pstmt.setString(2, evento.getFechaEvento());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.severe("Error al insertar evento extensible: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizar(EventoExtensible evento) {
        String sql = "UPDATE eventos_extensibles SET motivo = ?, fecha_evento = ? WHERE id_extensible = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, evento.getMotivo());
            pstmt.setString(2, evento.getFechaEvento());
            pstmt.setInt(3, evento.getIdExtensible());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.severe("Error al actualizar evento extensible: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM eventos_extensibles WHERE id_extensible = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.severe("Error al eliminar evento extensible: " + e.getMessage());
            return false;
        }
    }

    public static List<EventoExtensible> obtenerTodos() {
        List<EventoExtensible> lista = new ArrayList<>();
        String sql = "SELECT id_extensible, motivo, fecha_evento FROM eventos_extensibles ORDER BY fecha_evento ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new EventoExtensible(
                        rs.getInt("id_extensible"),
                        rs.getString("motivo"),
                        rs.getString("fecha_evento")
                ));
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener eventos extensibles: " + e.getMessage());
        }

        return lista;
    }

    public static EventoExtensible obtenerPorId(int id) {
        String sql = "SELECT id_extensible, motivo, fecha_evento FROM eventos_extensibles WHERE id_extensible = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new EventoExtensible(
                            rs.getInt("id_extensible"),
                            rs.getString("motivo"),
                            rs.getString("fecha_evento")
                    );
                }
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener evento extensible por ID: " + e.getMessage());
        }

        return null;
    }

    public static List<EventoExtensible> buscarPorTexto(String texto) {
        List<EventoExtensible> lista = new ArrayList<>();
        String sql = "SELECT id_extensible, motivo, fecha_evento FROM eventos_extensibles WHERE LOWER(motivo) LIKE ? ORDER BY fecha_evento ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + texto.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new EventoExtensible(
                            rs.getInt("id_extensible"),
                            rs.getString("motivo"),
                            rs.getString("fecha_evento")
                    ));
                }
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al buscar eventos extensibles: " + e.getMessage());
        }

        return lista;
    }

    public static boolean existePorMotivoYFecha(String motivo, String fechaEvento) {
        String sql = "SELECT COUNT(*) FROM eventos_extensibles WHERE LOWER(motivo) = LOWER(?) AND fecha_evento = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, motivo.toLowerCase());
            pstmt.setString(2, fechaEvento);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al verificar existencia de evento extensible: " + e.getMessage());
            return false;
        }
    }

    public static EventoExtensible buscarPorMotivoYFecha(String motivo, String fechaEvento) {
        String sql = "SELECT id_extensible, motivo, fecha_evento FROM eventos_extensibles WHERE LOWER(motivo) = LOWER(?) AND fecha_evento = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, motivo.toLowerCase());
            pstmt.setString(2, fechaEvento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new EventoExtensible(
                            rs.getInt("id_extensible"),
                            rs.getString("motivo"),
                            rs.getString("fecha_evento")
                    );
                }
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al buscar evento extensible por motivo y fecha: " + e.getMessage());
        }

        return null;
    }

    public static List<String> obtenerAniosDisponibles() {
        List<String> anios = new ArrayList<>();
        String sql = "SELECT DISTINCT strftime('%Y', fecha_evento) AS anio FROM eventos_extensibles ORDER BY anio ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                anios.add(rs.getString("anio"));
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener años de eventos extensibles: " + e.getMessage());
        }

        return anios;
    }

    public static List<String> obtenerFechasDisponibles() {
        List<String> fechas = new ArrayList<>();
        String sql = "SELECT DISTINCT fecha_evento FROM eventos_extensibles ORDER BY fecha_evento ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                fechas.add(rs.getString("fecha_evento"));
            }
        } catch (SQLException e) {
            AppLogger.severe("Error al obtener fechas disponibles: " + e.getMessage());
        }

        return fechas;
    }
}
