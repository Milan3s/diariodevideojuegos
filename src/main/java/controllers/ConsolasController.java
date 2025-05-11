package controllers;

import config.Conexion;
import dao.ComboDAO;
import dao.ConsolaDAO;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import models.Consola;
import models.Estado;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ConsolasController implements Initializable {

    @FXML
    private Text tituloConsolas;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button btnLimpiar, btnAgregar, btnEditar, btnEliminar;
    @FXML
    private ListView<Consola> listaConsolas;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private ImageView imgDetalle;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblAbreviatura;
    @FXML
    private Label lblAnio, lblFabricante, lblGeneracion, lblRegion, lblTipo, lblEstado;

    private ObservableList<Consola> todasLasConsolas = FXCollections.observableArrayList();
    private ObservableList<Consola> consolasFiltradas = FXCollections.observableArrayList();
    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;
    private Consola consolaSeleccionada;
    @FXML
    private Label lblProcesador;
    @FXML
    private Label lblMemoria;
    @FXML
    private Label lblFrecuencia;
    @FXML
    private Label lblTieneChip;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
        cargarConsolas();
        configurarListView();
    }

    private void cargarCombos() {
        ObservableList<Estado> estados = ComboDAO.cargarEstadosConsolas();

        // Agrega opción "Todos"
        estados.add(0, new Estado(-1, "Todos"));

        comboEstado.setItems(estados);

        comboEstado.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null || item.getId() == -1) ? "Estados" : item.getNombre());
            }
        });

        comboEstado.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        comboEstado.getSelectionModel().selectFirst();
        comboEstado.setOnAction(this::filtrarConsolas);
    }

    private void cargarConsolas() {
        todasLasConsolas.setAll(new ConsolaDAO().obtenerTodas());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        Estado estadoSel = comboEstado.getSelectionModel().getSelectedItem();

        // Si no hay ningún filtro activo, mostrar todo
        if ((texto.isEmpty()) && (estadoSel == null || estadoSel.getId() == -1)) {
            consolasFiltradas.setAll(todasLasConsolas);
        } else {
            consolasFiltradas.setAll(todasLasConsolas.stream()
                    .filter(c -> texto.isEmpty() || c.getNombre().toLowerCase().contains(texto))
                    .filter(c -> estadoSel == null || estadoSel.getId() == -1
                    || (c.getEstado() != null && c.getEstado().getId() == estadoSel.getId()))
                    .collect(Collectors.toList()));
        }

        pagina = 1;
        actualizarPaginado();
    }

    private void actualizarPaginado() {
        int totalPaginas = (int) Math.ceil((double) consolasFiltradas.size() / ITEMS_POR_PAGINA);
        if (totalPaginas == 0) {
            totalPaginas = 1;
        }

        if (pagina < 1) {
            pagina = 1;
        }
        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }

        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, consolasFiltradas.size());

        if (desde >= consolasFiltradas.size()) {
            desde = 0;
            hasta = Math.min(ITEMS_POR_PAGINA, consolasFiltradas.size());
            pagina = 1;
        }

        listaConsolas.setItems(FXCollections.observableArrayList(consolasFiltradas.subList(desde, hasta)));
        paginaActual.setText(pagina + " / " + totalPaginas);
    }

    private void configurarListView() {
        listaConsolas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Consola item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        listaConsolas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nueva) -> {
            if (nueva != null) {
                consolaSeleccionada = nueva;
                mostrarDetalle(nueva);
            } else {
                limpiarDetalle();
            }
        });
    }

    private void mostrarDetalle(Consola consola) {
        lblNombre.setText(consola.getNombre());
        lblAbreviatura.setText(consola.getAbreviatura());
        lblAnio.setText(consola.getAnio() != null ? String.valueOf(consola.getAnio()) : "No disponible");
        lblFabricante.setText(consola.getFabricante());
        lblGeneracion.setText(consola.getGeneracion());
        lblRegion.setText(consola.getRegion());
        lblTipo.setText(consola.getTipo());

        lblProcesador.setText(consola.getProcesador());
        lblMemoria.setText(consola.getMemoria());
        lblFrecuencia.setText(consola.getFrecuenciaMHz() != null ? consola.getFrecuenciaMHz() + " MHz" : "No disponible");
        lblTieneChip.setText(consola.tieneChip() ? "Sí" : "No");

        if (consola.getEstado() != null && consola.getEstado().getNombre() != null) {
            lblEstado.setText(consola.getEstado().getNombre());
        } else {
            lblEstado.setText("No disponible");
        }

        File imageFile = new File(Conexion.imagenesPath, consola.getImagen() != null ? consola.getImagen() : "");
        if (imageFile.exists()) {
            imgDetalle.setImage(new Image(imageFile.toURI().toString()));
            iconoImagenNoDisponible.setVisible(false);
        } else {
            imgDetalle.setImage(null);
            iconoImagenNoDisponible.setVisible(true);
        }
    }

    private void limpiarDetalle() {
        lblNombre.setText("");
        lblAbreviatura.setText("");
        lblAnio.setText("");
        lblFabricante.setText("");
        lblGeneracion.setText("");
        lblRegion.setText("");
        lblTipo.setText("");
        lblEstado.setText("");
        lblProcesador.setText("");
        lblMemoria.setText("");
        lblFrecuencia.setText("");
        lblTieneChip.setText("");
        imgDetalle.setImage(null);
        iconoImagenNoDisponible.setVisible(false);
    }

    private void filtrarConsolas(ActionEvent event) {
        aplicarFiltros();
    }

    private void filtrarConsolas(KeyEvent event) {
        aplicarFiltros();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        comboEstado.getSelectionModel().selectFirst();
        campoBusqueda.clear();
        listaConsolas.getSelectionModel().clearSelection();
        limpiarDetalle();
        aplicarFiltros(); // muy importante para mostrar todo
    }

    @FXML
    private void abrirModalAgregarConsola(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormConsolas.fxml"));
            Parent root = loader.load();
            FormConsolasController controller = loader.getController();
            controller.limpiarFormulario();

            controller.setOnGuardarCallback(() -> {
                cargarConsolas();
                seleccionarUltimaConsola();
            });

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            modal.setTitle("Añadir Consola");
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seleccionarUltimaConsola() {
        if (!consolasFiltradas.isEmpty()) {
            int totalPaginas = (int) Math.ceil((double) consolasFiltradas.size() / ITEMS_POR_PAGINA);
            pagina = totalPaginas;
            actualizarPaginado();

            listaConsolas.getSelectionModel().selectLast();
            listaConsolas.scrollTo(listaConsolas.getItems().size() - 1);
        }
    }

    @FXML
    private void editarConsola(ActionEvent event) {
        if (consolaSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormConsolas.fxml"));
                Parent root = loader.load();
                FormConsolasController controller = loader.getController();
                controller.cargarConsolaParaEditar(consolaSeleccionada);

                controller.setOnGuardarCallback(() -> {
                    cargarConsolas();
                    seleccionarConsolaPorId(consolaSeleccionada.getId());
                });

                Stage modal = new Stage();
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.setScene(new Scene(root));
                modal.setTitle("Editar Consola");
                modal.setResizable(false);
                modal.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Seleccione una consola para editar.");
        }
    }

    private void seleccionarConsolaPorId(int id) {
        for (int i = 0; i < consolasFiltradas.size(); i++) {
            if (consolasFiltradas.get(i).getId() == id) {
                int paginaConsola = (i / ITEMS_POR_PAGINA) + 1;
                pagina = paginaConsola;
                actualizarPaginado();

                int indiceEnPagina = i % ITEMS_POR_PAGINA;
                listaConsolas.getSelectionModel().select(indiceEnPagina);
                listaConsolas.scrollTo(indiceEnPagina);
                break;
            }
        }
    }

    @FXML
    private void eliminarConsola(ActionEvent event) {
        if (consolaSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Consola");
            alert.setHeaderText("¿Eliminar consola seleccionada?");
            alert.setContentText(consolaSeleccionada.getNombre());
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    boolean ok = new ConsolaDAO().eliminar(consolaSeleccionada.getId());
                    if (ok) {
                        mostrarAlerta("Consola eliminada.");
                        cargarConsolas();
                    } else {
                        mostrarAlerta("No se pudo eliminar.");
                    }
                }
            });
        } else {
            mostrarAlerta("Seleccione una consola para eliminar.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
        }
        actualizarPaginado();
    }

    @FXML
    private void irPaginaSiguiente(ActionEvent event) {
        int total = (int) Math.ceil((double) consolasFiltradas.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
        }
        actualizarPaginado();
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) consolasFiltradas.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }
}
