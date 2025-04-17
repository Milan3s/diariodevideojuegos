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
            lbl_nombre_consola.setText(c.getNombre());
            lbl_anio.setText(c.getAnio() != null ? String.valueOf(c.getAnio()) : "");
            lbl_fabricante.setText(c.getFabricante());
            lbl_generacion.setText(c.getGeneracion());
            lbl_region.setText(c.getRegion());
            lbl_tipo.setText(c.getTipo());
            lbl_procesador.setText(c.getProcesador());
            lbl_memoria.setText(c.getMemoria());
            lbl_almacenamiento.setText(c.getAlmacenamiento());
            lbl_fecha_lanzamiento.setText(c.getFechaLanzamiento());

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

            // Crear nueva ventana (modal)
            Stage stage = new Stage();
            stage.setTitle("Formulario de Consolas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Hace que sea modal
            stage.setResizable(false);
            stage.showAndWait(); // Espera a que se cierre para volver

            Logger.info("Ventana modal 'FormularioConsolas.fxml' abierta correctamente");

        } catch (IOException e) {
            Logger.error("Error al abrir el formulario como ventana modal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accion_editar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormularioConsolas.fxml"));
            Parent root = loader.load();

            // Crear nueva ventana (modal)
            Stage stage = new Stage();
            stage.setTitle("Formulario de Consolas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Hace que sea modal
            stage.setResizable(false);
            stage.showAndWait(); // Espera a que se cierre para volver

            Logger.info("Ventana modal 'FormularioConsolas.fxml' abierta correctamente");

        } catch (IOException e) {
            Logger.error("Error al abrir el formulario como ventana modal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accion_eliminar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cruds/FormularioConsolas.fxml"));
            Parent root = loader.load();

            // Crear nueva ventana (modal)
            Stage stage = new Stage();
            stage.setTitle("Formulario de Consolas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Hace que sea modal
            stage.setResizable(false);
            stage.showAndWait(); // Espera a que se cierre para volver

            Logger.info("Ventana modal 'FormularioConsolas.fxml' abierta correctamente");

        } catch (IOException e) {
            Logger.error("Error al abrir el formulario como ventana modal: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
