package dao;

import config.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Estado;
import models.Moderador;

import java.sql.*;
import java.time.LocalDate;

public class ModeradorDAO {

    public ObservableList<Moderador> obtenerModeradores() {
        ObservableList<Moderador> lista = FXCollections.observableArrayList();
        String sql = "SELECT m.*, e.nombre AS nombre_estado FROM moderadores m "
                + "LEFT JOIN estados e ON m.id_estado = e.id_estado "
                + "ORDER BY m.nombre ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estado estado = null;
                if (rs.getObject("id_estado") != null) {
                    estado = new Estado(rs.getInt("id_estado"), rs.getString("nombre_estado"));
                }

                Moderador mod = new Moderador(
                        rs.getInt("id_moderador"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("fecha_alta"),
                        rs.getString("fecha_baja"),
                        estado
                );
                lista.add(mod);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean insertarModerador(Moderador m) {
        String sql = "INSERT INTO moderadores (nombre, email, fecha_alta, id_estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getEmail());

            // Si no hay fecha, usar la de hoy (YYYY-MM-DD)
            String fechaAlta = m.getFechaAlta() != null ? m.getFechaAlta() : LocalDate.now().toString();
            stmt.setString(3, fechaAlta);

            if (m.getEstado() != null) {
                stmt.setInt(4, m.getEstado().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarModerador(Moderador m) {
        String sql = "UPDATE moderadores SET nombre = ?, email = ?, fecha_alta = ?, fecha_baja = ?, id_estado = ? WHERE id_moderador = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getEmail());
            stmt.setString(3, m.getFechaAlta());
            stmt.setString(4, m.getFechaBaja());
            if (m.getEstado() != null) {
                stmt.setInt(5, m.getEstado().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, m.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarModerador(int id) {
        String sql = "DELETE FROM moderadores WHERE id_moderador = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean readmitirModerador(int id, String nuevaFechaAlta) {
        String sql = "UPDATE moderadores SET fecha_baja = NULL, fecha_alta = ?, "
                + "id_estado = (SELECT id_estado FROM estados WHERE nombre = 'Activo' AND tipo = 'moderador') "
                + "WHERE id_moderador = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevaFechaAlta); // debe ser YYYY-MM-DD
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean darDeBajaModerador(int idModerador) {
        String sql = "UPDATE moderadores SET fecha_baja = DATE('now'), "
                + "id_estado = (SELECT id_estado FROM estados WHERE nombre = 'Dado de baja' AND tipo = 'moderador') "
                + "WHERE id_moderador = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idModerador);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int insertarModeradorYDevolverId(Moderador m) {
        String sql = "INSERT INTO moderadores (nombre, email, fecha_alta, id_estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getEmail());

            String fechaAlta = m.getFechaAlta() != null ? m.getFechaAlta() : LocalDate.now().toString();
            stmt.setString(3, fechaAlta);

            if (m.getEstado() != null) {
                stmt.setInt(4, m.getEstado().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // ID generado
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // fallo
    }

}
