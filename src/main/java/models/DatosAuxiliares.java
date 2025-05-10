package models;

/**
 * Modelo genérico para datos auxiliares como estados, dificultades, etc.
 */
public class DatosAuxiliares {

    private int id;
    private String nombre;

    public DatosAuxiliares() {
    }

    public DatosAuxiliares(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    // Para mostrar el nombre en listas (ListView, ComboBox, etc.)
    @Override
    public String toString() {
        return nombre;
    }
}
