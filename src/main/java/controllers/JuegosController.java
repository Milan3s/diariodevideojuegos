package controllers;

import config.AppLogger;
import config.Conexion;
import dao.JuegoDAO;
import models.Juego;
import models.Estado;
import models.Consola;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

public class JuegosController implements Initializable {

    @FXML
    private Text tituloJuegos;

    @FXML
    private ListView<Juego> listaJuegos;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblEditor;
    @FXML
    private Label lblDesarrollador;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblModo;
    @FXML
    private Label lblRecomendado;
    @FXML
    private ImageView imgDetalle;
    @FXML
    private Label lblNoImagen;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblConsola;

    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ComboBox<?> comboEstado;
    @FXML
    private ComboBox<?> comboConsola;
    @FXML
    private MediaView videoDetalle;

    private Juego juegoSeleccionado;
    private MediaPlayer mediaPlayer;

    @FXML
    private Button btnReproducir;
    @FXML
    private Button btnPausar;
    @FXML
    private Button btnDetener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarJuegos();
        configurarListView();
    }

    private void cargarJuegos() {
        JuegoDAO dao = new JuegoDAO();
        ObservableList<Juego> juegos = dao.obtenerTodos();
        listaJuegos.setItems(juegos);
    }

    private void configurarListView() {
        listaJuegos.setCellFactory(lv -> new ListCell<Juego>() {
            @Override
            protected void updateItem(Juego item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    setDisable(true);
                    setMouseTransparent(true);
                } else {
                    setText(item.getNombreConsola()); // Usamos el campo juego_consola que contiene "Super Mario (SNES)"
                    setDisable(false);
                    setMouseTransparent(false);
                }
            }
        });

        listaJuegos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> {
            if (nuevo != null) {
                juegoSeleccionado = nuevo;  // Guardamos el juego seleccionado
                mostrarDetalle(juegoSeleccionado);  // Mostramos los detalles en los labels
                // Reproducir automáticamente el video si está disponible
                reproducirVideo(null);
            }
        });
    }

    private void mostrarDetalle(Juego juego) {
        lblNombre.setText(juego.getNombre());
        lblGenero.setText(juego.getGenero());
        lblEditor.setText(juego.getEditor());
        lblDesarrollador.setText(juego.getDesarrollador());
        lblFecha.setText(juego.getFechaLanzamiento());
        lblModo.setText(juego.getModoJuego());
        lblRecomendado.setText(juego.isEsRecomendado() ? "Sí" : "No");
        lblEstado.setText(juego.getEstado() != null ? juego.getEstado().getNombre() : "No disponible");
        lblConsola.setText(juego.getConsola() != null ? juego.getConsola().getNombre() : "No disponible");

        // Verificar si el juego tiene una imagen asignada
        if (juego.getImagen() != null && !juego.getImagen().isEmpty()) {
            File imageFile = new File(Conexion.imagenesPath, juego.getImagen());
            if (imageFile.exists()) {
                imgDetalle.setImage(new Image(imageFile.toURI().toString()));
                lblNoImagen.setVisible(false);
            } else {
                imgDetalle.setImage(null);
                lblNoImagen.setVisible(true);
            }
        } else {
            imgDetalle.setImage(null);
            lblNoImagen.setVisible(true);
        }

        // **Previsualización de video compatible con MP4 y FLV**
        if (juego.getVideo() != null && !juego.getVideo().isEmpty()) {
            File videoFile = new File(Conexion.videosPath, juego.getVideo());
            if (videoFile.exists()) {
                try {
                    // Si ya existe un MediaPlayer, lo detenemos antes de crear uno nuevo
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }

                    Media media = new Media(videoFile.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    videoDetalle.setMediaPlayer(mediaPlayer);
                    mediaPlayer.setAutoPlay(false); // No reproducir automáticamente al cargar

                } catch (Exception e) {
                    AppLogger.warning("No se pudo reproducir el video: " + videoFile.getAbsolutePath());
                    videoDetalle.setMediaPlayer(null);  // Limpiar el MediaView
                }
            }
        } else {
            videoDetalle.setMediaPlayer(null);
        }
    }

    @FXML
    private void reproducirVideo(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play(); // Reproducir el video
            btnReproducir.setDisable(true);  // Deshabilitar el botón "Reproducir" cuando el video está en reproducción
            btnPausar.setDisable(false);   // Habilitar el botón "Pausar"
            btnDetener.setDisable(false);  // Habilitar el botón "Detener"
            AppLogger.info("Reproduciendo video.");
        }
    }

    @FXML
    private void pausarVideo(ActionEvent event) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause(); // Pausar el video
            btnPausar.setDisable(true);  // Deshabilitar el botón "Pausar" mientras el video está en pausa
            btnReproducir.setDisable(false);  // Habilitar el botón "Reproducir"
            AppLogger.info("Video pausado.");
        }
    }

    @FXML
    private void detenerVideo(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Detener la reproducción del video
            videoDetalle.setMediaPlayer(null); // Limpiar el MediaView
            btnReproducir.setDisable(false); // Habilitar el botón "Reproducir"
            btnPausar.setDisable(true);     // Deshabilitar el botón "Pausar"
            btnDetener.setDisable(true);    // Deshabilitar el botón "Detener"
            AppLogger.info("Video detenido.");
        }
    }

    @FXML
    private void abrirModalAgregarJuego(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
            Parent root = loader.load();
            FormJuegosController formJuegosController = loader.getController();
            formJuegosController.limpiarFormulario();  // Limpiar el formulario para añadir un nuevo juego
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Añadir Juego");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarJuegos();  // Recargar juegos después de agregar uno
        } catch (IOException e) {
            AppLogger.severe("Error al abrir el modal de agregar juego: " + e.getMessage());
        }
    }

    @FXML
    private void editarJuego(ActionEvent event) {
        if (juegoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
                Parent root = loader.load();
                FormJuegosController formJuegosController = loader.getController();
                formJuegosController.cargarJuegoParaEditar(juegoSeleccionado);  // Cargar los detalles del juego en el formulario
                Stage modal = new Stage();
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.setTitle("Editar Juego");
                modal.setScene(new Scene(root));
                modal.setResizable(false);
                modal.showAndWait();

                cargarJuegos();  // Recargar juegos después de editar uno
            } catch (IOException e) {
                AppLogger.severe("Error al abrir el modal de editar juego: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Por favor, seleccione un juego para editar.");
        }
    }

    private void eliminarJuego(Juego juegoSeleccionado) {
        boolean exito = new JuegoDAO().eliminarJuego(juegoSeleccionado.getId());
        if (exito) {
            mostrarAlerta("Juego eliminado correctamente.");
            AppLogger.info("Juego eliminado correctamente: " + juegoSeleccionado.getNombre());
            cargarJuegos();  // Recargar la lista de juegos después de la eliminación
        } else {
            mostrarAlerta("Error al eliminar el juego.");
            AppLogger.severe("Error al eliminar el juego: " + juegoSeleccionado.getNombre());
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void confirmarEliminacionJuego(Juego juegoSeleccionado) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación de Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar el juego?");
        confirmacion.setContentText("El juego: " + juegoSeleccionado.getNombre());

        // Opciones de respuesta
        confirmacion.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        // Acción a realizar según la respuesta del usuario
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                eliminarJuego(juegoSeleccionado);  // Si confirma, eliminamos el juego
            }
        });
    }

    @FXML
    private void eliminarJuego(ActionEvent event) {
        if (juegoSeleccionado != null) {
            confirmarEliminacionJuego(juegoSeleccionado); // Llamar al método de confirmación
        } else {
            mostrarAlerta("No se ha seleccionado ningún juego.");
        }
    }

}
