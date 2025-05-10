package controllers;

import dao.MetasTwitchDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.MetasTwitch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.util.StringConverter;

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
    private MetasTwitch metaGuardada;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void initialize() {
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
        txtMes.setText(meta.getMes());
        txtAnio.setText(String.valueOf(meta.getAnio()));
        pickerInicio.setValue(meta.getFechaInicio());
        pickerFin.setValue(meta.getFechaFin());
    }

    public MetasTwitch getMetaGuardada() {
        return metaGuardada;
    }

    @FXML
    private void guardarMeta(ActionEvent event) {
        guardar(event);
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

        boolean exito = false;

        if (metaExistente == null) {
            Integer idInsertado = dao.insertarMetaYDevolverId(meta);
            if (idInsertado != null) {
                meta.setIdMeta(idInsertado);
                metaGuardada = meta;
                mostrarInfo("Meta registrada correctamente.");
                exito = true;
            }
        } else {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar actualización");
            confirmacion.setHeaderText("¿Deseas actualizar esta meta?");
            confirmacion.setContentText("Se sobrescribirá la información actual.");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
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
        } else if (metaExistente != null) {
            mostrarAlerta("No se pudo actualizar la meta.");
        } else {
            mostrarAlerta("No se pudo registrar la meta.");
        }
    }

    private boolean validarCampos() {
        if (txtDescripcion.getText().isEmpty() || txtMeta.getText().isEmpty()
                || txtActual.getText().isEmpty() || txtMes.getText().isEmpty() || txtAnio.getText().isEmpty()
                || pickerInicio.getValue() == null || pickerFin.getValue() == null) {
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

    private void configurarDatePickers() {
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;
                try {
                    return LocalDate.parse(string, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e1) {
                    try {
                        return LocalDate.parse(string, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    } catch (Exception e2) {
                        mostrarAlerta("Formato de fecha inválido. Usa dd/MM/yyyy o dd-MM-yyyy");
                        return null;
                    }
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
