package models;

public class Juego {

    private int id;
    private String nombre;
    private String descripcion;
    private String desarrollador;
    private String editor;
    private String genero;
    private String modoJuego;
    private String fechaLanzamiento;
    private Estado estado;
    private boolean esRecomendado;
    private String imagen;
    private Consola consola;
    private String nombreConsola; // Nombre formateado: "Juego (Consola)"
    private String video;
    private String overlay;

    // Constructores
    public Juego() {
    }

    public Juego(String nombre) {
        this.nombre = nombre;
    }

    public Juego(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
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

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getModoJuego() {
        return modoJuego;
    }

    public void setModoJuego(String modoJuego) {
        this.modoJuego = modoJuego;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isEsRecomendado() {
        return esRecomendado;
    }

    public void setEsRecomendado(boolean esRecomendado) {
        this.esRecomendado = esRecomendado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Consola getConsola() {
        return consola;
    }

    public void setConsola(Consola consola) {
        this.consola = consola;
    }

    public String getNombreConsola() {
        return nombreConsola;
    }

    public void setNombreConsola(String nombreConsola) {
        this.nombreConsola = nombreConsola;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getOverlay() {
        return overlay;
    }

    public void setOverlay(String overlay) {
        this.overlay = overlay;
    }

    @Override
    public String toString() {
        String consolaStr = (consola != null && consola.getNombre() != null) ? consola.getNombre() : "Sin consola";
        return (nombre != null ? nombre : "Juego sin nombre") + " (" + consolaStr + ")";
    }
}
