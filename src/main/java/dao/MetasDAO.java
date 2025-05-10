package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Metas;

import java.sql.*;
import java.time.LocalDate;

public class MetasDAO {

    public boolean insertarMeta(Metas meta) {
        switch (meta.getTipo().toLowerCase()) {
            case "twitch":
                return insertarTwitch(meta);
            case "especifica":
                return insertarEspecifica(meta);
            case "mejora":
                return insertarMejora(meta);
            case "extensible":
                return insertarExtensible(meta);
            case "seguidores":
                return insertarSeguidores(meta);
            default:
                return false;
        }
    }

    // === INSERTAR: metas_twitch ===
    private boolean insertarTwitch(Metas meta) {
        String sql = "INSERT INTO metas_twitch (descripcion, meta, actual, mes, anio, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setInt(2, meta.getMeta());
            stmt.setInt(3, meta.getActual() != null ? meta.getActual() : 0);
            stmt.setString(4, String.valueOf(LocalDate.now().getMonth()));
            stmt.setInt(5, LocalDate.now().getYear());
            stmt.setString(6, dateToString(meta.getFechaInicio()));
            stmt.setString(7, dateToString(meta.getFechaFin()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === INSERTAR: metas_especificas ===
    private boolean insertarEspecifica(Metas meta) {
        String sql = "INSERT INTO metas_especificas (descripcion, juegos_objetivo, juegos_completados, fabricante, id_consola, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setInt(2, meta.getJuegosObjetivo());
            stmt.setInt(3, meta.getJuegosCompletados());
            stmt.setString(4, meta.getFabricante());
            stmt.setInt(5, meta.getIdConsola());
            stmt.setString(6, dateToString(meta.getFechaInicio()));
            stmt.setString(7, dateToString(meta.getFechaFin()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === INSERTAR: mejoras_canal ===
    private boolean insertarMejora(Metas meta) {
        String sql = "INSERT INTO mejoras_canal (descripcion, fecha) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setString(2, dateToString(meta.getFechaInicio(), true)); // default: current date

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === INSERTAR: eventos_extensibles ===
    private boolean insertarExtensible(Metas meta) {
        String sql = "INSERT INTO eventos_extensibles (motivo, fecha_evento) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getMotivo());
            stmt.setString(2, dateToString(meta.getFechaEvento()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === INSERTAR: seguidores ===
    private boolean insertarSeguidores(Metas meta) {
        String sql = "INSERT INTO seguidores (cantidad, fecha_registro) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, meta.getActual() != null ? meta.getActual() : 0);
            stmt.setString(2, dateToString(meta.getFechaInicio(), true)); // default: now

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === Utilidad: convierte fecha a String ISO o nulo ===
    private String dateToString(LocalDate date) {
        return date != null ? date.toString() : null;
    }

    // Sobrecarga para dar fecha actual como fallback
    private String dateToString(LocalDate date, boolean usarFechaActualSiNulo) {
        return (date != null ? date : LocalDate.now()).toString();
    }

    // === FUTURO: obtener/actualizar/eliminar ===
    public ObservableList<Metas> obtenerMetasDummy() {
        return FXCollections.observableArrayList(); // método base de ejemplo
    }
}
