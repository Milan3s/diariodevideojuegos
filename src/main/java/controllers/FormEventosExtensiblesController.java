package controllers;

import dao.EventosExtensiblesDAO;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.EventoExtensible;
import javafx.util.StringConverter;

public class FormEventosExtensiblesController implements Initializable {

    @FXML private AnchorPane formularioEvento;
    @FXML private GridPane gridFormulario;
    @FXML private TextField txtMotivo;
    @FXML private DatePicker dateFechaEvento;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private EventoExtensible eventoEditar = null;
    private final DateTimeFormatter formatoVisual = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter formatoBD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Runnable onGuardarCallback;

    public void setOnGuardarCallback(Runnable callback) {
        this.onGuardarCallback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarDatePicker();
    }

    private void configurarDatePicker() {
        dateFechaEvento.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatoVisual.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatoVisual);
                }
                return null;
            }
        });
    }

    public void setModoAgregar() {
        this.eventoEditar = null;
        txtMotivo.setText("");
        dateFechaEvento.setValue(null);
    }

    public void setEventoEditar(EventoExtensible evento) {
        this.eventoEditar = evento;
        txtMotivo.setText(evento.getMotivo());

        try {
            LocalDate fecha = LocalDate.parse(evento.getFechaEvento(), formatoBD);
            dateFechaEvento.setValue(fecha);
        } catch (Exception e) {
            dateFechaEvento.setValue(null);
        }
    }

    @FXML
    private void guardarEvento(ActionEvent event) {
        String motivo = txtMotivo.getText().trim();
        LocalDate fecha = dateFechaEvento.getValue();

        if (motivo.isEmpty() || fecha == null) {
            mostrarAlerta("Debe ingresar motivo y fecha.");
            return;
        }

        String fechaBD = fecha.format(formatoBD);

        boolean existeDuplicado = EventosExtensiblesDAO.existePorMotivoYFecha(motivo, fechaBD);

        if (eventoEditar == null) {
            if (existeDuplicado) {
                mostrarConfirmacionSobrescribir(motivo, fechaBD);
            } else {
                guardarODetener(motivo, fechaBD);
            }
        } else {
            boolean sinCambios = eventoEditar.getMotivo().equalsIgnoreCase(motivo)
                               && eventoEditar.getFechaEvento().equals(fechaBD);

            // Si hay otro evento con mismo motivo y fecha pero no es este
            boolean duplicadoExterno = existeDuplicado &&
                    !(eventoEditar.getMotivo().equalsIgnoreCase(motivo)
                    && eventoEditar.getFechaEvento().equals(fechaBD));

            if (duplicadoExterno) {
                mostrarAlerta("Ya existe otro evento con el mismo motivo y fecha.");
                return;
            }

            if (sinCambios) {
                mostrarInfo("No se realizaron cambios.");
                cerrarVentana();
            } else {
                guardarODetener(motivo, fechaBD);
            }
        }
    }

    private void mostrarConfirmacionSobrescribir(String motivo, String fechaBD) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Registro existente");
        confirm.setHeaderText("Ya existe un evento con ese motivo y fecha.");
        confirm.setContentText("¿Deseas sobrescribirlo?");

        ButtonType sobrescribir = new ButtonType("Sobrescribir", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(sobrescribir, cancelar);

        confirm.showAndWait().ifPresent(respuesta -> {
            if (respuesta.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                sobrescribirEvento(motivo, fechaBD);
            }
        });
    }

    private void sobrescribirEvento(String motivo, String fechaBD) {
        EventoExtensible existente = EventosExtensiblesDAO.buscarPorMotivoYFecha(motivo, fechaBD);
        if (existente != null) {
            existente.setMotivo(motivo);
            existente.setFechaEvento(fechaBD);
            boolean actualizado = EventosExtensiblesDAO.actualizar(existente);

            if (actualizado) {
                mostrarInfo("Evento sobrescrito correctamente.");
                ejecutarCallbackYCerrar();
            } else {
                mostrarAlerta("Error al sobrescribir el evento.");
            }
        } else {
            mostrarAlerta("No se encontró el evento para sobrescribir.");
        }
    }

    private void guardarODetener(String motivo, String fechaBD) {
        if (eventoEditar == null) {
            EventoExtensible nuevo = new EventoExtensible(motivo, fechaBD);
            boolean ok = EventosExtensiblesDAO.insertar(nuevo);
            if (ok) {
                mostrarInfo("Evento agregado correctamente.");
                ejecutarCallbackYCerrar();
            } else {
                mostrarAlerta("Error al insertar el evento.");
            }
        } else {
            eventoEditar.setMotivo(motivo);
            eventoEditar.setFechaEvento(fechaBD);
            boolean ok = EventosExtensiblesDAO.actualizar(eventoEditar);
            if (ok) {
                mostrarInfo("Evento actualizado correctamente.");
                ejecutarCallbackYCerrar();
            } else {
                mostrarAlerta("Error al actualizar el evento.");
            }
        }
    }

    private void ejecutarCallbackYCerrar() {
        if (onGuardarCallback != null) {
            onGuardarCallback.run();
        }
        cerrarVentana();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) formularioEvento.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
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
}
