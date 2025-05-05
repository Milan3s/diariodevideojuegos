package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Milanes
 */
public class InicioController implements Initializable {

    @FXML
    private GridPane gridResumen;
    @FXML
    private Text txt_juegos_total;
    @FXML
    private Text txt_logros_total;
    @FXML
    private Text txt_consolas_total;
    @FXML
    private Text txt_eventos_total;
    @FXML
    private Text tituloResumen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
