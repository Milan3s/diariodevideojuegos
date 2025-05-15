package config;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Ruta base principal
    private static final String mainDirectoryPath = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos").toString();

    // Rutas principales
    private static final String recursosPath = Paths.get(mainDirectoryPath, "recursos").toString();
    private static final String diarioPath = Paths.get(recursosPath, "diario").toString();
    private static final String votosPath = Paths.get(recursosPath, "votos").toString();

    // Rutas específicas de recursos
    public static final String imagenesJuegosDiarioPath = Paths.get(diarioPath, "imagenes", "juegos").toString();
    public static final String imagenesConsolaDiarioPath = Paths.get(diarioPath, "imagenes", "consola").toString();
    public static final String videosJuegosDiarioPath = Paths.get(diarioPath, "videos", "juegos").toString();

    public static final String imagenesVotosPath = Paths.get(votosPath, "imagenes").toString();

    // Ruta a la base de datos
    private static final String dbPath = Paths.get(mainDirectoryPath, "Config", "Database", "midiario.db").toString();
    private static final String url = "jdbc:sqlite:" + dbPath;

    // Obtener URL de conexión
    public static String getUrl() {
        return url;
    }

    // Obtener conexión
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    // Borrar todo el directorio principal
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

    // Guardar imagen en una ruta dada
    public static String guardarImagen(File imagen, String destinoBasePath) {
        if (!imagen.exists()) {
            AppLogger.severe("El archivo de imagen no existe: " + imagen.getAbsolutePath());
            return null;
        }

        File directorio = new File(destinoBasePath);
        if (!directorio.exists() && !directorio.mkdirs()) {
            AppLogger.severe("No se pudo crear el directorio de imágenes: " + destinoBasePath);
            return null;
        }

        String nombreImagen = imagen.getName();
        Path destinoPath = Paths.get(destinoBasePath, nombreImagen);
        File destino = destinoPath.toFile();

        if (destino.exists()) {
            nombreImagen = generarNombreUnico(imagen);
            destinoPath = Paths.get(destinoBasePath, nombreImagen);
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

    // Guardar video en una ruta dada
    public static String guardarVideo(File video, String destinoBasePath) {
        if (!video.exists()) {
            AppLogger.severe("El archivo de video no existe: " + video.getAbsolutePath());
            return null;
        }

        File directorio = new File(destinoBasePath);
        if (!directorio.exists() && !directorio.mkdirs()) {
            AppLogger.severe("No se pudo crear el directorio de videos: " + destinoBasePath);
            return null;
        }

        String nombreVideo = video.getName();
        Path destinoPath = Paths.get(destinoBasePath, nombreVideo);
        File destino = destinoPath.toFile();

        if (destino.exists()) {
            nombreVideo = generarNombreUnico(video);
            destinoPath = Paths.get(destinoBasePath, nombreVideo);
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

    // Generar nombre único para archivo existente
    private static String generarNombreUnico(File archivo) {
        String baseName = archivo.getName().substring(0, archivo.getName().lastIndexOf('.'));
        String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.'));
        return baseName + "_" + System.currentTimeMillis() + extension;
    }

    // Guarda imagen en la ruta de consolas del diario
    public static String guardarImagenConsola(File imagen) {
        return guardarImagen(imagen, imagenesConsolaDiarioPath);
    }

// Guarda imagen en la ruta de juegos del diario
    public static String guardarImagenJuego(File imagen) {
        return guardarImagen(imagen, imagenesJuegosDiarioPath);
    }

// Guarda video en la ruta de juegos del diario
    public static String guardarVideoJuego(File video) {
        return guardarVideo(video, videosJuegosDiarioPath);
    }

// Guarda imagen en la ruta de votos
    public static String guardarImagenVoto(File imagen) {
        return guardarImagen(imagen, imagenesVotosPath);
    }

}
