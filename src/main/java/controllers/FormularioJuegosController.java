package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Milanes
 */
public class FormularioJuegosController implements Initializable {

    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_anio;
    @FXML
    private ComboBox<?> combo_consolas;
    @FXML
    private ComboBox<?> combo_estados;
    @FXML
    private TextField txt_descripcion;
    @FXML
    private TextField txt_fecha_inicio;
    @FXML
    private TextField txt_fecha_fin;
    @FXML
    private TextField txt_intentos;
    @FXML
    private TextField txt_creditos;
    @FXML
    private TextField txt_puntuacion;
    @FXML
    private Button btn_guardar;
    @FXML
    private Button btn_cancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void btnActionCancelar(ActionEvent event) {
    }

    
}
