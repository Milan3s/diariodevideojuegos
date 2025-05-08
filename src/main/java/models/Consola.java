package models;

public class Consola {

    private int id;
    private String nombre;
    private String abreviatura;
    private Integer anio;
    private String fabricante;
    private String generacion;
    private String region;
    private String tipo;
    private String procesador;
    private String memoria;
    private String almacenamiento;
    private String fechaLanzamiento;
    private String imagen;
    private Estado estado;

    // Constructor completo
    public Consola(int id, String nombre, String abreviatura, Integer anio, String fabricante,
            String generacion, String region, String tipo, String procesador,
            String memoria, String almacenamiento, String fechaLanzamiento,
            String imagen, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
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
        this.estado = estado;
    }

    // Constructor mínimo
    public Consola(int id, String nombre, String abreviatura) {
        this(id, nombre, abreviatura, null, null, null, null, null, null, null, null, null, null, null);
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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public String getMemoria() {
        return memoria;
    }

    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    public String getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(String almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getAbreviaturaCompleta() {
        return abreviatura;
    }
}
