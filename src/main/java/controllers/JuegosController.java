package controllers;

import dao.JuegoDAO;
import models.Juego;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarJuegos();
    }

    private void cargarJuegos() {
        JuegoDAO dao = new JuegoDAO();
        ObservableList<Juego> juegos = dao.obtenerTodos();
        listaJuegos.setItems(juegos);
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

            // Recargar lista tras cerrar el modal
            cargarJuegos();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
