package controllers;

import dao.ComboDAO;
import dao.MetasTwitchDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import models.MetasTwitch;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MetasTwitchController implements Initializable {

    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;
    @FXML
    private ListView<MetasTwitch> listaMetas;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private Text tituloMetas;
    @FXML
    private ImageView imgBoxart;
    @FXML
    private Label lblDescripcion, lblMeta, lblActual, lblMes, lblAnio, lblFechaInicio, lblFechaFin, lblFechaRegistro;
    @FXML
    private ComboBox<Integer> comboAnios;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;

    private final MetasTwitchDAO dao = new MetasTwitchDAO();
    private ObservableList<MetasTwitch> todasLasMetas = FXCollections.observableArrayList();
    private ObservableList<MetasTwitch> metasPaginadas = FXCollections.observableArrayList();

    private final int elementosPorPagina = 10;
    private int pagina = 1;
    private int totalPaginas = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaMetas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> mostrarDetalle(newVal));
        campoBusqueda.setOnKeyReleased(this::buscar);
        comboAnios.setOnAction(this::filtrarPorAnio);

        comboAnios.setItems(ComboDAO.cargarAniosMetasTwitch());
        comboAnios.getItems().add(0, null); // opción para "Todos los años"
        comboAnios.setPromptText("Año");

        cargarMetas();
    }

    private void cargarMetas() {
        todasLasMetas = dao.obtenerMetas();
        aplicarFiltroYActualizarPaginacion();
    }

    private void aplicarFiltroYActualizarPaginacion() {
        String filtro = campoBusqueda.getText().toLowerCase();
        Integer anioSeleccionado = comboAnios.getValue();

        ObservableList<MetasTwitch> filtradas = todasLasMetas.filtered(meta -> {
            boolean coincideTexto = meta.getDescripcion().toLowerCase().contains(filtro)
                    || meta.getMes().toLowerCase().contains(filtro)
                    || String.valueOf(meta.getAnio()).contains(filtro);
            boolean coincideAnio = anioSeleccionado == null || meta.getAnio() == anioSeleccionado;
            return coincideTexto && coincideAnio;
        });

        totalPaginas = (int) Math.ceil((double) filtradas.size() / elementosPorPagina);
        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }
        if (pagina < 1) {
            pagina = 1;
        }

        int desde = (pagina - 1) * elementosPorPagina;
        int hasta = Math.min(desde + elementosPorPagina, filtradas.size());

        metasPaginadas.setAll(filtradas.subList(desde, hasta));
        listaMetas.setItems(metasPaginadas);
        paginaActual.setText(String.valueOf(pagina));
    }

    private void mostrarDetalle(MetasTwitch meta) {
        if (meta == null) {
            lblDescripcion.setText("");
            lblMeta.setText("");
            lblActual.setText("");
            lblMes.setText("");
            lblAnio.setText("");
            lblFechaInicio.setText("");
            lblFechaFin.setText("");
            lblFechaRegistro.setText("");
            return;
        }

        lblDescripcion.setText(meta.getDescripcion());
        lblMeta.setText(String.valueOf(meta.getMeta()));
        lblActual.setText(String.valueOf(meta.getActual()));
        lblMes.setText(meta.getMes());
        lblAnio.setText(String.valueOf(meta.getAnio()));
        lblFechaInicio.setText(meta.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFechaFin.setText(meta.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFechaRegistro.setText(meta.getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void buscar(KeyEvent event) {
        pagina = 1;
        aplicarFiltroYActualizarPaginacion();
    }

    private void filtrarPorAnio(ActionEvent event) {
        pagina = 1;
        aplicarFiltroYActualizarPaginacion();
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        // TODO: Abrir modal, crear nueva meta y recargar lista
    }

    @FXML
    private void editarMeta(ActionEvent event) {
        MetasTwitch seleccionada = listaMetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una meta para editar.");
            return;
        }
        // TODO: Abrir modal con datos de la meta seleccionada
    }

    @FXML
    private void eliminarMeta(ActionEvent event) {
        MetasTwitch seleccionada = listaMetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una meta para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("¿Estás seguro?");
        alert.setContentText("Esta acción eliminará la meta seleccionada.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dao.eliminarMeta(seleccionada.getIdMeta());
                cargarMetas();
            }
        });
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        aplicarFiltroYActualizarPaginacion();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            aplicarFiltroYActualizarPaginacion();
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        if (pagina < totalPaginas) {
            pagina++;
            aplicarFiltroYActualizarPaginacion();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = totalPaginas;
        aplicarFiltroYActualizarPaginacion();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
