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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MejorasdelcanalController implements Initializable {

    @FXML
    private Text tituloMejoras;
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ListView<MejorasDelCanal> listaMejoras;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private Label lblDescripcion, lblFecha;

    private final MejorasDelCanalDAO dao = new MejorasDelCanalDAO();
    private final ObservableList<MejorasDelCanal> mejorasOriginales = FXCollections.observableArrayList();
    private final ObservableList<MejorasDelCanal> mejorasFiltradas = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 15;
    private int pagina = 1;
    private MejorasDelCanal mejoraSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoBusqueda.setOnKeyReleased(this::filtrarMejoras);
        cargarMejoras();
        listaMejoras.setOnMouseClicked(this::mostrarDetalle);
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
        if (pagina < 1) {
            pagina = 1;
        }
        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }

        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, mejorasFiltradas.size());

        if (desde > hasta) {
            desde = 0;
            hasta = Math.min(ITEMS_POR_PAGINA, mejorasFiltradas.size());
            pagina = 1;
        }

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
        lblFecha.setText(mejoraSeleccionada.getFecha().toString());
    }

    private void limpiarDetalle() {
        lblDescripcion.setText("");
        lblFecha.setText("");
    }

    @FXML
    private void abrirModalAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMejorasDelCanal.fxml"));
            Parent root = loader.load();

            FormMejorasDelCanalController controller = loader.getController();
            controller.limpiarFormulario();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Añadir Mejora");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarMejoras();
        } catch (IOException e) {
            mostrarError("Error al abrir el formulario de mejora", e);
        }
    }

    @FXML
    private void editarMejora(ActionEvent event) {
        if (mejoraSeleccionada == null) {
            mostrarAlerta("Seleccione una mejora para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormMejorasDelCanal.fxml"));
            Parent root = loader.load();

            FormMejorasDelCanalController controller = loader.getController();
            controller.cargarMejoraParaEditar(mejoraSeleccionada);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Editar Mejora");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarMejoras();
        } catch (IOException e) {
            mostrarError("Error al abrir el formulario de edición", e);
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
}
