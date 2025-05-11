package models;

public class ConfiguracionAuxiliar {

    private int idConfiguracion;
    private String nombreVisual;
    private String nombreTabla;
    private String columnaId;
    private String columnaNombre;

    // Constructor vacío requerido por JavaFX y frameworks de serialización
    public ConfiguracionAuxiliar() {
    }

    // Constructor completo útil para inicializaciones directas
    public ConfiguracionAuxiliar(int idConfiguracion, String nombreVisual, String nombreTabla, String columnaId, String columnaNombre) {
        this.idConfiguracion = idConfiguracion;
        this.nombreVisual = nombreVisual;
        this.nombreTabla = nombreTabla;
        this.columnaId = columnaId;
        this.columnaNombre = columnaNombre;
    }

    public int getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(int idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public String getNombreVisual() {
        return nombreVisual;
    }

    public void setNombreVisual(String nombreVisual) {
        this.nombreVisual = nombreVisual;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public String getColumnaId() {
        return columnaId;
    }

    public void setColumnaId(String columnaId) {
        this.columnaId = columnaId;
    }

    public String getColumnaNombre() {
        return columnaNombre;
    }

    public void setColumnaNombre(String columnaNombre) {
        this.columnaNombre = columnaNombre;
    }

    @Override
    public String toString() {
        return nombreVisual;
    }
}
