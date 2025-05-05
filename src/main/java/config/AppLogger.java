package config;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class AppLogger {  // Cambié el nombre de Logger a AppLogger

    // Usamos la clase Logger de Java para la gestión de logs
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    static {
        try {
            // Crear el FileHandler para registrar los logs en un archivo
            FileHandler fileHandler = new FileHandler(System.getProperty("user.home") + "/Documents/diariodevideojuegos/logs/app.log", true);
            fileHandler.setFormatter(new SimpleFormatter()); // Usar un formato simple
            logger.addHandler(fileHandler);

            // Configurar un ConsoleHandler para mostrar logs en la consola
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);  // Mostrar solo INFO y niveles más altos en consola
            logger.addHandler(consoleHandler);

            // Establecer el nivel de logging (INFO, SEVERE, WARNING, etc.)
            logger.setLevel(Level.ALL); // Capturar todos los niveles de logs

        } catch (IOException e) {
            System.err.println("No se pudo configurar el logger: " + e.getMessage());
        }
    }

    // Métodos para diferentes niveles de log
    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    public static void warning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void severe(String message) {
        logger.log(Level.SEVERE, message);
    }

    public static void config(String message) {
        logger.log(Level.CONFIG, message);
    }

    public static void fine(String message) {
        logger.log(Level.FINE, message);
    }

    public static void finer(String message) {
        logger.log(Level.FINER, message);
    }

    public static void finest(String message) {
        logger.log(Level.FINEST, message);
    }
}
