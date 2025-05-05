package models;

public class Juego {
    private int id;
    private String nombre;

    public Juego() {}

    public Juego(String nombre) {
        this.nombre = nombre;
    }

    public Juego(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
