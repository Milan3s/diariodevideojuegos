package controllers;

import dao.DatosAuxiliaresDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DatosAuxiliares;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
    private Label paginaActual;
    @FXML
    private Label lblId;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblTipo;
    @FXML
    private Label lblTipoLabel;
    @FXML
    private Label lblTabla;

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

    private final DatosAuxiliaresDAO dao = new DatosAuxiliaresDAO();
    private final ObservableList<DatosAuxiliares> datos = FXCollections.observableArrayList();
    private List<DatosAuxiliares> datosFiltrados = new ArrayList<>();
    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> tiposVisuales = dao.obtenerTiposVisuales();
        tiposVisuales.removeIf(Objects::isNull);
        Collections.sort(tiposVisuales);
        tiposVisuales.add(0, "Todos");

        comboTipoDato.setItems(FXCollections.observableArrayList(tiposVisuales));
        comboTipoDato.setPromptText("Seleccione una opción");

        comboTipoDato.setOnAction(e -> cargarDatos());
        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        listaAuxiliares.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> mostrarDetalle(nuevo));

        listaAuxiliares.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(DatosAuxiliares item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String tipo = item.getTipo() != null ? " | Tipo: " + item.getTipo() : "";
                    String tabla = item.getTipoVisual() != null ? " | Tabla: " + item.getTipoVisual() : "";
                    setText(item.getNombre() + tipo + tabla);
                }
            }
        });

        comboTipoDato.getSelectionModel().clearSelection();
        cargarDatos("Todos");
    }

    private void cargarDatos() {
        String tipo = comboTipoDato.getValue();
        cargarDatos(tipo != null ? tipo : "Todos");
    }

    private void cargarDatos(String tipo) {
        if (tipo == null || tipo.equals("Seleccione una opción")) {
            return;
        }

        datos.clear();

        if (tipo.equals("Todos")) {
            for (String visual : dao.obtenerTiposVisuales()) {
                if (!visual.equals("Todos") && !visual.equals("Seleccione una opción")) {
                    List<DatosAuxiliares> lista = dao.listar(visual);
                    for (DatosAuxiliares da : lista) {
                        da.setTipoVisual(visual);
                    }
                    datos.addAll(lista);
                }
            }
        } else {
            datos.setAll(dao.listar(tipo).stream().peek(d -> d.setTipoVisual(tipo)).collect(Collectors.toList()));
        }

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

        // No seleccionar ningún ítem
        listaAuxiliares.getSelectionModel().clearSelection();
        mostrarDetalle(null); // Limpia el panel de detalles
    }

    private void mostrarDetalle(DatosAuxiliares dato) {
        if (dato != null) {
            lblId.setText(String.valueOf(dato.getId()));
            lblNombre.setText(dato.getNombre());
            lblTabla.setText(dato.getTipoVisual() != null ? dato.getTipoVisual() : "-");

            boolean tieneTipo = dato.getTipo() != null && !dato.getTipo().isBlank();
            lblTipo.setText(tieneTipo ? dato.getTipo() : "");
            lblTipo.setVisible(tieneTipo);
            lblTipoLabel.setVisible(tieneTipo);
        } else {
            lblId.setText("");
            lblNombre.setText("");
            lblTipo.setText("");
            lblTabla.setText("");
            lblTipo.setVisible(false);
            lblTipoLabel.setVisible(false);
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        campoBusqueda.clear();
        listaAuxiliares.getItems().clear();
        datos.clear();
        datosFiltrados.clear();
        paginaActual.setText("1");
        mostrarDetalle(null);

        comboTipoDato.getSelectionModel().clearSelection();
        comboTipoDato.setValue(null); // Esto ayuda a mostrar el promptText
        comboTipoDato.setPromptText("Seleccione una opción");

        cargarDatos("Todos");
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        mostrarFormulario(null);
    }

    @FXML
    private void editarItem(ActionEvent event) {
        DatosAuxiliares seleccionado = listaAuxiliares.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            mostrarFormulario(seleccionado);
        }
    }

    private void mostrarFormulario(DatosAuxiliares dato) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormDatosAuxiliares.fxml"));
            AnchorPane root = loader.load();

            FormDatosAuxiliaresController controller = loader.getController();
            String tipo = dato != null ? dato.getTipoVisual() : comboTipoDato.getValue();

            controller.configurarFormulario(dato, tipo, () -> {
                DatosAuxiliares guardado = controller.getDatoGuardado();

                cargarDatos();
                if (guardado != null) {
                    // Seleccionar el dato recién guardado
                    datos.stream()
                            .filter(d -> d.getId() == guardado.getId() && Objects.equals(d.getTipoVisual(), guardado.getTipoVisual()))
                            .findFirst()
                            .ifPresent(d -> listaAuxiliares.getSelectionModel().select(d));
                } else if (dato != null) {
                    // En modo edición, seleccionar el mismo
                    datos.stream()
                            .filter(d -> d.getId() == dato.getId() && Objects.equals(d.getTipoVisual(), tipo))
                            .findFirst()
                            .ifPresent(d -> listaAuxiliares.getSelectionModel().select(d));
                }
            });

            Stage stage = new Stage();
            stage.setTitle(dato == null ? "Agregar dato auxiliar" : "Editar dato auxiliar");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo cargar el formulario.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarItem(ActionEvent event) {
        DatosAuxiliares seleccionado = listaAuxiliares.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }

        String tipo = comboTipoDato.getValue();
        if (tipo == null || tipo.equals("Todos")) {
            tipo = seleccionado.getTipoVisual();
        }

        if (tipo == null) {
            return;
        }

        final String tipoFinal = tipo;
        final DatosAuxiliares seleccionadoFinal = seleccionado;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar " + tipoFinal);
        confirm.setHeaderText("¿Eliminar este registro?");
        confirm.setContentText(seleccionadoFinal.getNombre());

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                dao.eliminar(tipoFinal, seleccionadoFinal.getId());
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
}
