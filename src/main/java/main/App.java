package main;

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
        // Cargar la escena principal
        scene = new Scene(loadFXML("principal")); // Añadimos la ruta relativa correcta
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Método para cargar el archivo FXML
    private static Parent loadFXML(String fxml) throws IOException {
        // Usamos la ruta correcta en el directorio resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/main/" + fxml + ".fxml"));
        
        // Verificar si el archivo FXML existe
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML archivo no encontrado: " + fxml);
        }
        
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
