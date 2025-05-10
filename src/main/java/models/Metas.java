package models;

import java.time.LocalDate;

/**
 * Modelo base para las metas del sistema. Puede representar metas generales
 * como: - Twitch (con meta y actual) - Específica (con juegos, consola,
 * fabricante) - Mejora del canal - Extensible
 */
public class Metas {

    private Integer id;
    private String tipo; // twitch, especifica, mejora, extensible
    private String descripcion;

    // Comunes en twitch y seguidores
    private Integer meta;
    private Integer actual;

    // Solo para metas específicas
    private Integer juegosObjetivo;
    private Integer juegosCompletados;
    private String fabricante;
    private Integer idConsola;

    // Fechas comunes
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Extensible
    private String motivo;
    private LocalDate fechaEvento;

    // Constructor base vacío
    public Metas() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMeta() {
        return meta;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public Integer getJuegosObjetivo() {
        return juegosObjetivo;
    }

    public void setJuegosObjetivo(Integer juegosObjetivo) {
        this.juegosObjetivo = juegosObjetivo;
    }

    public Integer getJuegosCompletados() {
        return juegosCompletados;
    }

    public void setJuegosCompletados(Integer juegosCompletados) {
        this.juegosCompletados = juegosCompletados;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Integer getIdConsola() {
        return idConsola;
    }

    public void setIdConsola(Integer idConsola) {
        this.idConsola = idConsola;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }
}
