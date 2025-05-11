package models;

import java.time.LocalDate;

public class MetasTwitch {

    private int idMeta;
    private String descripcion;
    private int meta;
    private int actual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaRegistro;

    public MetasTwitch() {
    }

    public MetasTwitch(int idMeta, String descripcion, int meta, int actual,
                       LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaRegistro) {
        this.idMeta = idMeta;
        this.descripcion = descripcion;
        this.meta = meta;
        this.actual = actual;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(int idMeta) {
        this.idMeta = idMeta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
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

    @Override
    public String toString() {
        return descripcion + " (" + fechaInicio + ")";
    }
}
