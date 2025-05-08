// models/Inicio.java
package models;

public class Inicio {

    private int totalJuegos;
    private int totalLogros;
    private int totalConsolas;
    private int totalEventos;
    private int totalMetas;
    private int totalSeguidores;

    public Inicio(int totalJuegos, int totalLogros, int totalConsolas, int totalEventos, int totalMetas, int totalSeguidores) {
        this.totalJuegos = totalJuegos;
        this.totalLogros = totalLogros;
        this.totalConsolas = totalConsolas;
        this.totalEventos = totalEventos;
        this.totalMetas = totalMetas;
        this.totalSeguidores = totalSeguidores;
    }

    public int getTotalJuegos() {
        return totalJuegos;
    }

    public int getTotalLogros() {
        return totalLogros;
    }

    public int getTotalConsolas() {
        return totalConsolas;
    }

    public int getTotalEventos() {
        return totalEventos;
    }

    public int getTotalMetas() {
        return totalMetas;
    }

    public int getTotalSeguidores() {
        return totalSeguidores;
    }
}
