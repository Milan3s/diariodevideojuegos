package controllers;

import dao.MetasEspecificasDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.MetasEspecificas;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FormMetasEspecificasController {

    @FXML private TextField campoDescripcion;
    @FXML private TextField campoJuegosObjetivo;
    @FXML private TextField campoJuegosCompletados;
    @FXML private TextField campoFabricante;
    @FXML private ComboBox<String[]> comboConsolas;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private DatePicker fechaRegistroPicker;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    @FXML private RadioButton radioCumplidaSi;
    @FXML private RadioButton radioCumplidaNo;

    private final ToggleGroup grupoCumplida = new ToggleGroup();

    private final MetasEspecificasDAO dao = new MetasEspecificasDAO();
    private Runnable onGuardarCallback;
    private MetasEspecificas metaExistente;
    private MetasEspecificas metaGuardada;

    public void initialize() {
        cargarConsolas();

        radioCumplidaSi.setToggleGroup(grupoCumplida);
        radioCumplidaNo.setToggleGroup(grupoCumplida);
        radioCumplidaNo.setSelected(true); // Selección por defecto
    }

    public void setOnGuardarCallback(Runnable callback) {
        this.onGuardarCallback = callback;
    }

    public MetasEspecificas getMetaGuardada() {
        return metaGuardada;
    }

    public void setMetaExistente(MetasEspecificas meta) {
        this.metaExistente = meta;
        cargarDatosEnFormulario(meta);
    }

    private void cargarConsolas() {
        List<String[]> consolas = dao.obtenerConsolas();
        comboConsolas.setItems(FXCollections.observableArrayList(consolas));
        comboConsolas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item[1]);
            }
        });
        comboConsolas.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item[1]);
            }
        });
    }

    private void cargarDatosEnFormulario(MetasEspecificas meta) {
        campoDescripcion.setText(meta.getDescripcion());
        campoJuegosObjetivo.setText(String.valueOf(meta.getJuegosObjetivo()));
        campoJuegosCompletados.setText(String.valueOf(meta.getJuegosCompletados()));
        campoFabricante.setText(meta.getFabricante());
        fechaInicioPicker.setValue(meta.getFechaInicio());
        fechaFinPicker.setValue(meta.getFechaFin());
        fechaRegistroPicker.setValue(meta.getFechaRegistro());

        // Seleccionar radio correspondiente
        if (meta.isCumplida()) {
            radioCumplidaSi.setSelected(true);
        } else {
            radioCumplidaNo.setSelected(true);
        }

        for (String[] consola : comboConsolas.getItems()) {
            if (Integer.parseInt(consola[0]) == meta.getConsolaId()) {
                comboConsolas.getSelectionModel().select(consola);
                break;
            }
        }
    }

    @FXML
    private void guardarMeta() {
        MetasEspecificas meta = construirDesdeFormulario();

        if (meta == null) {
            mostrarAlerta("Faltan campos obligatorios o hay datos inválidos.");
            return;
        }

        boolean continuar = true;

        if (metaExistente != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar edición");
            confirmacion.setHeaderText("Estás a punto de sobrescribir esta meta.");
            confirmacion.setContentText("¿Deseas continuar y guardar los cambios?");
            ButtonType resultado = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

            if (resultado != ButtonType.OK) {
                continuar = false;
            }
        }

        if (continuar) {
            if (metaExistente != null) {
                meta.setId(metaExistente.getId());
                dao.actualizar(meta);
            } else {
                dao.insertar(meta);
            }

            metaGuardada = meta;

            mostrarInfo("La meta se guardó correctamente.");
            cerrarVentana();

            if (onGuardarCallback != null) {
                onGuardarCallback.run();
            }
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private MetasEspecificas construirDesdeFormulario() {
        try {
            String descripcion = campoDescripcion.getText().trim();
            int juegosObjetivo = Integer.parseInt(campoJuegosObjetivo.getText().trim());
            int juegosCompletados = Integer.parseInt(campoJuegosCompletados.getText().trim());
            boolean cumplida = radioCumplidaSi.isSelected();
            String fabricante = campoFabricante.getText().trim();
            LocalDate fechaInicio = fechaInicioPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();
            LocalDate fechaRegistro = fechaRegistroPicker.getValue() != null ? fechaRegistroPicker.getValue() : LocalDate.now();

            String[] consolaSeleccionada = comboConsolas.getSelectionModel().getSelectedItem();
            if (descripcion.isEmpty() || fechaInicio == null || fechaFin == null || consolaSeleccionada == null) {
                return null;
            }

            int idConsola = Integer.parseInt(consolaSeleccionada[0]);
            String nombreConsola = consolaSeleccionada[1];

            return new MetasEspecificas(
                0,
                descripcion,
                juegosObjetivo,
                juegosCompletados,
                cumplida,
                fabricante,
                idConsola,
                nombreConsola,
                fechaInicio,
                fechaFin,
                fechaRegistro
            );

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void abrirAgregarConsola(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormConsolas.fxml"));
            Parent root = loader.load();
            FormConsolasController controller = loader.getController();
            controller.limpiarFormulario();

            controller.setOnGuardarCallback(() -> {
                cargarConsolas();
                seleccionarUltimaConsola();
            });

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Añadir Consola");
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seleccionarUltimaConsola() {
        String[] ultima = dao.obtenerUltimaConsola();
        if (ultima != null) {
            for (String[] consola : comboConsolas.getItems()) {
                if (consola[0].equals(ultima[0])) {
                    comboConsolas.getSelectionModel().select(consola);
                    break;
                }
            }
        }
    }
}
