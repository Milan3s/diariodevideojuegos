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

/**
 * Controlador para la gestión de datos auxiliares (estados, dificultades, años,
 * etc.)
 */
public class DatosAuxiliaresController implements Initializable {

    @FXML
    private Text tituloAuxiliares;
    @FXML
    private ComboBox<String> comboTipoDato;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ListView<DatosAuxiliares> listaAuxiliares;
    @FXML
    private Label paginaActual, lblDetalle;

    private final DatosAuxiliaresDAO dao = new DatosAuxiliaresDAO();

    private final Map<String, TablaAuxiliar> configuraciones = new LinkedHashMap<>();
    private ObservableList<DatosAuxiliares> datos = FXCollections.observableArrayList();
    private List<DatosAuxiliares> datosFiltrados = new ArrayList<>();

    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
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
        configurarTipos();
        comboTipoDato.setItems(FXCollections.observableArrayList(configuraciones.keySet()));
        comboTipoDato.setOnAction(e -> cargarDatos());

        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        listaAuxiliares.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> mostrarDetalle(nuevo));
    }

    private void configurarTipos() {
        configuraciones.put("Estados", new TablaAuxiliar("estados", "id_estado", "nombre"));
        configuraciones.put("Dificultades", new TablaAuxiliar("dificultades_logros", "id_dificultad", "nombre"));
        configuraciones.put("Años Metas", new TablaAuxiliar("anios_metas_especificas", "anio", "anio"));
        // Puedes agregar más aquí si es necesario
    }

    private void cargarDatos() {
        String tipo = comboTipoDato.getValue();
        if (tipo == null) {
            return;
        }

        TablaAuxiliar config = configuraciones.get(tipo);
        datos.clear();
        for (String[] fila : dao.listar(config.tabla, config.columnaNombre, config.columnaId)) {
            try {
                int id = Integer.parseInt(fila[0]);
                datos.add(new DatosAuxiliares(id, fila[1]));
            } catch (NumberFormatException ignored) {
            }
        }
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String filtro = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        datosFiltrados = datos.stream()
                .filter(d -> filtro.isEmpty() || d.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());  // << CAMBIA esto

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
            lblDetalle.setText("ID: " + dato.getId() + "\nNombre: " + dato.getNombre());
        } else {
            lblDetalle.setText("");
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        comboTipoDato.getSelectionModel().clearSelection();
        campoBusqueda.clear();
        listaAuxiliares.getItems().clear();
        datos.clear();
        datosFiltrados.clear();
        lblDetalle.setText("");
        paginaActual.setText("1");
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        String tipo = comboTipoDato.getValue();
        if (tipo == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar " + tipo);
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                TablaAuxiliar config = configuraciones.get(tipo);
                dao.insertar(config.tabla, config.columnaNombre, nombre.trim());
                cargarDatos();
            }
        });
    }

    @FXML
    private void editarItem(ActionEvent event) {
        DatosAuxiliares seleccionado = listaAuxiliares.getSelectionModel().getSelectedItem();
        String tipo = comboTipoDato.getValue();
        if (seleccionado == null || tipo == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog(seleccionado.getNombre());
        dialog.setTitle("Editar " + tipo);
        dialog.setHeaderText(null);
        dialog.setContentText("Nuevo nombre:");

        dialog.showAndWait().ifPresent(nuevo -> {
            if (!nuevo.trim().isEmpty()) {
                TablaAuxiliar config = configuraciones.get(tipo);
                dao.editar(config.tabla, config.columnaNombre, config.columnaId, seleccionado.getId(), nuevo.trim());
                cargarDatos();
            }
        });
    }

    @FXML
    private void eliminarItem(ActionEvent event) {
        DatosAuxiliares seleccionado = listaAuxiliares.getSelectionModel().getSelectedItem();
        String tipo = comboTipoDato.getValue();
        if (seleccionado == null || tipo == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar " + tipo);
        confirm.setHeaderText("¿Eliminar este registro?");
        confirm.setContentText(seleccionado.getNombre());

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                TablaAuxiliar config = configuraciones.get(tipo);
                dao.eliminar(config.tabla, config.columnaId, seleccionado.getId());
                cargarDatos();
            }
        });
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        actualizarPaginado();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            actualizarPaginado();
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        int total = (int) Math.ceil((double) datosFiltrados.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
            actualizarPaginado();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) datosFiltrados.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }

    /**
     * Clase interna para representar la configuración de cada tipo auxiliar.
     */
    private static class TablaAuxiliar {

        final String tabla;
        final String columnaId;
        final String columnaNombre;

        public TablaAuxiliar(String tabla, String columnaId, String columnaNombre) {
            this.tabla = tabla;
            this.columnaId = columnaId;
            this.columnaNombre = columnaNombre;
        }
    }
}
