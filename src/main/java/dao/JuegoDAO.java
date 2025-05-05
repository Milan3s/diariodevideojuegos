package dao;

import config.AppLogger;
import config.Conexion;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Consola;
import models.Estado;
import models.Juego;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JuegoDAO {

    // Insertar un juego con su relación a consola
    public boolean insertarJuego(Juego juego) {
        String sqlJuego = "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlRelacion = "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (?, ?)";

        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmt = conn.prepareStatement(sqlJuego, PreparedStatement.RETURN_GENERATED_KEYS)) {
                // Insertar juego
                stmt.setString(1, juego.getNombre());
                stmt.setString(2, juego.getDescripcion());
                stmt.setString(3, juego.getDesarrollador());
                stmt.setString(4, juego.getEditor());
                stmt.setString(5, juego.getGenero());
                stmt.setString(6, juego.getModoJuego());
                stmt.setString(7, juego.getFechaLanzamiento());
                if (juego.getEstado() != null) {
                    stmt.setInt(8, juego.getEstado().getId()); // Obtener el ID del estado
                } else {
                    stmt.setNull(8, java.sql.Types.INTEGER);
                }
                stmt.setBoolean(9, juego.isEsRecomendado());
                
                // Solo el nombre de la imagen, no la ruta completa
                String nombreImagen = juego.getImagen() != null ? new File(juego.getImagen()).getName() : null;
                stmt.setString(10, nombreImagen); // Guardar solo el nombre de la imagen

                int filas = stmt.executeUpdate(); // Ejecuta la inserción

                if (filas > 0) {
                    // Obtener el ID generado del juego
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idJuego = rs.getInt(1);

                            // Relación con consola
                            try (PreparedStatement relStmt = conn.prepareStatement(sqlRelacion)) {
                                if (juego.getConsola() != null) {
                                    relStmt.setInt(1, idJuego);
                                    relStmt.setInt(2, juego.getConsola().getId());  // Obtener el ID de la consola
                                    relStmt.executeUpdate();
                                }
                            }
                        }
                    }

                    conn.commit(); // Si todo va bien, commit de la transacción
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback si algo falla
                AppLogger.severe("Error al insertar juego: " + e.getMessage());
            }

        } catch (SQLException e) {
            AppLogger.severe("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Obtener todos los juegos junto con su estado y consola
    public ObservableList<Juego> obtenerTodos() {
        ObservableList<Juego> lista = FXCollections.observableArrayList();

        String sql = "SELECT j.id_juegos, j.nombre, j.descripcion, j.desarrollador, j.editor, "
                + "j.genero, j.modo_juego, j.fecha_lanzamiento, j.es_recomendado, j.imagen, "
                + "e.id_estado, e.nombre AS nombre_estado, "
                + "c.id_consola, c.nombre AS nombre_consola "
                + "FROM juegos j "
                + "LEFT JOIN estados e ON j.id_estado = e.id_estado "
                + "LEFT JOIN juegos_consolas jc ON j.id_juegos = jc.id_juego "
                + "LEFT JOIN consolas c ON jc.id_consola = c.id_consola "
                + "ORDER BY j.nombre ASC";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Juego juego = new Juego();
                juego.setId(rs.getInt("id_juegos"));
                juego.setNombre(rs.getString("nombre"));
                juego.setDescripcion(rs.getString("descripcion"));
                juego.setDesarrollador(rs.getString("desarrollador"));
                juego.setEditor(rs.getString("editor"));
                juego.setGenero(rs.getString("genero"));
                juego.setModoJuego(rs.getString("modo_juego"));
                juego.setFechaLanzamiento(rs.getString("fecha_lanzamiento"));
                juego.setEsRecomendado(rs.getBoolean("es_recomendado"));
                juego.setImagen(rs.getString("imagen")); // Solo el nombre de la imagen

                // Relacionados con Estado y Consola
                Estado estado = new Estado(rs.getInt("id_estado"), rs.getString("nombre_estado"));
                Consola consola = new Consola(rs.getInt("id_consola"), rs.getString("nombre_consola"));

                juego.setEstado(estado);
                juego.setConsola(consola);

                lista.add(juego);
            }

        } catch (SQLException e) {
            AppLogger.severe("Error al obtener juegos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }
}
