package controllers;

import dao.InicioDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Inicio;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioController implements Initializable {

    @FXML
    private GridPane gridResumen;
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
    private Text tituloResumen;

    @FXML
    private GridPane gridMetasDetalle;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarResumen();
    }

    private void cargarResumen() {
        InicioDAO dao = new InicioDAO();
        Inicio resumen = dao.obtenerResumen();

        // Tarjetas resumen
        txt_juegos_total.setText(String.valueOf(resumen.getTotalJuegos()));
        txt_logros_total.setText(String.valueOf(resumen.getTotalLogros()));
        txt_consolas_total.setText(String.valueOf(resumen.getTotalConsolas()));
        txt_eventos_total.setText(String.valueOf(resumen.getTotalEventos()));
        txt_metas_total.setText(String.valueOf(resumen.getTotalMetas()));
        txt_seguidores_total.setText(String.valueOf(resumen.getTotalSeguidores()));

        // Detalles dinámicos
        lblSeguidorestotales.setText(resumen.getMetaSeguidoresProgreso() != null ? resumen.getMetaSeguidoresProgreso() : "No disponible");
        lblJuegosCompletados.setText(resumen.getMetaJuegosCompletadosDescripcion() != null ? resumen.getMetaJuegosCompletadosDescripcion() : "No disponible");
        lblResultadoMejoraDelCanal.setText(resumen.getMejorasDelCanal() != null ? resumen.getMejorasDelCanal() : "Sin mejoras registradas");
        lblFaltanXDias.setText(resumen.getDiasParaExtensible() != null ? resumen.getDiasParaExtensible() : "No disponible");
        lblFechaExtensible.setText(resumen.getFechaExtensible() != null ? resumen.getFechaExtensible() : "No registrada");

        // Nueva meta específica
        lblMetaEspecifica.setText(resumen.getMetaEspecifica() != null ? resumen.getMetaEspecifica() : "No hay metas específicas registradas");
    }
}
