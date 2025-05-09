package controllers;

import dao.ComboDAO;
import dao.LogrosDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Estado;
import models.Logros;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LogrosController implements Initializable {

    private final LogrosDAO logrosDAO = new LogrosDAO();

    @FXML
    private Text tituloLogros;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ListView<Logros> listaLogros;
    @FXML
    private Button btnPrimero;
    @FXML
    private Button btnAnterior;
    @FXML
    private Label paginaActual;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnUltimo;
    @FXML
    private ImageView imgBoxart;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblJuego;
    @FXML
    private Label lblConsola;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblDificultad;
    @FXML
    private Label lblHoras;
    @FXML
    private Label lblIntentos;
    @FXML
    private Label lblPuntuacion;
    @FXML
    private Label lblCreditos;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;

    private ObservableList<Logros> logrosOriginales = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarComboEstado();
        cargarLogros();
        listaLogros.setOnMouseClicked(this::mostrarDetalle);
    }

    private void cargarComboEstado() {
        comboEstado.setItems(ComboDAO.cargarEstadosPorTipo("logro"));
    }

    private void cargarLogros() {
        logrosOriginales = FXCollections.observableArrayList(logrosDAO.obtenerLogros());
        listaLogros.setItems(logrosOriginales);
        if (!logrosOriginales.isEmpty()) {
            listaLogros.getSelectionModel().selectFirst();
            mostrarDetalle(null);
        }
    }

    private void mostrarDetalle(MouseEvent event) {
        Logros logro = listaLogros.getSelectionModel().getSelectedItem();
        if (logro == null) {
            return;
        }

        lblNombre.setText(logro.getNombre());
        lblDescripcion.setText(logro.getDescripcion());
        lblJuego.setText(logro.getJuego() != null ? logro.getJuego().getNombre() : "");
        lblConsola.setText(logro.getConsola() != null ? logro.getConsola().getNombre() : "");
        lblEstado.setText(logro.getEstado() != null ? logro.getEstado().getNombre() : "");
        lblDificultad.setText(logro.getDificultad() != null ? logro.getDificultad().getNombre() : "Sin dificultad");
        lblHoras.setText(String.valueOf(logro.getHorasEstimadas()));
        lblIntentos.setText(String.valueOf(logro.getIntentos()));
        lblPuntuacion.setText(String.valueOf(logro.getPuntuacion()));
        lblCreditos.setText(String.valueOf(logro.getCreditos()));

        if (logro.getJuego() != null && logro.getJuego().getImagen() != null) {
            File imgFile = new File(config.Conexion.imagenesPath + File.separator + logro.getJuego().getImagen());
            if (imgFile.exists()) {
                imgBoxart.setImage(new Image(imgFile.toURI().toString()));
                iconoImagenNoDisponible.setVisible(false);
            } else {
                imgBoxart.setImage(null);
                iconoImagenNoDisponible.setVisible(true);
            }
        } else {
            imgBoxart.setImage(null);
            iconoImagenNoDisponible.setVisible(true);
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        comboEstado.getSelectionModel().clearSelection();
        campoBusqueda.clear();
        cargarLogros();
    }

    @FXML
    private void abrirModalAgregarLogro(ActionEvent event) {
        // Pendiente: abrirModalAgregarEditarLogro(null);
    }

    @FXML
    private void eliminarLogro(ActionEvent event) {
        Logros seleccionado = listaLogros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de eliminar este logro?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmación");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            LogrosDAO dao = new LogrosDAO(); // ← Instancia del DAO
            if (dao.eliminarLogro(seleccionado.getId())) {
                cargarLogros(); // ← Recarga la lista
            } else {
                new Alert(Alert.AlertType.ERROR, "No se pudo eliminar el logro.").showAndWait();
            }
        }
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        listaLogros.getSelectionModel().selectFirst();
        mostrarDetalle(null);
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        int index = listaLogros.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            listaLogros.getSelectionModel().select(index - 1);
            mostrarDetalle(null);
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        int index = listaLogros.getSelectionModel().getSelectedIndex();
        if (index < listaLogros.getItems().size() - 1) {
            listaLogros.getSelectionModel().select(index + 1);
            mostrarDetalle(null);
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        listaLogros.getSelectionModel().selectLast();
        mostrarDetalle(null);
    }

    @FXML
    private void editarLogro(ActionEvent event) {
    }
}
