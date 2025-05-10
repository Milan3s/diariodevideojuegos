package models;

/**
 * Modelo genérico para datos auxiliares como estados, dificultades, etc.
 */
public class DatosAuxiliares {

    private int id;
    private String nombre;
    private String tipo; // <--- nuevo campo opcional: puede ser 'juego', 'logro', etc.

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

    // Para mostrar el nombre en listas
    @Override
    public String toString() {
        return nombre + (tipo != null ? " (" + tipo + ")" : "");
    }
}
