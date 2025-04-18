package models;

public class Consola {

    private int id;
    private String nombre;
    private String anio; // <-- cambiado de Integer a String
    private String fabricante;
    private String generacion;
    private String region;
    private String tipo;
    private String procesador;
    private String memoria;
    private String almacenamiento;
    private String fechaLanzamiento;
    private String imagen; // nombre del archivo de imagen

    public Consola(int id, String nombre, String anio, String fabricante, String generacion,
                   String region, String tipo, String procesador, String memoria,
                   String almacenamiento, String fechaLanzamiento, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.fabricante = fabricante;
        this.generacion = generacion;
        this.region = region;
        this.tipo = tipo;
        this.procesador = procesador;
        this.memoria = memoria;
        this.almacenamiento = almacenamiento;
        this.fechaLanzamiento = fechaLanzamiento;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAnio() { // <-- cambiado a String
        return anio;
    }

    public String getFabricante() {
        return fabricante;
    }

    public String getGeneracion() {
        return generacion;
    }

    public String getRegion() {
        return region;
    }

    public String getTipo() {
        return tipo;
    }

    public String getProcesador() {
        return procesador;
    }

    public String getMemoria() {
        return memoria;
    }

    public String getAlmacenamiento() {
        return almacenamiento;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return nombre != null ? nombre : "(Sin nombre)";
    }
}
