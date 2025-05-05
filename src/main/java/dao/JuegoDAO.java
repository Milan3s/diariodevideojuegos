package dao;

import models.Juego;
import config.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    // 🔍 Obtener todos los juegos (nombre y ID)
    public ObservableList<Juego> obtenerTodos() {
        ObservableList<Juego> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_juegos, nombre FROM juegos ORDER BY nombre ASC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl());
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Juego juego = new Juego(rs.getInt("id_juegos"), rs.getString("nombre"));
                lista.add(juego);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
