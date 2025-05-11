package models;

import java.time.LocalDate;

public class MetasEspecificas {

    private Integer id;
    private String descripcion;
    private Integer juegosObjetivo;
    private Integer juegosCompletados;
    private boolean cumplida;
    private String fabricante;
    private Integer idConsola;
    private String nombreConsola;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaRegistro;

    public MetasEspecificas() {}

    public MetasEspecificas(Integer id, String descripcion, Integer juegosObjetivo, Integer juegosCompletados,
                            boolean cumplida, String fabricante, Integer idConsola, String nombreConsola,
                            LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaRegistro) {
        this.id = id;
        this.descripcion = descripcion;
        this.juegosObjetivo = juegosObjetivo;
        this.juegosCompletados = juegosCompletados;
        this.cumplida = cumplida;
        this.fabricante = fabricante;
        this.idConsola = idConsola;
        this.nombreConsola = nombreConsola;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean isCumplida() {
        return cumplida;
    }

    public void setCumplida(boolean cumplida) {
        this.cumplida = cumplida;
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

    public String getNombreConsola() {
        return nombreConsola;
    }

    public void setNombreConsola(String nombreConsola) {
        this.nombreConsola = nombreConsola;
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

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getConsolaId() {
        return idConsola != null ? idConsola : -1;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
