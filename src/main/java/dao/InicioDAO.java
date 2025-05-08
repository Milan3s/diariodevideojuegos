// dao/InicioDAO.java
package dao;

import config.Conexion;
import models.Inicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InicioDAO {

    public Inicio obtenerResumen() {
        String[] tablas = {
            "SELECT COUNT(*) FROM juegos",
            "SELECT COUNT(*) FROM logros",
            "SELECT COUNT(*) FROM consolas",
            "SELECT COUNT(*) FROM eventos",
            "SELECT COUNT(*) FROM metas_twitch",
            "SELECT SUM(cantidad) FROM seguidores"
        };

        try (Connection conn = Conexion.obtenerConexion()) {
            int[] resultados = new int[tablas.length];
            for (int i = 0; i < tablas.length; i++) {
                try (PreparedStatement stmt = conn.prepareStatement(tablas[i]);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        resultados[i] = rs.getInt(1);
                    }
                }
            }
            return new Inicio(resultados[0], resultados[1], resultados[2], resultados[3], resultados[4], resultados[5]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Inicio(0, 0, 0, 0, 0, 0);
        }
    }
}
