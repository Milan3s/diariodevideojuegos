package controllers;

import dao.MetasDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import models.Metas;

import java.net.URL;
import java.util.ResourceBundle;

public class FormMetasController implements Initializable {

    @FXML private Text tituloFormulario;

    @FXML private HBox boxMeta;
    @FXML private HBox boxJuegos;
    @FXML private HBox boxFabricante;
    @FXML private HBox boxConsola;
    @FXML private HBox boxFechas;
    @FXML private HBox boxExtensible;

    @FXML private TextField txtDescripcionMeta;
    @FXML private TextField txtValorMeta;
    @FXML private TextField txtValorActual;
    @FXML private TextField txtFabricanteMeta;
    @FXML private TextField txtJuegosObjetivoMeta;
    @FXML private TextField txtJuegosCompletadosMeta;
    @FXML private ComboBox<?> comboConsolaMeta;
    @FXML private TextField txtMotivoMeta;
    @FXML private DatePicker dpInicioMeta;
    @FXML private DatePicker dpFinMeta;
    @FXML private DatePicker dpFechaEventoMeta;

    @FXML private Button btnGuardarMeta;
    @FXML private Button btnCancelarMeta;

    @FXML private AnchorPane formularioMetas;

    private String tipoMeta;
    private final MetasDAO metasDAO = new MetasDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ocultarTodosLosCampos();
        configurarBotones();
    }

    private void configurarBotones() {
        btnGuardarMeta.setOnAction(event -> guardarMeta());

        btnCancelarMeta.setOnAction(event -> {
            if (btnCancelarMeta.getScene() != null && btnCancelarMeta.getScene().getWindow() != null) {
                btnCancelarMeta.getScene().getWindow().hide();
            }
        });
    }

    private void ocultarTodosLosCampos() {
        if (boxMeta != null) boxMeta.setVisible(false);
        if (boxJuegos != null) boxJuegos.setVisible(false);
        if (boxFabricante != null) boxFabricante.setVisible(false);
        if (boxConsola != null) boxConsola.setVisible(false);
        if (boxFechas != null) boxFechas.setVisible(false);
        if (boxExtensible != null) boxExtensible.setVisible(false);
    }

    public void prepararFormularioSeguidores() {
        tipoMeta = "seguidores";
        ocultarTodosLosCampos();
        if (tituloFormulario != null) tituloFormulario.setText("Formulario de Meta: SEGUIDORES");
        if (boxMeta != null) boxMeta.setVisible(true);
        if (boxFechas != null) boxFechas.setVisible(true);
    }

    public void prepararFormularioJuegos() {
        tipoMeta = "twitch";
        ocultarTodosLosCampos();
        if (tituloFormulario != null) tituloFormulario.setText("Formulario de Meta: JUEGOS");
        if (boxMeta != null) boxMeta.setVisible(true);
        if (boxFechas != null) boxFechas.setVisible(true);
    }

    public void prepararFormularioEspecifica() {
        tipoMeta = "especifica";
        ocultarTodosLosCampos();
        if (tituloFormulario != null) tituloFormulario.setText("Formulario de Meta: ESPECÍFICA");
        if (boxJuegos != null) boxJuegos.setVisible(true);
        if (boxFabricante != null) boxFabricante.setVisible(true);
        if (boxConsola != null) boxConsola.setVisible(true);
        if (boxFechas != null) boxFechas.setVisible(true);
    }

    public void prepararFormularioMejora() {
        tipoMeta = "mejora";
        ocultarTodosLosCampos();
        if (tituloFormulario != null) tituloFormulario.setText("Formulario de Meta: MEJORA");
        if (boxFechas != null) boxFechas.setVisible(true);
    }

    public void prepararFormularioExtensible() {
        tipoMeta = "extensible";
        ocultarTodosLosCampos();
        if (tituloFormulario != null) tituloFormulario.setText("Formulario de Meta: EXTENSIBLE");
        if (boxExtensible != null) boxExtensible.setVisible(true);
    }

    private void guardarMeta() {
        Metas meta = new Metas();
        meta.setTipo(tipoMeta);
        meta.setDescripcion(txtDescripcionMeta != null ? txtDescripcionMeta.getText() : "");

        if ("twitch".equals(tipoMeta)) {
            meta.setMeta(parseInt(txtValorMeta.getText()));
            meta.setActual(parseInt(txtValorActual.getText()));
            meta.setFechaInicio(dpInicioMeta.getValue());
            meta.setFechaFin(dpFinMeta.getValue());

        } else if ("especifica".equals(tipoMeta)) {
            meta.setJuegosObjetivo(parseInt(txtJuegosObjetivoMeta.getText()));
            meta.setJuegosCompletados(parseInt(txtJuegosCompletadosMeta.getText()));
            meta.setFabricante(txtFabricanteMeta.getText());
            meta.setIdConsola(comboConsolaMeta != null && !comboConsolaMeta.getSelectionModel().isEmpty()
                    ? comboConsolaMeta.getSelectionModel().getSelectedIndex() + 1
                    : null);
            meta.setFechaInicio(dpInicioMeta.getValue());
            meta.setFechaFin(dpFinMeta.getValue());

        } else if ("mejora".equals(tipoMeta)) {
            meta.setFechaInicio(dpInicioMeta.getValue());
            meta.setFechaFin(dpFinMeta.getValue());

        } else if ("extensible".equals(tipoMeta)) {
            meta.setMotivo(txtMotivoMeta.getText());
            meta.setFechaEvento(dpFechaEventoMeta.getValue());

        } else if ("seguidores".equals(tipoMeta)) {
            meta.setActual(parseInt(txtValorActual.getText()));
            meta.setFechaInicio(dpInicioMeta.getValue());
            meta.setFechaFin(dpFinMeta.getValue());
        }

        boolean exito = metasDAO.insertarMeta(meta);
        if (exito) {
            mostrarAlerta("Meta guardada correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al guardar la meta.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormulario() {
        if (txtDescripcionMeta != null) txtDescripcionMeta.clear();
        if (txtValorMeta != null) txtValorMeta.clear();
        if (txtValorActual != null) txtValorActual.clear();
        if (txtJuegosObjetivoMeta != null) txtJuegosObjetivoMeta.clear();
        if (txtJuegosCompletadosMeta != null) txtJuegosCompletadosMeta.clear();
        if (txtFabricanteMeta != null) txtFabricanteMeta.clear();
        if (comboConsolaMeta != null) comboConsolaMeta.getSelectionModel().clearSelection();
        if (dpInicioMeta != null) dpInicioMeta.setValue(null);
        if (dpFinMeta != null) dpFinMeta.setValue(null);
        if (txtMotivoMeta != null) txtMotivoMeta.clear();
        if (dpFechaEventoMeta != null) dpFechaEventoMeta.setValue(null);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
