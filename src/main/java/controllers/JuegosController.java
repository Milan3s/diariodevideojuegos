package controllers;

import config.AppLogger;
import config.Conexion;
import dao.ComboDAO;
import dao.JuegoDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Consola;
import models.Estado;
import models.Juego;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.layout.BorderPane;

public class JuegosController implements Initializable {

    @FXML
    private Text tituloJuegos;
    @FXML
    private ListView<Juego> listaJuegos;
    @FXML
    private Label lblNombre, lblGenero, lblEditor, lblDesarrollador, lblFecha, lblModo, lblRecomendado, lblEstado, lblConsola;
    @FXML
    private ImageView imgDetalle;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private ComboBox<Consola> comboConsola;
    @FXML
    private MediaView videoDetalle;
    @FXML
    private HBox controlesVideo;
    @FXML
    private StackPane videoContainer;
    @FXML
    private FontAwesomeIconView iconoVideoNoDisponible;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Label paginaActual;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Button btnEditar, btnEliminar;

    private ObservableList<Juego> todosLosJuegos = FXCollections.observableArrayList();
    private ObservableList<Juego> juegosFiltrados = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;
    private Juego juegoSeleccionado;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
        comboEstado.setOnAction(this::filtrarJuegos);
        comboConsola.setOnAction(this::filtrarJuegos);
        campoBusqueda.setOnKeyReleased(this::filtrarJuegos);
        cargarJuegos();
        configurarListView();
        ocultarControlesYIconos();
    }

    private void cargarCombos() {
        ObservableList<Estado> estados = ComboDAO.cargarEstadosPorTipo("juego");
        estados.add(0, new Estado(-1, "Todos"));
        comboEstado.setItems(estados);
        comboEstado.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null || item.getId() == -1) ? "Estados" : item.getNombre());
            }
        });
        comboEstado.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboEstado.getSelectionModel().selectFirst();

        ObservableList<Consola> consolas = ComboDAO.cargarConsolas();
        consolas.add(0, new Consola(-1, "Todos", ""));
        comboConsola.setItems(consolas);
        comboConsola.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Consola item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null || item.getId() == -1) ? "Consolas" : item.getNombre());
            }
        });
        comboConsola.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Consola item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboConsola.getSelectionModel().selectFirst();
    }

    private void cargarJuegos() {
        todosLosJuegos.setAll(new JuegoDAO().obtenerTodos());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        Estado estadoSel = comboEstado.getSelectionModel().getSelectedItem();
        Consola consolaSel = comboConsola.getSelectionModel().getSelectedItem();

        juegosFiltrados.setAll(todosLosJuegos.stream()
                .filter(j -> texto.isEmpty() || j.getNombre().toLowerCase().contains(texto))
                .filter(j -> estadoSel == null || estadoSel.getId() == -1 || (j.getEstado() != null && j.getEstado().getId() == estadoSel.getId()))
                .filter(j -> consolaSel == null || consolaSel.getId() == -1 || (j.getConsola() != null && j.getConsola().getId() == consolaSel.getId()))
                .collect(Collectors.toList())
        );

        pagina = 1;
        actualizarPaginado();
    }

    private void actualizarPaginado() {
        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, juegosFiltrados.size());
        if (desde > hasta) {
            desde = 0;
        }
        listaJuegos.setItems(FXCollections.observableArrayList(juegosFiltrados.subList(desde, hasta)));
        paginaActual.setText(String.valueOf(pagina));
    }

    @FXML
    private void filtrarJuegos(ActionEvent e) {
        aplicarFiltros();
    }

    @FXML
    private void filtrarJuegos(KeyEvent e) {
        aplicarFiltros();
    }

    @FXML
    private void irPrimeraPagina(ActionEvent e) {
        pagina = 1;
        actualizarPaginado();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent e) {
        if (pagina > 1) {
            pagina--;
        }
        actualizarPaginado();
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent e) {
        int total = (int) Math.ceil((double) juegosFiltrados.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
        }
        actualizarPaginado();
    }

    @FXML
    private void irUltimaPagina(ActionEvent e) {
        pagina = (int) Math.ceil((double) juegosFiltrados.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }

    private void configurarListView() {
        listaJuegos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Juego item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreConsola());
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

        // Formatear la fecha a dd/MM/yyyy
        if (juego.getFechaLanzamiento() != null && !juego.getFechaLanzamiento().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(juego.getFechaLanzamiento());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                lblFecha.setText(fecha.format(formatter));
            } catch (Exception e) {
                lblFecha.setText("Fecha inválida");
            }
        } else {
            lblFecha.setText("No disponible");
        }

        lblModo.setText(juego.getModoJuego());
        lblRecomendado.setText(juego.isEsRecomendado() ? "Sí" : "No");
        lblEstado.setText(juego.getEstado() != null ? juego.getEstado().getNombre() : "No disponible");
        lblConsola.setText(juego.getConsola() != null ? juego.getConsola().getNombre() : "No disponible");

        File imageFile = new File(Conexion.imagenesPath, juego.getImagen() != null ? juego.getImagen() : "");
        if (imageFile.exists()) {
            imgDetalle.setImage(new Image(imageFile.toURI().toString()));
            iconoImagenNoDisponible.setVisible(false);
        } else {
            imgDetalle.setImage(null);
            iconoImagenNoDisponible.setVisible(true);
        }

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

    private void ocultarControlesYIconos() {
        controlesVideo.setVisible(false);
        iconoVideoNoDisponible.setVisible(false);
        iconoImagenNoDisponible.setVisible(false);
    }

    @FXML
    private void reproducirVideo(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void pausarVideo(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void detenerVideo(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
    }

    @FXML
    private void abrirModalAgregarJuego(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
            Parent root = loader.load();
            FormJuegosController controller = loader.getController();            
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Añadir Juego");
            modal.setResizable(false);

            modal.showAndWait();
        } catch (IOException e) {
            AppLogger.severe("Error al abrir el modal de agregar juego: " + e.getMessage());
        }
    }

    @FXML
    private void editarJuego(ActionEvent event) {
        if (juegoSeleccionado != null) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
                Parent root = loader.load();
                FormJuegosController controller = loader.getController();
                controller.cargarJuegoParaEditar(juegoSeleccionado);
                Stage modal = new Stage();
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.setScene(new Scene(root));
                modal.setTitle("Editar Juego");
                modal.showAndWait();
                cargarJuegos();
            } catch (IOException e) {
                AppLogger.severe("Error al abrir el modal de editar juego: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Seleccione un juego para editar.");
        }
    }

    @FXML
    private void eliminarJuego(ActionEvent event) {
        if (juegoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Juego");
            alert.setHeaderText("¿Eliminar juego seleccionado?");
            alert.setContentText(juegoSeleccionado.getNombre());
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    boolean ok = new JuegoDAO().eliminarJuego(juegoSeleccionado.getId());
                    if (ok) {
                        mostrarAlerta("Juego eliminado.");
                        cargarJuegos();
                    } else {
                        mostrarAlerta("No se pudo eliminar.");
                    }
                }
            });
        } else {
            mostrarAlerta("Seleccione un juego para eliminar.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        comboEstado.getSelectionModel().selectFirst();
        comboConsola.getSelectionModel().selectFirst();
        campoBusqueda.clear();
        aplicarFiltros();
    }

}
