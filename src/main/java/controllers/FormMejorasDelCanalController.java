package controllers;

import dao.MejorasDelCanalDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.MejorasDelCanal;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.util.StringConverter;

public class FormMejorasDelCanalController implements Initializable {

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtMeta;
    @FXML private TextField txtActual;
    @FXML private DatePicker pickerFechaInicio;
    @FXML private DatePicker pickerFechaFin;
    @FXML private RadioButton radioCumplidaSi;
    @FXML private RadioButton radioCumplidaNo;
    @FXML private ToggleGroup cumplidaGroup;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private MejorasDelCanal mejoraEnEdicion;
    private final MejorasDelCanalDAO dao = new MejorasDelCanalDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioCumplidaSi.setToggleGroup(cumplidaGroup);
        radioCumplidaNo.setToggleGroup(cumplidaGroup);
        radioCumplidaNo.setSelected(true);
        configurarDatePickers();
    }

    private void configurarDatePickers() {
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        };

        pickerFechaInicio.setConverter(converter);
        pickerFechaFin.setConverter(converter);
    }

    public void limpiarFormulario() {
        mejoraEnEdicion = null;
        txtDescripcion.clear();
        txtMeta.clear();
        txtActual.clear();
        pickerFechaInicio.setValue(null);
        pickerFechaFin.setValue(null);
        radioCumplidaNo.setSelected(true);
    }

    public void cargarMejoraParaEditar(MejorasDelCanal mejora) {
        if (mejora != null) {
            mejoraEnEdicion = mejora;
            txtDescripcion.setText(mejora.getDescripcion());
            txtMeta.setText(String.valueOf(mejora.getMeta()));
            txtActual.setText(String.valueOf(mejora.getActual()));
            pickerFechaInicio.setValue(mejora.getFechaInicio());
            pickerFechaFin.setValue(mejora.getFechaFin());
            if (mejora.isCumplida()) {
                radioCumplidaSi.setSelected(true);
            } else {
                radioCumplidaNo.setSelected(true);
            }
        }
    }

    @FXML
    private void guardarMejora() {
        String descripcion = txtDescripcion.getText().trim();
        String metaText = txtMeta.getText().trim();
        String actualText = txtActual.getText().trim();
        LocalDate fechaInicio = pickerFechaInicio.getValue();
        LocalDate fechaFin = pickerFechaFin.getValue();
        boolean cumplida = radioCumplidaSi.isSelected();

        if (descripcion.isEmpty() || metaText.isEmpty() || actualText.isEmpty()
                || fechaInicio == null || fechaFin == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        int meta, actual;
        try {
            meta = Integer.parseInt(metaText);
            actual = Integer.parseInt(actualText);
        } catch (NumberFormatException e) {
            mostrarAlerta("Meta y Actual deben ser números válidos.");
            return;
        }

        int idEstado = dao.obtenerIdEstadoCumplidaDesdeNombre(cumplida ? "Sí" : "No");
        if (idEstado == -1) {
            mostrarAlerta("No se encontró el estado 'Cumplida'. Verifica la tabla estado_cumplida.");
            return;
        }

        boolean exito;
        Integer idGenerado = null;

        if (mejoraEnEdicion == null) {
            MejorasDelCanal nueva = new MejorasDelCanal(descripcion, meta, actual, fechaInicio, fechaFin, cumplida);
            nueva.setIdEstadoCumplida(idEstado);
            idGenerado = dao.insertarMejoraYDevolverId(nueva);
            exito = (idGenerado != null);
        } else {
            if (!confirmarEdicion()) {
                return;
            }
            mejoraEnEdicion.setDescripcion(descripcion);
            mejoraEnEdicion.setMeta(meta);
            mejoraEnEdicion.setActual(actual);
            mejoraEnEdicion.setFechaInicio(fechaInicio);
            mejoraEnEdicion.setFechaFin(fechaFin);
            mejoraEnEdicion.setCumplida(cumplida);
            mejoraEnEdicion.setIdEstadoCumplida(idEstado);
            exito = dao.actualizarMejora(mejoraEnEdicion);
        }

        if (exito) {
            mostrarConfirmacion(mejoraEnEdicion == null ? "Mejora añadida correctamente." : "Mejora actualizada correctamente.");
            if (idGenerado != null) {
                MejorasDelCanal insertada = new MejorasDelCanal(idGenerado, descripcion, meta, actual, fechaInicio, fechaFin, cumplida, idEstado, cumplida ? "Sí" : "No");
                btnGuardar.getScene().getRoot().setUserData(insertada);
            } else {
                btnGuardar.getScene().getRoot().setUserData(mejoraEnEdicion);
            }
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo guardar la mejora.");
        }
    }

    private boolean confirmarEdicion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar edición");
        alert.setHeaderText("Se sobrescribirán los datos de la mejora");
        alert.setContentText("¿Deseas continuar con la modificación?");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
