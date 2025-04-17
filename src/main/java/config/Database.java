package config;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DB_NAME = "diariodevideojuegos.db";
    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".diariodevideojuegos", "config");
    private static final Path DB_PATH = CONFIG_DIR.resolve(DB_NAME);
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH.toString();

    public static Connection connect() {
        try {
            // Verifica si existe el directorio
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
                Logger.info("📁 Directorio creado: " + CONFIG_DIR);
            } else {
                Logger.info("📁 Directorio ya existente: " + CONFIG_DIR);
            }

            // Verifica si existe la base de datos
            boolean exists = Files.exists(DB_PATH);

            Connection conn = DriverManager.getConnection(JDBC_URL);

            if (conn != null) {
                if (exists) {
                    Logger.success("🗂️  Conectado a la base de datos existente correctamente.");
                } else {
                    Logger.success("🆕 Base de datos creada y conectada correctamente.");
                }
                return conn;
            }

        } catch (SQLException e) {
            Logger.error("❌ Error de conexión a la base de datos: " + e.getMessage());
        } catch (Exception e) {
            Logger.error("❌ Error al inicializar la base de datos: " + e.getMessage());
        }
        return null;
    }

    public static Path getDatabasePath() {
        return DB_PATH;
    }
}
