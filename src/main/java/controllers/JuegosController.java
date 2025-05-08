package controllers;

import config.AppLogger;
import config.Conexion;
import dao.JuegoDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import models.Juego;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JuegosController implements Initializable {

    @FXML private Text tituloJuegos;
    @FXML private ListView<Juego> listaJuegos;
    @FXML private Label lblNombre, lblGenero, lblEditor, lblDesarrollador, lblFecha, lblModo, lblRecomendado, lblEstado, lblConsola;
    @FXML private ImageView imgDetalle;
    @FXML private ComboBox<?> comboEstado, comboConsola;
    @FXML private MediaView videoDetalle;
    @FXML private HBox controlesVideo;
    @FXML private StackPane videoContainer;
    @FXML private FontAwesomeIconView iconoVideoNoDisponible;
    @FXML private FontAwesomeIconView iconoImagenNoDisponible;

    private Juego juegoSeleccionado;
    private MediaPlayer mediaPlayer;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Label paginaActual;
    @FXML
    private Button btnPrimero;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnUltimo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarJuegos();
        configurarListView();
        ocultarControlesYIconos();
    }

    private void ocultarControlesYIconos() {
        if (controlesVideo != null) controlesVideo.setVisible(false);
        if (iconoVideoNoDisponible != null) iconoVideoNoDisponible.setVisible(false);
        if (iconoImagenNoDisponible != null) iconoImagenNoDisponible.setVisible(false);
    }

    private void cargarJuegos() {
        JuegoDAO dao = new JuegoDAO();
        ObservableList<Juego> juegos = dao.obtenerTodos();
        listaJuegos.setItems(juegos);
    }

    private void configurarListView() {
        listaJuegos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Juego item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setDisable(true);
                    setMouseTransparent(true);
                } else {
                    setText(item.getNombreConsola());
                    setDisable(false);
                    setMouseTransparent(false);
                }
            }
        });

        listaJuegos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                videoDetalle.setMediaPlayer(null);
                mediaPlayer = null;
            }

            if (nuevo != null) {
                juegoSeleccionado = nuevo;
                mostrarDetalle(juegoSeleccionado);
            } else {
                ocultarControlesYIconos();
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

        // Imagen
        if (juego.getImagen() != null && !juego.getImagen().isEmpty()) {
            File imageFile = new File(Conexion.imagenesPath, juego.getImagen());
            if (imageFile.exists()) {
                imgDetalle.setImage(new Image(imageFile.toURI().toString()));
                iconoImagenNoDisponible.setVisible(false);
            } else {
                imgDetalle.setImage(null);
                iconoImagenNoDisponible.setVisible(true);
            }
        } else {
            imgDetalle.setImage(null);
            iconoImagenNoDisponible.setVisible(true);
        }

        // Video
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            videoDetalle.setMediaPlayer(null);
        }

        if (juego.getVideo() != null && !juego.getVideo().isEmpty()) {
            File videoFile = new File(Conexion.videosPath, juego.getVideo());
            if (videoFile.exists()) {
                try {
                    Media media = new Media(videoFile.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    videoDetalle.setMediaPlayer(mediaPlayer);
                    mediaPlayer.setAutoPlay(true);
                    controlesVideo.setVisible(true);
                    iconoVideoNoDisponible.setVisible(false);
                } catch (Exception e) {
                    AppLogger.warning("No se pudo reproducir el video: " + videoFile.getAbsolutePath());
                    controlesVideo.setVisible(false);
                    iconoVideoNoDisponible.setVisible(true);
                }
            } else {
                controlesVideo.setVisible(false);
                iconoVideoNoDisponible.setVisible(true);
            }
        } else {
            controlesVideo.setVisible(false);
            iconoVideoNoDisponible.setVisible(true);
        }
    }

    @FXML
    private void reproducirVideo(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            AppLogger.info("Reproduciendo video.");
        }
    }

    @FXML
    private void pausarVideo(MouseEvent event) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            AppLogger.info("Video pausado.");
        }
    }

    @FXML
    private void detenerVideo(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            AppLogger.info("Video detenido.");
        }
    }

    @FXML
    private void abrirModalAgregarJuego(ActionEvent event) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            AppLogger.info("Video pausado automáticamente al abrir el formulario para agregar un juego.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
            Parent root = loader.load();
            FormJuegosController formJuegosController = loader.getController();
            formJuegosController.limpiarFormulario();
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Añadir Juego");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarJuegos();
        } catch (IOException e) {
            AppLogger.severe("Error al abrir el modal de agregar juego: " + e.getMessage());
        }
    }

    @FXML
    private void editarJuego(ActionEvent event) {
        if (juegoSeleccionado != null) {
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                AppLogger.info("Video pausado automáticamente al editar el juego.");
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
                Parent root = loader.load();
                FormJuegosController formJuegosController = loader.getController();
                formJuegosController.cargarJuegoParaEditar(juegoSeleccionado);
                Stage modal = new Stage();
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.setTitle("Editar Juego");
                modal.setScene(new Scene(root));
                modal.setResizable(false);
                modal.showAndWait();

                cargarJuegos();
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
            cargarJuegos();
        } else {
            mostrarAlerta("Error al eliminar el juego.");
            AppLogger.severe("Error al eliminar el juego: " + juegoSeleccionado.getNombre());
        }
    }

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
        confirmacion.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                eliminarJuego(juegoSeleccionado);
            }
        });
    }

    @FXML
    private void eliminarJuego(ActionEvent event) {
        if (juegoSeleccionado != null) {
            confirmarEliminacionJuego(juegoSeleccionado);
        } else {
            mostrarAlerta("No se ha seleccionado ningún juego.");
        }
    }


}