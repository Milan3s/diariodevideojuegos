package dao;

import models.Juego;
import config.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JuegoDAO {

    public boolean insertarJuego(Juego juego) {
        String sql = "INSERT INTO juegos (nombre, id_estado) VALUES (?, 1)";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl());
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, juego.getNombre());
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
