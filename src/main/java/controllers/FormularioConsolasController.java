package controllers;

import config.Database;
import dao.ConsolaDAO;
import models.Consola;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * Controlador del formulario para añadir o editar consolas.
 */
public class FormularioConsolasController {

    private Consola consolaEditando;
    private String imagenTemporal = null;

    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_anio;
    @FXML
    private TextField txt_fabricante;
    @FXML
    private TextField txt_generacion;
    @FXML
    private TextField txt_region;
    @FXML
    private TextField txt_tipo;
    @FXML
    private TextField txt_procesador;
    @FXML
    private TextField txt_memoria;
    @FXML
    private TextField txt_almacenamiento;
    @FXML
    private TextField txt_fecha_lanzamiento;
    @FXML
    private Button btn_cambiar_imagen;
    @FXML
    private Button btn_guardar_consola;
    @FXML
    private ImageView imagen_consola_form;

    public void setConsolaAEditar(Consola consola) {
        this.consolaEditando = consola;

        if (consola != null) {
            txt_nombre.setText(consola.getNombre());
            txt_anio.setText(consola.getAnio());
            txt_fabricante.setText(consola.getFabricante());
            txt_generacion.setText(consola.getGeneracion());
            txt_region.setText(consola.getRegion());
            txt_tipo.setText(consola.getTipo());
            txt_procesador.setText(consola.getProcesador());
            txt_memoria.setText(consola.getMemoria());
            txt_almacenamiento.setText(consola.getAlmacenamiento());
            txt_fecha_lanzamiento.setText(consola.getFechaLanzamiento());

            cargarImagenConsola(consola.getImagen());
        }
    }

    private void cargarImagenConsola(String nombreArchivo) {
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            Path ruta = Database.getImagesDir().resolve(nombreArchivo);
            File archivo = ruta.toFile();

            if (archivo.exists()) {
                imagen_consola_form.setImage(new Image(archivo.toURI().toString()));
            } else {
                imagen_consola_form.setImage(null);
                System.out.println("Imagen no encontrada: " + ruta);
            }
        } else {
            imagen_consola_form.setImage(null);
        }
    }

    @FXML
    private void accion_guardar_consola(ActionEvent event) {
        String nombre = txt_nombre.getText();
        String anio = txt_anio.getText();
        String fabricante = txt_fabricante.getText();
        String generacion = txt_generacion.getText();
        String region = txt_region.getText();
        String tipo = txt_tipo.getText();
        String procesador = txt_procesador.getText();
        String memoria = txt_memoria.getText();
        String almacenamiento = txt_almacenamiento.getText();
        String fechaLanzamiento = txt_fecha_lanzamiento.getText();

        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("El campo 'Nombre' no puede estar vacío.");
            return;
        }

        if (consolaEditando == null) {
            // Nueva consola
            Consola nueva = new Consola(0, nombre, anio, fabricante, generacion, region, tipo,
                    procesador, memoria, almacenamiento, fechaLanzamiento, imagenTemporal);

            if (ConsolaDAO.insertarConsola(nueva)) {
                mostrarAlerta("Consola añadida correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error al añadir la consola.");
            }
        } else {
            // Edición
            consolaEditando.setNombre(nombre);
            consolaEditando.setAnio(anio);
            consolaEditando.setFabricante(fabricante);
            consolaEditando.setGeneracion(generacion);
            consolaEditando.setRegion(region);
            consolaEditando.setTipo(tipo);
            consolaEditando.setProcesador(procesador);
            consolaEditando.setMemoria(memoria);
            consolaEditando.setAlmacenamiento(almacenamiento);
            consolaEditando.setFechaLanzamiento(fechaLanzamiento);

            if (ConsolaDAO.actualizarConsola(consolaEditando)) {
                mostrarAlerta("Consola actualizada correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error al actualizar la consola.");
            }
        }

        // Cerrar la ventana
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void accion_cambiar_imagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File archivoSeleccionado = fileChooser.showOpenDialog(btn_cambiar_imagen.getScene().getWindow());

        if (archivoSeleccionado != null) {
            try {
                // Copiar imagen al directorio de imágenes del sistema
                Path destino = Database.getImagesDir().resolve(archivoSeleccionado.getName());
                Files.copy(archivoSeleccionado.toPath(), destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                imagen_consola_form.setImage(new Image(destino.toUri().toString()));

                if (consolaEditando != null) {
                    consolaEditando.setImagen(archivoSeleccionado.getName());
                } else {
                    imagenTemporal = archivoSeleccionado.getName();
                }

            } catch (IOException e) {
                mostrarAlerta("No se pudo copiar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void mostrarAlerta(String mensaje) {
        mostrarAlerta(mensaje, Alert.AlertType.WARNING);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
