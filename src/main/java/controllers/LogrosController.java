package controllers;

import config.Conexion;
import dao.ComboDAO;
import dao.LogrosDAO;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Estado;
import models.Logros;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LogrosController implements Initializable {

    @FXML
    private Text tituloLogros;
    @FXML
    private ComboBox<Estado> comboEstado;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button btnLimpiar, btnAgregar, btnEditar, btnEliminar;
    @FXML
    private ListView<Logros> listaLogros;
    @FXML
    private Button btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    @FXML
    private Label paginaActual;
    @FXML
    private ImageView imgBoxart;
    @FXML
    private Label lblNombre, lblDescripcion, lblJuego, lblConsola, lblEstado,
            lblDificultad, lblHoras, lblIntentos, lblPuntuacion, lblCreditos;
    @FXML
    private FontAwesomeIconView iconoImagenNoDisponible;

    private final LogrosDAO logrosDAO = new LogrosDAO();
    private final ObservableList<Logros> logrosOriginales = FXCollections.observableArrayList();
    private final ObservableList<Logros> logrosFiltrados = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 30;
    private int pagina = 1;
    private Logros logroSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarComboEstado();
        comboEstado.setOnAction(this::filtrarLogros);
        campoBusqueda.setOnKeyReleased(this::filtrarLogros);

        cargarLogros();
        listaLogros.setOnMouseClicked(this::mostrarDetalle);
    }

    private void cargarComboEstado() {
        ObservableList<Estado> estados = ComboDAO.cargarEstadosPorTipo("logro");
        estados.add(0, new Estado(-1, "Todos"));
        comboEstado.setItems(estados);

        // Mostrar "Estados" en lugar de "Todos" cuando se selecciona
        comboEstado.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null || item.getId() == -1) ? "Estados" : item.getNombre());
            }
        });

        // Mostrar solo los nombres en la lista desplegable
        comboEstado.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        comboEstado.getSelectionModel().selectFirst(); // Selecciona "Todos"
    }

    private void cargarLogros() {
        logrosOriginales.setAll(logrosDAO.obtenerLogros());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        Estado estadoSel = comboEstado.getSelectionModel().getSelectedItem();

        if ((texto.isEmpty()) && (estadoSel == null || estadoSel.getId() == -1)) {
            logrosFiltrados.setAll(logrosOriginales);
        } else {
            logrosFiltrados.setAll(logrosOriginales.stream()
                    .filter(l -> texto.isEmpty() || l.getNombre().toLowerCase().contains(texto))
                    .filter(l -> estadoSel == null || estadoSel.getId() == -1
                    || (l.getEstado() != null && l.getEstado().getId() == estadoSel.getId()))
                    .collect(Collectors.toList()));
        }

        pagina = 1;
        actualizarPaginado();
    }

    private void actualizarPaginado() {
        int totalLogros = logrosFiltrados.size();
        int totalPaginas = (int) Math.ceil((double) totalLogros / ITEMS_POR_PAGINA);

        if (totalPaginas == 0) {
            pagina = 1;
            listaLogros.setItems(FXCollections.observableArrayList());
            limpiarDetalle();
            paginaActual.setText("1 / 1");
            return;
        }

        // Asegurar que la página esté dentro del rango válido
        if (pagina < 1) {
            pagina = 1;
        }
        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }

        int desde = (pagina - 1) * ITEMS_POR_PAGINA;
        int hasta = Math.min(desde + ITEMS_POR_PAGINA, totalLogros);

        // Prevenir posibles errores si desde >= hasta
        if (desde >= hasta || desde < 0) {
            listaLogros.setItems(FXCollections.observableArrayList());
        } else {
            listaLogros.setItems(FXCollections.observableArrayList(logrosFiltrados.subList(desde, hasta)));
        }

        listaLogros.getSelectionModel().clearSelection();
        limpiarDetalle();
        paginaActual.setText(pagina + " / " + totalPaginas);
    }

    private void filtrarLogros(ActionEvent e) {
        aplicarFiltros();
    }

    private void filtrarLogros(KeyEvent e) {
        aplicarFiltros();
    }

    private void mostrarDetalle(MouseEvent event) {
        logroSeleccionado = listaLogros.getSelectionModel().getSelectedItem();
        if (logroSeleccionado == null) {
            return;
        }

        lblNombre.setText(logroSeleccionado.getNombre());
        lblDescripcion.setText(logroSeleccionado.getDescripcion());
        lblJuego.setText(logroSeleccionado.getJuego() != null ? logroSeleccionado.getJuego().getNombre() : "");
        lblConsola.setText(logroSeleccionado.getConsola() != null ? logroSeleccionado.getConsola().getNombre() : "");
        lblEstado.setText(logroSeleccionado.getEstado() != null ? logroSeleccionado.getEstado().getNombre() : "");
        lblDificultad.setText(logroSeleccionado.getDificultad() != null ? logroSeleccionado.getDificultad().getNombre() : "Sin dificultad");
        lblHoras.setText(String.valueOf(logroSeleccionado.getHorasEstimadas()));
        lblIntentos.setText(String.valueOf(logroSeleccionado.getIntentos()));
        lblPuntuacion.setText(String.valueOf(logroSeleccionado.getPuntuacion()));
        lblCreditos.setText(String.valueOf(logroSeleccionado.getCreditos()));

        if (logroSeleccionado.getJuego() != null && logroSeleccionado.getJuego().getImagen() != null) {
            File imgFile = new File(Conexion.imagenesPath + File.separator + logroSeleccionado.getJuego().getImagen());
            if (imgFile.exists()) {
                imgBoxart.setImage(new Image(imgFile.toURI().toString()));
                iconoImagenNoDisponible.setVisible(false);
            } else {
                imgBoxart.setImage(null);
                iconoImagenNoDisponible.setVisible(true);
            }
        } else {
            imgBoxart.setImage(null);
            iconoImagenNoDisponible.setVisible(true);
        }
    }

    private void limpiarDetalle() {
        lblNombre.setText("");
        lblDescripcion.setText("");
        lblJuego.setText("");
        lblConsola.setText("");
        lblEstado.setText("");
        lblDificultad.setText("");
        lblHoras.setText("");
        lblIntentos.setText("");
        lblPuntuacion.setText("");
        lblCreditos.setText("");
        imgBoxart.setImage(null);
        iconoImagenNoDisponible.setVisible(false);
    }

    @FXML
    private void abrirModalAgregarLogro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormLogros.fxml"));
            Parent root = loader.load();

            FormLogrosController controller = loader.getController();
            controller.limpiarFormulario();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Añadir Logro");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            // Recargar logros
            cargarLogros();

            // Obtener el logro insertado desde el formulario
            Logros nuevoLogro = (Logros) root.getUserData();
            if (nuevoLogro != null) {
                seleccionarLogroPorId(nuevoLogro.getId());
            }

        } catch (IOException e) {
            mostrarError("Error al abrir el formulario de nuevo logro", e);
        }
    }

    @FXML
    private void editarLogro(ActionEvent event) {
        if (logroSeleccionado == null) {
            mostrarAlerta("Seleccione un logro para editar.");
            return;
        }

        int idEditado = logroSeleccionado.getId();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormLogros.fxml"));
            Parent root = loader.load();

            FormLogrosController controller = loader.getController();
            controller.cargarLogroParaEditar(logroSeleccionado);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Editar Logro");
            modal.setScene(new Scene(root));
            modal.setResizable(false);
            modal.showAndWait();

            cargarLogros();  // recarga la lista completa
            seleccionarLogroPorId(idEditado);  // selecciona el logro que fue editado

        } catch (IOException e) {
            mostrarError("Error al abrir el formulario de edición", e);
        }
    }

    @FXML
    private void eliminarLogro(ActionEvent event) {
        if (logroSeleccionado == null) {
            mostrarAlerta("Seleccione un logro para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar logro seleccionado?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Eliminar Logro");
        alert.setHeaderText(logroSeleccionado.getNombre());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean ok = logrosDAO.eliminarLogro(logroSeleccionado.getId());
                if (ok) {
                    mostrarAlerta("Logro eliminado correctamente.");
                    cargarLogros();
                } else {
                    mostrarAlerta("No se pudo eliminar el logro.");
                }
            }
        });
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        comboEstado.getSelectionModel().selectFirst();
        campoBusqueda.clear();
        listaLogros.getSelectionModel().clearSelection();
        limpiarDetalle();
        aplicarFiltros();
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
        int total = (int) Math.ceil((double) logrosFiltrados.size() / ITEMS_POR_PAGINA);
        if (pagina < total) {
            pagina++;
            actualizarPaginado();
        }
    }

    @FXML
    private void irUltimaPagina(ActionEvent event) {
        pagina = (int) Math.ceil((double) logrosFiltrados.size() / ITEMS_POR_PAGINA);
        actualizarPaginado();
    }

    private void seleccionarLogroPorId(int id) {
        for (int i = 0; i < logrosFiltrados.size(); i++) {
            if (logrosFiltrados.get(i).getId() == id) {
                int paginaLogro = (i / ITEMS_POR_PAGINA) + 1;
                pagina = paginaLogro;
                actualizarPaginado();

                int indiceEnPagina = i % ITEMS_POR_PAGINA;
                listaLogros.getSelectionModel().select(indiceEnPagina);
                listaLogros.scrollTo(indiceEnPagina);
                mostrarDetalle(null);
                break;
            }
        }
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
