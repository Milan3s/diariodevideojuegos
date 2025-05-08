package controllers;

import config.Conexion;
import dao.ComboDAO;
import dao.ConsolaDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Consola;
import models.Estado;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FormConsolasController implements Initializable {

    @FXML
    private TextField txtNombre, txtAbreviatura, txtAnio, txtFabricante, txtGeneracion,
            txtRegion, txtTipo, txtProcesador, txtMemoria, txtAlmacenamiento;
    @FXML
    private DatePicker dateFechaLanzamiento;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private ImageView imgPreview;

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
        dateFechaLanzamiento.setValue(null);
        comboEstado.getSelectionModel().clearSelection();
        imgPreview.setImage(null);
        imagenSeleccionada = null;
    }

    public void cargarConsolaParaEditar(Consola consola) {
        consolaActual = consola;
        txtNombre.setText(consola.getNombre());
        txtAbreviatura.setText(consola.getAbreviatura());
        txtAnio.setText(String.valueOf(consola.getAnio()));
        txtFabricante.setText(consola.getFabricante());
        txtGeneracion.setText(consola.getGeneracion());
        txtRegion.setText(consola.getRegion());
        txtTipo.setText(consola.getTipo());
        txtProcesador.setText(consola.getProcesador());
        txtMemoria.setText(consola.getMemoria());
        txtAlmacenamiento.setText(consola.getAlmacenamiento());
        dateFechaLanzamiento.setValue(java.time.LocalDate.parse(consola.getFechaLanzamiento()));
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
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            imagenSeleccionada = file;
            imgPreview.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void guardarConsola(ActionEvent event) {
        String nombre = txtNombre.getText();
        String abreviatura = txtAbreviatura.getText();
        int anio = Integer.parseInt(txtAnio.getText());
        String fabricante = txtFabricante.getText();
        String generacion = txtGeneracion.getText();
        String region = txtRegion.getText();
        String tipo = txtTipo.getText();
        String procesador = txtProcesador.getText();
        String memoria = txtMemoria.getText();
        String almacenamiento = txtAlmacenamiento.getText();
        String fechaLanzamiento = dateFechaLanzamiento.getValue().format(DateTimeFormatter.ISO_DATE);
        Estado estado = comboEstado.getValue();

        String imagenNombre = consolaActual != null ? consolaActual.getImagen() : null;
        if (imagenSeleccionada != null) {
            imagenNombre = Conexion.guardarImagen(imagenSeleccionada);
        }

        Consola nueva = new Consola(
                consolaActual != null ? consolaActual.getId() : 0,
                nombre, abreviatura, anio, fabricante, generacion, region,
                tipo, procesador, memoria, almacenamiento, fechaLanzamiento, imagenNombre, estado
        );

        boolean exito = consolaActual == null ? dao.insertar(nueva) : dao.actualizar(nueva);
        if (exito) {
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo guardar la consola.");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
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
