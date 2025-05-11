package controllers;

import config.AppLogger;
import dao.InicioDAO;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Inicio;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Label lblFaltanXDias;
    @FXML
    private Label lblFechaExtensible;
    @FXML
    private Label lblMetaEspecifica;

    @FXML
    private Text tituloResumen;
    @FXML
    private GridPane gridResumen;
    @FXML
    private GridPane gridMetasDetalle;
    @FXML
    private Button btnAsignarMetaSeguidores;
    @FXML
    private Button btnMetaJuegos;
    @FXML
    private Button btnMetaEspecifica;
    @FXML
    private Button btnAsignarMejoraDelCanal;
    @FXML
    private Button btnProximoExtensible;

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
        lblFaltanXDias.setText(defaultText(resumen.getDiasParaExtensible(), "No disponible"));
        lblFechaExtensible.setText(formatearFecha(resumen.getFechaExtensible()));
        lblMetaEspecifica.setText(defaultText(resumen.getMetaEspecifica(), "No hay metas específicas registradas"));
    }

    private String defaultText(String value, String fallback) {
        return (value != null && !value.isBlank()) ? value : fallback;
    }

    private String formatearFecha(String fechaOriginal) {
        if (fechaOriginal != null && fechaOriginal.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                LocalDate fecha = LocalDate.parse(fechaOriginal);
                return fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                return "Formato inválido";
            }
        }
        return (fechaOriginal != null) ? fechaOriginal : "No registrada";
    }

    // ---------------------------
    // Métodos para abrir formularios con callback
    // ---------------------------
    private void abrirFormularioMeta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();

            FormMetasController controller = loader.getController();
            controller.setOnGuardarCallback(this::cargarResumen);  // Refrescar al guardar

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Asignar Meta");

            modal.setResizable(true); // ✅ Permitir maximizar
            modal.showAndWait();

        } catch (IOException e) {
            AppLogger.severe("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleAsignarMetaSeguidores(ActionEvent event) {
        abrirFormularioMeta(event);
    }

    @FXML
    private void handleMetaJuegos(ActionEvent event) {
        abrirFormularioMeta(event);
    }

    @FXML
    private void handleMetaEspecifica(ActionEvent event) {
        abrirFormularioMeta(event);
    }

    @FXML
    private void handleAsignarMejoraDelCanal(ActionEvent event) {
        abrirFormularioMeta(event);
    }

    @FXML
    private void handleProximoExtensible(ActionEvent event) {
        abrirFormularioMeta(event);
    }
}
