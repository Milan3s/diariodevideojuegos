package main;

import config.AppLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Scene scene = new Scene(loadFXML("setup")); // Pantalla inicial
        stage.setScene(scene);
        stage.setTitle("Instalador - Diario de Videojuegos");
        stage.setResizable(false); // El setup no se puede redimensionar
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        primaryStage.getScene().setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void mostrarPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/inicio.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();

            // Ajustar ventana a pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.setScene(new Scene(root));
            stage.setTitle("Gestor de Juegos");
            stage.setResizable(true);
            stage.show();

            // Cerrar la ventana del setup
            primaryStage.close();
        } catch (IOException e) {
            AppLogger.severe("Error al cargar la pantalla principal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
