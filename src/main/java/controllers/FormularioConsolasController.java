package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 *
 * @author Milanes
 */
public class FormularioConsolasController {

    @FXML
    private ComboBox<?> combo_consolas;
    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_anio;
    @FXML
    private TextField txt_fabricante;
    @FXML
    private TextField txt_generacion;
    @FXML
    private TextField txt_region;
    @FXML
    private TextField txt_tipo;
    @FXML
    private TextField txt_procesador;
    @FXML
    private TextField txt_memoria;
    @FXML
    private TextField txt_almacenamiento;
    @FXML
    private TextField txt_fecha_lanzamiento;
    @FXML
    private Button btn_guardar;
    @FXML
    private Button btn_editar;
    @FXML
    private Button btn_eliminar;
    @FXML
    private Button btn_cambiar_imagen;
    @FXML
    private ImageView imagen_consola_form;
    
}
