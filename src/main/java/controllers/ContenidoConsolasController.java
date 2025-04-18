package controllers;

import config.Database;
import config.Logger;
import dao.ConsolaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Consola;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ContenidoConsolasController implements Initializable {

    @FXML
    private ListView<Consola> list_view_consolas;
    @FXML
    private Label lbl_nombre_consola;
    @FXML
    private TextField btn_buscar_consolas;
    @FXML
    private Label lbl_paginado;
    @FXML
    private Text lbl_total_consolas;

    @FXML
    private Label lbl_anio;
    @FXML
    private Label lbl_fabricante;
    @FXML
    private Label lbl_generacion;
    @FXML
    private Label lbl_region;
    @FXML
    private Label lbl_tipo;
    @FXML
    private Label lbl_procesador;
    @FXML
    private Label lbl_memoria;
    @FXML
    private Label lbl_almacenamiento;
    @FXML
    private Label lbl_fecha_lanzamiento;

    @FXML
    private ImageView imagen_consola;

    private final ObservableList<Consola> todasLasConsolas = FXCollections.observableArrayList();
    private static final int CONSOLAS_POR_PAGINA = 20;
    private int paginaActual = 0;
    private int totalPaginas = 1;
    @FXML
    private Button btn_editar;
    @FXML
    private Button btn_eliminar;
    @FXML
    private AnchorPane root_contenido_consolas;
    @FXML
    private Button btn_agregar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Logger.info("Inicializando ContenidoConsolasController");
        list_view_consolas.setOnMouseClicked(event -> mostrarInfoConsolaSeleccionada());
        cargarConsolas();
    }

    private void cargarConsolas() {
        todasLasConsolas.setAll(ConsolaDAO.obtenerTodasLasConsolas());
        int totalConsolas = ConsolaDAO.contarConsolas();
        lbl_total_consolas.setText(String.valueOf(totalConsolas));
        calcularPaginas();
        mostrarPagina(paginaActual);
    }

    private void calcularPaginas() {
        int total = todasLasConsolas.size();
        totalPaginas = Math.max(1, (int) Math.ceil((double) total / CONSOLAS_POR_PAGINA));
        paginaActual = 0;
    }

    private void mostrarPagina(int pagina) {
        int inicio = pagina * CONSOLAS_POR_PAGINA;
        int fin = Math.min(inicio + CONSOLAS_POR_PAGINA, todasLasConsolas.size());

        if (inicio >= fin || inicio < 0) {
            Logger.warn("Índice de página fuera de rango: " + pagina);
            list_view_consolas.setItems(FXCollections.observableArrayList());
        } else {
            List<Consola> subLista = todasLasConsolas.subList(inicio, fin);
            list_view_consolas.setItems(FXCollections.observableArrayList(subLista));
            lbl_paginado.setText("Página " + (pagina + 1) + " de " + totalPaginas);
        }
    }

    @FXML
    private void accion_buscar_consolas(ActionEvent event) {
        String textoBusqueda = btn_buscar_consolas.getText().trim();
        List<Consola> resultado = ConsolaDAO.buscarPorNombre(textoBusqueda);
        todasLasConsolas.setAll(resultado);
        calcularPaginas();
        mostrarPagina(paginaActual);
    }

    private void mostrarInfoConsolaSeleccionada() {
        Consola c = list_view_consolas.getSelectionModel().getSelectedItem();
        if (c != null) {
            lbl_nombre_consola.setText(c.getNombre() != null ? c.getNombre() : "");
            lbl_anio.setText(c.getAnio() != null ? String.valueOf(c.getAnio()) : "");
            lbl_fabricante.setText(c.getFabricante() != null ? c.getFabricante() : "");
            lbl_generacion.setText(c.getGeneracion() != null ? c.getGeneracion() : "");
            lbl_region.setText(c.getRegion() != null ? c.getRegion() : "");
            lbl_tipo.setText(c.getTipo() != null ? c.getTipo() : "");
            lbl_procesador.setText(c.getProcesador() != null ? c.getProcesador() : "");
            lbl_memoria.setText(c.getMemoria() != null ? c.getMemoria() : "");
            lbl_almacenamiento.setText(c.getAlmacenamiento() != null ? c.getAlmacenamiento() : "");
            lbl_fecha_lanzamiento.setText(c.getFechaLanzamiento() != null ? c.getFechaLanzamiento() : "");

            cargarImagenConsola(c.getImagen());
        } else {
            limpiarInfo();
        }
    }

    private void cargarImagenConsola(String nombreArchivo) {
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            Path ruta = Database.getImagesDir().resolve(nombreArchivo);
            File archivo = ruta.toFile();

            if (archivo.exists()) {
                imagen_consola.setImage(new Image(archivo.toURI().toString()));
            } else {
                Logger.warn("Imagen no encontrada: " + ruta);
                imagen_consola.setImage(null);
            }
        } else {
            imagen_consola.setImage(null);
        }
    }

    private void limpiarInfo() {
        lbl_nombre_consola.setText("");
        lbl_anio.setText("");
        lbl_fabricante.setText("");
        lbl_generacion.setText("");
        lbl_region.setText("");
        lbl_tipo.setText("");
        lbl_procesador.setText("");
        lbl_memoria.setText("");
        lbl_almacenamiento.setText("");
        lbl_fecha_lanzamiento.setText("");
        imagen_consola.setImage(null);
    }

    @FXML
    private void accion_primeraPagina(ActionEvent event) {
        if (paginaActual != 0) {
            paginaActual = 0;
            mostrarPagina(paginaActual);
        }
    }

    @FXML
    private void accion_paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) {
            paginaActual--;
            mostrarPagina(paginaActual);
        }
    }

    @FXML
    private void accion_paginaSiguiente(ActionEvent event) {
        if (paginaActual < totalPaginas - 1) {
            paginaActual++;
            mostrarPagina(paginaActual);
        }
    }

    @FXML
    private void accion_ultimaPagina(ActionEvent event) {
        if (paginaActual != totalPaginas - 1) {
            paginaActual = totalPaginas - 1;
            mostrarPagina(paginaActual);
        }
    }

    @FXML
    private void accion_agregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormularioConsolas.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario de Consolas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait(); // Esperamos a que se cierre

            // ✅ Volver a cargar las consolas después de cerrar
            cargarConsolas();

        } catch (IOException e) {
            Logger.error("Error al abrir el formulario de agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accion_editar(ActionEvent event) {
        Consola seleccionada = list_view_consolas.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormularioConsolas.fxml"));
                Parent root = loader.load();

                FormularioConsolasController controller = loader.getController();
                controller.setConsolaAEditar(seleccionada);

                Stage stage = new Stage();
                stage.setTitle("Editar Consola");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait(); // Esperamos a que se cierre

                // ✅ Volver a cargar las consolas después de cerrar
                cargarConsolas();

                // ✅ Buscar la consola nuevamente en la lista por ID
                Consola consolaActualizada = todasLasConsolas.stream()
                        .filter(c -> c.getId() == seleccionada.getId())
                        .findFirst()
                        .orElse(null);

                if (consolaActualizada != null) {
                    list_view_consolas.getSelectionModel().select(consolaActualizada);
                    mostrarInfoConsolaSeleccionada(); // 👈 vuelve a cargar la info (incluye imagen)
                }

            } catch (IOException e) {
                Logger.error("Error al abrir el formulario de edición: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Edición no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona una consola de la lista para editar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void accion_eliminar(ActionEvent event) {
        Consola seleccionada = list_view_consolas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eliminar consola");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona una consola de la lista para eliminar.");
            alert.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar la consola '" + seleccionada.getNombre() + "'?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                boolean eliminada = ConsolaDAO.eliminarConsola(seleccionada.getId());

                if (eliminada) {
                    Logger.info("Consola eliminada correctamente.");
                    mostrarAlerta("Consola eliminada correctamente.");
                    cargarConsolas(); // Refrescar la lista
                    limpiarInfo();    // Limpiar detalles
                } else {
                    mostrarAlerta("No se pudo eliminar la consola.");
                }
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        mostrarAlerta(mensaje, Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
