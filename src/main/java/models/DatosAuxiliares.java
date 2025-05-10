package models;

/**
 * Modelo genérico para datos auxiliares como estados, dificultades, etc.
 */
public class DatosAuxiliares {

    private int id;
    private String nombre;
    private String tipo;         // Puede representar valores como 'juego', 'logro', etc. (solo para algunas tablas)
    private String tipoVisual;   // Representa el tipo visual seleccionado (ej. "Estados", "Dificultades")

    public DatosAuxiliares() {
    }

    public DatosAuxiliares(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public DatosAuxiliares(int id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public DatosAuxiliares(int id, String nombre, String tipo, String tipoVisual) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.tipoVisual = tipoVisual;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoVisual() {
        return tipoVisual;
    }

    public void setTipoVisual(String tipoVisual) {
        this.tipoVisual = tipoVisual;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nombre);
        if (tipo != null) sb.append(" (").append(tipo).append(")");
        if (tipoVisual != null) sb.append(" | ").append(tipoVisual);
        return sb.toString();
    }
}
