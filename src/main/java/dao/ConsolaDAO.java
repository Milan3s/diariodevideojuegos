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

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
}
