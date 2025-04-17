package config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger personalizado para registrar errores y mensajes en consola y en un archivo local.
 * Ruta por defecto: carpeta del usuario/.diariodevideojuegos/logs.log
 */
public class Logger {

    private static final Path LOG_DIR = Paths.get(System.getProperty("user.home"), ".diariodevideojuegos");
    private static final Path LOG_FILE = LOG_DIR.resolve("logs.log");

    static {
        try {
            if (!Files.exists(LOG_DIR)) {
                Files.createDirectories(LOG_DIR);
            }
        } catch (IOException e) {
            System.err.println("Error creando el directorio de logs: " + e.getMessage());
        }
    }

    private static void writeToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE.toFile(), true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo de log: " + e.getMessage());
        }
    }

    private static String format(String level, String msg) {
        return String.format("[%s] [%s] %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), level, msg);
    }

    public static void info(String msg) {
        String log = format("INFO", msg);
        System.out.println(log);
        writeToFile(log);
    }

    public static void warn(String msg) {
        String log = format("WARN", msg);
        System.out.println(log);
        writeToFile(log);
    }

    public static void error(String msg) {
        String log = format("ERROR", msg);
        System.err.println(log);
        writeToFile(log);
    }

    public static void success(String msg) {
        String log = format("SUCCESS", msg);
        System.out.println(log);
        writeToFile(log);
    }
}
