package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Milanes
 */
public class ContenidoJuegosController implements Initializable {

    @FXML
    private AnchorPane root_contenido_juegos;
    @FXML
    private TextField campo_busqueda_juegos;
    @FXML
    private ListView<?> list_view_juegos;
    @FXML
    private Label lbl_paginado_juegos;
    @FXML
    private Button btn_agregar_juego;
    @FXML
    private Button btn_editar_juego;
    @FXML
    private Button btn_eliminar_juego;
    @FXML
    private AnchorPane panel_informacion_juego;
    @FXML
    private Text lbl_total_juegos;
    @FXML
    private Label lbl_nombre_juego;
    @FXML
    private Label lbl_anio_juego;
    @FXML
    private Label lbl_estado_juego;
    @FXML
    private Label lbl_consola_juego;
    @FXML
    private ComboBox<?> combo_consolas;
    @FXML
    private ComboBox<?> combo_estados;
    @FXML
    private Label lbl_descripcion_juego;
    @FXML
    private Label lbl_fecha_inicio_juego;
    @FXML
    private Label lbl_fecha_fin_juego;
    @FXML
    private Label lbl_intentos_juego;
    @FXML
    private Label lbl_creditos_juego;
    @FXML
    private Label lbl_puntuacion_juego;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void accion_buscar_juegos(ActionEvent event) {
    }

    @FXML
    private void accion_primeraPagina(ActionEvent event) {
    }

    @FXML
    private void accion_paginaAnterior(ActionEvent event) {
    }

    @FXML
    private void accion_paginaSiguiente(ActionEvent event) {
    }

    @FXML
    private void accion_ultimaPagina(ActionEvent event) {
    }

    @FXML
    private void accion_agregar(ActionEvent event) {
    }

    @FXML
    private void accion_editar(ActionEvent event) {
    }

    @FXML
    private void accion_eliminar(ActionEvent event) {
    }
    
}
