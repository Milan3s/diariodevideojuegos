package controllers;

import dao.JuegoDAO;
import models.Juego;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FormJuegosController implements Initializable {

    @FXML
    private TextField txtNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No se requiere lógica al iniciar, por ahora.
    }

    @FXML
    private void guardarJuego(ActionEvent event) {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            return;
        }

        Juego juego = new Juego(nombre);
        JuegoDAO dao = new JuegoDAO();

        boolean exito = dao.insertarJuego(juego);

        if (exito) {
            mostrarAlerta("Juego guardado correctamente.");
            txtNombre.clear();
        } else {
            mostrarAlerta("Error al guardar el juego.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
