package controllers;

import dao.ComboDAO;
import dao.JuegoDAO;
import dao.LogrosDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class FormLogrosController implements Initializable {

    @FXML
    private GridPane gridFormulario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtHoras;
    @FXML
    private TextField txtAnio;
    @FXML
    private DatePicker dateInicio;
    @FXML
    private DatePicker dateFin;
    @FXML
    private TextField txtIntentos;
    @FXML
    private TextField txtCreditos;
    @FXML
    private TextField txtPuntuacion;
    @FXML
    private ComboBox<Juego> comboJuego;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private ComboBox<Dificultad> comboDificultad;
    @FXML
    private ComboBox<Consola> comboConsola;
    @FXML
    private Button btnGuardar;

    private Logros logroActual = null;
    @FXML
    private AnchorPane formularioLogros;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
        configurarDatePickers();
    }

    private void configurarDatePickers() {
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        };

        dateInicio.setConverter(converter);
        dateFin.setConverter(converter);
    }

    private void cargarCombos() {
        comboJuego.setItems(FXCollections.observableArrayList(new JuegoDAO().obtenerTodos()));
        comboEstado.setItems(FXCollections.observableArrayList(ComboDAO.cargarEstadosPorTipo("logro")));
        comboDificultad.setItems(FXCollections.observableArrayList(ComboDAO.cargarDificultades()));
        comboConsola.setItems(FXCollections.observableArrayList(ComboDAO.cargarConsolas()));
    }

    public void cargarLogroParaEditar(Logros logro) {
        this.logroActual = logro;

        txtNombre.setText(logro.getNombre());
        txtDescripcion.setText(logro.getDescripcion());
        txtHoras.setText(String.valueOf(logro.getHorasEstimadas()));
        txtIntentos.setText(String.valueOf(logro.getIntentos()));
        txtCreditos.setText(String.valueOf(logro.getCreditos()));
        txtPuntuacion.setText(String.valueOf(logro.getPuntuacion()));
        txtAnio.setText(String.valueOf(logro.getAnio()));

        if (logro.getFechaInicio() != null) {
            dateInicio.setValue(LocalDate.parse(logro.getFechaInicio(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (logro.getFechaFin() != null) {
            dateFin.setValue(LocalDate.parse(logro.getFechaFin(), DateTimeFormatter.ISO_LOCAL_DATE));
        }

        comboJuego.getSelectionModel().select(logro.getJuego());
        comboEstado.getSelectionModel().select(logro.getEstado());
        comboConsola.getSelectionModel().select(logro.getConsola());
        comboDificultad.getSelectionModel().select(logro.getDificultad());
    }

    public void limpiarFormulario() {
        logroActual = null;
        txtNombre.clear();
        txtDescripcion.clear();
        txtHoras.clear();
        txtAnio.clear();
        txtIntentos.clear();
        txtCreditos.clear();
        txtPuntuacion.clear();
        dateInicio.setValue(null);
        dateFin.setValue(null);
        comboJuego.getSelectionModel().clearSelection();
        comboEstado.getSelectionModel().clearSelection();
        comboDificultad.getSelectionModel().clearSelection();
        comboConsola.getSelectionModel().clearSelection();
    }

    @FXML
    private void guardarLogro(javafx.event.ActionEvent event) {
        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            return;
        }

        Logros logro = (logroActual != null) ? logroActual : new Logros();

        try {
            logro.setNombre(txtNombre.getText().trim());
            logro.setDescripcion(txtDescripcion.getText().trim());
            logro.setHorasEstimadas(parseEntero(txtHoras.getText()));
            logro.setIntentos(parseEntero(txtIntentos.getText()));
            logro.setCreditos(parseEntero(txtCreditos.getText()));
            logro.setPuntuacion(parseEntero(txtPuntuacion.getText()));
            logro.setAnio(parseEntero(txtAnio.getText()));

            if (dateInicio.getValue() != null) {
                logro.setFechaInicio(dateInicio.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            if (dateFin.getValue() != null) {
                logro.setFechaFin(dateFin.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            logro.setJuego(comboJuego.getValue());
            logro.setEstado(comboEstado.getValue());
            logro.setConsola(comboConsola.getValue());
            logro.setDificultad(comboDificultad.getValue());

            if (logroActual != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar Edición");
                alert.setHeaderText("¿Deseas sobrescribir los datos del logro?");
                alert.setContentText("Esta acción reemplazará la información existente.");

                if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                    return;
                }
            }

            LogrosDAO dao = new LogrosDAO();
            boolean exito = (logroActual != null)
                    ? dao.actualizarLogro(logro)
                    : dao.insertarLogro(logro);

            if (exito) {
                cerrarVentana();
            } else {
                mostrarAlerta("No se pudo guardar el logro.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Uno de los campos numéricos contiene texto no válido.");
        }
    }

    private int parseEntero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
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
