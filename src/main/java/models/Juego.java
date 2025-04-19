package models;

public class Juego {

    private int id;
    private String nombre;
    private int anio;
    private String consola;
    private String estado;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private int intentos;
    private int creditos;
    private int puntuacion;

    public Juego(int id, String nombre, int anio, String consola, String estado,
                 String descripcion, String fechaInicio, String fechaFin,
                 int intentos, int creditos, int puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.consola = consola;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.intentos = intentos;
        this.creditos = creditos;
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getAnio() { return anio; }
    public String getConsola() { return consola; }
    public String getEstado() { return estado; }
    public String getDescripcion() { return descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public int getIntentos() { return intentos; }
    public int getCreditos() { return creditos; }
    public int getPuntuacion() { return puntuacion; }
}
