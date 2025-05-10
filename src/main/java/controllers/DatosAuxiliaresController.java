package controllers;

import dao.DatosAuxiliaresDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import models.DatosAuxiliares;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DatosAuxiliaresController implements Initializable {

    @FXML private Text tituloAuxiliares;
    @FXML private ComboBox<String> comboTipoDato;
    @FXML private TextField campoBusqueda;
    @FXML private ListView<DatosAuxiliares> listaAuxiliares;
    @FXML private Label paginaActual, lblDetalle;

    private final DatosAuxiliaresDAO dao = new DatosAuxiliaresDAO();
    private ObservableList<DatosAuxiliares> datos = FXCollections.observableArrayList();
    private List<DatosAuxiliares> datosFiltrados = new ArrayList<>();

    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;

    @FXML private Button btnLimpiar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnPrimero;
    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnUltimo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboTipoDato.setItems(FXCollections.observableArrayList(dao.obtenerTiposVisuales()));
        comboTipoDato.setOnAction(e -> cargarDatos());

        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        listaAuxiliares.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> mostrarDetalle(nuevo));

        // Personalizar cómo se ve cada ítem en el ListView
        listaAuxiliares.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(DatosAuxiliares item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String tipo = item.getTipo() != null ? " | Tipo: " + item.getTipo() : "";
                    String tabla = comboTipoDato.getValue() != null ? " | Tabla: " + comboTipoDato.getValue() : "";
                    setText(item.getNombre() + tipo + tabla);
                }
            }
        });
    }

    private void cargarDatos() {
        String tipo = comboTipoDato.getValue();
        if (tipo == null) return;

        datos.setAll(dao.listar(tipo));
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String filtro = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        datosFiltrados = datos.stream()
                .filter(d -> filtro.isEmpty() || d.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        pagina = 1;
        actualizarPaginado();
    }

    private void actualizarPaginado() {
        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, datosFiltrados.size());

        if (desde >= hasta && pagina > 1) {
            pagina--;
            actualizarPaginado();
            return;
        }

        List<DatosAuxiliares> paginaActualDatos = datosFiltrados.subList(desde, hasta);
        listaAuxiliares.setItems(FXCollections.observableArrayList(paginaActualDatos));
        paginaActual.setText(String.valueOf(pagina));
    }

    private void mostrarDetalle(DatosAuxiliares dato) {
        if (dato != null) {
            String tipoSeleccionado = comboTipoDato.getValue() != null ? comboTipoDato.getValue() : "Desconocida";
            lblDetalle.setText(
                    "ID: " + dato.getId() +
                    "\nNombre: " + dato.getNombre() +
                    (dato.getTipo() != null ? "\nTipo: " + dato.getTipo() : "") +
                    "\nTabla: " + tipoSeleccionado
            );
        } else {
            lblDetalle.setText("");
        }
    }

    @FXML private void limpiarFiltros(ActionEvent event) {
        comboTipoDato.getSelectionModel().clearSelection();
        campoBusqueda.clear();
        listaAuxiliares.getItems().clear();
        datos.clear();
        datosFiltrados.clear();
        lblDetalle.setText("");
        paginaActual.setText("1");
    }

    @FXML private void abrirModalAgregar(ActionEvent event) {
        String tipo = comboTipoDato.getValue();
        if (tipo == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar " + tipo);
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                dao.insertar(tipo, nombre.trim());
                cargarDatos();
            }
        });
    }

    @FXML private void editarItem(ActionEvent event) {
        DatosAuxiliares seleccionado = listaAuxiliares.getSelectionModel().getSelectedItem();
        String tipo = comboTipoDato.getValue();
        if (seleccionado == null || tipo == null) return;

        TextInputDialog dialog = new TextInputDialog(seleccionado.getNombre());
        dialog.setTitle("Editar " + tipo);
        dialog.setHeaderText(null);
        dialog.setContentText("Nuevo nombre:");

        dialog.showAndWait().ifPresent(nuevo -> {
            if (!nuevo.trim().isEmpty()) {
                dao.editar(tipo, seleccionado.getId(), nuevo.trim());
                cargarDatos();
            }
        });
    }

    @FXML private void eliminarItem(ActionEvent event) {
        DatosAuxiliares seleccionado = listaAuxiliares.getSelectionModel().getSelectedItem();
        String tipo = comboTipoDato.getValue();
        if (seleccionado == null || tipo == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar " + tipo);
        confirm.setHeaderText("¿Eliminar este registro?");
        confirm.setContentText(seleccionado.getNombre());

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                dao.eliminar(tipo, seleccionado.getId());
                cargarDatos();
            }
        });
    }

    @FXML private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        actualizarPaginado();
    }

    @FXML private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            actualizarPaginado();
        }
    }

    @FXML private void irPaginaSiguiente(ActionEvent event) {
        int total = (int) Math.ceil((double) datosFiltrados.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
            actualizarPaginado();
        }
    }

    @FXML private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) datosFiltrados.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }
}
