package main;

import config.AppLogger;
import controllers.JuegosController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("setup")); // Vista inicial
        stage.setScene(scene);
        stage.setTitle("Instalador - Diario de Videojuegos");
        stage.setResizable(false); // Solo el setup no es redimensionable
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void mostrarPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(JuegosController.class.getResource("/views/Juegos.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();

            // Ajustar pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.setScene(new Scene(root));
            stage.setTitle("Gestor de Juegos");
            stage.setResizable(true); // ✅ Mantener botón de maximizar activo
            stage.show();
        } catch (IOException e) {
            AppLogger.severe("Error al cargar la pantalla principal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
