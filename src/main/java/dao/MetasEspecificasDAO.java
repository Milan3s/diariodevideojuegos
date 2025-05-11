package dao;

import config.Conexion;
import models.MetasEspecificas;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MetasEspecificasDAO {

    public List<MetasEspecificas> obtenerTodas(Integer anio, String filtro) {
        List<MetasEspecificas> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT m.*, c.nombre AS consola_nombre "
                + "FROM metas_especificas m "
                + "LEFT JOIN consolas c ON m.id_consola = c.id_consola WHERE 1=1 ");

        if (anio != null) {
            sql.append("AND strftime('%Y', m.fecha_inicio) = ? ");
        }
        if (filtro != null && !filtro.isEmpty()) {
            sql.append("AND LOWER(m.descripcion) LIKE ? ");
        }
        sql.append("ORDER BY m.fecha_inicio ASC");

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (anio != null) {
                stmt.setString(index++, anio.toString());
            }
            if (filtro != null && !filtro.isEmpty()) {
                stmt.setString(index, "%" + filtro.toLowerCase() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(extraerMeta(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<String[]> obtenerConsolas() {
        List<String[]> consolas = new ArrayList<>();
        String sql = "SELECT id_consola, nombre FROM consolas ORDER BY nombre ASC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                consolas.add(new String[]{
                    String.valueOf(rs.getInt("id_consola")),
                    rs.getString("nombre")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consolas;
    }

    public List<Integer> obtenerAniosDisponibles() {
        List<Integer> anios = new ArrayList<>();
        String sql = "SELECT DISTINCT strftime('%Y', fecha_inicio) AS anio FROM metas_especificas ORDER BY anio DESC";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                anios.add(rs.getInt("anio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anios;
    }

    public void insertar(MetasEspecificas meta) {
        String sql = "INSERT INTO metas_especificas "
                + "(descripcion, juegos_objetivo, juegos_completados, cumplida, fecha_inicio, fecha_fin, fecha_registro, fabricante, id_consola) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setInt(2, meta.getJuegosObjetivo());
            stmt.setInt(3, meta.getJuegosCompletados());
            stmt.setBoolean(4, meta.isCumplida());
            stmt.setString(5, meta.getFechaInicio().toString());
            stmt.setString(6, meta.getFechaFin().toString());
            stmt.setString(7, meta.getFechaRegistro().toString());
            stmt.setString(8, meta.getFabricante());
            stmt.setInt(9, meta.getConsolaId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(MetasEspecificas meta) {
        String sql = "UPDATE metas_especificas SET "
                + "descripcion = ?, juegos_objetivo = ?, juegos_completados = ?, cumplida = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, fecha_registro = ?, fabricante = ?, id_consola = ? "
                + "WHERE id_meta_especifica = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescripcion());
            stmt.setInt(2, meta.getJuegosObjetivo());
            stmt.setInt(3, meta.getJuegosCompletados());
            stmt.setBoolean(4, meta.isCumplida());
            stmt.setString(5, meta.getFechaInicio().toString());
            stmt.setString(6, meta.getFechaFin().toString());
            stmt.setString(7, meta.getFechaRegistro().toString());
            stmt.setString(8, meta.getFabricante());
            stmt.setInt(9, meta.getConsolaId());
            stmt.setInt(10, meta.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM metas_especificas WHERE id_meta_especifica = ?";

        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private MetasEspecificas extraerMeta(ResultSet rs) throws SQLException {
        return new MetasEspecificas(
                rs.getInt("id_meta_especifica"),
                rs.getString("descripcion"),
                rs.getInt("juegos_objetivo"),
                rs.getInt("juegos_completados"),
                rs.getBoolean("cumplida"),
                rs.getString("fabricante"),
                rs.getInt("id_consola"),
                rs.getString("consola_nombre"),
                LocalDate.parse(rs.getString("fecha_inicio").substring(0, 10)),
                LocalDate.parse(rs.getString("fecha_fin").substring(0, 10)),
                LocalDate.parse(rs.getString("fecha_registro").substring(0, 10))
        );
    }

    public String[] obtenerUltimaConsola() {
        String sql = "SELECT id_consola, nombre FROM consolas ORDER BY id_consola DESC LIMIT 1";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("id_consola")),
                    rs.getString("nombre")
                };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
