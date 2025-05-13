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
    private Label lblId, lblNombre, lblTipo, lblTipoLabel, lblTabla;
    @FXML
    private Button btnLimpiar, btnAgregar, btnEditar, btnEliminar;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;

    private final DatosAuxiliaresDAO dao = new DatosAuxiliaresDAO();
    private final ObservableList<DatosAuxiliares> datos = FXCollections.observableArrayList();
    private List<DatosAuxiliares> datosFiltrados = new ArrayList<>();
    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;
    private List<String> tiposVisuales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiposVisuales = dao.obtenerTiposVisuales();
        tiposVisuales.removeIf(Objects::isNull);
        Collections.sort(tiposVisuales);
        tiposVisuales.add(0, "Todos");

        comboTipoDato.setItems(FXCollections.observableArrayList(tiposVisuales));
        comboTipoDato.setPromptText("Selecciona una opción...");
        comboTipoDato.getSelectionModel().clearSelection();

        comboTipoDato.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "Selecciona una opción..." : item);
            }
        });

        comboTipoDato.setOnAction(e -> {
            String tipoSeleccionado = comboTipoDato.getSelectionModel().getSelectedItem();
            if (tipoSeleccionado != null && !tipoSeleccionado.isBlank()) {
                cargarDatos(tipoSeleccionado);
            }
        });

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

        // ⚠️ Solo carga si es compatible
        cargarDatos("Todos");
    }

    private ListCell<String> crearCeldaCombo() {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Selecciona una opción..." : item);
            }
        };
    }

    private void cargarDatosDesdeCombo() {
        String tipo = comboTipoDato.getValue();
        if (tipo != null && !tipo.isBlank()) {
            cargarDatos(tipo);
        }
    }

    private void cargarDatos(String tipo) {
        datos.clear();
        if (tipo.equals("Todos")) {
            for (String visual : tiposVisuales) {
                if (!visual.equals("Todos")) {
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
        int totalPaginas = (int) Math.ceil((double) datosFiltrados.size() / ITEMS_POR_PAGINA);
        if (pagina < 1) {
            pagina = 1;
        }
        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }

        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, datosFiltrados.size());

        if (desde > hasta || desde >= datosFiltrados.size()) {
            desde = 0;
            hasta = Math.min(ITEMS_POR_PAGINA, datosFiltrados.size());
            pagina = 1;
        }

        List<DatosAuxiliares> paginaActualDatos = datosFiltrados.subList(desde, hasta);
        listaAuxiliares.setItems(FXCollections.observableArrayList(paginaActualDatos));
        listaAuxiliares.getSelectionModel().clearSelection();
        mostrarDetalle(null);

        paginaActual.setText(pagina + " / " + (totalPaginas == 0 ? 1 : totalPaginas));
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
        comboTipoDato.getSelectionModel().clearSelection();
        comboTipoDato.setValue(null);  // fuerza deselección visual
        comboTipoDato.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "Selecciona una opción..." : item);
            }
        });

        listaAuxiliares.getSelectionModel().clearSelection();
        mostrarDetalle(null);
        datos.clear();
        datosFiltrados.clear();
        pagina = 1;
        paginaActual.setText("1");
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
                cargarDatos("Todos");
                if (guardado != null) {
                    datos.stream()
                            .filter(d -> d.getId() == guardado.getId() && Objects.equals(d.getTipoVisual(), guardado.getTipoVisual()))
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

        String tipo = seleccionado.getTipoVisual();
        if (tipo == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar " + tipo);
        confirm.setHeaderText("¿Eliminar este registro?");
        confirm.setContentText(seleccionado.getNombre());

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                dao.eliminar(tipo, seleccionado.getId());
                cargarDatos("Todos");
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
