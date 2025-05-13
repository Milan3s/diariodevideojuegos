package controllers;

import config.AppLogger;
import dao.DatosAuxiliaresDAO;
import dao.InicioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Inicio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioController implements Initializable {

    @FXML
    private Label txt_juegos_total;
    @FXML
    private Label txt_logros_total;
    @FXML
    private Label txt_consolas_total;
    @FXML
    private Label txt_eventos_total;
    @FXML
    private Label txt_metas_total;
    @FXML
    private Label txt_seguidores_total;

    @FXML
    private Label lblSeguidorestotales;
    @FXML
    private Label lblJuegosCompletados;
    @FXML
    private Label lblResultadoMejoraDelCanal;
    @FXML
    private Label lblFechaExtensible;
    @FXML
    private Label lblMetaEspecifica;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblDias;

    @FXML
    private Text tituloResumen;
    @FXML
    private GridPane gridResumen;
    @FXML
    private GridPane gridMetasDetalle;

    private final DatosAuxiliaresDAO auxDAO = new DatosAuxiliaresDAO();
    @FXML
    private Button btnAsignarMetas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarResumen();
    }

    private void cargarResumen() {
        InicioDAO dao = new InicioDAO();
        Inicio resumen = dao.obtenerResumen();

        if (resumen == null) {
            return;
        }

        txt_juegos_total.setText(String.valueOf(resumen.getTotalJuegos()));
        txt_logros_total.setText(String.valueOf(resumen.getTotalLogros()));
        txt_consolas_total.setText(String.valueOf(resumen.getTotalConsolas()));
        txt_eventos_total.setText(String.valueOf(resumen.getTotalEventos()));
        txt_metas_total.setText(String.valueOf(resumen.getTotalMetas()));
        txt_seguidores_total.setText(String.valueOf(resumen.getTotalSeguidores()));

        lblSeguidorestotales.setText(defaultText(resumen.getMetaSeguidoresProgreso(), "No disponible"));
        lblJuegosCompletados.setText(defaultText(resumen.getMetaJuegosCompletadosDescripcion(), "No disponible"));
        lblResultadoMejoraDelCanal.setText(defaultText(resumen.getMejorasDelCanal(), "Sin mejoras registradas"));
        lblDias.setText(defaultText(resumen.getDiasParaExtensible(), "No disponible"));
        lblFechaExtensible.setText(defaultText(resumen.getFechaExtensible(), "No registrada"));
        lblMetaEspecifica.setText(defaultText(resumen.getMetaEspecifica(), "No hay metas específicas registradas"));
        lblNombre.setText(defaultText(resumen.getNombreExtensible(), "No registrado"));
    }

    private String defaultText(String value, String fallback) {
        return (value != null && !value.isBlank()) ? value : fallback;
    }

    private void abrirFormularioMeta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();

            FormMetasController controller = loader.getController();
            controller.setOnGuardarCallback(this::cargarResumen);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Asignar Meta");
            modal.setResizable(true);
            modal.showAndWait();

        } catch (IOException e) {
            AppLogger.severe("Error al abrir el formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAsignarMetas(ActionEvent event) {
        abrirFormularioMeta(event);
    }
}
