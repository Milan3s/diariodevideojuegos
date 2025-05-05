package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Estado;
import models.Consola;
import models.Dificultad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ComboDAO {

    public static ObservableList<Estado> cargarEstadosPorTipo(String tipo) {
        ObservableList<Estado> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_estado, nombre FROM estados WHERE tipo = ? ORDER BY nombre ASC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Estado(rs.getInt("id_estado"), rs.getString("nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<Consola> cargarConsolas() {
        ObservableList<Consola> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_consola, nombre, abreviatura FROM consolas ORDER BY nombre ASC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Crear el objeto Consola con los tres parámetros
                lista.add(new Consola(rs.getInt("id_consola"), rs.getString("nombre"), rs.getString("abreviatura")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<Dificultad> cargarDificultades() {
        ObservableList<Dificultad> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_dificultad, nombre FROM dificultades_logros ORDER BY nombre ASC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Dificultad(rs.getInt("id_dificultad"), rs.getString("nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
