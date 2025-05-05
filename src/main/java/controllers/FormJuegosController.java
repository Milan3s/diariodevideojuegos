package controllers;

import dao.ComboDAO;
import dao.JuegoDAO;
import models.Consola;
import models.Estado;
import models.Juego;
import config.Conexion;
import config.AppLogger;

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
    private Juego juegoEditando;

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

        // Mantener la imagen existente si no se selecciona una nueva
        String nombreImagen = null;
        if (imagenSeleccionada != null) {
            // Utilizar la clase Conexion para guardar la nueva imagen si se seleccionó
            nombreImagen = Conexion.guardarImagen(imagenSeleccionada); // Guardamos la imagen en la carpeta
            if (nombreImagen == null) {
                mostrarAlerta("Error al guardar la imagen.");
                AppLogger.severe("Error al guardar la imagen seleccionada.");
                return;
            }
        } else if (juegoEditando != null) {
            // Si no se selecciona una nueva imagen, mantener la imagen original
            nombreImagen = juegoEditando.getImagen();
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

        JuegoDAO dao = new JuegoDAO();
        boolean exito;
        if (juegoEditando == null) {
            // Nuevo juego, insertar
            exito = dao.insertarJuego(juego);
        } else {
            // Editar juego existente
            juego.setId(juegoEditando.getId());
            exito = dao.actualizarJuego(juego);  // Debes implementar este método en JuegoDAO
        }

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

    // Método que llena el formulario con los datos de un juego seleccionado para editar
    public void cargarJuegoParaEditar(Juego juegoSeleccionado) {
        juegoEditando = juegoSeleccionado;

        // Llenar los campos con los valores del juego seleccionado
        txtNombre.setText(juegoSeleccionado.getNombre());
        txtDescripcion.setText(juegoSeleccionado.getDescripcion());
        txtDesarrollador.setText(juegoSeleccionado.getDesarrollador());
        txtEditor.setText(juegoSeleccionado.getEditor());
        txtGenero.setText(juegoSeleccionado.getGenero());
        txtModoJuego.setText(juegoSeleccionado.getModoJuego());
        dateFechaLanzamiento.setValue(juegoSeleccionado.getFechaLanzamiento() != null
                ? java.time.LocalDate.parse(juegoSeleccionado.getFechaLanzamiento())
                : null);

        // Configurar estado y consola
        comboEstado.setValue(juegoSeleccionado.getEstado());
        comboConsola.setValue(juegoSeleccionado.getConsola());

        // Configurar imagen si existe
        if (juegoSeleccionado.getImagen() != null && !juegoSeleccionado.getImagen().isEmpty()) {
            File imageFile = new File(Conexion.imagenesPath, juegoSeleccionado.getImagen());
            if (imageFile.exists()) {
                imgPreview.setImage(new Image(imageFile.toURI().toString()));
            }
        }

        // Configurar el estado del radio button
        if (juegoSeleccionado.isEsRecomendado()) {
            radioSi.setSelected(true);
        } else {
            radioNo.setSelected(true);
        }
    }
}
