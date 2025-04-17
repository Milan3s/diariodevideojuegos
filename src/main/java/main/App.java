package main;

import config.Logger; // Asegúrate de que la clase Logger esté en el paquete correcto
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Cargar la escena principal
            //scene = new Scene(loadFXML("instalacion")); // pantalla de instalación
            scene = new Scene(loadFXML("principal")); // pantalla para la aplicación principal
            stage.setScene(scene);
            stage.show();
            Logger.info("Aplicación iniciada con éxito.");
        } catch (IOException e) {
            Logger.error("Error al iniciar la aplicación: " + e.getMessage());
        }
    }

    static void setRoot(String fxml) throws IOException {
        try {
            scene.setRoot(loadFXML(fxml));
            Logger.info("Pantalla cambiada a: " + fxml);
        } catch (IOException e) {
            Logger.error("Error al cargar el archivo FXML: " + fxml + " - " + e.getMessage());
            throw e; // Rethrow para que se maneje adecuadamente en la aplicación.
        }
    }

    // Método para cargar el archivo FXML
    private static Parent loadFXML(String fxml) throws IOException {
        // Usamos la ruta correcta en el directorio resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/main/" + fxml + ".fxml"));
        
        // Verificar si el archivo FXML existe
        if (fxmlLoader.getLocation() == null) {
            Logger.error("FXML archivo no encontrado: " + fxml);
            throw new IOException("FXML archivo no encontrado: " + fxml);
        }
        
        Logger.info("Archivo FXML cargado correctamente: " + fxml);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        Logger.info("Aplicación iniciada.");
        launch();
    }

}
