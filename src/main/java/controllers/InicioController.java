package controllers;

import dao.InicioDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Inicio;

import java.net.URL;
import java.util.ResourceBundle;

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
    @FXML
    private Text txt_metas_total;
    @FXML
    private Text txt_seguidores_total;
    @FXML
    private GridPane gridMetasDetalle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarResumen();
    }

    private void cargarResumen() {
        InicioDAO dao = new InicioDAO();
        Inicio resumen = dao.obtenerResumen();

        txt_juegos_total.setText(String.valueOf(resumen.getTotalJuegos()));
        txt_logros_total.setText(String.valueOf(resumen.getTotalLogros()));
        txt_consolas_total.setText(String.valueOf(resumen.getTotalConsolas()));
        txt_eventos_total.setText(String.valueOf(resumen.getTotalEventos()));
        txt_metas_total.setText(String.valueOf(resumen.getTotalMetas()));
        txt_seguidores_total.setText(String.valueOf(resumen.getTotalSeguidores()));
    }
}
