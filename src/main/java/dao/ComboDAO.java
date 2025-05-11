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

    // Método para cargar los estados por tipo (incluido moderador)
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

    // Método específico para cargar los estados de los moderadores
    public static ObservableList<Estado> cargarEstadosModeradores() {
        ObservableList<Estado> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_estado, nombre FROM estados WHERE tipo = 'moderador' ORDER BY nombre ASC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Estado(rs.getInt("id_estado"), rs.getString("nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Método para cargar las consolas
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

    // Método para cargar las dificultades de logros
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

    // Método para cargar los estados de consolas
    public static ObservableList<Estado> cargarEstadosConsolas() {
        ObservableList<Estado> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_estado, nombre FROM estados WHERE tipo = 'consola' ORDER BY nombre ASC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Estado(rs.getInt("id_estado"), rs.getString("nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Método para cargar años desde anios_metas_twitch
    public static ObservableList<Integer> cargarAniosMetasTwitch() {
        ObservableList<Integer> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT anio FROM anios_metas_twitch ORDER BY anio DESC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getInt("anio"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<Integer> cargarAniosMejorasCanal() {
        ObservableList<Integer> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT strftime('%Y', fecha_inicio) AS anio FROM mejoras_canal ORDER BY anio DESC";

        try (Connection conn = DriverManager.getConnection(Conexion.getUrl()); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getInt("anio"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    

}
