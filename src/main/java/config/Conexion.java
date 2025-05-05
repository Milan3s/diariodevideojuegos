package config;

import java.nio.file.Paths;

public class Conexion {
    private static final String dbPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "Config", "Database", "midiario.db").toString();
    private static final String url = "jdbc:sqlite:" + dbPath;

    public static String getUrl() {
        return url;
    }
}
