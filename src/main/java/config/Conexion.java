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

    // Obtener conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    // Guardar imagen en una ruta base
    public static String guardarImagen(File imagen, String destinoBasePath) {
        if (imagen == null || !imagen.exists()) {
            return null;
        }

        File directorio = new File(destinoBasePath);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        String nombreImagen = generarNombreUnico(imagen);
        Path destinoPath = Paths.get(destinoBasePath, nombreImagen);

        try {
            Files.copy(imagen.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
            return nombreImagen;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Guardar video en una ruta base
    public static String guardarVideo(File video, String destinoBasePath) {
        if (video == null || !video.exists()) {
            return null;
        }

        File directorio = new File(destinoBasePath);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        String nombreVideo = generarNombreUnico(video);
        Path destinoPath = Paths.get(destinoBasePath, nombreVideo);

        try {
            Files.copy(video.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
            return nombreVideo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generar nombre único si ya existe
    private static String generarNombreUnico(File archivo) {
        String baseName = archivo.getName().substring(0, archivo.getName().lastIndexOf('.'));
        String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.'));
        return baseName + "_" + System.currentTimeMillis() + extension;
    }

    // Métodos directos para guardar en rutas específicas
    public static String guardarImagenConsola(File imagen) {
        return guardarImagen(imagen, imagenesConsolaDiarioPath);
    }

    public static String guardarImagenJuego(File imagen) {
        return guardarImagen(imagen, imagenesJuegosDiarioPath);
    }

    public static String guardarVideoJuego(File video) {
        return guardarVideo(video, videosJuegosDiarioPath);
    }

    public static String guardarImagenVoto(File imagen) {
        return guardarImagen(imagen, imagenesVotosPath);
    }

    // Eliminar todo el directorio de trabajo
    public static void borrarDirectorioPrincipal() {
        Path directory = Paths.get(mainDirectoryPath);
        if (!Files.exists(directory)) {
            return;
        }

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<>() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
