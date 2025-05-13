package controllers;

import dao.EventosExtensiblesDAO;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.EventoExtensible;

public class EventosExtensiblesController implements Initializable {

    @FXML
    private Text tituloEventos;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ListView<EventoExtensible> listaEventos;
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
    private Label lblMotivo;
    @FXML
    private Label lblFechaEvento;
    @FXML
    private ComboBox<String> comboFechas;

    private List<EventoExtensible> eventosFiltrados = new ArrayList<>();
    private static final int ELEMENTOS_POR_PAGINA = 10;
    private int pagina = 1;

    private final SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat formatoVisual = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarEventos();
        cargarFechas();
        cargarEventos();
        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> filtrar());
        comboFechas.setOnAction(e -> filtrar());
    }

    private void configurarEventos() {
        listaEventos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> {
            if (nuevo != null) {
                lblMotivo.setText(nuevo.getMotivo());
                lblFechaEvento.setText(formatearFechaVisual(nuevo.getFechaEvento()));
            } else {
                lblMotivo.setText("");
                lblFechaEvento.setText("");
            }
        });
    }

    private void cargarFechas() {
        List<String> fechasRaw = EventosExtensiblesDAO.obtenerFechasDisponibles();
        List<String> fechasFormateadas = new ArrayList<>();

        fechasFormateadas.add("Filtrar por fecha"); // ← opción visible como título
        fechasFormateadas.add("Todas");

        for (String f : fechasRaw) {
            fechasFormateadas.add(formatearFechaVisual(f));
        }

        comboFechas.setItems(FXCollections.observableArrayList(fechasFormateadas));
        comboFechas.getSelectionModel().select(0); // ← mostrar "Filtrar por fecha"
    }

    private void cargarEventos() {
        eventosFiltrados = EventosExtensiblesDAO.obtenerTodos();
        pagina = 1;
        actualizarLista();
    }

    private void filtrar() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String fechaSeleccionada = comboFechas.getValue();

        List<EventoExtensible> base = EventosExtensiblesDAO.obtenerTodos();
        eventosFiltrados.clear();

        for (EventoExtensible e : base) {
            boolean coincideTexto = e.getMotivo().toLowerCase().contains(texto);
            boolean sinFiltroFecha = fechaSeleccionada == null
                    || fechaSeleccionada.equals("Todas")
                    || fechaSeleccionada.equals("Filtrar por fecha");
            boolean coincideFecha = sinFiltroFecha
                    || formatearFechaVisual(e.getFechaEvento()).equals(fechaSeleccionada);

            if (coincideTexto && coincideFecha) {
                eventosFiltrados.add(e);
            }
        }

        pagina = 1;
        actualizarLista();
    }

    private void actualizarLista() {
        int totalEventos = eventosFiltrados.size();
        int totalPaginas = (int) Math.ceil((double) totalEventos / ELEMENTOS_POR_PAGINA);
        pagina = Math.max(1, Math.min(pagina, totalPaginas == 0 ? 1 : totalPaginas));

        int inicio = (pagina - 1) * ELEMENTOS_POR_PAGINA;
        int fin = Math.min(inicio + ELEMENTOS_POR_PAGINA, totalEventos);

        if (totalEventos == 0) {
            listaEventos.setItems(FXCollections.observableArrayList());
            paginaActual.setText("1 / 1");
            limpiarDetalle();
            return;
        }

        List<EventoExtensible> sublista = eventosFiltrados.subList(inicio, fin);
        listaEventos.setItems(FXCollections.observableArrayList(sublista));
        listaEventos.getSelectionModel().clearSelection();
        limpiarDetalle();

        paginaActual.setText(pagina + " / " + totalPaginas);
    }

    private void limpiarDetalle() {
        lblMotivo.setText("");
        lblFechaEvento.setText("");
    }

    @FXML
    private void eliminarEvento(ActionEvent event) {
        EventoExtensible seleccionado = listaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar este evento?", ButtonType.YES, ButtonType.NO);
            confirmacion.setHeaderText(null);
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.YES) {
                    EventosExtensiblesDAO.eliminar(seleccionado.getIdExtensible());
                    refrescarDatos();
                }
            });
        }
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        actualizarLista();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            actualizarLista();
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        int totalPaginas = (int) Math.ceil((double) eventosFiltrados.size() / ELEMENTOS_POR_PAGINA);
        if (pagina < totalPaginas) {
            pagina++;
            actualizarLista();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) eventosFiltrados.size() / ELEMENTOS_POR_PAGINA);
        actualizarLista();
    }

    private void refrescarDatos() {
        cargarFechas();
        filtrar(); // Ya incluye cargarEventos()
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void editarEvento(ActionEvent event) {
        EventoExtensible seleccionado = listaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecciona un evento para editar.", ButtonType.OK);
            alerta.setHeaderText(null);
            alerta.showAndWait();
        }
    }

    private void abrirFormulario(EventoExtensible evento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormEventosExtensibles.fxml"));
            Parent root = loader.load();
            FormEventosExtensiblesController controller = loader.getController();

            if (evento == null) {
                controller.setModoAgregar();
            } else {
                controller.setEventoEditar(evento);
            }

            controller.setOnGuardarCallback(this::refrescarDatos);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(evento == null ? "Nuevo Evento Extensible" : "Editar Evento Extensible");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatearFechaVisual(String fechaBD) {
        try {
            Date fecha = formatoBD.parse(fechaBD);
            return formatoVisual.format(fecha);
        } catch (ParseException e) {
            return fechaBD;
        }
    }
}
