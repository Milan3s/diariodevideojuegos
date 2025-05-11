package controllers;

import dao.MejorasDelCanalDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.MejorasDelCanal;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MejorasdelcanalController implements Initializable {

    @FXML
    private Text tituloMejoras;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ListView<MejorasDelCanal> listaMejoras;
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;

    @FXML
    private Label lblDescripcion, lblMeta, lblActual, lblFechaInicio, lblFechaFin, lblCumplida;

    private final MejorasDelCanalDAO dao = new MejorasDelCanalDAO();
    private final ObservableList<MejorasDelCanal> mejorasOriginales = FXCollections.observableArrayList();
    private final ObservableList<MejorasDelCanal> mejorasFiltradas = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 15;
    private int pagina = 1;
    private MejorasDelCanal mejoraSeleccionada;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML
    private ComboBox<?> cboxFecha;
    @FXML
    private ComboBox<?> cboxSiNo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoBusqueda.setOnKeyReleased(this::filtrarMejoras);
        listaMejoras.setOnMouseClicked(this::mostrarDetalle);
        cargarMejoras();
    }

    private void cargarMejoras() {
        mejorasOriginales.setAll(dao.obtenerMejoras());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";

        if (texto.isEmpty()) {
            mejorasFiltradas.setAll(mejorasOriginales);
        } else {
            mejorasFiltradas.setAll(mejorasOriginales.stream()
                    .filter(m -> m.getDescripcion().toLowerCase().contains(texto))
                    .collect(Collectors.toList()));
        }

        pagina = 1;
        actualizarPaginado();
    }

    private void actualizarPaginado() {
        int totalPaginas = (int) Math.ceil((double) mejorasFiltradas.size() / ITEMS_POR_PAGINA);
        pagina = Math.max(1, Math.min(pagina, totalPaginas));

        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, mejorasFiltradas.size());

        listaMejoras.setItems(FXCollections.observableArrayList(mejorasFiltradas.subList(desde, hasta)));
        listaMejoras.getSelectionModel().clearSelection();
        limpiarDetalle();

        paginaActual.setText(pagina + " / " + (totalPaginas == 0 ? 1 : totalPaginas));
    }

    private void mostrarDetalle(MouseEvent event) {
        mejoraSeleccionada = listaMejoras.getSelectionModel().getSelectedItem();
        if (mejoraSeleccionada == null) {
            return;
        }

        lblDescripcion.setText(mejoraSeleccionada.getDescripcion());
        lblMeta.setText(String.valueOf(mejoraSeleccionada.getMeta()));
        lblActual.setText(String.valueOf(mejoraSeleccionada.getActual()));
        lblFechaInicio.setText(mejoraSeleccionada.getFechaInicio().format(formatoFecha));
        lblFechaFin.setText(mejoraSeleccionada.getFechaFin().format(formatoFecha));
        lblCumplida.setText(mejoraSeleccionada.isCumplida() ? "Sí" : "No");
    }

    private void limpiarDetalle() {
        lblDescripcion.setText("");
        lblMeta.setText("");
        lblActual.setText("");
        lblFechaInicio.setText("");
        lblFechaFin.setText("");
        lblCumplida.setText("");
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void editarMejora(ActionEvent event) {
        if (mejoraSeleccionada == null) {
            mostrarAlerta("Seleccione una mejora para editar.");
            return;
        }
        abrirFormulario(mejoraSeleccionada);
    }

    private void abrirFormulario(MejorasDelCanal mejora) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMejorasDelCanal.fxml"));
            Parent root = loader.load();
            FormMejorasDelCanalController controller = loader.getController();

            if (mejora != null) {
                controller.cargarMejoraParaEditar(mejora);
            } else {
                controller.limpiarFormulario();
            }

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle(mejora == null ? "Añadir Mejora" : "Editar Mejora");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            Object resultado = root.getUserData();
            cargarMejoras();

            if (resultado instanceof MejorasDelCanal) {
                MejorasDelCanal mejoraActualizada = (MejorasDelCanal) resultado;

                int index = mejorasFiltradas.indexOf(
                        mejorasFiltradas.stream()
                                .filter(m -> m.getId() == mejoraActualizada.getId())
                                .findFirst().orElse(null)
                );

                if (index >= 0) {
                    listaMejoras.getSelectionModel().select(index);
                    listaMejoras.scrollTo(index);
                    mostrarDetalle(null);
                }
            }

        } catch (IOException e) {
            mostrarError("Error al abrir el formulario de mejora", e);
        }
    }

    @FXML
    private void eliminarMejora(ActionEvent event) {
        if (mejoraSeleccionada == null) {
            mostrarAlerta("Seleccione una mejora para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar mejora seleccionada?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Eliminar Mejora");
        alert.setHeaderText(mejoraSeleccionada.getDescripcion());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean ok = dao.eliminarMejora(mejoraSeleccionada.getId());
                if (ok) {
                    mostrarAlerta("Mejora eliminada correctamente.");
                    cargarMejoras();
                } else {
                    mostrarAlerta("No se pudo eliminar la mejora.");
                }
            }
        });
    }

    @FXML
    private void irPrimeraPagina(ActionEvent event) {
        pagina = 1;
        actualizarPaginado();
    }

    @FXML
    private void irPaginaAnterior(ActionEvent event) {
        if (pagina > 1) {
            pagina--;
            actualizarPaginado();
        }
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        int total = (int) Math.ceil((double) mejorasFiltradas.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
            actualizarPaginado();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) mejorasFiltradas.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }

    private void filtrarMejoras(KeyEvent e) {
        aplicarFiltros();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(mensaje);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    @FXML
    private void handledcboxFecha(ActionEvent event) {
    }

    @FXML
    private void hanledcboxSiNo(ActionEvent event) {
    }
}
