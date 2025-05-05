package config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Ruta para la base de datos
    private static final String dbPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "Config", "Database", "midiario.db").toString();
    private static final String url = "jdbc:sqlite:" + dbPath;

    // Ruta base donde se almacenarán las imágenes de los juegos
    public static final String imagenesPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "imagenes", "juegos").toString();

    // Método para obtener la URL de la base de datos
    public static String getUrl() {
        return url;  // Devuelve la URL completa para conectar con la base de datos SQLite
    }

    // Método para obtener la conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(getUrl());  // Conectar con la base de datos
    }

    // Método para guardar la imagen del juego
    public static String guardarImagen(File imagen) {
        // Verificar si el archivo de imagen existe
        if (!imagen.exists()) {
            AppLogger.severe("El archivo de imagen no existe: " + imagen.getAbsolutePath());
            return null;
        }

        // Crear directorio si no existe
        File directorio = new File(imagenesPath);
        if (!directorio.exists()) {
            boolean dirCreado = directorio.mkdirs();  // Crea los directorios necesarios si no existen
            if (!dirCreado) {
                AppLogger.severe("No se pudo crear el directorio de imágenes: " + imagenesPath);
                return null;
            }
        }

        // Obtener el nombre del archivo de la imagen
        String nombreImagen = imagen.getName();

        // Crear la ruta de destino para la imagen
        Path destinoPath = Paths.get(imagenesPath, nombreImagen);
        File destino = destinoPath.toFile();

        // Verificar si el archivo ya existe
        if (destino.exists()) {
            // Si deseas reemplazar el archivo existente, descomenta la siguiente línea
            // Files.delete(destinoPath); // Eliminar archivo existente
            // O si prefieres renombrar el archivo, puedes usar un nombre único
            nombreImagen = generarNombreUnico(imagen);  // Crear un nombre único
            destinoPath = Paths.get(imagenesPath, nombreImagen);
            destino = destinoPath.toFile();
        }

        try {
            // Mover la imagen al directorio de destino
            Files.move(imagen.toPath(), destinoPath); // Usamos Files.move() para mover el archivo
            AppLogger.info("Imagen movida correctamente: " + destinoPath.toString());
        } catch (IOException e) {
            AppLogger.severe("Error al mover la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;  // Si ocurre un error, retornamos null
        }

        // Retornar solo el nombre de la imagen, no la ruta completa
        return nombreImagen;  // Solo guardamos y retornamos el nombre del archivo
    }

    // Método auxiliar para generar un nombre único para la imagen (si el archivo ya existe)
    private static String generarNombreUnico(File imagen) {
        String baseName = imagen.getName().substring(0, imagen.getName().lastIndexOf('.'));
        String extension = imagen.getName().substring(imagen.getName().lastIndexOf('.'));
        String uniqueName = baseName + "_" + System.currentTimeMillis() + extension;  // Nombre único basado en timestamp
        return uniqueName;
    }
}
