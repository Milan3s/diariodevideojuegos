package dao;

import config.Database;
import config.Logger;
import models.Consola;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsolaDAO {

    public static List<Consola> obtenerTodasLasConsolas() {
        List<Consola> consolas = new ArrayList<>();
        String sql = "SELECT * FROM consolas ORDER BY nombre ASC;";

        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Consola consola = new Consola(
                        rs.getInt("id_consolas"),
                        rs.getString("nombre"),
                        rs.getString("anio"),
                        rs.getString("fabricante"),
                        rs.getString("generacion"),
                        rs.getString("region"),
                        rs.getString("tipo"),
                        rs.getString("procesador"),
                        rs.getString("memoria"),
                        rs.getString("almacenamiento"),
                        rs.getString("fecha_lanzamiento"),
                        rs.getString("imagen_de_la_consola")
                );
                consolas.add(consola);
            }

            Logger.info("Consulta completada. Total de consolas encontradas: " + consolas.size());

        } catch (SQLException e) {
            Logger.error("Error al obtener consolas: " + e.getMessage());
        }

        return consolas;
    }

    public static List<Consola> buscarPorNombre(String nombre) {
        List<Consola> resultado = new ArrayList<>();
        String sql = "SELECT * FROM consolas WHERE nombre LIKE ? ORDER BY nombre ASC;";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Consola consola = new Consola(
                            rs.getInt("id_consolas"),
                            rs.getString("nombre"),
                            rs.getString("anio"),
                            rs.getString("fabricante"),
                            rs.getString("generacion"),
                            rs.getString("region"),
                            rs.getString("tipo"),
                            rs.getString("procesador"),
                            rs.getString("memoria"),
                            rs.getString("almacenamiento"),
                            rs.getString("fecha_lanzamiento"),
                            rs.getString("imagen_de_la_consola")
                    );
                    resultado.add(consola);
                }
            }

            Logger.info("Búsqueda realizada con el texto '" + nombre + "'. Resultados: " + resultado.size());

        } catch (SQLException e) {
            Logger.error("Error en la búsqueda de consolas: " + e.getMessage());
        }

        return resultado;
    }

    public static int contarConsolas() {
        String sql = "SELECT COUNT(*) AS total FROM consolas;";
        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int total = rs.getInt("total");
                Logger.info("Total de consolas en la base de datos: " + total);
                return total;
            }

        } catch (SQLException e) {
            Logger.error("Error al contar las consolas: " + e.getMessage());
        }
        return 0;
    }

    public static boolean insertarConsola(Consola consola) {
        String sql = "INSERT INTO consolas (nombre, anio, fabricante, generacion, region, tipo, procesador, memoria, almacenamiento, fecha_lanzamiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, consola.getNombre());
            pstmt.setString(2, consola.getAnio());
            pstmt.setString(3, consola.getFabricante());
            pstmt.setString(4, consola.getGeneracion());
            pstmt.setString(5, consola.getRegion());
            pstmt.setString(6, consola.getTipo());
            pstmt.setString(7, consola.getProcesador());
            pstmt.setString(8, consola.getMemoria());
            pstmt.setString(9, consola.getAlmacenamiento());
            pstmt.setString(10, consola.getFechaLanzamiento());

            int filasAfectadas = pstmt.executeUpdate();
            Logger.info("Consola insertada correctamente.");
            return filasAfectadas > 0;

        } catch (SQLException e) {
            Logger.error("Error al insertar consola: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarConsola(Consola consola) {
        String sql = "UPDATE consolas SET nombre = ?, anio = ?, fabricante = ?, generacion = ?, region = ?, tipo = ?, procesador = ?, memoria = ?, almacenamiento = ?, fecha_lanzamiento = ?, imagen_de_la_consola = ? "
                + "WHERE id_consolas = ?;";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, consola.getNombre());
            pstmt.setString(2, consola.getAnio());
            pstmt.setString(3, consola.getFabricante());
            pstmt.setString(4, consola.getGeneracion());
            pstmt.setString(5, consola.getRegion());
            pstmt.setString(6, consola.getTipo());
            pstmt.setString(7, consola.getProcesador());
            pstmt.setString(8, consola.getMemoria());
            pstmt.setString(9, consola.getAlmacenamiento());
            pstmt.setString(10, consola.getFechaLanzamiento());
            pstmt.setString(11, consola.getImagen()); // ✅ AÑADIDO
            pstmt.setInt(12, consola.getId());         // ✅ SE MUEVE AL FINAL

            int filasAfectadas = pstmt.executeUpdate();
            Logger.info("Consola actualizada correctamente.");
            return filasAfectadas > 0;

        } catch (SQLException e) {
            Logger.error("Error al actualizar consola: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarConsola(int id) {
        String sql = "DELETE FROM consolas WHERE id_consolas = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            Logger.info("Consola eliminada con ID: " + id);
            return filasAfectadas > 0;

        } catch (SQLException e) {
            Logger.error("Error al eliminar consola: " + e.getMessage());
            return false;
        }
    }

}
