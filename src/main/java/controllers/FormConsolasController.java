package controllers;

import config.Conexion;
import dao.ComboDAO;
import dao.ConsolaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.Consola;
import models.Estado;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FormConsolasController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtAbreviatura;
    @FXML private TextField txtAnio;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtGeneracion;
    @FXML private TextField txtRegion;
    @FXML private TextField txtTipo;
    @FXML private TextField txtProcesador;
    @FXML private TextField txtMemoria;
    @FXML private TextField txtAlmacenamiento;
    @FXML private TextField txtFrecuencia;
    @FXML private TextField txtCaracteristicas;

    @FXML private DatePicker dateFechaLanzamiento;
    @FXML private ComboBox<Estado> comboEstado;
    @FXML private CheckBox chkChip;
    @FXML private ImageView imgPreview;
    @FXML private AnchorPane formularioConsola;
    @FXML private GridPane gridFormulario;
    @FXML private Button btnGuardar;

    private File imagenSeleccionada;
    private Consola consolaActual;
    private final ConsolaDAO dao = new ConsolaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboEstado.setItems(ComboDAO.cargarEstadosPorTipo("consola"));
    }

    public void limpiarFormulario() {
        consolaActual = null;
        txtNombre.clear();
        txtAbreviatura.clear();
        txtAnio.clear();
        txtFabricante.clear();
        txtGeneracion.clear();
        txtRegion.clear();
        txtTipo.clear();
        txtProcesador.clear();
        txtMemoria.clear();
        txtAlmacenamiento.clear();
        txtFrecuencia.clear();
        txtCaracteristicas.clear();
        chkChip.setSelected(false);
        dateFechaLanzamiento.setValue(null);
        comboEstado.getSelectionModel().clearSelection();
        imgPreview.setImage(null);
        imagenSeleccionada = null;
    }

    public void cargarConsolaParaEditar(Consola consola) {
        consolaActual = consola;

        txtNombre.setText(consola.getNombre());
        txtAbreviatura.setText(consola.getAbreviatura());
        txtAnio.setText(consola.getAnio() != null ? consola.getAnio().toString() : "");
        txtFabricante.setText(consola.getFabricante());
        txtGeneracion.setText(consola.getGeneracion());
        txtRegion.setText(consola.getRegion());
        txtTipo.setText(consola.getTipo());
        txtProcesador.setText(consola.getProcesador());
        txtMemoria.setText(consola.getMemoria());
        txtAlmacenamiento.setText(consola.getAlmacenamiento());
        txtFrecuencia.setText(consola.getFrecuenciaMHz() != null ? consola.getFrecuenciaMHz().toString() : "");
        txtCaracteristicas.setText(consola.getCaracteristicas());
        chkChip.setSelected(consola.tieneChip());

        if (consola.getFechaLanzamiento() != null) {
            dateFechaLanzamiento.setValue(java.time.LocalDate.parse(consola.getFechaLanzamiento()));
        }

        comboEstado.getSelectionModel().select(consola.getEstado());

        if (consola.getImagen() != null) {
            File imgFile = new File(Conexion.imagenesPath, consola.getImagen());
            if (imgFile.exists()) {
                imgPreview.setImage(new Image(imgFile.toURI().toString()));
                imagenSeleccionada = imgFile;
            }
        }
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de la Consola");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png")
        );
        File file = fileChooser.showOpenDialog(formularioConsola.getScene().getWindow());
        if (file != null) {
            imagenSeleccionada = file;
            imgPreview.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void guardarConsola(ActionEvent event) {
        try {
            String nombre = txtNombre.getText().trim();
            String abreviatura = txtAbreviatura.getText().trim();
            Integer anio = txtAnio.getText().isEmpty() ? null : Integer.parseInt(txtAnio.getText());
            String fabricante = txtFabricante.getText().trim();
            String generacion = txtGeneracion.getText().trim();
            String region = txtRegion.getText().trim();
            String tipo = txtTipo.getText().trim();
            String procesador = txtProcesador.getText().trim();
            String memoria = txtMemoria.getText().trim();
            String almacenamiento = txtAlmacenamiento.getText().trim();
            Double frecuencia = txtFrecuencia.getText().isEmpty() ? null : Double.parseDouble(txtFrecuencia.getText());
            String caracteristicas = txtCaracteristicas.getText().trim();
            boolean chip = chkChip.isSelected();
            String fechaLanzamiento = dateFechaLanzamiento.getValue() != null
                    ? dateFechaLanzamiento.getValue().format(DateTimeFormatter.ISO_DATE) : null;
            Estado estado = comboEstado.getValue();

            String imagenNombre = (consolaActual != null) ? consolaActual.getImagen() : null;
            if (imagenSeleccionada != null) {
                imagenNombre = Conexion.guardarImagen(imagenSeleccionada);
            }

            Consola nueva = new Consola(
                    consolaActual != null ? consolaActual.getId() : 0,
                    nombre, abreviatura, anio, fabricante, generacion, region,
                    tipo, procesador, memoria, almacenamiento,
                    fechaLanzamiento, imagenNombre, estado,
                    frecuencia, chip, caracteristicas
            );

            boolean exito = consolaActual == null ? dao.insertar(nueva) : dao.actualizar(nueva);
            if (exito) {
                cerrarVentana();
            } else {
                mostrarAlerta("No se pudo guardar la consola.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Año o Frecuencia no válidos. Deben ser números.");
        } catch (Exception e) {
            mostrarAlerta("Error al guardar: " + e.getMessage());
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) formularioConsola.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
