package controllers;

import dao.MetasTwitchDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.MetasTwitch;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.util.StringConverter;

public class FormMetasTwitchController implements Initializable {

    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtMeta;
    @FXML
    private TextField txtActual;
    @FXML
    private DatePicker pickerInicio;
    @FXML
    private DatePicker pickerFin;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Runnable onGuardarCallback;
    private final MetasTwitchDAO dao = new MetasTwitchDAO();
    private MetasTwitch metaExistente;
    private MetasTwitch metaGuardada;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarDatePickers();
    }

    public void setOnGuardarCallback(Runnable callback) {
        this.onGuardarCallback = callback;
    }

    public void setMeta(MetasTwitch meta) {
        this.metaExistente = meta;
        txtDescripcion.setText(meta.getDescripcion());
        txtMeta.setText(String.valueOf(meta.getMeta()));
        txtActual.setText(String.valueOf(meta.getActual()));
        pickerInicio.setValue(meta.getFechaInicio());
        pickerFin.setValue(meta.getFechaFin());
    }

    public MetasTwitch getMetaGuardada() {
        return metaGuardada;
    }

    @FXML
    private void guardarMeta(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        MetasTwitch meta = new MetasTwitch(
                metaExistente != null ? metaExistente.getIdMeta() : 0,
                txtDescripcion.getText().trim(),
                Integer.parseInt(txtMeta.getText().trim()),
                Integer.parseInt(txtActual.getText().trim()),
                pickerInicio.getValue(),
                pickerFin.getValue(),
                LocalDate.now()
        );

        boolean exito = false;

        if (metaExistente == null) {
            Integer id = dao.insertarMetaYDevolverId(meta);
            if (id != null) {
                meta.setIdMeta(id);
                metaGuardada = meta;
                mostrarInfo("Meta registrada correctamente.");
                exito = true;
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmación de edición");
            alerta.setHeaderText("¿Deseas actualizar esta meta?");
            alerta.setContentText("Esta acción reemplazará la información existente.");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                exito = dao.actualizarMeta(meta);
                if (exito) {
                    metaGuardada = meta;
                    mostrarInfo("Meta actualizada correctamente.");
                }
            }
        }

        if (exito) {
            if (onGuardarCallback != null) {
                onGuardarCallback.run();
            }
            cerrarVentana();
        } else {
            mostrarAlerta(metaExistente != null ? "No se pudo actualizar la meta." : "No se pudo registrar la meta.");
        }
    }

    private boolean validarCampos() {
        if (txtDescripcion.getText().trim().isEmpty()
                || txtMeta.getText().trim().isEmpty()
                || txtActual.getText().trim().isEmpty()
                || pickerInicio.getValue() == null
                || pickerFin.getValue() == null) {
            mostrarAlerta("Todos los campos deben estar completos.");
            return false;
        }
        try {
            Integer.parseInt(txtMeta.getText().trim());
            Integer.parseInt(txtActual.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Meta y Actual deben ser números válidos.");
            return false;
        }
        return true;
    }

    private void configurarDatePickers() {
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                try {
                    return LocalDate.parse(string, formatter);
                } catch (Exception e) {
                    mostrarAlerta("Formato de fecha inválido. Usa dd/MM/yyyy");
                    return null;
                }
            }
        };
        pickerInicio.setConverter(converter);
        pickerFin.setConverter(converter);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
