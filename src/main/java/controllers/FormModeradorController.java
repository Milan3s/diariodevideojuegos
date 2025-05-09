package controllers;

import dao.ComboDAO;
import dao.ModeradorDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Estado;
import models.Moderador;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class FormModeradorController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Moderador moderadorEdicion;
    private Moderador moderadorGuardado;
    private Runnable onGuardarCallback; // NUEVO

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML
    private AnchorPane formularioModerador;
    @FXML
    private GridPane gridFormulario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Estado> estados = ComboDAO.cargarEstadosModeradores();
        comboEstado.setItems(estados);
    }

    public void cargarModeradorParaEditar(Moderador m) {
        this.moderadorEdicion = m;
        txtNombre.setText(m.getNombre());
        txtEmail.setText(m.getEmail());
        if (m.getEstado() != null) {
            comboEstado.getSelectionModel().select(m.getEstado());
        }
    }

    public Moderador getModeradorGuardado() {
        return moderadorGuardado;
    }

    public void setOnGuardarCallback(Runnable callback) { // NUEVO
        this.onGuardarCallback = callback;
    }

    @FXML
    private void guardarModerador(javafx.event.ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        Estado estado = comboEstado.getSelectionModel().getSelectedItem();

        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            return;
        }

        ModeradorDAO dao = new ModeradorDAO();
        boolean exito = false;

        if (moderadorEdicion == null) {
            Moderador nuevo = new Moderador();
            nuevo.setNombre(nombre);
            nuevo.setEmail(email);
            nuevo.setFechaAlta(LocalDate.now().format(formatter));
            nuevo.setEstado(estado);
            int idGenerado = dao.insertarModeradorYDevolverId(nuevo);
            if (idGenerado > 0) {
                nuevo.setId(idGenerado);
                this.moderadorGuardado = nuevo;
                exito = true;
            }

        } else {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmación de actualización");
            alerta.setHeaderText("¿Desea sobrescribir los datos del moderador?");
            alerta.setContentText("Esta acción actualizará la información existente.");

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
                return;
            }

            moderadorEdicion.setNombre(nombre);
            moderadorEdicion.setEmail(email);
            moderadorEdicion.setEstado(estado);
            exito = dao.actualizarModerador(moderadorEdicion);
            if (exito) {
                this.moderadorGuardado = moderadorEdicion;
            }
        }

        if (exito) {
            if (onGuardarCallback != null) {
                onGuardarCallback.run();
            }
            cerrarVentana();
        } else {
            mostrarAlerta("Error al guardar el moderador.");
        }
    }

    @FXML
    private void cancelar(javafx.event.ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
