package models;

public class Logros {

    private int id;
    private String nombre;
    private String descripcion;
    private int horasEstimadas;
    private int anio;
    private String fechaInicio;
    private String fechaFin;
    private int intentos;
    private int creditos;
    private int puntuacion;
    private String fechaRegistro;

    private Juego juego;
    private Estado estado;
    private Dificultad dificultad;
    private Consola consola;

    public Logros() {
    }

    public Logros(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Logros(int id, String nombre, String descripcion, int horasEstimadas, int anio,
            String fechaInicio, String fechaFin, int intentos, int creditos,
            int puntuacion, String fechaRegistro, Juego juego, Estado estado,
            Dificultad dificultad, Consola consola) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horasEstimadas = horasEstimadas;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.intentos = intentos;
        this.creditos = creditos;
        this.puntuacion = puntuacion;
        this.fechaRegistro = fechaRegistro;
        this.juego = juego;
        this.estado = estado;
        this.dificultad = dificultad;
        this.consola = consola;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getHorasEstimadas() {
        return horasEstimadas;
    }

    public void setHorasEstimadas(int horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    public Consola getConsola() {
        return consola;
    }

    public void setConsola(Consola consola) {
        this.consola = consola;
    }

    @Override
    public String toString() {
        return nombre + " (" + juego.getNombre() + ")";
    }
}
