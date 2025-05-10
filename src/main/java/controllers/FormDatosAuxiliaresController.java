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

public class FormDatosAuxiliaresController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private ComboBox<String> comboTipoDato;
    @FXML private TextField campoNombre;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final DatosAuxiliaresDAO dao = new DatosAuxiliaresDAO();
    private DatosAuxiliares datoExistente;
    private Runnable onGuardarCallback;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboTipoDato.setItems(FXCollections.observableArrayList(dao.obtenerTiposVisuales()));
    }

    public void configurarFormulario(DatosAuxiliares dato, String tipoPreseleccionado, Runnable callback) {
        this.datoExistente = dato;
        this.onGuardarCallback = callback;

        if (tipoPreseleccionado != null) {
            comboTipoDato.getSelectionModel().select(tipoPreseleccionado);
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
        String tipoSeleccionado = comboTipoDato.getValue();
        String nombre = campoNombre.getText().trim();

        if (tipoSeleccionado == null) {
            mostrarAlerta("Debes seleccionar el tipo de dato auxiliar.");
            return;
        }

        if (nombre.isEmpty()) {
            mostrarAlerta("El campo de nombre no puede estar vacío.");
            return;
        }

        boolean ok;
        if (datoExistente == null) {
            // Insertar nuevo
            ok = dao.insertar(tipoSeleccionado, nombre);
        } else {
            // Editar existente
            ok = dao.editar(tipoSeleccionado, datoExistente.getId(), nombre);
        }

        if (ok) {
            if (onGuardarCallback != null) onGuardarCallback.run();
            cerrarVentana();
        } else {
            mostrarAlerta("Error al guardar los datos.");
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
}
