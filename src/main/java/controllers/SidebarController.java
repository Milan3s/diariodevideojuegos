package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private VBox sidebar;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnJuegos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //handleInicio(); 
        handleJuegos();
    }

    @FXML
    private void handleInicio() {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/views/inicio.fxml"));
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleJuegos() {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/views/juegos.fxml"));
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
