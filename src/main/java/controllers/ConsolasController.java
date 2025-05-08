package controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Milanes
 */
public class ConsolasController implements Initializable {

    @FXML
    private Text tituloConsolas;
    @FXML
    private ComboBox<?> comboEstado;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ListView<?> listaConsolas;
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
    private ImageView imgDetalle;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblAbreviatura;
    @FXML
    private Label lblAnio;
    @FXML
    private Label lblFabricante;
    @FXML
    private Label lblGeneracion;
    @FXML
    private Label lblRegion;
    @FXML
    private Label lblTipo;
    @FXML
    private Label lblEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void filtrarConsolas(ActionEvent event) {
    }

    @FXML
    private void filtrarConsolas(KeyEvent event) {
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
    }

    @FXML
    private void abrirModalAgregarConsola(ActionEvent event) {
    }

    @FXML
    private void editarConsola(ActionEvent event) {
    }

    @FXML
    private void eliminarConsola(ActionEvent event) {
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
