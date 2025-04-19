package controllers;

import dao.JuegoDAO;
import models.Juego;
import config.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ContenidoJuegosController implements Initializable {

    @FXML private AnchorPane root_contenido_juegos;
    @FXML private TextField campo_busqueda_juegos;
    @FXML private ListView<Juego> list_view_juegos;
    @FXML private Label lbl_paginado_juegos;
    @FXML private Button btn_agregar_juego;
    @FXML private Button btn_editar_juego;
    @FXML private Button btn_eliminar_juego;
    @FXML private AnchorPane panel_informacion_juego;
    @FXML private Text lbl_total_juegos;
    @FXML private Label lbl_nombre_juego;
    @FXML private Label lbl_anio_juego;
    @FXML private Label lbl_estado_juego;
    @FXML private Label lbl_consola_juego;
    @FXML private ComboBox<String> combo_consolas;
    @FXML private ComboBox<String> combo_estados;
    @FXML private Label lbl_descripcion_juego;
    @FXML private Label lbl_fecha_inicio_juego;
    @FXML private Label lbl_fecha_fin_juego;
    @FXML private Label lbl_intentos_juego;
    @FXML private Label lbl_creditos_juego;
    @FXML private Label lbl_puntuacion_juego;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarJuegos();

        list_view_juegos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                mostrarDetallesJuego(selected);
                Logger.info("Juego seleccionado: " + selected.getNombre());
            }
        });
    }

    private void cargarJuegos() {
        ObservableList<Juego> lista = FXCollections.observableArrayList(JuegoDAO.obtenerTodos());
        list_view_juegos.setItems(lista);
        lbl_total_juegos.setText("Total: " + lista.size());
        Logger.info("Se han cargado " + lista.size() + " juegos.");
    }

    private void mostrarDetallesJuego(Juego juego) {
        lbl_nombre_juego.setText(juego.getNombre());
        lbl_anio_juego.setText(String.valueOf(juego.getAnio()));
        lbl_estado_juego.setText(juego.getEstado());
        lbl_consola_juego.setText(juego.getConsola());

        lbl_descripcion_juego.setText(juego.getDescripcion() != null ? juego.getDescripcion() : "-");
        lbl_fecha_inicio_juego.setText(juego.getFechaInicio() != null ? juego.getFechaInicio() : "-");
        lbl_fecha_fin_juego.setText(juego.getFechaFin() != null ? juego.getFechaFin() : "-");

        lbl_intentos_juego.setText(juego.getIntentos() > 0 ? String.valueOf(juego.getIntentos()) : "-");
        lbl_creditos_juego.setText(juego.getCreditos() > 0 ? String.valueOf(juego.getCreditos()) : "-");
        lbl_puntuacion_juego.setText(juego.getPuntuacion() > 0 ? String.valueOf(juego.getPuntuacion()) : "-");
    }

    @FXML private void accion_buscar_juegos() {}
    @FXML private void accion_primeraPagina() {}
    @FXML private void accion_paginaAnterior() {}
    @FXML private void accion_paginaSiguiente() {}
    @FXML private void accion_ultimaPagina() {}
    @FXML private void accion_agregar() {}
    @FXML private void accion_editar() {}
    @FXML private void accion_eliminar() {}
}
