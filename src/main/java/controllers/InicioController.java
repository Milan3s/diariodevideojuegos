package controllers;

import dao.InicioDAO;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class InicioController implements Initializable {

    @FXML private GridPane gridResumen;
    @FXML private Label txt_juegos_total, txt_logros_total, txt_consolas_total, txt_eventos_total, txt_metas_total, txt_seguidores_total;
    @FXML private Text tituloResumen;

    @FXML private GridPane gridMetasDetalle;
    @FXML private Label lblSeguidorestotales, lblJuegosCompletados, lblResultadoMejoraDelCanal, lblFaltanXDias, lblFechaExtensible, lblMetaEspecifica;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarResumen();
    }

    private void cargarResumen() {
        InicioDAO dao = new InicioDAO();
        Inicio resumen = dao.obtenerResumen();

        if (resumen != null) {
            txt_juegos_total.setText(String.valueOf(resumen.getTotalJuegos()));
            txt_logros_total.setText(String.valueOf(resumen.getTotalLogros()));
            txt_consolas_total.setText(String.valueOf(resumen.getTotalConsolas()));
            txt_eventos_total.setText(String.valueOf(resumen.getTotalEventos()));
            txt_metas_total.setText(String.valueOf(resumen.getTotalMetas()));
            txt_seguidores_total.setText(String.valueOf(resumen.getTotalSeguidores()));

            lblSeguidorestotales.setText(resumen.getMetaSeguidoresProgreso() != null ? resumen.getMetaSeguidoresProgreso() : "No disponible");
            lblJuegosCompletados.setText(resumen.getMetaJuegosCompletadosDescripcion() != null ? resumen.getMetaJuegosCompletadosDescripcion() : "No disponible");
            lblResultadoMejoraDelCanal.setText(resumen.getMejorasDelCanal() != null ? resumen.getMejorasDelCanal() : "Sin mejoras registradas");
            lblFaltanXDias.setText(resumen.getDiasParaExtensible() != null ? resumen.getDiasParaExtensible() : "No disponible");

            String fechaOriginal = resumen.getFechaExtensible();
            if (fechaOriginal != null && fechaOriginal.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    LocalDate fecha = LocalDate.parse(fechaOriginal);
                    String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    lblFechaExtensible.setText(fechaFormateada);
                } catch (DateTimeParseException e) {
                    lblFechaExtensible.setText("Formato inválido");
                }
            } else {
                lblFechaExtensible.setText(fechaOriginal != null ? fechaOriginal : "No registrada");
            }

            lblMetaEspecifica.setText(resumen.getMetaEspecifica() != null ? resumen.getMetaEspecifica() : "No hay metas específicas registradas");
        }
    }

    private void abrirFormularioMetasBase(String tituloVentana, Runnable preparacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();

            FormMetasController controller = loader.getController();
            preparacion.run(); // ejecuta método específico

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle(tituloVentana);
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

        } catch (IOException e) {
            System.err.println("Error al abrir el formulario de metas: " + tituloVentana);
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormSeguidores(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();
            FormMetasController controller = loader.getController();
            controller.prepararFormularioSeguidores();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Meta - Seguidores");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormJuegos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();
            FormMetasController controller = loader.getController();
            controller.prepararFormularioJuegos();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Meta - Juegos (Twitch)");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormEspecifica(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();
            FormMetasController controller = loader.getController();
            controller.prepararFormularioEspecifica();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Meta - Específica");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormMejora(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();
            FormMetasController controller = loader.getController();
            controller.prepararFormularioMejora();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Meta - Mejora");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormExtensible(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetas.fxml"));
            Parent root = loader.load();
            FormMetasController controller = loader.getController();
            controller.prepararFormularioExtensible();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Meta - Extensible");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
