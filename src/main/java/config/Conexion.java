package config;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Ruta base para la carpeta principal de "diariodevideojuegos"
    private static final String mainDirectoryPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos").toString();

    // Ruta para la base de datos
    private static final String dbPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "Config", "Database", "midiario.db").toString();
    private static final String url = "jdbc:sqlite:" + dbPath;

    // Ruta base donde se almacenarán las imágenes de los juegos
    public static final String imagenesPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "imagenes", "juegos").toString();

    // Ruta base donde se almacenarán los videos de los juegos
    public static final String videosPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "videos", "juegos").toString();

    // Ruta base donde se almacenarán las imágenes de overlay para cada juego
    public static final String overlaysPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "overlays", "juegos").toString();

    // Método para obtener la URL de la base de datos
    public static String getUrl() {
        return url;  // Devuelve la URL completa para conectar con la base de datos SQLite
    }

    // Método para obtener la conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(getUrl());  // Conectar con la base de datos
    }

    // Método para borrar el directorio principal "diariodevideojuegos" y todo su contenido
    public static void borrarDirectorioPrincipal() {
        Path directory = Paths.get(mainDirectoryPath);
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            try {
                // Utilizamos Files.walkFileTree para eliminar todos los archivos y subdirectorios
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);  // Eliminar archivo
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);  // Eliminar directorio vacío
                        return FileVisitResult.CONTINUE;
                    }
                });
                AppLogger.info("Directorio 'diariodevideojuegos' y su contenido han sido eliminados.");
            } catch (IOException e) {
                AppLogger.severe("Error al eliminar el directorio principal: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            AppLogger.warning("El directorio 'diariodevideojuegos' no existe o ya ha sido eliminado.");
        }
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
            nombreImagen = generarNombreUnico(imagen);  // Crear un nombre único
            destinoPath = Paths.get(imagenesPath, nombreImagen);
            destino = destinoPath.toFile();
        }

        try {
            // Copiar la imagen al directorio de destino
            Files.copy(imagen.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING); // Usamos Files.copy() para copiar el archivo
            AppLogger.info("Imagen copiada correctamente: " + destinoPath.toString());
        } catch (IOException e) {
            AppLogger.severe("Error al copiar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;  // Si ocurre un error, retornamos null
        }

        return nombreImagen;  // Solo guardamos y retornamos el nombre del archivo
    }

    // Método para guardar el video del juego
    public static String guardarVideo(File video) {
        // Verificar si el archivo de video existe
        if (!video.exists()) {
            AppLogger.severe("El archivo de video no existe: " + video.getAbsolutePath());
            return null;
        }

        // Crear directorio si no existe
        File directorio = new File(videosPath);
        if (!directorio.exists()) {
            boolean dirCreado = directorio.mkdirs();  // Crea los directorios necesarios si no existen
            if (!dirCreado) {
                AppLogger.severe("No se pudo crear el directorio de videos: " + videosPath);
                return null;
            }
        }

        // Obtener el nombre del archivo de video
        String nombreVideo = video.getName();

        // Crear la ruta de destino para el video
        Path destinoPath = Paths.get(videosPath, nombreVideo);
        File destino = destinoPath.toFile();

        // Verificar si el archivo ya existe
        if (destino.exists()) {
            nombreVideo = generarNombreUnico(video);  // Crear un nombre único
            destinoPath = Paths.get(videosPath, nombreVideo);
            destino = destinoPath.toFile();
        }

        try {
            // Copiar el video al directorio de destino
            Files.copy(video.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
            AppLogger.info("Video copiado correctamente: " + destinoPath.toString());
        } catch (IOException e) {
            AppLogger.severe("Error al copiar el video: " + e.getMessage());
            e.printStackTrace();
            return null;  // Si ocurre un error, retornamos null
        }

        return nombreVideo;  // Solo guardamos y retornamos el nombre del archivo
    }

    // Método para guardar la imagen de overlay del juego
    public static String guardarOverlay(File overlay) {
        // Verificar si el archivo de overlay existe
        if (!overlay.exists()) {
            AppLogger.severe("El archivo de overlay no existe: " + overlay.getAbsolutePath());
            return null;
        }

        // Crear directorio si no existe
        File directorio = new File(overlaysPath);
        if (!directorio.exists()) {
            boolean dirCreado = directorio.mkdirs();  // Crea los directorios necesarios si no existen
            if (!dirCreado) {
                AppLogger.severe("No se pudo crear el directorio de overlays: " + overlaysPath);
                return null;
            }
        }

        // Obtener el nombre del archivo de overlay
        String nombreOverlay = overlay.getName();

        // Crear la ruta de destino para el overlay
        Path destinoPath = Paths.get(overlaysPath, nombreOverlay);
        File destino = destinoPath.toFile();

        // Verificar si el archivo ya existe
        if (destino.exists()) {
            nombreOverlay = generarNombreUnico(overlay);  // Crear un nombre único
            destinoPath = Paths.get(overlaysPath, nombreOverlay);
            destino = destinoPath.toFile();
        }

        try {
            // Copiar el overlay al directorio de destino
            Files.copy(overlay.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
            AppLogger.info("Overlay copiado correctamente: " + destinoPath.toString());
        } catch (IOException e) {
            AppLogger.severe("Error al copiar el overlay: " + e.getMessage());
            e.printStackTrace();
            return null;  // Si ocurre un error, retornamos null
        }

        return nombreOverlay;  // Solo guardamos y retornamos el nombre del archivo
    }

    // Método auxiliar para generar un nombre único para el archivo (si ya existe)
    private static String generarNombreUnico(File archivo) {
        String baseName = archivo.getName().substring(0, archivo.getName().lastIndexOf('.'));
        String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.'));
        String uniqueName = baseName + "_" + System.currentTimeMillis() + extension;  // Nombre único basado en timestamp
        return uniqueName;
    }
}
