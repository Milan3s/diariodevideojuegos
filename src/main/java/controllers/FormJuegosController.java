package controllers;

import dao.ComboDAO;
import dao.JuegoDAO;
import models.Consola;
import models.Estado;
import models.Juego;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FormJuegosController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtDesarrollador;
    @FXML private TextField txtEditor;
    @FXML private TextField txtGenero;
    @FXML private TextField txtModoJuego;

    @FXML private DatePicker dateFechaLanzamiento;
    @FXML private ComboBox<Estado> comboEstado;
    @FXML private ComboBox<Consola> comboConsola;

    @FXML private RadioButton radioSi;
    @FXML private RadioButton radioNo;
    @FXML private RadioButton radioNoSe;

    @FXML private ToggleGroup grupoRecomendado;

    @FXML private ImageView imgPreview;
    private File imagenSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Grupo para los RadioButton
        grupoRecomendado = new ToggleGroup();
        radioSi.setToggleGroup(grupoRecomendado);
        radioNo.setToggleGroup(grupoRecomendado);
        radioNoSe.setToggleGroup(grupoRecomendado);
        radioNoSe.setSelected(true);

        // Cargar datos en ComboBoxes
        comboEstado.setItems(ComboDAO.cargarEstadosPorTipo("juego"));
        comboEstado.setPromptText("Seleccione uno...");

        comboConsola.setItems(ComboDAO.cargarConsolas());
        comboConsola.setPromptText("Seleccione una...");
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
            imgPreview.setImage(new Image(archivo.toURI().toString()));
        }
    }

    @FXML
    private void guardarJuego(ActionEvent event) {
        // Validaciones mínimas
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            return;
        }

        Estado estadoSeleccionado = comboEstado.getValue();
        Consola consolaSeleccionada = comboConsola.getValue();

        if (estadoSeleccionado == null || consolaSeleccionada == null) {
            mostrarAlerta("Debe seleccionar estado y consola.");
            return;
        }

        // Captura datos
        String descripcion = txtDescripcion.getText().trim();
        String desarrollador = txtDesarrollador.getText().trim();
        String editor = txtEditor.getText().trim();
        String genero = txtGenero.getText().trim();
        String modoJuego = txtModoJuego.getText().trim();
        String fechaLanzamiento = (dateFechaLanzamiento.getValue() != null)
                ? dateFechaLanzamiento.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)
                : null;

        boolean recomendado = false;
        if (radioSi.isSelected()) recomendado = true;
        else if (radioNo.isSelected()) recomendado = false;
        else if (radioNoSe.isSelected()) recomendado = false; // por ahora se trata como false

        String rutaImagen = (imagenSeleccionada != null) ? imagenSeleccionada.getAbsolutePath() : null;

        // Crea juego
        Juego juego = new Juego();
        juego.setNombre(nombre);
        juego.setDescripcion(descripcion);
        juego.setDesarrollador(desarrollador);
        juego.setEditor(editor);
        juego.setGenero(genero);
        juego.setModoJuego(modoJuego);
        juego.setFechaLanzamiento(fechaLanzamiento);
        juego.setIdEstado(estadoSeleccionado.getId());
        juego.setIdConsola(consolaSeleccionada.getId());
        juego.setEsRecomendado(recomendado);
        juego.setImagen(rutaImagen);

        boolean exito = new JuegoDAO().insertarJuego(juego);

        if (exito) {
            mostrarAlerta("Juego guardado correctamente.");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al guardar el juego.");
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
        grupoRecomendado.selectToggle(radioNoSe);
        imgPreview.setImage(null);
        imagenSeleccionada = null;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
