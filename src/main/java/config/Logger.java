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

    // ANSI Colors + Emojis Unicode
    public static class AnsiColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";

        // Emojis
        public static final String INFO_ICON = "\u2139\uFE0F";     // ℹ️
        public static final String WARN_ICON = "\u26A0\uFE0F";     // ⚠️
        public static final String ERROR_ICON = "\u274C";          // ❌
        public static final String OK_ICON = "\u2705";             // ✅
    }

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

    public static void info(String msg) {
        String log = format("INFO", msg);
        System.out.println(AnsiColors.CYAN + AnsiColors.INFO_ICON + " " + log + AnsiColors.RESET);
        writeToFile("[INFO] " + msg);
    }

    public static void warn(String msg) {
        String log = format("WARN", msg);
        System.out.println(AnsiColors.YELLOW + AnsiColors.WARN_ICON + " " + log + AnsiColors.RESET);
        writeToFile("[WARN] " + msg);
    }

    public static void error(String msg) {
        String log = format("ERROR", msg);
        System.err.println(AnsiColors.RED + AnsiColors.ERROR_ICON + " " + log + AnsiColors.RESET);
        writeToFile("[ERROR] " + msg);
    }

    private static String format(String level, String msg) {
        return String.format("[%s] %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), msg);
    }
}