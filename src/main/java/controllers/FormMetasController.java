package controllers;

import dao.InicioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import models.ConfiguracionAuxiliar;
import models.DatosAuxiliares;

public class FormMetasController {

    private Runnable onGuardarCallback;

    @FXML private AnchorPane formularioMetas;
    @FXML private VBox vboxConfiguracion;
    @FXML private TextField filtroTablasAuxiliares;
    @FXML private ComboBox<ConfiguracionAuxiliar> comboTablas;
    @FXML private TextField filtroCampoAuxiliar;
    @FXML private ComboBox<DatosAuxiliares> comboCampos;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private HBox botonera;

    private final InicioDAO dao = new InicioDAO();
    private ObservableList<ConfiguracionAuxiliar> configuracionesOriginales = FXCollections.observableArrayList();
    private ObservableList<DatosAuxiliares> camposOriginales = FXCollections.observableArrayList();

    public void initialize() {
        cargarComboTablas();
        comboTablas.setOnAction(this::actualizarComboCampos);

        // Configurar cómo se muestra cada objeto DatosAuxiliares
        comboCampos.setConverter(new StringConverter<>() {
            @Override
            public String toString(DatosAuxiliares object) {
                return (object != null) ? object.getNombre() : "";
            }

            @Override
            public DatosAuxiliares fromString(String string) {
                return null;
            }
        });
    }

    public void setOnGuardarCallback(Runnable callback) {
        this.onGuardarCallback = callback;
    }

    private void cargarComboTablas() {
        configuracionesOriginales = dao.obtenerConfiguracionesDeMetas();
        comboTablas.setItems(configuracionesOriginales);
    }

    private void actualizarComboCampos(ActionEvent event) {
        ConfiguracionAuxiliar seleccion = comboTablas.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            camposOriginales = dao.obtenerDatosPorConfiguracion(seleccion);
            comboCampos.setItems(camposOriginales);
            filtroCampoAuxiliar.clear();
            if (!camposOriginales.isEmpty()) {
                comboCampos.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    private void handleTablasAuxiliares(ActionEvent event) {
        String texto = filtroTablasAuxiliares.getText().toLowerCase().trim();
        if (texto.isEmpty()) {
            comboTablas.setItems(configuracionesOriginales);
        } else {
            ObservableList<ConfiguracionAuxiliar> filtradas = FXCollections.observableArrayList();
            for (ConfiguracionAuxiliar config : configuracionesOriginales) {
                if (config.getNombreVisual().toLowerCase().contains(texto)) {
                    filtradas.add(config);
                }
            }
            comboTablas.setItems(filtradas);
        }
    }

    @FXML
    private void handlefiltroCampoAuxiliar(ActionEvent event) {
        String texto = filtroCampoAuxiliar.getText().toLowerCase().trim();
        if (texto.isEmpty()) {
            comboCampos.setItems(camposOriginales);
        } else {
            ObservableList<DatosAuxiliares> filtrados = FXCollections.observableArrayList();
            for (DatosAuxiliares campo : camposOriginales) {
                if (campo.getNombre().toLowerCase().contains(texto)) {
                    filtrados.add(campo);
                }
            }
            comboCampos.setItems(filtrados);
        }
    }

    @FXML
    private void guardarDatoAuxiliar(ActionEvent event) {
        ConfiguracionAuxiliar config = comboTablas.getValue();
        DatosAuxiliares seleccionado = comboCampos.getValue();

        if (config == null || seleccionado == null) {
            mostrarAlerta("Debe seleccionar una tabla y un valor.");
            return;
        }

        boolean exito = dao.asignarConfiguracionAuxiliar(config.getNombreTabla(), config.getColumnaId(), seleccionado.getId());

        if (exito) {
            mostrarInfo("Asignación registrada correctamente.");
            if (onGuardarCallback != null) {
                onGuardarCallback.run();
            }
            formularioMetas.getScene().getWindow().hide();
        } else {
            mostrarAlerta("Error al guardar la asignación.");
        }
    }

    @FXML
    private void cancearDatoAuxiliar(ActionEvent event) {
        formularioMetas.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Advertencia");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void preseleccionarConfiguracion(ConfiguracionAuxiliar seleccionPredefinida) {
        if (seleccionPredefinida == null) return;

        cargarComboTablas();
        comboTablas.setValue(seleccionPredefinida);
        camposOriginales = dao.obtenerDatosPorConfiguracion(seleccionPredefinida);
        comboCampos.setItems(camposOriginales);

        if (!camposOriginales.isEmpty()) {
            comboCampos.getSelectionModel().selectFirst();
        }
    }
}
