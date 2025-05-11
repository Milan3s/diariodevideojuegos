package controllers;

import dao.ComboDAO;
import dao.MetasTwitchDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.MetasTwitch;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MetasTwitchController implements Initializable {

    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;
    @FXML
    private ListView<MetasTwitch> listaMetas;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private Text tituloMetas;
    @FXML
    private ImageView imgBoxart;
    @FXML
    private Label lblDescripcion, lblMeta, lblActual, lblMes, lblAnio, lblFechaInicio, lblFechaFin, lblFechaRegistro;
    @FXML
    private ComboBox<Integer> comboAnios;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;

    private final MetasTwitchDAO dao = new MetasTwitchDAO();
    private ObservableList<MetasTwitch> todasLasMetas = FXCollections.observableArrayList();
    private ObservableList<MetasTwitch> metasPaginadas = FXCollections.observableArrayList();

    private final int elementosPorPagina = 10;
    private int pagina = 1;
    private int totalPaginas = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaMetas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> mostrarDetalle(newVal));
        campoBusqueda.setOnKeyReleased(this::buscar);
        comboAnios.setOnAction(this::filtrarPorAnio);

        comboAnios.setItems(ComboDAO.cargarAniosMetasTwitch());
        comboAnios.getItems().add(0, null);
        comboAnios.setPromptText("Año");

        cargarMetas();
    }

    private void cargarMetas() {
        todasLasMetas = dao.obtenerMetas();
        aplicarFiltroYActualizarPaginacion();
    }

    private void aplicarFiltroYActualizarPaginacion() {
        String filtro = campoBusqueda.getText().toLowerCase();
        Integer anioSeleccionado = comboAnios.getValue();

        ObservableList<MetasTwitch> filtradas = todasLasMetas.filtered(meta -> {
            boolean coincideTexto = meta.getDescripcion().toLowerCase().contains(filtro)
                    || meta.getMes().toLowerCase().contains(filtro)
                    || String.valueOf(meta.getAnio()).contains(filtro);
            boolean coincideAnio = anioSeleccionado == null || meta.getAnio() == anioSeleccionado;
            return coincideTexto && coincideAnio;
        });

        totalPaginas = (int) Math.ceil((double) filtradas.size() / elementosPorPagina);
        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }
        if (pagina < 1) {
            pagina = 1;
        }

        int desde = (pagina - 1) * elementosPorPagina;
        int hasta = Math.min(desde + elementosPorPagina, filtradas.size());

        metasPaginadas.setAll(filtradas.subList(desde, hasta));
        listaMetas.setItems(metasPaginadas);
        paginaActual.setText(pagina + " / " + (totalPaginas == 0 ? 1 : totalPaginas));

    }

    private void mostrarDetalle(MetasTwitch meta) {
        if (meta == null) {
            lblDescripcion.setText("");
            lblMeta.setText("");
            lblActual.setText("");
            lblMes.setText("");
            lblAnio.setText("");
            lblFechaInicio.setText("");
            lblFechaFin.setText("");
            lblFechaRegistro.setText("");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        lblDescripcion.setText(meta.getDescripcion());
        lblMeta.setText(String.valueOf(meta.getMeta()));
        lblActual.setText(String.valueOf(meta.getActual()));
        lblMes.setText(meta.getMes());
        lblAnio.setText(String.valueOf(meta.getAnio()));
        lblFechaInicio.setText(meta.getFechaInicio().format(fmt));
        lblFechaFin.setText(meta.getFechaFin().format(fmt));
        lblFechaRegistro.setText(meta.getFechaRegistro().format(fmt));
    }

    private void buscar(KeyEvent event) {
        pagina = 1;
        aplicarFiltroYActualizarPaginacion();
    }

    private void filtrarPorAnio(ActionEvent event) {
        pagina = 1;
        aplicarFiltroYActualizarPaginacion();
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetasTwitch.fxml"));
            Parent root = loader.load();

            FormMetasTwitchController controller = loader.getController();
            controller.setOnGuardarCallback(() -> {
                cargarMetas();
                seleccionarMetaPorId(controller.getMetaGuardada().getIdMeta());
            });

            Stage stage = new Stage();
            stage.setTitle("Nueva Meta Twitch");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarMeta(ActionEvent event) {
        MetasTwitch seleccionada = listaMetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una meta para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMetasTwitch.fxml"));
            Parent root = loader.load();

            FormMetasTwitchController controller = loader.getController();
            controller.setMeta(seleccionada);
            controller.setOnGuardarCallback(() -> {
                cargarMetas();
                seleccionarMetaPorId(controller.getMetaGuardada().getIdMeta());
            });

            Stage stage = new Stage();
            stage.setTitle("Editar Meta Twitch");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarMeta(ActionEvent event) {
        MetasTwitch seleccionada = listaMetas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una meta para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("¿Estás seguro?");
        alert.setContentText("Esta acción eliminará la meta seleccionada.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dao.eliminarMeta(seleccionada.getIdMeta());
                cargarMetas();
            }
        });
    }

    private void seleccionarMetaPorId(int id) {
        for (int i = 0; i < todasLasMetas.size(); i++) {
            if (todasLasMetas.get(i).getIdMeta() == id) {
                pagina = (i / elementosPorPagina) + 1;
                aplicarFiltroYActualizarPaginacion();
                int indiceEnPagina = i % elementosPorPagina;
                listaMetas.getSelectionModel().select(indiceEnPagina);
                listaMetas.scrollTo(indiceEnPagina);
                break;
            }
        }
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        aplicarFiltroYActualizarPaginacion();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            aplicarFiltroYActualizarPaginacion();
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        if (pagina < totalPaginas) {
            pagina++;
            aplicarFiltroYActualizarPaginacion();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = totalPaginas;
        aplicarFiltroYActualizarPaginacion();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
