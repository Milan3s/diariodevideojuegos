package controllers;

import dao.DatosAuxiliaresDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.DatosAuxiliares;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public class FormDatosAuxiliaresController implements Initializable {

    @FXML
    private Label lblTitulo;
    @FXML
    private ComboBox<String> comboTablaDestino;
    @FXML
    private TextField campoNombre;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private AnchorPane formularioBase;

    private final DatosAuxiliaresDAO dao = new DatosAuxiliaresDAO();
    private DatosAuxiliares datoExistente;
    private Runnable onGuardarCallback;
    private DatosAuxiliares datoGuardado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboTablaDestino.setItems(FXCollections.observableArrayList(dao.obtenerTiposVisuales()));
        comboTablaDestino.setPromptText("Selecciona tabla de destino");
    }

    public void configurarFormulario(DatosAuxiliares dato, String tipoPreseleccionado, Runnable callback) {
        this.datoExistente = dato;
        this.onGuardarCallback = callback;

        if (tipoPreseleccionado != null) {
            comboTablaDestino.getSelectionModel().select(tipoPreseleccionado);
        }

        if (dato != null) {
            campoNombre.setText(dato.getNombre());
            lblTitulo.setText("Editar " + tipoPreseleccionado);
        } else {
            lblTitulo.setText("Agregar nuevo dato auxiliar");
        }
    }

    @FXML
    private void guardarDatoAuxiliar(ActionEvent event) {
        String tipoSeleccionado = comboTablaDestino.getValue();
        String nombre = campoNombre.getText().trim();

        if (tipoSeleccionado == null) {
            mostrarAlerta("Debes seleccionar la tabla destino.");
            return;
        }

        if (nombre.isEmpty()) {
            mostrarAlerta("El campo de nombre no puede estar vacío.");
            return;
        }

        if (datoExistente == null && dao.existeRegistro(tipoSeleccionado, nombre)) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Registro existente");
            confirm.setHeaderText("Ya existe un dato con ese nombre en la tabla.");
            confirm.setContentText("¿Deseas sobrescribirlo?");
            
            ButtonType btnSobrescribir = new ButtonType("Sobrescribir", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            confirm.getButtonTypes().setAll(btnSobrescribir, btnCancelar);
            
            confirm.showAndWait().ifPresent(respuesta -> {
                if (respuesta == btnSobrescribir) {
                    sobrescribirDato(tipoSeleccionado, nombre);
                }
            });

            return;
        }

        guardarODetener(tipoSeleccionado, nombre);
    }

    private void sobrescribirDato(String tipo, String nombre) {
        DatosAuxiliares existente = dao.buscarPorNombre(tipo, nombre);
        if (existente != null) {
            dao.editar(tipo, existente.getId(), nombre);
            if (onGuardarCallback != null) onGuardarCallback.run();
            cerrarVentana();
        }
    }

    private void guardarODetener(String tipo, String nombre) {
        boolean ok;
        if (datoExistente == null) {
            int idNuevo = dao.insertarYObtenerId(tipo, nombre);
            if (idNuevo > 0) {
                datoGuardado = new DatosAuxiliares(idNuevo, nombre, null, tipo);
                if (onGuardarCallback != null) {
                    onGuardarCallback.run();
                }
                cerrarVentana();
            } else {
                mostrarAlerta("Error al guardar los datos.");
            }
        } else {
            ok = dao.editar(tipo, datoExistente.getId(), nombre);
            if (ok) {
                if (onGuardarCallback != null) {
                    onGuardarCallback.run();
                }
                cerrarVentana();
            } else {
                mostrarAlerta("Error al guardar los datos.");
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) campoNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public DatosAuxiliares getDatoGuardado() {
        return datoGuardado;
    }
}
