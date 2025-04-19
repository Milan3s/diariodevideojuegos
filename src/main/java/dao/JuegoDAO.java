package dao;

import config.Database;
import config.Logger;
import models.Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JuegoDAO {

    public static List<Juego> obtenerTodos() {
        List<Juego> juegos = new ArrayList<>();

        String sql = "SELECT j.id_juegos, j.nombre, j.anio, c.nombre AS consola, e.nombre AS estado, " +
                     "ji.descripcion, ji.fecha_inicio, ji.fecha_fin, " +
                     "ji.intentos, ji.creditos, ji.puntuacion " +
                     "FROM juegos j " +
                     "JOIN consolas c ON j.id_consolas = c.id_consolas " +
                     "JOIN estados e ON j.id_estados = e.id_estados " +
                     "LEFT JOIN juegos_informacion ji ON ji.id_juegos = j.id_juegos " +
                     "ORDER BY j.nombre ASC";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Juego juego = new Juego(
                        rs.getInt("id_juegos"),
                        rs.getString("nombre"),
                        rs.getInt("anio"),
                        rs.getString("consola"),
                        rs.getString("estado"),
                        rs.getString("descripcion"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin"),
                        rs.getInt("intentos"),
                        rs.getInt("creditos"),
                        rs.getInt("puntuacion")
                );
                juegos.add(juego);
            }

            Logger.success("Juegos cargados correctamente desde la base de datos.");

        } catch (Exception e) {
            Logger.error("Error al obtener los juegos: " + e.getMessage());
        }

        return juegos;
    }
}
