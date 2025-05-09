package config;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Ruta base para la carpeta principal
    private static final String mainDirectoryPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos").toString();

    // Ruta a la base de datos
    private static final String dbPath = Paths.get(mainDirectoryPath, "Config", "Database", "midiario.db").toString();
    private static final String url = "jdbc:sqlite:" + dbPath;

    // Ruta de imágenes de juegos
    public static final String imagenesPath = Paths.get(mainDirectoryPath, "imagenes", "juegos").toString();

    // Ruta de videos de juegos
    public static final String videosPath = Paths.get(mainDirectoryPath, "videos", "juegos").toString();

    // Obtener URL de conexión
    public static String getUrl() {
        return url;
    }

    // Obtener conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    // Borrar el directorio principal completo
    public static void borrarDirectorioPrincipal() {
        Path directory = Paths.get(mainDirectoryPath);
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            try {
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
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

    // Guardar imagen de juego
    public static String guardarImagen(File imagen) {
        if (!imagen.exists()) {
            AppLogger.severe("El archivo de imagen no existe: " + imagen.getAbsolutePath());
            return null;
        }

        File directorio = new File(imagenesPath);
        if (!directorio.exists() && !directorio.mkdirs()) {
            AppLogger.severe("No se pudo crear el directorio de imágenes: " + imagenesPath);
            return null;
        }

        String nombreImagen = imagen.getName();
        Path destinoPath = Paths.get(imagenesPath, nombreImagen);
        File destino = destinoPath.toFile();

        if (destino.exists()) {
            nombreImagen = generarNombreUnico(imagen);
            destinoPath = Paths.get(imagenesPath, nombreImagen);
            destino = destinoPath.toFile();
        }

        try {
            Files.copy(imagen.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
            AppLogger.info("Imagen copiada correctamente: " + destinoPath);
        } catch (IOException e) {
            AppLogger.severe("Error al copiar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return nombreImagen;
    }

    // Guardar video de juego
    public static String guardarVideo(File video) {
        if (!video.exists()) {
            AppLogger.severe("El archivo de video no existe: " + video.getAbsolutePath());
            return null;
        }

        File directorio = new File(videosPath);
        if (!directorio.exists() && !directorio.mkdirs()) {
            AppLogger.severe("No se pudo crear el directorio de videos: " + videosPath);
            return null;
        }

        String nombreVideo = video.getName();
        Path destinoPath = Paths.get(videosPath, nombreVideo);
        File destino = destinoPath.toFile();

        if (destino.exists()) {
            nombreVideo = generarNombreUnico(video);
            destinoPath = Paths.get(videosPath, nombreVideo);
            destino = destinoPath.toFile();
        }

        try {
            Files.copy(video.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
            AppLogger.info("Video copiado correctamente: " + destinoPath);
        } catch (IOException e) {
            AppLogger.severe("Error al copiar el video: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return nombreVideo;
    }

    // Generar nombre único si ya existe
    private static String generarNombreUnico(File archivo) {
        String baseName = archivo.getName().substring(0, archivo.getName().lastIndexOf('.'));
        String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.'));
        return baseName + "_" + System.currentTimeMillis() + extension;
    }
}
