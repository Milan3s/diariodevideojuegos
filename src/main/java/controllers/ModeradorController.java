package controllers;

import dao.ComboDAO;
import dao.ModeradorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.Moderador;
import models.Estado;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModeradorController implements Initializable {

    @FXML
    private Text tituloLogros;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private Button btnLimpiar, btnAgregar, btnReadmitir, btnEliminar, btnEditar, btnDarDeBaja;
    @FXML
    private ListView<Moderador> listaModeradores;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private Label lblNombre, lblEmail, lblEstado, lblFechaAlta, lblFechaBaja;

    private final ModeradorDAO dao = new ModeradorDAO();
    private final ObservableList<Moderador> listaOriginal = FXCollections.observableArrayList();
    private final ObservableList<Moderador> listaFiltrada = FXCollections.observableArrayList();
    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;
    private Moderador moderadorSeleccionado;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarModeradores();
        configurarListView();
        cargarCombos();
    }

    private void cargarCombos() {
        Estado estadoTodos = new Estado(-1, "Todos");
        ObservableList<Estado> estados = ComboDAO.cargarEstadosModeradores();
        estados.add(0, estadoTodos);
        comboEstado.setItems(estados);
        comboEstado.getSelectionModel().selectFirst();
        comboEstado.setOnAction(e -> aplicarFiltros());
    }

    private void cargarModeradores() {
        listaOriginal.setAll(dao.obtenerModeradores());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText().toLowerCase().trim();
        Estado estadoSeleccionado = comboEstado.getSelectionModel().getSelectedItem();

        listaFiltrada.setAll(listaOriginal.stream()
                .filter(m -> texto.isEmpty() || m.getNombre().toLowerCase().contains(texto))
                .filter(m -> estadoSeleccionado == null || estadoSeleccionado.getId() == -1 || (m.getEstado() != null && m.getEstado().getId() == estadoSeleccionado.getId()))
                .collect(Collectors.toList()));

        pagina = 1;
        actualizarPaginado();
    }

    private void actualizarPaginado() {
        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, listaFiltrada.size());
        listaModeradores.setItems(FXCollections.observableArrayList(listaFiltrada.subList(desde, hasta)));
        listaModeradores.getSelectionModel().clearSelection();
        paginaActual.setText(String.valueOf(pagina));
    }

    private void configurarListView() {
        listaModeradores.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Moderador item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        listaModeradores.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevo) -> mostrarDetalle());
    }

    private void mostrarDetalle() {
        moderadorSeleccionado = listaModeradores.getSelectionModel().getSelectedItem();
        if (moderadorSeleccionado != null) {
            lblNombre.setText(moderadorSeleccionado.getNombre());
            lblEmail.setText(moderadorSeleccionado.getEmail());
            lblEstado.setText(moderadorSeleccionado.getEstado() != null ? moderadorSeleccionado.getEstado().getNombre() : "");
            lblFechaAlta.setText(formatearFecha(moderadorSeleccionado.getFechaAlta()));
            lblFechaBaja.setText(formatearFecha(moderadorSeleccionado.getFechaBaja()));
        }
    }

    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return "";
        }
        try {
            return LocalDate.parse(fecha).format(formatter);
        } catch (Exception e) {
            return fecha; // Por si está ya en formato legible
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        campoBusqueda.clear();
        comboEstado.getSelectionModel().selectFirst();
        listaModeradores.getSelectionModel().clearSelection();
        lblNombre.setText("");
        lblEmail.setText("");
        lblEstado.setText("");
        lblFechaAlta.setText("");
        lblFechaBaja.setText("");
        aplicarFiltros();
    }

    @FXML
    private void abrirModalAltaModerador(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormModerador.fxml"));
            Parent root = loader.load();
            FormModeradorController controller = loader.getController();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Alta de Moderador");
            modal.setResizable(false);

            controller.setOnGuardarCallback(() -> {
                Moderador guardado = controller.getModeradorGuardado();
                cargarModeradores();
                if (guardado != null) {
                    seleccionarModeradorPorId(guardado.getId());
                }
            });

            modal.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void editarModerador(ActionEvent event) {
        if (moderadorSeleccionado == null) {
            mostrarAlerta("Seleccione un moderador para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormModerador.fxml"));
            Parent root = loader.load();
            FormModeradorController controller = loader.getController();
            controller.cargarModeradorParaEditar(moderadorSeleccionado);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Editar Moderador");
            modal.setResizable(false);

            controller.setOnGuardarCallback(() -> {
                Moderador guardado = controller.getModeradorGuardado();
                cargarModeradores();
                if (guardado != null) {
                    seleccionarModeradorPorId(guardado.getId());
                }
            });

            modal.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("Error al abrir el formulario de edición: " + e.getMessage());
        }
    }

    private void seleccionarModeradorPorId(int id) {
        for (int i = 0; i < listaFiltrada.size(); i++) {
            if (listaFiltrada.get(i).getId() == id) {
                pagina = (i / ITEMS_POR_PAGINA) + 1;
                actualizarPaginado();
                int indexPagina = i % ITEMS_POR_PAGINA;
                listaModeradores.getSelectionModel().select(indexPagina);
                listaModeradores.scrollTo(indexPagina);
                break;
            }
        }
    }

    @FXML
    private void readmitirModerador(ActionEvent event) {
        if (moderadorSeleccionado == null) {
            mostrarAlerta("Seleccione un moderador para readmitir.");
            return;
        }
        boolean exito = dao.readmitirModerador(moderadorSeleccionado.getId(), LocalDate.now().format(formatter));
        if (exito) {
            mostrarAlerta("Moderador readmitido correctamente.");
            cargarModeradores();
        } else {
            mostrarAlerta("No se pudo readmitir al moderador.");
        }
    }

    @FXML
    private void eliminarModerador(ActionEvent event) {
        if (moderadorSeleccionado == null) {
            mostrarAlerta("Seleccione un moderador para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar moderador seleccionado?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Eliminar Moderador");
        alert.setHeaderText(moderadorSeleccionado.getNombre());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean ok = dao.eliminarModerador(moderadorSeleccionado.getId());
                if (ok) {
                    mostrarAlerta("Moderador eliminado correctamente.");
                    cargarModeradores();
                } else {
                    mostrarAlerta("No se pudo eliminar al moderador.");
                }
            }
        });
    }

    @FXML
    private void darDeBajaModerador(ActionEvent event) {
        if (moderadorSeleccionado == null) {
            mostrarAlerta("Por favor, seleccione un moderador.");
            return;
        }

        boolean exito = dao.darDeBajaModerador(moderadorSeleccionado.getId());
        if (exito) {
            mostrarAlerta("Moderador dado de baja correctamente.");
            cargarModeradores();
        } else {
            mostrarAlerta("Error al dar de baja al moderador.");
        }
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
        int total = (int) Math.ceil((double) listaFiltrada.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
            actualizarPaginado();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) listaFiltrada.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
