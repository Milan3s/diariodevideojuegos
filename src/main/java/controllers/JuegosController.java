package controllers;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        listaJuegos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Juego item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    setDisable(true);
                    setMouseTransparent(true);
                } else {
                    setText(item.getNombre());
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
        String recomendado = juego.isEsRecomendado() ? "Sí" : "No";
        lblRecomendado.setText(recomendado);

        // Mostrar estado y consola
        if (juego.getEstado() != null) {
            lblEstado.setText(juego.getEstado().getNombre());  // Mostrar el nombre del estado
        } else {
            lblEstado.setText("No disponible");
        }

        if (juego.getConsola() != null) {
            lblConsola.setText(juego.getConsola().getNombre()); // Mostrar el nombre de la consola
        } else {
            lblConsola.setText("No disponible");
        }

        // Mostrar imagen o texto "No hay imagen"
        String rutaImagen = Paths.get(IMAGENES_PATH, juego.getImagen()).toString();
        File imgFile = new File(rutaImagen);

        if (imgFile.exists()) {
            imgDetalle.setImage(new Image("file:" + rutaImagen)); // Cargar imagen
            lblNoImagen.setVisible(false); // Ocultar el texto "No hay imagen" si la imagen existe
        } else {
            imgDetalle.setImage(null); // No mostrar imagen
            lblNoImagen.setVisible(true); // Mostrar texto "No hay imagen"
        }
    }

    @FXML
    private void abrirModalAgregarJuego() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml"));
            Parent root = loader.load();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Añadir Juego");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarJuegos();  // Recargar juegos después de agregar uno

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
