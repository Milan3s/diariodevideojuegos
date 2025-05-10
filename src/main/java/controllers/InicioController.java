package controllers;

import dao.InicioDAO;
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
import javafx.scene.control.Button;

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
    @FXML
    private Button btnAsignarMetaEspecifica;
    @FXML
    private Button btnAsignarExtensible;
    @FXML
    private Button btnAddSeguidores;
    @FXML
    private Button btnAsignarSeguidores;
    @FXML
    private Button btnEditarSeguidores;
    @FXML
    private Button btnEliminarSeguidores;
    @FXML
    private Button btnAddJuegos;
    @FXML
    private Button btnAsignarJuegos;
    @FXML
    private Button btnEditarJuegos;
    @FXML
    private Button btnEliminarJuegos;
    @FXML
    private Button btnAddMetaEspecifica;
    @FXML
    private Button btnEditarMetaEspecifica;
    @FXML
    private Button btnEliminarMetaEspecifica;
    @FXML
    private Button btnAddMejora;
    @FXML
    private Button btnAsignarMejora;
    @FXML
    private Button btnEditarMejora;
    @FXML
    private Button btnEliminarMejora;
    @FXML
    private Button btnAddExtensible;
    @FXML
    private Button btnEditarExtensible;
    @FXML
    private Button btnEliminarExtensible;

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
        lblSeguidorestotales.setText(
                resumen.getMetaSeguidoresProgreso() != null
                        ? resumen.getMetaSeguidoresProgreso()
                        : "No disponible"
        );

        lblJuegosCompletados.setText(
                resumen.getMetaJuegosCompletadosDescripcion() != null
                        ? resumen.getMetaJuegosCompletadosDescripcion()
                        : "No disponible"
        );

        lblResultadoMejoraDelCanal.setText(
                resumen.getMejorasDelCanal() != null
                        ? resumen.getMejorasDelCanal()
                        : "Sin mejoras registradas"
        );

        lblFaltanXDias.setText(
                resumen.getDiasParaExtensible() != null
                        ? resumen.getDiasParaExtensible()
                        : "No disponible"
        );

        // Formatear la fecha si es válida, sino mostrar el texto tal cual
        String fechaOriginal = resumen.getFechaExtensible();
        if (fechaOriginal != null && fechaOriginal.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                LocalDate fecha = LocalDate.parse(fechaOriginal); // yyyy-MM-dd
                String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                lblFechaExtensible.setText(fechaFormateada);
            } catch (DateTimeParseException e) {
                lblFechaExtensible.setText("Formato inválido");
            }
        } else {
            lblFechaExtensible.setText(fechaOriginal != null ? fechaOriginal : "No registrada");
        }

        lblMetaEspecifica.setText(
                resumen.getMetaEspecifica() != null
                        ? resumen.getMetaEspecifica()
                        : "No hay metas específicas registradas"
        );
    }
}
