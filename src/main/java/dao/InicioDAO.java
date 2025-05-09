package dao;

import config.Conexion;
import models.Inicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class InicioDAO {

    public Inicio obtenerResumen() {
        int totalJuegos = 0;
        int totalLogros = 0;
        int totalConsolas = 0;
        int totalEventos = 0;
        int totalMetas = 0;
        int totalSeguidores = 0;

        String metaSeguidoresProgreso = "No disponible";
        String metaJuegosCompletadosDescripcion = "No disponible";
        String mejorasDelCanal = "Sin mejoras registradas";
        String fechaExtensible = "No registrada";
        String diasParaExtensible = "No disponible";
        String metaEspecifica = "No hay metas específicas registradas";

        try (Connection conn = Conexion.obtenerConexion()) {

            // Conteos generales
            totalJuegos = ejecutarConteo(conn, "SELECT COUNT(*) FROM juegos");
            totalLogros = ejecutarConteo(conn, "SELECT COUNT(*) FROM logros");
            totalConsolas = ejecutarConteo(conn, "SELECT COUNT(*) FROM consolas");
            totalEventos = ejecutarConteo(conn, "SELECT COUNT(*) FROM eventos");
            totalMetas = ejecutarConteo(conn, "SELECT COUNT(*) FROM metas_twitch");
            totalSeguidores = ejecutarConteo(conn, "SELECT SUM(cantidad) FROM seguidores");

            // Meta de seguidores
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT meta FROM metas_twitch WHERE descripcion LIKE '%seguidores%' LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int meta = rs.getInt("meta");
                    if (meta > 0) {
                        int progreso = Math.min((totalSeguidores * 100) / meta, 100);
                        metaSeguidoresProgreso = totalSeguidores + " / " + meta + " - " + progreso + "%";
                    } else {
                        metaSeguidoresProgreso = "Meta inválida";
                    }
                }
            }

            // Meta de juegos completados
            int juegosCompletados = 0;
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM juegos j JOIN estados e ON j.id_estado = e.id_estado WHERE e.tipo = 'juego' AND e.nombre = 'Completado'");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    juegosCompletados = rs.getInt(1);
                    if (totalJuegos < 50) {
                        metaJuegosCompletadosDescripcion =
                                "Completados: " + juegosCompletados + " / " + totalJuegos +
                                        ". No hay suficientes juegos para alcanzar la meta de 50.";
                    } else if (juegosCompletados >= 50) {
                        metaJuegosCompletadosDescripcion =
                                "Completados: " + juegosCompletados + " / " + totalJuegos + ". ¡Meta alcanzada!";
                    } else {
                        int faltan = 50 - juegosCompletados;
                        metaJuegosCompletadosDescripcion =
                                "Completados: " + juegosCompletados + " / " + totalJuegos +
                                        ". Faltan " + faltan + " para la meta.";
                    }
                }
            }

            // Mejoras del canal (última mejora registrada)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT descripcion FROM mejoras_canal ORDER BY fecha DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mejorasDelCanal = rs.getString("descripcion");
                }
            }

            // Próximo extensible (buscado en metas_twitch por texto relacionado)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT fecha_fin FROM metas_twitch WHERE descripcion LIKE '%extensible%' OR descripcion LIKE '%aniversario%' ORDER BY fecha_fin DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fechaStr = rs.getString("fecha_fin");
                    fechaExtensible = fechaStr;
                    LocalDate fechaEvento = LocalDate.parse(fechaStr);
                    long diasFaltan = ChronoUnit.DAYS.between(LocalDate.now(), fechaEvento);
                    diasParaExtensible = "Faltan " + diasFaltan + " días";
                }
            }

            // Nueva consulta: Meta específica cumplida más reciente
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT descripcion, juegos_completados, juegos_objetivo FROM metas_especificas " +
                            "WHERE cumplida = 1 ORDER BY fecha_fin DESC LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String descripcion = rs.getString("descripcion");
                    int completados = rs.getInt("juegos_completados");
                    int objetivo = rs.getInt("juegos_objetivo");
                    metaEspecifica = String.format("%s (%d/%d)", descripcion, completados, objetivo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Inicio(
                totalJuegos,
                totalLogros,
                totalConsolas,
                totalEventos,
                totalMetas,
                totalSeguidores,
                metaSeguidoresProgreso,
                metaJuegosCompletadosDescripcion,
                mejorasDelCanal,
                fechaExtensible,
                diasParaExtensible,
                metaEspecifica
        );
    }

    private int ejecutarConteo(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
