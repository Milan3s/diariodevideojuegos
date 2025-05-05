package controllers;

import dao.ComboDAO;
import dao.JuegoDAO;
import models.Consola;
import models.Estado;
import models.Juego;
import config.Conexion;
import config.AppLogger; // Importa la clase de Logger

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FormJuegosController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtDesarrollador;
    @FXML
    private TextField txtEditor;
    @FXML
    private TextField txtGenero;
    @FXML
    private TextField txtModoJuego;

    @FXML
    private DatePicker dateFechaLanzamiento;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private ComboBox<Consola> comboConsola;

    @FXML
    private RadioButton radioSi;
    @FXML
    private RadioButton radioNo;

    @FXML
    private ToggleGroup grupoRecomendado;

    @FXML
    private ImageView imgPreview;

    private File imagenSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración del grupo de RadioButtons
        grupoRecomendado = new ToggleGroup();
        radioSi.setToggleGroup(grupoRecomendado);
        radioNo.setToggleGroup(grupoRecomendado);
        radioSi.setSelected(true); // Se selecciona "Sí" por defecto

        // Cargar datos en ComboBoxes
        comboEstado.setItems(ComboDAO.cargarEstadosPorTipo("juego"));
        comboEstado.setPromptText("Seleccione uno...");

        comboConsola.setItems(ComboDAO.cargarConsolas());
        comboConsola.setPromptText("Seleccione una...");

        // Agregar log para depuración
        AppLogger.info("Formulario de juegos inicializado correctamente.");
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Juego");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            imagenSeleccionada = archivo;
            imgPreview.setImage(new Image(archivo.toURI().toString()));  // Actualiza el ImageView con la imagen seleccionada
            AppLogger.info("Imagen seleccionada: " + archivo.getAbsolutePath());
        }
    }

    @FXML
    private void guardarJuego(ActionEvent event) {
        // Validaciones mínimas: solo el campo "Nombre" es obligatorio
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            AppLogger.warning("El nombre del juego está vacío.");
            return;
        }

        // Validar estado y consola
        Estado estadoSeleccionado = comboEstado.getValue();
        Consola consolaSeleccionada = comboConsola.getValue();
        if (estadoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un estado.");
            AppLogger.warning("No se seleccionó un estado.");
            return;
        }
        if (consolaSeleccionada == null) {
            mostrarAlerta("Debe seleccionar una consola.");
            AppLogger.warning("No se seleccionó una consola.");
            return;
        }

        // Captura de datos
        String descripcion = txtDescripcion.getText().trim();
        String desarrollador = txtDesarrollador.getText().trim();
        String editor = txtEditor.getText().trim();
        String genero = txtGenero.getText().trim();
        String modoJuego = txtModoJuego.getText().trim();
        String fechaLanzamiento = (dateFechaLanzamiento.getValue() != null)
                ? dateFechaLanzamiento.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)
                : null;

        boolean recomendado = radioSi.isSelected();  // "Sí" es true, "No" es false

        // Guardar la imagen en la ruta deseada
        String nombreImagen = null;
        if (imagenSeleccionada != null) {
            // Utilizar la clase Conexion para guardar la imagen
            nombreImagen = Conexion.guardarImagen(imagenSeleccionada); // Guardamos la imagen en la carpeta
            if (nombreImagen == null) {
                mostrarAlerta("Error al guardar la imagen.");
                AppLogger.severe("Error al guardar la imagen seleccionada.");
                return;
            }
        }

        // Crear el objeto Juego
        Juego juego = new Juego();
        juego.setNombre(nombre);
        juego.setDescripcion(descripcion);
        juego.setDesarrollador(desarrollador);
        juego.setEditor(editor);
        juego.setGenero(genero);
        juego.setModoJuego(modoJuego);
        juego.setFechaLanzamiento(fechaLanzamiento);
        juego.setEstado(estadoSeleccionado);
        juego.setConsola(consolaSeleccionada);
        juego.setEsRecomendado(recomendado);
        juego.setImagen(nombreImagen); // Guardar solo el nombre de la imagen

        boolean exito = new JuegoDAO().insertarJuego(juego);

        if (exito) {
            mostrarAlerta("Juego guardado correctamente.");
            AppLogger.info("Juego guardado correctamente: " + nombre);
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al guardar el juego.");
            AppLogger.severe("Error al guardar el juego: " + nombre);
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtDesarrollador.clear();
        txtEditor.clear();
        txtGenero.clear();
        txtModoJuego.clear();
        dateFechaLanzamiento.setValue(null);
        comboEstado.getSelectionModel().clearSelection();
        comboConsola.getSelectionModel().clearSelection();
        grupoRecomendado.selectToggle(radioSi); // Seleccionar "Sí" por defecto
        imgPreview.setImage(null);
        imagenSeleccionada = null;

        AppLogger.info("Formulario de juego limpiado.");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
