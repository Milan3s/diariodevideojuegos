package controllers;

import dao.MetasTwitchDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.MetasTwitch;

import java.time.LocalDate;
import java.util.function.Consumer;

public class FormMetasTwitchController {

    @FXML
    private TextField txtDescripcion, txtMeta, txtActual, txtMes, txtAnio;
    @FXML
    private DatePicker pickerInicio, pickerFin;
    @FXML
    private Button btnGuardar, btnCancelar;

    private Runnable onGuardarCallback;
    private final MetasTwitchDAO dao = new MetasTwitchDAO();
    private MetasTwitch metaExistente;

    public void setOnGuardarCallback(Runnable callback) {
        this.onGuardarCallback = callback;
    }

    public void setMeta(MetasTwitch meta) {
        this.metaExistente = meta;

        txtDescripcion.setText(meta.getDescripcion());
        txtMeta.setText(String.valueOf(meta.getMeta()));
        txtActual.setText(String.valueOf(meta.getActual()));
        txtMes.setText(meta.getMes());
        txtAnio.setText(String.valueOf(meta.getAnio()));
        pickerInicio.setValue(meta.getFechaInicio());
        pickerFin.setValue(meta.getFechaFin());
    }

    private void guardar(ActionEvent event) {
        if (!validarCampos()) return;

        MetasTwitch meta = new MetasTwitch(
                metaExistente != null ? metaExistente.getIdMeta() : 0,
                txtDescripcion.getText(),
                Integer.parseInt(txtMeta.getText()),
                Integer.parseInt(txtActual.getText()),
                txtMes.getText(),
                Integer.parseInt(txtAnio.getText()),
                pickerInicio.getValue(),
                pickerFin.getValue(),
                LocalDate.now()
        );

        boolean exito;
        if (metaExistente == null) {
            exito = dao.insertarMetaYDevolverId(meta) != null;
        } else {
            exito = dao.actualizarMeta(meta);
        }

        if (exito) {
            if (onGuardarCallback != null) {
                onGuardarCallback.run();
            }
            cerrarVentana();
        } else {
            mostrarAlerta("Error al guardar la meta.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private boolean validarCampos() {
        if (txtDescripcion.getText().isEmpty() || txtMeta.getText().isEmpty() ||
            txtActual.getText().isEmpty() || txtMes.getText().isEmpty() || txtAnio.getText().isEmpty() ||
            pickerInicio.getValue() == null || pickerFin.getValue() == null) {
            mostrarAlerta("Todos los campos deben estar completos.");
            return false;
        }

        try {
            Integer.parseInt(txtMeta.getText());
            Integer.parseInt(txtActual.getText());
            Integer.parseInt(txtAnio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Meta, Actual y Año deben ser números válidos.");
            return false;
        }

        return true;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void guardarMeta(ActionEvent event) {
    }
}