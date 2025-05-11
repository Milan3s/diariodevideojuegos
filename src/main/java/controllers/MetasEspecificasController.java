package controllers;

import dao.MetasEspecificasDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.MetasEspecificas;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MetasEspecificasController implements Initializable {

    @FXML
    private Text tituloMetas;
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;
    @FXML
    private ComboBox<Integer> comboAnios;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ListView<MetasEspecificas> listaMetas;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private ImageView imgBoxart;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;

    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblJuegosObjetivo;
    @FXML
    private Label lblJuegosCompletados;
    @FXML
    private Label lblFabricante;
    @FXML
    private Label lblConsola;
    @FXML
    private Label lblFechaInicio;
    @FXML
    private Label lblFechaFin;
    @FXML
    private Label lblCumplida;

    private final MetasEspecificasDAO dao = new MetasEspecificasDAO();
    private final int ITEMS_POR_PAGINA = 10;
    private int pagina = 1;
    private int totalPaginas = 1;
    private List<MetasEspecificas> todasMetas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> buscar());
        comboAnios.setOnAction(e -> filtrarPorAnio());
        listaMetas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> mostrarDetalle(newVal));
        cargarAnios();
        actualizarLista();
    }

    private void cargarAnios() {
        List<Integer> anios = dao.obtenerAniosDisponibles();
        comboAnios.setItems(FXCollections.observableArrayList(anios));
        comboAnios.getItems().add(0, null);
        comboAnios.setPromptText("Año");
    }

    private void actualizarLista() {
        String filtro = campoBusqueda.getText().toLowerCase().trim();
        Integer anio = comboAnios.getValue();
        todasMetas = dao.obtenerTodas(anio, filtro);
        totalPaginas = (int) Math.ceil((double) todasMetas.size() / ITEMS_POR_PAGINA);
        pagina = Math.min(Math.max(pagina, 1), Math.max(1, totalPaginas));
        cargarPagina();
    }

    private void cargarPagina() {
        int inicio = (pagina - 1) * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todasMetas.size());
        listaMetas.setItems(FXCollections.observableArrayList(todasMetas.subList(inicio, fin)));
        paginaActual.setText(pagina + " / " + totalPaginas);
    }

    private void mostrarDetalle(MetasEspecificas meta) {
        if (meta == null) {
            lblDescripcion.setText("");
            lblJuegosObjetivo.setText("");
            lblJuegosCompletados.setText("");
            lblFabricante.setText("");
            lblConsola.setText("");
            lblFechaInicio.setText("");
            lblFechaFin.setText("");
            lblCumplida.setText("");
            imgBoxart.setImage(null);
            return;
        }

        lblDescripcion.setText(meta.getDescripcion());
        lblJuegosObjetivo.setText(String.valueOf(meta.getJuegosObjetivo()));
        lblJuegosCompletados.setText(String.valueOf(meta.getJuegosCompletados()));
        lblFabricante.setText(meta.getFabricante());
        lblConsola.setText(meta.getNombreConsola());
        lblFechaInicio.setText(meta.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFechaFin.setText(meta.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblCumplida.setText(meta.isCumplida() ? "Sí" : "No");
        imgBoxart.setImage(null); // La imagen aún no se usa
    }

    private void buscar() {
        pagina = 1;
        actualizarLista();
    }

    private void filtrarPorAnio() {
        pagina = 1;
        actualizarLista();
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        // TODO: lógica para abrir el modal de agregar
    }

    @FXML
    private void editarMeta(ActionEvent event) {
        MetasEspecificas seleccionada = listaMetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            return;
        }
        // TODO: lógica para editar
    }

    @FXML
    private void eliminarMeta(ActionEvent event) {
        MetasEspecificas seleccionada = listaMetas.getSelectionModel().getSelectedItem();
        if (seleccionada != null && dao.eliminar(seleccionada.getId())) {
            actualizarLista();
        }
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        cargarPagina();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            cargarPagina();
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        if (pagina < totalPaginas) {
            pagina++;
            cargarPagina();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = totalPaginas;
        cargarPagina();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
