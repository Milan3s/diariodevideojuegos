package controllers;

import config.AppLogger;
import config.Conexion;
import dao.JuegoDAO;
import models.Juego;
import models.Estado;
import models.Consola;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.media.MediaView;

public class JuegosController implements Initializable {

    @FXML
    private Text tituloJuegos;

    @FXML
    private ListView<Juego> listaJuegos;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblEditor;
    @FXML
    private Label lblDesarrollador;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblModo;
    @FXML
    private Label lblRecomendado;
    @FXML
    private ImageView imgDetalle;
    @FXML
    private Label lblNoImagen;
    @FXML
    private Label lblEstado;  // Agregado para mostrar el estado
    @FXML
    private Label lblConsola; // Agregado para mostrar la consola

    private static final String IMAGENES_PATH = Paths.get(System.getProperty("user.home"), "Documents", "diariodevideojuegos", "imagenes", "juegos").toString();
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ComboBox<?> comboEstado;
    @FXML
    private ComboBox<?> comboConsola;
    @FXML
    private MediaView videoDetalle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarJuegos();
        configurarListView();
    }

    private void cargarJuegos() {
        JuegoDAO dao = new JuegoDAO();
        ObservableList<Juego> juegos = dao.obtenerTodos();
        listaJuegos.setItems(juegos);
    }

    private void configurarListView() {
        listaJuegos.setCellFactory(lv -> new ListCell<Juego>() {
            @Override
            protected void updateItem(Juego item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    setDisable(true);
                    setMouseTransparent(true);
                } else {
                    // Mostrar el nombre del juego con la consola entre paréntesis
                    setText(item.getNombreConsola());  // Usamos el campo juego_consola que contiene "Super Mario (SNES)"
                    setDisable(false);
                    setMouseTransparent(false);
                }
            }
        });

        listaJuegos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> {
            if (nuevo != null) {
                mostrarDetalle(nuevo);
            }
        });
    }

    private void mostrarDetalle(Juego juego) {
        // Mostrar detalles del juego
        lblNombre.setText(juego.getNombre());
        lblGenero.setText(juego.getGenero());
        lblEditor.setText(juego.getEditor());
        lblDesarrollador.setText(juego.getDesarrollador());
        lblFecha.setText(juego.getFechaLanzamiento());
        lblModo.setText(juego.getModoJuego());

        // Determinar si el juego es recomendado
        lblRecomendado.setText(juego.isEsRecomendado() ? "Sí" : "No");

        // Mostrar estado y consola
        lblEstado.setText(juego.getEstado() != null ? juego.getEstado().getNombre() : "No disponible");
        lblConsola.setText(juego.getConsola() != null ? juego.getConsola().getNombre() : "No disponible");

        // Verificar si el juego tiene una imagen asignada
        if (juego.getImagen() != null && !juego.getImagen().isEmpty()) {
            // Si tiene imagen, intenta cargarla desde la ruta
            File imageFile = new File(Conexion.imagenesPath, juego.getImagen());  // No necesitas un nuevo método
            if (imageFile.exists()) {
                imgDetalle.setImage(new Image(imageFile.toURI().toString()));  // Cargar la imagen
                lblNoImagen.setVisible(false);  // Ocultar el mensaje "No hay imagen"
            } else {
                imgDetalle.setImage(null);  // Si la imagen no existe, asegurarse de que no se muestra nada
                lblNoImagen.setVisible(true);  // Mostrar mensaje "No hay imagen"
            }
        } else {
            // Si no tiene imagen, mostrar el mensaje "No hay imagen"
            imgDetalle.setImage(null);  // Asegurarse de que no se muestra ninguna imagen
            lblNoImagen.setVisible(true);  // Mostrar mensaje "No hay imagen"
        }
    }

    @FXML
    private void abrirModalAgregarJuego() {
        // Obtener el juego seleccionado antes de abrir el formulario
        Juego juegoSeleccionado = listaJuegos.getSelectionModel().getSelectedItem();
        int juegoSeleccionadoIndex = listaJuegos.getSelectionModel().getSelectedIndex();  // Guardamos el índice del juego seleccionado

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
            Parent root = loader.load();

            // Enviar el juego seleccionado al formulario para editar
            FormJuegosController formJuegosController = loader.getController();
            if (juegoSeleccionado != null) {
                formJuegosController.cargarJuegoParaEditar(juegoSeleccionado);  // Cargar los detalles del juego en el formulario
            }

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle(juegoSeleccionado == null ? "Añadir Juego" : "Editar Juego");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarJuegos();  // Recargar juegos después de agregar o editar uno

            // Volver a seleccionar el juego previamente seleccionado en la lista
            if (juegoSeleccionadoIndex >= 0) {
                listaJuegos.getSelectionModel().select(juegoSeleccionadoIndex);  // Volver a seleccionar el juego
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarJuego(ActionEvent event) {
        abrirModalAgregarJuego();  // Usar el mismo método para abrir el formulario de edición
    }

    @FXML
    private void eliminarJuego(ActionEvent event) {
        // Obtener el juego seleccionado de la lista
        Juego juegoSeleccionado = listaJuegos.getSelectionModel().getSelectedItem();
        if (juegoSeleccionado != null) {
            confirmarEliminacionJuego(juegoSeleccionado);  // Mostrar confirmación antes de eliminar
        } else {
            mostrarAlerta("No se ha seleccionado ningún juego.");
        }
    }

    // Método para mostrar la alerta de confirmación de eliminación
    private void confirmarEliminacionJuego(Juego juegoSeleccionado) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación de Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar el juego?");
        confirmacion.setContentText("El juego: " + juegoSeleccionado.getNombre());

        // Opciones de respuesta
        confirmacion.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        // Acción a realizar según la respuesta del usuario
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                eliminarJuego(juegoSeleccionado);  // Si confirma, eliminamos el juego
            }
        });
    }

    // Método para eliminar el juego
    private void eliminarJuego(Juego juegoSeleccionado) {
        boolean exito = new JuegoDAO().eliminarJuego(juegoSeleccionado.getId());
        if (exito) {
            mostrarAlerta("Juego eliminado correctamente.");
            AppLogger.info("Juego eliminado correctamente: " + juegoSeleccionado.getNombre());
            actualizarListaJuegos();  // Actualizar la lista de juegos
        } else {
            mostrarAlerta("Error al eliminar el juego.");
            AppLogger.severe("Error al eliminar el juego: " + juegoSeleccionado.getNombre());
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para actualizar la lista de juegos después de eliminar uno
    private void actualizarListaJuegos() {
        cargarJuegos();
    }
}
