package controllers;

import javafx.fxml.Initializable;
import models.MetasEspecificas;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class FormMetasEspecificasController implements Initializable {

    private MetasEspecificas metaGuardada;
    private Runnable onGuardarCallback;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }

    public void setMeta(MetasEspecificas seleccionada) {
        // Aquí se debe cargar la información en los campos del formulario
        // metaGuardada = seleccionada; ← puedes hacerlo si lo necesitas
    }

    public MetasEspecificas getMetaGuardada() {
        return metaGuardada;
    }

    public void setMetaGuardada(MetasEspecificas meta) {
        this.metaGuardada = meta;
    }

    public void setOnGuardarCallback(Runnable callback) {
        this.onGuardarCallback = callback;
    }

    // Este método deberías llamarlo cuando guardes la meta
    private void guardarYCerrar() {
        // lógica para guardar la meta...
        if (onGuardarCallback != null) {
            onGuardarCallback.run();
        }
    }
}
