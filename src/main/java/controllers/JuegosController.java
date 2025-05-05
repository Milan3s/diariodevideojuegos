package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Milanes
 */
public class JuegosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void abrirModalAgregarJuego() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormJuegos.fxml")); // o un formulario diferente si ya existe
            Parent root = loader.load();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Añadir Juego");
            modal.setScene(new Scene(root));
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
