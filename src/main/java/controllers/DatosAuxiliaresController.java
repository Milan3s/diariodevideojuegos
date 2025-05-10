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
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Milanes
 */
public class DatosAuxiliaresController implements Initializable {

    @FXML
    private Text tituloAuxiliares;
    @FXML
    private ComboBox<?> comboTipoDato;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ListView<?> listaAuxiliares;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnPrimero;
    @FXML
    private Button btnAnterior;
    @FXML
    private Label paginaActual;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnUltimo;
    @FXML
    private Label lblDetalle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void limpiarFiltros(ActionEvent event) {
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
    }

    @FXML
    private void editarItem(ActionEvent event) {
    }

    @FXML
    private void eliminarItem(ActionEvent event) {
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
    }
    
}
