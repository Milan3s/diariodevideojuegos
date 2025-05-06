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
    private Estado estado;  // Se cambió a Estado
    private boolean esRecomendado;
    private String imagen;
    private Consola consola; // Se cambió a Consola
    private String nombreConsola; // Nuevo campo para almacenar el nombre formateado
    
    // Nuevos campos para video y overlay
    private String video; // Ruta del video
    private String overlay; // Ruta del overlay

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

    // Nuevo getter y setter para el campo nombreConsola
    public String getNombreConsola() {
        return nombreConsola;
    }

    public void setNombreConsola(String nombreConsola) {
        this.nombreConsola = nombreConsola;
    }

    // Nuevos getters y setters para video y overlay
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
        // Concatenamos el nombre del juego con el nombre de la consola entre paréntesis
        return nombre + " (" + consola.getNombre() + ")";
    }
}
