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
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class FormJuegosController implements Initializable {

    @FXML
    private TextField txtNombre, txtDescripcion, txtDesarrollador, txtEditor, txtGenero, txtModoJuego;
    @FXML
    private DatePicker dateFechaLanzamiento;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private ComboBox<Consola> comboConsola;
    @FXML
    private RadioButton radioSi, radioNo;
    @FXML
    private ToggleGroup grupoRecomendado;
    @FXML
    private MediaView mediaPreview;
    @FXML
    private ImageView imgPreview;
    @FXML
    private AnchorPane formularioJuego;
    @FXML
    private GridPane gridFormulario;
    @FXML
    private Button btnGuardar;

    private File imagenSeleccionada;
    private Juego juegoEditando;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grupoRecomendado = new ToggleGroup();
        radioSi.setToggleGroup(grupoRecomendado);
        radioNo.setToggleGroup(grupoRecomendado);
        radioSi.setSelected(true);

        comboEstado.setItems(ComboDAO.cargarEstadosPorTipo("juego"));
        comboConsola.setItems(ComboDAO.cargarConsolas());

        comboEstado.setPromptText("Seleccione uno...");
        comboConsola.setPromptText("Seleccione una...");

        dateFechaLanzamiento.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });

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
            imgPreview.setImage(new Image(archivo.toURI().toString()));
            AppLogger.info("Imagen seleccionada: " + archivo.getAbsolutePath());
        }
    }

    @FXML
    private void guardarJuego(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            AppLogger.warning("El nombre del juego está vacío.");
            return;
        }

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

        String descripcion = txtDescripcion.getText().trim();
        String desarrollador = txtDesarrollador.getText().trim();
        String editor = txtEditor.getText().trim();
        String genero = txtGenero.getText().trim();
        String modoJuego = txtModoJuego.getText().trim();
        String fechaLanzamiento = (dateFechaLanzamiento.getValue() != null)
                ? dateFechaLanzamiento.getValue().format(formatter)
                : null;

        boolean recomendado = radioSi.isSelected();

        String nombreImagen = null;
        if (imagenSeleccionada != null) {
            nombreImagen = Conexion.guardarImagen(imagenSeleccionada);
            if (nombreImagen == null) {
                mostrarAlerta("Error al guardar la imagen.");
                AppLogger.severe("Error al guardar la imagen seleccionada.");
                return;
            }
        } else if (juegoEditando != null) {
            nombreImagen = juegoEditando.getImagen();
        }

        String nombreVideo = null;
        if (mediaPreview.getMediaPlayer() != null) {
            nombreVideo = mediaPreview.getMediaPlayer().getMedia().getSource();
            nombreVideo = new File(nombreVideo).getName();
            try {
                nombreVideo = URLDecoder.decode(nombreVideo, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                AppLogger.severe("Error al decodificar el nombre del video: " + e.getMessage());
            }
        }

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
        juego.setImagen(nombreImagen);
        juego.setVideo(nombreVideo);

        JuegoDAO dao = new JuegoDAO();
        boolean exito;
        if (juegoEditando == null) {
            exito = dao.insertarJuego(juego);
        } else {
            juego.setId(juegoEditando.getId());
            exito = dao.actualizarJuego(juego);
        }

    }

    void limpiarFormulario() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtDesarrollador.clear();
        txtEditor.clear();
        txtGenero.clear();
        txtModoJuego.clear();
        dateFechaLanzamiento.setValue(null);
        comboEstado.getSelectionModel().clearSelection();
        comboConsola.getSelectionModel().clearSelection();
        grupoRecomendado.selectToggle(radioSi);
        imagenSeleccionada = null;
        mediaPreview.setMediaPlayer(null);
        AppLogger.info("Formulario de juego limpiado.");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cargarJuegoParaEditar(Juego juegoSeleccionado) {
        juegoEditando = juegoSeleccionado;

        txtNombre.setText(juegoSeleccionado.getNombre());
        txtDescripcion.setText(juegoSeleccionado.getDescripcion());
        txtDesarrollador.setText(juegoSeleccionado.getDesarrollador());
        txtEditor.setText(juegoSeleccionado.getEditor());
        txtGenero.setText(juegoSeleccionado.getGenero());
        txtModoJuego.setText(juegoSeleccionado.getModoJuego());
        dateFechaLanzamiento.setValue(
                (juegoSeleccionado.getFechaLanzamiento() != null && !juegoSeleccionado.getFechaLanzamiento().isEmpty())
                ? LocalDate.parse(juegoSeleccionado.getFechaLanzamiento(), formatter)
                : null
        );

        comboEstado.setValue(juegoSeleccionado.getEstado());
        comboConsola.setValue(juegoSeleccionado.getConsola());

        if (juegoSeleccionado.getImagen() != null && !juegoSeleccionado.getImagen().isEmpty()) {
            File imageFile = new File(Conexion.imagenesPath, juegoSeleccionado.getImagen());
            if (imageFile.exists()) {
                imgPreview.setImage(new Image(imageFile.toURI().toString()));
            }
        }

        if (juegoSeleccionado.getVideo() != null && !juegoSeleccionado.getVideo().isEmpty()) {
            File videoFile = new File(Conexion.videosPath, juegoSeleccionado.getVideo());
            if (videoFile.exists()) {
                javafx.scene.media.Media media = new javafx.scene.media.Media(videoFile.toURI().toString());
                javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
                mediaPreview.setMediaPlayer(mediaPlayer);
                mediaPlayer.setAutoPlay(false);
                AppLogger.info("Video cargado correctamente: " + videoFile.getAbsolutePath());
            } else {
                AppLogger.warning("El archivo de video no existe: " + videoFile.getAbsolutePath());
            }
        } else {
            mediaPreview.setMediaPlayer(null);
        }

        radioSi.setSelected(juegoSeleccionado.isEsRecomendado());
        radioNo.setSelected(!juegoSeleccionado.isEsRecomendado());
    }

    @FXML
    private void seleccionarVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Video", "*.mp4", "*.avi", "*.mov"));

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            String nombreVideo = archivo.getName();
            try {
                nombreVideo = URLDecoder.decode(nombreVideo, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                AppLogger.severe("Error al decodificar el nombre del video: " + e.getMessage());
            }

            if (Conexion.guardarVideo(archivo) != null) {
                mediaPreview.setMediaPlayer(new javafx.scene.media.MediaPlayer(
                        new javafx.scene.media.Media(archivo.toURI().toString())
                ));
                AppLogger.info("Video seleccionado: " + archivo.getAbsolutePath());

                if (juegoEditando != null) {
                    juegoEditando.setVideo(nombreVideo);
                }
            } else {
                mostrarAlerta("Error al guardar el video.");
                AppLogger.severe("Error al guardar el video.");
            }
        }
    }
}
