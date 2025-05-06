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
    private MediaView mediaPreview; // Solo el MediaView, sin ImageView para el overlay

    @FXML
    private ImageView imgPreview; // ImageView para mostrar la previsualización de la imagen

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
            // Actualiza la vista previa de la imagen seleccionada si es necesario
            imgPreview.setImage(new Image(archivo.toURI().toString()));  // Mostrar la imagen seleccionada en imgPreview
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

        // Video
        String nombreVideo = null;
        if (mediaPreview.getMediaPlayer() != null) {
            nombreVideo = mediaPreview.getMediaPlayer().getMedia().getSource();  // Esto te dará la URL completa
            nombreVideo = new File(nombreVideo).getName();  // Solo obtener el nombre del archivo
            try {
                nombreVideo = URLDecoder.decode(nombreVideo, "UTF-8");  // Decodificar para reemplazar %20 por espacios
            } catch (UnsupportedEncodingException e) {
                AppLogger.severe("Error al decodificar el nombre del video: " + e.getMessage());
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
        juego.setVideo(nombreVideo);  // Guardar nombre de video

        JuegoDAO dao = new JuegoDAO();
        boolean exito;
        if (juegoEditando == null) {
            // Nuevo juego, insertar
            exito = dao.insertarJuego(juego);
        } else {
            // Editar juego existente
            juego.setId(juegoEditando.getId());
            exito = dao.actualizarJuego(juego);  // Implementar este método en JuegoDAO
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
        grupoRecomendado.selectToggle(radioSi); // Seleccionar "Sí" por defecto
        imagenSeleccionada = null;
        mediaPreview.setMediaPlayer(null);  // Limpiar el video

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
                imgPreview.setImage(new Image(imageFile.toURI().toString())); // Mostrar la imagen en el ImageView
            }
        }

        // Configurar el video si existe
        if (juegoSeleccionado.getVideo() != null && !juegoSeleccionado.getVideo().isEmpty()) {
            File videoFile = new File(Conexion.videosPath, juegoSeleccionado.getVideo());
            if (videoFile.exists()) {
                // Crear un nuevo MediaPlayer con el video seleccionado
                javafx.scene.media.Media media = new javafx.scene.media.Media(videoFile.toURI().toString());
                javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
                mediaPreview.setMediaPlayer(mediaPlayer);
                mediaPlayer.setAutoPlay(false); // No reproducir automáticamente al cargar el video

                // Aquí puedes añadir controles, como la opción de reproducir el video si es necesario.
                AppLogger.info("Video cargado correctamente: " + videoFile.getAbsolutePath());
            } else {
                AppLogger.warning("El archivo de video no existe: " + videoFile.getAbsolutePath());
            }
        } else {
            mediaPreview.setMediaPlayer(null);  // Limpiar el MediaPlayer si no hay video
        }

        // Configurar el estado del radio button
        if (juegoSeleccionado.isEsRecomendado()) {
            radioSi.setSelected(true);
        } else {
            radioNo.setSelected(true);
        }
    }

    @FXML
    private void seleccionarVideo(ActionEvent event) {
        // Usar FileChooser para seleccionar el archivo de video
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Video", "*.mp4", "*.avi", "*.mov"));

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            // Decodificar el nombre del archivo si contiene caracteres codificados
            String nombreVideo = archivo.getName();  // Nombre del archivo, con %20 para los espacios

            try {
                // Decodificar el nombre del archivo (reemplazar '%20' por espacio)
                nombreVideo = URLDecoder.decode(nombreVideo, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                AppLogger.severe("Error al decodificar el nombre del video: " + e.getMessage());
            }

            // Guardamos el video en el disco y obtenemos el nombre del archivo
            if (Conexion.guardarVideo(archivo) != null) {
                // Actualizamos la vista previa del video
                mediaPreview.setMediaPlayer(new javafx.scene.media.MediaPlayer(
                        new javafx.scene.media.Media(archivo.toURI().toString())
                ));
                AppLogger.info("Video seleccionado: " + archivo.getAbsolutePath());
                // Guardamos el nombre del video en el juego
                juegoEditando.setVideo(nombreVideo);  // Asignar el nombre del video al objeto Juego
            } else {
                mostrarAlerta("Error al guardar el video.");
                AppLogger.severe("Error al guardar el video.");
            }
        }
    }

}
