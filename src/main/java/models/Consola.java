package models;

public class Consola {
    private int id;
    private String nombre;
    private String abreviatura;

    // Constructor
    public Consola(int id, String nombre, String abreviatura) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    // Modificamos el toString para devolver el nombre completo
    @Override
    public String toString() {
        return nombre;  // Devolvemos el nombre completo de la consola
    }

    // Método adicional para obtener la abreviatura
    public String getAbreviaturaCompleta() {
        return abreviatura;  // Devuelve la abreviatura de la consola
    }
}
